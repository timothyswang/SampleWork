<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search Results</title>

		<link rel="stylesheet" type="text/css" href="SearchResults.css">
		
		<style>
		#LoginIcon{
			position: absolute;
			left: 80%;
			top: 5%;
		}

		</style>
		
		
		<script>
		
		function vali(){
			if (document.getElementById("searchbar").value === ""){
				 return false;
			 }

			 
			 localStorage.setItem("errorMessage", "");
			 
			 var searchbar = document.getElementById("searchbar").value;
			 var searchparameters = "";
			 var myArray = document.getElementsByName("searchparameters");

			 for (var i = 0, length = myArray.length; i < length; i++)
			 {
			  if (myArray[i].checked)
			  {
			   searchparameters = myArray[i].value;
			   break;
			  }
			 }
			 
			 localStorage.setItem("searchitem", searchbar);
			 localStorage.setItem("searchparitem", searchparameters);
			 return true;
 		}
		
		function Validate(){
			
			var xhttp = new XMLHttpRequest();
			xhttp.open("GET", "SearchResults", false);
			xhttp.send();

			console.log("1 " + xhttp.responseText);
			if (xhttp.responseText.trim().length > 0){ //There is a username
				console.log("2 " + xhttp.responseText);
				//console.log(xhttp.responseText);
				document.getElementById("LoginIcon").innerHTML = "<a href= \"Profile.jsp\" ><img src=./image/UserImage.png width=\"80\" height=\"80\"><\a>";
			}
			
			var searchbar = localStorage.getItem("searchitem");
			var searchbar2 = searchbar;
			searchbar = searchbar.replace(" ","+");
			var searchparameters = localStorage.getItem("searchparitem");
			
			var results = document.getElementById("results");
			results.innerHTML = "Results for \"" + searchbar2 + "\"";
				
			var xhttp = new XMLHttpRequest();
			var src = "";
			if (searchparameters === "intitle"){
				src = "https://www.googleapis.com/books/v1/volumes?q=+" + searchparameters + ":" + searchbar + "&key=AIzaSyAeMNeGQUrhz7RYOqKUv0qXHq9fB0xb8NQ";
			}
			else if (searchparameters === "inauthor"){
				src = "https://www.googleapis.com/books/v1/volumes?q=+" + searchparameters + ":" + searchbar + "&key=AIzaSyAeMNeGQUrhz7RYOqKUv0qXHq9fB0xb8NQ";
			}
			else if (searchparameters === "isbn"){
				src = "https://www.googleapis.com/books/v1/volumes?q=+" + searchparameters + ":"  + searchbar + "&key=AIzaSyAeMNeGQUrhz7RYOqKUv0qXHq9fB0xb8NQ";
			}
			else if (searchparameters === "inpublisher"){
				src = "https://www.googleapis.com/books/v1/volumes?q=+" + searchparameters + ":" + searchbar + "&key=AIzaSyAeMNeGQUrhz7RYOqKUv0qXHq9fB0xb8NQ";
			}
			else{
				src = "https://www.googleapis.com/books/v1/volumes?q=" + searchbar + "&key=AIzaSyAeMNeGQUrhz7RYOqKUv0qXHq9fB0xb8NQ";
			}
			
			xhttp.open("GET", src, false);
			xhttp.send();
			var input = JSON.parse(xhttp.responseText);
			
			if (input.totalItems == 0){
				localStorage.setItem("errorMessage", "true");
				window.location.replace("HomePage.jsp");
			}
			
			var table = document.getElementById("mytable");
			var i;
			var j;
			if (input.totalItems < 10){
				j = input.totalItems;
			}
			else{
				j = 10;
			}
			
			for(i = 0; i<j; i++){
				
				//set local storage to stuff
				var bookCover= "";
				if(input.items[i].volumeInfo.imageLinks.smallThumbnail == undefined){
					bookCover = "No book cover is provided.";
				}
				else{
					bookCover = input.items[i].volumeInfo.imageLinks.smallThumbnail;
				}
				localStorage.setItem("bookCover" + i, bookCover);	
				
				var bookTitle = "";
				if (input.items[i].volumeInfo.title == undefined){
					bookTitle = "No book title is provided.";
				}
				else{
					bookTitle = input.items[i].volumeInfo.title
				}
				localStorage.setItem("bookTitle" + i, bookTitle);

				
				var bookAuthor = "";
				if (input.items[i].volumeInfo.authors == undefined){
					bookAuthor = "No book author is provided."
				}
				else{
					bookAuthor = input.items[i].volumeInfo.authors[0];
				}
				localStorage.setItem("bookAuthor" + i, bookAuthor);	

				
				var bookPublisher = "";
				if (input.items[i].volumeInfo.publisher == undefined){
					bookPublisher = "No book publisher is provided.";
				}
				else{
					bookPublisher = input.items[i].volumeInfo.publisher;
				}
				localStorage.setItem("bookPublisher" + i, bookPublisher);

				
				var bookDate = "";
				if (input.items[i].volumeInfo.publishedDate == undefined){
					bookDate = "No book date is provided."
				}
				else{
					bookDate = input.items[i].volumeInfo.publishedDate;
				}
				localStorage.setItem("bookDate" + i, bookDate);

				
				var bookISBN = "";
				if (input.items[i].volumeInfo.industryIdentifiers == undefined){
					bookISBN = "No book ISBN is provided.";
				}
				else{
					bookISBN = input.items[i].volumeInfo.industryIdentifiers[0].identifier;
				}
				localStorage.setItem("bookISBN" + i, bookISBN);

				
				var bookSummary = ""
				if (input.items[i].volumeInfo.description == undefined){
					bookSummary = "No book summary is provided.";
				}
				else{
					bookSummary = input.items[i].volumeInfo.description;
				}
				localStorage.setItem("bookSummary" + i, bookSummary);

				
				var bookRating = ""
				if (input.items[i].volumeInfo.averageRating == undefined){
					bookRating = "No book rating";
				}
				else{
					bookRating = input.items[i].volumeInfo.averageRating;
				}
				localStorage.setItem("bookRating" + i, bookRating);
				
				var row = table.insertRow(i);
				
				var cell1 = row.insertCell(0);
				var cell2 = row.insertCell(1);
				
				var detailsNumber = "Details.jsp?itemNumber=" + i;
				
				var srcInput = input.items[i].volumeInfo.imageLinks.smallThumbnail;
				
				cell1.innerHTML = "<a href = "+detailsNumber+"><img src = "+srcInput+"></a>";
				cell2.innerHTML = "<h1>"+bookTitle+"</h1>" + "<h2>"+bookAuthor+"<h2>" + "<h3><b>Summary:</b> " + bookSummary+"<h3>";
				
			}
				
		}
			
				
		</script>

</head>
<body onload="Validate()">
		<div id="worm">
        <a href = HomePage.jsp><img id="content" src="./image/bookworm.png" width=125 height=100></a>
        </div>
        <div id="LoginIcon"></div>
        <br>
        <form name= "myform" method="GET" action="SearchResults.jsp" onsubmit="return vali()">
            <input type="text" name="searchbar" id="searchbar" placeholder="What book is on your mind?">
            <br>
            <div id= namebutton>
            <input type="radio" id="name" name="searchparameters" value="intitle">
             <label for="name">Name</label>
             </div>
             <br>
             <div id="isbnbutton">
             <input type="radio" id="isbn" name="searchparameters" value="isbn">
             <label for="isbn">ISBN</label>
             </div>
             <br>
             <div id="authorbutton">
             <input type="radio" id="author" name="searchparameters" value="inauthor">
             <label for="author">Author</label>
             </div>
             <br>
             <div id="publisherbutton">
             <input type="radio" id="publisher" name="searchparameters" value="inpublisher">
             <label for="publisher">Publisher</label>
             </div>
             <br>
             <input type="image" name="submit" src="./image/magnifying_glass.png" id="submit" alt="Submit" width=25 height=15>
        </form>
        
        <hr color="Gainsboro" size=".5">
        
        <h2 id="results"></h2>
        <br>
        
        <table id="mytable"></table>
        
        
        
</body>
</html>
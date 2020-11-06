<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Details</title>

<link rel="stylesheet" type="text/css" href="Details.css">

<style>
#LoginIcon{
	position: absolute;
	left: 80%;
	top: 5%;
}

#errormsg{
	font-family: myFont;
	color: red;
	margin-left: 5%;
}

</style>

</head>
<body onload="validate()">

	<script>
	
	function Favorite(BookCover, Title, Author, Url, BookISBN, Add, Summary){
		//Add date created in java servlet.
		//Add = 1 add to favorite.
		//Add = 2, remove from favorite
		
		console.log(BookCover);
		BookCover = "'" + BookCover + "'";
		console.log(BookCover);
		
		BookCover = encodeURIComponent(BookCover);
		
		var Add = localStorage.getItem("add");
		console.log(Add);
		var flag = false;
		if (Add === "1"){
			console.log("1");
			var xhttp3 = new XMLHttpRequest();
			xhttp3.onreadystatechange = function () {
				//Check if there is an error message.
				if (flag === false){
					console.log(xhttp3.responseText);
					
					
					<%
						String n=(String)session.getAttribute("username");
						if (n == null || n.contentEquals("")){ 
					%>
						document.getElementById("errormsg").innerHTML = "Not logged in.";
						flag = true;
					<%
						} 
						else {
					%>
						document.getElementById("Favorite").innerHTML = "Remove";
						localStorage.setItem("add", 2);
						console.log("Change to remove button");
						flag = true;
					<%
						}
					%>

				}
			}
			console.log("Url1: " + Url);
			console.log("BookISBN1: " + BookISBN);
			xhttp3.open("GET", "Details3?Url=" + Url + "&BookISBN=" + BookISBN + "&BookCover=" + BookCover + "&Title=" + Title + "&Author=" + Author + "&Summary=" + Summary, true);
			xhttp3.send();
			
			
		}
		else if (Add === "2"){
			console.log("2");
			var xhttp4 = new XMLHttpRequest();
			
			xhttp4.onreadystatechange = function () {
				if (flag === false){
				<%
					String m=(String)session.getAttribute("username");
					if (m == null || m.contentEquals("")){ 
				%>
					document.getElementById("errormsg").innerHTML = "Not logged in.";
					flag = true;
				<%
					} 
					else {
				%>
					document.getElementById("Favorite").innerHTML = "Favorite";
					localStorage.setItem("add", 1);
					console.log("Change to remove button");
					flag = true;
				<%
					}
				%>
				}
			}
			
			
			xhttp4.open("GET", "Details4?BookISBN=" + BookISBN, true);
			xhttp4.send();
			
		}
	}
	
	function getURLParameter(name) {
		  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
	}
	
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
	
	function validate(){
		
		var xhttp = new XMLHttpRequest();
		xhttp.open("GET", "Details", false);
		xhttp.send();

		console.log("1 " + xhttp.responseText);
		if (xhttp.responseText.trim().length > 0){ //There is a username
			console.log("2 " + xhttp.responseText);
			//console.log(xhttp.responseText);
			document.getElementById("LoginIcon").innerHTML = "<a href= \"Profile.jsp\" ><img src=./image/UserImage.png width=\"80\" height=\"80\"><\a>";
		}
		
		var table = document.getElementById("mytable");
		var row = table.insertRow(0);
		
		var cell1 = row.insertCell(0);
		var cell2 = row.insertCell(1);
		
		var item = getURLParameter('itemNumber');
		
		var bookCover = "bookCover" + item;
		var bookTitle = "bookTitle" + item;		
		var bookAuthor = "bookAuthor" + item;
		var bookPublisher = "bookPublisher" + item;
		var bookDate = "bookDate" + item;
		var bookISBN = "bookISBN" + item;
		var bookSummary = "bookSummary" + item;
		var bookRating = "bookRating" + item;
		
		var starRating = localStorage.getItem(bookRating);
		
		var imagesrc = "";
		
		if (starRating === "No book rating"){
			imagesrc = "<h7><b>Rating:</b> No book rating </h7>"
		}
		else{
			starRating = Math.floor(starRating);
			if (starRating === 0){
				imagesrc = "<h7><b>Rating:</b><img src=./image/zeroStar.png width=\"110\" height=\"20\"></h7>";
			}
			if (starRating === 1){
				imagesrc = "<h7><b>Rating:</b><img src=./image/oneStar.png width=\"110\" height=\"20\"></h7>";
			}
			if (starRating === 2){
				imagesrc = "<h7><b>Rating:</b><img src=./image/twoStar.png width=\"110\" height=\"20\"></h7>";
			}
			if (starRating === 3){
				imagesrc = "<h7><b>Rating:</b><img src=./image/threeStar.png width=\"110\" height=\"20\"></h7>";
			}
			if (starRating === 4){
				imagesrc = "<h7><b>Rating:</b><img src=./image/fourStar.png width=\"110\" height=\"20\"></h7>";
			}
			if (starRating === 5){
				imagesrc = "<h7><b>Rating:</b><img src=./image/fiveStar.png width=\"110\" height=\"20\"></h7>";
			}
		}
		
		var Url = "Details.jsp?itemNumber=" + item
		
		var onclick = "Favorite('" + localStorage.getItem(bookCover)+ "','" + localStorage.getItem(bookTitle) + "','" + localStorage.getItem(bookAuthor) + "','" + Url + "','" + localStorage.getItem(bookISBN)+ "','" + localStorage.getItem("add") + "','" +localStorage.getItem(bookSummary)+ "')";
		
		//console.log(onclick);
		
		cell1.innerHTML = "<a href = SearchResults.jsp><img src = "+localStorage.getItem(bookCover)+"></a>" + "<button id=\"Favorite\" onclick=\"" + onclick +"\"></button>";
		
		cell2.innerHTML = "<h1>"+localStorage.getItem(bookTitle)+"</h1>" + "<h2><b>Author: </b>" +localStorage.getItem(bookAuthor)+"</h2>" + "<h3><b><i>Publisher:</i></b> " + localStorage.getItem(bookPublisher)+"</h3>" + "<h4><b><i>Published Date:</i></b> " + localStorage.getItem(bookDate)+"</h4>" + "<h5><b><i>ISBN:</i></b> " + localStorage.getItem(bookISBN)+"</h5>" + "<h6><b>Summary:</b> " + localStorage.getItem(bookSummary)+"</h6>" + imagesrc;
		
        var	tempBookISBN = localStorage.getItem(bookISBN);
		
		
		//This just checks if the book is already in the username's favorite books list.
		var xhttp2 = new XMLHttpRequest();
		xhttp2.open("GET", "Details2?BookISBN=" + tempBookISBN, false);
		xhttp2.send();
		
		if (xhttp2.responseText.trim().length > 0){ //There is a username and this book is a favorite.
			console.log("2 " + xhttp.responseText);
			//console.log(xhttp.responseText);
			document.getElementById("Favorite").innerHTML = "Remove";
			localStorage.setItem("add", 2); //If Add = 2, remove from favorite 
		}
		else{ //Either no user or not a favorite book.
			document.getElementById("Favorite").innerHTML = "Favorite";
			localStorage.setItem("add", 1); //If Add = 1, add to favorite
		}
		
	}
	</script>

	<a href = HomePage.jsp><img id="content" src="./image/bookworm.png" width=125 height=100></a>
    <br>
    <div id="LoginIcon"></div>
    <form name= "myform" method="GET" action="SearchResults.jsp" onsubmit="return vali()">
            <input type="text" name="searchbar" id="searchbar" placeholder="What book is on your mind?">
            <br>
            <div id="namebutton">
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
        
        
        <hr color="Gainsboro">
        
        <table id="mytable"></table>
        
       <hr color="Gainsboro" width = 90%>
       
       <div id="errormsg"></div>
        

</body>
</html>
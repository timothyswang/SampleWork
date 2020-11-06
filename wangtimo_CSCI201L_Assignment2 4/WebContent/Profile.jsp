<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Profile</title>

<link rel="stylesheet" type="text/css" href="Profile.css">

<style>
#LoginIcon{
	position: absolute;
	left: 80%;
	top: 5%;
}

#results{
	margin-left: 40%;
}

</style>


</head>
<body onload="Validate()">

<script>
function List(){
	
	//Adding rows to the page:
	var xhttp2 = new XMLHttpRequest();
	
	xhttp2.open("POST", "Profile2", false);
	xhttp2.send();
	 var str = xhttp2.responseText.trim();
	
	 
	 
	 var res = str.split("~");
	 
	 
	 var a;
	 for (a=0; a < res.length - 1 ; a++){
		 console.log(a + " " + res[a]);
	 }
	 
	 var table = document.getElementById("mytable");
	 
	 var i = 0
	 var j = res.length - 1;
	 var k = 0;
	 
 
	 while (i < j){
		 
		 var row = table.insertRow(k);
		 
		 var cell1 = row.insertCell(0);
		 var cell2 = row.insertCell(1);
		 
		 var detailsNumber = res[i];
		 i = i + 1;
		 var srcInput = res[i];
		 i = i + 1;
		 var bookTitle = res[i];
		 i = i + 1;
		 var bookAuthor = res[i];
		 i = i + 1;
		 var bookSummary = res[i];
		 i = i + 1;
		 var BookISBN = res[i];
		 row.id = "row-" + res[i];
		 i = i + 1;
		 
		 var onclick = "Remove('" + BookISBN + "')";
		 
		 cell1.innerHTML = "<a href = "+detailsNumber+"><img src = "+srcInput+"></a>";
		 cell2.innerHTML = "<h1>"+bookTitle+"</h1>" + "<h2>"+bookAuthor+"<h2>" + "<h3><b>Summary:</b> " + bookSummary+"<h3>" + "<button id=\"Favorite\" onclick=\"" + onclick +"\">Remove</button>";
		 
		 k = k + 1; // Do at end of each loop
	 }
		 
	
	
}



function Validate(){
	//Greeting:
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", "Profile", false);
	xhttp.send();
	document.getElementById("results").innerHTML = xhttp.responseText.trim() + "'s favorites:"
	
	List();
	
}

function Remove(BookISBN){
	var xhttp4 = new XMLHttpRequest();
	xhttp4.open("GET", "Details4?BookISBN=" + BookISBN, true);
	xhttp4.send();
	
	var xhttp2 = new XMLHttpRequest();
	
	xhttp2.open("POST", "Profile2", true);
	xhttp2.send();
	 var str = this.responseText;
	 
	 var tableRow = document.getElementById("row-" + BookISBN);
	 tableRow.parentNode.removeChild(tableRow);
}


</script>

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

</script>



		<div id="worm">
        <a href = HomePage.jsp><img id="content" src="./image/bookworm.png" width=125 height=100></a>
        </div>
        <div ><a href= "Profile.jsp" ><img id="LoginIcon" src=./image/UserImage.png width="80" height="80"></a></div>
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
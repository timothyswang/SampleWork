<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>HomePage</title>
	<meta charset="UTF-8">
	
	<link rel="stylesheet" type="text/css" href="HomePage.css">
	
	<style>
	
	#Left{
		position: absolute;
		font-family: myFont;
		top: 10%;
		left : 80%;
		color: white;
	}
	
	#Right{
		position: absolute;
		font-family: myFont;
		top: 10%;
		left : 87%;
		color: white;
	}
	
	
	</style>
	
	<script>
	
	function getURLParameter(name) {
		  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
	}
	
	function onload(){
		
		var item = getURLParameter('signout');
		if (item === "true"){
			localStorage.setItem("firstSession", "true");
		}
		 
		if (localStorage.getItem("errorMessage") === "true"){
			document.getElementById("errorMessage").innerHTML = "Search resulted in no results. Please try again.";
		}
		
		if(localStorage.getItem("firstSession") === "false"){ //Not first session
			
			var xhttp = new XMLHttpRequest();
			xhttp.open("GET", "HomePage?firstSession=false", false);
			xhttp.send();
			
			if (xhttp.responseText.trim().length > 0){ //There is a username
				console.log(xhttp.responseText);
				document.getElementById("LoginFeature").innerHTML = "<div id=\"Left\"><a href = Profile.jsp>Profile</a></div>" + "<div id=\"Right\"><a href = HomePage.jsp?signout=true>Sign Out</a></div>";
			}
			else{ //There isn't a username
				console.log("There isn't a username");
				document.getElementById("LoginFeature").innerHTML = "<div id=\"Left\"><a href = Login.jsp>Login</a></div>" + "<div id=\"Right\"><a href = Register.jsp>Register</a></div>";
			}
			
		}
		else{
			
			//firstSession = "true"
			
			localStorage.setItem("firstSession", "false");
			
			var xhttp = new XMLHttpRequest();
			xhttp.open("GET", "HomePage?firstSession=true", false);
			xhttp.send();
			
			document.getElementById("LoginFeature").innerHTML = "<div id=\"Left\"><a href = Login.jsp>Login</a></div>" + "<div id=\"Right\"><a href = Register.jsp>Register</a></div>";

			
		}
		 
	 }
	
	</script>
	 
	
</head>
<body onload="onload()">


<script>
 function validate(){
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

	<img src="./image/background.jpg" style="width: 100%" id="background">
	
	<img id="content" src="./image/bookworm.png" width=125 height=100>
	
	<div id = "LoginFeature"></div>
	
	<h1 id="Title">BookWorm: Just a Mini Program... Happy Days!</h1>
	<br>
	<form name= "myform" method="GET" action="SearchResults.jsp" onsubmit="validate()">
		<input type="text" name="searchbar" id="searchbar" placeholder="Search for your favorite book!">
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
 		<br>
 		</div>
 		<div id="publisherbutton">
 		<input type="radio" id="publisher" name="searchparameters" value="inpublisher">
 		<label for="publisher">Publisher</label>
 		</div>
 		<br>
 		<input type="submit" name="submit" value="Search!" id="submit">
	</form>
	
	
	
	<div id="errorMessage"></div>
	
	
	
	
	
	
	
	
</body>
</html>
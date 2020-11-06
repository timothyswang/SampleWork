<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>

<link rel="stylesheet" type="text/css" href="Login.css">

<style>
@font-face {
  font-family: myFont;
  src: url(fontawesome-webfont.ttf);
  src: url(fontawesome-webfont.eot);
  src: url(fontawesome-webfont.svg);
  src: url(fontawesome-webfont.woff);
  src: url(fontawesome-webfont.woff2);
  src: url(FontAwesome.otf);
}

#usernameinput{
	width : 80%;
	height : 3%;
	position: absolute;
	left: 10%;
	top: 32%;
}

#UsernameTag{
	font-family: myFont;
	position: absolute;
	left: 10%;
	top: 28%;
	color: Grey;
}

#passwordinput{
	width : 80%;
	height : 3%;
	position: absolute;
	left: 10%;
	top: 42%;
}

#PasswordTag{
	font-family: myFont;
	position: absolute;
	left: 10%;
	top: 38%;
	color: Grey;
}

#signIn{
	position: absolute;
	font-family: myFont;
	top: 52%;
	left : 10%;
	padding:6px 15px;
	-webkit-border-radius: 5px;
    border-radius: 5px;
	background: LightGrey;
	color: white;
	width : 80%;
}

#error_msg{
	position: absolute;
	font-family: myFont;
	top: 65%;
	left : 10%;
	color: red;
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
	
	
	function validate(){
		var xhttp = new XMLHttpRequest();
		xhttp.open("GET", "Login?usernameinput=" + document.loginform.usernameinput.value + "&passwordinput=" + document.loginform.passwordinput.value, false);
		xhttp.send();
		
		if (xhttp.responseText.trim().length > 0){
			document.getElementById("error_msg").innerHTML = xhttp.responseText;
			return false;
		}
		return true;
		
		
	}
	
	
	
	</script>

</head>
<body>
	<div id="worm">
        <a href = HomePage.jsp><img id="content" src="./image/bookworm.png" width=125 height=100></a>
        </div>
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
        
        <form name= "loginform" method="GET" action="HomePage.jsp" onsubmit="return validate();">
        	 <label for="usernameinput" id="UsernameTag">Username</label>
        	 <input type="text" name="usernameinput" id="usernameinput">
        	 <br>
        	 <label for="passwordinput" id="PasswordTag">Password</label>
        	 <input type="text" name="passwordinput" id="passwordinput">
        	 <br>
        	 <input type="submit" name="signIn" value="Sign In" id="signIn">
        </form>
        
        <div id="error_msg">
		</div>

</body>
</html>
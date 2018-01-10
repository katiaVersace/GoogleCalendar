<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

  
<link rel="stylesheet" type="text/css" href="resources/css/index.css">
</head>
<body>
<div class="row" >
 <div class="col-sm-3"></div>
<div  class="col-sm-6">
<form  method="post" class="loginForm">
  <div class="input-group">
    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
    <input id="username" type="text" class="form-control" name="username" placeholder="Username">
  </div>
  <div class="input-group">
    <span class="input-group-addon"><i class="glyphicon glyphicon-email"></i></span>
    <input id="email" type="text" class="form-control" name="email" placeholder="Email">
  </div>
  <div class="input-group">
    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
    <input id="password" type="password" class="form-control" name="password" placeholder="Password">
  </div>
  
  <br>


<input type="submit" class="btn btn-success" formaction="loginAttempt"></input>



<form  method="post" class="loginForm">
		<input class="btn btn-info" id="Register-btn" type="submit" formaction="registrationAttempt" value="Registrati"></input>
		</form>
		</form>
  </div>
   
  </div>
</body>
</html>
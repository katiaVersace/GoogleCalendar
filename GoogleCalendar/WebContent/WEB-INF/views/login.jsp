<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link rel="stylesheet" type="text/css" href="resources/css/login.css">

<script src="resources/scripts/login.js"></script>
<script>
	// Load the SDK asynchronously
	(function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id))
			return;
		js = d.createElement(s);
		js.id = id;
		js.src = "https://connect.facebook.net/en_US/sdk.js";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));

	window.fbAsyncInit = function() {
		FB.init({
			appId : '841220369398837',
			cookie : true, // enable cookies to allow the server to access 
			// the session
			xfbml : true, // parse social plugins on this page
			version : 'v2.8' // use graph api version 2.11
		});

	};

	// This function is called when someone finishes with the Login
	function checkLoginState() {
		FB.getLoginStatus(function(response) {
			statusChangeCallback(response);
		});
	}

	// This is called with the results from from FB.getLoginStatus().
	function statusChangeCallback(response) {

		console.log('statusChangeCallback');
		console.log(response);

		if (response.status === 'connected') {
			// Logged into your app and Facebook.

			//user data
			var user_name;
			var user_email;

			//calendar data
			var user_birthday;
			var user_events;
 

			//get Data From FB
			FB
					.api(
							'/me',
							{
								locale : 'it_IT',
								fields : 'name, email,birthday,events'
							},
							function(response) {

								document.getElementById('status').innerHTML = 'You are logged as '
										+ response.name;
 
								user_email = response.email;
								user_name = response.name;
								user_birthday = response.birthday;
								
								
 								events = response.events.data;
								
								/* console.log("eventssssssssssssssssssssss \n");
								console.log(events); */
								
/* 								var str = JSON.stringify(events);
								var jsonOBJ = JSON.parse(str); */
								
							
								// used to read events properties ( some are not defined)
								function getSafe(fn) {
								    try {
								        return fn();
								    } catch (e) {
								        return undefined;
								    }
								}
								
								var evtJSONArray =[];
 
								/* for (var i = 0; i < jsonOBJ.length; i++) {
								    
				
								    console.log("evento ==> "+getSafe(() => jsonOBJ[i].name)+" ----  "+jsonOBJ[i].start_time+ " * "+ getSafe(() => jsonOBJ[i].place.name)+"\n");
							
								    var description = "si tiene presso"+getSafe(() => jsonOBJ[i].place.name);
								    var evtJSON = { "name":getSafe(() => jsonOBJ[i].name), "description":description, "date":getSafe(() => jsonOBJ[i].start_time)};
								    evtJSONArray.push(evtJSON);
								    
								} */
								
								console.log("SIZEEEEEEEEEEEEEEEEEEEEEE=Z"+events.length);
								
								for (var i = 0; i < events.length; i++) {
								    
									
								    console.log("evento ==> "+getSafe(() => events[i].name)+" ----  "+events[i].start_time+ " * "+ getSafe(() => events[i].place.name)+"\n");
							
								    var description = "si tiene presso"+getSafe(() => events[i].place.name);
								    var evtJSON = { "name":getSafe(() => events[i].name), "description":description, "date":getSafe(() => events[i].start_time)};
								    evtJSONArray.push(evtJSON);
								    
								} 
								
								
 
								var resultJSON = {"name":user_name,"email":user_email,"birthday":user_birthday,"events":evtJSONArray}
  								
								console.log(JSON.stringify(resultJSON));
								
								//pass data to LoginController
								$.ajax({
									type: "POST",
									url : "getFBData",
									data :  { resultJSON: JSON.stringify(resultJSON)}
									 ,
									dataType : "json",
									success : function(result) {

										 var url = window.location.href;
										var lastIndex = url.lastIndexOf("/");
										url = url.substring(0, lastIndex);
										var redirect = url + "/index";

										location.href = redirect; 

									},
									error : function(result) {
 										
 
										 var url = window.location.href;
										var lastIndex = url.lastIndexOf("/");
										url = url.substring(0, lastIndex);
										var redirect = url + "/index";

										location.href = redirect;

									}

								});

							});

		} else {
			// The person is not logged into your app or we are unable to tell.
			document.getElementById('status').innerHTML = 'Please log '
					+ 'into this app.';
		}
	}
</script>

</head>
<div class="container">
	<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<div class="panel panel-login">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-6">
							<a href="#" class="${login_title}" id="login-form-link">Login</a>
						</div>
						<div class="col-xs-6">
							<a href="#" class="${register_title}" id="register-form-link">Register</a>
						</div>
					</div>
					<hr>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-lg-12">
							<form id="login-form" method="post" role="form"
								style="display: ${login_block};">
								<div class="form-group">
									<input type="text" name="email" id="email" tabindex="1"
										class="form-control" placeholder="Email" value="">
								</div>
								<div class="form-group">
									<input type="password" name="password" id="password"
										tabindex="2" class="form-control" placeholder="Password">
								</div>
								<div class="form-group text-center">
									<input type="checkbox" tabindex="3" class="" name="remember"
										id="remember"> <label for="remember"> Remember
										Me</label>
								</div>
								<div class="form-group">
									<div class="row">
										<div class="col-sm-6 col-sm-offset-3">
											<input type="submit" name="login-submit" id="login-submit"
												formaction="loginAttempt" tabindex="4"
												class="form-control btn btn-login" value="Log In">
										</div>
									</div>
								</div>

								<fb:login-button
									scope="public_profile,email,user_hometown,user_birthday,user_education_history,user_website,user_work_history,user_events"
									onlogin="checkLoginState();">
								</fb:login-button>

								<div id="status"></div>

							</form>
							<form id="register-form" method="post" role="form"
								style="display: ${register_block};">
								<div class="form-group">
									<input type="text" name="username" id="username" tabindex="1"
										class="form-control" placeholder="Username" value="">
								</div>
								<div class="form-group">
									<input type="email" name="email" id="email" tabindex="1"
										class="form-control" placeholder="Email Address" value="">
								</div>
								<div class="form-group">
									<input type="password" name="password" id="password"
										tabindex="2" class="form-control" placeholder="Password">
								</div>
								<div class="form-group">
									<input type="password" name="confirmPassword"
										id="confirm-password" tabindex="2" class="form-control"
										placeholder="Confirm Password">
								</div>
								<div class="form-group">
									<div class="row">
										<div class="col-sm-6 col-sm-offset-3">
											<input type="submit" name="register-submit"
												id="register-submit" formaction="registrationAttempt"
												onclick="validation(this)" tabindex="4"
												class="form-control btn btn-register" value="Register Now">
										</div>
									</div>
								</div>

							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</div>
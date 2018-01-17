<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Login Page</title>
<meta charset="utf-8">

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link rel="stylesheet" type="text/css" href="resources/css/login.css">

<script src="resources/scripts/login.js"></script>
<script src="resources/scripts/checkRegistrationForm.js"></script>


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
			version : 'v2.11' // use graph api version 2.11
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
			var name;
			var email;

			//calendar data
			var birthday;
			var friends;
			var events;

			//get Data From FB
			FB
					.api(
							'/me',
							{
								locale : 'it_IT',
								fields : 'name, email,birthday,friends,events'
							},
							function(response) {

								email = response.email;
								name = response.name;
								birthday = response.birthday;

								//friends = response.friends.summary.total_count;
								events = response.events.data;

								//pass data to LoginController
								$.ajax({
									url : "getFBData",
									data : {
										name : name,
										birthday : birthday,
										email : email
									},
									success : function(result) {

										var url = window.location.href;
										var lastIndex = url.lastIndexOf("/");
										url = url.substring(0, lastIndex);
										var redirect = url + "/"+result;

										location.href = redirect;

									},
									error : function(result) {
										alert("facebook ajax error ")
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
<body>
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
											class="form-control" placeholder="Email" value=""
											style="color: #337ab7;">
									</div>
									<div class="form-group">
										<input type="password" name="password" id="password"
											tabindex="2" class="form-control" placeholder="Password"
											style="color: #337ab7;">
									</div>
									<div class="form-group text-center">
										<input type="checkbox" tabindex="3" class="" name="remember"
											id="remember"> <label for="remember"
											style="color: #337ab7">Remember Me</label>
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
								</form>

								<fb:login-button
									scope="public_profile,email,user_hometown,user_birthday,user_education_history,user_website,user_work_history,user_events"
									onlogin="checkLoginState();">
								</fb:login-button>

								<form id="register-form" method="post" role="form"
									style="display: ${register_block};">

									<div class="form-group">
										<input type="text" name="username" id="username_1"
											onkeyup='check();' tabindex="1" class="form-control"
											placeholder="Username" value="" style="color: #337ab7;">
									</div>
									<div class="form-group">
										<input type="email" name="email" id="email_1"
											onkeyup='check();' tabindex="1" class="form-control"
											placeholder="Email Address" value="" style="color: #337ab7;">
									</div>

									<div class="form-group">
										<input name="password" id="password_1" type="password"
											onkeyup='check();' tabindex="2" class="form-control"
											placeholder="Password" style="color: #337ab7;" />
									</div>

									<div class="form-group">
										<input type="password" name="confirm_password"
											id="confirm_password" onkeyup='check();' tabindex="2"
											class="form-control" placeholder="Confirm Password"
											style="color: #337ab7;" />
									</div>

									<div class="form-group" style="text-align: center">
										<span id='message'></span>
									</div>

									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="register-submit"
													id="register_button" formaction="registrationAttempt"
													disabled="true" tabindex="4"
													class="form-control btn btn-register" value="Register Now"
													style="color: green;">
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
</html>




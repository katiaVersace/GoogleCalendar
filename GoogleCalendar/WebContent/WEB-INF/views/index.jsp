<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>MyBlog</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
   
<link rel="stylesheet" type="text/css" href="resources/css/index.css">
</head>
<body>

<div class="container-fluid">
  <div class="row content">
    <div class="col-sm-3 sidenav">
      <h4>katia's Blog</h4>
      <ul class="nav nav-pills nav-stacked">
        <li class="active"><a href="#section1">Home</a></li>
        <li><a href="#section2">Friends</a></li>
        <li><a href="#section3">Family</a></li>
        <li><a href="#section3">Photos</a></li>
      </ul><br>
      <div class="input-group">
        <input type="text" class="form-control" placeholder="Search Blog..">
        <span class="input-group-btn">
          <button class="btn btn-default" type="button">
            <span class="glyphicon glyphicon-search"></span>
          </button>
        </span>
      </div>
    </div>

    <div id="posts" class="col-sm-9">
				<h4>
					<small>RECENT POSTS</small>
				</h4>
				<hr>

				<c:forEach items="${posts}" var="post">
					<h2>${post.title}</h2>
					<h5>
						<span class="glyphicon glyphicon-time"></span> Post by ${post.author}, ${post.data}
					</h5>
					<br>
					<p>${post.text}</p>
					<hr>
						<h4>Leave a Comment:</h4>
						<form role="form" action="comment">
							<div class="form-group">
								<textarea class="form-control" name="comment" rows="3" required></textarea>
							</div>
							<input type="hidden" name="post" value="${post.id}">
							<button type="submit" class="btn btn-success">Submit</button>
						</form>
					
					<br>
					<div class="row">
						<c:forEach items="${post.comments}" var="comment">
							<div class="col-sm-2 text-center">
								<img src="resources/images/avatar1.png" class="img-circle"
									height="65" width="65" alt="Avatar">
							</div>
							<div class="col-sm-10">
								<h4>
									${comment.author} <small> ${comment.data}</small>
								</h4>
								<p>${comment.text}</p>
								<br>
							</div>
						</c:forEach>
					</div>
					<br>
					<br>

					<hr>
				</c:forEach>

			</div>

  <form action="logout">
		<input class="btn btn-danger" id="logout-btn" type="submit" value="Logout"></input>
	</form>
  </div>
</div>

<footer class="container-fluid">
  <p>Footer Text</p>
</footer>

</body>
</html>

<!DOCTYPE html>
<html>
<head>
<style><%@include file="css/404.css"%></style>
<title>Insert title here</title>
</head>
<body>
<div class="mars"></div>
<img src="https://assets.codepen.io/1538474/404.svg" class="logo-404" />
<img src="https://assets.codepen.io/1538474/meteor.svg" class="meteor" />
<p class="title">Error message: <%= request.getAttribute("errorMessage") %></p>
<p class="title">Error Type:<%= request.getAttribute("errorCode") %></p>
<p class="subtitle">
	You’re either misspelling the URL <br /> or requesting a page that's no longer here.
</p>
<div align="center">
	<a class="btn-back" href="#">Back to previous page</a>
</div>
<img src="https://assets.codepen.io/1538474/astronaut.svg" class="astronaut" />
<img src="https://assets.codepen.io/1538474/spaceship.svg" class="spaceship" />
<b></b>
<br>
<b></b>
</body>
</html>
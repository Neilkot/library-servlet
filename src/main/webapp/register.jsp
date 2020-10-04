<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<link rel="stylesheet" href="css/style.css">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/md5.js"></script>
</head>
<body>
	<div class="wrapper">
		<div class="container">
			<h1>Welcome</h1>
			<form id="registration-form" class="form" action="./reader" method="post">
				<input type="text" name="login" placeholder="Login" />
				<input type="text" name="firstName" placeholder="First Name" />
				<input type="text" name="lastName" placeholder="Last Name" />
				<input id="password" type="password" name="password" placeholder="Password" />
				<input id="checksum" type="hidden" name="checksum" />
				<button type="submit" id="login-button">Register</button>
			</form>
		</div>

		<ul class="bg-bubbles">
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
		</ul>
	</div>
	
	<script>
		$("#registration-form").on("submit", function() {
			var password = $("#password");
			if(password) {
				var checksum = md5(password);
				$("#checksum").val(checksum);
			}
	    });
	</script>
</body>
</html>
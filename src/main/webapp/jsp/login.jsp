<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<link rel="stylesheet" href="css/style.css">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<script src="${pageContext.request.contextPath}/js/md5.js"></script>
</head>
<body>
	<div class="wrapper">
		<div class="container">
			<h1>Welcome</h1>
			<form id="login-form" class="form" action="login" method="post">
				<input type="text" name="login" placeholder="Login" /> <input id="password" type="password" name="password" placeholder="Password" /> <input id="checksum" type="hidden" name="checksum" />
				<button type="submit" id="login-button">Login</button>
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
		$("#login-form").on("submit", function() {
			var password = $("#password");
			if (password) {
				var checksum = md5(password);
				$("#checksum").val(checksum);
			}
		});
	</script>
</body>
</html>
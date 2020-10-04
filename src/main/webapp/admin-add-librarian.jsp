<!DOCTYPE html>
<html>
<head>
<title>Adding librarian</title>
<link rel="stylesheet" href="css/style.css">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>
	<div class="wrapper">
		<div class="container">
			<h1>Adding librarian</h1>
			<form id="add-librarian-form" class="form" action="./librarian" method="post" accept-charset="utf-8"
			enctype="text/html;charset=UTF-8">
				<input type="text" name="login" placeholder="Login" />
				<input type="text" name="firstName" placeholder="First Name" />
				<input type="text" name="lastName" placeholder="Last Name" />
				<input id="password" type="password" name="password" placeholder="Password" />
				<input id="checksum" type="hidden" name="checksum" />
				<button type="submit" id="login-button"><fmt:message key="admin.add.librarian.add" /></button>
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
		$("#add-librarian-form").on("submit", function() {
			var password = $("#password");
			if(password) {
				var checksum = md5(password);
				$("#checksum").val(checksum);
			}
	    });
	</script>

</body>
</html>
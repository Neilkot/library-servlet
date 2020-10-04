<!DOCTYPE html>
<html>
<head>
<title>Adding book</title>
<link rel="stylesheet" href="css/style.css">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
</head>
<body>
	<div class="wrapper">
		<div class="container">
			<h1>Welcome</h1>
			<form class="form" action="./admin-books" method="post" accept-charset="utf-8"
			enctype="text/html;charset=UTF-8">
				<input type="text" name="bookName" placeholder="Book name" />
				<input type="text" name="author_name" placeholder="Author name" />
				<input type="text" name="publisher" placeholder="Publisher" />
				<input type="text" name="publish_year" placeholder="Publish year" />
                <input type="text" name="image_link" placeholder="Image link" />
				<button type="submit" id="login-button"><fmt:message key="admin.add.book.add" /></button>
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


</body>
</html>
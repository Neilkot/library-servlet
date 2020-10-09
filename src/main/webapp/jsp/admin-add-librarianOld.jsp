<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<c:choose>
	<c:when test="${cookie.get('language').value == 'ua'}">
		<fmt:setLocale value='ua' scope="session" />
	</c:when>
	<c:otherwise>
		<fmt:setLocale value='en' scope="session" />
	</c:otherwise>
</c:choose>
<fmt:requestEncoding value="UTF-8" />
<fmt:setBundle basename="locale" />
<!DOCTYPE html>
<html>
<head>
<title>Adding librarian</title>
<link rel="stylesheet" href="css/style.css">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/md5.js"></script>
</head>
<body>
	<div class="wrapper">
		<div class="container">
			<h1>Adding librarian</h1>
			<form id="add-librarian-form" class="form" action="/admin-librarians" method="post" accept-charset="utf-8" enctype="text/html;charset=UTF-8">
				<input type="text" name="login" placeholder="Login" />
				 <input type="text" name="firstName" placeholder="First Name" />
				  <input type="text" name="lastName" placeholder="Last Name" /> 
				  <input id="password" type="password" name="password" placeholder="Password" /> 
				  <input id="checksum" type="hidden" name="checksum" />
				<button type="submit" id="login-button">
					<fmt:message key="admin.add.librarian.add" />
				</button>
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
			if (password) {
				var checksum = md5(password);
				$("#checksum").val(checksum);
			}
		});
	</script>

</body>
</html>
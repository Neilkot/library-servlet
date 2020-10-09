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
<title>Adding book</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<div align="center">
		<form action="/admin-books" method="post" accept-charset="utf-8" enctype="text/html;charset=UTF-8">
			<div class="form-group row">
				<label for="inputEmail3" class="col-sm-2 col-form-label">Book name</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="bookName" id="inputEmail3" placeholder="Book name">
				</div>
			</div>
			<div class="form-group row">
				<label for="inputPassword3" class="col-sm-2 col-form-label">Author name</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="author_name" id="inputPassword3" placeholder="Author">
				</div>
			</div>
			<div class="form-group row">
				<label for="inputPassword3" class="col-sm-2 col-form-label">Publisher</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="publisher" id="inputPassword3" placeholder="Publisher">
				</div>
			</div>
			<div class="form-group row">
				<label for="inputPassword3" class="col-sm-2 col-form-label">Published year</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="published_year" id="inputPassword3" placeholder="Published year">
				</div>
			</div>
			<div class="form-group row">
				<label for="inputPassword3" class="col-sm-2  col-form-label">Image</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="image_link" id="inputPassword3" placeholder="Image">
				</div>
			</div>

			<div class="form-group row">
				<div class="col-sm-10">
					<button type="submit" class="btn btn-primary">
						<fmt:message key="admin.add.book.add" />
					</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
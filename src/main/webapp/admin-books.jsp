<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.epam.lab.exam.library.dto.BookDTO"%>
<style><%@include file="css/style.css"%></style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
</head>
<body>
	<jsp:include page="/admin-books" />
	<%@include file="header.jsp"%>

	<h1>Admin's books page</h1>
	<%
		List<BookDTO> books = (List<BookDTO>) request.getAttribute("books");
	%>
	<a href="./admin-add-book.jsp"><fmt:message key="admin.books.add.book" /></a>
	<p>
	<form id="submit_form" action="./admin-books" method="post">
		<input id="submit_book_item_id" name="bookItemId" type="hidden"/>
		<input id="submit_request_type" name="method" type="hidden"/>
	</form>
	
	<img src="https://i.insider.com/5b8407d03cccd122058b4580?width=1100&format=jpeg&auto=webp" alt="Girl in a jacket" width="150" height="150">
	<table>
	<tr>
		<td><fmt:message key="books.table.book.id" /></td>
		<td><fmt:message key="books.table.book.item.id" /></td>
		<td><fmt:message key="books.table.book.name" /></td>
		<td><fmt:message key="books.table.book.author.name" /></td>
		<td><fmt:message key="books.table.book.publisher" /></td>
		<td><fmt:message key="books.table.book.published.year" /></td>
		<td><fmt:message key="books.table.book.image" /></td>
		<td><fmt:message key="admin.books.table.book.action" /></td>
		<td><fmt:message key="admin.books.table.book.submit" /></td>
		</tr>
		<c:forEach items="${books}" var="book">
			<tr>
				<td><c:out value="${book.bookId}" /></td>
				<td><c:out value="${book.bookItemId}" /></td>
				<td><c:out value="${book.name}" /></td>
				<td><c:out value="${book.authorName}" /></td>
				<td><c:out value="${book.publisher}" /></td>
				<td><c:out value="${book.publishedYear}" /></td>
				<td><img src="<c:out value="${book.imgLink}" />" width="150" height="150"></td>
				<td>
					<select id="request_type_${book.bookItemId}">
						<option value="" />
						<option value="delete"><fmt:message key="admin.books.delete.book" /></option>
						<option value="put"><fmt:message key="admin.books.edit.book" /></option>
					</select>
				</td>
				<td><input type="button" onclick="submitRequest(${book.bookItemId});"/></td>
			</tr>
		</c:forEach>
	</table>
</body>
<script>
	function submitRequest(bookItemId) {
		requestType = $("#request_type_" + bookItemId).val();
		if (!requestType) {
			alert("Invalid input. Change this message...")
		} else {
			$("#submit_book_item_id").val(bookItemId);
			$("#submit_request_type").val(requestType);
			if(requestType == 'put') {
				// set other fields
			}
			$("#submit_form").submit();
		}
	}
</script>
</html>
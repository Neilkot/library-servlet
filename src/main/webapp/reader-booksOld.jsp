<%@ page import="com.epam.lab.exam.library.util.DBHelper"%>
<%@ page import="java.util.List"%>
<%@ page import="com.epam.lab.exam.library.dto.BookDTO"%>
<style><%@include file="css/style.css"%></style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="/book" />
	<%@include file="header.jsp"%>

	<h1>Reader Books</h1>
	<%
		List<BookDTO> books = (List<BookDTO>) request.getAttribute("books");
	%>
	
	<form id="submit_form" action="book-request" method="post">
		<input id="submit_book_id" name="bookId" type="hidden"/>
		<input id="submit_request_type" name="requestType" type="hidden"/>
	</form>
	
	<img src="https://i.insider.com/5b8407d03cccd122058b4580?width=1100&format=jpeg&auto=webp" alt="Girl in a jacket" width="150" height="150">
	<table>
		<c:forEach items="${books}" var="book">
			<tr>
				<td><c:out value="${book.bookId}" /></td>
				<td><c:out value="${book.name}" /></td>
				<td><c:out value="${book.authorName}" /></td>
				<td><c:out value="${book.publisher}" /></td>
				<td><c:out value="${book.publishedYear}" /></td>
				<td><img src="<c:out value="${book.imgLink}" />" width="150" height="150"></td>
				<td>
					<select id="request_type_${book.bookId}">
						<option value="" />
						<option value="ABONEMENT"><fmt:message key="request.type.abonement" /></option>
						<option value="READING_AREA"><fmt:message key="request.type.reading.area" /></option>
					</select>
				</td>
				<td><input type="button" onclick="submitRequest(${book.bookId});"/></td>
			</tr>
		</c:forEach>
	</table>
</body>
<script>
	function submitRequest(bookId) {
		requestType = $("#request_type_" + bookId).val();
		if (!requestType) {
			alert("Invalid input. Change this message...")
		} else {
			$("#submit_book_id").val(bookId);
			$("#submit_request_type").val(requestType);
			$("#submit_form").submit();
		}
	}
</script>
</html>
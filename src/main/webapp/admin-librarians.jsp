<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.epam.lab.exam.library.dto.UserDTO"%>
<style><%@include file="css/style.css"%></style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Admin Librarians page</title>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<script src="${pageContext.request.contextPath}/js/md5.js"></script>
</head>
<body>
	<jsp:include page="/librarian" />
	<%@include file="header.jsp"%>

	<h1>Admin's librarians page</h1>
	<%
		List<UserDTO> librarians = (List<UserDTO>) request.getAttribute("librarians");
	%>
	<a href="./admin-add-librarian.jsp"><fmt:message key="admin.librarians.add.librarian" /></a>
	<p>
	<form id="submit_form" action="./librarian" method="post">
		<input id="submit_librarian_id" name="id" type="hidden"/>
		<input id="submit_request_type" name="method" value="delete" type="hidden"/>
	</form>
	
	<table>
	<tr>
		<td><fmt:message key="librarians.table.librarian.id" /></td>
		<td><fmt:message key="librarian.table.librarian.login" /></td>
		<td><fmt:message key="librarian.table.librarian.first.name" /></td>
		<td><fmt:message key="librarian.table.librarian.last.name" /></td>
		<td><fmt:message key="librarian.table.submit" /></td>
		</tr>
		<c:forEach items="${librarians}" var="librarian">
			<tr>
				<td><c:out value="${librarian.id}" /></td>
				<td><c:out value="${librarian.login}" /></td>
				<td><c:out value="${librarian.firstName}" /></td>
				<td><c:out value="${librarian.lastName}" /></td>
				<td><input type="button" onclick="submitRequest(${librarian.id});"/></td>
			</tr>
		</c:forEach>
	</table>
</body>
<script>
	function submitRequest(id) {
		$("#submit_librarian_id").val(id);
		$("#submit_form").submit();
	}
</script>
</html>
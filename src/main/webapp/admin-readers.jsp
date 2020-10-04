<%@ page import="java.util.List"%>
<%@ page import="com.epam.lab.exam.library.dto.UserDTO"%>
<style><%@include file="css/style.css"%></style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="/reader" />
	<%@include file="header.jsp"%>

	<h1>Admin's reader page</h1>
	<%
		List<UserDTO> readers = (List<UserDTO>) request.getAttribute("readers");
	%>
	<p>
	<form id="change_reader_blocked_form" action="./reader" method="post">
		<input type="hidden" name="method" value="put" />
		<input id="change_reader_blocked_id" name="readerId" type="hidden"/>
	</form>
	
	<table>
	<tr>
				<td><fmt:message key="users.table.user.id" /></td>
				<td><fmt:message key="users.table.user.login" /></td>
				<td><fmt:message key="users.table.user.first.name" /></td>
				<td><fmt:message key="users.table.user.last.name" /></td>
				<td><fmt:message key="users.table.user.change.blocked" /></td>
			</tr>
		<c:forEach items="${readers}" var="reader">
			<tr>
				<td><c:out value="${reader.id}" /></td>
				<td><c:out value="${reader.login}" /></td>
				<td><c:out value="${reader.firstName}" /></td>
				<td><c:out value="${reader.lastName}" /></td>
				<td><input type="button" onclick="submitRequest(${reader.id});"/></td>
			</tr>
		</c:forEach>
	</table>

</body>
<script>
function submitRequest(id) {
	$("#change_reader_blocked_id").val(id);
	$("#change_reader_blocked_form").submit();
}
</script>
</html>
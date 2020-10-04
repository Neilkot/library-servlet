<%@ page import="java.util.List" %>
<%@ page import="com.epam.lab.exam.library.dto.BookRequestDTO" %>
<style><%@include file="css/style.css"%></style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
<jsp:include page="/approved-request" />
<%@include  file="header.jsp" %>

<h1>Approved Reader Requests</h1>
<% 
List<BookRequestDTO> requests = (List<BookRequestDTO>)  request.getAttribute("requests"); 
%>
<form id="submit_form" action="./approved-request" method="post">
		<input id="submit_request_id" name="bookRequestId" type="hidden"/>
	</form>
<table>
  <c:forEach items="${requests}" var="bookRequest">
    <tr>
      <td><c:out value="${bookRequest.requestId}" /></td>
      <td><c:out value="${bookRequest.username}" /></td>
      <td><c:out value="${bookRequest.bookName}" /></td>
      <td><c:out value="${bookRequest.authorName}" /></td>
      <td><c:out value="${bookRequest.requestType}" /></td>
      <td><c:out value="${bookRequest.createDate}" /></td>
      <td><c:out value="${bookRequest.approveDate}" /></td>
      <td><c:out value="${bookRequest.expirationDate}" /></td>
      <td><c:out value="${bookRequest.returnDate}" /></td>
      <td><c:out value="${bookRequest.fee}" /></td>
      <td><input type="button" onclick="submitRequest(${bookRequest.requestId});"/></td>
    </tr>
    
  </c:forEach>
</table>
<script>
	function submitRequest(requestId) {
			$("#submit_request_id").val(requestId);
			$("#submit_form").submit();
	}
</script>
</body>
</html>
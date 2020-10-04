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
<jsp:include page="/book-request" />
<%@include  file="header.jsp" %>

<h1>Reader Requests</h1>
<% 
List<BookRequestDTO> requests = (List<BookRequestDTO>)  request.getAttribute("requests"); 
%>
<table>
  <c:forEach items="${requests}" var="bookRequest">
    <tr>
      <td><c:out value="${bookRequest.requestId}" /></td>
      <td><c:out value="${bookRequest.bookId}" /></td>
      <td><c:out value="${bookRequest.username}" /></td>
      <td><c:out value="${bookRequest.bookName}" /></td>
      <td><c:out value="${bookRequest.authorName}" /></td>
      <td><c:out value="${bookRequest.requestType}" /></td>
      <td class="table-column-date"><c:out value="${bookRequest.createDate}" /></td>
      <td class="table-column-date"><c:out value="${bookRequest.approveDate}" /></td>
      <td class="table-column-date"><c:out value="${bookRequest.expirationDate}" /></td>
      <td class="table-column-date"><c:out value="${bookRequest.returnDate}" /></td>
      <td><c:out value="${bookRequest.fee}" /></td>
    </tr>
  </c:forEach>
</table>

<script>
	$(document).ready(function() {
		formatDates();
	});
</script>
</body>
</html>
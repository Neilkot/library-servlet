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
<title>Insert title here</title>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/styles.css"/>
</head>
<body>
<p>
<p>
<p>
<p>
<p>
<form id="login-form" class="form" action="../login" method="post">
  <div class="form-group">
    <label for="exampleInputEmail1"><fmt:message key="header.login" /></label>
    <input type="text" class="form-control" id="exampleInputEmail1" name="login" placeholder="Login" aria-describedby="emailHelp"">
  </div>
  <div class="form-group">
    <label for="exampleInputPassword1"><fmt:message key="header.password" /></label>
    <input id="checksum" type="password" name="checksum" class="form-control"  placeholder="Password">
  </div>
  <div class="form-group form-check">
  </div>
  <button type="submit" class="btn btn-primary" id="login-button"><fmt:message key="header.submit" /></button>
</form>

</body>
</html>
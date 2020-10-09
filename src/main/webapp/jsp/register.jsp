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
<title><fmt:message key="header.registration.entermsg" /></title>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<script src="${pageContext.request.contextPath}/js/md5.js"></script>
<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/styles.css"/>
</head>
<body>
<p>
<p>
<p>
<p>
<p>
<form id="login-form" class="form" action="/reader" method="post">
<h2><fmt:message key="header.registration.entermsg" /></h2>
  <div class="form-group">
    <label for="login"><fmt:message key="header.login" /></label>
    <input type="text" class="form-control" id="exampleInputEmail1" name="login" placeholder="<fmt:message key="header.login" />" aria-describedby="emailHelp">
  </div>
   <div class="form-group">
    <label for="firstName"><fmt:message key="header.firstname" /></label>
    <input type="text" class="form-control" id="exampleInputEmail1" name="firstName" placeholder="<fmt:message key="header.firstname" />" aria-describedby="emailHelp">
  </div>
   <div class="form-group">
    <label for="lastName"> <fmt:message key="header.lastname" /></label>
    <input type="text" class="form-control" id="exampleInputEmail1" name="lastName" placeholder="<fmt:message key="header.lastname" />" aria-describedby="emailHelp">
  </div>
  <div class="form-group">
    <label for="password"><fmt:message key="header.password" /></label>
    <input id="password" type="password" name="password" class="form-control"  placeholder="<fmt:message key="header.password" />">
    <input id="checksum" type="hidden" name="checksum" />
  </div>
  <div class="form-group form-check">
  </div>
  <button type="submit" class="btn btn-primary" id="login-button"><fmt:message key="header.submit" /></button>
</form>

	<script>
		$("#login-form").on("submit", function() {
			var password = $("#password");
			if (password) {
				var checksum = md5(password);
				$("#checksum").val(checksum);
			}
		});
	</script>
</body>
</html>
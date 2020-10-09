<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page import="com.epam.lab.exam.library.dto.UserSessionDTO"%>
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
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<title>Library</title>
<!-- Favicon-->
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon.ico" />
<!-- Google fonts-->
<link href="https://fonts.googleapis.com/css?family=Merriweather+Sans:400,700" rel="stylesheet" />
<link href="https://fonts.googleapis.com/css?family=Merriweather:400,300,300italic,400italic,700,700italic" rel="stylesheet" type="text/css" />
<!-- Third party plugin CSS-->
<link href="https://cdnjs.cloudflare.com/ajax/libs/magnific-popup.js/1.1.0/magnific-popup.min.css" rel="stylesheet" />
<!-- Core theme CSS (includes Bootstrap)-->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css"/>
 
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://use.fontawesome.com/releases/v5.13.0/js/all.js" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/globalVars.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/md5.js"></script>
</head>

<%
	UserSessionDTO userSession = null;
if (session != null) {
	userSession = (UserSessionDTO) session.getAttribute("userSession");
}
%>
<body id="page-top">
	<!-- Navigation-->
	<nav class="navbar navbar-expand-lg navbar-light fixed-top py-3 position-fixed" id="mainNav">
		<div class="container">
						<a class="navbar-brand js-scroll-trigger" href="#page-top">LIBRARY PROJECT</a>
			<button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarResponsive">
				<ul class="navbar-nav ml-auto my-2 my-lg-0">
					<c:choose>
						<c:when test="${empty userSession}">
							<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/reader-books"><fmt:message key="header.home" /></a></li>
							<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/jsp/login.jsp"><fmt:message key="header.login" /></a></li>
							<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/jsp/register.jsp"><fmt:message key="header.register" /></a></li>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${userSession.roleName == 'READER'}">
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href=/reader-books><fmt:message key="header.home" /></a></li>
									<!-- Find select for bootstrap -->
									<!-- <select name="nav-menu" id="nav-menu"> -->
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/reader-books"><fmt:message key="header.reader.books" /></a></li>
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/reader-requests"><fmt:message key="header.reader.requests" /></a></li>
									<!-- </select> -->
								</c:when>
								<c:when test="${userSession.roleName == 'ADMIN'}">
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/admin-books"><fmt:message key="header.home" /></a></li>
									<!-- <select name="nav-menu" id="nav-menu"> -->
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/admin-books"><fmt:message key="header.reader.books" /></a></li>
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/admin-librarians"><fmt:message key="header.admin.librarians" /></a></li>
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/reader"><fmt:message key="header.admin.users" /></a></li>
									<!-- </select> -->
								</c:when>
								<c:when test="${userSession.roleName == 'LIBRARIAN'}">
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/pending-request"><fmt:message key="header.home" /></a></li>
									<!-- <select name="nav-menu" id="nav-menu"> -->
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/pending-request"><fmt:message key="header.pending" /></a></li>
									<li class="nav-item"><a class="nav-link js-scroll-trigger" href="/approved-request"><fmt:message key="header.approved" /></a></li>
									<!-- </select> -->
								</c:when>
							</c:choose>
							<li class="nav-item"><a class="nav-link js-scroll-trigger" href="./logout"><fmt:message key="header.logout" /></a></li>
						</c:otherwise>
					</c:choose>
					<li><a class='dropdown-trigger' href='#' data-target='dropdown_lang'> <i class="material-icons">language</i>
					</a></li>
					<li>
						<ul id='dropdown_lang' class='dropdown-content'>
							<li id="en">En</li>
							<li id="ua">Ua</li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</nav>
	
	<script>
	$(document).ready(function() {

		$("#en").click(function() {
			document.cookie = "language=en";
			location.reload();
		});
		$("#ua").click(function() {
			document.cookie = "language=ua";
			location.reload();
		});
	});
	</script>
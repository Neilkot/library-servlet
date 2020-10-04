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

<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<link rel="stylesheet" href="css/header.css" type="text/css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript" src="js/globalVars.js"></script>
<title>Welcome</title>
</head>

	<fmt:requestEncoding value="UTF-8" />
	<fmt:setBundle basename="locale" />
	<%
		UserSessionDTO userSession = null;
	if (session != null) {
		userSession = (UserSessionDTO) session.getAttribute("userSession");
	}
	%>
	<header class="main__header">
		<div class="header__top">
			<div class="header__logo">
				<span>LIBRARY</span>
			</div>
			<label for="chk"><i class="fas fa-bars"></i></label> <input type="checkbox" id="chk">
			<nav class="main__menu">

				<c:choose>
					<c:when test="${empty userSession}">
					<a href="./reader-books.jsp"><fmt:message key="header.home" /></a>
						<a href="./login.jsp"><fmt:message key="header.login" /></a>
						<a href="./register.jsp"><fmt:message key="header.register" /></a>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${userSession.roleName == 'READER'}">
							<a href="./reader-books.jsp"><fmt:message key="header.home" /></a>
								<select name="nav-menu" id="nav-menu">
									<option value="./reader-books.jsp"><fmt:message key="header.reader.books" /></option>
									<option value="./reader-requests.jsp"><fmt:message key="header.reader.requests" /></option>
								</select>
							</c:when>
							<c:when test="${userSession.roleName == 'ADMIN'}">
							<a href="./admin-books.jsp"><fmt:message key="header.home"  /></a>
								<select name="nav-menu" id="nav-menu">
									<option value=""></option>
									<option value="./admin-books.jsp"><fmt:message key="header.admin.books" /></option>
									<option value="./admin-librarians.jsp"><fmt:message key="header.admin.librarians" /></option>
									<option value="./admin-readers.jsp"><fmt:message key="header.admin.users" /></option>
								</select>
							</c:when>
							<c:when test="${userSession.roleName == 'LIBRARIAN'}">
<a href="./pending-requests.jsp"><fmt:message key="header.home" /></a>
								<select name="nav-menu" id="nav-menu">
									<option value="./pending-requests.jsp"><fmt:message key="header.librarian.pending" /></option>
									<option value="./approved-requests.jsp"><fmt:message key="header.librarian.allowed" /></option>
								</select>
							</c:when>
						</c:choose>
						<a href="logout"><fmt:message key="header.logout" /></a>
					</c:otherwise>
				</c:choose>
				<a class='dropdown-trigger' href='#' data-target='dropdown_lang'> <i class="material-icons">language</i>
				</a>
				<ul id='dropdown_lang' class='dropdown-content'>
					<li id="en">En</li>
					<li id="ua">Ua</li>
				</ul>
				<a href="#contact">Contact</a> <label for="chk" class="close"><i class="fas fa-window-close"></i></label>
			</nav>

		</div>
		<script>
			$(document).ready(function() {

				chooseNavigationSelect();
				
				$("#en").click(function() {
					document.cookie = "language=en";
					location.reload();
				});
				$("#ua").click(function() {
					document.cookie = "language=ua";
					location.reload();
				});
			});
			
			function chooseNavigationSelect() {
				console.log("choosing nav select...");
				if($("#nav-menu")) {
					
					console.log("location.href=" + location.href);
					
					const options = document.querySelectorAll("#nav-menu option");
					for(const option of options) {
					  if(location.href.includes(option.value.substring(1))) {
					    option.setAttribute("selected", "");
					    console.log("option selected: " + option.value);
					    break;
					  } else {
						  console.log("option not selected: " + option.value); 
					  }
					}
					
					$('#nav-menu').on('change', function() {
						window.location.href=this.value;
					});
				}
			}
			
			function getCurrPageSuffix() {
				var pathName = window.location.pathname;
				if(pathName.includes("/")) {
					return pathName.substring(pathName.lastIndexOf("/"));
				}
			}
		</script>
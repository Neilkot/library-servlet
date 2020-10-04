<%@ page import="com.epam.lab.exam.library.dto.UserSessionDTO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ page import="java.util.List"%>
<%@ page import="com.epam.lab.exam.library.dto.BookDTO"%>
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
<html>
    <head>
        <!-- JavaScript for buttons-->
      <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
      <script type="text/javascript" src="js/globalVars.js"></script>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Library</title>
        <!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="assets/img/favicon.ico" />
        <!-- Font Awesome icons (free version)-->
        <script src="https://use.fontawesome.com/releases/v5.13.0/js/all.js" crossorigin="anonymous"></script>
        <!-- Google fonts-->
        <link href="https://fonts.googleapis.com/css?family=Merriweather+Sans:400,700" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css?family=Merriweather:400,300,300italic,400italic,700,700italic" rel="stylesheet" type="text/css" />
        <!-- Third party plugin CSS-->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/magnific-popup.js/1.1.0/magnific-popup.min.css" rel="stylesheet" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="css/styles.css" rel="stylesheet" />
    </head>
  
  	<%
  		UserSessionDTO userSession = null;
  	if (session != null) {
  		userSession = (UserSessionDTO) session.getAttribute("userSession");
  	}
  	%>
    <body id="page-top">
      <%-- 	<jsp:include page="/book" /> --%>
        <!-- Navigation-->
        <nav class="navbar navbar-expand-lg navbar-light fixed-top py-3 position-fixed" id="mainNav">
            <div class="container">
                <a class="navbar-brand js-scroll-trigger" href="#page-top">LIBRARY PROJECT</a>
                <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav ml-auto my-2 my-lg-0">
                      <c:choose>
                        <c:when test="${empty userSession}">
                        <li class="nav-item"><a class="nav-link js-scroll-trigger" href="./reader-books.jsp"><fmt:message key="header.home" /></a></li>
<li class="nav-item"><a class="nav-link js-scroll-trigger" href="./login.jsp"><fmt:message key="header.login" /></a></li>
                          <li class="nav-item"><a class="nav-link js-scroll-trigger" href="./register.jsp"><fmt:message key="header.register"/></a></li>
                        </c:when>
                        <c:otherwise>
                          <c:choose>
                            <c:when test="${userSession.roleName == 'READER'}">
                            <li class="nav-item"><a class="nav-link js-scroll-trigger" href=./reader-books.jsp><fmt:message key="header.home"/></a></li>
                            <!-- Find select for bootstrap -->
                              <!-- <select name="nav-menu" id="nav-menu"> -->
                              <li class="nav-item"><a class="nav-link js-scroll-trigger" href="./reader-books.jsp"><fmt:message key="header.reader.books" /></a></li>
                                <li class="nav-item"><a class="nav-link js-scroll-trigger" href="./reader-requests.jsp"><fmt:message key="header.reader.requests" /></a></li>
                              <!-- </select> -->
                            </c:when>
                            <c:when test="${userSession.roleName == 'ADMIN'}">
                            <li class="nav-item"><a class="nav-link js-scroll-trigger" href="./admin-books.jsp"><fmt:message key="header.home"  /></a></li>
                              <!-- <select name="nav-menu" id="nav-menu"> -->
<li class="nav-item"><a class="nav-link js-scroll-trigger" href="./admin-books.jsp"><fmt:message key="header.admin.books" /></a></li>
<li class="nav-item"><a class="nav-link js-scroll-trigger" href="./admin-librarians.jsp"><fmt:message key="header.admin.librarians" /></a></li>
<li class="nav-item"><a class="nav-link js-scroll-trigger" href="./admin-readers.jsp"><fmt:message key="header.admin.users" /></a></li>
                              <!-- </select> -->
                            </c:when>
                            <c:when test="${userSession.roleName == 'LIBRARIAN'}">
                              <li class="nav-item"><a class="nav-link js-scroll-trigger" href="./pending-requests.jsp">Services</a><fmt:message key="header.home" /></li>
                              <!-- <select name="nav-menu" id="nav-menu"> -->
                              <li class="nav-item"><a class="nav-link js-scroll-trigger" href="./pending-requests.jsp"><fmt:message key="header.librarian.pending" /></a></li>
<li class="nav-item"><a class="nav-link js-scroll-trigger" href="./approved-requests.jsp"><fmt:message key="header.librarian.allowed" /></a></li>
                              <!-- </select> -->
                            </c:when>
                          </c:choose>
                          <li class="nav-item"><a class="nav-link js-scroll-trigger" href="./logout"><fmt:message key="header.logout" /></a></li>
                        </c:otherwise>
                      </c:choose>
                      <a class='dropdown-trigger' href='#' data-target='dropdown_lang'> <i class="material-icons">language</i>
                      </a>
                      <ul id='dropdown_lang' class='dropdown-content'>
                        <li id="en">En</li>
                        <li id="ua">Ua</li>
                    </ul>
                </div>
            </div>
        </nav>
        <!-- Masthead-->
        <header class="masthead overflow-auto">
            <div class="container h-100">
                <div class="row h-100 align-items-center justify-content-center text-center ">
                    <div id="media_hidden" class="col-lg-10 align-self-end">
                        <h1 class="text-uppercase text-white font-weight-bold">Where am I</h1>
                        <hr class="divider my-4" />
                    </div>
                    <div  class="container-sm align-self-baseline">
                        <p id="media_hidden" class="text-white-75 font-weight-light mb-5">Here you can do some stuff</p>
                   
                      	<form id="submit_form" action="book-request" method="post">
                      		<input id="submit_book_id" name="bookId" type="hidden"/>
                      		<input id="submit_request_type" name="requestType" type="hidden"/>
                      	</form>
                        <div class="table-responsive table table-fixed ">
                          <table class="table">
                            <thead class="thead-dark">
                              <tr>
                                <th scope="col">#</th>
                                <th scope="col">Heading</th>
                                <th scope="col">Heading</th>
                                <th scope="col">Heading</th>
                                <th scope="col">Heading</th>
                                <th scope="col">Heading</th>
                                <th scope="col">Heading</th>
                                <th scope="col">Heading</th>
                                <th scope="col">Heading</th>
                              </tr>
                            </thead>
                            <tbody>
                              <c:forEach items="${books}" var="book">
                          			<tr>
                                    <th scope="row">1</th>
                          				<td><c:out value="${book.bookId}" /></td>
                          				<td><c:out value="${book.name}" /></td>
                          				<td><c:out value="${book.authorName}" /></td>
                          				<td><c:out value="${book.publisher}" /></td>
                          				<td><c:out value="${book.publishedYear}" /></td>
                          				<td><img src="<c:out value="${book.imgLink}" />" width="150" height="150"></td>
  <c:choose>
    <c:when test="${userSession.roleName == 'READER'}">
                                	<td>
                          					<select class="form-control form-control-sm" id="request_type_${book.bookId}">
                          						<option value="" />
                          						<option value="ABONEMENT"><fmt:message key="request.type.abonement" /></option>
                          						<option value="READING_AREA"><fmt:message key="request.type.reading.area" /></option>
                          					</select>
                          				</td>
                          				<td><button type="button" class="btn btn-warning" onclick="submitRequest(${book.bookId});"/>Success</button></td>
                                    </c:when>
                                  </c:choose>
                          			</tr>
                          		</c:forEach>
                            </tbody>
                          </table>
                        </div>
                      </div>
                        <nav aria-label="Page navigation example">
  <ul class="pagination justify-content-right">
  <c:if test="${offset != 0}">
    <li class="page-item"><a class="page-link" href="./book?offset=${currentPage - 1}">Previous</a></li>
    </c:if>
    <c:forEach begin="0" end="${noOfPages}" var="i">
                <c:choose>
                    <c:when test="${offset eq i}">
                         <li class="page-item active"><a class="page-link" >${i}</a></li>
                    </c:when>
                    <c:otherwise>
                         <li class="page-item"><a class="page-link" href="./book?offset=${i}">${i}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
 <c:if test="${offset lt noOfPages}">
         <li class="page-item"><a class="page-link" href="./book?offset=${offset + 1}">Next</a></li>
    </c:if>
  </ul>
</nav>
                    </div>
            </div>
        </header>
        <!-- Bootstrap core JS-->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"></script>
        <!-- Third party plugin JS-->
        <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min.js"></script> -->
        <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/magnific-popup.js/1.1.0/jquery.magnific-popup.min.js"></script> -->
        <!-- Core theme JS-->
        <!-- <script src="js/scripts.js"></script> -->
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

    		</script>
    </body>
</html>
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
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon.ico" />
<!-- Google fonts-->
<link href="https://fonts.googleapis.com/css?family=Merriweather+Sans:400,700" rel="stylesheet" />
<link href="https://fonts.googleapis.com/css?family=Merriweather:400,300,300italic,400italic,700,700italic" rel="stylesheet" type="text/css" />
<!-- Third party plugin CSS-->
<link href="https://cdnjs.cloudflare.com/ajax/libs/magnific-popup.js/1.1.0/magnific-popup.min.css" rel="stylesheet" />
<!-- Core theme CSS (includes Bootstrap)-->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" />

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://use.fontawesome.com/releases/v5.13.0/js/all.js" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/globalVars.js" type="text/javascript"></script>
</head>
<jsp:include page="header.jsp" />
<!-- Masthead-->
<header class="masthead overflow-hidden">
	<div class="container h-100">
		<div class="row h-100 align-items-center justify-content-center text-center ">
			<div id="media_hidden" class="col-lg-10 align-self-end">
				<h1 class="text-uppercase text-white font-weight-bold">
					<fmt:message key="librarian.page.welcome" />
				</h1>
				<hr class="divider my-4" />
			</div>
			<div class="container-sm align-self-baseline">

				<form id="submit_form" action="./pending-request" method="post">
					<input id="submit_request_id" name="id" type="hidden" /> <input id="submit_allow" name="allow" type="hidden" /> <input id="submit_request_type" name="method" value="put" type="hidden" />
				</form>
				<div class="table-responsive table table-fixed ">
					<table class="table">
						<thead class="thead-dark">
							<tr>
								<th scope="col"><fmt:message key="table.book.id" /></th>
								<th scope="col"><fmt:message key="table.book.username" /></th>
								<th scope="col"><fmt:message key="table.book.name" /></th>
								<th scope="col"><fmt:message key="table.book.type" /></th>
								<th scope="col"><fmt:message key="table.book.createdate" /></th>
								<th scope="col"><fmt:message key="table.librarian.allow" /></th>
								<th scope="col"><fmt:message key="table.librarian.disallow" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${requests}" var="bookRequest">
								<tr>
									<td><c:out value="${bookRequest.requestId}" /></td>
									<td><c:out value="${bookRequest.username}" /></td>
									<td><c:out value="${bookRequest.bookName}" /></td>
									<td><c:out value="${bookRequest.requestType}" /></td>
									<td class="table-column-date"><c:out value="${bookRequest.createDate}" /></td>
									<td><button type="button" class="btn btn-warning" onclick="submitRequest(${bookRequest.requestId}, true);">Allow</button></td>
									<td><button type="button" class="btn btn-warning" onclick="submitRequest(${bookRequest.requestId}, false);">Cancel</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<nav aria-label="Page navigation example">
				<ul class="pagination justify-content-right">
					<c:if test="${offset != 0}">
						<li class="page-item"><a class="page-link" href="/pending-request?offset=${pageSize * currPage - pageSize * 2}"><fmt:message key="pagination.previous" /></a></li>
					</c:if>
					<c:forEach begin="1" end="${noOfPages}" var="i">
						<c:choose>
							<c:when test="${i == currPage}">
								<li class="page-item active"><a class="page-link">${i}</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a class="page-link" href="/pending-request?offset=${i * pageSize - pageSize}">${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${((pageSize * noOfPages) - pageSize) gt offset}">
						<li class="page-item"><a class="page-link" href="/pending-request?offset=${pageSize * currPage}"><fmt:message key="pagination.next" /></a></li>
					</c:if>
				</ul>
			</nav>
		</div>
	</div>
</header>
<script>
    			$(document).ready(function() {

    				chooseNavigationSelect();
    				formatDates();
    				
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
    			function submitRequest(id, allow) {
    				$("#submit_request_id").val(id);
    				$("#submit_allow").val(allow);
    				$("#submit_form").submit();
    		}

    		</script>
</body>
</html>

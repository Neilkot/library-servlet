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
<!-- Favicon-->
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
<script src="${pageContext.request.contextPath}/js/md5.js"></script>
</head>
<jsp:include page="header.jsp" />
<!-- Masthead-->
<header class="masthead overflow-hidden">
	<div class="container h-100">
		<div class="row h-100 align-items-center justify-content-center text-center ">
			<div id="media_hidden" class="col-lg-10 align-self-end">
				<h1 class="text-uppercase text-white font-weight-bold">
					<fmt:message key="header.admin.books" />
				</h1>
				<hr class="divider my-4" />
			</div>
			<div class="container-sm align-self-baseline">

				<form id="submit_delete_form" action="/admin-books" method="post">
					<input id="submit_delete_book_item_id" name="bookItemId" type="hidden" /> <input name="method" type="hidden" value="delete" />
				</form>

				<form id="submit_update_form" action="/admin-books" method="post">
					<input id="submit_update_book_item_id" name="bookItemId" type="hidden" /> <input id="submit_update_book_id" name="bookId" type="hidden" /> <input id="submit_update_book_name" name="bookName"
						type="hidden" /> <input id="submit_update_author_name" name="author_name" type="hidden" /> <input id="submit_update_publisher" name="publisher" type="hidden" /> <input
						id="submit_update_published_year" name="published_year" type="hidden" /> <input id="submit_update_img_link" name="image_link" type="hidden" /> <input name="method" type="hidden"
						value="pre-put" />
				</form>

				<a href="/jsp/admin-add.jsp"><button type="button" class="btn btn-warning">
						<fmt:message key="admin.newbutton" />
					</button></a>
				<div class="table-responsive table table-fixed ">
					<table class="table">
						<thead class="thead-dark">
							<tr>
								<th scope="col"><fmt:message key="table.book.item.id" /></th>
								<th scope="col"><fmt:message key="table.book.id" /></th>
								<th scope="col"><fmt:message key="table.book.name" /></th>
								<th scope="col"><fmt:message key="table.book.author.name" /></th>
								<th scope="col"><fmt:message key="table.book.publisher" /></th>
								<th scope="col"><fmt:message key="table.book.published.year" /></th>
								<th scope="col"><fmt:message key="table.book.image" /></th>
								<th scope="col"><fmt:message key="table.book.action" /></th>
								<th scope="col"><fmt:message key="header.action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${books}" var="book">
								<tr>
									<td><c:out value="${book.bookItemId}" /></td>
									<td><c:out value="${book.bookId}" /></td>
									<td><c:out value="${book.name}" /></td>
									<td><c:out value="${book.authorName}" /></td>
									<td><c:out value="${book.publisher}" /></td>
									<td><c:out value="${book.publishedYear}" /></td>
									<td><img src="<c:out value="${book.imgLink}" />" width="150" height="150"></td>
									<td><select id="request_type_${book.bookItemId}">
											<option value="" />
											<option value="delete"><fmt:message key="admin.books.delete.book" /></option>
											<option value="pre-put"><fmt:message key="admin.books.edit.book" /></option>
									</select></td>
									<td><button type="button" class="btn btn-warning"
											onclick="submitRequest(${book.bookItemId}, ${book.bookId},
									'${book.name}', '${book.authorName}', '${book.publisher}', ${book.publishedYear}, '${book.imgLink}');">
											<fmt:message key="header.submit" />
										</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<nav aria-label="Page navigation example">
				<ul class="pagination justify-content-right">
					<c:if test="${offset != 0}">
						<li class="page-item"><a class="page-link" href="/admin-books?offset=${pageSize * currPage - pageSize * 2}"><fmt:message key="pagination.previous" /></a></li>
					</c:if>
					<c:forEach begin="1" end="${noOfPages}" var="i">
						<c:choose>
							<c:when test="${i == currPage}">
								<li class="page-item active"><a class="page-link">${i}</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a class="page-link" href="/admin-books?offset=${i * pageSize - pageSize}">${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${((pageSize * noOfPages) - pageSize) gt offset}">
						<li class="page-item"><a class="page-link" href="/admin-books?offset=${pageSize * currPage}"><fmt:message key="pagination.next" /></a></li>
					</c:if>
				</ul>
			</nav>
		</div>
	</div>
</header>
<script>
    			function submitRequest(bookItemId, bookId, bookName, authorName, publisher, publishedYear, imgLink) {
    				requestType = $("#request_type_" + bookItemId).val();
    				if (!requestType) {
    					alert("Invalid input. Change this message...")
    				} else {
    					if(requestType == 'pre-put') {
    						$("#submit_update_book_item_id").val(bookItemId);
    						$("#submit_update_book_id").val(bookId);
    						$("#submit_update_book_name").val(bookName);
    						$("#submit_update_author_name").val(authorName);
    						$("#submit_update_publisher").val(publisher);
    						$("#submit_update_published_year").val(publishedYear);
    						$("#submit_update_img_link").val(imgLink);
    						$("#submit_update_form").submit();
    					} else {
    						$("#submit_delete_book_item_id").val(bookItemId);
    						$("#submit_delete_form").submit();
    					}
    				}
    			}
    		</script>
</body>
</html>

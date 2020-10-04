package com.epam.lab.exam.library.controller;

import static com.epam.lab.exam.library.constants.HTTP.ERROR_PAGE;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.lab.exam.library.constants.HTTP;
import com.epam.lab.exam.library.dto.BookDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.service.BookService;
import com.epam.lab.exam.library.service.RoleService;
import com.epam.lab.exam.library.util.Formatter;
import com.epam.lab.exam.library.util.Validator;

@WebServlet("/admin-books")
public class AdminBookController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private final RoleService roleService = RoleService.getInstance();
	private final BookService bookService = BookService.getInstance();

	public AdminBookController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UserSessionDTO userSession = getUserSession(request);
			
			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.ADMIN) {
				logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			Validator.validate(pageSize, offset);
			logger.debug("incoming request parameters. pageSize={} offset={}", pageSize, offset);
			List<BookDTO> books = bookService.getAllBooks(pageSize, offset - 1);
			request.setAttribute(HTTP.ATTRIBUTE_ADMIN_BOOKS_BOOKS, books);
			logger.debug("response.{}={}", HTTP.ATTRIBUTE_ADMIN_BOOKS_BOOKS, books);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (isPutMethod(request)) {
				processBookUpdate(request, response);
			} else if (isDeleteMethod(request)) {
				processBookDelete(request, response);
			} else {
				processBookCreate(request, response);
			}

		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

	private void processBookCreate(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, IOException {
		UserSessionDTO userSession = getUserSession(request);
		
		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		BookDTO bookDto = BookDTO.createBookDTOfrom(request);
		Validator.validate(bookDto);
		bookDto = Formatter.format(bookDto);
		logger.debug("creating book={}", bookDto);
		bookService.createBook(bookDto);
		response.sendRedirect(HTTP.ADMIN_BOOKS_PAGE);
	}

	private void processBookUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, IOException, ServletException {
		UserSessionDTO userSession = getUserSession(request);
		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		BookDTO bookDto = (BookDTO) request.getAttribute("book");
		Validator.validateUpdate(bookDto);
		logger.debug("updating book={}", bookDto);
		bookService.updateBook(bookDto);
		response.sendRedirect(HTTP.ADMIN_BOOKS_PAGE);
	}

	private void processBookDelete(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, IOException, ServletException {
		UserSessionDTO userSession = getUserSession(request);
		
		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		Integer bookItemId = parseIntegerParameter(request, "bookItemId");
		logger.info("deleting bookItem with id={}", bookItemId);
		bookService.deleteBookItem(bookItemId);
		response.sendRedirect(HTTP.ADMIN_BOOKS_PAGE);
	}

}

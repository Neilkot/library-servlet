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
import com.epam.lab.exam.library.dto.CreateBookDTO;
import com.epam.lab.exam.library.dto.UpdateBookDto;
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
			logger.info("loading books page");
			UserSessionDTO userSession = getUserSession(request);

			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.ADMIN) {
				logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			logger.info("pageSize={} offset={}", pageSize, offset);
			Validator.validate(pageSize, offset);

			int noOfRecords = bookService.getAllBooksCount();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
			int currPage = offset / pageSize + 1;
			logger.info("noOfRecords={} noOfPages={} currPage={}", noOfRecords, noOfPages, currPage);

			List<CreateBookDTO> books = bookService.getAllBooks(pageSize, offset);

			request.setAttribute("pageSize", pageSize);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("offset", offset);
			request.setAttribute("currPage", currPage);
			
			request.setAttribute(HTTP.ATTRIBUTE_ADMIN_BOOKS_BOOKS, books);
			logger.info("returning books {}", books);

			request.getRequestDispatcher("/jsp/admin-books.jsp").forward(request, response);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (isPrePutMethod(request)) {
				processBookPreUpdate(request, response);
			} else if (isPutMethod(request)) {
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
			throws ClientRequestException, SQLException, IOException, ServletException {
		logger.info("creating book");
		UserSessionDTO userSession = getUserSession(request);

		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		CreateBookDTO bookDto = CreateBookDTO.createBookDTOfrom(request);
		logger.info("user input={}", bookDto);
		Validator.validate(bookDto);
		bookDto = Formatter.format(bookDto);
		bookService.createBook(bookDto);
		logger.info("book created");
		
		response.sendRedirect(HTTP.ADMIN_BOOKS_PAGE);
	}

	private void processBookPreUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, IOException, ServletException {
		logger.info("pre-updating book");
		UserSessionDTO userSession = getUserSession(request);
		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		UpdateBookDto bookDto = UpdateBookDto.createBookDTOfrom(request);
		logger.info("user input {}", bookDto);
		Validator.validateUpdate(bookDto);
		
		request.setAttribute("book", bookDto);
		String forwardPage = "/jsp/admin-update.jsp";
		logger.info("forwarding to book update page {}", forwardPage);
		
		request.getRequestDispatcher(forwardPage).forward(request, response);
	}

	private void processBookUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, IOException, ServletException {
		logger.info("updating book");
		UserSessionDTO userSession = getUserSession(request);
		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		UpdateBookDto bookDto = UpdateBookDto.createBookDTOfrom(request);
		logger.info("user input {}", bookDto);
		Validator.validateUpdate(bookDto);
		
		bookService.updateBook(bookDto);
		logger.info("book updated");
		
		response.sendRedirect(HTTP.ADMIN_BOOKS_PAGE);
	}

	private void processBookDelete(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, IOException, ServletException {
		logger.info("deleting book");
		UserSessionDTO userSession = getUserSession(request);
		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		Integer bookItemId = parseIntegerParameter(request, "bookItemId");
		logger.info("user input {}", bookItemId);
		
		bookService.deleteBookItem(bookItemId);
		logger.info("book deleted");
		
		response.sendRedirect(HTTP.ADMIN_BOOKS_PAGE);
	}

}

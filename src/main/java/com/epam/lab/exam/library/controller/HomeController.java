package com.epam.lab.exam.library.controller;

import static com.epam.lab.exam.library.constants.HTTP.ADMIN_BOOKS_PAGE;
import static com.epam.lab.exam.library.constants.HTTP.ERROR_PAGE;
import static com.epam.lab.exam.library.constants.HTTP.PENDING_REQUESTS_PAGE;
import static com.epam.lab.exam.library.constants.HTTP.READER_BOOKS_PAGE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.RoleType;

@WebServlet("/home")
public class HomeController extends AbstractController {
	private static final long serialVersionUID = 1L;

	public HomeController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UserSessionDTO userSession = getOptionalUserSession(request);
			RoleType roleType = userSession != null ? RoleType.valueOf(userSession.getRoleName()) : RoleType.READER;
			String homePage = getDefaultPage(roleType);
			logger.info("returning home page {}", homePage);
			response.sendRedirect(homePage);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

	private String getDefaultPage(RoleType roleType) {
		switch (roleType) {
		case ADMIN:
			return ADMIN_BOOKS_PAGE;
		case LIBRARIAN:
			return PENDING_REQUESTS_PAGE;
		case READER:
			return READER_BOOKS_PAGE;
		default:
			throw new IllegalArgumentException("Not supported RoleType: " + roleType);
		}
	}
}

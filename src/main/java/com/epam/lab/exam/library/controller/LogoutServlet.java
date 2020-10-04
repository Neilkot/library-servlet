package com.epam.lab.exam.library.controller;

import static com.epam.lab.exam.library.constants.HTTP.ERROR_PAGE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.lab.exam.library.constants.HTTP;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;

@WebServlet("/logout")
public class LogoutServlet extends AbstractController {
	private static final long serialVersionUID = 1L;

	public LogoutServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			getUserSession(request);
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
				logger.debug("Invalidating session");
			}
			request.getRequestDispatcher(HTTP.READER_BOOKS_PAGE).forward(request, response);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}
}

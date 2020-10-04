package com.epam.lab.exam.library.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.constants.HTTP;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.service.ConfigService;

@SuppressWarnings("serial")
public abstract class AbstractController extends HttpServlet {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	private final ConfigService configService = ConfigService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleError(req, resp, HTTP.ERROR_PAGE, ErrorType.METHOD_NOT_ALLOWED, null);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleError(req, resp, HTTP.ERROR_PAGE, ErrorType.METHOD_NOT_ALLOWED, null);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleError(req, resp, HTTP.ERROR_PAGE, ErrorType.METHOD_NOT_ALLOWED, null);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleError(req, resp, HTTP.ERROR_PAGE, ErrorType.METHOD_NOT_ALLOWED, null);
	}

	protected void handleError(HttpServletRequest req, HttpServletResponse resp, String errorPage, ErrorType errorType,
			Exception e) throws ServletException, IOException {
		String locale = (String) req.getSession().getAttribute("localeName");
		if (locale == null) {
			locale = configService.getDefaultLocale();
		}
		String errorMessage = configService.getPropertyValue(locale, errorType.getMessageCode());
		int errorCode = errorType.getErrorCode();
		req.setAttribute("errorMessage", errorMessage);
		req.setAttribute("errorCode", errorCode);

		if (e != null) {
			if (errorCode < 500) {
				logger.debug("HTTP client error. Redirecting to error page. code={} message={}", errorCode,
						errorMessage);
			} else {
				logger.error("Server error. Redirecting client to error page. code=" + errorCode + " message="
						+ errorMessage + " cause=" + e.getMessage(), e);
			}
		}
		req.getRequestDispatcher(errorPage).forward(req, resp);
	}

	protected String getPath(HttpServletRequest req) {
		return null;
	}

	protected UserSessionDTO getUserSession(HttpServletRequest req) throws ClientRequestException {
		HttpSession session = req.getSession(false);
		if (session == null) {
			logger.debug("Unauthorized client request. No http session found");
			throw new ClientRequestException(ErrorType.UNAUTHORIZED);
		}
		return (UserSessionDTO) session.getAttribute("userSession");
	}

	// TODO what this for?
	protected UserSessionDTO getOptionalUserSession(HttpServletRequest req) throws ClientRequestException {
		return Optional.ofNullable(req.getSession(false)).map(s -> (UserSessionDTO) s.getAttribute("userSession"))
				.orElse(null);
	}

	protected String parseStringParameter(HttpServletRequest req, String name) throws ClientRequestException {
		String param = req.getParameter(name);
		if (param == null) {
			logger.debug("Missing required request parameter: {}", name);
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		return param;
	}

	protected Integer parseIntegerParameter(HttpServletRequest req, String name) throws ClientRequestException {
		String param = req.getParameter(name);
		if (param == null) {
			logger.debug("Missing required request parameter: {}", name);
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		try {
			return Integer.parseInt(param);
		} catch (NumberFormatException e) {
			logger.debug("Invalid required request parameter: {} param={}", name, param);
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
	}

	protected Boolean parseBooleanParameter(HttpServletRequest req, String name) throws ClientRequestException {
		String param = req.getParameter(name);
		if (param == null || (!param.trim().equalsIgnoreCase("true") && !param.trim().equalsIgnoreCase("false"))) {
			logger.debug("Invalid required request parameter: {} param={}", name, param);
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		return Boolean.valueOf(param);
	}

	protected Integer parsePageSizeParameter(HttpServletRequest req) throws ClientRequestException {
		return Optional.ofNullable(req.getParameter("pageSize")).map(Integer::parseInt).orElse(2);
	}

	protected Integer parseOffsetParameter(HttpServletRequest req) throws ClientRequestException {
		return Optional.ofNullable(req.getParameter("offset")).map(Integer::parseInt).orElse(1);
	}

	protected boolean isPutMethod(HttpServletRequest req) {
		return Optional.ofNullable(req.getParameter("method")).filter(m -> m.equalsIgnoreCase("put")).isPresent();
	}

	protected boolean isDeleteMethod(HttpServletRequest req) {
		return Optional.ofNullable(req.getParameter("method")).filter(m -> m.equalsIgnoreCase("delete")).isPresent();
	}
}

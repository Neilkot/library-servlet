package com.epam.lab.exam.library.controller;

import static com.epam.lab.exam.library.constants.HTTP.ERROR_PAGE;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.lab.exam.library.constants.HTTP;
import com.epam.lab.exam.library.dto.PendingRequestDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.service.BookRequestService;
import com.epam.lab.exam.library.service.RoleService;
import com.epam.lab.exam.library.util.Validator;

@WebServlet("/pending-request")
public class PendingRequestController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private final RoleService roleService = RoleService.getInstance();
	private final BookRequestService bookRequestService = BookRequestService.getInstance();

	public PendingRequestController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.info("incide pending requests cotroller");
			UserSessionDTO userSession = getUserSession(request);
			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.LIBRARIAN) {
				logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			logger.info("loading books page. pageSize={} offset={}", pageSize, offset);

			Validator.validate(pageSize, offset);
			int noOfRecords = bookRequestService.getPendinCount();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
			logger.debug("incoming request parameters. pageSize={} offset={}", pageSize, offset);
			
			List<PendingRequestDTO> requests = bookRequestService.getPendingNonBlockedReaderRequests(pageSize, offset - 1);
			request.setAttribute("noOfRecords", noOfPages);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("offset", offset);
			request.setAttribute(HTTP.ATTRIBUTE_PENDINGREQUESTCONTROLLER_REQUESTS, requests);
			logger.debug("response.{}={}", HTTP.ATTRIBUTE_PENDINGREQUESTCONTROLLER_REQUESTS, requests);
			request.getRequestDispatcher("/jsp/pending-requests.jsp").forward(request, response);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UserSessionDTO userSession = getUserSession(request);
			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.LIBRARIAN) {
				logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			
			Integer id = parseIntegerParameter(request, "id");
			if (id == null || id < 0) {
				logger.info("Invalid parametr id={}", id);
				throw new ClientRequestException(ErrorType.BAD_REQUEST);
			}

			Boolean allow = parseBooleanParameter(request, "allow");
			if (allow) {
				bookRequestService.approve(id);
			} else {
				bookRequestService.delete(id);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			logger.info("loading books page. pageSize={} offset={}", pageSize, offset);

			Validator.validate(pageSize, offset);
			int noOfRecords = bookRequestService.getPendinCount();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
			List<PendingRequestDTO> requests = bookRequestService.getPendingNonBlockedReaderRequests(pageSize, offset);
			request.setAttribute("noOfRecords", noOfPages);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("offset", offset);
			request.setAttribute(HTTP.ATTRIBUTE_PENDINGREQUESTCONTROLLER_REQUESTS, requests);
			logger.debug("response.{}={}", HTTP.ATTRIBUTE_PENDINGREQUESTCONTROLLER_REQUESTS, requests);
			request.getRequestDispatcher("/jsp/pending-requests.jsp").forward(request, response);
			
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

}

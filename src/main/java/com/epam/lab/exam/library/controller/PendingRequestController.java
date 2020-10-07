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
			logger.info("loading pending requests page");
			UserSessionDTO userSession = getUserSession(request);
			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.LIBRARIAN) {
				logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			logger.info("user input: pageSize={} offset={}", pageSize, offset);

			Validator.validate(pageSize, offset);
			int noOfRecords = bookRequestService.getPendinCount();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
			int currPage = offset / pageSize + 1;
			logger.info("noOfRecords={} noOfPages={} currPage={}", noOfRecords, noOfPages, currPage);

			List<PendingRequestDTO> requests = bookRequestService.getPendingNonBlockedReaderRequests(pageSize, offset);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("offset", offset);
			request.setAttribute("currPage", currPage);
			request.setAttribute(HTTP.ATTRIBUTE_PENDINGREQUESTCONTROLLER_REQUESTS, requests);
			logger.info("returning pending requests {}", requests);

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
			logger.info("performing approve/cancel request action");
			UserSessionDTO userSession = getUserSession(request);
			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.LIBRARIAN) {
				logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}

			Integer id = parseIntegerParameter(request, "id");
			if (id == null || id < 0) {
				logger.info("Invalid parametr id={}", id);
				throw new ClientRequestException(ErrorType.BAD_REQUEST);
			}

			Boolean allow = parseBooleanParameter(request, "allow");
			logger.info("user input: requestId={} doApprove={}", id, allow);

			if (allow) {
				bookRequestService.approve(id);
			} else {
				bookRequestService.delete(id);
			}
			response.sendRedirect(HTTP.PENDING_REQUESTS_PAGE);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

}

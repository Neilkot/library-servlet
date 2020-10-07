package com.epam.lab.exam.library.controller;

import static com.epam.lab.exam.library.constants.HTTP.ERROR_PAGE;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.lab.exam.library.constants.HTTP;
import com.epam.lab.exam.library.dto.BookRequestDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.service.BookRequestService;
import com.epam.lab.exam.library.service.RequestExpirationService;
import com.epam.lab.exam.library.service.RoleService;
import com.epam.lab.exam.library.util.Validator;

@WebServlet("/approved-request")
public class ApprovedRequestController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private final RoleService roleService = RoleService.getInstance();
	private final BookRequestService bookRequestService = BookRequestService.getInstance();
	private final RequestExpirationService requestExpirationService = RequestExpirationService.getInstance();

	public ApprovedRequestController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.info("loading approved requests page");
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
			int noOfRecords = bookRequestService.getCountApprovedRequests();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
			int currPage = offset / pageSize + 1;
			logger.info("noOfRecords={} noOfPages={} currPage={}", noOfRecords, noOfPages, currPage);


			List<BookRequestDTO> requests = bookRequestService.getApprovedRequests(pageSize, offset);
			requestExpirationService.applyFee(requests);

			request.setAttribute(HTTP.ATTRIBUTE_LIBRARIANS_APPROVED_REQUESTS_REQUESTS, requests);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("offset", offset);
			request.setAttribute("currPage", currPage);
			request.setAttribute("location", "approved-request");

			logger.info("returning approved requests {}", requests);
			request.getRequestDispatcher("/jsp/reader-requests.jsp").forward(request, response);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.info("returning book");
			UserSessionDTO userSession = getUserSession(request);
			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.LIBRARIAN) {
				logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer bookRequestId = parseIntegerParameter(request, "bookRequestId");
			logger.info("user input: {}", bookRequestId);
			if (bookRequestId < 0) {
				logger.info("Wrong parametr for bookRequestId={}", bookRequestId);
				throw new ClientRequestException(ErrorType.BAD_REQUEST);
			}

			bookRequestService.returnBook(bookRequestId);
			logger.info("Book returned");

			response.sendRedirect(HTTP.APPROVED_REQUESTS_PAGE);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

}

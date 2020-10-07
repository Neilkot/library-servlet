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
import com.epam.lab.exam.library.dto.SubmitRequestDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.service.BookRequestService;
import com.epam.lab.exam.library.service.RequestExpirationService;
import com.epam.lab.exam.library.service.RoleService;
import com.epam.lab.exam.library.util.Validator;

@WebServlet("/reader-requests")
public class BookRequestController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private final RoleService roleService = RoleService.getInstance();
	private final BookRequestService bookRequestService = BookRequestService.getInstance();
	private final RequestExpirationService requestExpirationService = RequestExpirationService.getInstance();

	public BookRequestController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.info("loading reader requests page");
			UserSessionDTO userSession = getUserSession(request);

			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.READER) {
				logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			logger.info("user input: pageSize={} offset={}", pageSize, offset);

			Validator.validate(pageSize, offset);
			int noOfRecords = bookRequestService.getUserApprovedRequestsCount(userSession.getUserId());
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
			int currPage = offset / pageSize + 1;
			logger.info("noOfRecords={} noOfPages={} currPage={}", noOfRecords, noOfPages, currPage);


			List<BookRequestDTO> userApprovedRequests = bookRequestService
					.getUserApprovedRequests(userSession.getUserId(), pageSize, offset);
			requestExpirationService.applyFee(userApprovedRequests);

			request.setAttribute("pageSize", pageSize);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("offset", offset);
			request.setAttribute("currPage", currPage);
			request.setAttribute("location", "reader-requests");
			request.setAttribute(HTTP.ATTRIBUTE_READER_READER_REQUEST_REQUESTS, userApprovedRequests);
			logger.info("returning requests {}", userApprovedRequests);

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
			logger.info("submitting book request");
			UserSessionDTO userSession = getUserSession(request);

			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.READER) {
				logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			
			SubmitRequestDTO dto = SubmitRequestDTO.from(request);
			logger.info("user input: {}", dto);
			Validator.validate(dto);

			bookRequestService.submitRequest(userSession.getUserId(), dto);
			logger.info("request submitted");

			response.sendRedirect(HTTP.READER_BOOKS_PAGE);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

}

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

@WebServlet("/book-request")
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
			UserSessionDTO userSession = getUserSession(request);

			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.READER) {
				logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			Validator.validate(pageSize, offset);
			logger.debug("incoming request parameters. pageSize={} offset={}", pageSize, offset);
			logger.debug("userUd:{}", userSession.getUserId());
			List<BookRequestDTO> userApprovedRequests = bookRequestService
					.getUserApprovedRequests(userSession.getUserId(), pageSize, offset);
			requestExpirationService.applyFee(userApprovedRequests);
			request.setAttribute(HTTP.ATTRIBUTE_READER_READER_REQUEST_REQUESTS, userApprovedRequests);
			logger.debug("response.{}={}", HTTP.ATTRIBUTE_READER_READER_REQUEST_REQUESTS, userApprovedRequests);
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
			if (role.getType() != RoleType.READER) {
				logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			SubmitRequestDTO dto = SubmitRequestDTO.from(request);
			Validator.validate(dto);
			logger.debug("user id:{}", userSession.getUserId());
			bookRequestService.submitRequest(userSession.getUserId(), dto);
			logger.debug("Submitted user request={}", dto);
			response.sendRedirect(HTTP.READER_BOOKS_PAGE);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}

	}

}

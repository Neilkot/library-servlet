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
import com.epam.lab.exam.library.dto.CreateUserDTO;
import com.epam.lab.exam.library.dto.UserDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;
import com.epam.lab.exam.library.service.RoleService;
import com.epam.lab.exam.library.service.UserService;
import com.epam.lab.exam.library.util.Formatter;
import com.epam.lab.exam.library.util.Validator;

@WebServlet("/reader")
public class ReaderController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private final RoleService roleService = RoleService.getInstance();
	private final UserService userService = UserService.getInstance();

	public ReaderController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.info("loading readers page");
			UserSessionDTO userSession = getUserSession(request);

			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.ADMIN) {
				logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			logger.info("user input: pageSize={} offset={}", pageSize, offset);

			Validator.validate(pageSize, offset);
			int noOfRecords = userService.countReaders();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
			int currPage = offset / pageSize + 1;
			logger.info("noOfRecords={} noOfPages={} currPage={}", noOfRecords, noOfPages, currPage);


			List<UserDTO> readers = userService.getReaders(pageSize, offset);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("offset", offset);
			request.setAttribute("currPage", currPage);
			request.setAttribute(HTTP.ATTRIBUTE_ADMIN_READERS_READERS, readers);
			logger.info("returning readers {}", readers);

			request.getRequestDispatcher("/jsp/admin-readers.jsp").forward(request, response);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (isPutMethod(request)) {
				processReaderChangeIsBlocked(request, response);
			} else {
				processReaderCreation(request, response);
			}
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

	private void processReaderCreation(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, ServletException, IOException {
		logger.info("Processing reader registration");
		UserSessionDTO userSession = getOptionalUserSession(request);

		CreateUserDTO userDto = CreateUserDTO.from(request);
		logger.info("Registration form: " + userDto);
		Validator.validate(userDto);
		CreateUserDTO formatted = Formatter.format(userDto);
		User reader = userService.createReader(formatted.getLogin(), formatted.getChecksum(), formatted.getFirstName(),
				formatted.getLastName());

		userSession = new UserSessionDTO();
		userSession.setUsername(reader.getFirstName() + " " + reader.getLastName());
		userSession.setRoleId(reader.getRoleId());
		userSession.setRoleName(RoleType.READER.toString());
		userSession.setUserId(reader.getId());

		request.getSession().setAttribute(HTTP.ATTRIBUTE_READERCONTROLLER_USERSESSION, userSession);
		logger.info("response.{}={}", HTTP.ATTRIBUTE_READERCONTROLLER_USERSESSION, userSession);
		response.sendRedirect(HTTP.READER_BOOKS_PAGE);
	}

	private void processReaderChangeIsBlocked(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClientRequestException, SQLException {
		logger.info("changing reader isBlocked status");
		UserSessionDTO userSession = getUserSession(request);

		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}

		Integer readerId = parseIntegerParameter(request, "readerId");
		if (readerId < 0) {
			logger.info("Invalid parametr readerId={}", readerId);
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		User user = userService.getUser(readerId);

		Boolean newStatus = !user.getIsBlocked();
		logger.info("user input: readerId={} isBlocked={}", readerId, newStatus);

		userService.updateIsBlocked(readerId, newStatus);
		logger.info("reader status changed");

		response.sendRedirect(HTTP.ADMIN_READERS_PAGE);
	}
}

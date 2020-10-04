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
import com.epam.lab.exam.library.service.RoleService;
import com.epam.lab.exam.library.service.UserService;
import com.epam.lab.exam.library.util.Formatter;
import com.epam.lab.exam.library.util.Validator;

@WebServlet("/librarian")
public class AdminLibrarianController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private final RoleService roleService = RoleService.getInstance();
	private final UserService userService = UserService.getInstance();

	public AdminLibrarianController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UserSessionDTO userSession = getUserSession(request);
			
			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.ADMIN) {
				logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			logger.debug("incoming request parameters. pageSize={} offset={}", pageSize, offset);

			Validator.validate(pageSize, offset);
			List<UserDTO> librarians = userService.getLibrarians(pageSize, offset);
			request.setAttribute(HTTP.ATTRIBUTE_ADMIN_LIBRARIANS_LIBRARIANS, librarians);

			logger.debug("response.{}={}", HTTP.ATTRIBUTE_ADMIN_LIBRARIANS_LIBRARIANS, librarians);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (isDeleteMethod(request)) {
				processLibrarianDelete(request, response);
			} else {
				processLibrarianCreate(request, response);
			}

		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

	private void processLibrarianCreate(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, ServletException, IOException {
		UserSessionDTO userSession = getUserSession(request);
		
		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		CreateUserDTO userDto = CreateUserDTO.from(request);
		Validator.validate(userDto);
		CreateUserDTO formatted = Formatter.format(userDto);
		logger.debug("Creating librarian={}",formatted );
		userService.createLibrarian(formatted.getLogin(), formatted.getChecksum(), formatted.getFirstName(),
				formatted.getLastName());
		response.sendRedirect(HTTP.ADMIN_LIBRARIANS_PAGE);

	}

	private void processLibrarianDelete(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, ServletException, IOException {
		UserSessionDTO userSession = getUserSession(request);
		
		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.debug("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		Integer id = parseIntegerParameter(request, "id");
		logger.debug("Deleting user ba Id={}", id);
		userService.deleteUser(id);
		response.sendRedirect(HTTP.ADMIN_LIBRARIANS_PAGE);
	}

}

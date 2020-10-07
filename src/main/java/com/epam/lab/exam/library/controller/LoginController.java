package com.epam.lab.exam.library.controller;

import static com.epam.lab.exam.library.constants.HTTP.ERROR_PAGE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.lab.exam.library.constants.HTTP;
import com.epam.lab.exam.library.dto.LoginDTO;
import com.epam.lab.exam.library.dto.UserSessionDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.User;
import com.epam.lab.exam.library.service.RoleService;
import com.epam.lab.exam.library.service.UserService;
import com.epam.lab.exam.library.util.Formatter;
import com.epam.lab.exam.library.util.Validator;

@WebServlet("/login")
public class LoginController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private final UserService userService = UserService.getInstance();
	private final RoleService roleService = RoleService.getInstance();

	public LoginController() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.info("user logging in");

			LoginDTO login = LoginDTO.from(request);
			logger.info("user input: {}", login);
			Validator.validate(login);
			LoginDTO formatted = Formatter.format(login);

			User user = userService.authenticateUser(formatted.getLogin(), formatted.getChecksum());
			if (user == null) {
				logger.info("User not found={}", user);
				throw new ClientRequestException(ErrorType.USER_NOT_FOUND);
			}
			UserSessionDTO userSession = getOptionalUserSession(request);
			if (userSession != null) {
				logger.info("Existing session: " + userSession);
				throw new ClientRequestException(ErrorType.ALREADY_LOGGED_IN);
			}

			Role role = roleService.getRole(user.getRoleId());
			userSession = new UserSessionDTO();
			userSession.setUserId(user.getId());
			userSession.setUsername(user.getFirstName() + " " + user.getLastName());
			userSession.setRoleId(user.getRoleId());
			userSession.setRoleName(role.getType().toString());

			request.getSession().setAttribute(HTTP.ATTRIBUTE_LOGINCONTROLLER_USERSESSION, userSession);
			logger.info("user logged in: {}", userSession);

			response.sendRedirect("/home");
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}
}

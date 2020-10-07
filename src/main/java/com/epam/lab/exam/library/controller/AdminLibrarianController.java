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

@WebServlet("/admin-librarians")
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
			logger.info("loading librarians page");
			UserSessionDTO userSession = getUserSession(request);

			Role role = roleService.getRole(userSession.getRoleId());
			if (role.getType() != RoleType.ADMIN) {
				logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
						userSession.getUserId(), userSession.getUsername());
				throw new ClientRequestException(ErrorType.FORBIDDEN);
			}
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			logger.info("pageSize={} offset={}", pageSize, offset);

			Validator.validate(pageSize, offset);
			int noOfRecords = userService.countLibrarians();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
			int currPage = offset / pageSize + 1;
			logger.info("noOfRecords={} noOfPages={} currPage={}", noOfRecords, noOfPages, currPage);


			List<UserDTO> librarians = userService.getLibrarians(pageSize, offset);
			request.setAttribute(HTTP.ATTRIBUTE_ADMIN_LIBRARIANS_LIBRARIANS, librarians);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("offset", offset);
			request.setAttribute("currPage", currPage);
			
			logger.info("returning librarians {}", librarians);

			request.getRequestDispatcher("/jsp/admin-librarians.jsp").forward(request, response);
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
		logger.info("creating librarian");
		UserSessionDTO userSession = getUserSession(request);

		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		CreateUserDTO userDto = CreateUserDTO.from(request);
		logger.info("user input {}", userDto);
		Validator.validate(userDto);
		CreateUserDTO formatted = Formatter.format(userDto);

		User librarian = userService.createLibrarian(formatted.getLogin(), formatted.getChecksum(), formatted.getFirstName(),
				formatted.getLastName());
		logger.info("librarian created {}", librarian);
		
		response.sendRedirect(HTTP.ADMIN_LIBRARIANS_PAGE);

	}

	private void processLibrarianDelete(HttpServletRequest request, HttpServletResponse response)
			throws ClientRequestException, SQLException, ServletException, IOException {
		logger.info("deleting librarian");
		UserSessionDTO userSession = getUserSession(request);

		Role role = roleService.getRole(userSession.getRoleId());
		if (role.getType() != RoleType.ADMIN) {
			logger.info("Forbidden user operation. userRole={} userId={} username={}", role.getType(),
					userSession.getUserId(), userSession.getUsername());
			throw new ClientRequestException(ErrorType.FORBIDDEN);
		}
		Integer id = parseIntegerParameter(request, "id");
		logger.info("user input: {}", id);
		
		userService.deleteUser(id);
		logger.info("librarian deleted");
		
		response.sendRedirect(HTTP.ADMIN_LIBRARIANS_PAGE);
	}

}

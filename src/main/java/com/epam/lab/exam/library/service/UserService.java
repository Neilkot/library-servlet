package com.epam.lab.exam.library.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.dao.UserDao;
import com.epam.lab.exam.library.db.DBManager;
import com.epam.lab.exam.library.db.DBManagerContainer;
import com.epam.lab.exam.library.dto.UserDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

public class UserService {

	private static final UserService INSTANCE = new UserService();

	private final DBManager dbManager = DBManagerContainer.getInstance().getdBManager();
	private final UserDao userDao = UserDao.getInstance();
	private final RoleService roleService = RoleService.getInstance();

	private final Logger logger = LogManager.getLogger(this.getClass());

	private UserService() {
	}

	public static UserService getInstance() {
		return INSTANCE;
	}

	public User authenticateUser(String login, String checksum) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return Optional.ofNullable(userDao.getByLogin(connection, login))
					.filter(u -> u.getChecksum().equalsIgnoreCase(checksum)).orElse(null);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public User createReader(String login, String checksum, String firstName, String lastName)
			throws SQLException, ClientRequestException {
		return createUser(login, checksum, firstName, lastName, roleService.getReaderRole().getId());
	}

	public User createLibrarian(String login, String checksum, String firstName, String lastName)
			throws SQLException, ClientRequestException {
		return createUser(login, checksum, firstName, lastName, roleService.getLibrarianRole().getId());
	}

	public void updateIsBlocked(Integer id, boolean isBlocked) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			userDao.updateIsBlocked(connection, id, isBlocked);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public List<UserDTO> getLibrarians(int pageSize, int offset) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return userDao.getByRole(connection, pageSize, offset, roleService.getLibrarianRole().getId()).stream()
					.map(UserDTO::from).collect(Collectors.toList());
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public List<UserDTO> getReaders(int pageSize, int offset) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			Integer roleId = roleService.getReaderRole().getId();
			return userDao.getByRole(connection, pageSize, offset, roleId).stream().map(UserDTO::from)
					.collect(Collectors.toList());
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	private User createUser(String login, String checksum, String firstName, String lastName, Integer roleId)
			throws SQLException, ClientRequestException {
		Connection connection = dbManager.getConnection();
		User existingUser = userDao.getByLogin(connection, login);
		if (existingUser != null) {
			logger.info("Can't create user for already existing login={}", login);
			throw new ClientRequestException(ErrorType.LOGIN_IN_USE);
		}
		User user = new User();
		user.setLogin(login);
		user.setChecksum(checksum);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setRoleId(roleId);
		user.setIsBlocked(false);
		user.setId(userDao.create(connection, user));
		return user;
	}

	public void deleteUser(Integer id) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			userDao.delete(connection, id);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public User getUser(Integer id) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return userDao.read(connection, id);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public int countLibrarians() throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return userDao.countUsers(connection, RoleType.LIBRARIAN);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public int countReaders() throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return userDao.countUsers(connection, RoleType.READER);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

}

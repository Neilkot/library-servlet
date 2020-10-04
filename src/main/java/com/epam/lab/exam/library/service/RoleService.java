package com.epam.lab.exam.library.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.epam.lab.exam.library.dao.RoleDao;
import com.epam.lab.exam.library.db.DBManager;
import com.epam.lab.exam.library.db.DBManagerContainer;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;

public class RoleService {

	private static final RoleService INSTANCE = new RoleService();

	private final DBManager dbManager = DBManagerContainer.getInstance().getdBManager();
	private final RoleDao roleDao = RoleDao.getInstance();

	private RoleService() {
	}

	public static RoleService getInstance() {
		return INSTANCE;
	}

	public Role getReaderRole() throws SQLException {
		return getRoleByType(RoleType.READER);

	}

	public Role getLibrarianRole() throws SQLException {
		return getRoleByType(RoleType.LIBRARIAN);

	}

	public Role getRole(Integer id) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return roleDao.read(connection, id);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	private Role getRoleByType(RoleType type) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return roleDao.getByType(connection, type);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

}

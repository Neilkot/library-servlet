package com.epam.lab.exam.library.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.epam.lab.exam.library.dao.BookRequestTypeDao;
import com.epam.lab.exam.library.db.DBManager;
import com.epam.lab.exam.library.db.DBManagerContainer;
import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;

public class BookRequestTypeService {

	private static final BookRequestTypeService INSTANCE = new BookRequestTypeService();

	private final DBManager dbManager = DBManagerContainer.getInstance().getdBManager();
	private final BookRequestTypeDao bookRequestTypeDao = BookRequestTypeDao.getInstance();

	private BookRequestTypeService() {
	}

	public static BookRequestTypeService getInstance() {
		return INSTANCE;
	}

	public BookRequestType getByType(RequestType type) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookRequestTypeDao.getByType(connection, type.toString());
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public BookRequestType getById(Integer id) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookRequestTypeDao.read(connection, id);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}
	
}

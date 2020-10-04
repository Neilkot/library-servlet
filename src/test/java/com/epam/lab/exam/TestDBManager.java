package com.epam.lab.exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.epam.lab.exam.library.db.DBManager;

public class TestDBManager implements DBManager {

	private String connectionUrl;

	public TestDBManager(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(connectionUrl);
	}

	@Override
	public void releaseConnection(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}
}

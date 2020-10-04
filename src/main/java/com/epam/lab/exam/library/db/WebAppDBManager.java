package com.epam.lab.exam.library.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class WebAppDBManager implements DBManager {

	private DataSource dataSource;

	public WebAppDBManager() throws NamingException {
		Context context = (Context) new InitialContext().lookup("java:/comp/env");
		dataSource = (DataSource) context.lookup("jdbc/mysql");
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void releaseConnection(Connection connection) throws SQLException {
		connection.close();
	}
}

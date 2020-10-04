package com.epam.lab.exam.library.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBManager {
	
	Connection getConnection() throws SQLException;
	void releaseConnection( Connection connection) throws SQLException;
}

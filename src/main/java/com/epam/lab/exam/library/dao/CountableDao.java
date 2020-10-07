package com.epam.lab.exam.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface CountableDao {

	//TODO I made logger inside a method. Learn if it is Ok?
	default int count(Connection connection) throws SQLException {
		Logger logger = LogManager.getLogger(CountableDao.class.getName());
		String sql = String.format("SELECT COUNT(*) from %s;", getTableName());
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return count;
		}
	}

	String getTableName();
}

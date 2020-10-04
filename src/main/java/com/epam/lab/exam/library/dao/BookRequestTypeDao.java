package com.epam.lab.exam.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.constants.DB;
import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;

public class BookRequestTypeDao implements Dao<BookRequestType, Integer> {

	private static final BookRequestTypeDao INSTANCE = new BookRequestTypeDao();
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	private BookRequestTypeDao() {

	}

	public static BookRequestTypeDao getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer create(Connection connection, BookRequestType element) throws SQLException {
		String sql = String.format("INSERT INTO %s (%s) VALUES(?);", DB.TABLE_BOOK_REQUEST_TYPE,
				DB.BOOK_REQUEST_TYPE_TYPE);
		logger.debug("Executing sql query: {}", sql); 	
		try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, element.getType().toString());
			pst.executeUpdate();

			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					logger.warn("Error creating bookRequestJournal. Prepared statement returned zero rows modified");
					throw new SQLException("Couldn't create BookRequestType entity");
				}
			}
		}
	}

	@Override
	public BookRequestType read(Connection connection, Integer id) throws SQLException {
		BookRequestType type = null;
		String sql = String.format("SELECT * FROM %s WHERE %s = ?", DB.TABLE_BOOK_REQUEST_TYPE,
				DB.BOOK_REQUEST_TYPE_ID);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					type = new BookRequestType();
					type.setId(rs.getInt(DB.BOOK_REQUEST_TYPE_ID));
					type.setType(RequestType.valueOf(rs.getString(DB.BOOK_REQUEST_TYPE_TYPE)));
				}
			}
		}
		return type;
	}

	@Override
	public void update(Connection connection, BookRequestType element) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ?;", DB.TABLE_BOOK_REQUEST_TYPE, DB.BOOK_REQUEST_TYPE_TYPE);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, element.getType().toString());
			pst.executeUpdate();
		}
	}

	@Override
	public void delete(Connection connection, Integer id) throws SQLException {
		String sql = String.format("DELETE FROM %s WHERE %s = ?;", DB.TABLE_BOOK_REQUEST_TYPE, DB.BOOK_REQUEST_TYPE_ID);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.executeUpdate();
		}
	}

	public BookRequestType getByType(Connection connection, String type) throws SQLException {
		BookRequestType item = null;
		String sql = String.format("SELECT * FROM %s WHERE %s = ?", DB.TABLE_BOOK_REQUEST_TYPE,
				DB.BOOK_REQUEST_TYPE_TYPE);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, type);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					item = new BookRequestType();
					item.setId(rs.getInt(DB.BOOK_REQUEST_TYPE_ID));
					item.setType(RequestType.valueOf(rs.getString(DB.BOOK_REQUEST_TYPE_TYPE)));
				}
			}
		}
		return item;
	}
}

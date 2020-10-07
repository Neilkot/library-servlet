package com.epam.lab.exam.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.constants.DB;
import com.epam.lab.exam.library.model.BookRequestJournal;
import com.epam.lab.exam.library.util.DBHelper;

public class BookRequestJournalDao implements Dao<BookRequestJournal, Integer> {

	private static final BookRequestJournalDao INSTANCE = new BookRequestJournalDao();
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	private BookRequestJournalDao() {
	}

	public static final BookRequestJournalDao getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer create(Connection connection, BookRequestJournal element) throws SQLException {
		String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?);",
				DB.TABLE_BOOK_REQUEST_JOURNAL, DB.BOOK_REQUEST_JOURNAL_BOOK_REQUEST_ID,
				DB.BOOK_REQUEST_JOURNAL_CREATE_DATE, DB.BOOK_REQUEST_JOURNAL_APPROVE_DATE,
				DB.BOOK_REQUEST_JOURNAL_EXPIRATION_DATE, DB.BOOK_REQUEST_JOURNAL_RETURN_DATE);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pst.setInt(1, element.getBookRequestId());
			pst.setTimestamp(2, DBHelper.toSqlTimestamp(element.getCreateDate()));
			pst.setTimestamp(3, DBHelper.toSqlTimestamp(element.getApproveDate()));
			pst.setTimestamp(4, DBHelper.toSqlTimestamp(element.getExpirationDate()));
			pst.setTimestamp(5, DBHelper.toSqlTimestamp(element.getReturnDate()));
			pst.executeUpdate();
			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					logger.warn("Error creating bookRequestJournal. Prepared statement returned zero rows modified");
					throw new SQLException("Couldn't create BookRequestJournal entity");
				}
			}
		}
	}

	@Override
	public BookRequestJournal read(Connection connection, Integer id) throws SQLException {
		BookRequestJournal bookRequestJournal = null;
		String sql = String.format("SELECT * FROM %s WHERE %s = ?;", DB.TABLE_BOOK_REQUEST_JOURNAL,
				DB.BOOK_REQUEST_JOURNAL_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					bookRequestJournal = DBHelper.getBookRequestJournal(rs);
				}
			}
		}
		return bookRequestJournal;
	}

	@Override
	public void update(Connection connection, BookRequestJournal element) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?;",
				DB.TABLE_BOOK_REQUEST_JOURNAL, DB.BOOK_REQUEST_JOURNAL_BOOK_REQUEST_ID,
				DB.BOOK_REQUEST_JOURNAL_CREATE_DATE, DB.BOOK_REQUEST_JOURNAL_APPROVE_DATE,
				DB.BOOK_REQUEST_JOURNAL_EXPIRATION_DATE, DB.BOOK_REQUEST_JOURNAL_RETURN_DATE,
				DB.BOOK_REQUEST_JOURNAL_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, element.getBookRequestId());
			pst.setTimestamp(2, DBHelper.toSqlTimestamp(element.getCreateDate()));
			pst.setTimestamp(3, DBHelper.toSqlTimestamp(element.getApproveDate()));
			pst.setTimestamp(4, DBHelper.toSqlTimestamp(element.getExpirationDate()));
			pst.setTimestamp(5, DBHelper.toSqlTimestamp(element.getReturnDate()));
			pst.setInt(6, element.getId());
			pst.executeUpdate();
		}
	}

	@Override
	public void delete(Connection connection, Integer id) throws SQLException {
		String sql = String.format("DELETE FROM %s WHERE %s = ?;", DB.TABLE_BOOK_REQUEST_JOURNAL,
				DB.BOOK_REQUEST_JOURNAL_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.executeUpdate();
		}
	}

	public BookRequestJournal getByBookRequest(Connection connection, Integer bookRequestId) throws SQLException {
		BookRequestJournal bookRequestJournal = null;
		String sql = String.format("SELECT * FROM %s WHERE %s = ?; ", DB.TABLE_BOOK_REQUEST_JOURNAL,
				DB.BOOK_REQUEST_JOURNAL_BOOK_REQUEST_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, bookRequestId);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					bookRequestJournal = DBHelper.getBookRequestJournal(rs);
				}
			}
		}
		return bookRequestJournal;
	}

	public void returnBook(Connection connection, Integer id, Instant returnDate) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?;", DB.TABLE_BOOK_REQUEST_JOURNAL,
				DB.BOOK_REQUEST_JOURNAL_RETURN_DATE, DB.BOOK_REQUEST_JOURNAL_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setTimestamp(1, DBHelper.toSqlTimestamp(returnDate));
			pst.setInt(2, id);
			pst.executeUpdate();
		}
	}

	public void setApprovedDate(Connection connection, Integer bookRequestId, Instant approveDate,
			Instant expirationDate) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?;", DB.TABLE_BOOK_REQUEST_JOURNAL,
				DB.BOOK_REQUEST_JOURNAL_APPROVE_DATE, DB.BOOK_REQUEST_JOURNAL_EXPIRATION_DATE,
				DB.BOOK_REQUEST_JOURNAL_BOOK_REQUEST_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setTimestamp(1, DBHelper.toSqlTimestamp(approveDate));
			pst.setTimestamp(2, DBHelper.toSqlTimestamp(expirationDate));
			pst.setInt(3, bookRequestId);
			pst.executeUpdate();
		}
	}
}

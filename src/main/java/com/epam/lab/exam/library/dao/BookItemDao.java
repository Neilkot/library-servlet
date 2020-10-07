package com.epam.lab.exam.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.constants.DB;
import com.epam.lab.exam.library.model.BookItem;

public class BookItemDao implements Dao<BookItem, Integer> {

	private static final BookItemDao INSTANCE = new BookItemDao();

	private final Logger logger = LogManager.getLogger(this.getClass());

	private BookItemDao() {
	}

	public static BookItemDao getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer create(Connection connection, BookItem element) throws SQLException {
		String sql = String.format("INSERT INTO %s (%s) VALUES(?);", DB.TABLE_BOOK_ITEM, DB.BOOK_ITEM_BOOK_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pst.setInt(1, element.getBookId());
			pst.executeUpdate();

			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					logger.warn("Error creating bookItem. Prepared statement returned zero rows modified");
					throw new SQLException("Couldn't create bookItem entity");
				}
			}
		}
	}

	@Override
	public BookItem read(Connection connection, Integer id) throws SQLException {
		BookItem bookItem = null;
		String sql = String.format("SELECT * from %s WHERE %s = ?;", DB.TABLE_BOOK_ITEM, DB.BOOK_ITEM_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				bookItem = new BookItem();
				bookItem.setId(rs.getInt(DB.BOOK_ITEM_ID));
				bookItem.setBookId(rs.getInt(DB.BOOK_ITEM_BOOK_ID));
			}
			return bookItem;
		}
	}

	@Override
	public void update(Connection connection, BookItem element) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?;", DB.TABLE_BOOK_ITEM, DB.BOOK_ITEM_BOOK_ID,
				DB.BOOK_ITEM_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, element.getBookId());
			pst.setInt(2, element.getId());
			pst.executeUpdate();
		}
	}

	@Override
	public void delete(Connection connection, Integer id) throws SQLException {
		String sql = String.format("DELETE FROM %s WHERE %s = ?;", DB.TABLE_BOOK_ITEM, DB.BOOK_ITEM_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.executeUpdate();
		}
	}

	public void deleteNonRequested(Connection connection, Integer id) throws SQLException {
		String sql = "DELETE FROM book_items bi WHERE bi.id = ? AND bi.id NOT IN ( SELECT br.book_item_id FROM book_requests br JOIN book_requests_journals brj ON\n"
				+ "br.id = brj.book_request_id WHERE brj.return_date IS NULL);";
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			int status = pst.executeUpdate();
			logger.info("delete status={}", status);
		}
	}

	public List<BookItem> getByBookId(Connection connection, Integer bookId) throws SQLException {
		List<BookItem> bookItems = new ArrayList<>();
		String sql = String.format("SELECT * from %s WHERE %s = ?;", DB.TABLE_BOOK_ITEM, DB.BOOK_ITEM_BOOK_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, bookId);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					BookItem bookItem = new BookItem();
					bookItem.setId(rs.getInt(DB.BOOK_ITEM_ID));
					bookItem.setBookId(rs.getInt(DB.BOOK_ITEM_BOOK_ID));
					bookItems.add(bookItem);
				}
			}
		}
		return bookItems;
	}

	public List<BookItem> getAllAvaliableBooks(Connection connection, Integer bookId) throws SQLException {
		List<BookItem> bookItems = new ArrayList<>();
		String sql = String.format(
				"SELECT * from %s WHERE %s = ? AND id NOT IN (SELECT br.book_item_id FROM book_requests_journals brj JOIN book_requests br ON brj.book_request_id = br.id WHERE return_date IS NULL);",
				DB.TABLE_BOOK_ITEM, DB.BOOK_ITEM_BOOK_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, bookId);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					BookItem bookItem = new BookItem();
					bookItem.setId(rs.getInt(DB.BOOK_ITEM_ID));
					bookItem.setBookId(rs.getInt(DB.BOOK_ITEM_BOOK_ID));
					bookItems.add(bookItem);
				}
			}
		}
		return bookItems;
	}

}

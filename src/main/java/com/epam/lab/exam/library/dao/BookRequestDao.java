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
import com.epam.lab.exam.library.dto.BookRequestDTO;
import com.epam.lab.exam.library.dto.PendingRequestDTO;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.RequestType;
import com.epam.lab.exam.library.util.DBHelper;

public class BookRequestDao implements Dao<BookRequest, Integer> {

	private static final BookRequestDao INSTANCE = new BookRequestDao();

	private final Logger logger = LogManager.getLogger(this.getClass());

	private BookRequestDao() {
	}

	public static BookRequestDao getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer create(Connection connection, BookRequest element) throws SQLException {
		String sql = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?);", DB.TABLE_BOOK_REQUEST,
				DB.BOOK_REQUEST_USER_ID, DB.BOOK_REQUEST_BOOK_ITEM_ID, DB.BOOK_REQUEST_BOOK_REQUEST_TYPE_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pst.setInt(1, element.getUserId());
			pst.setInt(2, element.getBookItemId());
			pst.setInt(3, element.getBookRequestTypeId());
			pst.executeUpdate();

			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					logger.warn("Error creating bookRequest. Prepared statement returned zero rows modified");
					throw new SQLException("Couldn't create BookRequest entity");
				}
			}
		}
	}

	@Override
	public BookRequest read(Connection connection, Integer id) throws SQLException {
		BookRequest bookRequest = null;
		String sql = String.format("SELECT * FROM %s WHERE %s = ?;", DB.TABLE_BOOK_REQUEST, DB.BOOK_REQUEST_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					bookRequest = DBHelper.getBookRequest(rs);
				}
			}
		}
		return bookRequest;
	}

	@Override
	public void update(Connection connection, BookRequest element) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?;", DB.TABLE_BOOK_REQUEST,
				DB.BOOK_REQUEST_USER_ID, DB.BOOK_REQUEST_BOOK_ITEM_ID, DB.BOOK_REQUEST_BOOK_REQUEST_TYPE_ID,
				DB.BOOK_REQUEST_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, element.getUserId());
			pst.setInt(2, element.getBookItemId());
			pst.setInt(3, element.getBookRequestTypeId());
			pst.setInt(4, element.getId());
			pst.executeUpdate();
		}
	}

	@Override
	public void delete(Connection connection, Integer id) throws SQLException {
		String sql = String.format("DELETE FROM %s WHERE %s = ?;", DB.TABLE_BOOK_REQUEST, DB.BOOK_REQUEST_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.executeUpdate();
		}
	}

	public List<PendingRequestDTO> getPendingNonBlockedReaderRequests(Connection connection, int pageSize, int offset)
			throws SQLException {
		List<PendingRequestDTO> bookRequests = new ArrayList<>();
		String sql = "SELECT CONCAT(u.first_name,' ', u.last_name) as user_name, b.name as book_name, a.name as author_name, brt.type as request_type, brj.create_date as create_date, br.id as request_id FROM book_requests br JOIN users u ON br.user_id = u.id JOIN book_items bi ON bi.id = br.book_item_id JOIN books b ON bi.book_id = b.id JOIN authors a ON b.author_id = a.id JOIN book_request_types brt ON br.book_request_type_id = brt.id JOIN book_requests_journals brj ON br.id = brj.book_request_id WHERE NOT u.is_blocked AND brj.approve_date IS NULL ORDER BY br.id LIMIT ? OFFSET ?;";
		logger.info("Executing sql query: {}", sql);

		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, pageSize);
			pst.setInt(2, offset);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					PendingRequestDTO bookRequestDto = new PendingRequestDTO();
					bookRequestDto.setUsername(rs.getNString(DB.COLUMN_NAME_USER_NAME));
					bookRequestDto.setBookName(rs.getString(DB.COLUMN_NAME_BOOK_NAME));
					bookRequestDto.setAuthorName(rs.getString(DB.COLUMN_NAME_AUTHOR_NAME));
					bookRequestDto
							.setCreateDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(DB.COLUMN_NAME_CREATE_DATE)));
					bookRequestDto.setRequestId(rs.getInt(DB.COLUMN_NAME_REQUEST_ID));
					bookRequestDto.setRequestType(RequestType.valueOf(rs.getString(DB.COLUMN_NAME_REQUEST_TYPE)));
					bookRequests.add(bookRequestDto);
				}

			}
		}
		return bookRequests;
	}

	public List<BookRequestDTO> getApprovedNonBlockedReaderRequests(Connection connection, int pageSize, int offset)
			throws SQLException {
		List<BookRequestDTO> bookRequests = new ArrayList<>();
		String sql = "SELECT br.id as request_id, CONCAT(u.first_name,' ', u.last_name) as user_name, b.name as book_name, a.name as author_name, "//
				+ "brt.type as request_type, brj.create_date as create_date, brj.approve_date as approve_date, "//
				+ "brj.expiration_date as expiration_date, brj.return_date as return_date "//
				+ "FROM book_requests br "//
				+ "JOIN users u ON br.user_id = u.id "//
				+ "JOIN book_items bi ON bi.id = br.book_item_id "//
				+ "JOIN books b ON bi.book_id = b.id "//
				+ "JOIN authors a ON b.author_id = a.id "//
				+ "JOIN book_request_types brt ON br.book_request_type_id = brt.id "//
				+ "JOIN book_requests_journals brj ON br.id = brj.book_request_id "//
				+ "WHERE NOT u.is_blocked AND brj.approve_date IS NOT NULL "//
				+ "ORDER BY br.id LIMIT ? OFFSET ?;";
		logger.info("Executing sql query: {}", sql);

		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, pageSize);
			pst.setInt(2, offset);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					BookRequestDTO bookRequestDto = new BookRequestDTO();
					bookRequestDto.setRequestId(rs.getInt(DB.COLUMN_NAME_REQUEST_ID));
					bookRequestDto.setUsername(rs.getNString(DB.COLUMN_NAME_USER_NAME));
					bookRequestDto.setBookName(rs.getString(DB.COLUMN_NAME_BOOK_NAME));
					bookRequestDto.setAuthorName(rs.getString(DB.COLUMN_NAME_AUTHOR_NAME));
					bookRequestDto.setRequestType(RequestType.valueOf(rs.getString(DB.COLUMN_NAME_REQUEST_TYPE)));
					bookRequestDto
							.setCreateDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(DB.COLUMN_NAME_CREATE_DATE)));
					bookRequestDto.setApproveDate(
							DBHelper.fromSqlTimestamp(rs.getTimestamp(DB.BOOK_REQUEST_JOURNAL_APPROVE_DATE)));
					bookRequestDto.setExpirationDate(
							DBHelper.fromSqlTimestamp(rs.getTimestamp(DB.BOOK_REQUEST_JOURNAL_EXPIRATION_DATE)));
					bookRequestDto.setReturnDate(
							DBHelper.fromSqlTimestamp(rs.getTimestamp(DB.BOOK_REQUEST_JOURNAL_RETURN_DATE)));
					bookRequests.add(bookRequestDto);
				}
			}
		}
		return bookRequests;
	}

	public List<BookRequestDTO> getUserApprovedRequests(Connection connection, Integer userId, Integer pageSize,
			Integer offset) throws SQLException {
		List<BookRequestDTO> requests = new ArrayList<>();
		String sql = "SELECT br.id as book_request_id, CONCAT(u.first_name, ' ', u.last_name) as full_name, b.name as book_name,\n"
				+ " a.name as author_name, brt.type as book_request_type, brj.create_date as create_date, \n"
				+ " brj.approve_date as approve_date, brj.expiration_date as expiration_date, brj.return_date as return_date \n"
				+ " FROM book_requests br JOIN users u ON\n" + " br.user_id = u.id JOIN\n"
				+ " book_items bi ON bi.id = br.book_item_id JOIN\n" + " books b ON b.id = bi.book_id JOIN\n"
				+ " authors a ON a.id = b.author_id JOIN\n"
				+ " book_request_types brt ON brt.id = br.book_request_type_id JOIN\n"
				+ " book_requests_journals brj ON brj.book_request_id = br.id \n"
				+ " WHERE NOT u.is_blocked AND brj.approve_date IS NOT NULL AND\n" + " u.id = ? LIMIT ? OFFSET ?;";
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, userId);
			pst.setInt(2, pageSize);
			pst.setInt(3, offset);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					BookRequestDTO request = new BookRequestDTO();
					request.setRequestId(rs.getInt(1));
					request.setUsername(rs.getString(2));
					request.setBookName(rs.getString(3));
					request.setAuthorName(rs.getString(4));
					request.setRequestType(RequestType.valueOf(rs.getString(5)));
					request.setCreateDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(6)));
					request.setApproveDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(7)));
					request.setExpirationDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(8)));
					request.setReturnDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(9)));
					requests.add(request);
				}
			}
		}
		return requests;
	}

	public int getPendingCount(Connection connection) throws SQLException {
		String sql = "SELECT COUNT(*) as 'count' FROM book_requests br JOIN users u ON br.user_id = u.id JOIN book_items bi ON bi.id = br.book_item_id JOIN books b ON bi.book_id = b.id JOIN authors a ON b.author_id = a.id JOIN book_request_types brt ON br.book_request_type_id = brt.id JOIN book_requests_journals brj ON br.id = brj.book_request_id WHERE NOT u.is_blocked AND brj.approve_date IS NULL;";
		try (PreparedStatement pst = connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
			return rs.next() ? rs.getInt("count") : 0;
		}
	}

	public int countUserApprovedRequests(Connection connection, Integer id) throws SQLException {
		String sql = "SELECT COUNT(*) as 'count'" + " FROM book_requests br JOIN users u ON\n"
				+ " br.user_id = u.id JOIN\n" + " book_items bi ON bi.id = br.book_item_id JOIN\n"
				+ " books b ON b.id = bi.book_id JOIN\n" + " authors a ON a.id = b.author_id JOIN\n"
				+ " book_request_types brt ON brt.id = br.book_request_type_id JOIN\n"
				+ " book_requests_journals brj ON brj.book_request_id = br.id \n"
				+ " WHERE NOT u.is_blocked AND brj.approve_date IS NOT NULL AND\n" + " u.id = ?;";
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				return rs.next() ? rs.getInt("count") : 0;
			}
		}
	}

	public int countApprovedRequests(Connection connection) throws SQLException {
		String sql = "SELECT COUNT(*) as count " + "FROM book_requests br "//
				+ "JOIN users u ON br.user_id = u.id "//
				+ "JOIN book_items bi ON bi.id = br.book_item_id "//
				+ "JOIN books b ON bi.book_id = b.id "//
				+ "JOIN authors a ON b.author_id = a.id "//
				+ "JOIN book_request_types brt ON br.book_request_type_id = brt.id "//
				+ "JOIN book_requests_journals brj ON br.id = brj.book_request_id "//
				+ "WHERE NOT u.is_blocked AND brj.approve_date IS NOT NULL";

		try (PreparedStatement pst = connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
				return rs.next() ? rs.getInt("count") : 0;
		}
	}
}

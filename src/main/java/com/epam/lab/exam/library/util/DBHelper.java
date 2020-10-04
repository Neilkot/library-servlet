package com.epam.lab.exam.library.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import com.epam.lab.exam.library.constants.DB;
import com.epam.lab.exam.library.model.Book;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.BookRequestJournal;
import com.epam.lab.exam.library.model.User;

public class DBHelper {

	public static User getUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt(DB.USER_ID));
		user.setLogin(rs.getString(DB.USER_LOGIN));
		user.setChecksum(rs.getString(DB.USER_CHECKSUM));
		user.setFirstName(rs.getString(DB.USER_FIRST_NAME));
		user.setLastName(rs.getString(DB.USER_LAST_NAME));
		user.setRoleId(rs.getInt(DB.USER_ROLE_ID));
		user.setIsBlocked(rs.getBoolean(DB.USER_IS_BLOCKED));
		return user;
	}

	public static Timestamp toSqlTimestamp(Instant instant) {
		return Optional.ofNullable(instant).map(Timestamp::from).orElse(null);
	}
	
	public static Instant fromSqlTimestamp(Timestamp timestamp) {
		return Optional.ofNullable(timestamp).map(Timestamp::toInstant).orElse(null);
	}

	public static BookRequest getBookRequest(ResultSet rs) throws SQLException {
		BookRequest bookRequest = new BookRequest();
		bookRequest.setId(rs.getInt(DB.BOOK_REQUEST_ID));
		bookRequest.setUserId(rs.getInt(DB.BOOK_REQUEST_USER_ID));
		bookRequest.setBookItemId(rs.getInt(DB.BOOK_REQUEST_BOOK_ITEM_ID));
		bookRequest.setBookRequestTypeId(rs.getInt(DB.BOOK_REQUEST_BOOK_REQUEST_TYPE_ID));
		return bookRequest;
	}

	public static Book getBook(ResultSet rs) throws SQLException {
		Book book = new Book();
		book.setId(rs.getInt(DB.BOOK_ID));
		book.setName(rs.getNString(DB.BOOK_NAME));
		book.setAuthorid(rs.getInt(DB.BOOK_AUTHOR_ID));
		book.setPublisher(rs.getNString(DB.BOOK_PUBLISHER));
		book.setPublishedYear(rs.getInt(DB.BOOK_PUBLISH_YEAR));
		book.setImgLink(rs.getString(DB.BOOK_IMAGE_LINK));
		return book;
	}

	public static BookRequestJournal getBookRequestJournal(ResultSet rs) throws SQLException {
		BookRequestJournal bookRequestJournal = new BookRequestJournal();
		bookRequestJournal.setId(rs.getInt(DB.BOOK_REQUEST_JOURNAL_ID));
		bookRequestJournal.setBookRequestId(rs.getInt(DB.BOOK_REQUEST_JOURNAL_BOOK_REQUEST_ID));
		bookRequestJournal.setCreateDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(DB.BOOK_REQUEST_JOURNAL_CREATE_DATE)));
		bookRequestJournal.setApproveDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(DB.BOOK_REQUEST_JOURNAL_APPROVE_DATE)));
		bookRequestJournal.setExpirationDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(DB.BOOK_REQUEST_JOURNAL_EXPIRATION_DATE)));
		bookRequestJournal.setReturnDate(DBHelper.fromSqlTimestamp(rs.getTimestamp(DB.BOOK_REQUEST_JOURNAL_RETURN_DATE)));
		return bookRequestJournal;
	}
}

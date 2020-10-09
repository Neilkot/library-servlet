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
import com.epam.lab.exam.library.dto.CreateBookDTO;
import com.epam.lab.exam.library.model.Book;
import com.epam.lab.exam.library.util.DBHelper;

public class BookDao implements Dao<Book, Integer>, CountableDao {

	private static final BookDao INSTANCE = new BookDao();

	private final Logger logger = LogManager.getLogger(this.getClass());

	private BookDao() {
	}

	public static BookDao getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer create(Connection connection, Book element) throws SQLException {
		String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES( ?,?,?,?,?);", DB.TABLE_BOOK,
				DB.BOOK_NAME, DB.BOOK_AUTHOR_ID, DB.BOOK_PUBLISHER, DB.BOOK_PUBLISH_YEAR, DB.BOOK_IMAGE_LINK);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, element.getName());
			pst.setInt(2, element.getAuthorid());
			pst.setString(3, element.getPublisher());
			pst.setInt(4, element.getPublishedYear());
			pst.setString(5, element.getImgLink());
			pst.executeUpdate();

			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					logger.warn("Error creating book. Prepared statement returned zero rows modified");
					throw new SQLException("Couldn't create book entity");
				}
			}
		}
	}

	@Override
	public Book read(Connection connection, Integer id) throws SQLException {
		Book book = null;
		String sql = String.format("SELECT * from %s WHERE %s = ?;", DB.TABLE_BOOK, DB.BOOK_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					book = DBHelper.getBook(rs);
				}
			}
		}
		return book;
	}

	@Override
	public void update(Connection connection, Book element) throws SQLException {
		String sql = String.format("UPDATE %s SET %s =?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?;", DB.TABLE_BOOK,
				DB.BOOK_NAME, DB.BOOK_AUTHOR_ID, DB.BOOK_PUBLISHER, DB.BOOK_PUBLISH_YEAR, DB.BOOK_IMAGE_LINK,
				DB.BOOK_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, element.getName());
			pst.setInt(2, element.getAuthorid());
			pst.setString(3, element.getPublisher());
			pst.setInt(4, element.getPublishedYear());
			pst.setString(5, element.getImgLink());
			pst.setInt(6, element.getId());
			pst.executeUpdate();
		}
	}

	@Override
	public void delete(Connection connection, Integer id) throws SQLException {
		String sql = String.format("DELETE FROM %s WHERE %s = ?;", DB.TABLE_BOOK, DB.BOOK_ID);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.executeUpdate();
		}
	}

	public List<CreateBookDTO> getAllAvaliableBooks(Connection connection, int pageSize, int offset)
			throws SQLException {

		List<CreateBookDTO> books = new ArrayList<>();

		String sql = "SELECT DISTINCT " + DB.TABLE_BOOK + "." + DB.BOOK_ID + " as book_id," + DB.TABLE_BOOK + "."
				+ DB.BOOK_NAME + " as book_name," + DB.TABLE_AUTHOR + "." + DB.AUTHOR_NAME + " as author_name, "
				+ DB.TABLE_BOOK + "." + DB.BOOK_PUBLISHER + " as publisher, " + DB.TABLE_BOOK + "."
				+ DB.BOOK_PUBLISH_YEAR + " as publish_year," + DB.TABLE_BOOK + "." + DB.BOOK_IMAGE_LINK
				+ " as img_link " + "FROM " + DB.TABLE_BOOK + " JOIN " + DB.TABLE_BOOK_ITEM + " ON " + DB.TABLE_BOOK
				+ "." + DB.BOOK_ID + " = " + DB.TABLE_BOOK_ITEM + "." + DB.BOOK_ITEM_BOOK_ID + " JOIN "
				+ DB.TABLE_AUTHOR + " ON " + DB.TABLE_BOOK + "." + DB.BOOK_AUTHOR_ID + " = " + DB.TABLE_AUTHOR + "."
				+ DB.AUTHOR_ID + " WHERE " + DB.TABLE_BOOK_ITEM + "." + DB.BOOK_ITEM_ID + " NOT IN ( SELECT "
				+ DB.TABLE_BOOK_REQUEST + "." + DB.BOOK_REQUEST_BOOK_ITEM_ID + " FROM " + DB.TABLE_BOOK_REQUEST
				+ " JOIN " + DB.TABLE_BOOK_REQUEST_JOURNAL + " ON " + DB.TABLE_BOOK_REQUEST + "." + DB.BOOK_REQUEST_ID
				+ " = " + DB.TABLE_BOOK_REQUEST_JOURNAL + "." + DB.BOOK_REQUEST_JOURNAL_BOOK_REQUEST_ID + " WHERE "
				+ DB.TABLE_BOOK_REQUEST_JOURNAL + "." + DB.BOOK_REQUEST_JOURNAL_RETURN_DATE + " IS NULL) ORDER BY "
				+ DB.TABLE_BOOK + "." + DB.BOOK_ID + " LIMIT ? OFFSET ?;";

		logger.info("Executing sql query: {}", sql);

		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, pageSize);
			pst.setInt(2, offset);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					CreateBookDTO bookDto = new CreateBookDTO();
					bookDto.setBookId(rs.getInt("book_id"));
					bookDto.setName(rs.getString("book_name"));
					bookDto.setAuthorName(rs.getString("author_name"));
					bookDto.setPublisher(rs.getString("publisher"));
					bookDto.setPublishedYear(rs.getInt("publish_year"));
					bookDto.setImgLink(rs.getString("img_link"));
					books.add(bookDto);
				}
			}
		}
		return books;
	}

	public List<CreateBookDTO> getPage(Connection connection, int pageSize, int offset) throws SQLException {
		List<CreateBookDTO> books = new ArrayList<>();
		String sql = "SELECT b.id, b.name, a.name, b.publisher, b.publish_year, b.image_link, bi.id "
				+ "FROM books b JOIN authors a ON b.author_id = a.id " + "JOIN book_items bi ON b.id = bi.book_id "
				+ "WHERE bi.id NOT IN (SELECT br.book_item_id FROM book_requests br JOIN book_requests_journals brj ON brj.book_request_id = br.id "
				+ " WHERE brj.return_date IS NOT NULL) " + "ORDER BY b.id LIMIT ? OFFSET ?;";
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, pageSize);
			pst.setInt(2, offset);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					CreateBookDTO bookDto = new CreateBookDTO();
					bookDto.setBookId(rs.getInt(1));
					bookDto.setName(rs.getString(2));
					bookDto.setAuthorName(rs.getString(3));
					bookDto.setPublisher(rs.getString(4));
					bookDto.setPublishedYear(rs.getInt(5));
					bookDto.setImgLink(rs.getString(6));
					bookDto.setBookItemId(rs.getInt(7));
					books.add(bookDto);
				}
			}
		}
		return books;
	}

	public int getAllBooksCount(Connection connection) throws SQLException {
		String sql = "SELECT COUNT(*) as count " + "FROM books b JOIN authors a ON b.author_id = a.id "
				+ "JOIN book_items bi ON b.id = bi.book_id "
				+ "WHERE bi.id NOT IN (SELECT br.book_item_id FROM book_requests br JOIN book_requests_journals brj ON brj.book_request_id = br.id "
				+ " WHERE brj.return_date IS NOT NULL);";
		try (PreparedStatement pst = connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
			return rs.next() ? rs.getInt("count") : 0;
		}
	}

	public Book getBook(Connection connection, String name, Integer authorId, String publisher, Integer publishedYear)
			throws SQLException {
		Book book = null;
		String sql = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ? AND %s = ? AND %s = ?;", DB.TABLE_BOOK,
				DB.BOOK_NAME, DB.BOOK_AUTHOR_ID, DB.BOOK_PUBLISHER, DB.BOOK_PUBLISH_YEAR);
		logger.info("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, name);
			pst.setInt(2, authorId);
			pst.setString(3, publisher);
			pst.setInt(4, publishedYear);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					book = DBHelper.getBook(rs);
				}
			}
		}
		return book;
	}

	@Override
	public String getTableName() {
		return DB.TABLE_BOOK;
	}

	public int countAllAvaliableBooks(Connection connection) throws SQLException {
		String sql = "SELECT COUNT(*) as 'count' FROM (SELECT DISTINCT books.id FROM books JOIN book_items ON books.id = book_items.book_id JOIN authors ON books.author_id = authors.id WHERE book_items.id NOT IN ( SELECT book_requests.book_item_id FROM book_requests JOIN book_requests_journals ON book_requests.id = book_requests_journals.book_request_id WHERE book_requests_journals.return_date IS NULL)) as unique_books";
		try (PreparedStatement pst = connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
			return rs.next() ? rs.getInt("count") : 0;
		}

	}

}

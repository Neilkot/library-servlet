package com.epam.lab.exam.library.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.dao.AuthorDao;
import com.epam.lab.exam.library.dao.BookDao;
import com.epam.lab.exam.library.dao.BookItemDao;
import com.epam.lab.exam.library.db.DBManager;
import com.epam.lab.exam.library.db.DBManagerContainer;
import com.epam.lab.exam.library.dto.CreateBookDTO;
import com.epam.lab.exam.library.dto.UpdateBookDto;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.Author;
import com.epam.lab.exam.library.model.Book;
import com.epam.lab.exam.library.model.BookItem;

public class BookService {

	private static final BookService INSTANCE = new BookService();

	private final DBManager dbManager = DBManagerContainer.getInstance().getdBManager();
	private final BookDao bookDao = BookDao.getInstance();
	private final BookItemDao bookItemDao = BookItemDao.getInstance();

	private final Logger logger = LogManager.getLogger(this.getClass());

	private BookService() {
	}

	public static BookService getInstance() {
		return INSTANCE;
	}

	public List<CreateBookDTO> getAllAvaliableBooks(int pageSize, int offset) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookDao.getAllAvaliableBooks(connection, pageSize, offset);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public List<CreateBookDTO> getAllBooks(int pageSize, int offset) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookDao.getPage(connection, pageSize, offset);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public void createBook(CreateBookDTO bookDto) throws SQLException {
		Connection connection = dbManager.getConnection();
		connection.setAutoCommit(false);
		try {
			Author author = AuthorDao.getInstance().getByName(connection, bookDto.getAuthorName());
			if (author == null) {
				author = new Author();
				author.setName(bookDto.getAuthorName());
				author.setId(AuthorDao.getInstance().create(connection, author));
			}
			Book book = bookDao.getBook(connection, bookDto.getName(), author.getId(), bookDto.getPublisher(),
					bookDto.getPublishedYear());
			if (book == null) {
				book = new Book();
				book.setAuthorid(author.getId());
				book.setImgLink(bookDto.getImgLink());
				book.setName(bookDto.getName());
				book.setPublishedYear(bookDto.getPublishedYear());
				book.setPublisher(bookDto.getPublisher());
				book.setPublishedYear(bookDto.getPublishedYear());
				book.setId(bookDao.create(connection, book));
			}

			BookItem bookItem = new BookItem();
			bookItem.setBookId(book.getId());
			BookItemDao.getInstance().create(connection, bookItem);

			connection.commit();
		} catch (SQLException e) {
			logger.warn("Rolling back transaction for book={}", bookDto.getName());
			connection.rollback();
			throw e;
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public void updateBook(UpdateBookDto bookDto) throws SQLException, ClientRequestException {
		Connection connection = dbManager.getConnection();
		connection.setAutoCommit(false);
		try {
			Author author = AuthorDao.getInstance().getByName(connection, bookDto.getAuthorName());
			if (author == null) {
				author = new Author();
				author.setName(bookDto.getAuthorName());
				author.setId(AuthorDao.getInstance().create(connection, author));
			}
			Book book = bookDao.read(connection, bookDto.getBookId());
			if (book == null) {
				logger.error("No book found in DB. id={} name={}", bookDto.getBookId(), bookDto.getName());
				throw new ClientRequestException(ErrorType.BOOK_NOT_FOUND);
			}

			book.setAuthorid(author.getId());
			book.setImgLink(bookDto.getImgLink());
			book.setName(bookDto.getName());
			book.setPublishedYear(bookDto.getPublishedYear());
			book.setPublisher(bookDto.getPublisher());
			book.setPublishedYear(bookDto.getPublishedYear());
			bookDao.update(connection, book);
			connection.commit();
		} catch (SQLException e) {
			logger.warn("Rolling back transaction for book={}", bookDto.getName());
			connection.rollback();
			throw e;
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public void deleteBookItem(Integer id) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			connection.setAutoCommit(false);

			BookItem bookItem = bookItemDao.read(connection, id);
			Integer bookId = bookItem.getBookId();
			bookItemDao.deleteNonRequested(connection, id);
			List<BookItem> bookItems = bookItemDao.getByBookId(connection, bookId);
			if (bookItems.isEmpty()) {
				logger.info("Deleted last book item. Deleting book. bookId={}", bookId);
				bookDao.delete(connection, bookId);
			}
			connection.commit();
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public List<BookItem> getAllAvaliableBooks(Integer bookId) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookItemDao.getAllAvaliableBooks(connection, bookId);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public int getBooksCount() throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookDao.count(connection);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public int getAllBooksCount() throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookDao.getAllBooksCount(connection);
		} finally {
			dbManager.releaseConnection(connection);
		}

	}
}

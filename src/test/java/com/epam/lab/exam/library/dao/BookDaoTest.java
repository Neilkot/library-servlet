package com.epam.lab.exam.library.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.epam.lab.exam.library.dto.CreateBookDTO;
import com.epam.lab.exam.library.model.Author;
import com.epam.lab.exam.library.model.Book;
import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.BookRequestJournal;
import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

public class BookDaoTest extends AbstractDaoTest {

	private BookDao bookDao = BookDao.getInstance();

	@Before
	public void beforeEach() throws SQLException {
		super.beforeEach();
		fillUserRoles();
		fillBookRequestTypes();
	}

	@Test
	public void shouldCreateAndRead() throws SQLException {
		Author author = createAuthor(AUTHER_NAME_BLOCK);
		Book book = getBook(author);
		Integer id = bookDao.create(getConnection(), book);
		book.setId(id);

		Book actual = bookDao.read(getConnection(), id);
		assertEquals(book, actual);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		Author block = createAuthor(AUTHER_NAME_BLOCK);
		Book book = getBook(block);
		Integer id = bookDao.create(getConnection(), book);

		Author martin = createAuthor(AUTHER_NAME_MARTIN);
		book.setId(id);
		book.setName(NAME_CLEAN_CODE);
		book.setAuthorid(martin.getId());
		book.setPublisher("PROSTYLE");
		book.setPublishedYear(2020);
		book.setImgLink("https://www.yakaboo.ua/ua/chistij-kod.html?gclid=CjwKCAjw2Jb");
		bookDao.update(getConnection(), book);
		Book actual = bookDao.read(getConnection(), id);
		assertEquals(book, actual);
	}

	@Test
	public void sholdDelete() throws SQLException {
		Author author = createAuthor(AUTHER_NAME_BLOCK);
		Book book = getBook(author);
		Integer id = bookDao.create(getConnection(), book);
		assertNotNull(bookDao.read(getConnection(), id));
		bookDao.delete(getConnection(), id);
		Book actual = bookDao.read(getConnection(), id);
		assertNull(actual);
	}

	@Test
	public void shouldReturnAllBooksWithAmountBiggerThanZero() throws SQLException {
		BookItem bookItemOne = createBookItem(AUTHER_NAME_MARTIN, NAME_CLEAN_CODE);
		BookItem bookItemTwo = createBookItem(AUTHER_NAME_BLOCK, NAME_EFFECTIVE_JAVA);

		List<CreateBookDTO> avaliableBooks = bookDao.getAllAvaliableBooks(getConnection(), 2, 0);
		assertEquals(2, avaliableBooks.size());
		assertEquals(bookItemOne.getBookId(), avaliableBooks.get(0).getBookId());
		assertEquals(bookItemTwo.getBookId(), avaliableBooks.get(1).getBookId());
	}

	@Test
	public void shouldNotReturnAllBooksWithAmountBiggerThanZeroIfTheyAreOccupide() throws SQLException {
		User user = createUser(RoleType.READER);
		BookItem bookItemOne = createBookItem(AUTHER_NAME_MARTIN, NAME_CLEAN_CODE);
		BookRequestType bookRequestType = getBookRequestType(RequestType.ABONEMENT);
		BookRequest bookRequest = createBookRequest(user, bookItemOne, bookRequestType);
		BookRequestJournal journal = getBookRequestJournal(bookRequest);
		journal.setReturnDate(null);
		BookRequestJournalDao.getInstance().create(getConnection(), journal);

		List<CreateBookDTO> avaliableBooks = bookDao.getAllAvaliableBooks(getConnection(), 0, 1);
		assertEquals(0, avaliableBooks.size());
	}

	@Test
	public void sholdReturnCount() throws SQLException {
		createBook(AUTHER_NAME_MARTIN, "FOO");
		createBook("BAR", "BAR");
		createBook("FIZZ", "FIZZ");
		int actual = bookDao.count(getConnection());
		assertEquals(3, actual);
	}

	@Test
	public void shouldReturnFirstPage() throws SQLException {
		BookItem bookItemOne = createBookItem(AUTHER_NAME_BLOCK);
		BookItem bookItemTwo = createBookItem(AUTHER_NAME_MARTIN, NAME_CLEAN_CODE);

		List<CreateBookDTO> books = bookDao.getPage(getConnection(), 2, 0);
		assertEquals(2, books.size());
		assertEquals(bookItemOne.getId(), books.get(0).getBookItemId());
		assertEquals(bookItemTwo.getId(), books.get(1).getBookItemId());
	}

	@Test
	public void shouldReturnSecondPage() throws SQLException {
		createBookItem(AUTHER_NAME_BLOCK);
		BookItem bookItemTwo = createBookItem(AUTHER_NAME_MARTIN, NAME_CLEAN_CODE);
		List<CreateBookDTO> books = bookDao.getPage(getConnection(), 2, 1);
		assertEquals(1, books.size());
		assertEquals(bookItemTwo.getBookId(), books.get(0).getBookId());
	}

}

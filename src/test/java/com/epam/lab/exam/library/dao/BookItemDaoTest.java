package com.epam.lab.exam.library.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.epam.lab.exam.library.model.Book;
import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.BookRequestJournal;
import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

public class BookItemDaoTest extends AbstractDaoTest {
	
	@Before
	public void beforeEach() throws SQLException {
		super.beforeEach();
		fillUserRoles();
		fillBookRequestTypes();
	}

	private final BookItemDao bookItemDao = BookItemDao.getInstance();

	@Test
	public void shouldCreateAndRead() throws SQLException {
		Book book = createBook(AUTHER_NAME_BLOCK);
		BookItem bookItem = new BookItem();
		bookItem.setBookId(book.getId());
		bookItem.setId(bookItemDao.create(getConnection(), bookItem));

		BookItem actual = bookItemDao.read(getConnection(), bookItem.getId());
		assertEquals(bookItem, actual);
	}
	
	@Test (expected = SQLException.class)
	public void shouldThrowSQLExceptionWhenInsertinSameEntry() throws SQLException {
		createBookItem(AUTHER_NAME_BLOCK);
		createBookItem(AUTHER_NAME_BLOCK);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		BookItem bookItem = createBookItem(AUTHER_NAME_BLOCK);
		Book book = createBook(AUTHER_NAME_MARTIN);
		assertNotEquals(bookItem.getBookId(), book.getId());
		bookItem.setBookId(book.getId());
		bookItemDao.update(getConnection(), bookItem);
		BookItem actual = bookItemDao.read(getConnection(), bookItem.getId());
		assertEquals(bookItem, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		BookItem bookItem = createBookItem(AUTHER_NAME_BLOCK);
		bookItemDao.delete(getConnection(), bookItem.getId());
		BookItem actual = bookItemDao.read(getConnection(), bookItem.getId());
		assertNull(actual);
	}
	
	@Test
	public void shouldDeleteNonRequested() throws SQLException {
		User user = createUser(RoleType.READER);
		BookItem bookItem = createBookItem(AUTHER_NAME_BLOCK);
		BookRequestType bookRequestType = getBookRequestType(RequestType.ABONEMENT);
		BookRequest bookRequest = createBookRequest(user, bookItem, bookRequestType);
		BookRequestJournal journal = getBookRequestJournal(bookRequest);
		journal.setReturnDate(null);
		BookRequestJournalDao.getInstance().create(getConnection(), journal);
		bookItemDao.deleteNonRequested(getConnection(), bookItem.getId());
		BookItem actual = bookItemDao.read(getConnection(), bookItem.getId());
		assertNull(actual);
	}
	
	@Test
	public void shouldNotDeleteNonRequestedIfReturnDateIsSet() throws SQLException {
		User user = createUser(RoleType.READER);
		BookItem bookItem = createBookItem(AUTHER_NAME_BLOCK);
		BookRequestType bookRequestType = getBookRequestType(RequestType.ABONEMENT);
		BookRequest bookRequest = createBookRequest(user, bookItem, bookRequestType);
		BookRequestJournal journal = getBookRequestJournal(bookRequest);
		BookRequestJournalDao.getInstance().create(getConnection(), journal);
		bookItemDao.deleteNonRequested(getConnection(), bookItem.getId());
		BookItem actual = bookItemDao.read(getConnection(), bookItem.getId());
		assertNotNull(actual);
	}
	
	@Test
	public void shouldReturnBookItemsListByBookId() throws SQLException {
		Book book = createBook(AUTHER_NAME_BLOCK);
		BookItem bookItemOne = new BookItem();
		bookItemOne.setBookId(book.getId());
		bookItemOne.setId(bookItemDao.create(getConnection(), bookItemOne));
		
		BookItem bookItemTwo = new BookItem();
		bookItemTwo.setBookId(book.getId());
		bookItemTwo.setId(bookItemDao.create(getConnection(), bookItemTwo));
		
		List<BookItem> byBookId = bookItemDao.getByBookId(getConnection(), book.getId());
		assertEquals(2,byBookId.size());
		assertEquals(bookItemOne,byBookId.get(0));
		assertEquals(bookItemTwo,byBookId.get(1));
		
		
		
	}

}

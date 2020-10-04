package com.epam.lab.exam.library.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.BookRequestJournal;
import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

public class BookRequestJournalDaoTest extends AbstractDaoTest {

	private final BookRequestJournalDao joornalDao = BookRequestJournalDao.getInstance();

	@Before
	public void beforeEach() throws SQLException {
		super.beforeEach();
		fillUserRoles();
		fillBookRequestTypes();
	}

	@Test
	public void shouldCreateAndRead() throws SQLException {
		User user = createUser(RoleType.READER);
		BookItem bookItem = createBookItem(AUTHER_NAME_BLOCK);
		BookRequestType bookRequestType = getBookRequestType(RequestType.ABONEMENT);
		BookRequest bookRequest = createBookRequest(user, bookItem, bookRequestType);
		BookRequestJournal journal = getBookRequestJournal(bookRequest);
		Integer id = joornalDao.create(getConnection(), journal);
		journal.setId(id);

		BookRequestJournal actual = joornalDao.read(getConnection(), id);
		assertEquals(journal, actual);
	}

	@Test
	public void shouldUpdate() throws Exception {
		User user = createUser(RoleType.READER);
		BookItem bookItem = createBookItem(AUTHER_NAME_BLOCK);
		BookRequestType bookRequestType = getBookRequestType(RequestType.ABONEMENT);
		BookRequest bookRequest = createBookRequest(user, bookItem, bookRequestType);
		BookRequestJournal journal = getBookRequestJournal(bookRequest);
		Integer id = joornalDao.create(getConnection(), journal);
		journal.setId(id);
		journal.setCreateDate(Instant.now().plus(1, ChronoUnit.DAYS));
		journal.setApproveDate(Instant.now().plus(2, ChronoUnit.DAYS));
		journal.setExpirationDate(Instant.now().plus(3, ChronoUnit.DAYS));
		journal.setReturnDate(Instant.now().plus(10, ChronoUnit.DAYS));
		joornalDao.update(getConnection(), journal);

		BookRequestJournal actual = joornalDao.read(getConnection(), id);
		assertEquals(journal, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		User user = createUser(RoleType.READER);
		BookItem bookItem = createBookItem(AUTHER_NAME_BLOCK);
		BookRequestType bookRequestType = getBookRequestType(RequestType.ABONEMENT);
		BookRequest bookRequest = createBookRequest(user, bookItem, bookRequestType);
		BookRequestJournal journal = getBookRequestJournal(bookRequest);
		Integer id = joornalDao.create(getConnection(), journal);
		journal.setId(id);

		joornalDao.delete(getConnection(), id);
		BookRequestJournal actual = joornalDao.read(getConnection(), id);
		assertNull(actual);
	}

}

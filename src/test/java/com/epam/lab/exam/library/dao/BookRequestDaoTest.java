package com.epam.lab.exam.library.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

public class BookRequestDaoTest extends AbstractDaoTest {

	private final BookRequestDao bookRequestDao = BookRequestDao.getInstance();

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
		BookRequest request = getBookRequest(user, bookItem, bookRequestType);
		Integer id = bookRequestDao.create(getConnection(), request);
		request.setId(id);
		BookRequest actual = bookRequestDao.read(getConnection(), id);
		assertEquals(request, actual);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		User firstUser = createUser(RoleType.READER);
		BookItem block = createBookItem(AUTHER_NAME_BLOCK);
		BookRequestType abonementRequest = getBookRequestType(RequestType.ABONEMENT);
		BookRequest request = getBookRequest(firstUser, block, abonementRequest);
		Integer id = bookRequestDao.create(getConnection(), request);
		request.setId(id);

		request.setUserId(createUser(RoleType.READER, "secondUser").getId());
		request.setBookItemId(createBookItem(AUTHER_NAME_MARTIN, NAME_CLEAN_CODE).getId());
		request.setBookRequestTypeId(getBookRequestType(RequestType.READING_AREA).getId());
		bookRequestDao.update(getConnection(), request);
		BookRequest actual = bookRequestDao.read(getConnection(), id);
		assertEquals(request, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		User user = createUser(RoleType.READER);
		BookItem bookItem = createBookItem(AUTHER_NAME_BLOCK);
		BookRequestType bookRequestType = getBookRequestType(RequestType.ABONEMENT);
		BookRequest request = getBookRequest(user, bookItem, bookRequestType);
		Integer id = bookRequestDao.create(getConnection(), request);
		bookRequestDao.delete(getConnection(), id);
		BookRequest actual = bookRequestDao.read(getConnection(), id);
		assertNull(actual);
	}

	@Test
	public void shouldReturnAllUserRequestsByUserId() throws SQLException {
		User user = createUser(RoleType.READER);
		BookItem bookOne = createBookItem(AUTHER_NAME_BLOCK);
		BookRequest requestOne = getBookRequest(user, bookOne, getBookRequestType(RequestType.ABONEMENT));
		requestOne.setId(bookRequestDao.create(getConnection(), requestOne));
		BookItem bookTwo = createBookItem(AUTHER_NAME_MARTIN, NAME_CLEAN_CODE);
		BookRequest requestTwo = getBookRequest(user, bookTwo, getBookRequestType(RequestType.READING_AREA));
		requestTwo.setId(bookRequestDao.create(getConnection(), requestTwo));

		List<BookRequest> bookRequests = bookRequestDao.getUserRequests(getConnection(), user.getId(), 2, 0);
		assertEquals(2, bookRequests.size());
		assertEquals(requestOne, bookRequests.get(0));
		assertEquals(requestTwo, bookRequests.get(1));
	}

	@Test
	public void test() throws Exception {
		bookRequestDao.getUserApprovedRequests(getConnection(), 2, 0, 10);
	}

}

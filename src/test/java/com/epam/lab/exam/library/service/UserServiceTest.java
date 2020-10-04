package com.epam.lab.exam.library.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.epam.lab.exam.library.dao.AbstractDaoTest;
import com.epam.lab.exam.library.dao.UserDao;
import com.epam.lab.exam.library.dto.UserDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

public class UserServiceTest extends AbstractDaoTest {

	@Before
	public void beforeEach() throws SQLException {
		super.beforeEach();
		fillUserRoles();
		fillBookRequestTypes();
	}

	private final UserService userService = UserService.getInstance();

	@Test
	public void sholdReturnAuthenticateUser() throws SQLException {
		createUser(RoleType.READER);
		User authenticateUser = userService.authenticateUser(LOGIN, CHECKSUM);
		assertNotNull(authenticateUser);
	}

	@Test
	public void sholdReturnNullForNonAuthenticateUser() throws SQLException {
		createUser(RoleType.READER);
		User authenticateUser = userService.authenticateUser(LOGIN, "FOO");
		assertNull(authenticateUser);
	}

	@Test
	public void shouldCreateReaderByLoginInfo() throws SQLException, ClientRequestException {
		User reader = userService.createReader(LOGIN, CHECKSUM, FIRST_NAME, LAST_NAME);
		assertNotNull(reader);
		Role readerRole = getRole(RoleType.READER);
		assertEquals(readerRole.getId(), reader.getRoleId());
	}

	@Test(expected = ClientRequestException.class)
	public void shouldThrowClientRequestExceptionWhenReaderLoginIsInUse() throws SQLException, ClientRequestException {
		userService.createReader(LOGIN, CHECKSUM, FIRST_NAME, LAST_NAME);
		userService.createReader(LOGIN, CHECKSUM, FIRST_NAME, LAST_NAME);
	}

	@Test
	public void shouldCreateLibrarianByLoginInfo() throws SQLException, ClientRequestException {
		User librarian = userService.createLibrarian(LOGIN, CHECKSUM, FIRST_NAME, LAST_NAME);
		assertNotNull(librarian);
		Role librarianRole = getRole(RoleType.LIBRARIAN);
		assertEquals(librarianRole.getId(), librarian.getRoleId());
	}

	@Test(expected = ClientRequestException.class)
	public void shouldThrowClientRequestExceptionWhenLibrarianLoginIsInUse()
			throws SQLException, ClientRequestException {
		userService.createLibrarian(LOGIN, CHECKSUM, FIRST_NAME, LAST_NAME);
		userService.createLibrarian(LOGIN, CHECKSUM, FIRST_NAME, LAST_NAME);
	}

	@Test
	public void shouldSubmitAndGetReaderRequests() throws SQLException {
		List<BookRequest> requests = new ArrayList<>();
		User user = createUser(RoleType.READER);
		BookItem bookOne = createBookItem(AUTHER_NAME_BLOCK);
		BookRequestType bookRequestTypeOne = getBookRequestType(RequestType.ABONEMENT);
		BookRequest requestOne = getBookRequest(user, bookOne, bookRequestTypeOne);

		BookItem bookTwo = createBookItem(AUTHER_NAME_MARTIN, NAME_CLEAN_CODE);
		BookRequestType bookRequestTypeTwo = getBookRequestType(RequestType.READING_AREA);
		BookRequest requestTwo = getBookRequest(user, bookTwo, bookRequestTypeTwo);

		requests.add(requestOne);
		requests.add(requestTwo);
		userService.submitReaderRequests(requests);

		List<BookRequest> readerRequests = userService.getReaderRequests(user.getId(), 2, 0);
		assertEquals(requests.size(), readerRequests.size());
		assertEquals(requests.get(0).getUserId(), readerRequests.get(1).getUserId());
	}

	@Test
	public void shouldReturnFalseIfuserIsNotBlocked() throws SQLException {
		createUser(RoleType.READER);
		boolean actual = userService.getReaders(1, 0).get(0).getIsBlocked();
		assertFalse(actual);
	}

	@Test
	public void shouldReturnFalseIfuserIsBlocked() throws SQLException {
		User user = getUser(RoleType.READER);
		user.setIsBlocked(true);
		user.setId(UserDao.getInstance().create(getConnection(), user));
		boolean actual = userService.getReaders(1, 0).get(0).getIsBlocked();
		assertTrue(actual);
	}

	@Test
	public void shouldBlockUser() throws SQLException {
		User user = createUser(RoleType.READER);
		user = UserDao.getInstance().read(getConnection(), user.getId());
		assertFalse(user.getIsBlocked());
		userService.updateIsBlocked(user.getId(), true);
		user = UserDao.getInstance().read(getConnection(), user.getId());
		assertTrue(user.getIsBlocked());
	}

	@Test
	public void shouldUnblockBlockUser() throws SQLException {
		User user = getUser(RoleType.READER);
		user.setIsBlocked(true);
		user.setId(UserDao.getInstance().create(getConnection(), user));
		userService.updateIsBlocked(user.getId(), false);
		user = UserDao.getInstance().read(getConnection(), user.getId());
		assertFalse(user.getIsBlocked());
	}

	@Test
	public void shouldReturnAllLibrarians() throws SQLException, ClientRequestException {
		User librarianOne = createUser(RoleType.LIBRARIAN, "FOO");
		User librarianTwo = createUser(RoleType.LIBRARIAN, "BAR");
		List<UserDTO> librarians = userService.getLibrarians(2, 0);
		assertEquals(2, librarians.size());
		assertEquals(UserDTO.from(librarianOne), librarians.get(0));
		assertEquals(UserDTO.from(librarianTwo), librarians.get(1));
	}

	@Test
	public void shouldReturnAllReaders() throws SQLException, ClientRequestException {
		User readerOne = createUser(RoleType.READER, "FOO");
		User readerTwo = createUser(RoleType.READER, "BAR");
		List<UserDTO> readers = userService.getReaders(2, 0);
		assertEquals(2, readers.size());
		assertEquals(UserDTO.from(readerOne), readers.get(0));
		assertEquals(UserDTO.from(readerTwo), readers.get(1));
	}
	
	@Test
	public void testTine() {
	
	}
}

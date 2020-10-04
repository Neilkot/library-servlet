package com.epam.lab.exam.library.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

public class UserDaoTest extends AbstractDaoTest {

	private UserDao userDao = UserDao.getInstance();

	@Before
	public void beforeEach() throws SQLException {
		super.beforeEach();
		fillUserRoles();
	}

	@Test
	public void shouldCreateAndRead() throws Exception {
		User user = getUser(RoleType.READER);
		Integer id = userDao.create(getConnection(), user);
		assertNotNull(id);
		user.setId(id);
		User actual = userDao.read(getConnection(), id);
		assertEquals(user, actual);
	}

	@Test
	public void shouldUpdate() throws Exception {
		User user = getUser(RoleType.READER);
		Integer id = userDao.create(getConnection(), user);
		user.setId(id);
		user.setLogin("FooBAR");
		user.setChecksum("2324214235325");
		user.setFirstName("Foo");
		user.setLastName("Bar");
		user.setRoleId(getRole(RoleType.LIBRARIAN).getId());
		user.setIsBlocked(!user.getIsBlocked());
		userDao.update(getConnection(), user);
		User actual = userDao.read(getConnection(), id);
		assertEquals(user, actual);

	}

	@Test
	public void shouldDelete() throws Exception {
		User user = getUser(RoleType.READER);
		Integer id = userDao.create(getConnection(), user);
		assertNotNull(userDao.read(getConnection(), id));
		userDao.delete(getConnection(), id);
		User actual = userDao.read(getConnection(), id);
		assertNull(actual);
	}

	@Test
	public void shouldUpdateIsBlocked() throws SQLException {
		User user = getUser(RoleType.READER);
		user.setId(userDao.create(getConnection(), user));
		user.setIsBlocked(!user.getIsBlocked());
		userDao.updateIsBlocked(getConnection(), user.getId(), user.getIsBlocked());
		User actual = userDao.read(getConnection(), user.getId());
		assertEquals(user, actual);

		user.setIsBlocked(!user.getIsBlocked());
		userDao.updateIsBlocked(getConnection(), user.getId(), user.getIsBlocked());
		actual = userDao.read(getConnection(), user.getId());
		assertEquals(user, actual);
	}

	@Test
	public void shouldGetUserByLogin() throws SQLException {
		User user = getUser(RoleType.READER);
		user.setId(userDao.create(getConnection(), user));

		User actual = userDao.getByLogin(getConnection(), user.getLogin());
		assertEquals(user, actual);
	}

	@Test
	public void shouldNotGetUserByNotExistingLogin() throws SQLException {
		User user = getUser(RoleType.READER);
		user.setId(userDao.create(getConnection(), user));

		User actual = userDao.getByLogin(getConnection(), "Foo");
		assertNull(actual);
	}
}

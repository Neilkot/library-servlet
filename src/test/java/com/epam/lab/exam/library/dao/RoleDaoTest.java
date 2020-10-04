package com.epam.lab.exam.library.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.junit.Test;

import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;

public class RoleDaoTest extends AbstractDaoTest {

	private static final RoleType TYPE_READER = RoleType.READER;
	private static final RoleType TYPE_LIBRARIAN = RoleType.LIBRARIAN;

	private RoleDao roleDao = RoleDao.getInstance();

	@Test
	public void shouldCreateAndRead() throws SQLException {
		Role role = new Role();
		role.setType(TYPE_READER);
		Integer id = (roleDao.create(getConnection(), role));
		assertNotNull(id);
		role.setId(id);
		Role actual = roleDao.read(getConnection(), id);
		assertEquals(role, actual);
	}

	@Test
	public void shouldUpdate() throws Exception {
		Role role = new Role();
		role.setType(TYPE_READER);
		Integer id = (roleDao.create(getConnection(), role));
		role.setId(id);
		role.setType(TYPE_LIBRARIAN);
		roleDao.update(getConnection(), role);
		Role actual = roleDao.read(getConnection(), id);
		assertEquals(role, actual);
	}

	@Test
	public void shouldDelete() throws Exception {
		Role role = new Role();
		role.setType(TYPE_READER);
		Integer id = (roleDao.create(getConnection(), role));
		role.setId(id);
		assertNotNull(roleDao.read(getConnection(), id));
		roleDao.delete(getConnection(), id);
		Role actual = roleDao.read(getConnection(), id);
		assertNull(actual);
	}

	@Test
	public void sholdGetByType() throws Exception {
		Role role = new Role();
		role.setType(TYPE_LIBRARIAN);
		Integer id = (roleDao.create(getConnection(), role));
		role.setId(id);
		Role actual = roleDao.getByType(getConnection(), TYPE_LIBRARIAN);
		assertEquals(role, actual);

	}

}

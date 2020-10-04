package com.epam.lab.exam.library.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.epam.lab.exam.library.dao.AbstractDaoTest;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;

public class RoleServiceTest extends AbstractDaoTest {
	private final RoleService roleService = RoleService.getInstance();

	@Before
	public void beforeEach() throws SQLException {
		super.beforeEach();
		fillUserRoles();
	}

	@Test
	public void shouldReturnReaderRole() throws SQLException {
		Role actual = roleService.getReaderRole();
		assertEquals(RoleType.READER, actual.getType());
		assertNotNull(actual.getId());
		Role expected = getRole(RoleType.READER);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnLibrarianRole() throws SQLException {
		Role actual = roleService.getLibrarianRole();
		assertEquals(RoleType.LIBRARIAN, actual.getType());
		assertNotNull(actual.getId());
		Role expected = getRole(RoleType.LIBRARIAN);
		assertEquals(expected, actual);
	}

	@Test
	public void sholdReturnRoleById() throws SQLException {
		Role expected = getRole(RoleType.READER);
		Role actual = roleService.getRole(expected.getId());
		assertEquals(expected, actual);
	}
}

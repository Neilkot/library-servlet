package com.epam.lab.exam.library.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.epam.lab.exam.library.model.Author;

public class AuthorDaoTest extends AbstractDaoTest {
	
	private static final String NAME = "ARTHUR CONAN DOYLE";
	
	private AuthorDao authorDao = AuthorDao.getInstance();

	@Test
	public void shouldCreateAndRead() throws SQLException{
		Author author = new Author();
		author.setName(NAME);
		Integer id = (authorDao.create(getConnection(), author));
		author.setId(id);
		Author actual = authorDao.read(getConnection(), id);
		assertEquals(author, actual);
	}
	
	@Test
	public void shouldUpdate() throws SQLException {
		Author author = new Author();
		author.setName(NAME);
		Integer id = (authorDao.create(getConnection(), author));
		author.setId(id);
		author.setName("FOO BAR");
		authorDao.update(getConnection(), author);
		Author actual = authorDao.read(getConnection(), id);
		assertEquals(author, actual);
	}
	
	@Test
	public void shouldDelete() throws SQLException {
		Author author = new Author();
		author.setName(NAME);
		Integer id = (authorDao.create(getConnection(), author));
		assertNotNull(authorDao.read(getConnection(), id));
		authorDao.delete(getConnection(), id);
		Author actual = authorDao.read(getConnection(), id);
		assertNull(actual);
	}

}

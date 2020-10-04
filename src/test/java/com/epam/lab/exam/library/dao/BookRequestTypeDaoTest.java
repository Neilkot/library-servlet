package com.epam.lab.exam.library.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;

public class BookRequestTypeDaoTest extends AbstractDaoTest {

	private static final BookRequestTypeDao bookRequestTypeDao = BookRequestTypeDao.getInstance();

	@Test
	public void shouldCreateAndRead() throws SQLException {
		BookRequestType type = new BookRequestType();
		type.setType(RequestType.READING_AREA);
		Integer id = bookRequestTypeDao.create(getConnection(), type);
		type.setId(id);

		BookRequestType actual = bookRequestTypeDao.read(getConnection(), id);
		assertEquals(type, actual);
	}
	
	@Test
	public void shouldUpdate() throws SQLException{
		BookRequestType type = new BookRequestType();
		type.setType(RequestType.READING_AREA);
		Integer id = bookRequestTypeDao.create(getConnection(), type);
		type.setId(id);
		
		type.setType(RequestType.ABONEMENT);
		bookRequestTypeDao.update(getConnection(), type);
		BookRequestType actual = bookRequestTypeDao.read(getConnection(), id);
		assertEquals(type, actual);
	}
	
	@Test
	public void shouldDelete() throws SQLException{
		BookRequestType type = new BookRequestType();
		type.setType(RequestType.READING_AREA);
		Integer id = bookRequestTypeDao.create(getConnection(), type);
		bookRequestTypeDao.delete(getConnection(), id);
		BookRequestType actual = bookRequestTypeDao.read(getConnection(), id);
		assertNull(actual);
	}

}

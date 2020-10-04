package com.epam.lab.exam.library.service;

import java.sql.SQLException;

import org.junit.Test;

import com.epam.lab.exam.library.dao.AbstractDaoTest;

public class BookServiceTest extends AbstractDaoTest {
	
	private final BookService bookService = BookService.getInstance();
	

	@Test
	public void shouldReturnAllAvaliableBooks() throws SQLException {
		//BookRequestService.getInstance().getPendingNonBlockedReaderRequests(0, 1);
	}

}

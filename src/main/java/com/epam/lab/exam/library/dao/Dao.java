package com.epam.lab.exam.library.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface Dao<E, ID> {
	
	ID create(Connection connection, E element) throws SQLException;

	E read(Connection connection, ID id) throws SQLException;

	void update(Connection connection, E element) throws SQLException;
	
	void delete(Connection connection, ID id) throws SQLException;

}

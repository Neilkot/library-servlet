package com.epam.lab.exam.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.constants.DB;
import com.epam.lab.exam.library.model.Author;

public class AuthorDao implements Dao<Author, Integer> {

	private static final AuthorDao INSTANCE = new AuthorDao();
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	private AuthorDao() {
	}

	public static AuthorDao getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer create(Connection connection, Author element) throws SQLException {
		String sql = String.format("INSERT INTO %s (%s) VALUES(?);", DB.TABLE_AUTHOR, DB.AUTHOR_NAME);
		logger.debug("Executing query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, element.getName());
			pst.executeUpdate();

			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					logger.warn("Error creating author. Prepared statement returned zero rows modified");
					throw new SQLException("Couldn't create author entity");
				}
			}
		}
	}

	@Override
	public Author read(Connection connection, Integer id) throws SQLException {
		Author author = null;
		String sql = String.format("SELECT * from %s WHERE %s = ?;", DB.TABLE_AUTHOR, DB.AUTHOR_ID);
		logger.debug("Executing query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				author = new Author();
				author.setId(rs.getInt(DB.AUTHOR_ID));
				author.setName(rs.getString(DB.AUTHOR_NAME));
			}
			return author;
		}
	}

	@Override
	public void update(Connection connection, Author element) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ?  WHERE %s = ?;", DB.TABLE_AUTHOR, DB.AUTHOR_NAME,
				DB.AUTHOR_ID);
		logger.debug("Executing query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, element.getName());
			pst.setInt(2, element.getId());
			pst.executeUpdate();
		}
	}

	@Override
	public void delete(Connection connection, Integer id) throws SQLException {
		String sql = String.format("DELETE FROM %s WHERE %s = ?;", DB.TABLE_AUTHOR, DB.AUTHOR_ID);
		logger.debug("Executing query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.executeUpdate();
		}
	}

	public Author getByName(Connection connection, String name) throws SQLException {
		Author author = null;
		String sql = String.format("SELECT * from %s WHERE %s = ?;", DB.TABLE_AUTHOR, DB.AUTHOR_NAME);
		logger.debug("Executing query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, name);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				author = new Author();
				author.setId(rs.getInt(DB.AUTHOR_ID));
				author.setName(rs.getString(DB.AUTHOR_NAME));
			}
			return author;
		}
	}
}

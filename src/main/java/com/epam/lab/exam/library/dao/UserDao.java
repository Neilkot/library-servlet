package com.epam.lab.exam.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.constants.DB;
import com.epam.lab.exam.library.model.User;
import com.epam.lab.exam.library.util.DBHelper;

public class UserDao implements Dao<User, Integer>, CountableDao {

	private static final UserDao INSTANCE = new UserDao();
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	private UserDao() {
	}

	public static UserDao getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer create(Connection connection, User element) throws SQLException {

		String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES(?,?,?,?,?,?);", DB.TABLE_USER,
				DB.USER_LOGIN, DB.USER_CHECKSUM, DB.USER_FIRST_NAME, DB.USER_LAST_NAME, DB.USER_ROLE_ID,
				DB.USER_IS_BLOCKED);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, element.getLogin());
			pst.setString(2, element.getChecksum());
			pst.setString(3, element.getFirstName());
			pst.setString(4, element.getLastName());
			pst.setInt(5, element.getRoleId());
			pst.setBoolean(6, element.getIsBlocked());

			pst.executeUpdate();

			try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				} else {
					logger.warn("Error creating roleDao. Prepared statement returned zero rows modified");
					throw new SQLException("Couldn't create user entity");
				}
			}
		}
	}

	@Override
	public User read(Connection connection, Integer id) throws SQLException {
		User user = null;
		String sql = String.format("SELECT * FROM %s WHERE %s = ?;", DB.TABLE_USER, DB.USER_ID);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					user = DBHelper.getUser(rs);
				}
				return user;
			}
		}
	}

	@Override
	public void update(Connection connection, User element) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ?, %s = ?,%s = ?,%s = ?,%s = ?,%s = ? WHERE %s = ?;",
				DB.TABLE_USER, DB.USER_LOGIN, DB.USER_CHECKSUM, DB.USER_FIRST_NAME, DB.USER_LAST_NAME, DB.USER_ROLE_ID,
				DB.USER_IS_BLOCKED, DB.USER_ID);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, element.getLogin());
			pst.setString(2, element.getChecksum());
			pst.setString(3, element.getFirstName());
			pst.setString(4, element.getLastName());
			pst.setInt(5, element.getRoleId());
			pst.setBoolean(6, element.getIsBlocked());
			pst.setInt(7, element.getId());
			pst.executeUpdate();
		}
	}

	@Override
	public void delete(Connection connection, Integer id) throws SQLException {
		String sql = String.format("DELETE FROM %s WHERE %s = ?", DB.TABLE_USER, DB.USER_ID);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.executeUpdate();
		}
	}

	public void updateIsBlocked(Connection connection, Integer id, Boolean isBlocked) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?", DB.TABLE_USER, DB.USER_IS_BLOCKED, DB.USER_ID);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setBoolean(1, isBlocked);
			pst.setInt(2, id);
			pst.executeUpdate();
		}
	}

	public User getByLogin(Connection connection, String login) throws SQLException {
		String sql = String.format("SELECT * from %s WHERE %s = ?", DB.TABLE_USER, DB.USER_LOGIN);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, login);
			try (ResultSet rs = pst.executeQuery()) {
				User user = null;
				if (rs.next()) {
					user = DBHelper.getUser(rs);
				}
				return user;
			}
		}

	}

	@Override
	public String getTableName() {
		return DB.TABLE_USER;
	}

	public List<User> getByRole(Connection connection, int pageSize, int offset, Integer roleId) throws SQLException {
		List<User> users = new ArrayList<>();
		String sql = String.format("SELECT * FROM %s WHERE %s = ? ORDER BY %s LIMIT %d OFFSET %d;",
				DB.TABLE_USER, DB.USER_ROLE_ID, DB.USER_ID, pageSize, offset);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, roleId);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					User user = DBHelper.getUser(rs);
					users.add(user);
				}
			}
		}
		return users;
	}
}

package com.epam.lab.exam.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.constants.DB;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;

public class RoleDao implements Dao<Role, Integer> {

	private static final RoleDao INSTANCE = new RoleDao();
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	private RoleDao() {
	}

	public static RoleDao getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer create(Connection connection, Role element) throws SQLException {
		String sql = String.format("INSERT INTO %s (%s) VALUES(?)", DB.TABLE_ROLE, DB.ROLE_TYPE);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, element.getType().toString());
			pst.executeUpdate();

			try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				} else {
					logger.warn("Error creating roleDao. Prepared statement returned zero rows modified");
					throw new SQLException("Couldn't create role entity");
				}
			}
		}
	}

	@Override
	public Role read(Connection connection, Integer id) throws SQLException {
		Role role = null;
		String sql = String.format("Select * from %s WHERE %s = ?", DB.TABLE_ROLE, DB.ROLE_ID);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					role = new Role();
					role.setId(rs.getInt(DB.ROLE_ID));
					role.setType(RoleType.valueOf(rs.getString(DB.ROLE_TYPE)));
				}
				return role;
			}
		}
	}

	@Override
	public void update(Connection connection, Role element) throws SQLException {
		String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?;", DB.TABLE_ROLE, DB.ROLE_TYPE, DB.ROLE_ID);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, element.getType().toString());
			pst.setInt(2, element.getId());
			pst.executeUpdate();
		}
	}

	@Override
	public void delete(Connection connection, Integer id) throws SQLException {
		String sql = String.format("DELETE FROM %s WHERE %s = ?", DB.TABLE_ROLE, DB.ROLE_ID);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.executeUpdate();
		}
	}

	public Role getByType(Connection connection, RoleType type) throws SQLException {
		Role role = null;
		String sql = String.format("SELECT * from %s WHERE %s = ?;", DB.TABLE_ROLE, DB.ROLE_TYPE);
		logger.debug("Executing sql query: {}", sql);
		try (PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.setString(1, type.toString());
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					role = new Role();
					role.setId(rs.getInt(DB.ROLE_ID));
					role.setType(RoleType.valueOf(rs.getString(DB.ROLE_TYPE)));
				}
			}
		}
		return role;
	}
}

package com.epam.lab.exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class ManualTest {

	@Test
	public void testUTF8CharactersInDB() throws Exception {
		Connection connection = getConnection(
				"jdbc:mysql://root:qwe98&*12@localhost/library?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8");
		//PreparedStatement pst = connection.prepareStatement("INSERT INTO users (login,first_name,last_name,checksum,role_id) VALUES('русиш','fname','lname','1234',1);");
		//pst.executeUpdate();
	
		
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("select * from users;");
		while(rs.next()) {
			System.out.println(rs.getString("login"));
		}
		
	}

	public Connection getConnection(String connectionUrl) throws SQLException {
		return DriverManager.getConnection(connectionUrl);
	}
}

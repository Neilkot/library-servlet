package com.epam.lab.exam.library.dto;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class CreateUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String login;
	private String checksum;
	private String firstName;
	private String lastName;

	@Override
	public String toString() {
		return "CreateUserDTO [login=" + login + ", checksum=" + checksum + ", firstName=" + firstName + ", lastName="
				+ lastName + "]";
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public static CreateUserDTO from(HttpServletRequest request) {
		CreateUserDTO dto = new CreateUserDTO();
		dto.setLogin(request.getParameter("login"));
		dto.setChecksum(request.getParameter("checksum"));
		dto.setFirstName(request.getParameter("firstName"));
		dto.setLastName(request.getParameter("lastName"));
		return dto;
	}

}

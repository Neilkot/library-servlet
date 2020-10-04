package com.epam.lab.exam.library.dto;

import javax.servlet.http.HttpServletRequest;

public class LoginDTO {
	
	private String login;
	private String checksum;
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
	@Override
	public String toString() {
		return "LoginDTO [login=" + login + ", checksum=" + checksum + "]";
	}
	
	public static LoginDTO from (HttpServletRequest request) {
		LoginDTO dto = new LoginDTO();
		
		dto.setLogin(request.getParameter("login"));
		dto.setChecksum(request.getParameter("checksum"));
		return dto;
	}

}

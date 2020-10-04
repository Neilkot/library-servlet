package com.epam.lab.exam.library.dto;

import java.io.Serializable;

public class UserSessionDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer userId;
	private String username;
	private Integer roleId;
	private String roleName;

	public UserSessionDTO() {
	}

	@Override
	public String toString() {
		return "UserSessionDTO [userId=" + userId + ", username=" + username + ", roleId=" + roleId + ", roleName="
				+ roleName + "]";
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}

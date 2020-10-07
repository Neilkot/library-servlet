package com.epam.lab.exam.library.util;

import com.epam.lab.exam.library.dto.CreateBookDTO;
import com.epam.lab.exam.library.dto.CreateUserDTO;
import com.epam.lab.exam.library.dto.LoginDTO;

public class Formatter {

	public static LoginDTO format(LoginDTO input) {
		String login = input.getLogin().trim();
		String checksum = input.getChecksum().trim();
		LoginDTO dto = new LoginDTO();
		dto.setLogin(login);
		dto.setChecksum(checksum);
		return dto;
	}

	public static CreateUserDTO format(CreateUserDTO input) {
		String login = input.getLogin().trim();
		String checksum = input.getChecksum().trim();
		String firstName = input.getFirstName().trim();
		String lastName = input.getLastName().trim();
		CreateUserDTO dto = new CreateUserDTO();
		dto.setLogin(login);
		dto.setChecksum(checksum);
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		return dto;
	}

	public static CreateBookDTO format(CreateBookDTO input) {
		String name = input.getName().trim();
		String authorName = input.getAuthorName().trim();
		String publisher = input.getPublisher().trim();
		String imgLink = input.getImgLink().trim();
		input.setName(name);
		input.setAuthorName(authorName);
		input.setPublisher(publisher);
		input.setImgLink(imgLink);
		return input;
	}

}

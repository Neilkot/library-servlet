package com.epam.lab.exam.library.util;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.epam.lab.exam.library.dto.BookDTO;
import com.epam.lab.exam.library.dto.BookRequestDTO;
import com.epam.lab.exam.library.dto.CreateUserDTO;
import com.epam.lab.exam.library.dto.LoginDTO;
import com.epam.lab.exam.library.dto.SubmitRequestDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;

public class Validator {
	
	public static void validate(LoginDTO dto) throws ClientRequestException {
		if (dto == null) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getLogin())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getChecksum())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}

	}
	
	public static void validate(Integer publishedYear) throws ClientRequestException {
		if (publishedYear == null || publishedYear <= 0) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
	}
	
	public static void validatMustBeNubmer (String text) throws ClientRequestException {
		if (text == null ) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (!(text.matches("[0-9]+") && text.length() > 2)) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
	}
	
	public static void validate(Integer pageSize, Integer offset) throws ClientRequestException {
		if (pageSize == null || pageSize <= 0) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (offset == null || offset < 1) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
	}

	public static void validate(CreateUserDTO dto) throws ClientRequestException {
		if (dto == null) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getLogin())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getChecksum())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getFirstName())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getLastName())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
	}

	public static void validateUpdate(BookDTO dto) throws ClientRequestException {
		validate(dto);
		if (dto.getBookId() == null) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
	}

	public static void validate(BookDTO dto) throws ClientRequestException {
		if (dto == null) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getAuthorName())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getImgLink())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getName())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getPublisher())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (dto.getPublishedYear() == null || dto.getPublishedYear() < 0) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
	}

	public static void validate(BookRequestDTO dto) throws ClientRequestException {
		if (dto == null) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (dto.getRequestId() == null || dto.getRequestId() < 0) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getUsername())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getBookName())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(dto.getAuthorName())) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (dto.getRequestType() == null) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
	}

	public static void validate(SubmitRequestDTO dto) throws ClientRequestException {
		if (dto == null) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (dto.getRequestType() == null) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}
		if (dto.getBookId() == null || dto.getBookId() < 0) {
			throw new ClientRequestException(ErrorType.BAD_REQUEST);
		}

	}
	
}

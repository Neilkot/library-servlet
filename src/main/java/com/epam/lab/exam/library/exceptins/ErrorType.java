package com.epam.lab.exam.library.exceptins;

public enum ErrorType {

	USER_NOT_FOUND("userNotFound", 404), BOOK_REQUEST_NOT_FOUND("bookRequestNotFound", 404), LOGIN_IN_USE("loginInUse", 422), BOOK_NOT_FOUND("bookNotFound", 404),
	BAD_REQUEST("badRequest", 400), INTERNAL_SERVER_ERROR("internalServerError", 500),
	ALREADY_LOGGED_IN("alreadyLoggedIn", 422), UNAUTHORIZED("unauthorized", 401), FORBIDDEN("forbidden", 403),
	METHOD_NOT_ALLOWED("methodNotAllowed", 405), BOOK_NOT_AVALIABLE("bookNotAvaliable", 422),
	REQUEST_NOT_FOUND("requestNotFound", 404);

	String messageCode;
	int errorCode;

	ErrorType(String messageCode, int errorCode) {
		this.messageCode = messageCode;
		this.errorCode = errorCode;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}

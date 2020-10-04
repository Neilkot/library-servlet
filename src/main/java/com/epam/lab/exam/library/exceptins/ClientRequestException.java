package com.epam.lab.exam.library.exceptins;

public class ClientRequestException extends Exception {

	private static final long serialVersionUID = 1L;

	private ErrorType type;

	public ClientRequestException(ErrorType type) {
		this(type.getMessageCode(), null, type);
	}

	public ClientRequestException(String message, Exception cause, ErrorType type) {
		super(message, cause);
		this.type = type;
	}

	public ErrorType getType() {
		return type;
	}

	public int getErrorCode() {
		return type.getErrorCode();
	}
	
	public String getMessageCode() {
		return type.getMessageCode();
	}
}

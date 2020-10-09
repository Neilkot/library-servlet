package com.epam.lab.exam.library.dto;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.epam.lab.exam.library.model.RequestType;

public class SubmitRequestDTO {
	private Integer bookId;
	private RequestType requestType;

	public static SubmitRequestDTO from(HttpServletRequest request) {
		SubmitRequestDTO dto = new SubmitRequestDTO();
		dto.setBookId(Optional.ofNullable(request.getParameter("bookId")).map(Integer::parseInt).orElse(null));
		dto.setRequestType(Optional.ofNullable(request.getParameter("requestType")).filter(v -> !v.isEmpty())
				.map(RequestType::valueOf).orElse(null));
		return dto;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	@Override
	public String toString() {
		return "SubmitRequestDTO [bookId=" + bookId + ", requestType=" + requestType + "]";
	}

}

package com.epam.lab.exam.library.dto;

import java.time.Instant;

import com.epam.lab.exam.library.model.RequestType;

public class BookRequestDTO {

	private Integer requestId;
	private Integer bookId;
	private String username;
	private String bookName;
	private String authorName;
	private RequestType requestType;
	private Instant createDate;
	private Instant approveDate;
	private Instant expirationDate;
	private Instant returnDate;
	private Float fee;

	public Instant getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Instant approvedDate) {
		this.approveDate = approvedDate;
	}

	public Instant getExpirationDate() {
		return expirationDate;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public void setExpirationDate(Instant expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Instant getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Instant returnDate) {
		this.returnDate = returnDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public Instant getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Instant createDate) {
		this.createDate = createDate;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public Float getFee() {
		return fee;
	}

	public void setFee(Float fee) {
		this.fee = fee;
	}

}

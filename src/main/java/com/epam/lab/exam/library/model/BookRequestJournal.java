package com.epam.lab.exam.library.model;

import java.time.Instant;

public class BookRequestJournal {
	
	private Integer id;
	private Integer bookRequestId;
	private Instant createDate;
	private Instant approveDate;
	private Instant expirationDate;
	private Instant returnDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBookRequestId() {
		return bookRequestId;
	}
	public void setBookRequestId(Integer bookRequestId) {
		this.bookRequestId = bookRequestId;
	}
	public Instant getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Instant createDate) {
		this.createDate = createDate;
	}
	public Instant getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Instant approveDate) {
		this.approveDate = approveDate;
	}
	public Instant getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Instant returnDate) {
		this.returnDate = returnDate;
	}
	public Instant getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Instant expirationDate) {
		this.expirationDate = expirationDate;
	}
	@Override
	public String toString() {
		return "BookRequestJournal [id=" + id + ", bookRequestId=" + bookRequestId + ", createDate=" + createDate
				+ ", approveDate=" + approveDate + ", expirationDate=" + expirationDate + ", returnDate=" + returnDate
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approveDate == null) ? 0 : approveDate.hashCode());
		result = prime * result + ((bookRequestId == null) ? 0 : bookRequestId.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((returnDate == null) ? 0 : returnDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookRequestJournal other = (BookRequestJournal) obj;
		if (approveDate == null) {
			if (other.approveDate != null)
				return false;
		} else if (!approveDate.equals(other.approveDate))
			return false;
		if (bookRequestId == null) {
			if (other.bookRequestId != null)
				return false;
		} else if (!bookRequestId.equals(other.bookRequestId))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (returnDate == null) {
			if (other.returnDate != null)
				return false;
		} else if (!returnDate.equals(other.returnDate))
			return false;
		return true;
	}
	
}

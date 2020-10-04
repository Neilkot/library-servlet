package com.epam.lab.exam.library.model;

public class BookRequest {

	private Integer id;
	private Integer userId;
	private Integer bookItemId;
	private Integer bookRequestTypeId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getBookItemId() {
		return bookItemId;
	}
	public void setBookItemId(Integer bookItemId) {
		this.bookItemId = bookItemId;
	}
	public Integer getBookRequestTypeId() {
		return bookRequestTypeId;
	}
	public void setBookRequestTypeId(Integer bookRequestTypeId) {
		this.bookRequestTypeId = bookRequestTypeId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookItemId == null) ? 0 : bookItemId.hashCode());
		result = prime * result + ((bookRequestTypeId == null) ? 0 : bookRequestTypeId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		BookRequest other = (BookRequest) obj;
		if (bookItemId == null) {
			if (other.bookItemId != null)
				return false;
		} else if (!bookItemId.equals(other.bookItemId))
			return false;
		if (bookRequestTypeId == null) {
			if (other.bookRequestTypeId != null)
				return false;
		} else if (!bookRequestTypeId.equals(other.bookRequestTypeId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BookRequest [id=" + id + ", userId=" + userId + ", bookItemId=" + bookItemId + ", bookRequestTypeId="
				+ bookRequestTypeId + "]";
	}
}

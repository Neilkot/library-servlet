package com.epam.lab.exam.library.model;

public class Book {

	private Integer id;
	private String name;
	private Integer authorid;
	private String publisher;
	private Integer publishedYear;
	private String imgLink;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAuthorid() {
		return authorid;
	}

	public void setAuthorid(Integer authorid) {
		this.authorid = authorid;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Integer getPublishedYear() {
		return publishedYear;
	}

	public void setPublishedYear(Integer publishedYear) {
		this.publishedYear = publishedYear;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorid == null) ? 0 : authorid.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imgLink == null) ? 0 : imgLink.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((publishedYear == null) ? 0 : publishedYear.hashCode());
		result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
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
		Book other = (Book) obj;
		if (authorid == null) {
			if (other.authorid != null)
				return false;
		} else if (!authorid.equals(other.authorid))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imgLink == null) {
			if (other.imgLink != null)
				return false;
		} else if (!imgLink.equals(other.imgLink))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (publishedYear == null) {
			if (other.publishedYear != null)
				return false;
		} else if (!publishedYear.equals(other.publishedYear))
			return false;
		if (publisher == null) {
			if (other.publisher != null)
				return false;
		} else if (!publisher.equals(other.publisher))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", authorid=" + authorid + ", publisher=" + publisher
				+ ", publishedYear=" + publishedYear + ", imgLink=" + imgLink + "]";
	}

}

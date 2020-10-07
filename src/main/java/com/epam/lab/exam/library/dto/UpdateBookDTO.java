package com.epam.lab.exam.library.dto;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.epam.lab.exam.library.exceptins.ClientRequestException;

public class UpdateBookDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer bookId;
	private Integer bookItemId;
	private String name;
	private String authorName;
	private String publisher;
	private Integer publishedYear;
	private String imgLink;

	public static UpdateBookDto createBookDTOfrom(HttpServletRequest request) throws ClientRequestException {
		UpdateBookDto dto = new UpdateBookDto();
		String bookId = request.getParameter("bookId");
		if (StringUtils.isNotBlank(bookId)) {
			dto.setBookId(Integer.parseInt(bookId));
		}
		String bookItemId = request.getParameter("bookItemId");
		if (StringUtils.isNotBlank(bookItemId)) {
			dto.setBookItemId(Integer.parseInt(bookItemId));
		}
		dto.setName(request.getParameter("bookName"));
		dto.setAuthorName(request.getParameter("author_name"));
		dto.setPublisher(request.getParameter("publisher"));
		String publishedYear = request.getParameter("published_year");
		if (StringUtils.isNotBlank(publishedYear)) {
			dto.setPublishedYear(Integer.parseInt(publishedYear));
		}
		dto.setImgLink(request.getParameter("image_link"));
		return dto;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public Integer getBookItemId() {
		return bookItemId;
	}

	public void setBookItemId(Integer bookItemId) {
		this.bookItemId = bookItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
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
	public String toString() {
		return "UpdateBookDTO [bookId=" + bookId + ", bookItemId=" + bookItemId + ", name=" + name + ", authorName="
				+ authorName + ", publisher=" + publisher + ", publishedYear=" + publishedYear + ", imgLink=" + imgLink
				+ "]";
	}

}

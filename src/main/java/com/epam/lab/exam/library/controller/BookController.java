package com.epam.lab.exam.library.controller;

import static com.epam.lab.exam.library.constants.HTTP.ERROR_PAGE;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.lab.exam.library.dto.BookDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.service.BookService;
import com.epam.lab.exam.library.util.Validator;

@WebServlet({ "/reader-books"})
public class BookController extends AbstractController {

	private static final long serialVersionUID = 1L;

	private final BookService bookService = BookService.getInstance();

	public BookController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Integer pageSize = parsePageSizeParameter(request);
			Integer offset = parseOffsetParameter(request);
			logger.info("loading books page. pageSize={} offset={}", pageSize, offset);

			Validator.validate(pageSize, offset);
			int noOfRecords = bookService.getBooksCount();
			int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / pageSize);
			List<BookDTO> books = bookService.getAllAvaliableBooks(pageSize, offset - 1);
			logger.debug("pageSize={}, offset={}, noOfRecords={}, noOfPages={}", pageSize, offset, noOfRecords,
					noOfPages);
			request.setAttribute("noOfRecords", noOfPages);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("offset", offset);
			request.setAttribute("books", books);
			request.getRequestDispatcher("/jsp/reader-books.jsp").forward(request, response);
		} catch (ClientRequestException e) {
			handleError(request, response, ERROR_PAGE, e.getType(), e);
		} catch (Exception e) {
			e.printStackTrace();
			handleError(request, response, ERROR_PAGE, ErrorType.INTERNAL_SERVER_ERROR, e);
		}
	}

}

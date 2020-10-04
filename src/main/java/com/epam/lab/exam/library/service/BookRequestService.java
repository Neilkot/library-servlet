package com.epam.lab.exam.library.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.dao.BookRequestDao;
import com.epam.lab.exam.library.dao.BookRequestJournalDao;
import com.epam.lab.exam.library.db.DBManager;
import com.epam.lab.exam.library.db.DBManagerContainer;
import com.epam.lab.exam.library.dto.BookRequestDTO;
import com.epam.lab.exam.library.dto.PendingRequestDTO;
import com.epam.lab.exam.library.dto.SubmitRequestDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.BookRequestJournal;
import com.epam.lab.exam.library.model.RequestType;
import com.epam.lab.exam.library.model.User;

public class BookRequestService {

	private static final BookRequestService INSTANCE = new BookRequestService();

	private final DBManager dbManager = DBManagerContainer.getInstance().getdBManager();
	private final BookRequestDao bookRequestDao = BookRequestDao.getInstance();
	private final RequestExpirationService requestExpirationService = RequestExpirationService.getInstance();
	private final UserService userService = UserService.getInstance();

	private final Logger logger = LogManager.getLogger(this.getClass());

	private BookRequestService() {
	}

	public static BookRequestService getInstance() {
		return INSTANCE;
	}

	public List<PendingRequestDTO> getPendingNonBlockedReaderRequests(int pageSize, int offset) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookRequestDao.getPendingNonBlockedReaderRequests(connection, pageSize, offset);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public int getPendinCount() throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookRequestDao.getPendingCount(connection);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public void returnBook(Integer bookRequestId) throws SQLException, ClientRequestException {
		Connection connection = dbManager.getConnection();
		try {
			BookRequestJournal journal = BookRequestJournalDao.getInstance().getByBookRequest(connection,
					bookRequestId);
			if (journal != null) {
				Instant returnDate = Instant.now();
				BookRequestJournalDao.getInstance().returnBook(connection, journal.getId(), returnDate);
			}
			// TODO Throw Exception, when not found 28.09
			else {
				logger.warn("Requested return book for non existing BookRequestJournal entry. bookRequestId={}",
						bookRequestId);
				throw new ClientRequestException(ErrorType.BOOK_REQUEST_NOT_FOUND);
			}
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public void delete(Integer id) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			bookRequestDao.delete(connection, id);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public void approve(Integer bookRequestId) throws SQLException, ClientRequestException {
		Connection connection = dbManager.getConnection();
		try {
			BookRequest bookRequest = bookRequestDao.read(connection, bookRequestId);
			if (bookRequest == null) {
				logger.warn("Approving request for non existing bookRequestId={}", bookRequestId);
				throw new ClientRequestException(ErrorType.REQUEST_NOT_FOUND);
			}
			RequestType requestType = BookRequestTypeService.getInstance().getById(bookRequest.getBookRequestTypeId())
					.getType();
			Instant approveDate = Instant.now();
			Instant expirationDate = requestExpirationService.calculateExpirationDate(requestType);
			BookRequestJournalDao.getInstance().setApprovedDate(connection, bookRequestId, approveDate, expirationDate);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public List<BookRequestDTO> getApprovedRequests(Integer pageSize, Integer offset) throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookRequestDao.getApprovedNonBlockedReaderRequests(connection, pageSize, offset);
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

	public List<BookRequestDTO> getUserApprovedRequests(Integer userId, Integer pageSize, Integer offset)
			throws SQLException {
		Connection connection = dbManager.getConnection();
		try {
			return bookRequestDao.getUserApprovedRequests(connection, userId, pageSize, offset);
		} finally {
			dbManager.releaseConnection(connection);
		}

	}

	public void submitRequest(Integer userId, SubmitRequestDTO request) throws SQLException, ClientRequestException {
		Connection connection = dbManager.getConnection();
		try {
			User user = userService.getUser(userId);
			if (user == null) {
				logger.warn("not user found={}", userId);
				throw new ClientRequestException(ErrorType.USER_NOT_FOUND);
			}
			if (BooleanUtils.isTrue(user.getIsBlocked())) {
				logger.warn("user is blocked", userId);
				throw new ClientRequestException(ErrorType.UNAUTHORIZED);
			}

			connection.setAutoCommit(false);

			List<BookItem> bookItems = BookService.getInstance().getAllAvaliableBooks(request.getBookId());
			if (bookItems.isEmpty()) {
				logger.warn("No avaliable books found for SubmitRequestDTO={}", request);
				throw new ClientRequestException(ErrorType.BOOK_NOT_AVALIABLE);
			}

			BookRequest bookRequest = new BookRequest();
			bookRequest.setUserId(userId);
			Integer requestTypeId = BookRequestTypeService.getInstance().getByType(request.getRequestType()).getId();
			bookRequest.setBookRequestTypeId(requestTypeId);
			bookRequest.setBookItemId(bookItems.get(0).getId());
			bookRequest.setId(bookRequestDao.create(connection, bookRequest));

			BookRequestJournal journal = new BookRequestJournal();
			journal.setBookRequestId(bookRequest.getId());
			journal.setCreateDate(Instant.now());
			BookRequestJournalDao.getInstance().create(connection, journal);

			connection.commit();
		} catch (SQLException e) {
			logger.warn("Rolling back transaction for SubmitRequestDTO={}", request);
			connection.rollback();
			throw e;
		} finally {
			dbManager.releaseConnection(connection);
		}
	}

}

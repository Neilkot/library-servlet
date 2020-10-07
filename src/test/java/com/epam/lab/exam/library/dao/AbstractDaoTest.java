package com.epam.lab.exam.library.dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.BeforeClass;

import com.epam.lab.exam.TestDBManager;
import com.epam.lab.exam.library.db.DBManagerContainer;
import com.epam.lab.exam.library.model.Author;
import com.epam.lab.exam.library.model.Book;
import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.BookRequestJournal;
import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

public abstract class AbstractDaoTest {

	protected static final String LOGIN = "login-stub";
	protected static final String CHECKSUM = "checksum-stub";
	protected static final String FIRST_NAME = "first-name-stub";
	protected static final String LAST_NAME = "last-name-stub";
	protected static final Boolean IS_BLOCKED = false;

	protected static final String NAME_EFFECTIVE_JAVA = "Effective Java";
	protected static final String NAME_CLEAN_CODE = "Clean Code";
	protected static final String AUTHER_NAME_BLOCK = "Joshua Bloch";
	protected static final String AUTHER_NAME_MARTIN = "Robert C.Martin";

	protected static final String PUBLISHER = "Pearson Education (US)";
	protected static final Integer PUBLISHED_YEAR = 2008;
	protected static final String IMANE_LINK = "https://d1w7fb2mkkr3kw.cloudfront.net/assets/images/book/lrg/9780/3213/9780321356680.jpg";

	private static final String CONNECTION_URL = "jdbc:h2:mem:library;DB_CLOSE_DELAY=-1";
	private static final DBManagerContainer container = DBManagerContainer.getInstance();

	private static boolean wasCalled = false;

	private Map<RoleType, Role> roles;
	private Map<RequestType, BookRequestType> bookRequestsTypes;

	enum CreateTable {
		CREATE_TABLE_ROLES,
		CREATE_TABLE_USERS,
		CREATE_TABLE_AUTHORS,
		CREATE_TABLE_BOOKS,
		CREATE_TABLE_BOOK_ITEMS,
		CREATE_TABLE_BOOK_REQUEST_TYPES,
		CREATE_TABLE_BOOK_REQUESTS,
		CREATE_TABLE_BOOK_REQUESTS_JOURNALS;
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		if (!wasCalled) {
			wasCalled = true;

			Class.forName("org.h2.Driver");

			TestDBManager manager = new TestDBManager(CONNECTION_URL);
			container.setdBManager(manager);

			// Arrays.stream(CreateTable.values()).forEach(AbstractDaoTest::createTable);
			// TODO: implement Enum and files
			createRoleTable();
			createUserTable();
			createAuthorTable();
			createBookTable();
			createBookItemTable();
			createBookRequestTypeTable();
			createBookRequestTable();
			createBookRequestJournalTable();
		}
	}

	@SuppressWarnings("unused")
	private static void createTable(CreateTable createTable) {
		String tableName = createTable.toString().toLowerCase();
		try {
			String sql = readFileAsText(tableName + ".sql");
			try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
				pst.executeUpdate();
			}
		} catch (Exception e) {
			throw new IllegalStateException("could not create table " + tableName);
		}

	}

	private static String readFileAsText(String path) throws IOException {
		return Files.readAllLines(Paths.get(AbstractDaoTest.class.getClassLoader().getResource(path).getPath()),
				StandardCharsets.UTF_8).stream().collect(Collectors.joining());
	}

	@Before
	public void beforeEach() throws SQLException {
		deleteBookRequestJournalTable();
		deleteBookRequestTable();
		deleteBookRequestTypeTable();
		deleteBookItemTable();
		deleteBookTable();
		deleteAuthorTable();
		deleteUserTable();
		deleteRoleTable();
	}

	protected Role getRole(RoleType type) {
		return roles.get(type);
	}

	protected BookRequestType getBookRequestType(RequestType type) {
		return bookRequestsTypes.get(type);
	}

	protected void fillUserRoles() throws SQLException {
		List<Role> items = new ArrayList<>();
		for (RoleType item : RoleType.values()) {
			Role role = new Role();
			role.setType(item);
			Integer id = RoleDao.getInstance().create(getConnection(), role);
			role.setId(id);
			items.add(role);
		}
		roles = items.stream().collect(Collectors.toMap(r -> r.getType(), Function.identity()));
	}

	protected void fillBookRequestTypes() throws SQLException {
		List<BookRequestType> items = new ArrayList<>();
		for (RequestType item : RequestType.values()) {
			BookRequestType type = new BookRequestType();
			type.setType(item);
			Integer id = BookRequestTypeDao.getInstance().create(getConnection(), type);
			type.setId(id);
			items.add(type);
		}
		bookRequestsTypes = items.stream().collect(Collectors.toMap(b -> b.getType(), Function.identity()));
	}

	protected BookRequest getBookRequest(User user, BookItem bookItem, BookRequestType bookRequestType)
			throws SQLException {
		BookRequest request = new BookRequest();
		request.setUserId(user.getId());
		request.setBookItemId(bookItem.getId());
		request.setBookRequestTypeId(bookRequestType.getId());
		return request;
	}

	protected BookRequest createBookRequest(User user, BookItem bookItem, BookRequestType bookRequestType)
			throws SQLException {
		BookRequest request = getBookRequest(user, bookItem, bookRequestType);
		request.setId(BookRequestDao.getInstance().create(getConnection(), request));
		return request;
	}

	protected Author getAuthor(String name) {
		Author author = new Author();
		author.setName(name);
		return author;
	}

	protected Author createAuthor(String name) throws SQLException {
		Author author = getAuthor(name);

		author.setId(AuthorDao.getInstance().create(getConnection(), author));
		return author;
	}

	protected Book getBook(Author author) {
		return getBook(author, NAME_EFFECTIVE_JAVA);
	}

	protected Book getBook(Author author, String name) {
		Book book = new Book();
		book.setName(name);
		book.setAuthorid(author.getId());
		book.setPublisher(PUBLISHER);
		book.setPublishedYear(PUBLISHED_YEAR);
		book.setImgLink(IMANE_LINK);
		return book;
	}

	protected Book createBook(String authorName) throws SQLException {
		return createBook(authorName, NAME_EFFECTIVE_JAVA);
	}

	protected Book createBook(String authorName, String name) throws SQLException {
		Author author = createAuthor(authorName);
		Book book = getBook(author, name);
		book.setId(BookDao.getInstance().create(getConnection(), book));
		return book;
	}

	protected BookItem createBookItem(String autherNameMartin) throws SQLException {
		return createBookItem(autherNameMartin, NAME_EFFECTIVE_JAVA);
	}

	protected BookItem createBookItem(String autherNameMartin, String name) throws SQLException {
		Book book = createBook(autherNameMartin, name);
		BookItem item = new BookItem();
		item.setBookId(book.getId());
		item.setId(BookItemDao.getInstance().create(getConnection(), item));
		return item;
	}

	public User getUser(RoleType roleType) {
		return getUser(roleType, LOGIN);
	}

	public User getUser(RoleType roleType, String login) {
		User user = new User();
		user.setLogin(login);
		user.setChecksum(CHECKSUM);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setIsBlocked(IS_BLOCKED);
		Role role = getRole(roleType);
		user.setRoleId(role.getId());
		return user;
	}

	public User createUser(RoleType roleType) throws SQLException {
		return createUser(roleType, LOGIN);
	}

	public User createUser(RoleType roleType, String login) throws SQLException {
		User user = getUser(roleType, login);
		user.setId(UserDao.getInstance().create(getConnection(), user));
		return user;
	}

	protected BookRequestJournal getBookRequestJournal(BookRequest bookRequest) throws SQLException {
		BookRequestJournal journal = new BookRequestJournal();
		journal.setBookRequestId(bookRequest.getId());
		journal.setCreateDate(Instant.now());
		journal.setApproveDate(Instant.now());
		journal.setExpirationDate(Instant.now());
		journal.setReturnDate(Instant.now());
		return journal;
	}

	private static void deleteBookRequestJournalTable() throws SQLException {
		String sql = "DELETE from book_requests_journals;";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void deleteBookRequestTable() throws SQLException {
		String sql = "DELETE from book_requests;";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void deleteBookRequestTypeTable() throws SQLException {
		String sql = "DELETE from book_request_types;";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private void deleteBookItemTable() throws SQLException {
		String sql = "DELETE FROM book_items;";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void deleteBookTable() throws SQLException {
		String sql = "DELETE FROM books;";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void deleteAuthorTable() throws SQLException {
		String sql = "DELETE FROM authors;";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void deleteUserTable() throws SQLException {
		String sql = "DELETE FROM users;";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void deleteRoleTable() throws SQLException {
		String sql = "DELETE FROM roles;";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void createRoleTable() throws SQLException {
		String sql = "CREATE TABLE roles("
				+ "id INT  AUTO_INCREMENT,`type` VARCHAR (35) NOT NULL UNIQUE, CONSTRAINT pk_role_id PRIMARY KEY (id));";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void createUserTable() throws SQLException {
		String sql = "CREATE TABLE users(\n" + "id INT AUTO_INCREMENT,\n" + "login VARCHAR (25) NOT NULL UNIQUE,\n"
				+ "checksum VARCHAR (200) NOT NULL,\n" + "first_name VARCHAR (35) NOT NULL,\n"
				+ "last_name VARCHAR (35) NOT NULL,\n" + "role_id INT DEFAULT 1 NOT NULL,\n"
				+ "is_blocked BOOLEAN DEFAULT FALSE,\n" + "CONSTRAINT pk_user_id PRIMARY KEY (id),\n"
				+ "CONSTRAINT fk_user_role_id FOREIGN KEY (role_id)\n"
				+ "REFERENCES roles (id) ON UPDATE CASCADE ON DELETE RESTRICT\n" + ");";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void createAuthorTable() throws SQLException {
		String sql = "CREATE TABLE authors (id INT AUTO_INCREMENT, name VARCHAR(100) NOT NULL UNIQUE, "
				+ "CONSTRAINT pk_author_id PRIMARY KEY (id));";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void createBookTable() throws SQLException {
		String sql = "CREATE TABLE books(\n" + "id INT AUTO_INCREMENT,\n" + "name VARCHAR (100) NOT NULL,\n"
				+ "author_id INT NOT NULL,\n" + "publisher VARCHAR(50) NOT NULL,\n" + "publish_year INT NOT NULL,\n"
				+ "image_link VARCHAR(150) NOT NULL,\n" + "CONSTRAINT pk_book_id PRIMARY KEY (id),\n"
				+ "CONSTRAINT uq_book_name UNIQUE KEY (name, author_id, publisher, publish_year),\n"
				+ "CONSTRAINT fk_book_author_id FOREIGN KEY (author_id)\n"
				+ "REFERENCES authors (id) ON UPDATE CASCADE ON DELETE RESTRICT\n" + ");";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void createBookItemTable() throws Exception {
		String sql = "CREATE TABLE book_items(\n" + "id INT AUTO_INCREMENT,\n" + "book_id INT NOT NULL,\n"
				+ "CONSTRAINT pk_book_items_id PRIMARY KEY (id),\n"
				+ "CONSTRAINT fk_book_items_books_id FOREIGN KEY (book_id) \n"
				+ "REFERENCES books (id) ON UPDATE CASCADE ON DELETE CASCADE\n" + ");";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void createBookRequestTypeTable() throws SQLException {
		String sql = "CREATE TABLE book_request_types(\n" + "id INT AUTO_INCREMENT,\n" + "type VARCHAR (50),\n"
				+ "CONSTRAINT pk_book_request_type_id PRIMARY KEY  (id)\n" + ");";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void createBookRequestTable() throws SQLException {
		String sql = "CREATE TABLE book_requests(\n" + "id INT AUTO_INCREMENT,\n" + "user_id INT NOT NULL,\n"
				+ "book_item_id INT NOT NULL,\n" + "book_request_type_id INT NOT NULL,\n"
				+ "CONSTRAINT pk_book_request_id PRIMARY KEY  (id),\n"
				+ "CONSTRAINT fk_book_request_user_id FOREIGN KEY (user_id)\n"
				+ "REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT,\n"
				+ "CONSTRAINT fk_book_request_book_item_id FOREIGN KEY (book_item_id)\n"
				+ "REFERENCES book_items (id) ON UPDATE CASCADE ON DELETE CASCADE,\n"
				+ "CONSTRAINT fk_book_request_type_id FOREIGN KEY (book_request_type_id)\n"
				+ "REFERENCES book_request_types (id) ON UPDATE CASCADE ON DELETE RESTRICT\n" + ");";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	private static void createBookRequestJournalTable() throws SQLException {
		String sql = "CREATE TABLE book_requests_journals(\n" + "id INT AUTO_INCREMENT,\n"
				+ "book_request_id INT NOT NULL,\n" + "create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
				+ "approve_date TIMESTAMP DEFAULT NULL,\n expiration_date TIMESTAMP DEFAULT NULL,\n"
				+ "return_date TIMESTAMP DEFAULT NULL,\n" + "CONSTRAINT pk_book_request_journal_id PRIMARY KEY  (id),\n"
				+ "CONSTRAINT fk_book_request_journal_book_request_id FOREIGN KEY (book_request_id)\n"
				+ "REFERENCES book_requests (id) ON UPDATE CASCADE ON DELETE CASCADE);";
		try (Connection connection = getConnection(); PreparedStatement pst = connection.prepareStatement(sql)) {
			pst.executeUpdate();
		}
	}

	protected static Connection getConnection() throws SQLException {
		return container.getdBManager().getConnection();
	}
}

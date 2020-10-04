package com.epam.lab.exam.library.constants;

public class DB {

	public static final String TABLE_USER = "users";
	public static final String USER_ID = "id";
	public static final String USER_LOGIN = "login";
	public static final String USER_CHECKSUM = "checksum";
	public static final String USER_FIRST_NAME = "first_name";
	public static final String USER_LAST_NAME = "last_name";
	public static final String USER_ROLE_ID = "role_id";
	public static final String USER_IS_BLOCKED = "is_blocked";

	public static final String TABLE_ROLE = "roles";
	public static final String ROLE_ID = "id";
	public static final String ROLE_TYPE = "type";

	public static final String TABLE_AUTHOR = "authors";
	public static final String AUTHOR_ID = "id";
	public static final String AUTHOR_NAME = "name";

	public static final String TABLE_BOOK = "books";
	public static final String BOOK_ID = "id";
	public static final String BOOK_NAME = "name";
	public static final String BOOK_AUTHOR_ID = "author_id";
	public static final String BOOK_PUBLISHER = "publisher";
	public static final String BOOK_PUBLISH_YEAR = "publish_year";
	public static final String BOOK_IMAGE_LINK = "image_link";
	
	public static final String TABLE_BOOK_ITEM = "book_items";
	public static final String BOOK_ITEM_ID = "id";
	public static final String BOOK_ITEM_BOOK_ID = "book_id";

	public static final String TABLE_BOOK_REQUEST_TYPE = "book_request_types";
	public static final String BOOK_REQUEST_TYPE_ID = "id";
	public static final String BOOK_REQUEST_TYPE_TYPE = "type";

	public static final String TABLE_BOOK_REQUEST = "book_requests";
	public static final String BOOK_REQUEST_ID = "id";
	public static final String BOOK_REQUEST_USER_ID = "user_id";
	public static final String BOOK_REQUEST_BOOK_ITEM_ID = "book_item_id";
	public static final String BOOK_REQUEST_BOOK_REQUEST_TYPE_ID = "book_request_type_id";

	public static final String TABLE_BOOK_REQUEST_JOURNAL = "book_requests_journals";
	public static final String BOOK_REQUEST_JOURNAL_ID = "id";
	public static final String BOOK_REQUEST_JOURNAL_BOOK_REQUEST_ID = "book_request_id";
	public static final String BOOK_REQUEST_JOURNAL_CREATE_DATE = "create_date";
	public static final String BOOK_REQUEST_JOURNAL_APPROVE_DATE = "approve_date";
	public static final String BOOK_REQUEST_JOURNAL_EXPIRATION_DATE = "expiration_date";
	public static final String BOOK_REQUEST_JOURNAL_RETURN_DATE = "return_date";

	public static final String TIMESTAMP_SQL_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String COLUMN_NAME_USER_NAME = "user_name";
	public static final String COLUMN_NAME_BOOK_NAME = "book_name";
	public static final String COLUMN_NAME_AUTHOR_NAME = "author_name";
	public static final String COLUMN_NAME_REQUEST_TYPE = "request_type";
	public static final String COLUMN_NAME_CREATE_DATE = "create_date";
	public static final String COLUMN_NAME_REQUEST_ID = "request_id";
}

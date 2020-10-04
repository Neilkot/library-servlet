create_table_book_requests_journals.sqlCREATE TABLE book_requests_journals(
id INT AUTO_INCREMENT,
book_request_id INT NOT NULL UNIQUE,
create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
approve_date TIMESTAMP DEFAULT NULL,
expiration_date TIMESTAMP DEFAULT NULL,
return_date TIMESTAMP DEFAULT NULL,
CONSTRAINT pk_book_request_journal_id PRIMARY KEY (id),
CONSTRAINT fk_book_request_journal_book_request_id FOREIGN KEY (book_request_id)
REFERENCES book_requests (id) ON UPDATE CASCADE ON DELETE CASCADE
);
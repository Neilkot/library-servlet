CREATE TABLE book_requests(
id INT AUTO_INCREMENT,
user_id INT NOT NULL,
book_item_id INT NOT NULL,
book_request_type_id INT NOT NULL,
CONSTRAINT pk_book_request_id  PRIMARY KEY (id),
CONSTRAINT fk_book_request_user_id FOREIGN KEY (user_id)
REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT fk_book_request_book_item_id FOREIGN KEY (book_item_id)
REFERENCES book_items (id) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT fk_book_request_type_id FOREIGN KEY (book_request_type_id)
REFERENCES book_request_types (id) ON UPDATE CASCADE ON DELETE CASCADE
);
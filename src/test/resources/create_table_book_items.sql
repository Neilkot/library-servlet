CREATE TABLE book_items(
id INT AUTO_INCREMENT,
book_id INT NOT NULL,
CONSTRAINT pk_book_items_id PRIMARY KEY (id),
CONSTRAINT fk_book_items_books_id FOREIGN KEY (book_id) 
REFERENCES books (id) ON UPDATE CASCADE ON DELETE CASCADE
);
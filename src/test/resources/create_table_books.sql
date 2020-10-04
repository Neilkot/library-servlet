CREATE TABLE books(
id INT AUTO_INCREMENT,
name VARCHAR (100) NOT NULL,
author_id INT NOT NULL,
publisher VARCHAR(50) NOT NULL,
publish_year INT NOT NULL,
image_link VARCHAR(150) NOT NULL,
CONSTRAINT pk_book_id PRIMARY KEY (id),
CONSTRAINT uq_book_name UNIQUE KEY (name, author_id, publisher, publish_year),
CONSTRAINT fk_book_author_id FOREIGN KEY (author_id)
REFERENCES authors (id) ON UPDATE CASCADE ON DELETE RESTRICT
);
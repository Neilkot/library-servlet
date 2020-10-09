DROP DATABASE IF EXISTS library;
CREATE DATABASE library character set UTF8 collate utf8_general_ci;

DROP TABLE IF EXISTS book_requests_journals;
DROP TABLE IF EXISTS book_requests;
DROP TABLE IF EXISTS book_request_types;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS book_items;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

CREATE TABLE roles(
id INT AUTO_INCREMENT,
type VARCHAR (35) NOT NULL UNIQUE,
CONSTRAINT pk_role_id PRIMARY KEY (id)
);
 
CREATE TABLE users(
id INT AUTO_INCREMENT,
login VARCHAR (25) NOT NULL UNIQUE,
`checksum` VARCHAR (200) NOT NULL,
first_name VARCHAR (35) NOT NULL,
last_name VARCHAR (35) NOT NULL,
role_id INT NOT NULL,
is_blocked BOOLEAN DEFAULT FALSE,
CONSTRAINT pk_user_id PRIMARY KEY (id),
CONSTRAINT fk_user_role_id FOREIGN KEY (role_id)
REFERENCES roles (id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE authors(
id INT AUTO_INCREMENT,
name VARCHAR(100) NOT NULL UNIQUE,
CONSTRAINT pk_author_id PRIMARY KEY  (id)
);

 
CREATE TABLE books(
id INT AUTO_INCREMENT,
name VARCHAR (100) NOT NULL,
author_id INT NOT NULL,
publisher VARCHAR(50) NOT NULL,
publish_year INT NOT NULL,
image_link VARCHAR(300) NOT NULL,
CONSTRAINT pk_book_id PRIMARY KEY (id),
CONSTRAINT uq_book_name UNIQUE KEY (name, author_id, publisher, publish_year),
CONSTRAINT fk_book_author_id FOREIGN KEY (author_id)
REFERENCES authors (id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE book_items(
id INT AUTO_INCREMENT,
book_id INT NOT NULL,
CONSTRAINT pk_book_items_id PRIMARY KEY (id),
CONSTRAINT fk_book_items_books_id FOREIGN KEY (book_id) 
REFERENCES books (id) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE book_request_types(
id INT AUTO_INCREMENT,
type VARCHAR (50) UNIQUE,
CONSTRAINT pk_book_request_type_id PRIMARY KEY (id)
);

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

CREATE TABLE book_requests_journals(
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

SET SESSION sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));



 -- VALUES for DB
 
INSERT INTO roles (type) VALUES("READER"), ("LIBRARIAN"), ("ADMIN");
INSERT INTO USERS (login, checksum, first_name, last_name, role_id) VALUES ("admin", "C4CA4238A0B923820DCC509A6F75849B", "Kostya", "Morozov", 3);  
INSERT INTO USERS (login, checksum, first_name, last_name, role_id) VALUES ("librarian", "C4CA4238A0B923820DCC509A6F75849B", "Jane", "Doe", 2);
INSERT INTO USERS (login, checksum, first_name, last_name, role_id) VALUES ("jane", "C4CA4238A0B923820DCC509A6F75849B", "Jane", "Doe", 2);
INSERT INTO authors (name) VALUES ("Robert Martin"), ("Joshua Bloch"), ("William Shakespeare"), ("Mark Twain"), ("Тарас Шевченко"), ("Іван Котляревський"), ("J. K. Rowling") ;

INSERT INTO books (name, author_id, publisher, publish_year, image_link) VALUES 
("Кобзар", 5, "Барвiнок", 1999, "https://img.yakaboo.ua/media/catalog/product/cache/1/image/398x565/234c7c011ba026e66d29567e1be1d1f7/1/8/18291_26236_13.jpg"),
("Clean Code", 1, "PROSTYLE", 2012, "https://saltares.com/img/wp/clean-code-uncle-bob.jpg"),
("JAVA Effective Programming", 2, "American House", 2015, "https://images-na.ssl-images-amazon.com/images/I/41JLgmt8MlL._SX402_BO1,204,203,200_.jpg"),
("Romeo and Juliet", 3, "Simon & Schuster", 2004, "https://images-na.ssl-images-amazon.com/images/I/31ObBpDEOcL._BO1,204,203,200_.jpg"),
("Енеїда", 6, "Книги Львова", 2004, "https://img.yakaboo.ua/media/catalog/product/cache/1/image/398x565/234c7c011ba026e66d29567e1be1d1f7/i/m/img240_1_60.jpg"),
("Harry Potter and the Philosopher's Stone", 1, "London Lit", 2002, "https://img.yakaboo.ua/media/catalog/product/cache/1/image/398x565/234c7c011ba026e66d29567e1be1d1f7/4/8/483720_11873905.jpg");

INSERT INTO book_items (book_id) VALUES (1), (2), (3), (4), (5), (6), (3), (3), (5), (5), (5);
INSERT INTO book_request_types (type) VALUES("ABONEMENT"), ("READING_AREA");
select * from books;
select * from authors;
select * from users;
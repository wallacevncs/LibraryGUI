create database library;

CREATE TABLE library.user(
id int AUTO_INCREMENT,
name varchar(100) NOT NULL,
cpf varchar(11) NOT NULL,
dateOfBirth varchar(10) NOT NULL,
numberOfPhone varchar(11) NOT NULL,
address varchar(100) NOT NULL,
historic json,
PRIMARY KEY (id)
);

CREATE TABLE library.book(
id int AUTO_INCREMENT,
bookCode varchar(10) NOT NULL,
titleBook varchar(100) NOT NULL,
category varchar(50) NOT NULL,
quantity int NOT NULL,
loans int NOT NULL,
historic json,
PRIMARY KEY (id)
);
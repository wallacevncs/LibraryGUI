package model.dao;

import java.util.List;

import model.entities.Book;
import model.entities.User;

public interface libraryDao {
	
	void insertUser(User user);
	void insertBook(Book book);
	boolean deleteUser(User user);
	boolean deleteBook(Book book);
	void updateUser(User user);
	void updateBook(Book book);
	List<User> users();
	List<Book> books();
	User findUser(String cpf);
	Book findBook(String bookCode);
	void bookLoan(User user, Book book);
	void bookReturn(User user, Book book);
}

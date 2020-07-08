package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.libraryDao;
import model.entities.Book;
import model.entities.User;

public class UserService {
	
	libraryDao libraryDao = DaoFactory.createLibraryDao();
	
	public List<User> findAll() {
		return libraryDao.users();
	}
	
	public void saveOrUpdate(User user) {
		if (user.getId() == null) {
			libraryDao.insertUser(user);
		}
		else {
			libraryDao.updateUser(user);
		}
	}
	
	public boolean loan(User user, String bookCode) {
		
		Book book = libraryDao.findBook(bookCode);
		if(book != null) {
			libraryDao.bookLoan(user, book);
			return true;
		}
		return false;

	}
	
	public void returnBook(User user, String bookCode) {
		Book book = libraryDao.findBook(bookCode);
		if(book != null)
			libraryDao.bookReturn(user, book);
	}
	
	public boolean remove(User user) {
		return libraryDao.deleteUser(user);
	}
	
	

}

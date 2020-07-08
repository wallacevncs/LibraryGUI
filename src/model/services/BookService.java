package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.libraryDao;
import model.entities.Book;

public class BookService {
	
	libraryDao libraryDao = DaoFactory.createLibraryDao();
	
	public List<Book> findAll(){
		return libraryDao.books();
	}
	
	public void saveOrUpdate(Book book) {
		
		if(book.getId() == null) 
			libraryDao.insertBook(book);
		else
			libraryDao.updateBook(book);
		
	}
	
	public void remove(Book book) {
		
		libraryDao.deleteBook(book);
	}
	
}

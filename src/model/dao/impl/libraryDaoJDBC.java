package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import db.ConexaoDb;
import db.DbException;
import model.dao.libraryDao;
import model.entities.Book;
import model.entities.User;
import model.services.Loan;
import model.services.LoanFor;

public class libraryDaoJDBC implements libraryDao{

	
	private Connection conn;
	
	
	public libraryDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertUser(User user) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO user "
					+ "(name, cpf, dateOfBirth, numberOfPhone, address, historic) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, user.getName());
			st.setString(2, user.getCpf());
			st.setString(3, user.getDateOfBirth());
			st.setString(4, user.getNumberOfPhone());
			st.setString(5, user.getAddress());
			st.setString(6, user.jsonHistoric());
			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					user.setId(id);
				}
				ConexaoDb.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
		}
		
	}

	@Override
	public void insertBook(Book book) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO book "
					+ "(bookCode, titleBook, category, quantity, loans, historic) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, book.getBookCode());
			st.setString(2, book.getTitleBook());
			st.setString(3, book.getCategory());
			st.setInt(4, book.getQuantity());
			st.setInt(5, book.getLoans());
			st.setString(6, book.jsonHistoric());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					book.setId(id);
				}
				ConexaoDb.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
		}
		
	}
	
	@Override
	public boolean deleteUser(User user) {
		
		PreparedStatement st = null;
		
		try {
			String cpf = user.getCpf();
			st = conn.prepareStatement("DELETE FROM user WHERE cpf = ?");
			st.setString(1, cpf);
			int rowsAffected = st.executeUpdate();
			return (rowsAffected > 0) ? true : false;
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
		}
		
	}

	@Override
	public boolean deleteBook(Book book) {
		
		PreparedStatement st = null;
		
		try {
			String bookCode = book.getBookCode();
			st = conn.prepareStatement("DELETE FROM book WHERE bookCode = ?");
			st.setString(1, bookCode);
			int rowsAffected = st.executeUpdate();
			return (rowsAffected > 0) ? true : false;
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
		}
		
	}

	@Override
	public void updateUser(User user) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
				  "UPDATE user "	
				+ "SET historic = ?"
			    + "WHERE Id = ?");
			
			st.setString(1, user.jsonHistoric());
			
			st.setInt(2, user.getId());

			st.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
		}
		
	}

	@Override
	public void updateBook(Book book) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
				  "UPDATE book "	
				+ "SET historic = ?, quantity = ?, loans = ? "
			    + "WHERE Id = ?");
			
			st.setString(1, book.jsonHistoric());
			st.setInt(2, book.getQuantity());
			st.setInt(3, book.getLoans());
			
			st.setInt(4, book.getId());

			st.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
		}
		
	}

	@Override
	public List<User> users() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("SELECT * FROM user ORDER BY name");
			
			rs = st.executeQuery();
			
			List<User> list = new ArrayList<>();
			
			while(rs.next()) {
				User user = instantiateUser(rs);
				
				if(user != null) {
					list.add(user);
				}
			}
			
			return list;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
			ConexaoDb.closeResultSet(rs);
		}
	}

	@Override
	public List<Book> books() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("SELECT * FROM book ORDER BY id");
			
			rs = st.executeQuery();
			
			List<Book> list = new ArrayList<>();
			
			while(rs.next()) {
				Book book = instantiateBook(rs);
				
				if(book != null) {
					list.add(book);
				}
			}
			
			return list;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
			ConexaoDb.closeResultSet(rs);
		}
	}

	@Override
	public User findUser(String cpf) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("SELECT * FROM user WHERE cpf = ?");

			st.setString(1, cpf);
			rs = st.executeQuery();
			if (rs.next()) {
				User user = instantiateUser(rs);
				return user;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
			ConexaoDb.closeResultSet(rs);
		}

	}
	
	private User instantiateUser(ResultSet rs) throws SQLException {
		
		User user = new User();
		user.setName(rs.getString("name"));
		user.setId(rs.getInt("id"));
		user.setCpf(rs.getString("cpf"));
		user.setDateOfBirth(rs.getString("dateOfBirth"));
		user.setNumberOfPhone(rs.getString("numberOfPhone"));
		user.setAddress(rs.getString("address"));
		user.setHistoric(rs.getString("historic"));
		return user;
		
	}

	@Override
	public Book findBook(String bookCode) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("SELECT * FROM book WHERE bookCode = ?");

			st.setString(1, bookCode);
			rs = st.executeQuery();
			if (rs.next()) {
				Book book = instantiateBook(rs);
				return book;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			ConexaoDb.closeStatement(st);
			ConexaoDb.closeResultSet(rs);
		}
		
	}

	private Book instantiateBook(ResultSet rs) throws SQLException {
		
		Book book = new Book();
		book.setId(rs.getInt("id"));
		book.setBookCode(rs.getString("bookCode"));
		book.setTitleBook(rs.getString("titleBook"));
		book.setCategory(rs.getString("Category"));
		book.setQuantity(rs.getInt("quantity"));
		book.setLoans(rs.getInt("loans"));
		book.setHistoric(rs.getString("historic"));
		return book;
		
	}
	
	
	@Override
	public void bookLoan(User user, Book book) {
		
		if(user.checksReminder()) {
			book.loan();
			LocalDate hoje = LocalDate.now();
			Loan loanUser = new Loan(book.getBookCode(), hoje, hoje.plusWeeks(1));
			LoanFor loanBook = new LoanFor(user.getCpf(), hoje, hoje.plusWeeks(1));
			user.addHistoric(loanUser);
			book.addUserHistoric(loanBook);
			updateUser(user);
			updateBook(book);
			
		}else {
			System.out.println(String.format("User blocked until: {0}", user.unlockDate));
		}
		
	}

	@Override
	public void bookReturn(User user, Book book) {
		
		LocalDate hoje = LocalDate.now();
		List<Loan> userHistoric = user.getHistoric();
		List<LoanFor> bookHistoric = book.getHistoric();
		for(Loan loan : userHistoric) {
			
			for(LoanFor loanFor : bookHistoric) {
				
				if(book.getBookCode().equals(loan.getBookCode()) && user.getCpf().equals(loanFor.getUserCode())) 
				{
					
					if(loan.getReturnDate().isEqual(hoje) || loan.getReturnDate().isAfter(hoje)) {
						loan.setReturnDate(hoje);
						loanFor.setReturnDate(hoje);
						book.giveBack();
					}
					else 
					{
						Period period = Period.between(hoje, loan.getReturnDate());
						int daysOfDelay = period.getDays();
						user.initializeReminder(daysOfDelay);
						loan.setReturnDate(hoje);
						loanFor.setReturnDate(hoje);
						book.giveBack();
					}
					
					updateUser(user);
					updateBook(book);
				}
			}
					
			
		}

	}


}







package model.entities;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import db.DbException;
import model.services.LoanFor;

public class Book {

	private Integer id;
	private String bookCode;
	private String titleBook;
	private String category;
	private Integer quantity;
	private Integer loans;
	private List<LoanFor> historic;

	public Book() {

	}

	public Book(Integer id, String bookCode, String titleBook, String category, Integer quantity, Integer loans) {
		this.id = id;
		this.bookCode = bookCode;
		this.titleBook = titleBook;
		this.category = category;
		this.quantity = quantity;
		this.loans = loans;
		this.historic = new ArrayList<>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBookCode() {
		return bookCode;
	}

	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}

	public String getTitleBook() {
		return titleBook;
	}

	public void setTitleBook(String titleBook) {
		this.titleBook = titleBook;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getLoans() {
		return loans;
	}

	public List<LoanFor> getHistoric() {
		return historic;
	}

	public void setHistoric(String jsonHistoric) {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting()
					.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer()).create();
			Type type = new TypeToken<List<LoanFor>>() {
			}.getType();
			historic = gson.fromJson(jsonHistoric, type);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public void setLoans(Integer loans) {
		this.loans = loans;
	}

	public void loan() {
		if (loans == quantity) {
			throw new DbException("Copy not available");
		} else {
			this.quantity -= 1;
			this.loans += 1;
		}
	}

	public void giveBack() {
		if (loans == 0) {
			throw new DbException("No borrowed copies");
		} else {
			this.quantity += 1;
			this.loans -= 1;
		}

	}

	public void addUserHistoric(LoanFor loanFor) {
		if(historic == null)
			historic = new ArrayList<>();

		historic.add(loanFor);
	}

	public String jsonHistoric() {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting()
					.registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
			String json = gson.toJson(getHistoric());
			return json;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}

	}

	@Override
	public String toString() {
		return "Id = " + id + ", Book code = " + bookCode + ", Title = " + titleBook + ", Category = " + category
				+ ", Quantity = " + quantity + ", Loans = " + loans + ", Historic = " + jsonHistoric();
	}

}

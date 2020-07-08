package model.services;

import java.time.LocalDate;

public class Loan {
	
	public String bookCode;
	public LocalDate loanDate;
	public LocalDate returnDate;
	
	public Loan() {
		
	}
	
	public Loan(String bookCode, LocalDate loanDate, LocalDate returnDate) {
		this.bookCode = bookCode;
		this.loanDate = loanDate;
		this.returnDate = returnDate;
	}
	
	public String getBookCode() {
		return bookCode;
	}
	
	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}
	
	public LocalDate getLoanDate() {
		return loanDate;
	}
	
	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
	
	@Override
	public String toString() {
		
		return "Book code: " + bookCode + ", Loan Date: " + loanDate + ", Return Date: " + returnDate;
	}

}

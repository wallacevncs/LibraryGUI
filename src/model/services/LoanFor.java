package model.services;

import java.time.LocalDate;

public class LoanFor {
	

	private String userCode;
	private LocalDate loanDate;
	private LocalDate returnDate;
	
	public LoanFor(String userCode, LocalDate loanDate, LocalDate returnDate) {
		this.userCode = userCode;
		this.loanDate = loanDate;
		this.returnDate = returnDate;
	}
	
	public String getUserCode() {
		return userCode;
	}
	
	public void setBookCode(String bookCode) {
		this.userCode = bookCode;
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
		
		return "User code: " + userCode + ", Loan Date: " + loanDate + ", Return Date: " + returnDate;
	}

}

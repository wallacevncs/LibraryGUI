package model.services;

import java.time.LocalDate;

public class Reminder {
	
	private int days;
	
	public Reminder(int days) {
		this.days = days;
	}
	
	public LocalDate unlock() {
		return LocalDate.now().plusWeeks(days);
	}

}

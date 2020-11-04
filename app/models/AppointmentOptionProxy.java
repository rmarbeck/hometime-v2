package models;

import java.time.LocalDateTime;

public class AppointmentOptionProxy {
	public String id;
	
	public String datetimeAsString;
	
	public boolean available;
	
	public String datetimeAsStringNiceToDisplay;
	
	public AppointmentOptionProxy(String id, String datetimeAsString, boolean available, String datetimeAsStringNiceToDisplay) {
		this.id = id;
		this.datetimeAsString = datetimeAsString;
		this.available = available;
		this.datetimeAsStringNiceToDisplay = datetimeAsStringNiceToDisplay;
	}
	
	public LocalDateTime getDatetime() {
		return LocalDateTime.now();
	}
	
	public String getNiceDisplayableDatetime() {
		return datetimeAsStringNiceToDisplay;
	}
}

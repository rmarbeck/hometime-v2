package models;


public class Price {
	public Long id;
	public String name;
	
	public boolean active = true;
	
	public String description;
	
	public Long battery_change = 0l;
	
	public Long battery_change_and_water = 0l;
	
	public Long battery_change_and_water_and_polish = 0l;
	
	public Long low_service_price_simple = 0l;
	
	public Long high_service_price_simple = 0l;
	
	public Long low_service_price_chrono = 0l;
	
	public Long high_service_price_chrono = 0l;
	
	public Long low_service_price_gmt = 0l;
	
	public Long high_service_price_gmt = 0l;
	
	public Long low_service_price_complex = 0l;
	
	public Long high_service_price_complex = 0l;
	
	public Long high_emergency_factor = 0l;
	
	public Long low_emergency_factor = 0l;
	
	private Price() {
	}
	
}

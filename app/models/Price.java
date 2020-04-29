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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getBatteryChange() {
		return battery_change;
	}

	public void setBatteryChange(Long battery_change) {
		this.battery_change = battery_change;
	}

	public Long getBatteryChangeAndWater() {
		return battery_change_and_water;
	}

	public void setBatteryChangeAndWater(Long battery_change_and_water) {
		this.battery_change_and_water = battery_change_and_water;
	}

	public Long getBatteryChangeAndWaterAndPolish() {
		return battery_change_and_water_and_polish;
	}

	public void setBatteryChangeAndWaterAndPolish(Long battery_change_and_water_and_polish) {
		this.battery_change_and_water_and_polish = battery_change_and_water_and_polish;
	}

	public Long getLowServicePriceSimple() {
		return low_service_price_simple;
	}

	public void setLowServicePriceSimple(Long low_service_price_simple) {
		this.low_service_price_simple = low_service_price_simple;
	}

	public Long getHighServicePriceSimple() {
		return high_service_price_simple;
	}

	public void setHighServicePriceSimple(Long high_service_price_simple) {
		this.high_service_price_simple = high_service_price_simple;
	}

	public Long getLowServicePriceChrono() {
		return low_service_price_chrono;
	}

	public void setLowServicePriceChrono(Long low_service_price_chrono) {
		this.low_service_price_chrono = low_service_price_chrono;
	}

	public Long getHighServicePriceChrono() {
		return high_service_price_chrono;
	}

	public void setHighServicePriceChrono(Long high_service_price_chrono) {
		this.high_service_price_chrono = high_service_price_chrono;
	}

	public Long getLowServicePriceGmt() {
		return low_service_price_gmt;
	}

	public void setLowServicePriceGmt(Long low_service_price_gmt) {
		this.low_service_price_gmt = low_service_price_gmt;
	}

	public Long getHighServicePriceGmt() {
		return high_service_price_gmt;
	}

	public void setHighServicePriceGmt(Long high_service_price_gmt) {
		this.high_service_price_gmt = high_service_price_gmt;
	}

	public Long getLowServicePriceComplex() {
		return low_service_price_complex;
	}

	public void setLowServicePriceComplex(Long low_service_price_complex) {
		this.low_service_price_complex = low_service_price_complex;
	}

	public Long getHighServicePriceComplex() {
		return high_service_price_complex;
	}

	public void setHighServicePriceComplex(Long high_service_price_complex) {
		this.high_service_price_complex = high_service_price_complex;
	}

	public Long getHighEmergencyFactor() {
		return high_emergency_factor;
	}

	public void setHighEmergencyFactor(Long high_emergency_factor) {
		this.high_emergency_factor = high_emergency_factor;
	}

	public Long getLowEmergencyFactor() {
		return low_emergency_factor;
	}

	public void setLowEmergencyFactor(Long low_emergency_factor) {
		this.low_emergency_factor = low_emergency_factor;
	}
	
}

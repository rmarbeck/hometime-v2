package models;

import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fr.hometime.utils.LiveConfigDeserializer;

@JsonDeserialize(using = LiveConfigDeserializer.class)
public class LiveConfig {
	public String key;
	
	public Optional<String> valuestring;
	public Optional<Long> valuelong;
	public Optional<Boolean> valueboolean;
	public Optional<Date> valuedate;
		
	private LiveConfig() {
		
	}
	
	private LiveConfig(String key, Optional<String> valuestring, Optional<Long> valuelong, Optional<Boolean> valueboolean, Optional<Date> valuedate) {
		this.key = key;
		this.valuestring = valuestring;
		this.valuelong = valuelong;
		this.valueboolean = valueboolean;
		this.valuedate = valuedate;
	}
	
	public static LiveConfig of(String key, String valuestring, Long valuelong, boolean valueboolean, Date valuedate) {
		return new LiveConfig(key, Optional.ofNullable(valuestring), Optional.ofNullable(valuelong), Optional.ofNullable(valueboolean), Optional.ofNullable(valuedate));
	}
	
	public LiveConfig(String key, String value) {
		this(key, Optional.of(value), Optional.empty(), Optional.empty(), Optional.empty());
	}
	
	public LiveConfig(String key, Long value) {
		this(key, Optional.empty(), Optional.of(value), Optional.empty(), Optional.empty());
	}
	
	public LiveConfig(String key, Boolean value) {
		this(key, Optional.empty(), Optional.empty(), Optional.of(value), Optional.empty());
	}
	
	public LiveConfig(String key, Date value) {
		this(key, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(value));
	}

	public Optional<String> getValuestring() {
		return valuestring;
	}

	public void setValuestring(Optional<String> valuestring) {
		this.valuestring = valuestring;
	}

	public Optional<Long> getValuelong() {
		return valuelong;
	}

	public void setValuelong(Optional<Long> valuelong) {
		this.valuelong = valuelong;
	}

	public Optional<Boolean> getValueboolean() {
		return valueboolean;
	}

	public void setValueboolean(Optional<Boolean> valueboolean) {
		this.valueboolean = valueboolean;
	}

	public Optional<Date> getValuedate() {
		return valuedate;
	}

	public void setValuedate(Optional<Date> valuedate) {
		this.valuedate = valuedate;
	}

	public String getKey() {
		return key;
	}
	
}

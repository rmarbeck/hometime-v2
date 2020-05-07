package models;

import java.time.Instant;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Helper Object
 */
public class AuthenticationCheckRequestData {
	public Boolean result;
	public String brand;
	public String model;
	public String reference;
	public String serial;
	public Instant date;
	  
	public AuthenticationCheckRequestData(JsonNode json) {
		  result = json.findPath("result").asBoolean();
		  brand = json.findPath("brand").textValue();
		  model = json.findPath("model").textValue();
		  reference = json.findPath("reference").textValue();
		  serial = json.findPath("serial").textValue();
		  date = Instant.ofEpochMilli(json.findPath("date").longValue());
	}
	
	public Date getDate() {
		return new Date(date.toEpochMilli());
	}
}

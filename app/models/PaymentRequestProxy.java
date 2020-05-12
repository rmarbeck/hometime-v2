package models;

import com.fasterxml.jackson.databind.JsonNode;

public class PaymentRequestProxy {
	public float priceInEuros;
	public String orderNumber;
	public String description;

	public PaymentRequestProxy(JsonNode json) {
		priceInEuros = json.findPath("priceInEuros").floatValue();
		orderNumber = json.findPath("orderNumber").textValue();
		description = json.findPath("description").textValue();
	}
	
}

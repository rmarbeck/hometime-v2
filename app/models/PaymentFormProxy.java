package models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class PaymentFormProxy {
	protected Map<String, String> parameters;
	protected String signature;

	public PaymentFormProxy(JsonNode json) {
		parameters = new HashMap<String, String>();
		json.findPath("parameters").forEach(node -> {
			parameters.put(node.findPath("parameterName").asText(), node.findPath("value").asText());
		});
		signature = json.findPath("signature").asText();
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String getSignature() {
		return signature;
	}
	
}

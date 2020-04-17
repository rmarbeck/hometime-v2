package fr.hometime.utils;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;

public interface JsonListParser {
	default <T> Optional<List<T>> parseJsonNode(JsonNode json, TypeReference<List<T>> type) throws Exception {
		List<T> value = Json.mapper().readValue(json.traverse(), type);
		    return Optional.of(value);
	}
}

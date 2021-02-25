package fr.hometime.utils;

import java.io.File;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

import play.api.Play;
import play.libs.Json;

public interface JsonLocalFileLoader {
	default Optional<JsonNode> loadJson(String filePath) throws Exception {
		JsonNode json = Json.mapper().readTree(new File(filePath));
		return Optional.of(json);
	}
}

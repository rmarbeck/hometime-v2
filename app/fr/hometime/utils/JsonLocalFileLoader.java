package fr.hometime.utils;

import java.io.File;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.api.Play;
import play.libs.Json;

public interface JsonLocalFileLoader {
	default Optional<JsonNode> loadJson(String filePath) throws Exception {
		File file = new File(filePath);
		
		if (!file.exists())
			file = new File("./target/universal/stage/"+filePath); 
		
		JsonNode json = Json.mapper().readTree(file);
		return Optional.of(json);
	}
}

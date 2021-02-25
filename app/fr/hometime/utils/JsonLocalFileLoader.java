package fr.hometime.utils;

import java.io.File;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.api.Play;
import play.libs.Json;

public interface JsonLocalFileLoader {
	default Optional<JsonNode> loadJson(String filePath) throws Exception {
		String[] pathnames;
		File f = new File("/");
		pathnames = f.list();
		for (String pathname : pathnames) {
			Logger.error(pathname);
        }
		
		f = new File("./target/universal/stage");
		pathnames = f.list();
		for (String pathname : pathnames) {
			Logger.error("-> "+pathname);
        }
		File file = new File(filePath);
		
		if (!file.canRead()) {
			file = new File("target/universal/stage/"+filePath); 
		}
		
		JsonNode json = Json.mapper().readTree(new File(filePath));
		return Optional.of(json);
	}
}

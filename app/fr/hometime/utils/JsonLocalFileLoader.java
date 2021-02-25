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
		
		f = new File("/conf/");
		pathnames = f.list();
		for (String pathname : pathnames) {
			Logger.error(pathname);
        }
		
		f = new File("conf/");
		pathnames = f.list();
		for (String pathname : pathnames) {
			Logger.error(pathname);
        }
		
		
		JsonNode json = Json.mapper().readTree(new File(filePath));
		return Optional.of(json);
	}
}

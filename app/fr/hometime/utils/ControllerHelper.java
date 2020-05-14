package fr.hometime.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerHelper {
	private final static Logger logger = LoggerFactory.getLogger("fr.hometime.utils.ControllerHelper");
	
	public static String flattenValues(String key, String[] values, String separator) {
		return Arrays.asList(values).stream().map(value -> { logger.debug(key+"="+encode(value)); return key+"="+encode(value);}).collect(Collectors.joining( "&" ));
	}
	
	public static String encode(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException uee) {
			logger.error("Unable to encode value {}", uee);
		}
		return value;
	}
}

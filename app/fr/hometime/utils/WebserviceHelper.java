package fr.hometime.utils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

public class WebserviceHelper {
	private final static String SECRET_KEY = "SECRET_KEY";
	private final static String SECRET_KEY_CONFIG_PATH = "ws.friendlylocation.secretkey";
	private static Optional<String> secretKey = Optional.empty();
	
	private static final Logger logger = LoggerFactory.getLogger("WebserviceHelper");
	
	private static String getSecretKey(Config config) {
		if (secretKey.isPresent())
			return secretKey.get();
		if (config.hasPath(SECRET_KEY_CONFIG_PATH)) {
			secretKey = Optional.of(config.getString(SECRET_KEY_CONFIG_PATH));
		} else {
			logger.error("Secret key is not configured [{}] not found", SECRET_KEY_CONFIG_PATH);
		}
		return secretKey.get();
	}
	
	public static WSRequest wsWithSecret(WSClient ws, String url, Config config) {
		return ws.url(url).addHeader(SECRET_KEY, getSecretKey(config));
	}
}

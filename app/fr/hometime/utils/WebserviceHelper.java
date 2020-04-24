package fr.hometime.utils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

public class WebserviceHelper {
	private final static String SECRET_KEY = "SECRET_KEY";
	private static Optional<String> secretKey = Optional.empty();
	
	private static final Logger logger = LoggerFactory.getLogger("WebserviceHelper");
	
	private static String getSecretKey(Config config) {
		if (secretKey.isPresent())
			return secretKey.get();
		secretKey = Optional.of(config.getString("ws.friendlylocation.secretkey"));
		logger.error("Secret key -->>" + secretKey.get());
		return secretKey.get();
	}
	
	public static WSRequest wsWithSecret(WSClient ws, String url, Config config) {
		return ws.url(url).addHeader(SECRET_KEY, getSecretKey(config));
	}
}

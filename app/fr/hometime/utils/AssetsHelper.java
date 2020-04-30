package fr.hometime.utils;

import java.util.Optional;

import play.mvc.Http.Request;

/**
 * Security class containing functions for security purpose 
 * 
 * @author Raphael
 *
 */

public class AssetsHelper {
	private static final String CONDITIONAL_HOST_CONFIG_KEY = "conditional.hostname.for.cdn.host";
	private static final String CDNHOST_CONFIG_KEY = "cdn.host";
	private static final String DEFAULT_VALUE = "";
	private static final String PROTOCOL = "https:";
	private static final String PROTOCOL_SEPARATOR = "//";
	
	public static String computeRootPath(ConfigWrapper config, Request request, Boolean isAbsolute) {
		if (isAbsolute)
			return computeRootPathWithProtocol(config, request);
		return computeRootPath(config, request);
	}
	
	public static String computeRootPath(ConfigWrapper config, Request request) {
		return tryToGetRootPath(config, request).orElse(DEFAULT_VALUE);
	}
	
	public static String computeRootPathWithProtocol(ConfigWrapper config, Request request) {
		StringBuilder path = new StringBuilder();
		
		Optional<String> hostValue =tryToGetRootPath(config, request);
		if (hostValue.isPresent()) {
			path.append(PROTOCOL);
			path.append(hostValue.get());
		} else {
			path.append(PROTOCOL_SEPARATOR);
			path.append(request.host());
		}
		return path.toString();
	}
	
	private static Optional<String> tryToGetRootPath(ConfigWrapper config, Request request) {
		return config.tryToGetString(CONDITIONAL_HOST_CONFIG_KEY).flatMap(conditionalHostname -> {
			if(request.host().contains(conditionalHostname))
				return config.tryToGetString(CDNHOST_CONFIG_KEY).map(value -> PROTOCOL_SEPARATOR+value);
			return Optional.empty();
		});
	}
}

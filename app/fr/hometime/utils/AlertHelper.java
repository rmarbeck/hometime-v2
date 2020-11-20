package fr.hometime.utils;

import java.util.Optional;

import models.LiveConfig;

/**
 * Security class containing functions for security purpose 
 * 
 * @author Raphael
 *
 */

public class AlertHelper {
	private static final String FLAG_KEY = "www.hometime.fr.alert.active";
	private static final String MESSAGE_KEY = "www.hometime.fr.alert.message";
	private static final String SIGN_KEY = "www.hometime.fr.alert.leading.sign";
	private static final String CLASS_KEY = "www.hometime.fr.alert.css.class";
	
	public static boolean shouldDisplay(LiveConfigProvider liveConfigProvider) {
		return tryToGetByKey(liveConfigProvider, FLAG_KEY).map(liveconfig -> liveconfig.getValueboolean()).orElse(Optional.of(Boolean.FALSE)).get();
	}
	
	public static Optional<String> message(LiveConfigProvider liveConfigProvider) {
		return getString(liveConfigProvider, MESSAGE_KEY);
	}
	
	public static Optional<String> leadingSign(LiveConfigProvider liveConfigProvider) {
		return getString(liveConfigProvider, SIGN_KEY);
	}
	
	public static Optional<String> cssClass(LiveConfigProvider liveConfigProvider) {
		return getString(liveConfigProvider, CLASS_KEY);
	}
	
	private static Optional<String> getString(LiveConfigProvider liveConfigProvider, String key) {
		return tryToGetByKey(liveConfigProvider, key).map(liveconfig -> liveconfig.getValuestring()).orElse(Optional.empty());
	}
	
	private static Optional<LiveConfig> tryToGetByKey(LiveConfigProvider liveConfigProvider, String key) {
		return liveConfigProvider.retrieveLiveConfigs().map(liveConfigs -> liveConfigs.stream().filter(liveconfig -> liveconfig.key.equals(key)).findFirst()).orElse(Optional.empty());
	}
}

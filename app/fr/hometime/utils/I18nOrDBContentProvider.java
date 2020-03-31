package fr.hometime.utils;

import java.util.Optional;

import javax.inject.Inject;

import play.i18n.Langs;
import play.i18n.MessagesApi;

/**
 * Allows to find content in database or as i18n messages
 */

public class I18nOrDBContentProvider implements ContentProvider {
	private final static String MESSAGE_KEY_SEPARATOR = ".";
	private final MessagesApi messagesApi;
	private final Langs langs;
	private Optional<String> key = Optional.empty();
	
	@Inject 
	public I18nOrDBContentProvider(MessagesApi messagesApi, Langs langs) {
		this.messagesApi = messagesApi;
		this.langs = langs;
	}
	
	@Override
	public void initialize(String prefixKey) {
		if (prefixKey != null && !prefixKey.equals(""))
			this.key = Optional.of(prefixKey);
	}

	@Override
	public String get(String subKey) {
		return getContent(computeFullkey(subKey));
	}

	@Override
	public String get(String prefixKey, String key) {
		return getContent(computeFullkey(prefixKey, key));
	}

	@Override
	public Boolean exists(String subKey) {
		return doesExist(computeFullkey(subKey));
	}

	@Override
	public Boolean exists(String prefixKey, String key) {
		return doesExist(computeFullkey(prefixKey, key));
	}
	
	private String getContent(String fullKey) {
		return messagesApi.preferred(langs.availables()).at(fullKey);
	}
	
	private String computeFullkey(String subKey) {
		if (key.isPresent())
			return computeFullkey(key.get(), subKey);
		return subKey;
	}
	
	private String computeFullkey(String prefixKey, String key) {
		return prefixKey+MESSAGE_KEY_SEPARATOR+key;
	}
	
	private Boolean seemsNotDefinedOrNull(String fullKey) {
		String result = getContent(fullKey);
		return (result == null || result.equals("") || result.equals(fullKey));
	}
	
	private Boolean doesExist(String fullKey) {
		return !seemsNotDefinedOrNull(fullKey);
	}
}

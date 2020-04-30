package fr.hometime.utils;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Function;
import com.typesafe.config.Config;

@Singleton
public class PlayConfigWrapper implements ConfigWrapper {
	public com.typesafe.config.Config config;

	@Inject
	public PlayConfigWrapper(Config config) {
		this.config = config;
	}
	
	@Override
	public Optional<Object> tryToGetValue(String path) {
		return tryToGet(path, this::getValue);
	}

	@Override
	public Optional<String> tryToGetString(String path) {
		return tryToGet(path, this::getString);
	}

	@Override
	public Object getValue(String path) {
		return config.getValue(path);
	}

	@Override
	public String getString(String path) {
		return config.getString(path);
	}
	
	private <T> Optional<T> tryToGet(String path, Function<String, T> supplier) {
		if (config.hasPath(path))
			return Optional.of(supplier.apply(path));
		return Optional.empty();
	}

}

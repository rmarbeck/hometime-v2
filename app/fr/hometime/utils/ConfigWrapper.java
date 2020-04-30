package fr.hometime.utils;

import java.util.Optional;

public interface ConfigWrapper {
	public Object getValue(String path);
	
	public String getString(String path);
	
	public Optional<Object> tryToGetValue(String path);
	
	public Optional<String> tryToGetString(String path);
}

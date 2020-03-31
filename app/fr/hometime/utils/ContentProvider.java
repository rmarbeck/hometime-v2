package fr.hometime.utils;

public interface ContentProvider {
	
	public void initialize(String prefixKey);
	
	public String get(String key);
	
	public String get(String prefixKey, String key);
	
	default String getOrElse(String key, String defaultValue) {
		if (exists(key))
			return get(key);
		return defaultValue;
	}
	
	default String getOrElse(String prefixKey, String key, String defaultValue) {
		if (exists(prefixKey, key))
			return get(prefixKey, key);
		return defaultValue;
	}
	
	public Boolean exists(String key);
	
	public Boolean exists(String prefixKey, String key);
	
	default int getIntValue(String key) {
		return Hidden.asInt(get(key));
	}
	
	default int getIntValue(String prefixKey, String key) {
		return Hidden.asInt(get(prefixKey, key));
	}
	
	class Hidden {
        private static int asInt(String asStringValue) {
    		try {
    			return Integer.valueOf(asStringValue);
    		} catch (NumberFormatException nbfe) {
    			return 0;
    		}
        }
    }
}

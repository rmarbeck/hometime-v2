package fr.hometime.utils;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import models.LiveConfig;

public class LiveConfigDeserializer extends StdDeserializer<LiveConfig> {
		 
	    public LiveConfigDeserializer() { 
	        this(null); 
	    } 
	 
	    public LiveConfigDeserializer(Class<?> vc) { 
	        super(vc); 
	    }
	 
	    @Override
	    public LiveConfig deserialize(JsonParser jp, DeserializationContext ctxt) 
	      throws IOException, JsonProcessingException {
	        JsonNode node = ctxt.readTree(jp);
	        String key = node.get("key").textValue();
	        String valuestring = node.get("valuestring").textValue();
	        boolean valueboolean = node.get("valueboolean").booleanValue();
	        Long valuelong = node.get("valuelong").longValue();
	        Date valuedate = new Date(node.get("valuedate").asLong());
	        
	        return LiveConfig.of(key, valuestring, valuelong, valueboolean, valuedate);
	    }
}

package fr.hometime.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import models.News;
import models.News.NewsType;

public class NewsDeserializer extends StdDeserializer<News> {
		 
	    public NewsDeserializer() { 
	        this(null); 
	    } 
	 
	    public NewsDeserializer(Class<?> vc) { 
	        super(vc); 
	    }
	 
	    @Override
	    public News deserialize(JsonParser jp, DeserializationContext ctxt) 
	      throws IOException, JsonProcessingException {
	        JsonNode node = ctxt.readTree(jp);
	        String title = node.get("title").textValue();
	        String body = node.get("body").textValue();
	        NewsType type = NewsType.fromString(node.get("type").textValue());
	        Optional<String> readMoreUrl = Optional.empty();
	        if (node.has("readMoreUrl"))
	        	readMoreUrl = Optional.of(node.get("readMoreUrl").textValue());
	        
	        Date date = new Date(node.get("date").asLong());
	        List<String> categories = readValues(node, "categories");
	        List<String> previewUrl = readValues(node, "previewUrl");

	        Optional<List<String>> previewAlt = Optional.empty();
	        for (JsonNode current : node.get("previewAlt")) {
	        	if (!previewAlt.isPresent())
	        		previewAlt = Optional.of(new ArrayList());
	        	previewAlt.get().add(current.textValue());
	        }

	        boolean active = node.get("active").booleanValue();
	        
	        return new News(title, body, type, date, categories, previewUrl, previewAlt, readMoreUrl, active);
	    }
	    
	    private List<String> readValues(JsonNode node, String key) {
	    	List<String> list = new ArrayList();
	    	for (JsonNode current : node.get(key))
	    		list.add(current.textValue());
	        return list;
	    }
}

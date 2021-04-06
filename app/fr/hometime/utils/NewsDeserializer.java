package fr.hometime.utils;

import java.io.IOException;
import java.util.Date;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import models.News;
import models.News.NewsType;

public class NewsDeserializer extends StdDeserializer<News> {
	private static final long serialVersionUID = -539926018451598923L;

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
	        Date date = new Date(node.get("date").asLong());
	        
	        News toBuild = new News(title, body, type, date);
	        
	        if (node.has("readMoreUrl"))
	        	toBuild.setReadMoreUrl(node.get("readMoreUrl").textValue());
	        
	        if (node.has("privateInfos"))
	        	toBuild.setPrivateInfos(node.get("privateInfos").textValue());
	        
	        addValues(node, "categories", toBuild::addCategory);
	        addValues(node, "previewUrl", toBuild::addPreviewUrl);
	        addValues(node, "previewAlt", toBuild::addPreviewAlt);

	        if (node.get("active").booleanValue()) {
	        	toBuild.activate();
	        } else {
	        	toBuild.unActivate();
	        }
	        
	        return toBuild;
	    }
	    
	    
	    private void addValues(JsonNode node, String key, Function<String, News> addingFunction) {
	    	for (JsonNode current : node.get(key))
	    		addingFunction.apply(current.textValue());
	    }
}

package models;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fr.hometime.utils.NewsDeserializer;
import sun.rmi.runtime.NewThreadAction;

@JsonDeserialize(using = NewsDeserializer.class)
public class News {
	public enum NewsType {
	    ONE_PICTURE ("ONE_PICTURE"),
	    VIDEO ("VIDEO"),
	    DIAPORAMA ("DIAPORAMA"),
	    RESERVED_1 ("RESERVED_1"),
	    RESERVED_2 ("RESERVED_2");
	    
		private String name = "";
		    
		NewsType(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public static NewsType fromString(String name) {
	        for (NewsType type : NewsType.values()) {
	            if (type.name.equals(name)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}
	
	public Long id;
	public String title;
	
	public String body;
	
	public NewsType type;
	
	public Date date;
	
	public Optional<List<String>> categories;
	
	public Optional<List<String>> previewUrl;
			
	public Optional<List<String>> previewAlt;
	
	public Optional<String> readMoreUrl;
		
	public boolean active = true;
	
	public Optional<String> privateInfos;
		
	private News() {
		System.out.println("??????????");
	}
	
	public News(String title, String body, NewsType type, Date date, String category, String previewUrl, Optional<String> previewAlt, Optional<String> readMoreUrl, boolean active) {
		this.title = title;
		this.body = body;
		this.type = type;
		this.date = date;
		setCategories(category);
		setPreviewUrl(previewUrl);
		previewAlt.ifPresent(value -> setPreviewAlt(value));
		this.readMoreUrl = readMoreUrl;
		this.active = active;
	}
	
	public News(String title, String body, NewsType type, Date date, List<String> category, List<String> previewUrl, Optional<List<String>> previewAlt, Optional<String> readMoreUrl, boolean active) {
		this.title = title;
		this.body = body;
		this.type = type;
		this.date = date;
		setCategories(category);
		setPreviewUrl(previewUrl);
		previewAlt.ifPresent(value -> setPreviewAlt(value));
		this.readMoreUrl = readMoreUrl;
		this.active = active;
	}
	
	public void setCategories(String singleValue) {
		categories = uniqueValueToOptionalArray(singleValue);
	}
	
	public void setPreviewUrl(String singleValue) {
		previewUrl = uniqueValueToOptionalArray(singleValue);
	}
	
	public void setPreviewAlt(String singleValue) {
		previewAlt = uniqueValueToOptionalArray(singleValue);
	}
	
	private Optional<List<String>> uniqueValueToOptionalArray(String singleValue) {
		return Optional.of(Arrays.asList(singleValue));
	}

	public String getType() {
		return type.toString();
	}

	public boolean isActive() {
		return active;
	}
	
	public void setNewsType(String value) {
		type = NewsType.fromString(value);
	}
	
	public void setCategories(List<String> values) {
		categories = Optional.empty();
		if (values != null && values.size() != 0)
			categories = Optional.of(values);
	}
	
	public void setPreviewAlt(List<String> values) {
		previewAlt = Optional.empty();
		if (values != null && values.size() != 0)
			previewAlt = Optional.of(values);
	}
	
	public void setPreviewUrl(List<String> values) {
		previewUrl = Optional.empty();
		if (values != null && values.size() != 0)
			previewUrl = Optional.of(values);
	}
	
	public void setReadMoreUrl(String value) {
		readMoreUrl = Optional.empty();
		if (value != null)
			readMoreUrl = Optional.of(value);
	}
	
	public void setPrivateInfos(String value) {
		readMoreUrl = Optional.empty();
		if (value != null)
			readMoreUrl = Optional.of(value);
	}
}

package models;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fr.hometime.utils.NewsDeserializer;

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
	
	public Optional<List<String>> categories = Optional.empty();
	
	public Optional<List<String>> previewUrl = Optional.empty();
			
	public Optional<List<String>> previewAlt = Optional.empty();
	
	public Optional<String> readMoreUrl = Optional.empty();
		
	public boolean active = true;
	
	public Optional<String> privateInfos = Optional.empty();
	
	public News(String title, String body, NewsType type, Date date) {
		this.title = title;
		this.body = body;
		this.type = type;
		this.date = date;
	}
	
	public News addCategory(String newCategory) {
		return addToList(newCategory, categories, (value) -> this.categories = value);
	}
	
	public News addPreviewUrl(String newPreviewUrl) {
		return addToList(newPreviewUrl, previewUrl, (value) -> this.previewUrl = value);
	}
	
	public News addPreviewAlt(String newPreviewAlt) {
		return addToList(newPreviewAlt, previewAlt, (value) -> this.previewAlt = value);
	}
	
	public News setReadMoreUrl(String newReadMoreUrl) {
		this.readMoreUrl = Optional.ofNullable(newReadMoreUrl);
		return this;
	}
	
	public News setPrivateInfos(String newPrivateInfos) {
		this.privateInfos = Optional.ofNullable(newPrivateInfos);
		return this;
	}
	
	private News addToList(String toAdd, Optional<List<String>> innerField, Consumer<Optional<List<String>>> setter) {
		setter.accept(Optional.of(Stream.concat(
										innerField.orElse(Collections.emptyList()).stream(),
										Stream.of(toAdd))
										.collect(Collectors.toList())));
		return this;
	}
	
	public News activate() {
		this.active = true;
		return this;
	}
	
	public News unActivate() {
		this.active = false;
		return this;
	}

	public String getType() {
		return type.toString();
	}

	public boolean isActive() {
		return active;
	}
}

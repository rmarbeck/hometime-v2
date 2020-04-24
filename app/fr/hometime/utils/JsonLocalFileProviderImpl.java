package fr.hometime.utils;

import java.util.List;
import java.util.Optional;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import models.Brand;
import models.Feedback;
import models.Price;

@Singleton
public class JsonLocalFileProviderImpl implements JsonLocalFileProvider, JsonListParser, JsonLocalFileLoader, BrandProvider, FeedbackProvider, PriceProvider {
	private String brandFile;
	private String feedbacksFile;
	private String pricesFile;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public JsonLocalFileProviderImpl() {
        this.brandFile = "conf/brands-default.json";
        this.feedbacksFile = "conf/feedbacks-default.json";
        this.pricesFile = "file://";
	}
	
	public JsonLocalFileProviderImpl of() {
		return this;
	}

	@Override
	public Optional<List<Brand>> retrieveBrands() {
		return tryToLoadJsonNode(brandFile).map(json -> tryToParseJsonNode(json, new TypeReference<List<Brand>>(){})).orElse(Optional.empty());
	}
	

	@Override
	public Optional<List<Price>> retrievePrices() {
		return tryToLoadJsonNode(pricesFile).map(json -> tryToParseJsonNode(json, new TypeReference<List<Price>>(){})).orElse(Optional.empty());
	}

	@Override
	public Optional<List<Feedback>> retrieveFeedbacks() {
		return tryToLoadJsonNode(feedbacksFile).map(json -> tryToParseJsonNode(json, new TypeReference<List<Feedback>>(){})).orElse(Optional.empty());
	}
	
	private <T> Optional<List<T>> tryToParseJsonNode(JsonNode json, TypeReference<List<T>> type) {
		try {
		    return parseJsonNode(json, type);
		} catch(Exception e){
		    logger.error("Exception when trying to get {} from file : {}", type ,e.getMessage());
		}
		return Optional.empty();
	}
	
	private Optional<JsonNode> tryToLoadJsonNode(String filePath) {
		try {
			return loadJson(filePath);
		} catch(Exception e){
		    logger.error("Exception when getting data from local file ({}), parsing failed : {}", filePath ,e.getMessage());
		}
		return Optional.empty();
	}
}
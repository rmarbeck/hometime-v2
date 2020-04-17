package fr.hometime.utils;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import models.Brand;
import models.Feedback;
import models.Price;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;

@Singleton
public class JsonWSProviderImpl implements JsonWSProvider, JsonListParser, JsonWSLoader, BrandProvider, FeedbackProvider, PriceProvider, WSBodyReadables, WSBodyWritables {
	private final WSClient ws;
	private String brandWSUrl;
	private String feedbacksUrl;
	private String pricesUrl;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public WSClient ws() {
		return ws;
	}
	
	@Inject
	public JsonWSProviderImpl(WSClient ws) {
        this.ws = ws;
        this.brandWSUrl = "https://www.hometime.fr/ws/brands/get/all";
        this.feedbacksUrl = "https://www.hometime.fr/ws/feedbacks/get/all";
        this.pricesUrl = "https://www.hometime.fr/ws/prices/get/all";
	}
	
	public JsonWSProviderImpl of() {
		return this;
	}

	@Override
	public Optional<List<Brand>> retrieveBrands() {
		return tryToLoadJsonNode(brandWSUrl).map(json -> tryToParseJsonNode(json, new TypeReference<List<Brand>>(){})).orElse(Optional.empty());
	}
	

	@Override
	public Optional<List<Price>> retrievePrices() {
		return tryToLoadJsonNode(pricesUrl).map(json -> tryToParseJsonNode(json, new TypeReference<List<Price>>(){})).orElse(Optional.empty());
	}

	@Override
	public Optional<List<Feedback>> retrieveFeedbacks() {
		return tryToLoadJsonNode(feedbacksUrl).map(json -> tryToParseJsonNode(json, new TypeReference<List<Feedback>>(){})).orElse(Optional.empty());
	}
	
	private <T> Optional<List<T>> tryToParseJsonNode(JsonNode json, TypeReference<List<T>> type) {
		try {
		    return parseJsonNode(json, type);
		} catch(Exception e){
		    logger.error("Exception when trying to get {} from webservice : {}", type ,e.getMessage());
		}
		return Optional.empty();
	}
	
	private Optional<JsonNode> tryToLoadJsonNode(String url) {
		try {
			return loadJson(url);
		} catch(Exception e){
		    logger.error("Exception when getting data from webservice ({}), call to external source failed : {}", url ,e.getMessage());
		}
		return Optional.empty();
	}
}

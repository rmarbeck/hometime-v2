package fr.hometime.utils;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;

import models.AppointmentOptionProxy;
import models.Brand;
import models.Feedback;
import models.LiveConfig;
import models.News;
import models.Price;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;

@Singleton
public class JsonWSProviderImpl implements JsonWSProvider, JsonListParser, JsonWSLoader, BrandProvider, FeedbackProvider, PriceProvider, NewsProvider, LiveConfigProvider, AppointmentOptionsProvider, WSBodyReadables, WSBodyWritables {
	private static final String DISTANT_HOST = "legacy.hometime.fr";
	private static final String DISTANT_PROT = "https://";
	private static final String URL_START = DISTANT_PROT+DISTANT_HOST;
	private final WSClient ws;
	private final Config config;
	private String brandWSUrl;
	private String feedbacksUrl;
	private String pricesUrl;
	private String newsUrl;
	private String liveConfigUrl;
	private String appointmentOptionsUrl;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public WSClient ws() {
		return ws;
	}
	
	@Override
	public Config config() {
		return config;
	}
	
	@Inject
	public JsonWSProviderImpl(WSClient ws, Config config) {
        this.ws = ws;
        this.config = config;
        this.brandWSUrl = "/ws/brands/get/all";
        this.feedbacksUrl = "/ws/feedbacks/get/all";
        this.pricesUrl = "/ws/prices/get/all";
        this.newsUrl = "/ws/news/get/all";
        this.liveConfigUrl = "/ws/live/config/get/all";
        this.appointmentOptionsUrl = "/ws/appointment/options/get/all";
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
	
	@Override
	public Optional<List<News>> retrieveNews() {
		return tryToLoadJsonNode(newsUrl).map(json -> tryToParseJsonNode(json, new TypeReference<List<News>>(){})).orElse(Optional.empty());
	}
	
	@Override
	public Optional<List<LiveConfig>> retrieveLiveConfigs() {
		return tryToLoadJsonNode(liveConfigUrl).map(json -> tryToParseJsonNode(json, new TypeReference<List<LiveConfig>>(){})).orElse(Optional.empty());
	}
	
	@Override
	public Optional<List<AppointmentOptionProxy>> retrieveAvailableOptions() {
		return tryToLoadJsonNode(appointmentOptionsUrl).map(json -> tryToParseJsonNode(json, new TypeReference<List<AppointmentOptionProxy>>(){})).orElse(Optional.empty());
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
			return loadJson(URL_START+url);
		} catch(Exception e){
		    logger.error("Exception when getting data from webservice ({}), call to external source failed : {}", URL_START+url ,e.getMessage());
		}
		return Optional.empty();
	}
}

package fr.hometime.utils;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import models.Brand;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;

@Singleton
public class ExternalWSBrandProvider implements BrandProvider, WSBodyReadables, WSBodyWritables  {
	private final WSClient ws;
	private Optional<List<Brand>> brands = Optional.empty();
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	public ExternalWSBrandProvider(WSClient ws) {
        this.ws = ws;
    }
	
	@Override
	public Optional<List<Brand>> retrieveBrands() {
		if (!brands.isPresent()) {
			brands = tryToLoadBrandsFromOutside();
		}
		return brands;
	}
	
	private Optional<List<Brand>> tryToLoadBrandsFromOutside() {
		CompletionStage<Optional<List<Brand>>> resultPromise = ws.url("https://www.hometime.fr/ws/brands/get/all").addHeader("secretKey", "secretValue").get()
				.exceptionally(e -> {
					logger.error("Exception when trying to get Brands from webservice, call to external source failed: "+e.getMessage());
					return null;
				})
				.thenApply( response -> {
					if (response == null) {
						return loadDefault();
					} else {
						JsonNode json = response.asJson();
						try{
							List<Brand> brands = Json.mapper().readValue(json.traverse(), new TypeReference<List<Brand>>(){});
						    return Optional.of(brands);
						}catch(Exception e){
						    logger.error("Exception when trying to get Brands from webservice: "+e.getMessage());
						}
						return Optional.empty();
					}
				});
		
		return resultPromise.toCompletableFuture().join();
	}
	
	private Optional<List<Brand>> loadDefault() {
		logger.warn("Loading default brands from local file.");
		try{
			List<Brand> brands = Json.mapper().readValue(new File("conf/brands-default.json"), new TypeReference<List<Brand>>(){});
		    return Optional.of(brands);
		}catch(Exception e){
		    logger.error("Exception when trying to get Brands from local file: "+e.getMessage());
		}
		return Optional.empty();
	}
}

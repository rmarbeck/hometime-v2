package fr.hometime.utils;

import java.util.Arrays;
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
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import play.libs.Json;

@Singleton
public class ExternalWSBrandProvider implements BrandProvider, WSBodyReadables, WSBodyWritables  {
	private final WSClient ws;
	private Optional<List<Brand>> brands = Optional.empty();
	
	private final Logger logger = LoggerFactory.getLogger(getClass()) ;

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
		CompletionStage<? extends WSResponse> responsePromise = ws.url("https://www.hometime.fr/ws/brands/get/all").get();
		
		CompletionStage<Optional<List<Brand>>> resultPromise = responsePromise.thenApply( response -> {
			JsonNode json = response.asJson();
			try{
				List<Brand> brands = Json.mapper().readValue(json.traverse(), new TypeReference<List<Brand>>(){});
			    return Optional.of(brands);
			}catch(Exception e){
			    //handle exception
			}
			return Optional.empty();
		});
		return resultPromise.toCompletableFuture().getNow(Optional.empty());
	}
}

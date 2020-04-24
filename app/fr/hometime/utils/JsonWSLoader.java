package fr.hometime.utils;

import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;

import play.libs.ws.WSClient;

public interface JsonWSLoader {
	public WSClient ws();
	public Config config();
	
	default Optional<JsonNode> loadJson(String url) throws Exception {
		CompletionStage<Optional<JsonNode>> resultPromise = WebserviceHelper.wsWithSecret(ws(), url, config()).get()
				.exceptionally(e -> {throw new CompletionException(e);})
				.thenApply( response -> Optional.of(response.asJson()));
		
		return resultPromise.toCompletableFuture().exceptionally(e -> Optional.empty()).get();
	}
}

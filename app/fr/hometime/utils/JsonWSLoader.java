package fr.hometime.utils;

import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.ws.WSClient;

public interface JsonWSLoader {
	public WSClient ws();
	
	default Optional<JsonNode> loadJson(String url) throws Exception {
		CompletionStage<Optional<JsonNode>> resultPromise = ws().url(url).addHeader("secretKey", "secretValue").get()
				.exceptionally(e -> {throw new CompletionException(e);})
				.thenApply( response -> Optional.of(response.asJson()));
		
		return resultPromise.toCompletableFuture().exceptionally(e -> Optional.empty()).get();
	}
}

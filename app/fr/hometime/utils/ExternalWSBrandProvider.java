package fr.hometime.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.Brand;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;

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
			brands = Optional.of(Arrays.asList(
					new Brand(1l, "baumemercier", "Baume & Mercier", "baume-et-mercier"),
					new Brand(22l, "blancpain", "Blancpain", "blancpain"),
					new Brand(12l, "rolex", "Rolex", "rolex"),
					new Brand(14l, "omega", "Omega", "omega")
				));
		}
		return brands;
	}
}

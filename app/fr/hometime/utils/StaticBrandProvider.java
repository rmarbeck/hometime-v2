package fr.hometime.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Singleton;

import models.Brand;

@Singleton
public class StaticBrandProvider implements BrandProvider {
	private Optional<List<Brand>> brands = Optional.empty();
	
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

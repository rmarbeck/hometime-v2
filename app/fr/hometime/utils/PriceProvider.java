package fr.hometime.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import models.Brand;
import models.Price;

public interface PriceProvider extends DataProvider {
	final static String BRAND_PRICES = "Marque ";
	final static String CATEGORY_PRICES = "Categorie ";
	
	public Optional<List<Price>> retrievePrices();
	
	public default Optional<Price> findByBrand(Brand brand) {
		return retrievePrices().map(prices -> prices.stream().sorted(Comparator.comparing(Price::getName).reversed()).filter(price -> Hidden.isPriceForThisBrand(price, brand)).findFirst()).orElse(Optional.empty());
	}
	
	public default Optional<Price> findByBrandQuartzCategory(Brand brand) {
		return retrievePrices().map(prices -> prices.stream().filter(price -> price.getName().contains(CATEGORY_PRICES)).filter(price -> Hidden.isPriceForThisBrand(price, brand)).findFirst()).orElse(Optional.empty());
	}
	
	public default Optional<Price> findByCategory(String categoryNumber) {
		return retrievePrices().map(prices -> prices.stream().filter(price -> price.getName().contains(CATEGORY_PRICES)).filter(price -> price.getName().contains(categoryNumber)).findFirst()).orElse(Optional.empty());
	}
	
	class Hidden {
        private static boolean isPriceForThisBrand(Price price, Brand brand) {
        	return price.getName().equals(BRAND_PRICES+brand.getSeoName()) || price.getName().equals(CATEGORY_PRICES+brand.getQuartzCategory());
        }
    }
}

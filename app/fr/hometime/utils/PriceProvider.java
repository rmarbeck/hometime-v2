package fr.hometime.utils;

import java.util.List;
import java.util.Optional;

import models.Price;

public interface PriceProvider extends DataProvider {
	public Optional<List<Price>> retrievePrices();
}

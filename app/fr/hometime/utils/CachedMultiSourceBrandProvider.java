package fr.hometime.utils;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.Brand;

@Singleton
@Deprecated
public class CachedMultiSourceBrandProvider implements BrandProvider  {
	private final static long DEFAULT_VALIDITY_DURATION = 30l;
	private final static ChronoUnit DEFAULT_VALIDITY_UNIT = ChronoUnit.MINUTES;
	
	class ProviderPrioritized {
		BrandProvider provider;
		int prioritization;
		
		ProviderPrioritized(BrandProvider provider, int prioritization) {
			this.provider = provider;
			this.prioritization = prioritization;
		}
	}
	
	class ResultPrioritized {
		Optional<List<Brand>> brands;
		int prioritization;
		
		ResultPrioritized(ProviderPrioritized providerPrioritiez) {
			this.brands = providerPrioritiez.provider.retrieveBrands();
			this.prioritization = providerPrioritiez.prioritization;
		}
	}
	
	private final Cachable<List<Brand>> cache;
	private Optional<Cachable<List<Brand>>> brands = Optional.empty();
	private final List<ProviderPrioritized> providersByImportance;
	private int bestProviderIndex = -1;
	
	private final Logger logger = LoggerFactory.getLogger(getClass()) ;

	@Inject
	public CachedMultiSourceBrandProvider(Cachable<List<Brand>> cache, JsonWSProvider wsprovider) {
        this.cache = cache;
        providersByImportance = Arrays.asList(
        		new ProviderPrioritized(wsprovider.of(), 0),
        		new ProviderPrioritized(new JsonLocalFileProviderImpl(), 1));
    }
	
	@Override
	public Optional<List<Brand>> retrieveBrands() {
		if (!brands.isPresent()) {
			brands = Optional.ofNullable(cache.of(() -> tryToGetBrands().get(), DEFAULT_VALIDITY_DURATION, DEFAULT_VALIDITY_UNIT));
		}
		return brands.map(brands -> brands.get()).orElse(Optional.empty());
	}
	
	private Optional<List<Brand>> tryToGetBrands() {
		Optional<List<Brand>> result = Optional.empty();
		Optional<ResultPrioritized> presult = providersByImportance.stream().map(pprovider -> new ResultPrioritized(pprovider)).filter(prioritiezResult -> prioritiezResult.brands.isPresent()).findFirst();

		if (presult.isPresent()) {
			if (bestProviderIndex > 0  && presult.get().prioritization > bestProviderIndex) {
				logger.warn("No update as only result found is less prioritized.");
			} else {
				logger.info("Brands got from provider n°{}.", presult.get().prioritization);
				result = presult.get().brands;
				this.bestProviderIndex = presult.get().prioritization;
			}
		} else {
			logger.warn("No update as no result was found.");
		}
		
		return result;
	}
}

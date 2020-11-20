package fr.hometime.utils;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.Brand;
import models.Feedback;
import models.LiveConfig;
import models.News;
import models.Price;

@Singleton
public class CachedMultiSourceDataProvider implements  BrandProvider, FeedbackProvider, PriceProvider, NewsProvider, LiveConfigProvider  {
	private final static long DEFAULT_VALIDITY_DURATION = 30l;
	private final static long LONG_VALIDITY_DURATION = 60l;
	private final static long SHORT_VALIDITY_DURATION = 10l;
	private final static ChronoUnit DEFAULT_VALIDITY_UNIT = ChronoUnit.MINUTES;
	
	private final Cachable<List<Brand>> cacheBrands;
	private final Cachable<List<Feedback>> cacheFeedbacks;
	private final Cachable<List<Price>> cachePrices;
	private final Cachable<List<News>> cacheNews;
	private final Cachable<List<LiveConfig>> cacheLiveConfig;
	private Optional<Cachable<List<Brand>>> brands = Optional.empty();
	private Optional<Cachable<List<Feedback>>> feedbacks = Optional.empty();
	private Optional<Cachable<List<Price>>> prices = Optional.empty();
	private Optional<Cachable<List<News>>> news = Optional.empty();
	private Optional<Cachable<List<LiveConfig>>> liveconfigs = Optional.empty();
	private final List<ProviderListPrioritized> providersByImportance;
	private int bestProviderIndex = -1;
	
	private final Logger logger = LoggerFactory.getLogger(getClass()) ;

	@Inject
	public CachedMultiSourceDataProvider(Cachable<List<Brand>> cacheBrands, Cachable<List<Feedback>> cacheFeedbacks, Cachable<List<Price>> cachePrices, Cachable<List<News>> cacheNews, Cachable<List<LiveConfig>> cacheLiveConfig, JsonWSProvider wsprovider, JsonLocalFileProvider lfprovider) {
        this.cacheBrands = cacheBrands;
        this.cacheFeedbacks = cacheFeedbacks;
        this.cachePrices = cachePrices;
        this.cacheNews = cacheNews;
        this.cacheLiveConfig = cacheLiveConfig;
        
        providersByImportance = Arrays.asList(
        		new ProviderListPrioritized(1)
        			.add(Brand.class.getTypeName(), wsprovider.of(), bprovider -> ((BrandProvider) bprovider).retrieveBrands())
        			.add(Feedback.class.getTypeName(), wsprovider.of(), provider -> ((FeedbackProvider) provider).retrieveFeedbacks())
        			.add(Price.class.getTypeName(), wsprovider.of(), provider -> ((PriceProvider) provider).retrievePrices())
        			.add(News.class.getTypeName(), wsprovider.of(), provider -> ((NewsProvider) provider).retrieveNews())
        			.add(LiveConfig.class.getTypeName(), wsprovider.of(), provider -> ((LiveConfigProvider) provider).retrieveLiveConfigs()),
        		new ProviderListPrioritized(2)
        			.add(Brand.class.getTypeName(), lfprovider.of(), provider -> ((BrandProvider) provider).retrieveBrands())
        			.add(Feedback.class.getTypeName(), lfprovider.of(), provider -> ((FeedbackProvider) provider).retrieveFeedbacks())
        			.add(Price.class.getTypeName(), lfprovider.of(), provider -> ((PriceProvider) provider).retrievePrices())
        			.add(News.class.getTypeName(), lfprovider.of(), provider -> ((NewsProvider) provider).retrieveNews())
        			.add(LiveConfig.class.getTypeName(), lfprovider.of(), provider -> ((LiveConfigProvider) provider).retrieveLiveConfigs()));
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public Optional<List<Brand>> retrieveBrands() {
		if (!brands.isPresent()) {
			brands = Optional.ofNullable(cacheBrands.of(() -> (List<Brand>) tryToGetValues(Brand.class.getTypeName()).get(), DEFAULT_VALIDITY_DURATION, DEFAULT_VALIDITY_UNIT));
		}
		return brands.map(brands -> brands.get()).orElse(Optional.empty());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Optional<List<Price>> retrievePrices() {
		if (!prices.isPresent()) {
			prices = Optional.ofNullable(cachePrices.of(() -> (List<Price>) tryToGetValues(Price.class.getTypeName()).get(), DEFAULT_VALIDITY_DURATION, DEFAULT_VALIDITY_UNIT));
		}
		return prices.map(prices -> prices.get()).orElse(Optional.empty());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<List<Feedback>> retrieveFeedbacks() {
		if (!feedbacks.isPresent()) {
			feedbacks = Optional.ofNullable(cacheFeedbacks.of(() -> (List<Feedback>) tryToGetValues(Feedback.class.getTypeName()).get(), DEFAULT_VALIDITY_DURATION, DEFAULT_VALIDITY_UNIT));
		}
		return feedbacks.map(feedbacks -> feedbacks.get()).orElse(Optional.empty());
	}


	@SuppressWarnings("unchecked")
	@Override
	public Optional<List<News>> retrieveNews() {
		if (!news.isPresent()) {
			news = Optional.ofNullable(cacheNews.of(() -> (List<News>) tryToGetValues(News.class.getTypeName()).get(), DEFAULT_VALIDITY_DURATION, DEFAULT_VALIDITY_UNIT));
		}
		return news.map(news -> news.get()).orElse(Optional.empty());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Optional<List<LiveConfig>> retrieveLiveConfigs() {
		if (!liveconfigs.isPresent()) {
			liveconfigs = Optional.ofNullable(cacheLiveConfig.of(() -> (List<LiveConfig>) tryToGetValues(LiveConfig.class.getTypeName()).get(), SHORT_VALIDITY_DURATION, DEFAULT_VALIDITY_UNIT));
		}
		return liveconfigs.map(liveconfigs -> liveconfigs.get()).orElse(Optional.empty());
	}
	
	private Optional<?> tryToGetValues(String typeOfData) {
		Optional<?> result = Optional.empty();
		Optional<ProviderAndResult<?>> presult = providersByImportance.stream().sorted(Comparator.comparing(ProviderListPrioritized::getPriorityLevel)).map(pprovider -> pprovider.retrieve(typeOfData)).filter(Optional::isPresent).findFirst().orElse(Optional.empty());

		if (presult.isPresent()) {
			if (bestProviderIndex > 0  && presult.get().getPriorityLevel() > bestProviderIndex) {
				logger.warn("No update as only result found is less prioritized.");
			} else {
				logger.info("Values got from provider n°{}.", presult.get().getPriorityLevel());
				result = presult.get().get();
				this.bestProviderIndex = presult.get().getPriorityLevel();
			}
		} else {
			logger.warn("No update as no result was found.");
		}
		
		return result;
	}

}

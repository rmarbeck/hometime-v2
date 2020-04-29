package fr.hometime.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryCached<T> implements Cachable<T> {
	private final static Duration DEFAULT_VALIDITY_DURATION = Duration.of(1l, ChronoUnit.HOURS);
	
	Duration validity = DEFAULT_VALIDITY_DURATION;
	Optional<Supplier<T>> supplier;
	Optional<T> value = Optional.empty();
	Optional<Instant> nextRefreshNeeded = Optional.empty();
	
	private final Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	@Override
	public Cachable<T> of(Supplier<T> supplier, long durationValue, ChronoUnit unitOfTime) {
		this.setValidityTime(durationValue, unitOfTime);
		this.set(supplier);
		return this;
	}
	
	@Override
	public Cachable<T> of(Supplier<T> supplier) {
		logger.info("Default duration for caching used : {} ms", validity.toMillis());
		this.set(supplier);
		return this;
	}
	
	public InMemoryCached() {
		super();
	}

	public void set(Supplier<T> supplier) {
		this.supplier = Optional.of(supplier);
	}

	@Override
	public Optional<T> get() {
		if (!isValid())
			this.supplier.ifPresent(supplier -> retrieveAndUpdateValue());
		return this.value;
	}

	@Override
	public void flush() {
		logger.info("Flushing cache");
		this.value = Optional.empty();
		this.nextRefreshNeeded = Optional.empty();
	}

	public void setValidityTime(long durationValue, ChronoUnit unitOfTime) {
		this.validity = Duration.of(durationValue, unitOfTime);		
	}

	@Override
	public boolean isValid() {
		boolean result = nextRefreshNeeded.map(instant -> instant.isAfter(Instant.now())).orElse(false);
		logger.info("Checking cache for validity : it is "+ result);
		return result;
	}
	
	private void retrieveAndUpdateValue() {
		logger.info("Retreving and caching data");
		this.supplier.ifPresent(supplier -> {
			T newValue = supplier.get();
			if (newValue != null) {
				this.value = Optional.of(newValue);
				this.nextRefreshNeeded = Optional.of(Instant.now().plus(validity));
			} else {
				log("Retrieved value is null, no update");	
			}	
		});
		log("Unable to retrieve a way to get the value, no update");	
	}
	
	private void log(String message) {
		logger.warn(message+" of type {}", getTypeOfClassCached());
	}
	
	private String getTypeOfClassCached() {
		return value.map(innerObject -> innerObject.getClass().getCanonicalName()).orElse("unknown");
	}

}

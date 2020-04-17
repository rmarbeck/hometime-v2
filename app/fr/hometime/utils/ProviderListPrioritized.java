package fr.hometime.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ProviderListPrioritized implements Prioritized {

	List<ProviderAndResult<?>> providersAndResults;
	int prioritizationLevel;
	
	public ProviderListPrioritized(int prioritizationLevel)  {
		super();
		this.prioritizationLevel = prioritizationLevel;
		this.providersAndResults = new ArrayList<>();
	}
	
	public static ProviderListPrioritized of(int prioritizationLevel) {
		return new ProviderListPrioritized(prioritizationLevel);
	}
	
	public <T> ProviderListPrioritized add(String typeOfData, DataProvider provider, Function<DataProvider, Optional<List<T>>> getter) {
		this.providersAndResults.add(new ProviderAndResult<T>(typeOfData, provider, getter, prioritizationLevel));
		return this;
	}
	
	public Optional<ProviderAndResult<?>> retrieve(String typeOfData) {
		return this.providersAndResults.stream().filter(provider -> provider.getTypeOfData().equals(typeOfData)).filter(provider -> provider.getOrRetrieve(true).isPresent()).findFirst();
	}
	
	public int getPriorityLevel() {
		return prioritizationLevel;
	}
}

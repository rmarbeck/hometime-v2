package fr.hometime.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ProviderListPrioritized implements Prioritized {
	public class ProviderAndResult<T> implements Prioritized {
		String typeOfData;
		DataProvider provider;
		Function<DataProvider, Optional<List<T>>> getter;
		Optional<List<T>> value;
		int prioritizationLevel;
		
		ProviderAndResult(String typeOfData, DataProvider provider, Function<DataProvider, Optional<List<T>>> getter, int prioritizationLevel) {
			this.typeOfData = typeOfData;
			this.provider = provider;
			this.getter = getter;
			this.value =  Optional.of(new ArrayList<>());
			this.prioritizationLevel = prioritizationLevel;
		}
		
		Optional<List<T>> getOrRetrieve(boolean forceRetrieving) {
			if (forceRetrieving)
				this.value = this.getter.apply(provider);
			return this.value;
		}
		
		public Optional<List<T>> get() {
			return this.value;
		}
		
		String getTypeOfData() {
			return typeOfData;
		}
		
		public int getPriorityLevel() {
			return prioritizationLevel;
		}
	}
	
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

package fr.hometime.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import models.RolexSerial;

public class RolexYearFromSerialResolverSevenDigitsHelper {
	
	public static List<RolexYearFromSerialResolverSevenDigitsImpl> createFromListOfStartingSerials(List<Integer> serials, int firstYear) {
		//serials.stream().skip(1).collect(initList(serials.get(0), firstYear), accumulate(), combiner(), finisher(), characteristics()); 
		return null;
	}
	
	private static Supplier<List<RolexYearFromSerialResolverSevenDigitsImpl>> initList(Integer firstSerial, int firstYear) {
		return () -> {
					List resultingList = new ArrayList<RolexYearFromSerialResolverSevenDigitsImpl>();
					resultingList.add(RolexYearFromSerialResolverSevenDigitsImpl.createOneYearMatcher(firstSerial, firstSerial, firstYear));
					return resultingList;
					};
	}
	
	private static BiConsumer<List<RolexYearFromSerialResolverSevenDigitsImpl>, Integer> accumulate() {
		return (currentList, newSerial) -> {
					RolexYearFromSerialResolverSevenDigitsImpl previousResolverInList = currentList.get(currentList.size()-1); 
					previousResolverInList.updateLastSerialIncluded(newSerial-1);
					currentList.add((RolexYearFromSerialResolverSevenDigitsImpl) RolexYearFromSerialResolverSevenDigitsImpl.createOneYearMatcher(newSerial, newSerial, previousResolverInList.getLastYearInRange()+1));
					};
	}
	
	private static BinaryOperator<List<RolexYearFromSerialResolverSevenDigitsImpl>> combiner() {
		return (list1, list2) -> Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
	}
	
	private static Function<List<RolexYearFromSerialResolverSevenDigitsImpl>, List<RolexYearFromSerialResolverSevenDigitsImpl>> finisher() {
		return (currentList) ->  {
			RolexYearFromSerialResolverSevenDigitsImpl lastResolverInList = currentList.get(currentList.size()-1);
			lastResolverInList.updateLastSerialIncluded(9999999);
			return currentList;
		};
	}
	
	public static Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
	}
	
	public RolexYearFromSerialResolver updatePreviousResolverAndcreateNextRangeOfSerial(int lastSerialIncluded, RolexYearFromSerialResolverSevenDigitsHelper previousResolver) {
		return null;//updatePreviousResolverAndcreateNextRangeOfSerial(lastSerialIncluded, previousResolver.lastYearInRange+1, previousResolver);
	}
	
	public RolexYearFromSerialResolver updatePreviousResolverAndcreateNextRangeOfSerial(int lastSerialIncluded, int year, RolexYearFromSerialResolverSevenDigitsHelper previousResolver) {
		return updatePreviousResolverAndcreateNextRangeOfSerial(lastSerialIncluded, year, year, previousResolver);
	}
	
	public RolexYearFromSerialResolver updatePreviousResolverAndcreateNextRangeOfSerial(int lastSerialIncluded, int firstYearInRange, int lastYearInRange, RolexYearFromSerialResolverSevenDigitsHelper previousResolver) {
		return null;//new RolexYearFromSerialResolverSevenDigitsHelper(previousResolver.lastSerialIncluded, lastSerialIncluded, firstYearInRange, lastSerialIncluded);
	}
}

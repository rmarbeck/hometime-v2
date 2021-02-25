package fr.hometime.utils;

import java.util.Optional;
import java.util.regex.Pattern;

import models.RolexSerial;
import models.RolexYearFromSerialResult;

public class RolexYearFromSerialResolverSevenDigitsImpl implements RolexYearFromSerialResolver {
	private int startingSerialIncluded;
	private int lastSerialIncluded;
	
	private int firstYearInRange;
	private int lastYearInRange;
	
	private RolexYearFromSerialResolverSevenDigitsImpl(	int startingSerialIncluded,
														int lastSerialIncluded,
														int firstYearInRange,
														int lastYearInRange	) {
		this.startingSerialIncluded = startingSerialIncluded;
		this.lastSerialIncluded = lastSerialIncluded;
		this.firstYearInRange = firstYearInRange;
		this.lastYearInRange = lastYearInRange;
	}
	
	public static RolexYearFromSerialResolver createOneYearMatcher(int startingSerialIncluded, int lastSerialIncluded, int year) {
		return createYearRangeMatcher(startingSerialIncluded, lastSerialIncluded, year, year);
	}
	
	public static RolexYearFromSerialResolver createYearRangeMatcher(int startingSerialIncluded, int lastSerialIncluded, int firstYearInRange, int lastYearInRange) {
		return new RolexYearFromSerialResolverSevenDigitsImpl(startingSerialIncluded, lastSerialIncluded, firstYearInRange, lastYearInRange);
	}
	
	public void updateLastSerialIncluded(int lastSerialIncluded) {
		this.lastSerialIncluded = lastSerialIncluded;
	}
	
	@Override
	public boolean doFilter(Optional<RolexSerial> serialToTest) {
		return serialToTest.map(serial -> Pattern.matches("[1-9][0-9]{6}", serial.getSerialAsString()) && doMatch(Integer.valueOf(serial.getSerialAsString()))).orElse(false);
	}
	
	private boolean doMatch(int serialToTest) {
		return serialToTest >= startingSerialIncluded && serialToTest <= lastSerialIncluded;
	}

	@Override
	public Optional<RolexYearFromSerialResult> doResolveIfMatches(Optional<RolexSerial> serialToTest) {
		if (!doFilter(serialToTest))
			return Optional.empty();
		if (firstYearInRange == lastYearInRange)
			return Optional.of(RolexYearFromSerialResolver.createSimpleYear(firstYearInRange));
		return Optional.of(RolexYearFromSerialResolver.createSimpleRange(firstYearInRange, lastYearInRange));
		
	}

	public int getStartingSerialIncluded() {
		return startingSerialIncluded;
	}

	public int getLastYearInRange() {
		return lastYearInRange;
	}

}

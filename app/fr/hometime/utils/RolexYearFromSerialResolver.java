package fr.hometime.utils;

import java.util.Optional;

import models.RolexSerial;
import models.RolexYearFromSerialResult;

public interface RolexYearFromSerialResolver {
	public boolean doFilter(Optional<RolexSerial> serial);
	public Optional<RolexYearFromSerialResult> doResolveIfMatches(Optional<RolexSerial> serial);
	
	public static RolexYearFromSerialResult createSimpleYear(int year) {
		return RolexYearFromSerialResult.newFromSingleYear(year);
	}
	
	public static RolexYearFromSerialResult createSimpleRange(int startingYear, int endingYear) {
		return RolexYearFromSerialResult.newFromRange(startingYear, endingYear);
	}
	
	public static RolexYearFromSerialResult createTwoRanges(int startingYear, int endingYear, int altStartingYear, int altEndingYear) {
		return RolexYearFromSerialResult.newFromRange(startingYear, endingYear).addRange(altStartingYear, altEndingYear);
	}
}

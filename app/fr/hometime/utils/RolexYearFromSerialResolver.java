package fr.hometime.utils;

import java.util.Optional;

import models.RolexSerial;

public interface RolexYearFromSerialResolver {
	public class Result {
		private Optional<Integer> startYear = Optional.empty();
		private Optional<Integer> endYear = Optional.empty();
		private Optional<Integer> altStartYear = Optional.empty();
		private Optional<Integer> altEndYear = Optional.empty();
	
		public Result(int startYear) {
			this(Optional.of(startYear), Optional.empty(), Optional.empty(), Optional.empty());
		}
		
		protected Result(int startYear, int endYear) {
			this(Optional.of(startYear), Optional.of(endYear), Optional.empty(), Optional.empty());
		}
		
		protected Result(int startYear, int endYear, int altStartYear, int altEndYear) {
			this(Optional.of(startYear), Optional.of(endYear), Optional.of(altStartYear), Optional.of(altEndYear));
		}
		
		private Result(Optional<Integer> startYear, Optional<Integer> endYear, Optional<Integer> altStartYear, Optional<Integer> altEndYear) {
			this.startYear = startYear;
			this.endYear = endYear;
			this.altStartYear = altStartYear;
			this.altEndYear = altEndYear;
		}
		
		public Optional<Integer> getStartYear() {
			return startYear;
		}
		
		public Optional<Integer> getEndYear() {
			return endYear;
		}
		
		public boolean isSingleYear() {
			return startYear.isPresent() && endYear.equals(startYear);
		}
		
		public boolean isOpenRange() {
			return !endYear.isPresent();
		}
	}
	
	public boolean doFilter(Optional<RolexSerial> serial);
	public Optional<Result> doResolveIfMatches(Optional<RolexSerial> serial);
	
	public static Result createSimpleYear(int value) {
		return new Result(value);
	}
	
	public static Result createSimpleRange(int startingValue, int endingValue) {
		return new Result(startingValue, endingValue);
	}
	
	public static Result createTwoRanges(int startingValue, int endingValue, int altStartingValue, int altEndingValue) {
		return new Result(startingValue, endingValue, altStartingValue, altEndingValue);
	}
}

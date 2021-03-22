package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RolexYearFromSerialResult {
	private final static int MAX_YEAR = 9999;
	private final static int MIN_YEAR = 0;
	
	public final static int RESULT_TYPE_SINGLE_YEAR = 1;
	public final static int RESULT_TYPE_SINGLE_RANGE = 2;
	public final static int RESULT_TYPE_OPEN_RANGE = 3;
	public final static int RESULT_TYPE_TWO_SINGLE_YEARS = 4;
	public final static int RESULT_TYPE_DEFAULT = 0;
	
	public static class Years {
		private List<Integer> years;
		private boolean isOpenRange = false;
		
		private Years() {
			super();
			this.years = new ArrayList<Integer>();
		}
		
		public static Years of() {
			return new Years();
		}
		
		public static Years from(List<Range> ranges) {
			return of().add(ranges);
		}
		
		public Years add(List<Range> ranges) {
			ranges.stream().forEach(this::add);
			return this;
		}
		
		public Years add(Range newRange) {
			if (newRange.isOpenRange()) {
				this.isOpenRange = true;
				this.years.add(newRange.getStartYear().get());
			} else {
				IntStream.range(newRange.getStartYear().get(), newRange.getEndYear().get()+1).forEach(this::addSingleYear);
			}
			return this;
		}
		
		public Years addSingleYear(Integer year) {
			years.add(year);
			return this;
		}
		
		public List<Integer> getYears() {
			return years.parallelStream().mapToInt(Integer::intValue).distinct().sorted().boxed().collect(Collectors.toList());
		}
		
		public String toString() {
			return getYears().stream().map(Object::toString).collect(Collectors.joining(", ")) + (isOpenRange()?"+":"");
		}
		
		public boolean isOpenRange() {
			return isOpenRange;
		}
		
		public boolean isSingleYear() {
			return !isOpenRange && years.size() == 1; 
		}
		
		public boolean containsTwoYearsExactly() {
			return !isOpenRange && years.size() == 2;
		}

		public int getNumberOfYears() {
			return years.size();
		}
		
		public Optional<Integer> getSingleYear() {
			if (isSingleYear())
				return Optional.of(years.get(0));
			return Optional.empty();
		}
	}
	
	
	public static class Range {
		private Optional<Integer> startYear = Optional.empty();
		private Optional<Integer> endYear = Optional.empty();
		
		private Range(Integer startYear) {
			this.startYear = Optional.of(startYear);
		}
		
		private Range(Integer startYear, Integer endYear) {
			this.startYear = Optional.of(startYear);
			this.endYear = Optional.of(endYear);
		}
		
		public static Range of(Integer startYear) {
			return new Range(startYear);
		}
		
		public static Range of(Integer startYear, Integer endYear) {
			return new Range(startYear, endYear);
		}
		
		public Optional<Integer> getStartYear() {
			return startYear;
		}
		
		public void setStartYear(Integer startYear) {
			this.startYear = Optional.of(startYear);
		}
		
		public Optional<Integer> getEndYear() {
			return endYear;
		}
		
		public void setEndYear(Integer endYear) {
			this.endYear = Optional.of(endYear);
		}
		
		public boolean isOpenRange() {
			return !endYear.isPresent();
		}
		
		public boolean isSingleYear() {
			return startYear.isPresent() && endYear.equals(startYear);
		}
		
		public List<Range> merge(Range toMergeWith) {		
			if (this.isOpenRange())
				return mergeFromOpenRange(toMergeWith);
			
			if (toMergeWith.isOpenRange())
				return toMergeWith.mergeFromOpenRange(this);
			
			List<Range> result = new ArrayList<Range>();
			
			if (doesOverlapWith(toMergeWith)) {
				result.add(new Range(minOf(this.startYear, toMergeWith.startYear), maxOf(this.endYear, toMergeWith.endYear)));
			} else {
				result.add(toMergeWith);
				result.add(this);
			}
			
			return result;	
		}
		
		private List<Range> mergeFromOpenRange(Range toMergeWith) {
			List<Range> result = new ArrayList<Range>();
			if (toMergeWith.isOpenRange() || this.doesOpenRangeOverlapWith(toMergeWith)) {
				result.add(mergeOpenRangesAsOne(toMergeWith));
			} else {
				result.add(toMergeWith);
				result.add(this);
			}
			
			return result;
		}
		
		private Range mergeOpenRangesAsOne(Range toMergeWith) {
			return new Range(minOf(this.startYear, toMergeWith.startYear));
		}
		
		private boolean doesOpenRangeOverlapWith(Range toCompare) {
			return isFirstBeforeOrSameThanSecond(this.startYear, toCompare.endYear);
		}
		
		private boolean doesOverlapWith(Range toCompare) {
			if (maxOf(this.startYear, toCompare.startYear) > minOf(this.endYear, toCompare.endYear))
				return false;
			return true;
		}
		
		private boolean isFirstBeforeOrSameThanSecond(Optional<Integer> first, Optional<Integer> second) {
			return first.map(firstValue -> firstValue <= second.orElse(MIN_YEAR)).orElse(false);
		}
		
		private int minOf(Optional<Integer> first, Optional<Integer> second) {
			return Math.min(first.orElse(MAX_YEAR), second.orElse(MAX_YEAR));
		}
		
		private int maxOf(Optional<Integer> first, Optional<Integer> second) {
			return Math.max(first.orElse(MIN_YEAR), second.orElse(MIN_YEAR));
		}
	}
	
	private List<Range> ranges;
	
	private RolexYearFromSerialResult() {
		this.ranges = new ArrayList<Range>();
	}
	
	private RolexYearFromSerialResult(List<Range> ranges) {
		this.ranges = ranges;
	}
	
	private RolexYearFromSerialResult(Integer year) {
		this();
		this.ranges.add(Range.of(year));
	}
	
	private RolexYearFromSerialResult(Integer startYear, Integer endYear) {
		this.ranges = new ArrayList<Range>();
		this.ranges.add(Range.of(startYear, endYear));
	}
	
	private RolexYearFromSerialResult(Range range) {
		this.ranges = new ArrayList<Range>();
		this.ranges.add(range);
	}
	
	public static RolexYearFromSerialResult newFromOpenRange(Integer year) {
		return new RolexYearFromSerialResult(year);
	}
	
	public static RolexYearFromSerialResult newFromSingleYear(Integer year) {
		return new RolexYearFromSerialResult(year, year);
	}
	
	public static RolexYearFromSerialResult newFromRange(Integer startYear, Integer endYear) {
		return new RolexYearFromSerialResult(startYear, endYear);
	}
	
	public static RolexYearFromSerialResult newFromRange(Range range) {
		return new RolexYearFromSerialResult(range);
	}
	
	public RolexYearFromSerialResult addRange(Range range) {
		this.ranges.add(range);
		return this;
	}
	
	public RolexYearFromSerialResult addOpenRange(Integer startYear) {
		return addRange(Range.of(startYear));
	}
	
	public RolexYearFromSerialResult addRange(Integer startYear, Integer endYear) {
		return addRange(Range.of(startYear, endYear));
	}
	
	public RolexYearFromSerialResult addSingleYear(Integer year) {
		return addRange(Range.of(year, year));
	}
	
	public Optional<RolexYearFromSerialResult> mergeWith(Optional<RolexYearFromSerialResult> toMergeWith) {
		if (toMergeWith.isPresent())
			return Optional.of(this.mergeWith(toMergeWith.get()));
		return Optional.of(this);
	}
	
	private RolexYearFromSerialResult mergeWith(RolexYearFromSerialResult toMergeWith) {
		List<Range> mergedRanges = ranges.parallelStream().flatMap(range -> toMergeWith.ranges.parallelStream().map(innerRange -> range.merge(innerRange))).flatMap(Collection::stream).collect(Collectors.toList());
		
		return new RolexYearFromSerialResult(mergedRanges);
	}

	public List<Range> getResults() {
		return ranges;
	}
	
	public Years getYears() {
		return Years.from(ranges);
	}
	
	public boolean isSingleYear() {
		return getYears().isSingleYear();
	}
	
	public Optional<Integer> getSecondYearPossible() {
		if (hasTwoPossibleYears())
			return getYears().getYears().stream().sorted().reduce((first, second) -> second);
		return Optional.empty();
	}
	
	public Optional<Integer> getFirstYearPossible() {
		if (hasTwoPossibleYears())
			return getYears().getYears().stream().sorted().findFirst();
		return Optional.empty();
	}
	
	public boolean hasTwoPossibleYears() {
		return getYears().containsTwoYearsExactly();
	}
	
	public Optional<Integer> getSingleYear() {
		return getYears().getSingleYear();
	}
	
	public Optional<Integer> getStartingYearFromOpenRange() {
		if (isOpenRange() && getYears().getNumberOfYears() == 1)
			return Optional.of(getYears().getYears().get(0));
		return Optional.empty();
	}
	
	public boolean isOpenRange() {
		return getYears().isOpenRange();
	}
	
	public boolean isSingleRange() {
		if (!isOpenRange() && !isSingleYear())
			return getYears().getYears().stream().count() == (getLastYear() - getFirstYear() + 1);
		return false;
	}
	
	private Integer getFirstYear() {
		return getYears().getYears().stream().findFirst().get();
	}
	
	private Integer getLastYear() {
		return getYears().getYears().stream().sorted(Collections.reverseOrder()).findFirst().get();
	}
	
	public String toString() {
		return this.getClass() + " : "+ getYears().years.stream().map(Object::toString).collect(Collectors.joining(", ")) + (isOpenRange()?"+":"");
	}
	
	public int typeOfResult() {
		if (isSingleYear())
			return RESULT_TYPE_SINGLE_YEAR;
		if (isOpenRange())
			return RESULT_TYPE_OPEN_RANGE;
		if (isSingleRange())
			return RESULT_TYPE_SINGLE_RANGE;
		if (hasTwoPossibleYears())
			return RESULT_TYPE_TWO_SINGLE_YEARS;
		
		return RESULT_TYPE_DEFAULT;
	}
	
	public List<String> getResultAsDisplayableValue() {
		List<Integer> result = getYearsAsDisplayableValue();
		if (result.size() > 2)
			return Arrays.asList(result.stream().limit(result.size()-1).map(value -> value.toString()).collect(Collectors.joining(", ")).concat(" ou "+result.get(result.size()-1)));
		return result.stream().map(value -> value.toString()).collect(Collectors.toList());
	}
	
	private List<Integer> getYearsAsDisplayableValue() {
		switch (typeOfResult()) {
			case RESULT_TYPE_SINGLE_YEAR:
				return Arrays.asList(getSingleYear().get());
			case RESULT_TYPE_OPEN_RANGE:
				return Arrays.asList(getFirstYear());
			case RESULT_TYPE_SINGLE_RANGE:
				return Arrays.asList(getFirstYear(), getLastYear());	
			case RESULT_TYPE_TWO_SINGLE_YEARS:
				return Arrays.asList(getFirstYearPossible().get(), getSecondYearPossible().get());
			default:
				return getYears().getYears();
		}
	}
}

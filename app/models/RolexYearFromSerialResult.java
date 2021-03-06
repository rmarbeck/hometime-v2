package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RolexYearFromSerialResult {
	private int MAX_YEAR = 9999;
	private int MIN_YEAR = 0;
	
	public class Range {
		private Optional<Integer> startYear = Optional.empty();
		private Optional<Integer> endYear = Optional.empty();
		
		Range(Integer startYear) {
			this.startYear = Optional.of(startYear);
		}
		
		Range(Integer startYear, Integer endYear) {
			this.startYear = Optional.of(startYear);
			this.endYear = Optional.of(endYear);
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
			if (toMergeWith.isOpenRange()) {
				result.add(mergeOpenRangesAsOne(toMergeWith));
			} else {
				if (this.doesOpenRangeOverlapWith(toMergeWith)) {
					result.add(new Range(minOf(this.startYear, toMergeWith.startYear)));
				} else {
					result.add(toMergeWith);
					result.add(this);
				}
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
		this.ranges.add(this.new Range(year));
	}
	
	private RolexYearFromSerialResult(Integer startYear, Integer endYear) {
		this.ranges = new ArrayList<Range>();
		this.ranges.add(this.new Range(startYear, endYear));
	}
	
	private RolexYearFromSerialResult(Range range) {
		this.ranges = new ArrayList<Range>();
		this.ranges.add(range);
	}
	
	public static RolexYearFromSerialResult newFromSingleYear(Integer year) {
		return new RolexYearFromSerialResult(year);
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
	
	public RolexYearFromSerialResult addRange(Integer startYear, Integer endYear) {
		return addRange(this.new Range(startYear, endYear));
	}
	
	public RolexYearFromSerialResult addSingleYear(Integer year) {
		return addRange(this.new Range(year));
	}

	public List<Range> getResults() {
		return ranges;
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
}

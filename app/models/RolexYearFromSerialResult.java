package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolexYearFromSerialResult {
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
			List<Range> result = new ArrayList<Range>();
		
			if (this.isOpenRange() && toMergeWith.isOpenRange()) {
				result.add(new Range(Math.min(this.startYear.get(), toMergeWith.startYear.get())));
			} else {
				if (this.isOpenRange() && toMergeWith.startYear.get() >= this.startYear.get())
					result.add(this);
			}
			return null;	
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
		return null;//return ranges.parallelStream().flatMap(range -> toMergeWith.ranges.parallelStream().map(innerRange -> ));
	}
}

package models;

import static fr.hometime.utils.SecurityHelper.doesFieldContainSPAM;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Validatable;
import play.data.validation.Constraints.Validate;
import play.data.validation.ValidationError;

/**
 * A form processing DTO that maps to the QuotationRequest form.
 */
@Validate
public class ServiceTestRequestData implements Validatable<List<ValidationError>> {
	public enum MovementTypes {
	    MANUAL ("1"),
	    AUTO ("2");
	    
		private String name = "";
		    
		MovementTypes(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public static MovementTypes fromString(String name) {
	        for (MovementTypes type : MovementTypes.values()) {
	            if (type.name.equals(name)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}
	
	public enum BuildPeriod {
	    THIS_YEAR ("0"),
	    THIS_YEAR_MINUS_1 ("1"),
	    THIS_YEAR_MINUS_2 ("2"),
	    THIS_YEAR_MINUS_3 ("3"),
	    THIS_YEAR_MINUS_4 ("4"),
	    THIS_YEAR_MINUS_5 ("5"),
	    THIS_YEAR_MINUS_6 ("6"),
	    THIS_YEAR_MINUS_7 ("7"),
	    THIS_YEAR_MINUS_8 ("8"),
	    THIS_YEAR_MINUS_9 ("9"),
	    THIS_YEAR_MINUS_10 ("10"),
	    THIS_YEAR_MINUS_11_20 ("11"),
	    THIS_YEAR_MINUS_21_30 ("12"),
	    THIS_YEAR_MINUS_31_40 ("13"),
	    THIS_YEAR_MINUS_41_50 ("14"),
	    THIS_YEAR_MINUS_51_60 ("15"),
	    THIS_YEAR_MINUS_61_100 ("16");
	    
		private String name = "";
		    
		BuildPeriod(String name){
		    this.name = name;
		}

		public String toString() {
		    return name;
		}
		
		public int intValue() {
			return Integer.valueOf(name);
		}
		
		public static BuildPeriod fromString(String name) {
	        for (BuildPeriod type : BuildPeriod.values()) {
	            if (type.name.equals(name)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}
	
	public enum LastServiceYear {
	    THIS_YEAR ("0"),
	    THIS_YEAR_MINUS_1 ("1"),
	    THIS_YEAR_MINUS_2 ("2"),
	    THIS_YEAR_MINUS_3 ("3"),
	    THIS_YEAR_MINUS_4 ("4"),
	    THIS_YEAR_MINUS_5 ("5"),
	    THIS_YEAR_MINUS_6 ("6"),
	    THIS_YEAR_MINUS_7 ("7"),
	    THIS_YEAR_MINUS_8 ("8"),
	    THIS_YEAR_MINUS_9 ("9"),
	    THIS_YEAR_MINUS_10 ("10"),
	    THIS_YEAR_MINUS_11_and_more ("11"),
	    NONE_AFTER_BUYING ("12"),
	    UNKNOWN ("13");
	    
		private String name = "";
		    
		LastServiceYear(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public int intValue() {
			return Integer.valueOf(name);
		}
		
		public static LastServiceYear fromString(String name) {
	        for (LastServiceYear type : LastServiceYear.values()) {
	            if (type.name.equals(name)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}
	
	public enum UsageFrequency {
	    EVERY_DAY ("0"),
	    MOST_OF_TIME ("1"),
	    RARELY ("2"),
	    NEVER ("3");
	    
		private String name = "";
		    
		UsageFrequency(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public static UsageFrequency fromString(String name) {
	        for (UsageFrequency type : UsageFrequency.values()) {
	            if (type.name.equals(name)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}
	
	public enum MovementComplexity {
	    THREE_HANDS ("0"),
	    CHRONO ("1"),
	    OTHER ("2");
	    
		private String name = "";
		    
		MovementComplexity(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public int intValue() {
			return Integer.valueOf(name);
		}
		
		public static MovementComplexity fromString(String name) {
	        for (MovementComplexity complexity : MovementComplexity.values()) {
	            if (complexity.name.equals(name)) {
	                return complexity;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}
	
	public enum TestResult {
	    IN_MORE_THAN_5_YEARS ("0"),
	    IN_5_YEARS ("1"),
	    IN_4_YEARS ("2"),
	    IN_2_TO_3_YEARS ("3"),
	    NEXT_YEAR ("4"),
	    THIS_YEAR ("5"),
	    NOW_FOR_SOFT_SERVICE ("6"),
	    NOW_FOR_FULL_SERVICE ("7");
	    
		public int intValue() {
			return Integer.valueOf(name);
		}
	    
		private String name = "";
		    
		TestResult(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public static TestResult fromString(String name) {
	        for (TestResult type : TestResult.values()) {
	            if (type.name.equals(name)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}

	@Constraints.Required
	public String movementType;
	
	@Constraints.Required
	public String movementComplexity;
	
	@Constraints.Required
	public String buildPeriod;
	
	@Constraints.Required
	public String lastServiceYear;
	
	@Constraints.Required
	public String performanceIssue;
	
	@Constraints.Required
	public String powerReserveIssue;
	
	@Constraints.Required
	public String waterIssue;

	@Constraints.Required
	public String doingSport;
	
	@Constraints.Required
	public String usageFrequency;

	@Constraints.MaxLength(1000)
	public String model;

	@Constraints.MaxLength(1000)
	public String nameOfCustomer;

	@Constraints.Email
	@Constraints.MaxLength(60)
	public String email;

	@Override
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
        if (doesFieldContainSPAM(model))
    		errors.add(new ValidationError("model", "buy.request.validation.error.model.spam.detected"));
        return errors.isEmpty() ? null : errors;
    }
    
    public ServiceTestRequestData() {
    	super();
    }
}

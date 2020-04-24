package models;

import static fr.hometime.utils.SecurityHelper.doesFieldContainSPAM;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Validatable;
import play.data.validation.Constraints.Validate;
import play.data.validation.ValidationError;

/**
 * A form processing DTO that maps to the QuotationRequest form.
 */
@Validate
public class BuyRequestData implements Validatable<List<ValidationError>> {
	public enum WatchStory {
	    NEW_ONLY ("NEW_ONLY"),
	    WORN_ONLY ("WORN_ONLY"),
	    BOTH ("BOTH");
	    
		private String name = "";
		    
		WatchStory(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}

		public static WatchStory fromString(String name) {
	        for (WatchStory story : WatchStory.values()) {
	            if (story.name.equals(name)) {
	                return story;
	            }
	        }
	        throw new IllegalArgumentException("Illegal story name: " + name);
	    }
	}
	
	public enum Package {
		FULL_SET_ONLY ("FULL_SET_ONLY"),
		WATCH_ONLY ("WATCH_ONLY"),
		BOTH ("BOTH"),;

		private String name = "";

		Package(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public static Package fromString(String name) {
	        for (Package pack : Package.values()) {
	            if (pack.name.equals(name)) {
	                return pack;
	            }
	        }
	        throw new IllegalArgumentException("Illegal package name: " + name);
	    }
	}

	public enum Timeframe {
		EMERGENCY ("EMERGENCY"),
		WITHIN_MONTH ("WITHIN_MONTH"),
		WITHIN_3_MONTHS ("WITHIN_3_MONTHS"),
		OPPORTUNITY ("OPPORTUNITY");

		private String name = "";

		Timeframe(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public static Timeframe fromString(String name) {
	        for (Timeframe timeframe : Timeframe.values()) {
	            if (timeframe.name.equals(name)) {
	                return timeframe;
	            }
	        }
	        throw new IllegalArgumentException("Illegal timeframe name: " + name);
	    }
	}
	
	@Constraints.Required
	public String story;
	
	@Constraints.Required
	public String pack;
	
	@Constraints.Required
	public String timeframe;

	
	public String brand;
	
	@Constraints.Required
	@Constraints.MaxLength(1000)
	public String model;

	@Constraints.MaxLength(10000)
	public String criteria;
	
	@Constraints.MaxLength(10000)
	public String remark;

	public String expectedPrice;
	
	public String priceHigherBound;
	
	@Constraints.Required
	public String nameOfCustomer;

	@Constraints.Required
	@Constraints.Email
	@Constraints.MaxLength(60)
	public String email;
	
	public String phoneNumber;
	
	@Constraints.Required
	public String city;

	@Override
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
        if (doesFieldContainSPAM(criteria))
    		errors.add(new ValidationError("criteria", "buy.request.validation.error.criteria.spam.detected"));
        if (doesFieldContainSPAM(remark))
    		errors.add(new ValidationError("remark", "buy.request.validation.error.remark.spam.detected"));
        if (doesFieldContainSPAM(model))
    		errors.add(new ValidationError("model", "buy.request.validation.error.model.spam.detected"));
        return errors.isEmpty() ? null : errors;
    }
    
    public BuyRequestData() {
    	super();
    }
    
    public void setCity(String newCity) {
    	this.city = newCity;
    }
}

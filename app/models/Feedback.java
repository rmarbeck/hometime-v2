package models;

import java.util.Date;

/**
 * Definition of a Customer Feedback
 */
public class Feedback {
	
	public enum FeedbackType {
	    FACEBOOK ("FACEBOOK"),
	    LINKED_IN ("LINKED_IN"),
	    INTERNAL ("INTERNAL"),
	    GOOGLE ("GOOGLE"),
	    OTHER ("OTHER");
	    
		private String name = "";
		    
		FeedbackType(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public static FeedbackType fromString(String name) {
	        for (FeedbackType type : FeedbackType.values()) {
	            if (type.name.equals(name)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}

	public Long id;
	
	public Date creationDate;
	
	public Date displayDate;
	
	public String body;
	
	public String author;
	
	public String pictureUrl;
	
	public FeedbackType typeOfFeedback;
	
	public Boolean shouldDisplay = true;
	
	public Boolean mayBeEmphasized = true;
	
	public Feedback() {
		super();
	}

	public Date getDisplayDate() {
		return displayDate;
	}

	public Boolean shouldDisplay() {
		return shouldDisplay;
	}

	public Boolean mayBeEmphasized() {
		return mayBeEmphasized;
	}    
}



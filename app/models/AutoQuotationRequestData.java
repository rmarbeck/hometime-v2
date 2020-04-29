package models;

/**
 * A form processing DTO that maps to the AutoRequestRequest form.
 */
public class AutoQuotationRequestData {
	public enum MovementTypes {
	    MANUAL ("MANUAL"),
	    AUTO ("AUTO"),
		QUARTZ ("QUARTZ"),
		UNKNOWN ("UNKNOWN");
	    
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
		
	public enum MovementComplexity {
	    TWO_OR_THREE_HANDS ("TWO_OR_THREE_HANDS"),
	    CHRONO ("CHRONO"),
	    GMT ("GMT"),
	    OTHER ("OTHER"),
	    UNKNOWN ("UNKNOWN");
	    
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
	
	public enum EmergencyLevel {
	    NORMAL ("NORMAL"),
	    HIGH ("HIGH"),
	    VERY_HIGH ("VERY_HIGH"),
	    LOW ("LOW");
	    
		private String name = "";
		    
		EmergencyLevel(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public int intValue() {
			return Integer.valueOf(name);
		}
		
		public static EmergencyLevel fromString(String name) {
	        for (EmergencyLevel emergency : EmergencyLevel.values()) {
	            if (emergency.name.equals(name)) {
	                return emergency;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}
	
	public enum WorkingCondition {
	    WORKING ("WORKING"),
	    NOT_WORKING ("NOT_WORKING"),
	    BROKEN ("BROKEN");
	    
		private String name = "";
		    
		WorkingCondition(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public int intValue() {
			return Integer.valueOf(name);
		}
		
		public static WorkingCondition fromString(String name) {
	        for (WorkingCondition condition : WorkingCondition.values()) {
	            if (condition.name.equals(name)) {
	                return condition;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}

	public String brand;
	
	public MovementTypes movementType;
	
	public MovementComplexity movementComplexity;
	
	public EmergencyLevel emergencyLevel;
	
	public WorkingCondition workingCondition;
	
	public String privateInfos;
    
    public AutoQuotationRequestData() {
    	super();
    }
}

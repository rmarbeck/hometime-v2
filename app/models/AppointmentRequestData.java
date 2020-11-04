package models;

import java.util.ArrayList;
import java.util.List;

import fr.hometime.utils.PhoneNumberHelper;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Validatable;
import play.data.validation.ValidationError;

/**
 * A form processing DTO that maps to the AppointmentRequest form.
 */
public class AppointmentRequestData implements Validatable<List<ValidationError>> {
	public enum Reason {
	    DEPOSIT ("DEPOSIT"),
	    PICKUP ("PICKUP");
	    
		private String name = "";
		    
		Reason(String name){
		    this.name = name;
		}

		public String toString(){
		    return name;
		}
		
		public static Reason fromString(String name) {
	        for (Reason type : Reason.values()) {
	            if (type.name.equals(name)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Illegal type name: " + name);
	    }
	}

	@Constraints.Required
	public String datetimeAsString;
	
	@Constraints.Required
	public Reason reason;
	
	@Constraints.Required
	@Formats.NonEmpty
	public String customerName;
	
	@Constraints.Required
	@Formats.NonEmpty
	public String phoneNumber;
	
	public String optionnalMessage;
	
	public String status;
	
	public String datetimeAsStringNiceToDisplay;
	
	public String uniqueKey;
    
    public AppointmentRequestData() {
    	super();
    }
    
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
        if (!PhoneNumberHelper.isItAFrenchMobilePhoneNumber(phoneNumber)) {
       		errors.add(new ValidationError("customerPhoneNumber", "appointment.request.phonenumber.invalid"));
        }
        return errors.isEmpty() ? null : errors;
    }
}

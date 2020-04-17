package models;

import static fr.hometime.utils.SecurityHelper.doesFieldContainSPAM;
import static fr.hometime.utils.SecurityHelper.doesFieldSeemToBeAPhoneNumber;

import java.util.ArrayList;
import java.util.List;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Validatable;
import play.data.validation.Constraints.Validate;
import play.data.validation.ValidationError;

/**
 * A form processing DTO that maps to the CallBackRequest form.
 */
@Validate
public class CallBackRequestData implements Validatable<List<ValidationError>> {
	@Constraints.Required
	@Formats.NonEmpty
	public String number;
	@Constraints.MaxLength(1000)
	public String reason;

	@Override
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
    	if (!doesFieldSeemToBeAPhoneNumber(number))
    		errors.add(new ValidationError("number", "call.back.request.validation.error.number.not.recognised"));
    	if (doesFieldContainSPAM(reason))
    		errors.add(new ValidationError("reason", "call.back.request.validation.error.reason.spam.detected"));
    	return errors.isEmpty() ? null : errors;
    }
    
    public CallBackRequestData() {
    	super();
    }
}

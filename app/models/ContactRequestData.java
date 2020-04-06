package models;

import static fr.hometime.utils.SecurityHelper.doesFieldContainSPAM;

import java.util.ArrayList;
import java.util.List;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Validatable;
import play.data.validation.Constraints.Validate;
import play.data.validation.ValidationError;

/**
 * A form processing DTO that maps to the ContactRequest form.
 */
@Validate
public class ContactRequestData implements Validatable<List<ValidationError>> {
	@Constraints.Required
	@Formats.NonEmpty
	public String title;
	@Constraints.Required
	@Formats.NonEmpty
	@Constraints.MaxLength(1000)
	public String message;
	@Constraints.Required
	@Formats.NonEmpty
	public String name;
	@Constraints.Required
	@Constraints.Email
	public String email;

	@Override
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
    	if (doesFieldContainSPAM(message))
    		errors.add(new ValidationError("message", "contact.us.request.validation.error.question.spam.detected"));
    	return errors.isEmpty() ? null : errors;
    }
    
    public ContactRequestData() {
    	super();
    }
}

package models;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import fr.hometime.utils.RolexSerialHelper;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Validatable;
import play.data.validation.Constraints.Validate;
import play.data.validation.ValidationError;

/**
 * A form processing DTO that maps to the RolexSerialRequest form.
 */
@Validate
public class RolexSerialRequestData implements Validatable<List<ValidationError>> {
	@Constraints.Required
	@Formats.NonEmpty
	@Constraints.MaxLength(8)
	public String serial;
	@Constraints.MaxLength(16)
	public String reference;

	@Override
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
    	if (!isValidSerial(Optional.ofNullable(serial)))
    		errors.add(new ValidationError("serial", "rolex.serial.request.validation.error.serial.not.recognised"));
    	if (isValidReference(Optional.ofNullable(reference)))
    		errors.add(new ValidationError("reference", "rolex.serial.request.validation.error.reference.not.recognised"));
    	return errors.isEmpty() ? null : errors;
    }
    
    public RolexSerialRequestData() {
    	super();
    }
    
    private boolean isValidSerial(Optional<String> serialCandidate) {
    	return RolexSerialHelper.serialMandatoryTests(serialCandidate);
    }
    
    private boolean isValidReference(Optional<String> referenceCandidate) {
    	return RolexSerialHelper.referenceMandatoryTests(referenceCandidate);
    }
}

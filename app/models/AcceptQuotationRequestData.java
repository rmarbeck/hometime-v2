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
 * A form processing DTO that maps to the AcceptQuotationRequest form.
 */
@Validate
public class AcceptQuotationRequestData implements Validatable<List<ValidationError>> {
	@Constraints.Required
	@Formats.NonEmpty
	public Long orderId;
	@Constraints.Required
	@Formats.NonEmpty
	@Constraints.MaxLength(1000)
	public String price;
	@Constraints.Required
	@Formats.NonEmpty
	public String delay;
	@Constraints.Required
	@Constraints.MaxLength(10000)
	public String address;

	@Override
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
    	if (doesFieldContainSPAM(address))
    		errors.add(new ValidationError("MaxLength", "accept.quotation.request.validation.error.address.spam.detected"));
    	return errors.isEmpty() ? null : errors;
    }
    
    public AcceptQuotationRequestData() {
    	super();
    }
}

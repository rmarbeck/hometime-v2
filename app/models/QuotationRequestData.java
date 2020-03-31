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
 * A form processing DTO that maps to the QuotationRequest form.
 */
@Validate
public class QuotationRequestData implements Validatable<List<ValidationError>> {
	@Constraints.Required
	public String orderType;
	@Constraints.Required
	public String brand;
	@Constraints.Required
	public String model;
	@Constraints.Required
	public String method;
	@Constraints.MaxLength(1000)
	public String remark;
	@Constraints.Required
	@Formats.NonEmpty
    @Constraints.MaxLength(60)
	public String nameOfCustomer;
	@Constraints.Required
	@Constraints.Email
	@Constraints.MaxLength(60)
	public String email;
	public String phoneNumber;
	@Constraints.Required
	public String city;
	
	public String privateInfos;

	@Override
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();
    	if (doesFieldContainSPAM(model))
    		errors.add(new ValidationError("model", "quotation.request.validation.error.model.spam.detected"));
    	if (doesFieldContainSPAM(remark))
    		errors.add(new ValidationError("remark", "quotation.request.validation.error.remark.spam.detected"));
    	return errors.isEmpty() ? null : errors;
    }
    
    public QuotationRequestData() {
    	super();
    }
}

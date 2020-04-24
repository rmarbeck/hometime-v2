package controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hometime.utils.BrandProvider;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.*;
import play.libs.ws.*;
import models.Brand;
import models.BuyRequestData;
import models.CallBackRequestData;
import models.ContactRequestData;
import models.QuotationRequestData;

/**
 * Controller for forms processing
 * @author Utilisateur
 *
 */
public class FormProcessingController extends Controller implements WSBodyReadables, WSBodyWritables {
	private final static String WATER_ISSUE_ORDER_TYPE = "5";
	private MessagesApi messagesApi;
	private FormFactory formFactory;
	private BrandProvider brandProvider;
	private final WSClient ws;
	
	private final Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	@Inject
    public FormProcessingController(FormFactory formFactory, MessagesApi messagesApi, WSClient ws, BrandProvider brandProvider) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.brandProvider = brandProvider;
        this.ws = ws;
    }
	
	public Result displayFormSuccess(Http.Request request, String contentKey) {
		return ok(views.html.action_success.render(contentKey, request, messagesApi.preferred(request)));
	}
	
	public Result displayFormUnknownError(Http.Request request, String contentKey) {
		return ok(views.html.action_error.render(contentKey, request, messagesApi.preferred(request)));
	}
	
	/*************************************
	 * 
	 * Call Back Request Management
	 * 
	 *************************************/
	public Result prepareCallBackRequest(Http.Request request) {
		return ok(views.html.call_back_form.render(formFactory.form(CallBackRequestData.class).withDirectFieldAccess(true), request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processCallBackRequest(Http.Request request) {
		final Form<CallBackRequestData> boundForm = formFactory.form(CallBackRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.call_back_form.render(boundForm, request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = ws.url("https://www.hometime.fr/new-call-back-request-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.handle((response, error) -> handleFormResponse(response, error, request, "call.back"));
		}
	}
	
	/*************************************
	 * 
	 * Contact Us Request Management
	 * 
	 *************************************/
	public Result prepareContactRequest(Http.Request request) {
		return ok(views.html.contact_us_form.render(formFactory.form(ContactRequestData.class).withDirectFieldAccess(true), request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processContactRequest(Http.Request request) {
		final Form<ContactRequestData> boundForm = formFactory.form(ContactRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.contact_us_form.render(boundForm, request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = ws.url("https://www.hometime.fr/new-contact-request-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.handle((response, error) -> handleFormResponse(response, error, request, "contact.us"));
		}
	}
	
	/*************************************
	 * 
	 * Buy Request Management
	 * 
	 *************************************/
	public Result prepareBuyRequest(Http.Request request) {
		return ok(views.html.buy_form.render(formFactory.form(BuyRequestData.class).withDirectFieldAccess(true), brandProvider.retrieveBrandsOrderedByName(), Optional.empty(), request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processBuyRequest(Http.Request request) {
		final Form<BuyRequestData> boundForm = formFactory.form(BuyRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.buy_form.render(boundForm, brandProvider.retrieveBrandsOrderedByName(), Optional.empty(), request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = ws.url("https://www.hometime.fr/new-buy-request-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.handle((response, error) -> handleFormResponse(response, error, request, "buy"));
		}
	}
	
	/*************************************
	 * 
	 * Quotation Request Management
	 * 
	 *************************************/
	
	public Result prepareQuotationRequestWater(Http.Request request) {
		return preparedQuotation(request, Optional.empty(), Optional.of(WATER_ISSUE_ORDER_TYPE));
	}
	
	public Result prepareQuotationRequestWaterWithBrand(Http.Request request, String brandSeoName) {
		return preparedQuotation(request, Optional.of(brandSeoName), Optional.of(WATER_ISSUE_ORDER_TYPE));
	}
	
	public Result prepareQuotationRequest(Http.Request request) {
		return preparedQuotation(request, Optional.empty(), Optional.empty());
	}
	
	public Result prepareQuotationRequestWithBrand(Http.Request request, String brandSeoName) {
		return preparedQuotation(request, Optional.of(brandSeoName), Optional.empty());
	}
	
	private Result preparedQuotation(Http.Request request, Optional<String> brandSeoName, Optional<String> typeOfOrder) {
		return ok(views.html.quotation_form.render(fillQuotationRequestWithDefaultData(brandSeoName, typeOfOrder), brandProvider.retrieveBrandsOrderedByName(), getBrand(brandSeoName), request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processQuotationRequest(Http.Request request) {
		final Form<QuotationRequestData> boundForm = getQuotationRequestForm().bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.quotation_form.render(boundForm, brandProvider.retrieveBrandsOrderedByName(), Optional.empty(), request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = ws.url("https://www.hometime.fr/new-order-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.handle((response, error) -> handleFormResponse(response, error, request, "quotation"));
		}
	}
	
	/*************************************
	 * 
	 * End Of Quotation Request Management
	 * 
	 *************************************/

	/*************************************
	 * 
	 * Usefull helpers
	 * 
	 *************************************/
	
	private Result handleFormResponse(WSResponse response, Throwable error, Http.Request request, String formKey) {
		if(response != null) {
			if (response.getStatus() < 400) {
				return displayFormSuccess(request, formKey);
			} else {
				return manageFatalError(request, formKey, new Exception("response has error code "+response.getStatus()));
			}
		}
		return manageFatalError(request, formKey, error);
	}
	
	private Result manageFatalError(Http.Request request, String formKey, Throwable error) {
		logger.error("Error when trying to manage form '{}' with calling external webservice : {}", formKey, error.getLocalizedMessage());
		error.printStackTrace();
		return displayFormUnknownError(request, formKey);
	}
	
	private String flattenValues(String key, String[] values, String separator) {
		return Arrays.asList(values).stream().map(value -> { logger.error(key+"="+value); return key+"="+value;}).collect(Collectors.joining( "&" ));
	}
	
	private Form<QuotationRequestData> fillQuotationRequestWithDefaultData(Optional<String> brandSeoName, Optional<String> typeOfOrder) {
		QuotationRequestData requestPrefilledIfNeeded = new QuotationRequestData();
		brandSeoName.ifPresent(seoName -> {
			Optional<Brand> brandFound = brandProvider.getBrandBySeoName(seoName);
			brandFound.ifPresent(brand -> requestPrefilledIfNeeded.brand = brand.id.toString());
		});
		typeOfOrder.ifPresent(orderType -> requestPrefilledIfNeeded.orderType = orderType);
		requestPrefilledIfNeeded.method = QuotationRequestData.METHOD_LOCAL;
		return getQuotationRequestForm().fill(requestPrefilledIfNeeded);
	}
	
	private Optional<Brand> getBrand(Optional<String> brandSeoName) {
		return brandSeoName.map(seoName -> brandProvider.getBrandBySeoName(seoName)).orElse(Optional.empty());
	}

	private Form<QuotationRequestData> getQuotationRequestForm() {
		return formFactory.form(QuotationRequestData.class).withDirectFieldAccess(true);
	}

	private Map<String, List<String>> getHeadersForFriendlyLocation() {
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		headers.put("secretKey", Arrays.asList("staticValue"));
		return headers;
	}
	
}

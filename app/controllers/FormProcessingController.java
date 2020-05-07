package controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;

import fr.hometime.utils.BrandProvider;
import fr.hometime.utils.NewsProvider;
import fr.hometime.utils.PriceProvider;
import fr.hometime.utils.WebserviceHelper;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.*;
import play.libs.Json;
import play.libs.ws.*;
import models.AcceptQuotationRequestData;
import models.AuthenticationCheckRequestData;
import models.AutoQuotationRequestData;
import models.Brand;
import models.BuyRequestData;
import models.CallBackRequestData;
import models.ContactRequestData;
import models.QuotationRequestData;
import models.ServiceTestRequestData;

/**
 * Controller for forms processing
 * @author Utilisateur
 *
 */
public class FormProcessingController extends Controller implements WSBodyReadables, WSBodyWritables {
	public final static String SERVICE_ORDER_TYPE = "3";
	public final static String REPAIR_ORDER_TYPE = "4";
	public final static String INTERMEDIATE_ORDER_TYPE = "2";
	public final static String SETTING_UP_ORDER_TYPE = "1";
	public final static String WATER_ISSUE_ORDER_TYPE = "5";
	public final static String ORDER_TYPE_PARAMETER_NAME = "type";
	
	public final static String CONTACT_TITLE_PARAMETER_NAME = "title";
	private MessagesApi messagesApi;
	private FormFactory formFactory;
	private BrandProvider brandProvider;
	private PriceProvider priceProvider;
	private final WSClient ws;
	private final Config config;
	
	public static PriceProvider injectedPriceProvider;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
    public FormProcessingController(FormFactory formFactory, MessagesApi messagesApi, WSClient ws, BrandProvider brandProvider, PriceProvider priceProvider, Config config) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.brandProvider = brandProvider;
        this.priceProvider = priceProvider;
        injectedPriceProvider = priceProvider;
        this.ws = ws;
        this.config = config;
    }
	
	public Result displayFormSuccess(Http.Request request, String contentKey) {
		return ok(views.html.action_success.render(contentKey, request, messagesApi.preferred(request)));
	}
	
	public Result displayServiceTestFormSuccess(Http.Request request, ServiceTestRequestData.TestResult result, boolean isCustomizationAsked, Optional<String> email) {
		return ok(views.html.service_test_success.render(result, isCustomizationAsked, email, request, messagesApi.preferred(request)));
	}
	
	public Result displayFormUnknownError(Http.Request request, String contentKey) {
		return ok(views.html.action_error.render(contentKey, request, messagesApi.preferred(request)));
	}
	
	public Result processRegister(Http.Request request) {
		final DynamicForm boundForm = formFactory.form().bindFromRequest(request);
		
		logger.error("....-------> "+boundForm.get("email"));
		
		ObjectNode resultAsJson = Json.newObject();
		resultAsJson.put("message", messagesApi.preferred(request).at("register.message.ok"));
		
		return ok(resultAsJson);
	}
	
	/*************************************
	 * 
	 * Call Back Request Management
	 * 
	 *************************************/
	public Result prepareCallBackRequest(Http.Request request) {
		return ok(views.html.call_back_form.render(formFactory.form(CallBackRequestData.class).withDirectFieldAccess(true), request, messagesApi.preferred(request)));
	}
	
	public Result prepareCallBackRequest_en(Http.Request request) {
		return ok(views.html.call_back_form.render(formFactory.form(CallBackRequestData.class).withDirectFieldAccess(true), request, getMessagesForInEnglishPages(request)));
	}
	
	public CompletionStage<Result> processCallBackRequest(Http.Request request) {
		final Form<CallBackRequestData> boundForm = formFactory.form(CallBackRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.call_back_form.render(boundForm, request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = wsWithSecret("https://www.hometime.fr/new-call-back-request-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.handle((response, error) -> handleFormResponse(response, error, request, "call.back"));
		}
	}
	
	/*************************************
	 * 
	 * Contact Us Request Management
	 * 
	 *************************************/
	public Result prepareContactRequest(Http.Request request) {
		ContactRequestData requestPrefilledIfNeeded = new ContactRequestData();
		guessContactTitleFromRequest(request).ifPresent(title -> requestPrefilledIfNeeded.title = title);
		
		return ok(views.html.contact_us_form.render(formFactory.form(ContactRequestData.class).withDirectFieldAccess(true).fill(requestPrefilledIfNeeded), request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processContactRequest(Http.Request request) {
		final Form<ContactRequestData> boundForm = formFactory.form(ContactRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.contact_us_form.render(boundForm, request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = wsWithSecret("https://www.hometime.fr/new-contact-request-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.handle((response, error) -> handleFormResponse(response, error, request, "contact.us"));
		}
	}
	
	private Optional<String> guessContactTitleFromRequest(Http.Request request) {
		return request.queryString(CONTACT_TITLE_PARAMETER_NAME);
	}
	
	/*************************************
	 * 
	 * Buy Request Management
	 * 
	 *************************************/
	public Result prepareBuyRequest(Http.Request request) {
		return ok(views.html.buy_form.render(formFactory.form(BuyRequestData.class).withDirectFieldAccess(true), brandProvider.retrieveBrandsOrderedByInternalName(), Optional.empty(), request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processBuyRequest(Http.Request request) {
		final Form<BuyRequestData> boundForm = formFactory.form(BuyRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.buy_form.render(boundForm, brandProvider.retrieveBrandsOrderedByInternalName(), Optional.empty(), request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = wsWithSecret("https://www.hometime.fr/new-buy-request-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.handle((response, error) -> handleFormResponse(response, error, request, "buy"));
		}
	}
	
	/*************************************
	 * 
	 * Service Test Request Management
	 * 
	 *************************************/
	public Result prepareServiceTestRequest(Http.Request request) {
		return ok(views.html.service_test_form.render(formFactory.form(ServiceTestRequestData.class).withDirectFieldAccess(true), request, messagesApi.preferred(request)));
	}
	
	public Result testServiceTestResult(Http.Request request, String type, String bool) {
		return displayServiceTestFormSuccess(request, ServiceTestRequestData.TestResult.fromString(type), bool.equals("1")?true:false, Optional.of("toto@titi"));
	}
	
	public CompletionStage<Result> processServiceTestRequest(Http.Request request) {
		final Form<ServiceTestRequestData> boundForm = formFactory.form(ServiceTestRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.service_test_form.render(boundForm, request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = wsWithSecret("https://www.hometime.fr/new-service-test-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.handle((response, error) -> handleJsonFormResponse(response, error, request, "service.test"));
		}
	}
	
	/*************************************
	 * 
	 * Accept Quotation Request Management
	 * 
	 *************************************/
	public Result prepareAcceptQuotationRequest(Http.Request request, Long orderId, String price, String delay) {
		AcceptQuotationRequestData requestPrefilled = new AcceptQuotationRequestData();
		requestPrefilled.orderId = orderId;
		requestPrefilled.price = price;
		requestPrefilled.delay = delay;
		
		return ok(views.html.accept_quotation_form.render(formFactory.form(AcceptQuotationRequestData.class).withDirectFieldAccess(true).fill(requestPrefilled), request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processAcceptQuotationRequest(Http.Request request) {
		final Form<AcceptQuotationRequestData> boundForm = formFactory.form(AcceptQuotationRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.accept_quotation_form.render(boundForm, request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = wsWithSecret("https://www.hometime.fr/new-accept-quotation-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.handle((response, error) -> handleFormResponse(response, error, request, "accept.quotation"));
		}
	}
	
	
	
	/*************************************
	 * 
	 * Auto Request Management
	 * 
	 *************************************/
	
	public Result prepareAutoQuotation(Http.Request request) {
		return preparedAutoQuotation(request, Optional.empty());
	}
	
	public Result prepareAutoQuotationWithBrand(Http.Request request, String brandSeoName) {
		return preparedAutoQuotation(request, Optional.of(brandSeoName));
	}
	
	public Result processAutoQuotation(Http.Request request) {
		final Form<AutoQuotationRequestData> boundForm = formFactory.form(AutoQuotationRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		Optional<String> brandSeoName = brandProvider.getBrandById(Long.parseLong(boundForm.get().brand)).map(Brand::getSeoName);
		return preparedQuotation(request, brandSeoName, Optional.of(SERVICE_ORDER_TYPE));
	}
	
	private Result preparedAutoQuotation(Http.Request request, Optional<String> brandSeoName) {
		return ok(views.html.auto_quotation.render(fillAutoQuotationRequestWithDefaultData(brandSeoName), brandProvider.retrieveSupportedBrandsOrderedByInternalName(), getBrand(brandSeoName), request, messagesApi.preferred(request)));
	}
	
	private Form<AutoQuotationRequestData> fillAutoQuotationRequestWithDefaultData(Optional<String> brandSeoName) {
		AutoQuotationRequestData requestPrefilledIfNeeded = new AutoQuotationRequestData();
		brandSeoName.ifPresent(seoName -> {
			Optional<Brand> brandFound = brandProvider.getBrandBySeoName(seoName);
			brandFound.ifPresent(brand -> requestPrefilledIfNeeded.brand = brand.id.toString());
		});

		return formFactory.form(AutoQuotationRequestData.class).withDirectFieldAccess(true).fill(requestPrefilledIfNeeded);
	}
	
	/*************************************
	 * 
	 * Quartz Pricing Management
	 * 
	 *************************************/
	
	public Result prepareQuartzPrice(Http.Request request) {
		return preparedQuartzPrice(request, Optional.empty(), false);
	}
	
	public Result prepareQuartzPriceWithBrand(Http.Request request, String brandSeoName) {
		return preparedQuartzPrice(request, Optional.of(brandSeoName), false);
	}
	
	public Result prepareQuartzPrice_en(Http.Request request) {
		return preparedQuartzPrice(request, Optional.empty(), true);
	}
	
	public Result prepareQuartzPriceWithBrand_en(Http.Request request, String brandSeoName) {
		return preparedQuartzPrice(request, Optional.of(brandSeoName), true);
	}
	
	private Result preparedQuartzPrice(Http.Request request, Optional<String> brandSeoName, boolean inEnglish) {
		return ok(views.html.quartz_prices.render(fillQuartzPriceRequestWithDefaultData(brandSeoName), brandProvider.retrieveSupportedBrandsOrderedByInternalName(), getBrand(brandSeoName), request, inEnglish?getMessagesForInEnglishPages(request):messagesApi.preferred(request)));
	}
	
	private DynamicForm fillQuartzPriceRequestWithDefaultData(Optional<String> brandSeoName) {
		Map<String, Object> prefilled = new HashMap<String, Object>();
		brandSeoName.ifPresent(seoName -> {
			Optional<Brand> brandFound = brandProvider.getBrandBySeoName(seoName);
			brandFound.ifPresent(brand -> prefilled.put("brand", brand.id.toString()) );
		});

		return formFactory.form().fill(prefilled);
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
		return ok(views.html.quotation_form.render(fillQuotationRequestWithDefaultData(brandSeoName, typeOfOrder, request), brandProvider.retrieveSupportedBrandsOrderedByInternalName(), getBrand(brandSeoName), request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processQuotationRequest(Http.Request request) {
		final Form<QuotationRequestData> boundForm = getQuotationRequestForm().bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.quotation_form.render(boundForm, brandProvider.retrieveSupportedBrandsOrderedByInternalName(), Optional.empty(), request, messagesApi.preferred(request))));
		} else {
			request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" ));
			CompletionStage<? extends WSResponse> responsePromise = wsWithSecret("https://www.hometime.fr/new-order-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
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
	 * Checking Authentication certificate
	 * 
	 *************************************/
	
    public CompletionStage<Result> checkAuthentication(Http.Request request, Long id1, Long id2, Long id3) {
    	CompletionStage<? extends WSResponse> responsePromise = wsWithSecret("https://www.hometime.fr/check-authentication").addQueryParameter("id1", id1.toString()).addQueryParameter("id2", id2.toString()).addQueryParameter("id3", id3.toString()).get();
		return responsePromise.handle((response, error) -> genericHandler(response, error, request, "check.authentication.test", () -> {
			JsonNode json = response.asJson();
			  if (json == null) {
			    return badRequest("Expecting Json data");
			  }
			  return ok(views.html.authentication_result.render(new AuthenticationCheckRequestData(json), request, messagesApi.preferred(request)));
		}));
    }
	
	
	/*************************************
	 * 
	 * Usefull helpers
	 * 
	 *************************************/
	
	private Result handleFormResponse(WSResponse response, Throwable error, Http.Request request, String formKey) {
		return genericHandler(response, error, request, formKey, () -> displayFormSuccess(request, formKey));
	}
	
	private Result handleJsonFormResponse(WSResponse response, Throwable error, Http.Request request, String formKey) {
		return genericHandler(response, error, request, formKey, () -> {
			JsonNode json = response.asJson();
			  if (json == null) {
			    return badRequest("Expecting Json data");
			  }
			  String serviceTestResult = json.findPath("ServiceTestResult").textValue();
			  String isCustomizationAsked = json.findPath("IsCustomizationAsked").textValue();
			  String customerEmail = json.findPath("CustomerEmail").textValue();
			
			return displayServiceTestFormSuccess(request, ServiceTestRequestData.TestResult.fromString(serviceTestResult), isCustomizationAsked.equals("1")?true:false, Optional.of(customerEmail));	
		});
	}
	
	private Result genericHandler(WSResponse response, Throwable error, Http.Request request, String formKey, Supplier<Result> toDo) {
		if(response != null) {
			if (response.getStatus() < 400) {
				return toDo.get();
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
	
	private Form<QuotationRequestData> fillQuotationRequestWithDefaultData(Optional<String> brandSeoName, Optional<String> typeOfOrder, Http.Request request) {
		QuotationRequestData requestPrefilledIfNeeded = new QuotationRequestData();
		brandSeoName.ifPresent(seoName -> {
			Optional<Brand> brandFound = brandProvider.getBrandBySeoName(seoName);
			brandFound.ifPresent(brand -> requestPrefilledIfNeeded.brand = brand.id.toString());
		});
		if (typeOfOrder.isPresent()) {
			requestPrefilledIfNeeded.orderType = typeOfOrder.get();
		} else {
			guessTypeOfOrderFromRequest(request).ifPresent(orderType -> requestPrefilledIfNeeded.orderType = orderType);
		}
		requestPrefilledIfNeeded.method = QuotationRequestData.METHOD_LOCAL;
		requestPrefilledIfNeeded.privateInfos = formFactory.form().bindFromRequest(request).get("privateInfos");
		return getQuotationRequestForm().fill(requestPrefilledIfNeeded);
	}
	
	private Optional<String> guessTypeOfOrderFromRequest(Http.Request request) {
		return request.queryString(ORDER_TYPE_PARAMETER_NAME);
	}
	
	private Optional<Brand> getBrand(Optional<String> brandSeoName) {
		return brandSeoName.map(seoName -> brandProvider.getBrandBySeoName(seoName)).orElse(Optional.empty());
	}

	private Form<QuotationRequestData> getQuotationRequestForm() {
		return formFactory.form(QuotationRequestData.class).withDirectFieldAccess(true);
	}
	
	private WSRequest wsWithSecret(String url) {
		return WebserviceHelper.wsWithSecret(ws, url, config);
	}
    
    private Messages getMessagesForInEnglishPages(Http.Request request) {
    	return messagesApi.preferred(Arrays.asList(Lang.forCode("en")));
    }
}

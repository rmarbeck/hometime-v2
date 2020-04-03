package controllers;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
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
import models.QuotationRequestData;

/**
 * Controller for forms processing
 * @author Utilisateur
 *
 */
public class FormProcessingController extends Controller implements WSBodyReadables, WSBodyWritables {
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
	
	public Result displayFormContent(Http.Request request, String content) {
		return redirect(controllers.routes.HomeController.index()).flashing("success", "Demande de devis prise en compte avec succès {"+content+"}").flashing("warning", "Demande de devis prise en compte avec succès {"+content+"}").flashing("error", "Demande de devis prise en compte avec succès {"+content+"}");
	}
	
	public Result prepareQuotationRequest(Http.Request request) {
		return ok(views.html.quotation_form.render(fillQuotationRequestWithDefaultData(Optional.empty()), brandProvider.retrieveBrands(), Optional.empty(), request, messagesApi.preferred(request)));
	}
	
	public Result prepareQuotationRequestWithBrand(Http.Request request, String brandSeoName) {
		QuotationRequestData requestWithPrefilledBrand = new QuotationRequestData();
		Optional<Brand> brandFound = brandProvider.getBrandBySeoName(brandSeoName);
		brandFound.ifPresent(brand -> requestWithPrefilledBrand.brand = brand.id.toString());
		requestWithPrefilledBrand.method = QuotationRequestData.METHOD_LOCAL;
		return ok(views.html.quotation_form.render(fillQuotationRequestWithDefaultData(Optional.of(brandSeoName)), brandProvider.retrieveBrands(), brandFound, request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processQuotationRequest(Http.Request request) {
		final Form<QuotationRequestData> boundForm = getQuotationRequestForm().bindFromRequest(request);
		
		if (boundForm.hasErrors()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.quotation_form.render(boundForm, brandProvider.retrieveBrands(), Optional.empty(), request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = ws.url("https://www.hometime.fr/new-order-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
			return responsePromise.thenApply( response -> {
				logger.error("!!!!!!!!!!!!!!!!!!!!! -> "+response.getStatus());
				return displayFormContent(request, Integer.toString(response.getStatus()));
			});
			//return CompletableFuture.supplyAsync(() -> displayFormContent(request, Integer.toString(0)));
		}
	}
	
	private String flattenValues(String key, String[] values, String separator) {
		return Arrays.asList(values).stream().map(value -> key+"="+value).collect(Collectors.joining( "&" ));
	}
	
	private Form<QuotationRequestData> fillQuotationRequestWithDefaultData(Optional<String> brandSeoName) {
		QuotationRequestData requestWithPrefilledBrand = new QuotationRequestData();
		brandSeoName.ifPresent(seoName -> {
			Optional<Brand> brandFound = brandProvider.getBrandBySeoName(seoName);
			brandFound.ifPresent(brand -> requestWithPrefilledBrand.brand = brand.id.toString());
		});
		requestWithPrefilledBrand.method = QuotationRequestData.METHOD_LOCAL;
		return getQuotationRequestForm().fill(requestWithPrefilledBrand);
	}

	private Form<QuotationRequestData> getQuotationRequestForm() {
		return formFactory.form(QuotationRequestData.class).withDirectFieldAccess(true);
	}
}

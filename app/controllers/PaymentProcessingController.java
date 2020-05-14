package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
import fr.hometime.utils.ControllerHelper;
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
import models.PaymentFormProxy;
import models.PaymentRequestProxy;
import models.QuotationRequestData;
import models.ServiceTestRequestData;

/**
 * Controller for forms processing
 * @author Utilisateur
 *
 */
public class PaymentProcessingController extends Controller implements WSBodyReadables, WSBodyWritables {
	private MessagesApi messagesApi;
	private FormFactory formFactory;
	private final WSClient ws;
	private final Config config;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
    public PaymentProcessingController(FormFactory formFactory, MessagesApi messagesApi, WSClient ws, BrandProvider brandProvider, PriceProvider priceProvider, Config config) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.ws = ws;
        this.config = config;
    }
	
	/*************************************
	 * 
	 * Forwarding confirmation call from
	 * Payment service provider
	 * 
	 *************************************/
	public CompletionStage<Result> manageBackOfficeAnswer(Http.Request request) {
		CompletionStage<? extends WSResponse> responsePromise = ws.url("https://legacy.hometime.fr/paiement/backoffice").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> ControllerHelper.flattenValues(entry.getKey(), entry.getValue(), "&")).collect( Collectors.joining( "&" )));
		return responsePromise.handle((response, error) -> {
			if(response != null) {
				return ok(response.asByteArray());
			}
			return badRequest(error.getLocalizedMessage());
		});
	}
	
	/*************************************
	 * 
	 * End of payment pages displayed
	 * to customer
	 * 
	 *************************************/
	public Result displaySuccessPage(Http.Request request) {
		return ok(views.html.payment_form.render(true, true, Optional.empty(), Optional.empty(), request, messagesApi.preferred(request)));
	}
	
	public Result displayErrorPage(Http.Request request) {
		return ok(views.html.payment_form.render(true, false, Optional.empty(), Optional.empty(), request, messagesApi.preferred(request)));
	}
		
	/*************************************
	 * 
	 * Payment processing
	 * 
	 *************************************/
	public CompletionStage<Result> preparePayment(Http.Request request, String accessKey) {
		CompletionStage<? extends WSResponse> responsePromise = wsWithSecret("https://legacy.hometime.fr/prepare-payment-from-outside").addQueryParameter("ak", accessKey).get();
		return responsePromise.handle((response, error) -> {
			if(response != null && response.getStatus() < 400) {
				JsonNode json = response.asJson();
				if (json != null)
					return ok(views.html.payment_form.render(false, false, Optional.of(new PaymentRequestProxy(json.findPath("PaymentRequest"))), Optional.of(new PaymentFormProxy(json.findPath("PaymentForm"))), request, messagesApi.preferred(request)));
			}
			return displayErrorPage(request);
		});
		

	}

	private WSRequest wsWithSecret(String url) {
		return WebserviceHelper.wsWithSecret(ws, url, config);
	}
}

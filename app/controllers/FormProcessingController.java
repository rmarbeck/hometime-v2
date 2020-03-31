package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.*;
import play.libs.ws.*;

import models.QuotationRequestData;

/**
 * Controller for forms processing
 * @author Utilisateur
 *
 */
public class FormProcessingController extends Controller implements WSBodyReadables, WSBodyWritables {
	private MessagesApi messagesApi;
	private FormFactory formFactory;
	private final WSClient ws;
	
	private final Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	@Inject
    public FormProcessingController(FormFactory formFactory, MessagesApi messagesApi, WSClient ws) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.ws = ws;
    }
	
	public Result displayFormContent(Http.Request request, String content) {
		logger.error("######"+content+"######");
		return redirect(controllers.routes.HomeController.index());
	}
	
	public Result prepareQuotationRequest(Http.Request request) {
		logger.error("!!!!!!!!!!!!!!!!!!!!!");
		return ok(views.html.quotation_form.render(formFactory.form(QuotationRequestData.class), request, messagesApi.preferred(request)));
	}
	
	public CompletionStage<Result> processQuotationRequest(Http.Request request) {
		final Form<QuotationRequestData> boundForm = formFactory.form(QuotationRequestData.class).withDirectFieldAccess(true).bindFromRequest(request);
		
		logger.error(" iiiiiiiiiiiiiii  => "+boundForm.get().brand);
		logger.error(" iiiiiiiiiiiiiii  => "+boundForm.get().city);
		
		logger.error(" oooooooooooooo  => "+request.body().asFormUrlEncoded());
		logger.error(" oooooooooooooo  => "+request.body().asFormUrlEncoded().entrySet().stream().map(entry -> entry.getKey()+"="+entry.getValue()[0]).collect( Collectors.joining( "&" )));
		
		if (boundForm.hasErrors()) {
			logger.error("######Error#######");
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.quotation_form.render(boundForm, request, messagesApi.preferred(request))));
		} else {
			CompletionStage<? extends WSResponse> responsePromise = ws.url("https://www.hometime.fr/new-order-from-outside").setContentType("application/x-www-form-urlencoded").post(request.body().asFormUrlEncoded().entrySet().stream().map(entry -> entry.getKey()+"="+entry.getValue()[0]).collect( Collectors.joining( "&" )));
			return responsePromise.thenApply( response -> {
				logger.error("!!!!!!!!!!!!!!!!!!!!! -> "+response.getStatus());
				return displayFormContent(request, boundForm.get().brand);
			});
			
		}
	}

}

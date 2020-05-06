import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import com.typesafe.config.Config;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import play.http.DefaultHttpErrorHandler;
import play.i18n.MessagesApi;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.*;
import play.mvc.*;
import play.mvc.Http.*;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {
	private final MessagesApi messagesApi;
	
	@Inject
	  public ErrorHandler(
		  MessagesApi messagesApi,
	      Config config,
	      Environment environment,
	      OptionalSourceMapper sourceMapper,
	      Provider<Router> routes) {
	    super(config, environment, sourceMapper, routes);
	    this.messagesApi = messagesApi;
	  }

	  protected CompletionStage<Result> onProdServerError(
		      RequestHeader request, UsefulException exception) {
		    return CompletableFuture.completedFuture(
		        Results.internalServerError(views.html.error.render("erreur", request.withBody(null),  messagesApi.preferred(request))));
		  }

	  protected CompletionStage<Result> onForbidden(RequestHeader request, String message) {
	    return CompletableFuture.completedFuture(
	        Results.forbidden(views.html.error.render("interdit", request.withBody(null),  messagesApi.preferred(request))));
	  }

	@Override
	protected CompletionStage<Result> onNotFound(RequestHeader request, String message) {
		return CompletableFuture.completedFuture(Results.notFound(views.html.error.render("404", request.withBody(null),  messagesApi.preferred(request))));
	}

}

package controllers;

import javax.inject.Inject;

import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
	private final MessagesApi messagesApi;
	private final views.html.content contentTemplate;
	
    @Inject
    public HomeController(MessagesApi messagesApi, views.html.content contentTemplate) {
        this.messagesApi = messagesApi;
        this.contentTemplate = contentTemplate;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.index.render(messages));
    }

    public Result quotationOptions(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.quotation_options.render(messages));
    }
    
    public Result content(Http.Request request, String key) {
    	Messages messages = messagesApi.preferred(request);
        return ok(contentTemplate.render(key, messages));
    }
    
    public Result content2(Http.Request request, String page) {
    	Messages messages = messagesApi.preferred(request);
        return ok(contentTemplate.render(page, messages));
    }

    public Result offers(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.offers.render(messages));
    }
}

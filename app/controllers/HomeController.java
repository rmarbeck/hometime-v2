package controllers;

import java.util.Optional;

import javax.inject.Inject;

import com.typesafe.config.Config;

import fr.hometime.utils.BrandProvider;
import fr.hometime.utils.ConfigWrapper;
import fr.hometime.utils.FeedbackProvider;
import fr.hometime.utils.NewsProvider;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.*;
import play.twirl.api.Html;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
	private final MessagesApi messagesApi;
	private final views.html.content contentTemplate;
	private final views.html.faq faq;
	private FeedbackProvider feedbackProvider;
	private NewsProvider newsProvider;
	private BrandProvider brandProvider;
	private ConfigWrapper configWrapper;
	
	public static NewsProvider injectedNewsProvider;
	
	public static ConfigWrapper injectedConfigWrapper;
	
    @Inject
    public HomeController(MessagesApi messagesApi, views.html.content contentTemplate, views.html.faq faq, FeedbackProvider feedbackProvider, NewsProvider newsProvider, BrandProvider brandProvider, ConfigWrapper configWrapper) {
        this.messagesApi = messagesApi;
        this.contentTemplate = contentTemplate;
        this.faq = faq;
        this.feedbackProvider = feedbackProvider;
        this.newsProvider = newsProvider;
        injectedNewsProvider = this.newsProvider;
        this.brandProvider = brandProvider;
        this.configWrapper = configWrapper;
        injectedConfigWrapper = configWrapper;
        
    }
    
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.index.render(feedbackProvider.retrieveRandomSubsetOfEmphasizedFeedbacks(9), brandProvider.retrieveSupportedBrandsOrderedByInternalName(), request, messages));
    }
    
    public Result test(Http.Request request) {
        return ok(views.html.test.render());
    }
    
    public Result visit(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.visit_us.render(request, messages));
    }
    
    public Result news(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.news.render(newsProvider.retrieveNews(), request, messages));
    }
    
    public Result prices(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.prices.render(request, messages));
    }

    public Result feedbacks(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.feedbacks.render(feedbackProvider.retrieveFeedbacksThatShouldBeDisplayedOrderedByDateAsc(), request, messages));
    }

    public Result quotationOptions(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.quotation_options.render(request, messages));
    }
    
    public Result chooseQuotation(Http.Request request, String brandSeoName) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.choose_quotation.render(brandProvider.retrieveSupportedBrandsOrderedByInternalName(), brandProvider.getBrandBySeoName(brandSeoName), request, messages));
    }
    
    public Result content(Http.Request request, String key) {
    	Messages messages = messagesApi.preferred(request);
        return ok(contentTemplate.render(key, request,  messages));
    }
    
    public Result content2(Http.Request request, String page) {
    	Messages messages = messagesApi.preferred(request);
        return ok(contentTemplate.render(page, request,  messages));
    }
    
    public Result faq(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(faq.render(request,  messages));
    }

    public Result offers(Http.Request request) {
    	Messages messages = messagesApi.preferred(request);
        return ok(views.html.offers.render(brandProvider.retrieveBrandsOrderedByName(), request, messages));
    }
}

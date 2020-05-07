package controllers;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import com.typesafe.config.Config;

import fr.hometime.utils.BrandProvider;
import fr.hometime.utils.ConfigWrapper;
import fr.hometime.utils.FeedbackProvider;
import fr.hometime.utils.NewsProvider;
import play.i18n.Lang;
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
    	return  getIndex(request, false);
    }
    
    public Result indexAlternate(Http.Request request) {
        return  getIndex(request, true);
    }
    
    private Result getIndex(Http.Request request, Boolean alternateVersion) {
        return ok(views.html.index.render(feedbackProvider.retrieveRandomSubsetOfEmphasizedFeedbacks(9), brandProvider.retrieveSupportedBrandsOrderedByInternalName(), alternateVersion, request, getDefaultMessages(request)));
    }
    
    public Result test(Http.Request request) {
        return ok(views.html.test.render());
    }
    
    public Result visit(Http.Request request) {
        return ok(views.html.visit_us.render(request, getDefaultMessages(request)));
    }
    
    public Result news(Http.Request request) {
        return ok(views.html.news.render(newsProvider.retrieveNews(), request, getDefaultMessages(request)));
    }
    
    public Result prices(Http.Request request) {
        return ok(views.html.prices.render(request, getDefaultMessages(request)));
    }

    public Result feedbacks(Http.Request request) {
        return ok(views.html.feedbacks.render(feedbackProvider.retrieveFeedbacksThatShouldBeDisplayedOrderedByDateAsc(), request, getDefaultMessages(request)));
    }

    public Result quotationOptions(Http.Request request) {
        return ok(views.html.quotation_options.render(request, getDefaultMessages(request)));
    }
    
    public Result chooseQuotation(Http.Request request, String brandSeoName) {
        return ok(views.html.choose_quotation.render(brandProvider.retrieveSupportedBrandsOrderedByInternalName(), brandProvider.getBrandBySeoName(brandSeoName), request, getDefaultMessages(request)));
    }
    
    public Result content(Http.Request request, String key) {
        return ok(contentTemplate.render(key, request, getDefaultMessages(request)));
    }
    
    public Result content2(Http.Request request, String page) {
        return ok(contentTemplate.render(page, request, getDefaultMessages(request)));
    }
    
    public Result error(Http.Request request) {
        return ok(views.html.error.render("404", request, getDefaultMessages(request)));
    }
    
    public Result faq(Http.Request request) {
        return ok(faq.render(request, getDefaultMessages(request)));
    }

    public Result offers(Http.Request request) {
        return ok(views.html.offers.render(brandProvider.retrieveBrandsOrderedByName(), request, getDefaultMessages(request)));
    }
    
    public Result sitemap(Http.Request request) {
        return ok(views.xml.sitemap.render(brandProvider.retrieveBrandsOrderedByName(), request, getDefaultMessages(request)));
    }
    
    public Result siteplan(Http.Request request) {
    	return ok(views.html.siteplan.render(brandProvider.retrieveBrandsOrderedByName(), request, getDefaultMessages(request)));
    }

    public Result visit_en(Http.Request request) {
        return ok(views.html.visit_us.render(request, getMessagesForInEnglishPages(request)));
    }
    
    private Messages getDefaultMessages(Http.Request request) {
    	return messagesApi.preferred(request);
    }
    
    private Messages getMessagesForInEnglishPages(Http.Request request) {
    	return messagesApi.preferred(Arrays.asList(Lang.forCode("en")));
    }
}

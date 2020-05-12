package controllers;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class RedirectController extends Controller {
	
    @Inject
    public RedirectController() {
        
    }
    
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result redirect(Http.Request request, String file, String startingUri) {
    	return  redirect(startingUri+file+formattedQueryString(request).orElse(""));
    }
    
    private Optional<String> formattedQueryString(Http.Request request) {
    	Optional<String> queryString = Optional.empty();
    	if (!request.queryString().keySet().isEmpty())
    		queryString = Optional.of("?"+request.queryString().keySet().stream().map(key -> key+"="+request.queryString(key).orElse("")).collect(Collectors.joining("&")));
    	return queryString;
    }
}

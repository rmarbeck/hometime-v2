package controllers;

import org.junit.Test;

import fr.hometime.utils.MockStaticNewsProvider;
import fr.hometime.utils.NewsProvider;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import static play.inject.Bindings.bind;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

public class HomeControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
        .overrides(bind(NewsProvider.class).to(MockStaticNewsProvider.class))
        .build();
    }

    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }
    
    @Test
    public void testNews() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/actualites/news");

        Result result = route(app, request);
        assertEquals(OK, result.status());
        assertEquals(true, Helpers.contentAsString(result).contains("Mocked contenu Video"));
    }

}

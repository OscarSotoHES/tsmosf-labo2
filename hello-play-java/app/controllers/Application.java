package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.cache.Cache;

public class Application extends Controller {
    
    @Cached(key = "homePage")
    public static Result index() {
        return ok(views.html.index.render("Hello Play Framework"));
    }
    
}

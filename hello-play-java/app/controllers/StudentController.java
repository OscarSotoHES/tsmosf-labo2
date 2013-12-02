package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class StudentController extends Controller {

    
    public static Result index() {
        return ok(views.html.index.render("Hello Play Framework"));
    }

}

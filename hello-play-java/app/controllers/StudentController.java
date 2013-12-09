package controllers;

import models.Student;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class StudentController extends AbstractRecordController<Student> {

    private static Class<?> thisClass=StudentController.class;
    //private static List<Student> items=new ArrayList<Student>();
    
//    static {
//    	Student o=null;
//    	for(int i=0; i<10; i++){
//
//    		create();
//    	}
//    	//play.db.jpa.JPA
//    }

    protected StudentController() {
		super(Student.class);
	}
    static StudentController _ME=null;
    public static StudentController me(){
    	if(_ME!=null)
    		return _ME;
    	_ME=new StudentController();
    	return _ME;
    }
    
    public static Result indexHtml() {
        return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".index"));
    }
    public static Result createHtml() {
    	return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".create"));
    }
    public static Result readHtml(String id) {
    	return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".read("+id+")"));
    }
    public static Result updateHtml(String id) {
    	return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".update("+id+")"));
    }
    public static Result deleteHtml(String id) {
    	return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".delete("+id+")"));
    }
    
    public static Result index() {
    	StudentController me = me();
    	return play.mvc.Results.ok(Json.toJson(me.list()));
    }
    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
    	Student model = null;
    	if(model==null){
	    	JsonNode json=request().body().asJson();
	    	model = Json.fromJson(json, Student.class);
    	}
    	StudentController me = me();
    	return play.mvc.Results.ok(Json.toJson(me.createIt(model)));
    }
    public static Result read(Long id) {

    	StudentController me = me();
    	Student dr = me.get(id);
    	return play.mvc.Results.ok(Json.toJson(dr));
    }
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {
    	Student model = null;
    	if(model==null){
	    	JsonNode json=request().body().asJson();
	    	model = Json.fromJson(json, Student.class);
    	}
    	StudentController me = me();
    	Student dr = me.updateIt(model);
    	return play.mvc.Results.ok(Json.toJson(dr));
    }
    public static Result delete(Long id) {
    	me().deleteIt(id);
    	return index();
    }

}

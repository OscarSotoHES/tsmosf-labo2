package controllers;

import java.util.List;

import models.Student;
import models.StudentLesson;
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
    	List<StudentLesson> l = model.getLessons();
    	StudentController me = me();
    	
    	model=me.createIt(model);
		if(l!=null && l.isEmpty()==false){
			updateRelation(l, model);
    		model=me.updateIt(model);
		}
		model=me.refresh(model);
    	return play.mvc.Results.created(Json.toJson(model));
    }
    public static Result read(Long id) {

    	StudentController me = me();
    	Student dr = me.get(id);
    	if(dr==null)
    		play.mvc.Results.badRequest("Item not found :"+id);
    	
    	return play.mvc.Results.ok(Json.toJson(dr));
    }
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {
    	Student model = null;
    	if(model==null){
	    	JsonNode json=request().body().asJson();
	    	model = Json.fromJson(json, Student.class);
    	}
    	model.setId(id);
    	List<StudentLesson> l = model.getLessons();
		if(l!=null)
			updateRelation(l, model);
    	StudentController me = me();
    	model= me.updateIt(model);
    	
    	model=me.refresh(model);
    	return play.mvc.Results.ok(Json.toJson(model));
    }
    public static Result delete(Long id) {
    	try{
    		me().deleteIt(id);
    		return play.mvc.Results.ok("Deleted "+id);
    	}catch(Exception ex){
    		return play.mvc.Results.internalServerError(Json.toJson(ex));
    	}
    }

}

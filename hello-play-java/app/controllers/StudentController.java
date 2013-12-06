package controllers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import models.Student;
import models.StudentLesson;

public class StudentController extends AbstractRecordController<Student> {

    private static Class<?> thisClass=StudentController.class;
    private static List<Student> items=new ArrayList<>();
    static {
    	Student o=null;
    	for(int i=0; i<10; i++){
    		create();
    		//items.add(o=new Student(i+1l, "Item "+i));
//    		for(int j=0; j<10; j++){
//    			o.add();
//    		}
    	}
    }
    public static Result index() {
        //return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".index"));
    	return play.mvc.Results.ok(Json.toJson(items));
    }
    public static Result create() {
    	long id = nexId(items);
    	items.add(new Student(id, "Item "+id));
    	return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".create"));
    }
    public static Result read(Long id) {
    	//return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".read("+id+")"));
    	Student dr = find(id, items);
    	
    	return play.mvc.Results.ok(Json.toJson(dr));
    }
    public static Result update(Long id) {
    	//return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".update("+id+")"));
    	Student dr = find(id, items);
    	return play.mvc.Results.ok(Json.toJson(dr));
    }
    public static Result delete(Long id) {
    	remove(id, items);
    	//return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".delete("+id+")"));
    	return index();
    }

}

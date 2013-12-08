package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import models.Lesson;
import models.Student;
import play.mvc.Controller;
import play.mvc.Result;

public class LessonController extends AbstractRecordController<Lesson> {


    protected LessonController() {
		super(Lesson.class);
	}
	private static Class<?> thisClass=LessonController.class;
    
    private static List<Lesson> items=new ArrayList<Lesson>();
    static {
    	Lesson o=null;
    	for(int i=0; i<10; i++){
    		items.add(o=new Lesson(i+1l, "Item "+i));
//    		for(int j=0; j<10; j++){
//    			o.add();
//    		}
    	}
    }
    static LessonController _ME=null;
    public static LessonController me(){
    	if(_ME!=null)
    		return _ME;
    	_ME=new LessonController();
    	return _ME;
    }
    
    public static Result index() {
        return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".index"));
    }
    public static Result create() {
    	return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".create"));
    }
    public static Result read(String id) {
    	return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".read("+id+")"));
    }
    public static Result update(String id) {
    	return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".update("+id+")"));
    }
    public static Result delete(String id) {
    	return ok(views.html.index.render("Hello Play Framework from "+thisClass.getName()+".delete("+id+")"));
    }
    

}

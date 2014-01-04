package controllers;

import java.util.List;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import play.*;
import play.mvc.*;
import models.*;
import views.html.*;

public class LessonController extends Controller {

	public static Result getAll() {
		List<Lesson> lessons = Lesson.find.all();
		for(Lesson lesson : lessons)
		{
			for(Student student : lesson.students)
			{
				student.name = Student.find.byId(student.id).name;
			}
		}
		return ok(Json.toJson(lessons));
	}
        
	public static Result get(long id) {
		Lesson lesson = Lesson.find.byId(id);
		for(Student student : lesson.students)
                {
                	student.name = Student.find.byId(student.id).name;
                }
                return ok(Json.toJson(lesson));
	}

	public static Result create() {
		JsonNode json = request().body().asJson();
                Lesson lesson  = Json.fromJson(json, Lesson.class);
		lesson.save();
		return ok(Json.toJson(lesson));
	}

	public static Result update(long id) {
		JsonNode json = request().body().asJson();
		Lesson lesson = Json.fromJson(json, Lesson.class);
		lesson.id = id;
		boolean end = false;
		int i = 0;
		while(!end)
		{ 
			try
			{
				lesson.update();
				end = true;
			}
			catch(Exception ex)
			{
				try {
   					 Thread.sleep(100);
				} catch (InterruptedException e) {
    					e.printStackTrace();
				}

				if(i > 100)
					throw ex;
				else
					i++;
			}
		}
		return ok(Json.toJson(lesson));
	}

	public static Result delete(long id) {
		Lesson.find.ref(id).delete();
		return ok();
	}
}

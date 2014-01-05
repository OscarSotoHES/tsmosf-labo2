package controllers;

import java.util.List;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import play.*;
import play.mvc.*;
import models.*;
import views.html.*;

public class StudentController extends Controller {
	public static Result getAll() {
		List<Student> students = Student.find.all();
		return ok(Json.toJson(students));
	}
	
	public static Student getStudent(long id){
		return Student.find.byId(id);
	}
        
	public static Result get(long id) {
                return ok(Json.toJson(getStudent(student)));
	}

	public static Result create() {
		JsonNode json = request().body().asJson();
                Student student  = Json.fromJson(json, Student.class);
		student.save();
		return ok(Json.toJson(student));
	}

	public static Result update(long id) {
		JsonNode json = request().body().asJson();
		Student student = Json.fromJson(json, Student.class);
		student.id = id;
		student.update();
		return ok(Json.toJson(student));
	}

	public static Result delete(long id) {
		Student.find.ref(id).delete();
		return ok();
	}
}

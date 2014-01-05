package controllers;
import java.util.concurrent.Callable;
import java.util.List;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import play.*;
import play.mvc.*;
import models.*;
import views.html.*;
import play.cache.Cache;

public class StudentControllerCache extends Controller {

        public static Result getAll() {
                try
                {
                        List<Student> students;
                        Callable<List<Student>> call = new Callable<List<Student>>(){
                                @Override
                                public List<Student> call() throws Exception {
                                        return Student.find.all();
                                }
                        };
                        students = Cache.getOrElse("students", call, 10000);
                        if(students == null)
                                students = call.call();

                        return ok(Json.toJson(students));
                } catch (Exception ex) {
                        System.out.println("Exception in list(): " + ex);
                        return null;
                }
        }
        
        public static Student getStudent(final long id) {
                  try {
                        Student s = Cache.getOrElse("student" + id, new Callable<Student>() {
                                @Override
                                public Student call() throws Exception {
                                        return Student.find.byId(id);
                                }
                        }, 10000);
                        return s;
                } catch (Exception ex) {
                        System.out.println("Exception in get(" + id + ")" + ex);
                        return null;
                } 
        }
        
        public static Result get(long id) {
              return ok(Json.toJson(getStudent(id)));
        }
        
        private static void addToCache(Student student) {
                Cache.remove("students");
                Cache.set("student" + student.id, student);
        }

        public static Result create() {
                JsonNode json = request().body().asJson();
                Student student  = Json.fromJson(json, Student.class);
                student.save();
                addToCache(student);
                return ok(Json.toJson(student));
        }

        public static Result update(long id) {
                JsonNode json = request().body().asJson();
                Student student = Json.fromJson(json, Student.class);
                student.id = id;
                boolean end = false;
                int i = 0;
                while(!end)
                { 
                        try
                        {
                                student.update();
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
                addToCache(student);
                return ok(Json.toJson(student));
        }

        public static Result delete(long id) {
                Cache.remove("student" + id);
                Student.find.ref(id).delete();
                return ok();
        }
}

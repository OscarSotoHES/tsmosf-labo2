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

public class LessonControllerCache extends Controller {

        public static Result getAll() {
                try
                {
                        List<Lesson> lessons;
                        Callable<List<Lesson>> call = new Callable<List<Lesson>>(){
                                @Override
                                public List<Lesson> call() throws Exception {
                                        List<Lesson> l = Lesson.find.all();
                                        for(Lesson lesson : l)
                                        {
                                                initStudentNames(lesson);
                                        }
                                        return l;                        
                                }
                        };
                        lessons = Cache.getOrElse("lessons", call, 10000);
                        if(lessons == null)
                                lessons = call.call();

                        return ok(Json.toJson(lessons));
                } catch (Exception ex) {
                        System.out.println("Exception in list(): " + ex);
                        return null;
                }
        }
        
        private static Lesson initStudentNames(Lesson lesson) {
             for(Student student : lesson.students)
             {
                student.name = StudentControllerCache.get(student.id).name;
             }
             return lesson;
        }
        
        public static Result get(final long id) {
                 try {
                        Lesson l = Cache.getOrElse("lesson" + id, new Callable<Lesson>() {
                                @Override
                                public Lesson call() throws Exception {
                                        Lesson lesson = Lesson.find.byId(id);
                                        return initStudentNames(lesson);
                                }
                        }, 10000);
                        return ok(Json.toJson(l));
                } catch (Exception ex) {
                        System.out.println("Exception in get(" + id + ")" + ex);
                        return null;
                }
        }
        
        private static void addToCache(Lesson lesson) {
                Cache.remove("lessons");
                Cache.set("lesson" + lesson.id, lesson);
        }

        public static Result create() {
                JsonNode json = request().body().asJson();
                Lesson lesson  = Json.fromJson(json, Lesson.class);
                lesson.save();
                addToCache(lesson);
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
                addToCache(lesson);
                return ok(Json.toJson(lesson));
        }

        public static Result delete(long id) {
                Cache.remove("lesson" + id);
                Lesson.find.ref(id).delete();
                return ok();
        }
}

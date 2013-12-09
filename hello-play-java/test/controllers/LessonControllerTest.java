package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import models.Lesson;

import org.junit.Test;

import play.mvc.Content;
import play.test.FakeApplication;

public class LessonControllerTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
    @Test
    public void createEntityManagerFactory(){
    	LessonController me = LessonController.me();
    	assertThat(me).isNotNull();
    	EntityManagerFactory emf = me.createEntityManagerFactory();
    	assertThat(emf).isNotNull();
    }
    @Test
    public void createEntityManager(){
    	LessonController me = LessonController.me();
    	assertThat(me).isNotNull();
    	EntityManager emf = me.createEntityManager();
    	assertThat(emf).isNotNull();
    }
    
    @Test
    public void simpleCreate(){
    	FakeApplication app = fakeApplication();
        running(app, new Runnable() {
            public void run() {
            	LessonController me = LessonController.me();
            	assertThat(me).isNotNull();
            	Lesson dr=new Lesson("Simple lesson "+ Calendar.getInstance().getTime());
            	assertThat(dr).isNotNull();
            	assertThat(dr.getId()).isNull();
            	assertThat(dr.getName()).isNotNull();
            	me.createIt(dr);
            	assertThat(dr.getId()).isNotNull();
            	assertThat(dr.getName()).isNotNull();
            }
        });
    }

    @Test
    public void indexTemplateShouldContainTheStringThatIsPassedToIt() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Content html = views.html.index.render("Your new application is ready.");
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Your new application is ready.");
            }
        });
    }

}

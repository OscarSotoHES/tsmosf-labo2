package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import org.junit.Test;

import play.mvc.Content;

public class LessonControllerTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
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

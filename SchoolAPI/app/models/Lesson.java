package models;

import java.util.*;
import javax.persistence.*;
import models.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity 
public class Lesson extends Model {

  @Id
  public Long id;
  
  @Constraints.Required
  public String name;
 
  @ManyToMany(cascade=CascadeType.REMOVE)
  public Set<Student> students = new HashSet();
 
  public static Finder<Long,Lesson> find = new Finder<Long,Lesson>(
    Long.class, Lesson.class
  ); 
}

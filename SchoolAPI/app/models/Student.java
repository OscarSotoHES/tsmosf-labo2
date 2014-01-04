package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity 
public class Student extends Model {

  @Id
  public Long id;
  
  @Constraints.Required
  public String name;
 
  
  public static Finder<Long,Student> find = new Finder<Long,Student>(
    Long.class, Student.class
  ); 
}

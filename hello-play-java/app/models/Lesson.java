package models;

//import java.util.*;
//import javax.persistence.*;
//
//import play.db.ebean.*;
//import play.data.format.*;
//import play.data.validation.*;
//
//@Entity 

public class Lesson implements IDataRecord{

	private Long id;
	private String name;
	public Lesson() {
	}
	public Lesson(String name) {
		this(null, name);
	}

	
	public Lesson(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}

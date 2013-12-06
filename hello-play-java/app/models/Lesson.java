package models;

import java.util.ArrayList;
import java.util.List;

//import java.util.*;
//import javax.persistence.*;
//
//import play.db.ebean.*;
//import play.data.format.*;
//import play.data.validation.*;
//
//@Entity 

public class Lesson implements IDataRecord{

	private List<StudentLesson> lessons=new ArrayList<>();
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
	
	public StudentLesson add(Student argv){
		return addStudent(argv.getId());
		
	}
	public StudentLesson addStudent(long argv){
		StudentLesson o = findStudent(argv);
		if(o!=null)
			return o;
		o=new StudentLesson(this.getId(), argv);
		lessons.add(o);
		return o;
	}
	public StudentLesson findStudent(long argv){
		int i = indexOfStudent(argv);
		if(i<0)
			return null;
		return lessons.get(i);
	}
	public int indexOfStudent(long argv){
		if(lessons==null ||lessons.isEmpty())
			return -1;
		//Long v=Long.valueOf(argv);
		for(int i=0; i<lessons.size(); i++)
			if(argv==lessons.get(i).getId())
				return i;
		return -1;
	}

}

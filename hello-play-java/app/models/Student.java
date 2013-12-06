package models;

import java.util.ArrayList;
import java.util.List;

public class Student implements IDataRecord {

	private List<StudentLesson> lessons=new ArrayList<>();
	private Long id;
	private String name;
	
	public Student() {
		// TODO Auto-generated constructor stub
	}
	public Student(String name) {
		this(null, name);
	}

	
	public Student(Long id, String name) {
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
	
	public StudentLesson add(Lesson argv){
		return addLesson(argv.getId());
		
	}
	public StudentLesson addLesson(long argv){
		StudentLesson o = findLesson(argv);
		if(o!=null)
			return o;
		o=new StudentLesson(this.getId(), argv);
		lessons.add(o);
		return o;
	}
	public StudentLesson findLesson(long argv){
		int i = indexOfLesson(argv);
		if(i<0)
			return null;
		return lessons.get(i);
	}
	public int indexOfLesson(long argv){
		if(lessons==null ||lessons.isEmpty())
			return -1;
		//Long v=Long.valueOf(argv);
		for(int i=0; i<lessons.size(); i++)
			if(argv==lessons.get(i).getId())
				return i;
		return -1;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;

//import java.util.*;
//import javax.persistence.*;
//
//import play.db.ebean.*;
//import play.data.format.*;
//import play.data.validation.*;
//
@Entity 
public class Lesson implements IDataRecord{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="lesson_id", referencedColumnName="id")
	private List<StudentLesson> lessons=new ArrayList<StudentLesson>();
	
	@Column
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
	
	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("Lesson [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (lessons != null) {
			builder.append("lessons=");
			builder.append(lessons.subList(0, Math.min(lessons.size(), maxLen)));
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
		}
		builder.append("]");
		return builder.toString();
	}
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		for(int i=0; i<10; i++){
			Lesson o = new Lesson("Lesson "+i);
			System.out.println("Persist:"+o);
			em.persist(o);
			System.out.println("Persisted:"+o);
		}
		if(tx!=null && tx.isActive())
			em.flush();
		for(Lesson o:em.createQuery(" Select t from Lesson as t" , Lesson.class).getResultList())
			System.out.println(o);
	}

}

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
import javax.persistence.OneToMany;
import javax.persistence.Persistence;

@Entity
public class Student implements IDataRecord {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="student_id", referencedColumnName="id")
	private List<StudentLesson> lessons=new ArrayList<StudentLesson>();

	@Column
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
	

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("Student [");
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
			Student o = new Student("Student "+i);
			System.out.println("Persist:"+o);
			em.persist(o);
			System.out.println("Persisted:"+o);
		}
		if(tx!=null && tx.isActive())
			em.flush();
		for(Student o:em.createQuery(" Select t from Student as t" , Student.class).getResultList())
			System.out.println(o);

	}

}
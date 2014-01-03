package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
@Access(AccessType.FIELD)
public class Lesson implements IDataRecord {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name = "lesson_id", referencedColumnName = "id")
	private List<LessonStudent> students = new ArrayList<LessonStudent>();

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

	public List<LessonStudent> getStudents() {
		return students;
	}

	public void setStudents(List<LessonStudent> students) {
		this.students = students;
	}

	public LessonStudent add(Student argv) {
		return addStudent(argv.getId());

	}

	public LessonStudent addStudent(long argv) {
		LessonStudent o = findStudent(argv);
		if (o != null)
			return o;
		o = new LessonStudent(this.getId(), argv);
		students.add(o);
		return o;
	}

	public LessonStudent findStudent(long argv) {
		if (students == null || students.isEmpty())
			return null;
		for (LessonStudent dr : students) {
			if (dr == null || dr.getId() == null)
				continue;
			if (argv == dr.getId())
				return dr;
		}

		return null;
	}

	// public int indexOfStudent(long argv){
	// if(students==null ||students.isEmpty())
	// return -1;
	// //Long v=Long.valueOf(argv);
	// for(int i=0; i<students.size(); i++){
	// LessonStudent dr = students.get(i);
	// if(dr==null || dr.getId()==null)
	// continue;
	// if(argv==dr.getId())
	// return i;
	// }
	//
	// return -1;
	// }
	//
	@Override
	public String toString() {
		//final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("Lesson [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (students != null) {
			builder.append("lessons=");
			// List<LessonStudent> l = students.subList(0,
			// Math.min(students.size(), maxLen));
			// for(LessonStudent o:l)
			// builder.append(o).append(", ");
			builder.append(students.size());
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
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("defaultPersistenceUnit");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		for (int i = 0; i < 10; i++) {
			Lesson o = new Lesson("Lesson " + i);
			System.out.println("Persist:" + o);
			o.addStudent(1);
			o.addStudent(2);
			o.addStudent(3);
			em.persist(o);
			System.out.println("Persisted:" + o);
		}
		if (tx != null && tx.isActive())
			em.flush();
		for (Lesson o : em.createQuery(" Select t from Lesson as t",
				Lesson.class).getResultList())
			System.out.println(o);
	}

}

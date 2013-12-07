package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;

@Entity
public class StudentLesson implements IDataRecord {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="student_id")
	private Long studentId;
	@Column(name="lesson_id")
	private Long lessonId;
	
	public StudentLesson() {
		this(null, null);
	}
	public StudentLesson(Long studentId, Long lessonId) {
		this(null, studentId, lessonId);
	}
	public StudentLesson(Long id, Long studentId, Long lessonId) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.lessonId = lessonId;
	}
	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getLessonId() {
		return lessonId;
	}
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StudentLesson [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (studentId != null) {
			builder.append("studentId=");
			builder.append(studentId);
			builder.append(", ");
		}
		if (lessonId != null) {
			builder.append("lessonId=");
			builder.append(lessonId);
		}
		
		builder.append("]");
		return builder.toString();
	}
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		for(long i=0; i<10; i++){
			Student drs=new Student("Student " +i);
			em.persist(drs);
			Lesson drl=new Lesson("Lesson " +i);
			em.persist(drl);
			em.flush();
			tx.begin();
			if(i<2)
				continue;
			StudentLesson o = new StudentLesson(i+1, i+1);
			System.out.println("Persist:"+o);
			em.persist(o);
			System.out.println("Persisted:"+o);
		}
		if(tx!=null && tx.isActive())
			em.flush();
		for(StudentLesson o:em.createQuery(" Select t from StudentLesson as t" , StudentLesson.class).getResultList())
			System.out.println(o);

	}
	
}

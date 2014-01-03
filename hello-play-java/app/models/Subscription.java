package models;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Persistence;
import javax.persistence.Table;

@Access(AccessType.FIELD)
@MappedSuperclass
public abstract class Subscription implements IDataRecord {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="student_id")
	private Long studentId;
	@Column(name="lesson_id")
	private Long lessonId;
	
	public Subscription() {
		this(null, null);
	}
	public Subscription(Long studentId, Long lessonId) {
		this(null, studentId, lessonId);
	}
	public Subscription(Long id, Long studentId, Long lessonId) {
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
	
}

package models;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name="Subscription")
@Entity@Access(AccessType.FIELD)
public class LessonStudent extends Subscription {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LessonStudent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LessonStudent(Long id, Long studentId, Long lessonId) {
		super(id, studentId, lessonId);
		// TODO Auto-generated constructor stub
	}

	public LessonStudent(Long studentId, Long lessonId) {
		super(studentId, lessonId);
		// TODO Auto-generated constructor stub
	}

}

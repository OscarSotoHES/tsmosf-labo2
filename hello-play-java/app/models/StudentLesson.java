package models;

public class StudentLesson implements IDataRecord {
	private Long id;
	private Long studentId;
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
	
}

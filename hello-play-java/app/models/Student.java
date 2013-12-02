package models;

public class Student implements IDataRecord {

	public Student() {
		// TODO Auto-generated constructor stub
	}
	private Long id;
	private String name;
	
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

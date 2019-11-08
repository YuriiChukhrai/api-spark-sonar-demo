package core.ws;

import java.util.*;
//import java.util.concurrent.atomic.AtomicInteger;

import core.pojo.Student;

public class StudentService {

	private static final Map<String, Student> students = new Hashtable<>();

	public StudentService() {
	}

	public Student findById(String id) {
		return students.get(id);
	}

	// ***************************** CRUD *****************************

	public Student add(Student student) {
		
		return students.put(String.valueOf(student.getId()), student);
	}

	public Student update(String id, Student studentNew) {

		Student student = students.get(id);
		studentNew.setId(Integer.parseInt(id));
		students.put(id, studentNew);

		return student;
	}

	public void delete(String id) {
		students.remove(id);
	}

	public List<Student> findAll() {
		return new ArrayList<>(students.values());
	}

	public int size() {
		return students.size();
	}
}

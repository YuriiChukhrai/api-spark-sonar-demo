package core.qa.demo.ws;

import java.util.*;
//import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import core.qa.demo.pojo.Student;

public class StudentService {

	private static final Map<String, Student> students = new Hashtable<>();

	public StudentService() {
	}

	public Student findById(String id) {
		return students.get(id);
	}

	// ***************************** CRUD *****************************

	public boolean add(Student student) {

		if (student.getId() < 0) {
			student.setId(students.size() + 1);
			return students.put(String.valueOf(student.getId()), student) == null;
		}

		else {
			return students.put(String.valueOf(student.getId()), student) == null;
		}
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

	public void deleteAll() {
		students.clear();
	}
	
	public List<Student> findAll() {
		return new ArrayList<>(students.values());
	}
	
	public Stream<Student> getAll() {
		return students.values().stream();
	}

	public int size() {
		return students.size();
	}
}

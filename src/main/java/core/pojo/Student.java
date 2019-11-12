package core.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "student")
public final class Student extends Person {

	private float gpa;

	public Student(int id, String lname, String fname, int age, int ssn, float gpa) {

		// Better way to use Setters
		this.id = id;
		this.gpa = gpa;
		this.lname = lname;
		this.fname = fname;
		this.age = age;
		this.ssn = ssn;
	}

	public Student() {
	}

	public void setGpa(float gpa) {
		this.gpa = gpa;
	}

	@XmlElement(name = "gpa")
	public float getGpa() {
		return gpa;
	}
}

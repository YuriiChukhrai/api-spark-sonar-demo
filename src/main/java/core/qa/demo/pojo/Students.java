package core.qa.demo.pojo;

import static core.qa.demo.util.BaseUtils.objToStringXml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.GsonBuilder;

import core.qa.demo.util.BaseUtils;

@XmlRootElement(name = "students")
public class Students {

	private List<Student> students;

	@XmlElement(name = "student")
	public List<Student> getStudents() {
		return students;
	}

	public Students setStudents(List<Student> students) {
		this.students = students;
		return this;
	}

	@Override
	public String toString() {
		return BaseUtils.objToStringXml(this, this.getClass(), false);
	}

	public final String toString(boolean isBeauty) {
		
		return objToStringXml(this, this.getClass(), isBeauty);
	}
	
	public final String toStringJson(boolean isBeauty) {
		
		return isBeauty ? new GsonBuilder().setPrettyPrinting().create().toJson(this) : new GsonBuilder().create().toJson(this);
	}
	
//	public String toStringJson() {
//		return new GsonBuilder().create().toJson(this);
//	}
	
	
}

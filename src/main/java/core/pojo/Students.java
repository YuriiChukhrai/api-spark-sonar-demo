package core.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.gson.GsonBuilder;
import core.util.BaseUtils;

@XmlRootElement(name = "students")
@JsonRootName(value = "students")
public class Students {

	private List<Student> students;
	
	@XmlElement(name="student")
	@JsonProperty("student")
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
	
	public String toStringJson() {
		 return new GsonBuilder().create().toJson(this);
	}
}

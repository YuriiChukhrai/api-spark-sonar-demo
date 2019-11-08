package core.pojo;

import static core.util.BaseUtils.objToStringXml;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.GsonBuilder;

public abstract class Person {

	protected String lname;
	protected String fname;
	protected int age;
	protected int ssn;
	protected int id;
	
	@XmlElement(name = "lname")
	public final String getLname() {
		return lname;
	}

	@XmlElement(name = "fname")
	public final String getFname() {
		return fname;
	}

	@XmlElement(name = "age")
	public final int getAge() {
		return age;
	}
	
	@XmlElement(name = "id")
	public final int getId() {
		return id;
	}

	@XmlElement(name = "ssn")
	public final int getSsn() {
		return ssn;
	}

	public final void setLname(String lname) {
		this.lname = lname;
	}

	public final void setFname(String fname) {
		this.fname = fname;
	}

	public final void setAge(int age) {
		this.age = age;
	}

	public final void setSsn(int ssn) {
		this.ssn = ssn;
	}

	public final void setId(int id) {
		this.id = id;
	}

	@Override
	public final String toString() {
		
		return objToStringXml(this, this.getClass(), false);
	}

	public final String toString(boolean isBeauty) {
		
		return objToStringXml(this, this.getClass(), isBeauty);
	}
	
	public final String toStringJson(boolean isBeauty) {
		
		return isBeauty ? new GsonBuilder().setPrettyPrinting().create().toJson(this) : new GsonBuilder().create().toJson(this);
	}
}

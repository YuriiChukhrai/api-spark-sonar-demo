package core.pojo;

import core.pojo.Student;

public enum Options {
	
	HC("GET", "Helth Check", "/rest", ""),
	OPT("OPTIONS", "Allowed methods.", "/opt", ""),
	ADD_S("POST", "Add students. ID must uinique. Body (XML/JSON)", "/student/add", new Student(1, "Chukhrai", "Yurii", 19, 1111111, 4.0f).toStringJson(true)),
	GET_S("GET", "Get student by ID.", "/student/{id}", ""),
	GET_AS("GET", "Get all students.", "/students", ""),
	DEL_S("DELETE", "Delete student by ID.", "/student/{id}", ""),
	UP_S("PUT", "Update student by ID. Body (XML/JSON)", "/student/{id}", new Student(1, "Chukhrai", "Yurii", 19, 1111111, 4.0f).toStringJson(true))
	;
	
	
	String method;
	String description;
	String path;
	String body;
		
	Options(String method, String description, String path, String body) {
		this.method = method;
		this.description = description;
		this.path = path;
		this.body = body;
	}


	public String getMethod() {
		return method;
	}

	public String getDescription() {
		return description;
	}

	public String getPath() {
		return path;
	}
	
	public String getBody() {
		return body;
	}
}

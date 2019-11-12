package core.ws;

import spark.Spark;
import spark.servlet.SparkApplication;

import java.util.List;

import com.google.gson.GsonBuilder;

import core.pojo.Options;
import core.pojo.Student;
import core.pojo.Students;
import core.util.BaseUtils;

public class WebService implements SparkApplication {

	/* Execute server - CRUD - Create - Read - Update - Delete */
	@Override
	public void init() {

		// Main Page, Info
		Spark.get("/", (request, response) -> "Yurii Chukhrai - [Student] WebService V1 - For RestAssure.");
		Spark.options("/opt", (request, response) -> {

			final StringBuilder sb = new StringBuilder("<!doctype html><html lang=\"en\"><body>");

			for (Options option : Options.values()) {
				sb.append(String.format("<p>Method [%s], Descriptions [%s], Path [%s]. <p>Body [%s].</p></p>",
						option.getMethod(), option.getDescription(), option.getPath(), option.getBody()));
			}

			sb.append("</body></html>");

			response.status(200);
			response.type("text/html");

			return sb.toString();
		});

		/* JSON-XML - POST - Add an student */
		Spark.post("student/add", (request, response) -> {

			String msg = "N/A";
			StudentService studentService = new StudentService();
			response.type("text/html");
			boolean isAdded = false;
			Student student;
			
			if (request.contentType().toLowerCase().contains("application/json")) {
				student = new GsonBuilder().create().fromJson(request.body(), Student.class);
				isAdded = studentService.add(student);
			} else if (request.contentType().toLowerCase().contains("application/xml")) {
				student = BaseUtils.stringXmlToObj(request.body(), Student.class);
				isAdded = studentService.add(student);
			} else {
				msg = String.format("Unknown Content-Type [%s].", request.contentType());
				response.status(406);
				return msg;
			}

			if (isAdded) {
				msg = String.format("Done. Size [%d].", studentService.size());
				response.status(201);
			} else {
				msg = "The user already present.";
				response.status(409);
			}

			return msg;
		});

		/* GET - Give me user with this id */
		Spark.get("student/:id", (request, response) -> {

			String msg = String.format("User ID [%s] not found.", request.params(":id"));
			StudentService studentService = new StudentService();
			Student student = studentService.findById(request.params(":id"));

			if (student != null) {

				if (request.headers("Accept").toLowerCase().contains("application/json")) {
					//response.header("Content-Type", "application/json");
					response.type("application/json");
					
					return student.toStringJson(false);
				} else if (request.headers("Accept").toLowerCase().contains("application/xml")) {
					//response.header("Content-Type", "application/xml");
					response.type("application/xml");

					return student.toString(false);
				}
			} else {
				response.status(404);
			}

			return msg;
		});

		/* GET - Give me all users */
		Spark.get("students", (request, response) -> {

			StudentService studentService = new StudentService();
			List<Student> result = studentService.findAll();

			if (result.isEmpty()) {
				response.status(404);
				response.type("text/html");
				//return objectMapper.writeValueAsString);
				return "Users were not found";
			} else {

				if (request.headers("Accept").toLowerCase().contains("application/json")) {
					response.status(200);
					response.type("application/json");

					return new Students().setStudents(result).toStringJson(false);
					
				} else if (request.headers("Accept").toLowerCase().contains("application/xml")) {
					response.status(200);
					response.type("application/xml");

					return new Students().setStudents(result).toString();
				}

				response.status(200);

				return studentService.findAll();
			}
		});

		/* PUT - Update user */
		Spark.put("student/:id", (request, response) -> {

			StudentService studentService = new StudentService();
			String msg = "Empty input";
			response.type("text/html");
			
			if (request != null && request.body() != null && !request.body().isEmpty()) {

				String id = request.params(":id");
				if (request.contentType().toLowerCase().contains("application/json")) {

					Student studentOld = studentService.findById(id);

					// If user exist
					if (studentOld != null) {
						Student studentNew = new GsonBuilder().create().fromJson(request.body(), Student.class);
						studentService.update(id, studentNew);

						return String.format("User with ID [%s] was updated.", id);
					}
					// If user not exist need to create User
					else {
						Student studentNew = new GsonBuilder().create().fromJson(request.body(), Student.class);
						studentService.add(studentNew);

						return String.format("User with ID [%s] was created.", id);
					}
				} else if (request.contentType().toLowerCase().contains("application/xml")) {

					Student studentOld = studentService.findById(id);

					if (studentOld != null) {
						Student studentNew = BaseUtils.stringXmlToObj(request.body(), Student.class);
						studentService.update(id, studentNew);

						return String.format("User with ID [%s] was updated.", id);
					} else {
						Student studentNew = BaseUtils.stringXmlToObj(request.body(), Student.class);
						studentService.add(studentNew);

						return String.format("User with ID [%s] was created.", id);
					}
				} else {
					msg = String.format("Unknown Content-Type [%s].", request.contentType());
					response.status(406);

					return msg;
				}

			} else {
				response.status(406);
			}

			return msg;
		});

		/* DELETE - delete user */
		Spark.delete("student/:id", (request, response) -> {

			StudentService studentService = new StudentService();
			response.type("text/html");
			String id = request.params(":id");

			Student student = studentService.findById(id);
			if (student != null) {
				studentService.delete(id);
				return String.format("User with ID [%s] was deleted. Size [%d].", id, studentService.size());
			} else {
				response.status(404);
				return String.format("User with ID [%s] was not found.", id);
			}
		});
		
		/* DELETE - delete user */
		Spark.delete("students", (request, response) -> {

			StudentService studentService = new StudentService();
			response.type("text/html");
			String id = request.params(":id");

			
			if(studentService.size() > 0) {
				studentService.deleteAll();
				response.status(200);
				return String.format("All users were deleted. Size [%d].", id, studentService.size());
			}
			else {
				response.status(400);
				return "Collection is already empty.";
			}
		});

	}// run
}// WebService

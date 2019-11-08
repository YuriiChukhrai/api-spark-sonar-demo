package core.ws;

import spark.Spark;
import spark.servlet.SparkApplication;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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
			//final ObjectMapper objectMapper = new ObjectMapper();

			for (Options option : Options.values()) {
				sb.append(String.format("<p>Method [%s], Descriptions [%s], Path [%s]. <p>Body [%s].</p></p>", option.getMethod(),
						option.getDescription(), option.getPath(), option.getBody()));
			}

			sb.append("</body></html>");
			
			response.status(201);
			response.type("text/html");

			return sb.toString();
			//return objectMapper.writeValueAsString(sb.toString());

		});

		/* JSON-XML - POST - Add an student */
		Spark.post("student/add", (request, response) -> {

			String msg = "N/A";
			ObjectMapper objectMapper = new ObjectMapper();
			StudentService studentService = new StudentService();

			Student student;
			if (request.contentType().toLowerCase().contains("application/json")) {
				student = new GsonBuilder().create().fromJson(request.body(), Student.class);
				studentService.add(student);
			} else if (request.contentType().toLowerCase().contains("application/xml")) {
				student = BaseUtils.stringXmlToObj(request.body(), Student.class);
				studentService.add(student);
			} else {
				msg = String.format("Unknown Content-Type [%s].", request.contentType());
				response.status(406);
				return objectMapper.writeValueAsString(msg);
			}

			response.status(201);
			msg = String.format("Done. Size [%d].", studentService.size());

			return objectMapper.writeValueAsString(msg);
		});

		/* GET - Give me user with this id */
		Spark.get("student/:id", (request, response) -> {

			String msg = String.format("User ID [%s] not found.", request.params(":id"));
			ObjectMapper objectMapper = new ObjectMapper();
			StudentService studentService = new StudentService();

			Student student = studentService.findById(request.params(":id"));

			if (student != null) {

				if (request.headers("Accept").toLowerCase().contains("application/json")) {
					response.header("Content-Type", "application/json");

					return student.toStringJson(false);
				} else if (request.headers("Accept").toLowerCase().contains("application/xml")) {
					response.header("Content-Type", "application/xml");

					return student.toString(false);
				}
			} else {
				response.status(404);
			}

			return objectMapper.writeValueAsString(msg);
		});

		/* GET - Give me all users */
		Spark.get("students", (request, response) -> {

			ObjectMapper objectMapper = new ObjectMapper();
			StudentService studentService = new StudentService();
			List<Student> result = studentService.findAll();

			if (result.isEmpty()) {
				response.status(404);

				return objectMapper.writeValueAsString("Users were not found");
			} else {

				if (request.headers("Accept").toLowerCase().contains("application/json")) {
					response.status(200);
					response.header("Content-Type", "application/json");

					return objectMapper.writeValueAsString(result);
				} else if (request.headers("Accept").toLowerCase().contains("application/xml")) {
					response.status(200);
					response.header("Content-Type", "application/xml");

					return new Students().setStudents(result).toString();
				}

				response.status(200);

				return objectMapper.writeValueAsString(studentService.findAll());
			}
		});

		/* PUT - Update user */
		Spark.put("student/:id", (request, response) -> {

			ObjectMapper objectMapper = new ObjectMapper();
			StudentService studentService = new StudentService();
			String msg = "Empty input";

			if (request != null && request.body() != null && !request.body().isEmpty()) {

				String id = request.params(":id");
				if (request.contentType().toLowerCase().contains("application/json")) {

					Student studentOld = studentService.findById(id);

					// If user exist
					if (studentOld != null) {
						Student studentNew = new GsonBuilder().create().fromJson(request.body(), Student.class);
						studentService.update(id, studentNew);

						return objectMapper.writeValueAsString(String.format("User with ID [%s] was updated.", id));
					}
					// If user not exist need to create User
					else {
						Student studentNew = new GsonBuilder().create().fromJson(request.body(), Student.class);
						studentService.add(studentNew);

						return objectMapper.writeValueAsString(String.format("User with ID [%s] was created.", id));
					}
				} else if (request.contentType().toLowerCase().contains("application/xml")) {

					Student studentOld = studentService.findById(id);

					if (studentOld != null) {
						Student studentNew = BaseUtils.stringXmlToObj(request.body(), Student.class);
						studentService.update(id, studentNew);

						return objectMapper.writeValueAsString(String.format("User with ID [%s] was updated.", id));
					} else {
						Student studentNew = BaseUtils.stringXmlToObj(request.body(), Student.class);
						studentService.add(studentNew);

						return objectMapper.writeValueAsString(String.format("User with ID [%s] was created.", id));
					}
				} else {
					msg = String.format("Unknown Content-Type [%s].", request.contentType());
					response.status(406);

					return objectMapper.writeValueAsString(msg);
				}

			} else {
				response.status(406);
			}

			return objectMapper.writeValueAsString(msg);
		});

		/* DELETE - delete user */
		Spark.delete("student/:id", (request, response) -> {

			ObjectMapper objectMapper = new ObjectMapper();
			StudentService studentService = new StudentService();
			String id = request.params(":id");

			Student student = studentService.findById(id);
			if (student != null) {
				studentService.delete(id);
				return objectMapper.writeValueAsString(
						String.format("User with ID [%s] was deleted. Size [%d].", id, studentService.size()));
			} else {
				response.status(404);
				return objectMapper.writeValueAsString(String.format("User with ID [%s] was not found.", id));
			}
		});

	}// run
}// WebService

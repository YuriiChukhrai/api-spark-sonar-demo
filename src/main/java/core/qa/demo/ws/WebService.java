package core.qa.demo.ws;

import spark.Spark;
import spark.servlet.SparkApplication;

import java.util.List;

import com.google.gson.GsonBuilder;

import core.qa.demo.enums.ContentType;
import core.qa.demo.enums.HttpHeaders;
import core.qa.demo.enums.Options;
import core.qa.demo.pojo.Student;
import core.qa.demo.pojo.Students;
import core.qa.demo.util.BaseUtils;

public class WebService implements SparkApplication {

	static final String AUTHOR = "2019. McKesson. QA. Yurii Chukhrai";
	static final StringBuilder sb = new StringBuilder();

	/* Execute server - CRUD - Create - Read - Update - Delete */
	@Override
	public void init() {

		/* Main Page, Info, Health check. [ {host}:{port}/v1/ ] */
		Spark.get("/", (request, response) -> String.format("%s - [Student] WebService project.", AUTHOR));

		/* OPTIONS. Info about all available methods */
		Spark.options("/opt", (request, response) -> {

			if (sb.length() <= 0) {
				sb.append("<!doctype html><html lang=\"en\"><body>");

				for (Options option : Options.values()) {
					sb.append(String.format("<p>Method [%s], Descriptions [%s], Path [%s]. <p>Body [%s].</p></p>",
							option.getMethod(), option.getDescription(), option.getPath(), option.getBody()));
				}
				sb.append("</body></html>");
			}

			response.header(HttpHeaders.X_POWERED_BY.getHeader(), AUTHOR);
			response.status(200);
			response.type(ContentType.HTML.getMime());

			return sb.toString();
		});

		/* JSON-XML - POST - Add a Student. [ {host}:{port}/v1/student/add + BODY ] */
		Spark.post("student/add", (request, response) -> {

			String msg = "N/A";
			StudentService studentService = new StudentService();
			response.type(ContentType.HTML.getMime());
			boolean isAdded = false;
			Student student;
			response.header(HttpHeaders.X_POWERED_BY.getHeader(), AUTHOR);

			if (request.contentType().toLowerCase().contains(ContentType.JSON.getMime())) {
				student = new GsonBuilder().create().fromJson(request.body(), Student.class);
				isAdded = studentService.add(student);

			} else if (request.contentType().toLowerCase().contains(ContentType.XML.getMime())) {
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

		/* GET - Give me user with this id. [ {host}:{port}/v1/student/{id} ] */
		Spark.get("student/:id", (request, response) -> {

			String msg = String.format("User ID [%s] not found.", request.params(":id"));
			StudentService studentService = new StudentService();
			Student student = studentService.findById(request.params(":id"));
			response.header(HttpHeaders.X_POWERED_BY.getHeader(), AUTHOR);

			if (student != null) {

				if (request.headers(HttpHeaders.ACCEPT.getHeader()).toLowerCase()
						.contains(ContentType.JSON.getMime())) {

					response.type(ContentType.JSON.getMime());

					return student.toStringJson(false);
				} else if (request.headers(HttpHeaders.ACCEPT.getHeader()).toLowerCase()
						.contains(ContentType.XML.getMime())) {
					response.type(ContentType.XML.getMime());

					return student.toString(false);
				}
			} else {
				response.status(404);
			}

			return msg;
		});

		/* GET - Give me all users. [ {host}:{port}/v1/students ] */
		Spark.get("students", (request, response) -> {

			StudentService studentService = new StudentService();
			List<Student> result = studentService.findAll();
			response.header(HttpHeaders.X_POWERED_BY.getHeader(), AUTHOR);

			if (result.isEmpty()) {
				response.status(404);
				response.type(ContentType.HTML.getMime());
				return "Users were not found";
			} else {

				if (request.headers(HttpHeaders.ACCEPT.getHeader()).toLowerCase()
						.contains(ContentType.JSON.getMime())) {
					response.status(200);
					response.type(ContentType.JSON.getMime());

					return new Students().setStudents(result).toStringJson(false);

				} else if (request.headers(HttpHeaders.ACCEPT.getHeader()).toLowerCase()
						.contains(ContentType.XML.getMime())) {
					response.status(200);
					response.type(ContentType.XML.getMime());

					return new Students().setStudents(result).toString();
				}

				response.status(200);

				return studentService.findAll();
			}
		});

		/* PUT - Update user. [ {host}:{port}/v1/student/{id} ] */
		Spark.put("student/:id", (request, response) -> {

			StudentService studentService = new StudentService();
			String msg = "Empty input";
			response.type(ContentType.HTML.getMime());
			response.header(HttpHeaders.X_POWERED_BY.getHeader(), AUTHOR);

			if (request != null && request.body() != null && !request.body().isEmpty()) {

				String id = request.params(":id");
				if (request.contentType().toLowerCase().contains(ContentType.JSON.getMime())) {

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
				} else if (request.contentType().toLowerCase().contains(ContentType.XML.getMime())) {

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

		/* DELETE - delete user. [ {host}:{port}/v1/student/{id} ] */
		Spark.delete("student/:id", (request, response) -> {

			StudentService studentService = new StudentService();
			response.type(ContentType.HTML.getMime());
			String id = request.params(":id");
			response.header(HttpHeaders.X_POWERED_BY.getHeader(), AUTHOR);
			Student student = studentService.findById(id);

			if (student != null) {
				studentService.delete(id);
				return String.format("User with ID [%s] was deleted. Size [%d].", id, studentService.size());
			} else {
				response.status(404);
				return String.format("User with ID [%s] was not found.", id);
			}
		});

		/* DELETE - delete all users. [ {host}:{port}/v1/students ] */
		Spark.delete("students", (request, response) -> {

			response.header(HttpHeaders.X_POWERED_BY.getHeader(), AUTHOR);
			StudentService studentService = new StudentService();
			response.type(ContentType.HTML.getMime());

			if (studentService.size() > 0) {
				studentService.deleteAll();
				response.status(200);

				return String.format("All users were deleted. Size [%d].", studentService.size());
			} else {
				response.status(400);
				return "Collection is already empty.";
			}
		});

	}// run
}// WebService

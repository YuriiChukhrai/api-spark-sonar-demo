package core.test.unit;

import org.testng.Assert;
import org.testng.annotations.Test;

import core.pojo.Student;
import core.pojo.Students;
import core.test.utils.BaseUtils;
import core.util.TestGroups;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.Links;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RestAssureTestJson extends BaseTest {

	private final String contentTypeJson = ContentType.JSON.getContentTypeStrings()[0];
	private Map<String, String> HEADERS = Collections.unmodifiableMap(new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("Content-Type", contentTypeJson);
			put("Accept", contentTypeJson);
		}
	});

	private Student origUser = new Student(0, "Chukhrai", "Yurii", 19, 1111111, 4.0f);

	@Story("Implement POST")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0001")
	@Issue("123")
	@Links({ @Link(name = "Link1", url = "https://example.org"), @Link(name = "Link2", url = "https://example.org") })
	@Description("Create Student")
	@Feature("CRUD")
	@Feature("POST")
	@Feature(TestGroups.JSON)
	@Test(priority = 1, enabled = true, groups = { TestGroups.API, TestGroups.JSON })
	public void test01() {

		String uri = String.format("http://%s%s", URI, "/student/add");
		BaseUtils.attachText(String.format("1. Request Body. URL [%s]", uri), origUser.toStringJson(true));

			RestAssured.given()
					.config(config)
					.relaxedHTTPSValidation()
					.log()
					.all(true)
					.port(PORT)
					.request()
					.headers(HEADERS)
					.body(origUser.toStringJson(false))
					.when()
					.post(uri)
					.then()
					.contentType(ContentType.HTML)
					.log()
					.all(true)
					.assertThat()
					.statusCode(201)
					.and()
					.assertThat()
					.body(notNullValue())
					.body(containsString("Done"));
	}
	
	@Story("Implement GET")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0002")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Get Student")
	@Feature("CRUD")
	@Feature("GET")
	@Feature(TestGroups.JSON)
	@Test(priority = 2, enabled = true, groups = {TestGroups.API, TestGroups.JSON}, dependsOnMethods = "test01")
	public void test02() {

		String uri = String.format("http://%s%s", URI, "/student/0");

				RestAssured.given()
				.config(config)
				.relaxedHTTPSValidation()
				.log()
				.all(true)
				.port(PORT)
				.request()
				.headers(HEADERS)
				.when()
				.get(uri)
				.then()
				.contentType(ContentType.JSON)
				.log()
				.all(true)
				.assertThat()
				.statusCode(200)
				.and()
				.assertThat()
				.body("age", equalTo(19))
				.body("id", equalTo(0))
				.body("lname", equalTo("Chukhrai"))
				.body("fname", equalTo("Yurii"))
				.body("gpa", equalTo(4.0f))
				.body("ssn", equalTo(1111111));
				
	}
	
	@Story("Implement PUT")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0003")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Update Student")
	@Feature("CRUD")
	@Feature("PUT")
	@Feature(TestGroups.JSON)
	@Test(priority = 3, enabled = true, groups = {TestGroups.API, TestGroups.JSON}, dependsOnMethods = "test01")
	public void test03() {

		
		origUser.setAge(34);
		origUser.setSsn(222222);
		
		String uri = String.format("http://%s%s", URI, "/student/0");
		BaseUtils.attachText(String.format("1. Request Body. URL [%s]", uri), origUser.toStringJson(true));

				RestAssured.given()
				.config(config)
				.relaxedHTTPSValidation()
				.log()
				.all(true)
				.port(PORT)
				.request()
				.headers(HEADERS)
				.body(origUser.toStringJson(false))
				.when()
				.put(uri)
				.then()
				.contentType(ContentType.HTML)
				.log()
				.all(true)
				.assertThat()
				.statusCode(200)
				.and()
				.assertThat()
				.body(notNullValue())
				.body(containsString(String.format("User with ID [%d] was updated.", 0)));
	}
	
	@Story("Implement GET")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0004")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Validate updated student")
	@Feature("CRUD")
	@Feature("GET")
	@Feature(TestGroups.JSON)
	@Test(priority = 4, enabled = true, groups = {TestGroups.API, TestGroups.JSON}, dependsOnMethods = "test03")
	public void test04() {

		String uri = String.format("http://%s%s", URI, "/student/0");

				RestAssured.given()
				.config(config)
				.relaxedHTTPSValidation()
				.log()
				.all(true)
				.port(PORT)
				.request()
				.headers(HEADERS)
				.when()
				.get(uri)
				.then()
				.contentType(ContentType.JSON)
				.log()
				.all(true)
				.assertThat()
				.statusCode(200)
				.and()
				.assertThat()
				.body("age", equalTo(origUser.getAge()))
				.body("id", equalTo(0))
				.body("lname", equalTo(origUser.getLname()))
				.body("fname", equalTo(origUser.getFname()))
				.body("gpa", equalTo(origUser.getGpa()))
				.body("ssn", equalTo(origUser.getSsn()));
	}
	
	@Story("Implement DELETE")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0005")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Delete Student")
	@Feature("CRUD")
	@Feature("DELETE")
	@Feature(TestGroups.JSON)
	@Test(priority = 5, enabled = true, groups = {TestGroups.API, TestGroups.JSON}, dependsOnMethods = "test01")
	public void test05() {

		String uri = String.format("http://%s%s", URI, "/student/0");

				RestAssured.given()
				.config(config)
				.relaxedHTTPSValidation()
				.log()
				.all(true)
				.port(PORT)
				.request()
				.headers(HEADERS)
				.when()
				.delete(uri)
				.then()
				.contentType(ContentType.HTML)
				.log()
				.all(true)
				.assertThat()
				.statusCode(200)
				.and()
				.assertThat()
				.body(notNullValue())
				.body(containsString(String.format("User with ID [%s] was deleted.", 0)));
	}
	
	@Story("Implement DELETE (not exist user)")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0005")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Delete Student not exist")
	@Feature("CRUD")
	@Feature("DELETE")
	@Feature(TestGroups.JSON)
	@Test(priority = 6, enabled = true, groups = {TestGroups.API, TestGroups.JSON}, dependsOnMethods = "test01")
	public void test06() {

		String uri = String.format("http://%s%s", URI, "/student/0");

				RestAssured.given()
				.config(config)
				.relaxedHTTPSValidation()
				.log()
				.all(true)
				.port(PORT)
				.request()
				.headers(HEADERS)
				.when()
				.delete(uri)
				.then()
				.contentType(ContentType.HTML)
				.log()
				.all(true)
				.assertThat()
				.statusCode(404)
				.and()
				.assertThat()
				.body(notNullValue())
				.body(containsString(String.format("User with ID [%s] was not found.", 0)));
	}
	
	@Story("Implement GET (Error)")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0007")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Validate")
	@Feature("CRUD")
	@Feature("GET")
	@Feature(TestGroups.JSON)
	@Test(priority = 7, enabled = true, groups = {TestGroups.API, TestGroups.JSON}, dependsOnMethods = "test01", expectedExceptions = { AssertionError.class })
	public void test07() {

		String uri = String.format("http://%s%s", URI, "/student/0");

				RestAssured.given()
				.config(config)
				.relaxedHTTPSValidation()
				.log()
				.all(true)
				.port(PORT)
				.request()
				.headers(HEADERS)
				.when()
				.get(uri)
				.then()
				.contentType(ContentType.HTML)
				.log()
				.all(true)
				.assertThat()
				.statusCode(405)
				.and()
				.assertThat()
				.body(notNullValue())
				.body(containsString(String.format("User with ID [%s] not found.", 0)));
	}
	
	@Story("Implement GET (Error)")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0008")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Validate")
	@Feature("CRUD")
	@Feature("GET")
	@Feature(TestGroups.JSON)
	@Test(priority = 8, enabled = true, groups = {TestGroups.API, TestGroups.JSON}, expectedExceptions = { AssertionError.class })
	public void test08() {
		
	}
	
	@Story("Implement GET (Error)")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0009")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Validate")
	@Feature("CRUD")
	@Feature("GET")
	@Feature(TestGroups.JSON)
	@Test(priority = 9, enabled = true, groups = {TestGroups.API, TestGroups.JSON})
	public void test09() {

		String uri = String.format("http://%s%s", URI, "/student/99999");

				RestAssured.given()
				.config(config)
				.relaxedHTTPSValidation()
				.log()
				.all(true)
				.port(PORT)
				.request()
				.headers(HEADERS)
				.when()
				.get(uri)
				.then()
				.contentType(ContentType.HTML)
				.log()
				.all(true)
				.assertThat()
				.statusCode(404)
				.and()
				.assertThat()
				.body(notNullValue())
				.body(containsString(String.format("User with ID [%s] not found.", 1)));
	}
	
	
	@Story("Implement OPTIONS.")
	@Epic("Allure examples.")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0010")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Information about all methods.")
	@Feature("CRUD")
	@Feature("OPTIONS")
	@Test(enabled = true, groups = {TestGroups.API})
	public void test10() {

		String uri = String.format("http://%s%s", URI, "/opt");

		String body = RestAssured.given()
				.config(config)
				.relaxedHTTPSValidation()
				.log()
				.all(true)
				.port(PORT)
				.request()
				.headers(HEADERS)
				.when()
				.options(uri)
				.then()
				.contentType(ContentType.HTML)
				.log()
				.all(true)
				.assertThat()
				.statusCode(200)
				.and()
				.assertThat()
				.body(notNullValue())
				.extract().asString();
				
				BaseUtils.attachHtml("OPTIONS", body);
	}
	
	
	@Story("Implement GET")
	@Epic("Allure examples. JSON")
	@Severity(SeverityLevel.CRITICAL)
	@TmsLink("0011")
	@Issue("123")
	@Link(name = "Link1", url="https://example.org")
	@Description("Get All Students.")
	@Feature("CRUD")
	@Feature("GET")
	@Feature(TestGroups.JSON)
	@Test(priority = 10, enabled = true, groups = {TestGroups.API, TestGroups.JSON}, dependsOnMethods = "test01")
	public void test11() {

		String uri = String.format("http://%s%s", URI, "/student/add");
		BaseUtils.attachText(String.format("1. Request Body. URL [%s]", uri), origUser.toStringJson(true));

		RestAssured.given()
					.config(config)
					.relaxedHTTPSValidation()
					.log()
					.all(true)
					.port(PORT)
					.request()
					.headers(HEADERS)
					.body(origUser.toStringJson(false))
					.when()
					.post(uri)
					.then()
					.contentType(ContentType.HTML)
					.log().all(true)
					.assertThat()
					.statusCode(201)
					.and()
					.assertThat()
					.body(notNullValue())
					.body(containsString("Done"));
		
		uri = String.format("http://%s%s", URI, "/students");

		final Response response = RestAssured.given()
				.config(config)
				.relaxedHTTPSValidation()
				.log()
				.all(true)
				.port(PORT)
				.request()
				.headers(HEADERS)
				.when()
				.get(uri);
				
				
		response.then()
				.contentType(ContentType.JSON)
				.log()
				.all(true)
				.assertThat()
				.statusCode(200)
				.and()
				.assertThat()
				.body(notNullValue());
		
		
		Students students = response.as(Students.class);
		
		BaseUtils.attachText("All students.", students.toStringJson(true));
		
		Assert.assertEquals(students.getStudents().size() > 0, true, "Should be not 0 students.");
	}
}

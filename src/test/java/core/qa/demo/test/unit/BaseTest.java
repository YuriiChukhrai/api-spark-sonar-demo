package core.qa.demo.test.unit;

import java.net.ConnectException;

import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.xml.XmlTest;

import core.qa.demo.ws.WebService;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import spark.Spark;

public class BaseTest {

	protected static final RestAssuredConfig config = RestAssured.config().httpClient(HttpClientConfig
			.httpClientConfig().setParam("http.connection.timeout", 60_000).setParam("http.socket.timeout", 60_000));

	protected static int PORT;
	protected static String URI;
	protected final static String contentTypeJson = ContentType.JSON.getContentTypeStrings()[0];
	protected final static String contentTypeXml = ContentType.XML.getContentTypeStrings()[0];

	@BeforeSuite()
	public static final void beforeSuite() {

		String uriString = System.getProperty("uri");
		String portString = System.getProperty("port");

		if ((uriString != null && !uriString.isEmpty()) && (portString != null && !portString.isEmpty())) {
			URI = System.getProperty("uri");
			PORT = Integer.parseInt(System.getProperty("port"));
		} else {
			URI = "localhost";
			PORT = 7779;

			Spark.port(PORT);
			new WebService().init();
		}

		String uri = String.format("http://%s%s", URI, "/students");

		try {
			RestAssured.given()
			.config(config)
			.relaxedHTTPSValidation()
			.log()
			.all(true)
			.port(PORT)
			.request()
			.when()
			.delete(uri)
			.body();

		} catch (Exception e) {
			throw new SkipException(String.format("Web service is down. URI [%s], PORT [%s].", URI, PORT));
		}

	}

	@BeforeTest()
	public final void beforeTest(XmlTest xmlTest) {
		String countString = System.getProperty("testng.thread.count");
		if (countString != null && !countString.isEmpty()) {
			xmlTest.setThreadCount(Integer.parseInt(countString));
		}
	}
}

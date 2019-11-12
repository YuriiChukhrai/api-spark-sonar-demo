package core.test.unit;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.xml.XmlTest;

import core.ws.WebService;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import spark.Spark;

public class BaseTest {

	protected static final RestAssuredConfig config = RestAssured.config()
	        		.httpClient(HttpClientConfig.httpClientConfig()
	                .setParam("http.connection.timeout", 60_000)
	                .setParam("http.socket.timeout", 60_000));
	
	protected static int PORT;
	protected static String URI;
	
	@BeforeSuite()
	public static final void beforeSuite() {
		
		String uriString = System.getProperty("uri");
		String portString = System.getProperty("port");
		
		if((uriString != null && !uriString.isEmpty()) && (portString != null && !portString.isEmpty())) {
			URI = System.getProperty("uri");
			PORT = Integer.parseInt(System.getProperty("port"));
		}
		else {
			URI = "localhost";
			PORT = 7779;
			
			Spark.port(PORT);
			new WebService().init();
		}

			String uri = String.format("http://%s%s", URI, "/students");

			RestAssured.given()
			.config(config)
			.relaxedHTTPSValidation()
			.log()
			.all(true)
			.port(PORT)
			.request()
			.when()
			.delete(uri);
	}
	
	@BeforeTest()
	public final void beforeTest(XmlTest xmlTest) {
		String countString = System.getProperty("testng.thread.count");
		if(countString != null && !countString.isEmpty()) {
			xmlTest.setThreadCount(Integer.parseInt(countString));
		}
	}
}

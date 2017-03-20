package testscripts;

import org.testng.annotations.BeforeClass;

import com.jayway.restassured.RestAssured;

public class FunctionalTest {

	@BeforeClass
	public static void setup() {
/*		String port = System.getProperty("server.port");
		if (port == null) {
			RestAssured.port = Integer.valueOf(8080);
		}
		else{
			RestAssured.port = Integer.valueOf(port);
		}


		String basePath = System.getProperty("server.base");
		if(basePath==null){
			basePath = "/title/ipsum/";
		}
		RestAssured.basePath = basePath;
*/
		String baseHost = System.getProperty("server.host");
		if(baseHost==null){
			baseHost = "http://echo.jsontest.com/title/ipsum/";
		}
		RestAssured.baseURI = baseHost;

	}
}

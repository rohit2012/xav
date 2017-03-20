package testscripts;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;


/*
 * URL = https://github.com/rest-assured/rest-assured/wiki/Usage
 * 		 https://semaphoreci.com/community/tutorials/testing-rest-endpoints-using-rest-assured
 * 		 http://www.swtestacademy.com/api-testing-with-rest-assured/
 * 		 http://goessner.net/articles/JsonPath/
 * 		http://www.programcreek.com/java-api-examples/index.php?api=com.jayway.jsonpath.JsonPath
 * 		https://github.com/jayway/JsonPath/tree/master/json-path/src/test/java/com/jayway/jsonpath
 */

public class TestRestAPI extends FunctionalTest{


		@Test(enabled = false)
	public void getResponse() {
		RestAssured.baseURI = "http://echo.jsontest.com/title/ipsum/";
		Response response = given().when().get("content/blah");
		System.out.println(response.asString());
		System.out.println(response.getStatusCode());
		System.out.println(response.getContentType());
		System.out.println(response.jsonPath());
	} 

	// Read Operations
	@Test
	public void verifyNameofblah()
	{
		given().when().get("/content/blah").then().body(containsString("blah"));
	}

	@Test
	public void verifyNameoftitle()
	{
		given().when().get("/content/blah").then().body("title",equalTo("ipsum")).body("content", equalTo("blah")).statusCode(200);
	}
	
	@Test
    public void aCarGoesIntoTheGarage() {
        Map<String,String> car = new HashMap<>();
        car.put("plateNumber", "xyx1111");
        car.put("brand", "audi");
        car.put("colour", "red");

        given()
       .contentType("application/json")
        .body(car)
        .when().post("/content/blah").then()
        .statusCode(200);
       
		System.out.println(given().when().get("content/blah").asString());
    }
	
/*	@Test
    public void aCarLeaves() {
        given().pathParam("title", "ipsum")
        .when().delete("/content/blah/{title}")
        .then().statusCode(200);

    }
*/	
	@Test(priority=1)
	public void addNewUser(String login, String www) {
        Response response =given().
            contentType("application/json").
        body("").post(www);
//        .
        System.out.println(response.asString());
        
        Map<String, String> cookies = response.getCookies();
        for (String cc : cookies.keySet()) {

            System.out.println(cc.toString() + "   " + cookies.get(cc));

    }



//        when().
//            post("/addNewUser").
//        then().
//            body(containsString("id")).
//            body(containsString("login")).
//            body(containsString("www")).
//            statusCode(200);
  }
	
	@Test
	public void shouldAddNewUser() {
	   // RestService service = new RestService();
	    addNewUser("lukaszroslonek", "10.5.3.245/MercuryServices/rest/mercuryview/getData");
	}
}
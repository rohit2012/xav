package testscripts;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import utlis.RestUtil;

public class TestCox {



	@Test(enabled=false)
	public void testView1Xpath() throws ParseException 
	{
		RestAssured.baseURI = "http://10.5.3.245/MercuryServices/rest/mercuryview/getData";
		Response response =  given().
				contentType("application/json").
				body("{\"lobList\":[{\"name\":\"Residential\",\"id\":\"1200004\"}],\"subLobList\":[{\"id\":\"1300006\",\"name\":\"Residential\",\"lobCode\":null}],\"functionalGroupList\":[{\"name\":\"Account Services\",\"sbFGId\":null,\"id\":\"1400010\"},{\"name\":\"Collections\",\"sbFGId\":null,\"id\":\"1400002\"},{\"name\":\"Inbound Sales\",\"sbFGId\":null,\"id\":\"1400026\"},{\"name\":\"Retention\",\"sbFGId\":null,\"id\":\"1400030\"},{\"name\":\"Technical Support\",\"sbFGId\":null,\"id\":\"1400004\"}],\"subfunctionsList\":[],\"organizationList\":[{\"id\":\"6\",\"name\":\"C3\"},{\"id\":\"1000\",\"name\":\"Cox\"},{\"id\":\"10\",\"name\":\"Plus One\"},{\"id\":\"11\",\"name\":\"Sitel\"},{\"id\":\"12\",\"name\":\"Sutherland\"},{\"id\":\"16\",\"name\":\"Xerox\"}],\"functionsList\":[],\"regionList\":[{\"name\":\"A\",\"id\":\"1\"}],\"coeList\":[],\"lenguage\":\"1100001\",\"timezone\":\"1\",\"length\":0,\"pageNo\":0,\"firstHitFlag\":\"true\",\"viewDetail\":{\"viewName\":\"RTP View\",\"baseViewId\":\"VIEW_1\",\"viewId\":\"VIEW_1\",\"customStatus\":\"false\"}}")
				.post();
		response.then().body(containsString("VIEW_1"));
		response.then().statusCode(200);
		response.then().body("status.statusMessage",equalTo("Info - Success"));
		response.then().body("data[0].viewTemplate.fgname",equalTo("Residential-Account Services"));


		System.out.println(RestUtil.getJsonPath(response).get("data[4].child[0].child[0].child[0].child[0].fgname"));
		System.out.println(RestUtil.getJsonPath(response).get("selectedFilters.subLobList[0]"));
		//System.out.println(RestUtil.getJsonPath(response).get("data[\"(@.length-1)\"].child[0].viewTemplate.fgname"));

		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response.asString());
		// Last Length(index) data
		//String author0 = JsonPath.read(document, "data[-1:]..viewTemplate.fgname").toString();

		//String author0 = JsonPath.read(document, "$..organizationList[*]").toString();
		//String author0 = JsonPath.read(document, "$..organizationList[0].length()").toString();

		// [?(@.id>12)] This is not working
		// String author0 = JsonPath.read(document, "$..organizationList[?(@.id>12)].id").toString();

		//String author0 = JsonPath.read(document, "$..organizationList[?(@.id==12)].id").toString();

		//All books matching regex (ignore case)
		//String author0 = JsonPath.read(document, "$..organizationList.[?(@.name=~ /.*ox/i)]").toString();

		// All selectedFilters with an lobCode
		//String author0 = JsonPath.read(document, "$.selectedFilters..[?(@.lobCode)]").toString();

		// the fgname of everything in the data.
		//String author0 = JsonPath.read(document, "$.data..fgname").toString();


		String author0 = JsonPath.read(document, "$.data..[?(@.fgname== 'Tulsa')].fgname").toString();
		System.out.println(author0);


	}



	@Test(enabled=false)
	public void testView1() throws ParseException
	{
		RestAssured.baseURI = "http://10.5.3.245/MercuryServices/rest/mercuryview/getData";
		Response response =  given().
				contentType("application/json").
				body("{\"lobList\":[{\"name\":\"Residential\",\"id\":\"1200004\"}],\"subLobList\":[{\"id\":\"1300006\",\"name\":\"Residential\",\"lobCode\":null}],\"functionalGroupList\":[{\"name\":\"Account Services\",\"sbFGId\":null,\"id\":\"1400010\"},{\"name\":\"Collections\",\"sbFGId\":null,\"id\":\"1400002\"},{\"name\":\"Inbound Sales\",\"sbFGId\":null,\"id\":\"1400026\"},{\"name\":\"Retention\",\"sbFGId\":null,\"id\":\"1400030\"},{\"name\":\"Technical Support\",\"sbFGId\":null,\"id\":\"1400004\"}],\"subfunctionsList\":[],\"organizationList\":[{\"id\":\"6\",\"name\":\"C3\"},{\"id\":\"1000\",\"name\":\"Cox\"},{\"id\":\"10\",\"name\":\"Plus One\"},{\"id\":\"11\",\"name\":\"Sitel\"},{\"id\":\"12\",\"name\":\"Sutherland\"},{\"id\":\"16\",\"name\":\"Xerox\"}],\"functionsList\":[],\"regionList\":[{\"name\":\"A\",\"id\":\"1\"}],\"coeList\":[],\"lenguage\":\"1100001\",\"timezone\":\"1\",\"length\":0,\"pageNo\":0,\"firstHitFlag\":\"true\",\"viewDetail\":{\"viewName\":\"RTP View\",\"baseViewId\":\"VIEW_1\",\"viewId\":\"VIEW_1\",\"customStatus\":\"false\"}}")
				.post();
		response.then().body(containsString("VIEW_1"));
		response.then().statusCode(200);
		response.then().body("status.statusMessage",equalTo("Info - Success"));
		response.then().body("data[0].viewTemplate.fgname",equalTo("Residential-Account Services"));

		String s = response.asString();
		//System.out.println(s);

		JSONObject arr = (JSONObject)getJSONArrayValue(createJSONObject(s), "data").get(0);
		JSONObject o = (JSONObject)getJSONObjectValue(arr, "viewTemplate");
		System.out.println(getJSONObjectValue(o, "fgname"));
		JSONObject l = (JSONObject)getJSONObjectValue(createJSONObject(s), "headers");
		JSONObject l1 = (JSONObject)getJSONObjectValue(l, "agentsPersonal");
		System.out.println(l1.get("elementDesc"));
		//		JSONParser p = new JSONParser();
		//		Object obj = p.parse(s);
		//		JSONObject jsonObject = (JSONObject) obj;
		//		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		//		jsonObject = (JSONObject) jsonArray.get(1);
		//		jsonObject = (JSONObject) jsonObject.get("viewTemplate");
		//		System.out.println(jsonObject.get("requiredStaff"));



		/*    Iterator<Object> iterator = jsonArray.iterator();

        int i =1;
        while (iterator.hasNext()) {

              //  System.out.println(i +" "+iterator.next());
                i++;

        } 
		 */

	}

	public JSONObject createJSONObject(String s)
	{
		JSONParser p =null;
		JSONObject jsonObject =null;
		try {
			p = new JSONParser();
			Object obj = p.parse(s);
			jsonObject = (JSONObject) obj;
		}catch(Exception e)
		{
		}
		return jsonObject;
	}

	public JSONArray getJSONArrayValue(JSONObject jsonObject, String object)
	{

		return (JSONArray) jsonObject.get(object);
	}

	public Object getJSONObjectValue(JSONObject jsonObject, String object)
	{

		return  jsonObject.get(object);
	}

	private static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		DocumentBuilder builder;  
		try  
		{  
			builder = factory.newDocumentBuilder();  
			Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
			return doc;
		} catch (Exception e) {  
			e.printStackTrace();  
		} 
		return null;
	}



	@Test(enabled=false)
	public void testView3()
	{
		RestAssured.baseURI = "http://10.5.3.245/MercuryServices/rest/mercuryview/getData";
		Response response =  given().
				contentType("application/json").
				body("{\"lobList\":[{\"name\":\"Residential\",\"id\":\"1200004\"}],\"subLobList\":[{\"id\":\"1300006\",\"name\":\"Residential\",\"lobCode\":null}],\"functionalGroupList\":[{\"name\":\"Technical Support\",\"sbFGId\":null,\"id\":\"1400004\"}],\"subfunctionsList\":[],\"organizationList\":[{\"id\":\"6\",\"name\":\"C3\"},{\"id\":\"1000\",\"name\":\"Cox\"},{\"id\":\"10\",\"name\":\"Plus One\"},{\"id\":\"11\",\"name\":\"Sitel\"},{\"id\":\"12\",\"name\":\"Sutherland\"},{\"id\":\"16\",\"name\":\"Xerox\"}],\"functionsList\":[],\"regionList\":[{\"name\":\"A\",\"id\":\"1\"}],\"coeList\":[],\"lenguage\":\"1100001\",\"timezone\":\"1\",\"length\":0,\"pageNo\":0,\"firstHitFlag\":\"true\",\"viewDetail\":{\"viewName\":\"RTP View\",\"baseViewId\":\"VIEW_3\",\"viewId\":\"VIEW_3\",\"customStatus\":\"false\"}}")
				.post();
		response.then().body(containsString("VIEW_3"));
		response.then().statusCode(200);
		response.then().body("tables[0].table1.headers.callsInQueue.name",equalTo("Calls in Queue"));
		response.then().body("tables[0].table2.headers.cumulativeServiceLevel.name",equalTo("CML Service Level"));
		response.then().body("tables[0].table2.headers.cumulativeCallsABNper.name",equalTo("CML % Calls ABN"));
	}

	
	public String readRequest(String jasonname)
	{
		String h= null;
		try {
			Reader reader = new FileReader("C:\\Users\\csingh5\\Desktop\\"+jasonname+".json");
			BufferedReader bufferReader = new BufferedReader(reader);
			int num=0;
			char ch = 0;
			String s=null;
			StringBuffer sb = new StringBuffer();
			while((num=bufferReader.read()) != -1)
			{	
				ch=(char)num;
				s=String.valueOf(ch);
				sb.append(s);
			}
			h= sb.toString();
			bufferReader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return h;
	}
	
	public String getResponse(String req)
	{
		RestAssured.baseURI = "http://10.5.3.245/MercuryServices/rest/mercuryview/getData";
		Response response =  given().
				contentType("application/json").
				body(req)
				.post();
		return response.asString();
	}
	
	@Test(enabled=true)
	public void getAPITest()
	{
		String req = readRequest("request");
		String res = getResponse(req);
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(res);
		String author0 = JsonPath.read(document, "$.data..[?(@.fgname== 'Tulsa')].fgname").toString();
		System.out.println(author0);
	}
}

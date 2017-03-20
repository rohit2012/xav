package api.rest.testscript;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import api.rest.util.HelperMethod;

public class TestView1 extends HelperMethod{

	private String requestFileName = "View1";
	private String url = "mercuryview/getData";
	private String command ="post";
	private Response response;
	
	@BeforeClass
	public void getResponse()
	{
		String req = readRequest(requestFileName);
		response = getResponse(req,url,command);
	}
	
	@Test(enabled = false)
	public void view1Status()
	{
		Assert.assertEquals(getStatus(url,requestFileName,command), 200,"Status of the request is not 200");
	}
	
	@Test(enabled=false)
	public void view1SingleData()
	{
		assertSingleString(url,requestFileName,response, "$..data[1].viewTemplate.forcastedStaff", "N/A");
	}
	
	@Test(enabled=true)
	public void view1ListData()
	{
		assertList(url,requestFileName,response, "$..headers.callsInQueue.id", false,"N/A","ELEMENT_433");
		assertList(url,requestFileName,response, "$..selectedFilters.lobList",true,"name", "Residential");
		//assertList(url,requestFileName,response, "$..data[1].viewTemplate",true,"fgname", "Residential-Collections");
		assertList(url,requestFileName,response, "$..selectedFilters.lobList",true,"name", "Residential");
		/*assertList(url,requestFileName,command, "$..selectedFilters.functionalGroupList",true, "name", "Retention");
		assertList(url,requestFileName,command, "$..data[1].viewTemplate",true,"cmlPerForecastFg", "N/A");
		assertList(url,requestFileName,command, "$..headers.fgname",true,"name", "Functional Group");
		assertList(url,requestFileName,command, "$..data[1].viewTemplate",true,"forcastedStaff", "N/A");
		assertList(url,requestFileName,command, "$..headers..fgname.display", false,"N/A","Y");
		assertList(url,requestFileName,command, "$..headers.callsInQueue.id", false,"N/A","ELEMENT_433");
		assertList(url,requestFileName,command, "$..headers.callsInQueue",true,"id", "ELEMENT_433");
		assertList(url,requestFileName,command, "$..organizationList[0]",true,"name", "C3");
		assertList(url,requestFileName,command, "$..organizationList[0].name", false,"N/A","C3"); */
		
		//softAssert.assertAll();
		
	}
}

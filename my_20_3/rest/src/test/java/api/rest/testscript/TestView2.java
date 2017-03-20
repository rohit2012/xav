package api.rest.testscript;

import java.util.LinkedHashMap;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;

import api.rest.util.HelperMethod;

public class TestView2 extends HelperMethod{

	private String requestFileName = "View2";
	private String url = "mercuryview/getData";
	private String command ="post";
	private Response response;
	
	@BeforeClass
	public void getResponse()
	{
		String req = readRequest(requestFileName);
		response = getResponse(req,url,command);
	}
	
	@Test(enabled=false)
	public void view2testStatus()
	{
		Assert.assertEquals(getStatus(url,requestFileName,command), 200,"Status of the request is not 200");
	}

	@Test(enabled=true)
	public void view2verifyDetails()
	{
		assertList(url,requestFileName,response, "$..functionalGroupList", true, "name", "Account Services");
		
		assertListWithKey(url,requestFileName,response, "$..functionalGroupList[0]", "name", "Account Services");
		
		assertList(url,requestFileName,response, "$..viewDetail", true, "viewId", "VIEW_2");
		
		//assertList(url,requestFileName,command, "$..status.statusMessage", false, "N/A", "Info - Success");
		
		assertListWitIndex(url,requestFileName,response, "$..functionalGroupList", true, "0", "name", "Account Services");
		
		//assertList(url,requestFileName,command, "$..data[0]..child[0]..child[0]..child[0]..child[0].agentsOtherAcd", false, "", "0");
		LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
		hm.put("name", "Account Services");
		hm.put("sbFGId", "null");
		hm.put("id", "1400010");
		Set<String> s = hm.keySet();
		for(String k : s)
		{
			assertList(url,requestFileName,response, "$..functionalGroupList", true, k, hm.get(k));
		}
	}
}

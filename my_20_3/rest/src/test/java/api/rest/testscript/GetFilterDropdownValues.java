package api.rest.testscript;

import java.util.ArrayList;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.jayway.restassured.response.Response;
import api.rest.util.DatabaseConnection;
import api.rest.util.HelperMethod;

public class GetFilterDropdownValues extends HelperMethod{

	private String requestFileName = "getFilterDropdownValues";
	private String url = "filter/getFilterDropdownValues";
	private String command ="post";
	private Response response;

	@BeforeClass
	public void getResponse()
	{
		String req = readRequest(requestFileName);
		response = getResponse(req,url,command);
	}

	@Test(enabled = false)
	public void filterDropDownValueStatus()
	{
		System.out.println(url);
		Assert.assertEquals(getStatus(url,requestFileName,command), 200,"Status of the request is not 200");
	}

	@Test(enabled=true)
	public void view1ListData()
	{
		DatabaseConnection db =  new DatabaseConnection();
		ArrayList<Object> arr1 = null;
		arr1 = db.getDataBaseData("select FUNCTION_GROUP_NAME from FUNCTIONAL_GROUPS",1);
		for(int l=0;l<=arr1.size()-1;l++)
		{
			assertList(url,requestFileName,response, "$..functionalGroupsList", true, "functionalGroupsName", arr1.get(l).toString());
		}	
		
		arr1 = db.getDataBaseData("select FUNCTION_GROUP_ID from FUNCTIONAL_GROUPS",1);
		for(int l=0;l<=arr1.size()-1;l++)
		{
			assertList(url,requestFileName,response, "$..functionalGroupsList", true, "functionalGroupsId", arr1.get(l).toString());
		}
	}

}

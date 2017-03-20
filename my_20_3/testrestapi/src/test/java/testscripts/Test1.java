package testscripts;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import utlis.HelperMethods;
import utlis.RestUtil;

public class Test1 {

	private Response res = null; //Response object
    private JsonPath jp = null; //JsonPath object
    
	@BeforeMethod
    public void setup (){
        //Test Setup
        RestUtil.setBaseURI("http://api.5min.com"); //Setup Base URI
        RestUtil.setBasePath("search"); //Setup Base Path
        RestUtil.setContentType(ContentType.JSON); //Setup Content Type
        RestUtil.createSearchQueryPath("barack obama", "videos.json", "num_of_videos", "4"); //Construct the path
        res = RestUtil.getResponse(); //Get response
        jp = RestUtil.getJsonPath(res); //Get JsonPath
    }
 
    @Test
    public void T01_StatusCodeTest() {
        //Verify the http response status returned. Check Status Code is 200?
        HelperMethods.checkStatusIs200(res);
    }
 
    @Test
    public void T02_SearchTermTest() {
        //Verify the response contained the relevant search term (barack obama)
        Assert.assertEquals(("Search results for \"barack obama\""), jp.get("api-info.title"),"Title is wrong!");
        //assertThat(jp.get("api-info.title"), containsString("barrack obama"));
    }
 
    @Test
    public void T03_verifyOnlyFourVideosReturned() {
        //Verify that only 4 video entries were returned
        Assert.assertEquals(4, HelperMethods.getVideoIdList(jp).size(),"Video Size is not equal to 4");
    }
 
    @Test
    public void T04_duplicateVideoVerification() {
        //Verify that there is no duplicate video
    	Assert.assertTrue(HelperMethods.findDuplicateVideos(HelperMethods.getVideoIdList(jp)),"Duplicate videos exist!");
    }
 
    @Test
    public void T05_printAttributes() {
        //Print video title, pubDate & duration
        printTitlePubDateDuration(jp);
    }
 
    @AfterMethod
    public void afterTest (){
        //Reset Values
        RestUtil.resetBaseURI();
        RestUtil.resetBasePath();
    }
 
    //*******************
    //***Local Methods***
    //*******************
    //Prints Attributes
    @Test(priority=1)
    private void printTitlePubDateDuration (JsonPath jp) {
        for(int i=0; i < HelperMethods.getVideoIdList(jp).size(); i++ ) {
            System.out.println("Title: " + jp.get("items.title[" + i + "]"));
            System.out.println("pubDate: " + jp.get("items.pubDate[" + i + "]"));
            System.out.println("duration: " + jp.get("items.duration[" + i + "]"));
            System.out.print("\n");
        }
    }
}

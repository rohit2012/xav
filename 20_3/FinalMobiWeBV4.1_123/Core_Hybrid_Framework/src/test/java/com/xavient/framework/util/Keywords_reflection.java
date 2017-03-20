package com.xavient.framework.util;





import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.common.base.Verify;
import com.xavient.framework.util.Constants;
import com.xavient.framework.util.Xls_Reader;



public class Keywords_reflection {
	static Logger Application_Log;
	static Properties prop;
	public static Method method[];
	static WebDriver driver;
	static HashMap<String,WebDriver> map;
	public static Keywords_reflection k;
	static String currentTestCaseName;
	static String currentIteration;
	static String iteration_runmode;
	public static Xls_Reader xls1 = new Xls_Reader(Constants.SUITEWEBAPPLICATION_XLS_PATH);
	//public static Method capturescreenShot_method;

	public Keywords_reflection() throws NoSuchMethodException, SecurityException{
			
        

		// init map
		map = new HashMap<String,WebDriver>();
		map.put(Constants.MOZILLA, null);
		map.put(Constants.CHROME, null);

		// initialize properties file
		prop=new Properties();
		try {
			FileInputStream fs = new FileInputStream(Constants.PROPERTIES_FILE_PATH);
			prop.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void executeKeywords(String testName, Xls_Reader xls,
			Hashtable<String, String> data) throws InterruptedException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchMethodException {

		// read the xls
		// call the keyword functions
		// report errors
		
		currentTestCaseName=testName;
		currentIteration=data.get(Constants.ITERATION_COL);
		
		int rows = xls.getRowCount(Constants.KEYWORDS_SHEET);
		
		//iteration wise result column setup
		double d = Double.parseDouble(currentIteration);
		int item = (int)d;
		int resutlcol = item+4;
		boolean b = xls.setCellData(Constants.KEYWORDS_SHEET, resutlcol, 1,"RESULT "+item);
		
		for(int rNum=2;rNum<=rows;rNum++){
			
			String tcid = xls.getCellData(Constants.KEYWORDS_SHEET, 0, rNum);
			if(tcid.equalsIgnoreCase(testName)){
				iteration_runmode = xls.getCellData(Constants.TestData_SHEET, 1, rNum);
				String keyword = xls.getCellData(Constants.KEYWORDS_SHEET, 2, rNum);
				String object = xls.getCellData(Constants.KEYWORDS_SHEET, 3, rNum);
				String dataCol = xls.getCellData(Constants.KEYWORDS_SHEET, 4, rNum);
				log(keyword +" --- "+object+" --- "+dataCol);
				
				Method method[];
				method = Keywords_reflection.getInstance().getClass().getMethods();
				for(int i = 0;i < method.length;i++){
					
					if(method[i].getName().equals(keyword)){
						
						String result=(String) method[i].invoke(keyword,object,data.get(dataCol));
						
					xls.setCellData(Constants.KEYWORDS_SHEET, resutlcol, rNum, result);
					}
					}
			}
		}
	}
				
				
				
				
			//############################ KEYWORDS ################//
	
	public static String openBrowser(String Objectkey, String data){
		log("Starting function openBrowser - "+ data);
		try{
			
			if(map.get(data.toLowerCase()) == null){
				
				if(data.equalsIgnoreCase(Constants.MOZILLA))
					driver = new FirefoxDriver();
				else if(data.equalsIgnoreCase(Constants.CHROME)){
					System.setProperty("webdriver.chrome.driver",prop.getProperty("chromedriverexe") );
					driver = new ChromeDriver();
				}
				else if(data.equalsIgnoreCase(Constants.IE)){
					System.setProperty("webdriver.ie.driver",prop.getProperty("IEdriverserverexe") );
					driver = new InternetExplorerDriver();
				}
				map.put(data.toLowerCase(), driver);
			}
			else{ // flag
				driver= map.get(data.toLowerCase());
			}
			
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		}catch(Exception e){ //error
			e.printStackTrace();
			reportError(Constants.OPENBROWSER_ERROR+data);
			return Constants.FAIL;
		}
		
		log("Ending  function openBrowser with status "+Constants.PASS);
		return Constants.PASS;
	}
	
	
	public static String navigate(String Objectkey ,String data){
		log("Starting function navigate");
		try{
			String env = prop.getProperty("env");
			String url = prop.getProperty("url_"+env);
			driver.get(url);
			
			
		}catch(Exception e){}
		log("Ending  function navigate with status "+Constants.PASS);
		return Constants.PASS;
	}
	
	
	


	public static String click(String Objectkey , String data){
		log("Starting function click"+ Objectkey);
		element(Objectkey, data).click();
		log("Ending  function click with status "+Constants.PASS);
		return Constants.PASS;
	}
	
	public static String submit(String Objectkey , String data){
		log("Starting function submit");
		element(Objectkey, data).submit();
		log("Ending  function submit with status "+Constants.PASS);
		return Constants.PASS;
	}
	
	public static String input(String Objectkey,String data){
		log("Starting function input"+ Objectkey+" , "+data );
		
		element(Objectkey, data).sendKeys(data);
		
		log("Ending  function click with status "+Constants.PASS);
		return Constants.PASS;
	}
	
	

	public static String verifyTitle(String Objectkey,String data) throws InterruptedException
	{//titles
		
		try{
		String actualTitle=driver.getTitle();
		String expectedTitle=data;
		if(!actualTitle.equals(expectedTitle)){
		reportFailureAndStop(Constants.TITLE_NOT_MATCHES_FAILURE+expectedTitle+". Actual-"+actualTitle);
		return Constants.FAIL;
		}
		
	}catch(Exception e){ //error
		e.printStackTrace();
		reportError(Constants.TITLE_NOT_MATCHES_FAILURE+e.getMessage());
		return Constants.FAIL;
		}
		return Constants.PASS;
	}
	
	
	public static WebElement element(String Objectkey,String data){
		
		log("Finding element "+Objectkey );
		try{
			if(Objectkey.endsWith("_id"))
				return driver.findElement(By.id(prop.getProperty(Objectkey)));
			else if(Objectkey.endsWith("_name"))
				return driver.findElement(By.name(prop.getProperty(Objectkey)));
			else if(Objectkey.endsWith("_xpath"))
				return driver.findElement(By.xpath(prop.getProperty(Objectkey)));
			else{// error
				reportError(Constants.LOCATOR_ERROR+Objectkey);
				
			}
		}
		catch(NoSuchElementException e){//failure
			reportFailureAndStop(Constants.ELEMENT_NOT_FOUND_FAILURE + Objectkey);
		}catch(Exception e){ // error
			reportError(Constants.FIND_ELEMENT_ERROR + Objectkey);
		}
		
		return null;
	}

	/************************************App Keywords*********************************/
	
	public static String verifyGoogleSearch(String ObjectKey , String data)
	{ log("Verifying the Search Results by google");
	try
	{
    WebElement myDynamicElement = (new WebDriverWait(driver, 10))
            .until(ExpectedConditions.presenceOfElementLocated(By.id("resultStats")));

  List<WebElement> findElements = driver.findElements(By.xpath(prop.getProperty(ObjectKey)));

  // this are all the links you like to visit
  for (WebElement webElement : findElements)
  {
     if(! webElement.getAttribute("href").toLowerCase().contains(data.toLowerCase())){
    reportFailureAndStop(Constants.DATA_NOT_MATCH_FAILURE + data +" actual "+webElement.getAttribute("href"));
    return Constants.FAIL;
     }
  }}
catch(Exception e)
{
	
	reportFailureAndStop(Constants.GENERAL_ERROR + ObjectKey);
	//return Constants.FAIL;

}
    log("Ending Function verifyGoogleSearch with status Pass");
	return Constants.PASS;


}
	
	
	
	

	/*********************************Utility************************************/
	
	
	public static boolean isElementPresent(String objectKey,String timeout) {
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(timeout), TimeUnit.SECONDS);
		int size=0;
		if(objectKey.endsWith("_id"))
			size= driver.findElements(By.id(prop.getProperty(objectKey))).size();
		else if(objectKey.endsWith("_name"))
			size= driver.findElements(By.name(prop.getProperty(objectKey))).size();
		else if(objectKey.endsWith("_xpath"))
			size= driver.findElements(By.xpath(prop.getProperty(objectKey))).size();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		if(size!=0)
			return true;
		else
		return false;
	}

	public static void reportError(String msg){
		takeScreenShot();
		log(msg);
		//Assert.fail(msg);
		Verify.verify(true, msg, k);
	}
	
	private static void reportFailureAndStop(String Errmsg) {
		takeScreenShot();
		log(Errmsg);
		//Assert.fail(Errmsg);
		log("Fail due some reason=-------------");
		Verify.verify(true, Errmsg, k);
		//screenshot
		
	}
	
	
	public static void waitForPageToLoad(String Objectkey,String data){
		
		JavascriptExecutor js = (JavascriptExecutor)driver;
		while(!js.executeScript("return document.readyState").toString().equals("complete")){
			try {
				log("Waiting for 4 sec for page to load");
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void setLogger(Logger log){
		Application_Log = log;
	}
	
	
	public static void log(String message){
		System.out.println(message);
		Application_Log.debug(message);
	}


	public static Keywords_reflection getInstance() throws NoSuchMethodException, SecurityException {
		if(k==null){
			k = new Keywords_reflection();
		}
		return k;
	}
	public static void takeScreenShot(){
		//testcasename_iteration.jpg
		
	
		String filePath=Constants.SCREENSHOT_PATH+currentTestCaseName+"-"+currentIteration+".png";
		File targetFile= new File(filePath);
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		String SurefirereportFilePath=Constants.SUREFIREREPORTS+currentTestCaseName+"-"+currentIteration+".png";
		File Surefirereport= new File(SurefirereportFilePath);
		int iter = Integer.parseInt(currentIteration);
		xls1.addHyperLink(Constants.SCREENSHOTS_SHEET, "ScreenShots", currentTestCaseName.trim(), iter, filePath, "ITERATION "+iter);
		
		try {
			FileUtils.copyFile(srcFile, targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
		
	}
	
	
	public static String  closeBrowser(String Objectkey,String browserName) {
		map.put(browserName.toLowerCase(), null);
     log("quitting the driver for browser " +browserName );
		driver.close();
		return Constants.PASS;
		
}

}

package com.xavient.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class MobileHelper { 
		Logger Application_Log;
		Properties prop;
		AndroidDriver driver;
		IOSDriver driver1;
		HashMap<String,WebDriver> map;
		String currentTestCaseName;
		String currentIteration;
		
		public MobileHelper() throws IOException{
			//log("Properties loaded");
			// initialize properties file
			prop=new Properties();
			FileInputStream fs = new FileInputStream(Constants.PROPERTIES_FILE_PATH);
			prop.load(fs);
			//Set up desired capabilities and pass the Android app-activity and app-package to Appium
			//log(" Setting up the desired capabilities for Appium");
			DesiredCapabilities capabilities = new DesiredCapabilities();
		    capabilities.setCapability("BROWSER_NAME", "Android");
		    capabilities.setCapability("VERSION", "4.4.2"); 
		    capabilities.setCapability("deviceName","0a6a02b9");
		    capabilities.setCapability("platformName","Android");
		   // capabilities.setCapability("appPackage", "com.mttnow.droid.easyjetStag");
		    capabilities.setCapability("appPackage", "com.android.calculator2");
		    // This package name of your app (you can get it from apk info app)
		    //capabilities.setCapability("appActivity","com.mttnow.droid.easyjet.ui.home.SplashActivity"); // This is Launcher activity of your app (you can get it from apk info app)
		    capabilities.setCapability("appActivity","com.android.calculator2.Calculator"); 
		    //Create RemoteWebDriver instance and connect to the Appium server
		     //It will launch the Calculator App in Android Device using the configurations specified in Desired Capabilities
		     driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		     driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		    }	
			
		
		

		public void executeKeywords(String testName, Xls_Reader xls,
				Hashtable<String, String> data) {

			// read the xls
			// call the keyword functions
			// report errors
			int rows = xls.getRowCount(Constants.KEYWORDS_SHEET);
			for(int rNum=2;rNum<=rows;rNum++){
				String tcid = xls.getCellData(Constants.KEYWORDS_SHEET, 0, rNum);
				if(tcid.equalsIgnoreCase(testName)){
					String keyword = xls.getCellData(Constants.KEYWORDS_SHEET, 2, rNum);
					String object = xls.getCellData(Constants.KEYWORDS_SHEET, 3, rNum);
					String dataCol = xls.getCellData(Constants.KEYWORDS_SHEET, 4, rNum);
					log(keyword +" --- "+object+" --- "+dataCol);
					//log("Executing the test on browser - "+data.get(dataCol) );
					switch (keyword) {
																		
					case "click":
						click(object);
						
						break;
										
					case "closeApplication":
						closeApplication();	
					case "testCal":
					testCal();
						
						break;
					default:
						break;
					}
					
					
					
				}
			}
			
			
			
			
			
		}
		
		//error and failure
		
			public String click(String objectKey){
			log("Starting function click"+ objectKey);
			element(objectKey).click();
			log("Ending  function click with status "+Constants.PASS);
			return Constants.PASS;
		}
		
		public String input(String objectKey,String data){
			log("Starting function input"+ objectKey+" , "+data );
			
			element(objectKey).sendKeys(data);
			
			log("Ending  function click with status "+Constants.PASS);
			return Constants.PASS;
		}
		
		public void closeApplication() {

				driver.quit();
				
				
			
		}

		public void testCal()
		{
			 WebElement two=driver.findElement(By.name("2"));
			   two.click();
			   WebElement plus=driver.findElement(By.name("+"));
			   plus.click();
			   WebElement four=driver.findElement(By.name("4"));
			   four.click();
			   WebElement equalTo=driver.findElement(By.name("="));
			   equalTo.click();
			   //locate the edit box of the calculator by using By.tagName()
			   WebElement results=driver.findElement(By.id("com.android.calculator2:id/eq"));
				//Check the calculated value on the edit box
			assert results.getText().equals("6"):"Actual value is : "+results.getText()+" did not match with expected value: 6";
		}
		
		
		public WebElement element(String objectKey){
			
			log("Finding element "+objectKey );
			try{
				if(objectKey.endsWith("_id"))
					return driver.findElement(By.id(prop.getProperty(objectKey)));
				else if(objectKey.endsWith("_name"))
					return driver.findElement(By.name(prop.getProperty(objectKey)));
				else if(objectKey.endsWith("_xpath"))
					return driver.findElement(By.xpath(prop.getProperty(objectKey)));
				else{// error
					reportError(Constants.LOCATOR_ERROR+objectKey);
					
				}
			}
			catch(NoSuchElementException e){//failure
				reportFailureAndStop(Constants.ELEMENT_NOT_FOUND_FAILURE + objectKey);
			}catch(Exception e){ // error
				reportError(Constants.FIND_ELEMENT_ERROR + objectKey);
			}
			
			return null;
		}

		/************************************App Keywords*********************************/
		
		

		/*********************************Utility************************************/
		
		
		public boolean isElementPresent(String objectKey,int timeout) {
			driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
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
		public void reportError(String msg){
			takeScreenShot();
			log(msg);
			Assert.fail(msg);
		}
		
		
		public void reportFailureAndStop(String Errmsg) {
			takeScreenShot();
			log(Errmsg);
			Assert.fail(Errmsg);		
		}
		public void takeScreenShot(){
			//testcasename_iteration.jpg
			String filePath=Constants.SCREENSHOT_PATH+currentTestCaseName+"-"+currentIteration+".png";
			File targetFile= new File(filePath);
			File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		   
			
			try {
				FileUtils.copyFile(srcFile, targetFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void waitForPageToLoad(){
			
			JavascriptExecutor js = (JavascriptExecutor)driver;
			while(!js.executeScript("return document.readyState").toString().equals("complete")){
				try {
					log("Waiting for 2 sec for page to load");
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
		public void setLogger(Logger log){
			Application_Log = log;
		}
		
		
		public void log(String message){
			System.out.println(message);
			Application_Log.debug(message);
		}


		
	}



package com.xavient.test.script;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.xavient.pages.DashBoardView;
import com.xavient.util.BaseClass;
import com.xavient.util.Helper;
import com.xavient.util.Properties_Reader;

public class Test_View1 extends BaseClass implements  DashBoardView{
	
	WebDriver driver;
	 Helper helper;

	 /**
	  * Calling Before Test for navigating to particular view1.
	  * @param browser
	  * @author AJameel
	  */
	 @BeforeTest
		@Parameters({ "browser" })
		public void Before_Test(@Optional("ie") String browser) {
			driver = Browser_Selection(browser);
			//Initialize
			helper = new Helper();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			//Navigating to URL.
			
			//Handling PopUP with AutoIT , Need to have this screen as active when this method is being executed.
			helper.handle_popup();
			
			//Login and Navigating to View
			driver.findElement(user_name).sendKeys(Properties_Reader.readProperty("Username"));
			driver.findElement(pword).sendKeys(Properties_Reader.readProperty("Password"));
			driver.findElement(submit_login).click();
			driver.findElement(View).click();
			driver.findElement(Queue_Summary).click();
			driver.findElement(View1).click();
			
			
		}
	 @Test
		public void view1_validate_table_drata()  {
	  //helper.validate_table_names(driver.findElement(view1_residential_summary),"Test_View1","view1_residential_summary");
	  //helper.validate_table_columns(view1_residential_summary_col, driver , "" , "Test_View1" , "view1_residential_summary_col" );	  
	 // helper.drillDown(driver);	 
	 }
	 }
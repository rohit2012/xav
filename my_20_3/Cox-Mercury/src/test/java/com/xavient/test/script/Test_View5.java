package com.xavient.test.script;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.xavient.pages.DashBoardView;
import com.xavient.util.BaseClass;
import com.xavient.util.Helper;

public class Test_View5 extends BaseClass implements  DashBoardView{
	

WebDriver driver;
 Helper helper;
 WebDriverWait wait;
 Logger logger = Logger.getLogger(Test_View3.class);
 
	/**
	 * Calling Before Test for navigating to particular view5.
	 * @param browser
	 * @author guneet
	 */
	@BeforeMethod
	@Parameters({ "browser" })
	public void Before_Test(@Optional("Chrome") String browser) {

		driver = Browser_Selection(browser);
		// Initialize
		helper = new Helper();
		wait = new WebDriverWait(driver, 10);

		// Handling PopUP with AutoIT , Need to have this screen as active when this method is being executed.
		helper.handle_popup();

		// Login and Navigating to View
		helper.login(driver);
		helper.waitForBrowserToLoadCompletely(driver);
		driver.findElement(View).click();
		driver.findElement(CumulativePerformance).click();
		driver.findElement(View5).click();
		System.out.println("------Before Test------");
	}

	/**
	 * @author guneet
	 * Method is validating stack chart
	 */
	@Test
	public void view5_stack_chart()  {
	
	helper.waitloader(driver);
	driver.findElement(chartCombineStackToolTip).click();
	
	helper.validate_list_data(view5_AgentCount_sub_label, driver, "Test_View5", "view5_AgentCount_sub_label");
	helper.validate_list_data(view5_Percentage_sub_label, driver, "Test_View5", "view5_Percentage_sub_label");
	helper.validate_list_data(view5_Time_sub_label, driver, "Test_View5", "view5_Time_sub_label");

	helper.validate_table_names((WebElement)driver.findElement(view5_AgentCount_y_axis_label), "Test_View5", "view5_AgentCount_y_axis_label");
	helper.validate_table_names((WebElement)driver.findElement(view5_Percentage_y_axis_label), "Test_View5", "view5_Percentage_y_axis_label");
	helper.validate_table_names((WebElement)driver.findElement(view5_Time_y_axis_label), "Test_View5", "view5_Time_y_axis_label");
	
	helper.validate_list_data_axis(view5_AgentCount_x_axis_label, driver,"Test_View5", "view5_AgentCount_x_axis_label");
	helper.validate_list_data_axis(view5_Percentage_x_axis_label, driver, "Test_View5", "view5_Percentage_x_axis_label");
	helper.validate_list_data_axis(view5_Time_x_axis_label, driver, "Test_View5", "view5_Time_x_axis_label");
	
	}
	
	/**
	 * @author guneet
	 * Method is validating filter name
	 */
	@Test
	public void view5_validate_filter_name()
	{
		logger.info("-----Start test case execution for :view5_validate_filter_name------");
		helper.waitloader(driver);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(filterBtn)));
		helper.clickByJavascript(driver, driver.findElement(filterBtn));
		helper.validate_filter_data(filterTxt, driver, "Test_View5", "view5_FilterName_data");
		logger.info("-----End test case execution for :view5_validate_filter_name------");
	}
	
	/**
	 * @author guneet
	 * Method is validating filter value.
	 */
	@Test
	public void view5_validate_filter_dropdown()
	{
		logger.info("-----Start test case execution for :view5_validate_filter_dropdown------");
		helper.waitloader(driver);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(filterBtn)));
		helper.clickByJavascript(driver, driver.findElement(filterBtn));
		
		// Validate Start Time FilterList
		helper.validate_filter_dropdown_data(startTimeHandllerFilterList, driver, "Test_View5", "view5_FilteIntervalRangeStart_data");
		
		// Validate End Time FilterList
		helper.validate_filter_dropdown_data(endTimeHandllerFilterList, driver, "Test_View5", "view5_FilteIntervalRangeEnd_data");
		
		// Validate Organization FilterList
		helper.validate_filter_dropdown_data(organizationFilterList, driver, "Test_View5", "view5_FilterOrganizationValue_data");
		
		// Validate COE FilterList
		helper.validate_filter_dropdown_data(coeFilterList, driver, "Test_View5", "view5_Filter_coeFilterList_data");		
		
		// Validate LOB FilterList
		helper.validate_filter_dropdown_data(lobFilterList, driver, "Test_View5", "view5_Filter_lobFilterList_data");
		
		// Validate SUB LOB FilterList
		helper.validate_filter_dropdown_data(subLobFilterList, driver, "Test_View5", "view5_Filter_subLobFilterList_data");		
		
		// Validate Functional Groups FilterList
		helper.validate_filter_dropdown_data(functionalGroupsFilterList, driver, "Test_View5", "view5_Filter_functionalGroupsFilterList_data");
		
		// Validate Sub Functional Groups FilterList
		helper.validate_filter_dropdown_data(subFunctionalGroupsFilterList, driver, "Test_View5", "view5_Filter_subFunctionalGroupsFilterList_data");
		
		// Validate  Function FilterList
		helper.validate_filter_dropdown_data(functionHandllerFilterList, driver, "Test_View5", "view5_Filter_functionFilterList_data");
	
		// Validate Language FilterList
		helper.validate_filter_dropdown_data(languageFilterList, driver, "Test_View5", "view5_Filter_languageFilterList_data");

		// Validate Time Zone FilterList
		helper.validate_filter_dropdown_data(timeZoneFilterList, driver, "Test_View5", "view5_Filter_timeZoneFilterList_data");
		logger.info("-----End test case execution for :view5_validate_filter_dropdown------");
	}
	/**
	 * Validating Table and Column data.
	 * @author guneet
	 */
		@Test
		public void view5_validate_table_data()  {
			logger.info("-----Start test case execution for :view3_validate_table_data------");
			helper.waitloader(driver);
			helper.waitForBrowserToLoadCompletely(driver);
			driver.findElement(pauseToolTip).click();
			helper.validate_list_data_using_attribute(view5_ForecastStaffing_table , driver , "Test_View5" , "view5_ForecastStaffing_table" );	
			logger.info("-----End of test case execution for :view3_validate_table_data------");
		}
	
	/**
	 * Closing Browser After Test.
	 */
	@AfterMethod
	public void After_Test() {
		driver.close();
		System.out.println("------End Test------");
	}


}
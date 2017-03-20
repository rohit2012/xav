package com.xavient.test.script;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.xavient.pages.DashBoardView;
import com.xavient.util.BaseClass;
import com.xavient.util.Helper;
import com.xavient.util.Properties_Reader;

public class Test_View3 extends BaseClass implements  DashBoardView {

WebDriver driver;
 Helper helper;
 WebDriverWait wait;
 Logger logger = Logger.getLogger(Test_View3.class);
 
/**
 * Calling Before Test for navigating to particular view3.
 * @param browser
 * @author NMakkar
 */
	@BeforeMethod
	@Parameters({ "browser"})
	public void Before_Test(@Optional("Chrome") String browser) {
		
		driver = Browser_Selection(browser);
		//Initialize
		helper = new Helper();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver , 5);
		
		//Handling PopUP with AutoIT , Need to have this screen as active when this method is being executed.
		helper.handle_popup();
		
		//Login and Navigating to View
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(user_name)));
		driver.findElement(user_name).sendKeys(Properties_Reader.readProperty("Username"));
		driver.findElement(pword).sendKeys(Properties_Reader.readProperty("Password"));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(submit_login)));
		driver.findElement(submit_login).click();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(View)));
		driver.findElement(View).click();
		driver.findElement(Queue_And_Agent_Overview).click();
		driver.findElement(View3).click();
		
		
		System.out.println("------Before Test------");
	}
	/**
	 * Closing Browser After Test.
	 */
		@AfterMethod
		public void After_Test() {
			driver.close();
			System.out.println("------End Test------");
		}
/**
 * Validating Table and Column data.
 * @author NMakkar
 */
	@Test(enabled=true, priority =1)
	public void view3_validate_table_data()  {
		logger.info("-----Start test case execution for :view3_validate_table_data------");
		helper.validate_table_names( driver.findElement(view3_curr_data) ,  "Test_View3" , "view3_curr_data" );	
		helper.validate_table_columns( view3_curr_data_table , driver , "" , "Test_View3" , "view3_curr_data_table" );	

		helper.validate_table_names( driver.findElement(view3_today_data) ,  "Test_View3" , "view3_today_data" );
		helper.validate_table_columns( view3_today_data_table , driver , "" , "Test_View3" , "view3_today_data_table" );

		helper.validate_table_names( driver.findElement(view3_curr_agent_stats_tbl) ,  "Test_View3" , "view3_curr_agent_stats_tbl" );
		helper.validate_table_columns( view3_curr_agent_stats_col , driver , "" , "Test_View3" , "view3_curr_agent_stats_col" );
	
		helper.validate_table_names( driver.findElement(view3_agent_details) ,  "Test_View3" , "view3_agent_details" );	
		helper.validate_table_columns( view3_Agent_table_data_start , driver , view3_Agent_table_data_end , "Test_View3" , "view3_Agent_table_data" );
		logger.info("-----End of test case execution for :view3_validate_table_data------");
	}
	
	/**
	 * @author NMakkar
	 * Method is validating static data set of line graph 
	 */
	@Test(enabled=true,priority=4)
	public void view3_validate_line_graph_data()  {
	logger.info("-----Start test case execution for :view3_validate_line_graph_data------");
		//Navigating to line chart page. 	
	wait.until(ExpectedConditions.visibilityOf(driver.findElement(lineChartToolTip)));
	driver.findElement(lineChartToolTip).click();
	wait.until(ExpectedConditions.visibilityOf(driver.findElement(view3_line_graph_title)));
	
	//Validating all static data.
	helper.validate_table_names(driver.findElement(view3_line_graph_title), "Test_View3", "view3_line_graph_title");
	helper.validate_table_names(driver.findElement(view3_line_graph_y_axis), "Test_View3", "view3_line_graph_y_axis");
	helper.validate_table_names(driver.findElement(view3_line_graph_header), "Test_View3", "view3_line_graph_header");
	helper.validate_list_data_axis(view3_graph_x_axis, driver , "Test_View3" , "view3_line_graph_x_axis" );	
	logger.info("-----End of test case execution for :view3_validate_line_graph_data------");
	}
	
	/**
	 * Method is validating static data set of bar graph 
	 * @author NMakkar
	 */
	@Test(enabled=true,priority =5)
	public void view3_validate_bar_graph_data()  {
		logger.info("-----Start test case execution for :view3_validate_bar_graph_data------");
		//Navigating to Bar chart page. 
	wait.until(ExpectedConditions.visibilityOf(driver.findElement(barGraphToolTip)));
	driver.findElement(barGraphToolTip).click();
	wait.until(ExpectedConditions.visibilityOf(driver.findElement(view3_bar_graph_title)));
	
	//Validating all static data.
	helper.validate_table_names(driver.findElement(view3_bar_graph_title), "Test_View3", "view3_bar_graph_title");
	helper.validate_table_names(driver.findElement(view3_bar_graph_y_axis), "Test_View3", "view3_bar_graph_y_axis");
	helper.validate_table_names(driver.findElement(view3_bar_graph_header), "Test_View3", "view3_bar_graph_header");
	helper.validate_list_data_axis(view3_graph_x_axis, driver , "Test_View3" , "view3_bar_graph_x_axis" );
	logger.info("-----End of test case execution for :view3_validate_bar_graph_data------");
	}
	
	/**
	 * @author guneet
	 * Method is validating table sorting
	 */
	@Test(enabled=true,priority =2)
	public void view3_table_sorting() {
		logger.info("-----Start test case execution for :view3_table_sorting------");
		LinkedList<String>  tableData = new LinkedList<String>();
		int tableComtentCount = helper.getWebelentSize(view3_agent_details_data, driver);
		logger.info("Agent details table ");
		int totalTableCount = helper.getWebelentSize(By.xpath(view3_Agent_table_data_start), driver);
		if (tableComtentCount > 0) {
			for (int i = 1; i <= totalTableCount; i++) {
				for (int j = 0; j < 2; j++) {
					driver.findElement(By.xpath(view3_Agent_table_data_start + "[" + i + "]" + view3_Agent_table_data_sort_arrow)).click();
					for (int i1 = 1; i1 <= tableComtentCount; i1++) {
						List<WebElement> el = driver.findElements(By.xpath("//div[@class='ui-grid-canvas']/div[" + i1 + "]//div[@role='gridcell']/div"));
						tableData.add(el.get(i - 1).getText().toString());
					}
					if(j==0)
						helper.validateListIsSorted(tableData,"asc");
					else if(j==1)
						helper.validateListIsSorted(tableData,"desc");
					tableData.clear();
				}
			}
		}
		logger.info("-----End of test case execution for :view3_table_sorting------");
	}
	
	/**
	 * @author guneet
	 * Method is validating table pagination
	 */
	@Test(enabled=true, priority = 3)
	public void  view3_table_pagination() {
		logger.info("-----Start test case execution for :view3_table_pagination------");
		Select select = new Select(driver.findElement(pagerPageDrop));

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(pagerGridCount)));
		String count=driver.findElement(pagerGridCount).getText();
		Object[] pagerGridCountList = count.split(" "); 
		
		//Validating text 
		Assert.assertEquals("of", pagerGridCountList[1]);
		Assert.assertEquals("items", pagerGridCountList[3]);		
		Assert.assertEquals(" items per page", driver.findElement(pagerPageDropText).getText());
		
		count = count.split(" ")[2];
		if(Integer.parseInt(count)>5){

			//Checking pagination first and previous is disabled
			Assert.assertEquals("true", driver.findElement(pagerFirst).getAttribute("disabled"),"Pagination first must be disabled");
			Assert.assertEquals("true", driver.findElement(pagerPrevious).getAttribute("disabled"),"Pagination Previous must be disabled");
			
			//clicking pagination last button
			driver.findElement(pagerLast).click();
			
			Assert.assertEquals("true", driver.findElement(pagerLast).getAttribute("disabled"),"Pagination Last must be disabled");
			Assert.assertEquals("true", driver.findElement(pagerNext).getAttribute("disabled"),"Pagination Next must be disabled");
			
			driver.findElement(pagerPrevious).click();

			//Validating pagination dropdown value
			Select select2 = new Select(driver.findElement(pagerPageDrop));
			List<WebElement> dCount = select2.getOptions();
			int dropList[] = {5,10,25,50,100};
			for (int i = 0; i < dCount.size()-1; i++) {
				select.selectByIndex(i);
				Assert.assertEquals(dropList[i], Integer.parseInt(select2.getFirstSelectedOption().getText()));
			}
		}
		logger.info("-----End of test case execution for :view3_table_pagination------");
	}
	
	/**
	 * Validate pie chart data 
	 * 
	 */
	
	@Test(enabled=true,priority = 6)
	public void view3_validate_piechart_data()  {
		
	wait.until(ExpectedConditions.visibilityOf(driver.findElement(lineChartToolTip)));
	driver.findElement(lineChartToolTip).click();
	logger.info("Click on the Line Chart Tool tip");
	wait.until(ExpectedConditions.visibilityOf(driver.findElement(view3_line_graph_title)));
	helper.validate_table_names(driver.findElement(view3_piechart_graph_header), "Test_View3", "view3_pie_chart_header");
	wait.until(ExpectedConditions.visibilityOf(driver.findElement(view3_piechart_labels)));
	helper.validate_list_data(view3_piechart_labels, driver, "Test_View3", "view3_piechart_labels");	
	helper.validate_table_names(driver.findElement(view3_piechart_Total_Agents_label), "Test_View3", "view3_piechart_Total_Agents_label");
	helper.validate_table_names(driver.findElement(view3_piechart_Agents_Staffed_label), "Test_View3", "view3_piechart_Agents_Staffed_label");
	}
	
	/**
	 * Validating data of COEs table
	 * @author NMakkar
	 */
	@Test
	public void view3_COEs_table_data_validation()
	{	
		//Key on which we have to validate other values.
		String key = "Calls In Queue";
		//Text on which we will fetch other tables columns
		String check_text = "Agents";
		
		//Initialize Elements  of tables and their columns data.
		//Table 3 - Data .
		List<WebElement> data_of_table3  = driver.findElements(view3_COEs_table_data_val);
		//Table 3 - Columns
		List<WebElement> col_of_table3 = driver.findElements(By.xpath(view3_curr_agent_stats_col));
		//Table 1 - Data
		List<WebElement> updated_col_table1 = driver.findElements(view3_Current_table_data_val);
		//Table 1 - Colums
		List<WebElement> col_of_table1 = driver.findElements(By.xpath(view3_curr_data_table));
		
		//Adding Column data from another table.
		data_of_table3 = helper.modify_cols_data_of_table(col_of_table1, data_of_table3, updated_col_table1 , check_text  ,  true);
		
		//Validating N/A and integer for columns
		helper.data_validate_Down(driver, key , col_of_table3, data_of_table3  );
		}

}


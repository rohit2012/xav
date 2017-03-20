package com.xavient.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.muthu.Verify;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.xavient.pages.DashBoardView;

public class Helper implements DashBoardView{
	/**
	 * Handling Browser pop-up with AutoIT.
	 * @author NMakkar
	 */


	static Logger logger = Logger.getLogger(Helper.class);

	public void handle_popup()
	{
		try {
			//Initialize.
			Robot robot = new Robot();
			//Pressing Escape for Browser Alert.
			robot.keyPress(KeyEvent.VK_ESCAPE);
			robot.keyRelease(KeyEvent.VK_ESCAPE);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Alert pop is closed");
	}
	/**
	 * Method is fetching and comparing data from XLS and UI for multiple values. 
	 * @author NMakkar
	 * @param element_start
	 * @param driver
	 * @param element_end
	 * @param class_name
	 * @param table_element
	 */
	public void validate_table_columns(  String element_start  , WebDriver driver ,  String element_end , String class_name , String table_element) 
	{
		//Initialize.
		List<String> xls_col_names  = ExcelCache.getExpectedListData(class_name , table_element );
		ArrayList<String> ui_col_names = new ArrayList<String>();	
		int rows = driver.findElements(By.xpath(element_start+element_end)).size();

		//Comparing No of columns
		logger.info("-----Comparing the table columns size----- ");
		logger.info("Actual table columns size from UI: "+rows);
		logger.info("Expected table columns size from Excel: "+xls_col_names.size());
		Verify.verifyEquals(rows, xls_col_names.size() , "No of columns are not same");
		WebDriverWait wait = new WebDriverWait(driver, 10);

		//Iterating for fetching elements from UI.
		for (int i = 1 ;  i <= rows ; i ++)
		{
			String locator = element_start +"[" + i + "]" + element_end;


			WebElement row_names = driver.findElement(By.xpath(locator));
			wait.until(ExpectedConditions.visibilityOf(row_names));
			ui_col_names.add(row_names.getText().trim());


		}
		//Comparing List (Column Names)
		logger.info("Actual table columns names from UI: "+ui_col_names);
		logger.info("Expected table columns names from Excel: "+xls_col_names);
		Verify.verifyEquals(xls_col_names.containsAll(ui_col_names) , true , "All Column Names does not match");		
	}
	/**
	 * Method is fetching and comparing data from XLS and UI for single values.
	 * @author NMakkar
	 * @param element
	 * @param class_name
	 * @param table_element
	 */

	public void validate_table_names(WebElement element  , String class_name , String table_element)
	{
		//XLS data.
		String ui_col_names = element.getText();
		logger.info("Actual table columns from UI: "+ui_col_names);
		//UI Element data.
		String xls_table_name = ExcelCache.getExpectedData(class_name , table_element);
		logger.info("Expected table columns from excel: "+xls_table_name);
		//Comparing String.
		Verify.verifyEquals(ui_col_names, xls_table_name , "Table Names are different");
	}
	public void validate_list_data( By element  , WebDriver driver  , String class_name , String table_element)
	{
		List<String> xls_col_names  = ExcelCache.getExpectedListData(class_name , table_element );
		ArrayList<String> ui_col_names = new ArrayList<String>();	
		List<WebElement> listelement = driver.findElements(element);
		for (WebElement webelement : listelement)
		{
			ui_col_names.add(webelement.getText());
		}
		Verify.verifyEquals(xls_col_names.containsAll(ui_col_names) , true , "All values does not match");				
	}

	public void validate_list_data_axis( By element  , WebDriver driver  , String class_name , String table_element)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		List<String> xls_col_names  = ExcelCache.getExpectedListData(class_name , table_element );
		ArrayList<String> ui_col_names = new ArrayList<String>();	
		System.out.println("excel column names are"+" "+xls_col_names);
		wait.until(ExpectedConditions.presenceOfElementLocated(element));
		List<WebElement> listelement = driver.findElements(element);
		for (int i=0;i<listelement.size();i++)
		{
			WebElement webelement= listelement.get(i);

			String ui_innerlist="";
			wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("tspan")));
			List<WebElement> listText=webelement.findElements(By.tagName("tspan"));
			if (listText.size()>1)
			{
				listText=webelement.findElements(By.tagName("tspan"));
				for(WebElement textobject:listText)
				{
					ui_innerlist=ui_innerlist+textobject.getText()+" ";

				}
				String ui_innerlistTrim = ui_innerlist.trim();
				ui_col_names.add(ui_innerlistTrim);
				//System.out.println(ui_col_names);
			}
			else
			{
				webelement= listelement.get(i);
				String ele = webelement.findElement(By.tagName("tspan")).getText();
				ui_col_names.add(ele);
			}
		}
		logger.info("Actual Values from UI:"+ui_col_names);
		logger.info("Expected values from Excel Sheet:"+xls_col_names);
		Verify.verifyEquals(xls_col_names.containsAll(ui_col_names) , true , "All values does not match");				
	}

	/*
	 * Waiting for browser loading to complete
	 * @author: guneet
	 * @param driver
	 */
	public void waitForBrowserToLoadCompletely(WebDriver driver) {
		String state = null;
		String oldstate = null;
		try {
			System.out.print("Waiting for browser loading to complete");
			int i = 0;
			while (i < 5) {
				Thread.sleep(1000);
				state = ((JavascriptExecutor) driver).executeScript("return document.readyState;").toString();
				System.out.print("." + Character.toUpperCase(state.charAt(0)) + ".");
				if (state.equals("interactive") || state.equals("loading"))
					break;
				/*
				 * If browser in 'complete' state since last X seconds. Return.
				 */

				if (i == 1 && state.equals("complete")) {
					System.out.println();
					return;
				}
				i++;
			}
			i = 0;
			oldstate = null;
			Thread.sleep(2000);

			/*
			 * Now wait for state to become complete
			 */
			while (true) {
				state = ((JavascriptExecutor) driver).executeScript("return document.readyState;").toString();
				System.out.print("." + state.charAt(0) + ".");
				if (state.equals("complete"))
					break;

				if (state.equals(oldstate))
					i++;
				else
					i = 0;
				/*
				 * If browser state is same (loading/interactive) since last 60
				 * secs. Refresh the page.
				 */
				if (i == 15 && state.equals("loading")) {
					System.out.println("\nBrowser in " + state + " state since last 60 secs. So refreshing browser.");
					driver.navigate().refresh();
					System.out.print("Waiting for browser loading to complete");
					i = 0;
				} else if (i == 6 && state.equals("interactive")) {
					System.out.println(
							"\nBrowser in " + state + " state since last 30 secs. So starting with execution.");
					return;
				}

				Thread.sleep(4000);
				oldstate = state;

			}
			System.out.println();

		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	/*
	 * Retrieve Webelement List Size
	 * @author: guneet
	 * @param loc
	 * @param driver
	 */
	public int getWebelentSize(By loc, WebDriver driver){
		return driver.findElements(loc).size();
	}


	/*
	 * Validate List is sorted
	 * @author: guneet
	 * @param tableData2
	 * @param order
	 */
	public void validateListIsSorted(LinkedList<String> tableData2, String order) {

		LinkedList<String> sortedData = new LinkedList<>(tableData2);
		LinkedList<String> sortedData1 = new LinkedList<>(tableData2);
		if (order.equalsIgnoreCase("asc"))
		{
			Collections.sort(sortedData);
			logger.info("List data in ascending order :-  "+sortedData);
		}
		else if (order.equalsIgnoreCase("desc")) {
			Collections.sort(sortedData1);
			sortedData.clear();
			for (int yy = sortedData1.size() - 1; yy >= 0; yy--) {
				sortedData.add(sortedData1.get(yy).toString());
			}
			logger.info("List data in descending order :-  "+sortedData);
		}
		for (int i = 0; i < tableData2.size(); i++) {
			Verify.verifyEquals(sortedData.get(i), tableData2.get(i));
		}

	}

	/**
	 * Login method.
	 * @author csingh5
	 */

	public void login(WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 5);
		driver.findElement(user_name).sendKeys(Properties_Reader.readProperty("Username"));
		driver.findElement(pword).sendKeys(Properties_Reader.readProperty("Password"));
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(submit_login)));
		//driver.findElement(submit_login).click();
		clickByJavascript(driver, driver.findElement(submit_login));
		Boolean viewPresent = isElementPresent(driver, View);
		if(!viewPresent)
		{
			clickByJavascript(driver, driver.findElement(submit_login));
			logger.info("Clicked login button at second attempt");
		}
	}

	public void clickByJavascript(WebDriver driver, WebElement ele)
	{
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", ele);
	}

	public boolean isElementPresent(WebDriver driver,By ele)
	{
		boolean flag = false;
		try {
			driver.findElement(ele);
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Handling table drill-down operation.
	 * @author guneet
	 */	
	public void validate_drilldown(String view2Table, String view2DrillStart, String view2DrillEnd, WebDriver driver) {
		waitForBrowserToLoadCompletely(driver);
		int count = driver.findElements(By.xpath(view2Table)).size();
		int i = 1;
		if (count > 0) {
			List<WebElement> dr = driver.findElements(By.xpath(view2DrillStart + i + view2DrillEnd));
			while (dr.get(dr.size() - 1).getAttribute("class").toString()
					.equalsIgnoreCase("treegrid-expander treegrid-expander-collapsed drilling")) {
				javaScriptExecutor(driver,dr.get(dr.size() - 1));
				i++;
				dr = driver.findElements(By.xpath(view2DrillStart + i + view2DrillEnd));
			}
		}
	}	

	/**
	 * Method is fetching and comparing data from XLS and UI for Y-axis values from the graph values.
	 * @author nkumar9
	 * @param element
	 * @param class_name
	 * @param table_element
	 */
	public void validate_graph_data_yaxis( By element  , WebDriver driver  , String class_name , String table_element)
	{
		List<String> xls_col_names  = ExcelCache.getExpectedListData(class_name , table_element );
		ArrayList<String> ui_col_names = new ArrayList<String>();	

		List<WebElement> listelement = driver.findElements(element);
		for (int i=0;i<listelement.size()-1;i++)
		{
			WebElement elmt= listelement.get(i);
			String ui_innerlist="";
			List<WebElement> listText=elmt.findElements(By.tagName("tspan"));
			if (listText.size()>1)
			{

				for(WebElement textobject:listText)
				{
					ui_innerlist=ui_innerlist+textobject.getText()+" ";

				}
				String ui_innerlistTrim = ui_innerlist.trim();
				ui_col_names.add(ui_innerlistTrim);
			}
			else
			{
				elmt= listelement.get(i);
				String ele = elmt.findElement(By.tagName("tspan")).getText();
				ui_col_names.add(ele);
			}
		}
		logger.info("Actual Values from UI:"+ui_col_names);
		logger.info("Expected values from Excel Sheet:"+xls_col_names);
		Verify.verifyEquals(xls_col_names.containsAll(ui_col_names) , true , "All values does not match");				
	}

	/*
	 * Validating filter heading
	 * @author guneet
	 */
	public void validate_filter_data(By element, WebDriver driver, String class_name, String table_element) {
		List<String> xls_col_names = ExcelCache.getExpectedListData(class_name, table_element);
		ArrayList<String> ui_col_names = new ArrayList<String>();
		List<WebElement> listelement = driver.findElements(element);
		for (int i = 0; i < listelement.size() - 2; i++) {
			ui_col_names.add(listelement.get(i).getText());
		}
		Verify.verifyEquals(xls_col_names.containsAll(ui_col_names), true, "All values does not match");
	}

	/**
	 * Validating dropdown values
	 * @author guneet
	 */
	public void validate_filter_dropdown_data(By element, WebDriver driver, String class_name, String table_element) {
		List<String> xls_col_names = ExcelCache.getExpectedListData(class_name, table_element);
		Select select = new Select(driver.findElement(element));
		ArrayList<String> ui_col_names = new ArrayList<String>();
		List<WebElement> elementCount = select.getOptions();

		for (int i = 0; i <=elementCount.size() - 1; i++) {
			ui_col_names.add(elementCount.get(i).getAttribute("label").toString());
		}
		logger.info("Actual Size from UI:"+ui_col_names.size());
		logger.info("Expected Size from Excel Sheet:"+xls_col_names.size());
		logger.info("Actual Values from UI:"+ui_col_names);
		logger.info("Expected values from Excel Sheet:"+xls_col_names);
		Verify.verifyEquals(xls_col_names.containsAll(ui_col_names), true, "All values does not match");
	}


	/**
	 * Method is fetching and comparing data from XLS and UI for Y-axis values from the graph values.
	 * @author nkumar9
	 * @param element
	 * @param class_name
	 * @param table_element
	 */
	public void validate_DropDownListData( By element, WebDriver driver, String class_name, String table_element)
	{
		List<String> xls_col_names  = ExcelCache.getExpectedListData(class_name , table_element );
		ArrayList<String> ui_col_names = new ArrayList<String>();	

		List<WebElement> listelement = driver.findElements(element);
		for(WebElement object :listelement)
		{
			String value = object.getText();
			ui_col_names.add(value);
		}

		logger.info("Actual drop down Values from UI:"+ui_col_names);
		logger.info("Expected drop down values from Excel Sheet:"+xls_col_names);
		Verify.verifyEquals(xls_col_names.containsAll(ui_col_names) , true , "All values does not match");				
	}




	public void javaScriptExecutor(WebDriver driver, WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	/**
	 * @author NMakkar
	 * @param driver
	 * @param col_of_table
	 * @param data_of_table
	 * @param key
	 */
	public void data_validate_Down(WebDriver driver, String key,List<WebElement> col_data, List<WebElement> data_of_table) {

		int index = 0;

		//Picking up Key index for comparing.
		for (int i = 0; i < col_data.size(); i++) {
			if (col_data.get(i).getText().equalsIgnoreCase(key)) {
				index = i;
				break;
			}
		}

		//Validating values based on Key.
		if (data_of_table.get(index).getText().matches(Properties_Reader.readProperty("int_regex"))) {
			for (WebElement element_data : data_of_table)
				Verify.verifyEquals(element_data.getText().matches(Properties_Reader.readProperty("int_regex")),true);
		} else if (data_of_table.get(index).getText().matches(Properties_Reader.readProperty("NA"))) {
			for (WebElement element_data : data_of_table)
				Verify.verifyEquals(element_data.getText().equals(key), true);
		}
	}

	/**
	 * @author NMakkar
	 * @param col_of_table
	 * @param data_of_table
	 * @param check_text
	 */
	public List<WebElement> modify_cols_data_of_table(List<WebElement> col_of_table, List<WebElement> data_of_table, List<WebElement> updated_col_data,
			String check_text, Boolean add_remove_flag) {
		// TODO Auto-generated method stub

		// Finding Index of columns to be removed.
		for (int i = 0; i < col_of_table.size(); i++) {
			//Validating Text
			if (col_of_table.get(i).getText().contains(check_text)) {
				//Checking flag for adding / removing elements and updating list on that basis.
				if (!add_remove_flag)
					data_of_table.remove(i);
				else 
					data_of_table.add(updated_col_data.get(i));
			}
		}
		//Returning Updated data list.
		return data_of_table;
	}

	/**
	 * @author guneet
	 * Method is waiting for loader to get invisible
	 */
	public void waitloader(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		for (int i = 0; i < 10; i++) {
			try {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(loader));
			} catch (Exception e) {
				break;
			}
		}
	}



	/**
	 * Method is fetching data from XLS select the fetched value in multi-select list.
	 * @author nkumar9
	 * @param element
	 * @param class_name
	 * @param list_element
	 */
	public void selectCheckboxFromDD( By element, WebDriver driver, String class_name, String list_element)
	{
		List<String> xls_col_names  = ExcelCache.getExpectedListData(class_name , list_element );
		List<WebElement> listelement = driver.findElements(element);
		logger.info("Selecting the following values from Column customization list :"+xls_col_names);
		for (int i=0;i<xls_col_names.size();i++)
		{
			for (int j=0;j<listelement.size();j++)
			{
				if(listelement.get(j).getAttribute("value").equals(xls_col_names.get(i)) && !listelement.get(j).isSelected() )
				{
					listelement.get(j).click();
					break;
				}

			}
		}	

	}

	/**
	 * Method is fetching data from XLS de-select the fetched value in multi-select list.
	 * @author nkumar9
	 * @param element
	 * @param class_name
	 * @param list_element
	 */
	public void deSelectCheckboxFromDD( By element, WebDriver driver, String class_name, String list_element)
	{
		List<String> xls_col_names  = ExcelCache.getExpectedListData(class_name , list_element );
		List<WebElement> listelement = driver.findElements(element);
		logger.info("Deselecting the following values from Column customization list :"+xls_col_names);
		for (int i=0;i<xls_col_names.size();i++)
		{
			for (int j=0;j<listelement.size();j++)
			{
				if(listelement.get(j).getAttribute("value").equals(xls_col_names.get(i)) && listelement.get(j).isSelected() )
				{
					listelement.get(j).click();
					break;
				}

			}
		}	

	}

	/**
	 * @author nkumar9 
	 * Method is to get table columns from UI
	 */

	public List<String> getTableColumns( By element, WebDriver driver) {
		System.out.println("abc");
		List<WebElement> listelement = driver.findElements(element);
		ArrayList<String> ui_col_names = new ArrayList<String>();
		for (WebElement webelement : listelement)
		{
			ui_col_names.add(webelement.getText());
		}

		return ui_col_names;


	}
	/**
	 * @author nkumar9 
	 * Method is to Compare two list
	 */
	public static boolean compareTwoList(List<String> list1,List<String> list2){
		boolean value=false;
		for(int i=0;i<list1.size();i++)
		{
			if(list2.contains(list1.get(i)))
			{
				value=true;
			}
			else
			{
				logger.error("Selected column : "+list1.get(i)+ " does not exist in UI table column list : "+list2);
				value=false;
			}
		}
		return value;

	}


	/**
	 * @author csingh 
	 * Wait for given seconds
	 */

	public void waitForLoad(int i)
	{
		try {
			Thread.sleep(i*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @author ggarg
	 * Method is fetching and comparing data from XLS and UI using attribute.
	 */
	public void validate_list_data_using_attribute( By element  , WebDriver driver  , String class_name , String table_element)
	{
		List<String> xls_col_names  = ExcelCache.getExpectedListData(class_name , table_element );
		ArrayList<String> ui_col_names = new ArrayList<String>();	
		List<WebElement> listelement = driver.findElements(element);
		for (WebElement webelement : listelement)
		{
			ui_col_names.add(webelement.getAttribute("textContent"));
		}
		Verify.verifyEquals(xls_col_names.containsAll(ui_col_names) , true , "All values does not match");
	}

	/**
	 * Validating dropdown values
	 * @author guneet
	 */
	public void explicitWait(WebDriver driver, int wait)
	{
		try {
			wait= 1000*wait;
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
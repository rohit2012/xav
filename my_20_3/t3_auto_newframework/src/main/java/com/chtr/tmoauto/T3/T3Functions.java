package com.chtr.tmoauto.T3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.chtr.tmoauto.jdbc.OracleDb;
import com.chtr.tmoauto.logging.Logging;
import com.chtr.tmoauto.webui.GUI;

/**
 * @author Richa_Khandelwal
 *
 */
/**
 * @author Richa_Khandelwal
 *
 */
public class T3Functions {

	Logging log = new Logging();
	GUI fwgui = new GUI();
	String productName;
	int revision_number = 0;
	private String billingCreatedDate;
	private String purchaseDate;
	private String overridePrice;
	private String billEndDate;
	String crm_orderid;
	String billingAccount;
	String customerAccount;
	String serviceAccount2;
	String serviceAccount1;

	/**
	 * This function click on the Refresh button and Open the task in OSM .
	 * 
	 * @author Richa Khandelwal
	 * @param parameter
	 * @param fwgui.driver
	 * @since 1/12/2016
	 */

	public void T3_OSM_Refresh_OpenTask(String configuration_map_fullpath, List<String> parameter, String tab_name)
			throws Exception {

		log.fw_writeLogEntry("Executing T3_OSM_Refresh_OpenTask of T3Functions.Java", "Info");
		log.fw_writeLogEntry("Refreshing the OSM. ", "NA");

		Thread.sleep(8000);
		String sourceNameFromExcel;
		String productNameFromExcel;
		sourceNameFromExcel = parameter.get(0);
		productNameFromExcel = parameter.get(1);
		String state = "";
		// Refresh
		fwgui.fw_event(configuration_map_fullpath, tab_name, "ClickJAVASCRIPT", "OSM_Refresh", "", "", "8000");
		// OpenTask
		String tablexpath = "//div[@id='aazone.queryResults']/form/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table";
		// Grab the table
		WebElement table = fwgui
				.fw_get_webelements_object("OSM search table element", "NA", "xpath", tablexpath, "", 1000).get(0);
		// Now get number of rows from the table
		int numOfRow = table.findElements(By.tagName("tr")).size();

		// divided Xpath In three parts to pass Row_count and Col_count values.
		String first_part = "//div[@id='aazone.queryResults']/form/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[";
		String second_part = "]/td[";
		String third_part = "]";

		// take the 8th column values
		int sourcecolumn = 8;
		int RefNoColumn = 13;
		int stateColumn = 18;
		int taskColumn = 16;
		Boolean taskFound = false;
		String finalState = "null";
		String source;
		String RefNo;
		String provisiontask;
		if (sourceNameFromExcel.equalsIgnoreCase("SomProvisionOE")) {

			for (int i = 2; i <= numOfRow; i++) {

				// Will retrieve value from located cell
				source = fwgui.fw_get_text("Source Value", "NA", "xpath",
						first_part + i + second_part + sourcecolumn + third_part, "NA", 0);
				RefNo = fwgui.fw_get_text("RefNo Value", "NA", "xpath",
						first_part + i + second_part + RefNoColumn + third_part, "NA", 0);
				state = fwgui.fw_get_text("State Value", "NA", "xpath",
						first_part + i + second_part + stateColumn + third_part, "NA", 0);
				provisiontask = fwgui.fw_get_text("ProvisionTask Value", "NA", "xpath",
						first_part + i + second_part + taskColumn + third_part, "NA", 0);

				if (source.contains(sourceNameFromExcel)) {
					finalState = state;
				}
				for (String t : parameter) {
					t = t.trim();
					if (provisiontask.trim().equalsIgnoreCase(t.trim())) {
						if (source.contains(sourceNameFromExcel)
								&& (RefNo.contains(productNameFromExcel) || productNameFromExcel.isEmpty())
								&& (!(state.equalsIgnoreCase("completed")))) {
							taskFound = true;
							fwgui.fw_click_element_using_javascript("Task Row", "NA", "xpath",
									first_part + i + second_part + "1" + third_part + "/input", "", 3000);

							break;

						}
					}
				}
				if (taskFound) {
					break;
				}

			}

		} else {

			for (int i = 2; i <= numOfRow; i++) {

				// Will retrieve below value from located cell
				source = fwgui.fw_get_text("Source Value", "NA", "xpath",
						first_part + i + second_part + sourcecolumn + third_part, "NA", 0);
				RefNo = fwgui.fw_get_text("RefNo Value", "NA", "xpath",
						first_part + i + second_part + RefNoColumn + third_part, "NA", 0);
				state = fwgui.fw_get_text("State Value", "NA", "xpath",
						first_part + i + second_part + stateColumn + third_part, "NA", 0);
				if (source.contains(sourceNameFromExcel)) {
					finalState = state;
				}
				if (source.contains(sourceNameFromExcel)
						&& (RefNo.contains(productNameFromExcel) || productNameFromExcel.isEmpty())
						&& (!(state.equalsIgnoreCase("completed")))) {
					taskFound = true;
					fwgui.fw_click_element_using_javascript("Task Row", "NA", "xpath",
							first_part + i + second_part + "1" + third_part + "/input", "", 3000);
					break;

				}
			}

			if (!taskFound) {
				Thread.sleep(60000);
				fwgui.fw_event(configuration_map_fullpath, tab_name, "ClickJAVASCRIPT", "OSM_Refresh", "", "", "8000");

				for (int i = 2; i <= numOfRow; i++) {

					// Will retrieve value from located cell
					source = fwgui.fw_get_text("Source Value", "NA", "xpath",
							first_part + i + second_part + sourcecolumn + third_part, "NA", 0);
					RefNo = fwgui.fw_get_text("RefNo Value", "NA", "xpath",
							first_part + i + second_part + RefNoColumn + third_part, "NA", 0);
					state = fwgui.fw_get_text("State Value", "NA", "xpath",
							first_part + i + second_part + stateColumn + third_part, "NA", 0);
					if (source.contains(sourceNameFromExcel)) {
						finalState = state;
					}
					if (source.contains(sourceNameFromExcel)
							&& (RefNo.contains(productNameFromExcel) || productNameFromExcel.isEmpty())
							&& (!(state.equalsIgnoreCase("completed")))) {
						taskFound = true;
						fwgui.fw_click_element_using_javascript("Task Row", "NA", "xpath",
								first_part + i + second_part + "1" + third_part + "/input", "", 3000);
						break;
					}
				}
			}
		}
		if (!taskFound) {
			throw new Exception("Error in opening " + productNameFromExcel + " task for " + sourceNameFromExcel
					+ " Either taskName is incorrect or state is Complete .State for this task is " + finalState);

		}
		log.fw_writeLogEntry("Step Passed :- T3_OSM_Refresh_OpenTask of T3Functions.Java", "NA");

	}

	/**
	 * This function performs 3 task : 
	 * 1. Refresh and Open Task as specified in parameter[0]
	 * 2. Verify the Task Name from the OSM and parameters
	 * 3. Click on Update Button
	 * 
	 * @param configuration_map_fullpath
	 * @param parameter
	 * @param tab_name
	 * @throws Exception
	 */
	public void T3_Verify_Plan_TaskName(String configuration_map_fullpath, List<String> parameter, String tab_name)
			throws Exception {

		log.fw_writeLogEntry("Executing T3_Verify_Plan_TaskName of T3Functions.Java", "Info");
		log.fw_writeLogEntry("Verify the Plan Taskname in OSM. ", "NA");

		Thread.sleep(2000);
		if (parameter.isEmpty()) {
			log.fw_writeLogEntry("Step Failed :- No TaskName present in Function Data Map to verify the OSM. ", "NA");

		}
		String OSMtask;
		boolean taskfound = false;
		List<String> OpenTask = new ArrayList<String>();
		OpenTask.add(parameter.get(0));
		OpenTask.add(parameter.get(1));
		for (int i = 2; i < parameter.size(); i++) {
			// Refresh and Open Task
			if (parameter.get(0).equalsIgnoreCase("SomProvisionOE")) {
				OpenTask = parameter;
			}
			T3_OSM_Refresh_OpenTask(configuration_map_fullpath, parameter, tab_name);

			// Validate Task Name
			fwgui.fw_event(configuration_map_fullpath, tab_name, "GetText", "OSM_TaskName", "", "", "2000");
			OSMtask = fwgui.return_get_text;
			taskfound = false;
			Thread.sleep(2000);
			for (String taskName : parameter) {
				taskName = taskName.trim();
				log.fw_writeLogEntry(taskName, "NA");
				if (taskName.contains("PlanNetwork") || taskName.contains("UNI Order")
						|| taskName.contains("ENNI Order") || taskName.contains("ConstructSite")
						|| taskName.equals("")) {
					continue;
				}

				Boolean Status = false;
				fwgui.fw_event(configuration_map_fullpath, tab_name, "GetText", "OSM_TaskName", "", "", "1000");

				String taskNamefetched = fwgui.return_get_text;
				if (taskNamefetched.equalsIgnoreCase(taskName)) {
					Status = true;
				}

				if (Status) {
					taskfound = true;
					log.fw_writeLogEntry("TaskName " + taskName + " is  present ", "NA");

					if (taskName.equalsIgnoreCase("Build Service")) {
						fwgui.fw_event(configuration_map_fullpath, tab_name, "SelectListValueByVisibleText",
								"OSM_ServiceProfile", "L3_PTP", "", "3000");
					}
					if (taskName.equalsIgnoreCase("Validate Order Task")) {

						// Set 0 to GLID textbox

						List<WebElement> elements = fwgui.driver.findElements(By.tagName("textarea"));
						for (WebElement element : elements) {
							if (element.getText().equalsIgnoreCase("NOT_FOUND")) {
								element.clear();
								Thread.sleep(2000);
								element.sendKeys("0");
								Thread.sleep(2000);
							}
						}

					}

					T3_OSM_Update_Task_Using_Update_Button(configuration_map_fullpath, tab_name);

					log.fw_writeLogEntry("Updating " + taskName + " ", "NA");
					break;
				}
			}
			if (!taskfound) {
				log.fw_writeLogEntry("Step Failed :- Task " + OSMtask + " is not present in functionDataMap list ",
						"NA");
				throw new Exception("************Task " + OSMtask + " is not present in functionDataMap list ");
			}
		}

		log.fw_writeLogEntry("Step Passed :- T3_Verify_Plan_TaskName of T3Functions.Java", "NA");

	}

	/**
	 * This function Click on the Update Button in OSM 
	 * 
	 * @param configuration_map_fullpath
	 * @param tab_name
	 * @throws Exception
	 */
	public void T3_OSM_Update_Task_Using_Update_Button(String configuration_map_fullpath, String tab_name)
			throws Exception {
		log.fw_writeLogEntry("Executing T3_OSM_Update_Task_Using_Update_Button of T3Functions.Java", "Info");
		log.fw_writeLogEntry("Updating Task by clicking on Update button.", "NA");
		// Click on update Button
		fwgui.fw_event(configuration_map_fullpath, tab_name, "ClickJAVASCRIPT", "OSM_UpdateButton", "", "", "3000");

		log.fw_writeLogEntry("Step Passed :- T3_OSM_Update_Task_Using_Update_Button of T3Functions.Java", "NA");

	}

	/**
	 * This function selects the circuit type and sets the Override Price .
	 * 
	 * @author Rohit
	 * @param tab_name
	 * @param tc_objectname
	 * @param orderid
	 * @throws Exception
	 * @since 1/13/2016
	 */

	public void T3_CRM_Select_Product_Line(String parameter) throws Exception {

		log.fw_writeLogEntry("Executing T3_CRM_Select_Product_Line of T3Functions.Java", "Info");
		log.fw_writeLogEntry("Adding OverridePrice and Circuit Type to the Line Items", "NA");

		String[] locator;
		String CircuitType = parameter;

		Thread.sleep(3000);
		String tablexpath = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[3]/div/table";
		String headertablexpath = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[2]/div/table";
		// Grab the table

		WebElement table = (WebElement) fwgui
				.fw_get_webelements_object("Table Element", "NA", "xpath", tablexpath, "", 1000).get(0);
		// Now get number of rows from the table
		int numOfRow = table.findElements(By.tagName("tr")).size();
		// Get number of columns In table.

		// divided Xpath In three parts to pass Row_count and Col_count values.
		String first_part = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[3]/div/table/tbody/tr[";
		String second_part = "]/td[";
		String third_part = "]";

		// take the 19th column values
		int netpriceColumn = getColumnNumber(headertablexpath, "Net_Price");

		int circuitTypeColumn = getColumnNumber(headertablexpath, "Application_Type");
		int overridePriceColumn = getColumnNumber(headertablexpath, "Unit_Price");

		int j = 16;
		String final_xpath;
		String NetPrice;
		int i;
		Thread.sleep(5000);
		for (i = 2; i <= numOfRow; i++) {
			// Prepared final xpath of specific cell as per values of i and j.
			final_xpath = first_part + i + second_part + netpriceColumn + third_part;

			// wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(final_xpath)));
			fwgui.fw_check_element_existence("netprice Column", "NA", "xpath", final_xpath, "200,1000", 1000);

			// Will retrieve value from located cell
			NetPrice = fwgui.fw_get_text("Net Price", "NA", "xpath", final_xpath, "", 1000);

			if (NetPrice.length() > 1) {
				fwgui.fw_check_element_existence("OverridePrice Column", "NA", "xpath",
						first_part + i + second_part + overridePriceColumn + third_part, "200,1000", 1000);
				fwgui.fw_click_button("Click on override Grid", "NA", "xpath",
						first_part + i + second_part + overridePriceColumn + third_part, "", 2000);
				fwgui.fw_check_element_existence("OverridePrice Column Input", "NA", "xpath",
						first_part + i + second_part + overridePriceColumn + third_part + "/input", "200,1000", 1000);
				fwgui.fw_enter_data_into_text_field("Set Override Price", "NA", "xpath",
						first_part + i + second_part + overridePriceColumn + third_part + "/input", "10", 2000);
				fwgui.fw_check_element_existence("OverridePrice Column Input", "NA", "xpath",
						first_part + i + second_part + "3" + third_part, "200,1000", 1000);
				fwgui.fw_click_button("Click on element", "NA", "xpath",
						first_part + i + second_part + "3" + third_part, "", 2000);
			}

			Thread.sleep(5000);

			// Set the circuit Type as Cell Backhaul
			fwgui.fw_check_element_existence("circuitTypeColumn ", "NA", "xpath",
					first_part + i + second_part + circuitTypeColumn + third_part, "200,1000", 1000);
			fwgui.fw_click_button("Click on circuit Type", "NA", "xpath",
					first_part + i + second_part + circuitTypeColumn + third_part, "", 2000);
			fwgui.fw_check_element_existence("circuitType Column Input", "NA", "xpath",
					first_part + i + second_part + circuitTypeColumn + third_part + "/input", "200,1000", 1000);
			fwgui.fw_enter_data_into_text_field("Set circuit Type", "NA", "xpath",
					first_part + i + second_part + circuitTypeColumn + third_part + "/input", CircuitType, 2000);
			fwgui.fw_check_element_existence("check on element", "NA", "xpath",
					first_part + i + second_part + "3" + third_part, "200,1000", 1000);
			fwgui.fw_click_button("Click on element", "NA", "xpath", first_part + i + second_part + "3" + third_part,
					"", 2000);

			Thread.sleep(3000);
		}
		int numOfRow1 = table.findElements(By.tagName("tr")).size();
		if (numOfRow1 == 11) {
			// click on last page for table
			fwgui.fw_click_button("Last Page", "NA", "id", "last_pager_s_2_l", "NA", 2000);
			Thread.sleep(5000);
			i = 11;
			// Prepared final xpath of specific cell as per values of i and j.
			final_xpath = first_part + i + second_part + j + third_part;
			// Will retrieve value from located cell
			fwgui.fw_check_element_existence("Check on element", "NA", "xpath", final_xpath, "200,1000", 1000);
			NetPrice = fwgui.fw_get_text("Net Price", "NA", "xpath", final_xpath, "", 1000);
			if (!"".equalsIgnoreCase(NetPrice)) {
				fwgui.fw_check_element_existence("Check on overridePriceColumn", "NA", "xpath",
						first_part + i + second_part + overridePriceColumn + third_part, "200,1000", 1000);
				fwgui.fw_click_button("Click on overRide Price", "NA", "xpath",
						first_part + i + second_part + overridePriceColumn + third_part, "", 2000);
				fwgui.fw_check_element_existence("Check on overridePriceColumn Input ", "NA", "xpath",
						first_part + i + second_part + overridePriceColumn + third_part + "/input", "200,1000", 1000);
				fwgui.fw_enter_data_into_text_field("Set Override Price", "NA", "xpath",
						first_part + i + second_part + overridePriceColumn + third_part + "/input", "10", 2000);
				fwgui.fw_check_element_existence("Check on  Element", "NA", "xpath",
						first_part + i + second_part + "3" + third_part, "200,1000", 1000);
				fwgui.fw_click_button("Click on any element", "NA", "xpath",
						first_part + i + second_part + "3" + third_part, "", 2000);

				Thread.sleep(1000);
			}
			Thread.sleep(5000);
			// Set the circuit Type as Cell Backhaul
			fwgui.fw_check_element_existence("Check on  circuitTypeColumn", "NA", "xpath",
					first_part + i + second_part + circuitTypeColumn + third_part, "200,1000", 1000);
			fwgui.fw_click_button("Click on circuitTypeColumn", "NA", "xpath",
					first_part + i + second_part + circuitTypeColumn + third_part, "", 2000);
			fwgui.fw_enter_data_into_text_field("Set circuit Type", "NA", "xpath",
					first_part + i + second_part + circuitTypeColumn + third_part + "/input", CircuitType, 2000);
			fwgui.fw_check_element_existence("Check on  element", "NA", "xpath",
					first_part + i + second_part + "3" + third_part, "200,1000", 1000);
			fwgui.fw_click_button("Click on any element", "NA", "xpath",
					first_part + i + second_part + "3" + third_part, "", 2000);
			Thread.sleep(3000);
		}
		log.fw_writeLogEntry("Step Passed :- T3_CRM_Select_Product_Line of T3Functions.Java", "NA");

	}

	/**
	 * This Function fetches the Column number from the table header xpath and Column Name 
	 * 
	 * @param xpath
	 * @param ColumnName
	 * @return
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public int getColumnNumber(String xpath, String ColumnName) throws InterruptedException, IOException {
		log.fw_writeLogEntry("Executing getColumnNumber of BusinessFunction Repository", "Info");

		log.fw_writeLogEntry("This step is used to get Column Number of table", "NA");
		int index = 0;
		Thread.sleep(2000);
		// Get number of columns in header
		fwgui.fw_check_element_existence("Check on  Table", "NA", "xpath", xpath, "200,1000", 1000);

		WebElement table = (WebElement) fwgui.fw_get_webelements_object("WebElements", "NA", "xpath", xpath, "", 1000)
				.get(0);
		// Now get number of rows from the table
		int numOfColumns = table.findElements(By.xpath(xpath + "/thead/tr/th")).size();

		// divided Xpath In three parts to pass Row_count and Col_count values.
		String first_part = xpath + "/thead/tr/th[";
		String second_part = "]";

		for (int i = 1; i <= numOfColumns; i++) {
			String id = fwgui.fw_get_attribute_value("xpath", first_part + i + second_part, "id", 0);
			if (id.contains(ColumnName)) {
				index = i;
				return index;
			}
		}
		log.fw_writeLogEntry("Step Passed :- getColumnNumber of T3Functions.Java", "NA");
		return index;
	}

	/**
	 * This Function completes Activities in Activities section
	 * @throws Exception
	 */
	public void T3_CRM_Complete_All_Activities_And_Validate() throws Exception {
		log.fw_writeLogEntry("Executing T3_CRM_Complete_All_Activities_And_Validate of BusinessFunction Repository",
				"Info");
		log.fw_writeLogEntry("Complete all the Activities in the Activities section", "NA");
		fwgui.fw_click_button("Show More Activities", "NA", "css", "span[title='Activities:Show more']", "", 5000);
		Thread.sleep(3000);
		String activitiesXpath = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[3]/div/table";
		String activitiesHeaderXpath = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[2]/div/table";
		fwgui.fw_check_element_existence("Activities Table", "NA", "xpath", activitiesXpath, "200,1000", 1000);
		WebElement table = (WebElement) fwgui
				.fw_get_webelements_object("Activities table", "NA", "xpath", activitiesXpath, "", 1000).get(0);
		// Now get number of rows from the table

		int numOfRow = table.findElements(By.tagName("tr")).size();
		// Get number of columns In table.

		// divided Xpath In three parts to pass Row_count and Col_count values.
		String first_part = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[3]/div/table/tbody/tr[";
		String second_part = "]/td[";
		String third_part = "]";

		// take the 9th column values
		int statusColumn = getColumnNumber(activitiesHeaderXpath, "l_Status");

		for (int i = 2; i <= numOfRow; i++) {
			fwgui.fw_check_element_existence("Activities Table Row", "NA", "xpath",
					first_part + i + second_part + statusColumn + third_part, "200,1000", 1000);
			fwgui.fw_click_button("Status column", "NA", "xpath",
					first_part + i + second_part + statusColumn + third_part, "NA", 2000);
			fwgui.fw_check_element_existence("Activities Table Row", "NA", "xpath",
					first_part + i + second_part + statusColumn + third_part + "/input", "200,1000", 1000);
			fwgui.fw_enter_data_into_text_field("Setting status As complete", "NA", "xpath",
					first_part + i + second_part + statusColumn + third_part + "/input", "Complete", 2000);
			fwgui.fw_check_element_existence("Any element", "NA", "xpath",
					first_part + i + second_part + "13" + third_part, "200,1000", 1000);
			fwgui.fw_click_button("Click on any element", "NA", "xpath",
					first_part + i + second_part + "13" + third_part, "", 2000);
			numOfRow = table.findElements(By.tagName("tr")).size();

		}
		Thread.sleep(2000);
		boolean openactivities = true;
		while (openactivities) {
			// click on validate button
			fwgui.fw_click_button("Validate Button", "NA", "css", "button[title='Quote:Validate']", "", 4000);
			openactivities = CompleteActivities(first_part, second_part, third_part, statusColumn);

		}
		log.fw_writeLogEntry(
				"Step Passed :- T3_CRM_Complete_All_Activities_And_Validate of BusinessFunction Repository", "NA");

	}

	/**
	 * This function is used to call again and again till all the activities are complete
	 * 
	 * @param xpathFirstPart
	 * @param xpathSecondPart
	 * @param xpathThirdPart
	 * @param j
	 * @return
	 * @throws Exception
	 */
	public boolean CompleteActivities(String xpathFirstPart, String xpathSecondPart, String xpathThirdPart, int j)
			throws Exception {
		log.fw_writeLogEntry("Executing CompleteActivities of BusinessFunction Repository", "Info");
		log.fw_writeLogEntry("This step completes all the activities for a Line Item .", "NA");
		Thread.sleep(3000);
		String activitiesXpath = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[3]/div/table";

		boolean openactivities = false;
		fwgui.fw_check_element_existence("activitiesXpath", "NA", "xpath", activitiesXpath, "200,1000", 1000);
		WebElement table = (WebElement) fwgui
				.fw_get_webelements_object("Activities table", "NA", "xpath", activitiesXpath, "", 1000).get(0);

		int numOfRow = table.findElements(By.tagName("tr")).size();
		for (int i = 2; i <= numOfRow; i++) {
			fwgui.fw_check_element_existence("Activities Table Row", "NA", "xpath",
					xpathFirstPart + i + xpathSecondPart + j + xpathThirdPart, "200,1000", 1000);
			String status = fwgui.fw_get_attribute_value("xpath",
					xpathFirstPart + i + xpathSecondPart + j + xpathThirdPart, "title", 0);
			if (status.equalsIgnoreCase("Open")) {
				Thread.sleep(2000);
				fwgui.fw_check_element_existence("Activities Table Row", "NA", "xpath",
						xpathFirstPart + i + xpathSecondPart + j + xpathThirdPart, "200,1000", 1000);
				fwgui.fw_click_button("Complete open task", "NA", "xpath",
						xpathFirstPart + i + xpathSecondPart + j + xpathThirdPart, "", 3000);
				fwgui.fw_check_element_existence("Activities Table Row", "NA", "xpath",
						xpathFirstPart + i + xpathSecondPart + j + xpathThirdPart + "/input", "200,1000", 1000);
				fwgui.fw_enter_data_into_text_field("Setting status As complete", "NA", "xpath",
						xpathFirstPart + i + xpathSecondPart + j + xpathThirdPart + "/input", "Complete", 2000);
				fwgui.fw_check_element_existence("Click on any element", "NA", "xpath",
						xpathFirstPart + i + xpathSecondPart + "13" + xpathThirdPart, "200,1000", 1000);
				fwgui.fw_click_button("Click on any element", "NA", "xpath",
						xpathFirstPart + i + xpathSecondPart + "13" + xpathThirdPart, "", 2000);
				numOfRow = table.findElements(By.tagName("tr")).size();
				openactivities = true;
			}
		}
		log.fw_writeLogEntry("Step Passed :- CompleteActivities of BusinessFunction Repository", "NA");
		return openactivities;
	}

	/**
	 * This Function checks the Validation Report and acknowledge the non critical task 
	 * 
	 * @throws Exception
	 */
	public void T3_CRM_ValidationReport() throws Exception {

		fwgui.fw_check_element_existence("Check the title", "NA", "class", "siebui-applet-title", "200,1000", 1000);
		log.fw_writeLogEntry("Executing T3_CRM_ValidationReport of BusinessFunction Repository", "Info");
		log.fw_writeLogEntry("Check the Validation Report. Acknowledge all the Non-Critical Task ", "NA");
		fwgui.fw_check_element_existence("Check for table Validation report", "NA", "id", "s_2_l", "200,1000", 1000);
		WebElement table = (WebElement) fwgui
				.fw_get_webelements_object("Table elements - Validation report", "NA", "id", "s_2_l", "NA", 1000)
				.get(0);
		Thread.sleep(4000);
		int numofRows = table.findElements(By.tagName("tr")).size();

		for (int i = 2; i <= numofRows; i++) {

			fwgui.fw_check_element_existence("Check for table row Validation report", "NA", "xpath",
					"//table[@id='s_2_l']/tbody/tr[" + i + "]/td[3]", "200,1000", 1000);
			String returnMessage = fwgui.fw_get_attribute_value("xpath",
					"//table[@id='s_2_l']/tbody/tr[" + i + "]/td[3]", "title", 2000);
			if (returnMessage.contains("Capital Approval does not exist on the Quote.") || returnMessage
					.contains("Please select at least one Technical contact for the Service Account.")) {

				fwgui.fw_check_element_existence("Check for task grid", "NA", "xpath",
						"//table[@id='s_2_l']/tbody/tr[" + i + "]/td[5]", "200,1000", 1000);
				fwgui.fw_click_button("click on task grid", "NA", "xpath",
						"//table[@id='s_2_l']/tbody/tr[" + i + "]/td[5]", "NA", 2000);
				fwgui.fw_check_element_existence("check for task input", "NA", "xpath",
						"//table[@id='s_2_l']/tbody/tr[" + i + "]/td[5]", "200,1000", 2000);

				fwgui.fw_click_button("click on task checkbox", "NA", "xpath",
						"//table[@id='s_2_l']/tbody/tr[" + i + "]/td[5]", "NA", 2000);

				log.fw_writeLogEntry("Ackowledged the " + returnMessage, "NA");

			}

		}

		log.fw_writeLogEntry("Step Passed :- T3_CRM_ValidationReport of BusinessFunction Repository", "NA");

	}

	/**
	 * This function calls the Validate Milestone in pooling loop
	 * 
	 * @param configuration_map_fullpath
	 * @param tab_name
	 * @param parameter
	 * @throws Exception
	 */
	public void T3_CRM_Verify_Milestone(String configuration_map_fullpath, String tab_name, List<String> parameter)
			throws Exception {
		log.fw_writeLogEntry("Executing T3_CRM_Verify_Milestone of BusinessFunction Repository", "Info");
		log.fw_writeLogEntry("Verifying Milestone from Line Items Table ", "NA");

		Timer_T3 scenarioTimer = new Timer_T3();

		scenarioTimer.setStartTime(0);
		int iStopTime = getStopTime(scenarioTimer);
		boolean validate, MilestoneFound = false;
		
		scenarioTimer.inTime();
		Thread.sleep(5000);

		// Click on the Order ID
				fwgui.fw_click_element_using_javascript("Sales Go Button", "NA", "xpath", "//*[@name='Order Number']", "", 2000);
				
				Thread.sleep(5000);
		// Click on the Line Item Tab
		fwgui.fw_click_element_using_javascript("Sales Go Button", "NA", "xpath", "//a[text()='Line Items']", "", 2000);
				Thread.sleep(2000);
				
		while (!scenarioTimer.isTimeout(iStopTime)) {
			//T3_CRM_Refresh(configuration_map_fullpath, tab_name, parameter);
			validate = T3_CRM_Validate_Milestone(parameter);
			if (validate) {
				// Exit if validation is true
				log.fw_writeLogEntry("Milestone Found ", "NA");
				MilestoneFound = true;
				break;
			}
			// call refresh method
			log.fw_writeLogEntry("Calling refresh Method ", "NA");
			T3_CRM_Refresh(configuration_map_fullpath, tab_name, parameter);
			// Wait till pooling time
			log.fw_writeLogEntry("Delay set to " + scenarioTimer.getPollingTime() + " seconds", "NA");

			scenarioTimer.delay(scenarioTimer.getPollingTime());
		}

		if (!MilestoneFound) {
			throw new Exception("Milestone not found");
		}

		log.fw_writeLogEntry("Step Passed :- T3_CRM_Verify_Milestone of BusinessFunction Repository", "NA");

	}

	/**
	 * This function validates the milestone in CRM 
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public Boolean T3_CRM_Validate_Milestone(List<String> parameter) throws Exception {

		log.fw_writeLogEntry("Executing T3_CRM_Validate_Milestone of BusinessFunctionRepository", "INFO");
		LinkedHashMap<String, String> Product_MilestoneValues = new LinkedHashMap<>();
		String locator[];

		// Click on Setting button
		Thread.sleep(10000);
		fwgui.fw_click_button("Line Item Menu Button", "NA", "css", "button[title='Line Items Menu']", "", 5000);

		// renumber
		fwgui.fw_click_button("Renumber Button", "NA", "link", "Renumber", "", 10000);

		WebDriverWait Alertwait = new WebDriverWait(fwgui.driver,
				5 /* timeout in seconds */);

		try {
			Alertwait.until(ExpectedConditions.alertIsPresent());
			fwgui.fw_accept_alert();

		} catch (TimeoutException eTO) {

		}

		Boolean status = false;
		Thread.sleep(10000);
		String tablexpath = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[3]/div/table";
		String headertablexpath = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[2]/div/table";
		String columnxpath = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[3]/div/table/tbody/tr[2]/td";
		// divided Xpath In three parts to pass Row_count and Col_count values.
		String first_part = "/html/body/div[1]/div/div[5]/div/div[6]/div/div[1]/div/div[3]/div[1]/div/form/span/div/div[3]/div/div/div[3]/div[3]/div/table/tbody/tr[";
		String second_part = "]/td[";
		String third_part = "]";
		log.fw_writeLogEntry("ProductName - " + productName, "NA");
		if (productName.equalsIgnoreCase("OE Network") || productName.equalsIgnoreCase("Fiber Wavelength Connection")) {

			// Order Entry - Line Item List (Sales)

			tablexpath = "//table[@summary='Line Items']";

			// Get number of columns In table.
			columnxpath = "//table[@summary='Line Items']/tbody/tr[2]/td";

			// divided Xpath In three parts to pass Row_count and Col_count
			// values.
			first_part = "//table[@summary='Line Items']/tbody/tr[";
			second_part = "]/td[";
			third_part = "]";
			headertablexpath = "//div[@class='ui-jqgrid-view']/div[2]/div/table";
		}
		if (parameter.get(0).equalsIgnoreCase("FULFILL SERVICE DSGN COMPLETE")) {

			// Order Entry - Line Item List (Sales)

			tablexpath = "//table[@id='s_3_l']";

			// Get number of columns In table.
			columnxpath = "//table[@id='s_3_l']/tbody/tr[2]/td";

			// divided Xpath In three parts to pass Row_count and Col_count
			// values.
			first_part = "//table[@id='s_3_l']/tbody/tr[";
			second_part = "]/td[";
			third_part = "]";
			headertablexpath = "//div[@class='ui-jqgrid-view']/div[2]/div/table";
			try {

				fwgui.fw_check_element_existence("Check for table", "NA", "xpath", tablexpath, "200,1000", 1000);
			} catch (Exception e) {
				tablexpath = "//table[@summary='Line Items']";

				// Get number of columns In table.
				columnxpath = "//table[@summary='Line Items']/tbody/tr[2]/td";

				// divided Xpath In three parts to pass Row_count and Col_count
				// values.
				first_part = "//table[@summary='Line Items']/tbody/tr[";
				second_part = "]/td[";
				third_part = "]";
			}
		}

		fwgui.fw_check_element_existence("Check for table", "NA", "xpath", tablexpath, "200,1000", 1000);
		// WebElement table = fwgui.driver.findElement(By.xpath(tablexpath));

		WebElement table = (WebElement) fwgui
				.fw_get_webelements_object("Elements for milestone Table", "NA", "xpath", tablexpath, "NA", 1000)
				.get(0);
		// Now get number of rows from the table
		int numOfRow = table.findElements(By.tagName("tr")).size();

		// take the 19th column values

		int j = getColumnNumber(headertablexpath, "Milestone");
		int k = getColumnNumber(headertablexpath, "Product");
		;
		String final_xpath_ms;
		String final_xpath_product;
		String mileStone = null;
		String product = null;
		int i;
		// Thread.sleep(5000);
		for (i = 2; i <= numOfRow; i++) {
			// Prepared final xpath of specific cell as per values of i and j.
			final_xpath_ms = first_part + i + second_part + j + third_part;
			final_xpath_product = first_part + i + second_part + k + third_part;

			// wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(final_xpath_product)));
			fwgui.fw_check_element_existence("Check for product", "NA", "xpath", final_xpath_product, "200,1000", 1000);
			// Will retrieve value from located cell
			product = fwgui.fw_get_text("product", "", "xpath", final_xpath_product, "", 2000);
			// wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(final_xpath_ms)));
			fwgui.fw_check_element_existence("Check for milestone", "NA", "xpath", final_xpath_ms, "200,1000", 1000);
			// Will retrieve value from located cell
			mileStone = fwgui.fw_get_text("milestone", "", "xpath", final_xpath_ms, "", 2000);
			Product_MilestoneValues.put(product, mileStone);

		}

		String[] AssignProjectStart_ProductList = new String[] { "E-Access", "E-Access EPL", "Charter OVC",
				"Charter ENNI Endpoint", "OE ENNI Connection", "UNI Endpoint" };
		String[] AssignProjectStart_ProductList_FI = new String[] { "Fiber Internet", "Fiber Internet Bandwidth",
				"IPv6 Addressing - Charter Provided" };
		String[] AssignProjectStart_ProductList_OENetwork = new String[] { "OE Network", "EVPL", "EVC",
				"UNI Endpoint" };
		String[] Initiate_Billing_Complete_ProductList = new String[] { "Optical Ethernet ENNI Bandwidth",
				"Optical Ethernet ENNI Install", "Optical Ethernet UNI Install", "Optical Ethernet UNI Bandwidth",
				"Truck Roll Fee" };
		String[] Initiate_Billing_Complete_ProductList_OENetwork = new String[] { "Optical Ethernet UNI Install",
				"Optical Ethernet UNI Bandwidth", "Truck Roll Fee" };
		String[] Initiate_Billing_Complete_ProductList_EarlyTermFee = new String[] { "Optical Ethernet UNI Install",
				"Optical Ethernet UNI Bandwidth", "Truck Roll Fee", "Early Termination Fee" };
		String[] Initiate_Billing_Complete_ProductList_FI = new String[] { "Fiber Internet Service" };

		String[] ConstructSiteOrder_ProductList = new String[] { "E-Access EPL", "Charter OVC", "Charter ENNI Endpoint",
				"OE ENNI Connection", "UNI Endpoint" };
		String[] ConstructSiteOrder_ProductList_OENetwork = new String[] { "OE Network", "EVPL", "EVC",
				"UNI Endpoint" };
		String[] ConstructSiteOrder_ProductList_FI = new String[] { "Fiber Internet" };
		String[] ProvisionStart_ProductList = new String[] { "E-Access EPL", "Charter OVC", "Charter ENNI Endpoint",
				"OE ENNI Connection", "UNI Endpoint" };
		String[] ProvisionStart_ProductList_OENetwork = new String[] { "EVPL", "EPL", "UNI Endpoint" };
		String[] ProvisionStart_ProductList_OENetwork_REV_EVPL = new String[] { "EVC", "EVPL", "UNI Endpoint",
				"OE Network" };
		String[] ProvisionStart_ProductList_FI = new String[] { "Fiber Internet" };
		String[] ProvisionComplete_ProductList = new String[] { "E-Access EPL", "Charter OVC", "UNI Endpoint" };
		String[] ProvisionComplete_ProductList_OENetwork = new String[] { "EVPL", "EPL", "UNI Endpoint" };
		String[] ProvisionComplete_ProductList_FI = new String[] { "Fiber Internet" };
		String[] FulfilServiceDesignStart_ProductList = new String[] { "E-Access", "E-Access EPL", "Charter OVC",
				"Charter ENNI Endpoint", "OE ENNI Connection", "UNI Endpoint" };
		String[] FulfilServiceDesignStart_ProductList_OENetwork = new String[] { "OE Network", "EVPL", "EPL",
				"UNI Endpoint" };
		String[] FulfilServiceDesignStart_ProductList_FI = new String[] { "Fiber Internet" };
		String[] FulfilServiceDesignComplete_ProductList = new String[] { "E-Access EPL", "Charter OVC",
				"Charter ENNI Endpoint", "OE ENNI Connection", "UNI Endpoint" };
		String[] FulfilServiceDesignComplete_ProductList_OENetwork = new String[] { "EVPL", "EPL", "UNI Endpoint" };
		String[] FulfilServiceDesignComplete_ProductList_FI = new String[] { "Fiber Internet" };
		String[] AssignProjectStart_ProductList_FW = new String[] { "Fiber Wavelength Connection",
				"Fiber Wavelength Bandwidth", "Fiber Wavelength Port", "OC192 SONET Interface" };
		String[] Initiate_Billing_Complete_ProductList_FW = new String[] { "Wavelength Service" };
		String[] Initiate_Billing_Complete_ProductList_FW_Rev = new String[] { "Wavelength Service",
				"Wavelength Install" };
		String[] ConstructSiteOrder_ProductList_FW_REV = new String[] { "Fiber Wavelength Connection",
				"Fiber Wavelength Bandwidth", "Fiber Wavelength Port", "OC192 SONET Interface" };
		String[] AssignProjectStart_ProductList_EVPL = new String[] { "OE Network", "EVPL", "EVC", "UNI Endpoint" };
		String[] Initiate_Billing_Complete_ProductList_EVPL_Rev = new String[] { "Optical Ethernet UNI Install",
				"Optical Ethernet UNI Bandwidth" };
		String IncorrectProduct = null;
		log.fw_writeLogEntry("ProductName - " + productName, "NA");
		for (String milestonefromExcel : parameter) {

			List<String> productNamefromMilestone = getKeysfromValue(milestonefromExcel, Product_MilestoneValues);

			switch (milestonefromExcel) {

			case "CONSTRUCT SITE START":
				if (productName.equals("Fiber Internet")) {
					IncorrectProduct = compare(ConstructSiteOrder_ProductList_FI, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for CONSTRUCT SITE START ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for CONSTRUCT SITE START Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = CONSTRUCT SITE START", "log");
						return false;
					}
				} else if (productName.equalsIgnoreCase("OE Network")
						|| productName.equalsIgnoreCase("Early Termination Fee")) {
					IncorrectProduct = compare(ConstructSiteOrder_ProductList_OENetwork, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for CONSTRUCT SITE START ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for CONSTRUCT SITE START Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = CONSTRUCT SITE START", "log");
						return false;
					}
				} else if (productName.equals("Fiber Wavelength Connection")) {

					IncorrectProduct = compare(ConstructSiteOrder_ProductList_FW_REV, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for CONSTRUCT SITE START", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for this Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = CONSTRUCT SITE START", "log");
						return false;
					}

				} else {
					IncorrectProduct = compare(ConstructSiteOrder_ProductList, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for CONSTRUCT SITE START ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for CONSTRUCT SITE START Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = CONSTRUCT SITE START", "log");
						return false;
					}
				}
				break;

			case "INITIATE BILLING COMPLETE":
				if (productName.equals("Fiber Internet")) {
					IncorrectProduct = compare(Initiate_Billing_Complete_ProductList_FI, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for Initiate Billing Complete ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for INITIATE BILLING COMPLETE Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = Initiate Billing Complete ", "log");
						return false;
					}
				} else if (productName.equals("OE Network")) {
					IncorrectProduct = compare(Initiate_Billing_Complete_ProductList_OENetwork,
							productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for Initiate Billing Complete ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							IncorrectProduct = compare(Initiate_Billing_Complete_ProductList_EVPL_Rev,
									productNamefromMilestone);
							if (IncorrectProduct.equalsIgnoreCase("")) {
								log.fw_writeLogEntry("Milestone Check is sucessful  for CONSTRUCT SITE START ", "log");
								status = true;
							} else {
								log.fw_writeLogEntry("No product found for CONSTRUCT SITE START Milestone", "log");

								log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
								log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct),
										"log");
								log.fw_writeLogEntry("Expected Value = CONSTRUCT SITE START", "log");
								status = false;

							}
						}
					}
				} else if (productName.equals("Fiber Wavelength Connection")) {
					if (revision_number != 2) {
						IncorrectProduct = compare(Initiate_Billing_Complete_ProductList_FW, productNamefromMilestone);
					} else {
						IncorrectProduct = compare(Initiate_Billing_Complete_ProductList_FW_Rev,
								productNamefromMilestone);
					}

					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for Initiate Billing Complete", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for INITIATE BILLING COMPLETE Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = Initiate Billing Complete ", "log");
						return false;
					}
				} else {
					IncorrectProduct = compare(Initiate_Billing_Complete_ProductList, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for Initiate Billing Complete ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for INITIATE BILLING COMPLETE Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = Initiate Billing Complete ", "log");
						return false;
					}
				}
				break;
			case "ASSIGN PROJECT START":
				if (productName.equals("Fiber Internet")) {
					IncorrectProduct = compare(AssignProjectStart_ProductList_FI, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for Assign Project Start ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for Assign Project Start Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = Assign Project Start", "log");
						return false;
					}
				} else if (productName.equalsIgnoreCase("OE Network")
						|| productName.equalsIgnoreCase("Early Termination Fee")) {
					IncorrectProduct = compare(AssignProjectStart_ProductList_OENetwork, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for Assign Project Start ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for this Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = Assign Project Start", "log");
						return false;
					}
				} else if (productName.equals("Fiber Wavelength Connection")) {

					IncorrectProduct = compare(AssignProjectStart_ProductList_FW, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for Assign Project Start ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for this Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = Assign Project Start", "log");
						return false;
					}

				} else {
					IncorrectProduct = compare(AssignProjectStart_ProductList, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for Assign Project Start ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for Assign Project Start Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = Assign Project Start", "log");
						return false;
					}
				}
				break;

			case "PROVISION START":
				if (productName.equals("Fiber Internet")) {
					IncorrectProduct = compare(ProvisionStart_ProductList_FI, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for PROVISION START", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for Provision Start Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = PROVISION START", "log");
						return false;
					}
				} else if (productName.equalsIgnoreCase("OE Network")
						|| productName.equalsIgnoreCase("Early Termination Fee")) {
					if (revision_number != 2) {
						IncorrectProduct = compare(ProvisionStart_ProductList_OENetwork, productNamefromMilestone);
					} else {
						IncorrectProduct = compare(ProvisionStart_ProductList_OENetwork_REV_EVPL,
								productNamefromMilestone);
					}

					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for PROVISION START", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for Provision Start Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = PROVISION START", "log");
						return false;
					}
				} else {
					IncorrectProduct = compare(ProvisionStart_ProductList, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for PROVISION START", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for Provision Start Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = PROVISION START", "log");
						return false;

					}
				}
				break;

			case "PROVISION COMPLETE":
				if (productName.equals("Fiber Internet")) {
					IncorrectProduct = compare(ProvisionComplete_ProductList_FI, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for PROVISION COMPLETE", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for PROVISION COMPLETE Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = PROVISION COMPLETE", "log");
						return false;
					}
				} else if (productName.equalsIgnoreCase("OE Network")
						|| productName.equalsIgnoreCase("Early Termination Fee")) {
					IncorrectProduct = compare(ProvisionComplete_ProductList_OENetwork, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for PROVISION COMPLETE", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for PROVISION COMPLETE Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = PROVISION COMPLETE", "log");
						return false;
					}
				} else {
					IncorrectProduct = compare(ProvisionComplete_ProductList, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for PROVISION COMPLETE", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for PROVISION COMPLETE Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = PROVISION COMPLETE", "log");
						return false;
					}
				}
				break;

			case "FULFILL SERVICE DESIGN START":
				if (productName.equals("Fiber Internet")) {
					IncorrectProduct = compare(FulfilServiceDesignStart_ProductList_FI, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for FULFILL SERVICE DESIGN START", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for this Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = FULFILL SERVICE DESIGN START", "log");
						return false;
					}
				} else if (productName.equals("Fiber Wavelength Connection")) {
					IncorrectProduct = compare(ConstructSiteOrder_ProductList_FW_REV, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for FULFILL SERVICE DESIGN START", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for this Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = FULFILL SERVICE DESIGN START", "log");
						return false;
					}
				} else if (productName.equalsIgnoreCase("OE Network")
						|| productName.equalsIgnoreCase("Early Termination Fee")) {
					IncorrectProduct = compare(FulfilServiceDesignStart_ProductList_OENetwork,
							productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for FULFILL SERVICE DESIGN START", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for FULFILL SERVICE DESIGN START Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = FULFILL SERVICE DESIGN START", "log");
						return false;
					}
				} else {
					IncorrectProduct = compare(FulfilServiceDesignStart_ProductList, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for FULFILL SERVICE DESIGN START", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for FULFILL SERVICE DESIGN START Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = FULFILL SERVICE DESIGN START", "log");
						return false;
					}
				}
				break;

			case "FULFILL SERVICE DSGN COMPLETE":
				if (productName.equals("Fiber Internet")) {
					IncorrectProduct = compare(FulfilServiceDesignComplete_ProductList_FI, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for FULFILL SERVICE DSGN COMPLETE", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for FULFILL SERVICE DESIGN COMPLETE Milestone",
									"log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = FULFILL SERVICE DSGN COMPLETE", "log");
						return false;
					}

				} else if (productName.equals("Fiber Wavelength Connection")) {
					IncorrectProduct = compare(ConstructSiteOrder_ProductList_FW_REV, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for FULFILL SERVICE DESIGN START", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for this Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = FULFILL SERVICE DESIGN START", "log");
						return false;
					}
				} else if (productName.equals("OE Network")) {
					IncorrectProduct = compare(FulfilServiceDesignComplete_ProductList_OENetwork,
							productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for FULFILL SERVICE DSGN COMPLETE", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for FULFILL SERVICE DESIGN COMPLETE  Milestone",
									"log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = FULFILL SERVICE DSGN COMPLETE", "log");
						return false;
					}

				} else {
					IncorrectProduct = compare(FulfilServiceDesignComplete_ProductList, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for FULFILL SERVICE DSGN COMPLETE", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for FULFILL SERVICE DESIGN COMPLETE Milestone",
									"log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = FULFILL SERVICE DSGN COMPLETE", "log");
						return false;
					}
				}
				break;
			case "CPR REQUESTED":
				if (productName.equals("Fiber Wavelength Connection")) {

					IncorrectProduct = compare(AssignProjectStart_ProductList_FW, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for CPR REQUESTED ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for this Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = CPR REQUESTED", "log");
						return false;
					}
				}
				break;
			case "INSTALL SCHEDULED":
				if (productName.equals("Fiber Wavelength Connection")) {

					IncorrectProduct = compare(ConstructSiteOrder_ProductList_FW_REV, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for INSTALL SCHEDULED ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for this Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = INSTALL SCHEDULED", "log");
						return false;
					}
				}
				break;
			case "FULFILL BILLING COMPLETE":
				if (productName.equals("Fiber Wavelength Connection")) {

					IncorrectProduct = compare(Initiate_Billing_Complete_ProductList_FW_Rev, productNamefromMilestone);
					if (IncorrectProduct.equalsIgnoreCase("")) {
						log.fw_writeLogEntry("Milestone Check is sucessful  for FULFILL BILLING COMPLETE ", "log");
						status = true;
					} else {
						if (IncorrectProduct.equalsIgnoreCase("Not Found")) {
							log.fw_writeLogEntry("No product found for this Milestone", "log");
							return false;
						}
						log.fw_writeLogEntry("Milestone in incorrect for " + IncorrectProduct, "log");
						log.fw_writeLogEntry("Actual Value=  " + Product_MilestoneValues.get(IncorrectProduct), "log");
						log.fw_writeLogEntry("Expected Value = FULFILL BILLING COMPLETE", "log");
						return false;
					}
				}

				break;

			}
		}

		return status;

	}

	/**
	 * This Function compares the Array and the List and return and unmatched element
	 * 
	 * @param ReferenceProductList
	 * @param ActualProductList
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public String compare(String[] ReferenceProductList, List<String> ActualProductList) throws InterruptedException, IOException {
		log.fw_writeLogEntry("Executing compare method ", "Info");
		log.fw_writeLogEntry("Comparing two lists ", "NA");

		String prod = "";

		List<String> prodListForAssign = Arrays.asList(ReferenceProductList);
		if (prodListForAssign.size() == ActualProductList.size()) {

			if (ActualProductList.containsAll(prodListForAssign)) {
				log.fw_writeLogEntry("All Product Matches", "NA");
			}
		} else {
			if (!ActualProductList.isEmpty()) {
				boolean flag = false;
				for (int i = 0; i < ReferenceProductList.length; i++) {
					for (int j = 0; j < ActualProductList.size(); j++) {
						if (ActualProductList.get(j).equalsIgnoreCase(ReferenceProductList[i])) {
							flag = true;
							break;

						}

					}
					if (!flag) {
						return ReferenceProductList[i];
					}

				}
			}
			if (ActualProductList.isEmpty()) {
				return "Not Found";
			}

		}
		log.fw_writeLogEntry("Step passed :- compare method  ", "NA");
		return prod;
	}

	/**
	 * This Function returns the keys where the values matches in Map
	 * 
	 * @param milestone
	 * @param map
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public List<String> getKeysfromValue(String milestone, LinkedHashMap<String, String> map) throws InterruptedException, IOException {
		log.fw_writeLogEntry("This step is used to get keys from values", "NA");
		List<String> keyValues = new ArrayList<String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(milestone))
				keyValues.add(entry.getKey());
		}
		return keyValues;
	}

	/**
	 * This function clicks on the Run Query to Refresh the CRM 
	 * 
	 * @param configuration_map_fullpath
	 * @param tab_name
	 * @param parameter
	 * @throws Exception
	 */
	public void T3_CRM_Refresh(String configuration_map_fullpath, String tab_name, List<String> parameter)
			throws Exception {

		log.fw_writeLogEntry("This step is used to refresh line items page", "NA");
		Thread.sleep(5000);

		// Click on Setting button
		fwgui.fw_check_element_existence("Line Items Menu", "NA", "xpath", "//button[@title='Line Items Menu']",
				"200,1000", 1000);

		fwgui.fw_event(configuration_map_fullpath, tab_name, "ClickJAVASCRIPT", "LineItem_LineItemsMenu", "", "",
				"7000");
		// RunQuery

		fwgui.fw_check_element_existence("Run Query", "NA", "partiallink", "Run Query", "200,1000", 1000);

		fwgui.fw_event(configuration_map_fullpath, tab_name, "ClickJAVASCRIPT", "RunQuery", "", "", "7000");

		Thread.sleep(2000);
	}

	/**
	 * This function checks whether the status of the Order is Complete or not
	 * 
	 * @throws Exception
	 */
	/*public void T3_CRM_ValidateOrderComplete() throws Exception {

		log.fw_writeLogEntry("Executing T3_CRM_ValidateOrderComplete of BusinessFunction Repository", "Info");

		log.fw_writeLogEntry("Validating if order is complete .", "NA");

		if (fwgui.fw_check_element_existence("Validate Order", "NA", "name", "s_1_1_93_0", "200,1000", 0)) {
			String status = "";

			int statuscolumn = getColumnNumber("//div[@id='gview_s_3_l']/div[2]/div/table", "l_Status");
			status = fwgui.fw_get_attribute_value("xpath", "//table[@id='s_3_l']/tbody/tr[2]/td[" + statuscolumn + "]",
					"title", 0);

			if (status.equalsIgnoreCase("Complete")) {

				log.fw_writeLogEntry("Order Status is complete", "NA");

			} else {

				log.fw_writeLogEntry("Order status is not complete . It is " + status, "NA");

			}

			log

					.fw_writeLogEntry("Step Passed :- T3_CRM_ValidateOrderComplete of BusinessFunction Repository",
							"NA");
		}

	}
*/
	/**
	 * This function checks whether the status of the Order is Complete or not
	 * 
	 * @throws Exception
	 */
	public boolean T3_CRM_ValidateOrderComplete() throws Exception {
		boolean flag = false;
		log.fw_writeLogEntry("Executing T3_CRM_ValidateOrderComplete of BusinessFunction Repository", "Info");
		log.fw_writeLogEntry("Validating if order is complete .", "NA");

		if (fwgui.fw_check_element_existence("Validate Order", "NA", "name", "s_1_1_93_0", "200,1000", 0)) {
			String status = "";

//			int statuscolumn = getColumnNumber("//div[@id='gview_s_3_l']/div[2]/div/table", "l_Status");
//			status = fwgui.fw_get_attribute_value("xpath", "//table[@id='s_3_l']/tbody/tr[2]/td[" + statuscolumn + "]",
//					"title", 0);
			status = fwgui.fw_get_attribute_value("xpath", "//td[@title='Complete']",
					"title", 0);

			if (status.equalsIgnoreCase("Complete")) {
				flag = true;
				log.fw_writeLogEntry("Order Status is complete", "NA");

			} else {
				flag = false;
				log.fw_writeLogEntry("Order status is not complete . It is " + status, "NA");
			}

			log

			.fw_writeLogEntry("Step Passed :- T3_CRM_ValidateOrderComplete of BusinessFunction Repository",
					"NA");
		}
		return flag;

	}
	
	
	
	/**
	 * This Function validates the Asset in CRM 
	 * 
	 * @throws Exception
	 */
	public void T3_CRM_ValidateAsset() throws Exception {

		log.fw_writeLogEntry("Executing T3_CRM_ValidateAsset of BusinessFunction Repository", "Info");

		log.fw_writeLogEntry("Validating the Asset in this Step .", "NA");

		// Fetch the Service Id

		int lineItemServiceIdcolumn = getColumnNumber("//div[@id='gview_s_3_l']/div[2]/div/table", "Service_Id");

		String tablexpath = "//table[@id='s_3_l']/tbody/tr[2]/td[" + lineItemServiceIdcolumn + "]";

		String lineItemServiceId = fwgui.fw_get_attribute_value("xpath", tablexpath, "title", 2000);

		System.out.println(

				"Service  ID ............... "
						+ fwgui.fw_get_text("Service  ID ", "NA", "id", "1_s_3_l_Service_Id", "NA", 2000));
		log.fw_writeLogEntry("Service  ID ............... "
				+ fwgui.fw_get_text("Service  ID ", "NA", "id", "1_s_3_l_Service_Id", "NA", 2000), "NA");

		// Click on Service Account

		int ServiceAccountcolumn = getColumnNumber("//div[@id='gview_s_3_l']/div[2]/div/table", "Service_Account");

		tablexpath = "//table[@id='s_3_l']/tbody/tr[2]/td[" + ServiceAccountcolumn + "]";

		fwgui.fw_click_button("Service Column", "NA", "xpath", tablexpath, "", 0);

		String serialNumber = "";

		serialNumber = fwgui.fw_get_attribute_value("id", "1_s_1_l_Serial_Number", "title", 2000);

		if (serialNumber.equals("")) {

			log.fw_writeLogEntry("Asset is not created", "NA");

		} else {

			if (serialNumber.equalsIgnoreCase(lineItemServiceId)) {

				log

						.fw_writeLogEntry("Asset Validation is successful. Serial Number is " + serialNumber, "NA");

			} else {

				log

						.fw_writeLogEntry("Step Failed :- Service ID and Serial # does not match ", "NA");

				log.fw_writeLogEntry("Step Failed :- Service id is " + lineItemServiceId,

						"NA");

				log.fw_writeLogEntry("Step Failed :- Serial # is " + serialNumber, "NA");

			}

		}

		log

				.fw_writeLogEntry("Step Passed :- T3_CRM_ValidateAsset of BusinessFunction Repository", "NA");

		fwgui.driver.navigate().back();

		Thread.sleep(5000);

	}

	
	/**
	 * This Method for search CRM Order under Sales Order
	 * @throws IOException 
	 * @throws InterruptedException 
	 * 
	 */
	public void T3_CRM_Search_Order() throws InterruptedException, IOException
	{
		log.fw_writeLogEntry(
				"Executing T3_CRM_Search_Order of BusinessFunction Repository", "Info");
		
		// Click on the Sales Order Tab
		fwgui.fw_click_element_using_javascript("Sales Order Tab", "NA", "xpath", "//div[@title='First Level View Bar']//li[7]/a", "", 5000);
		Thread.sleep(2000);
		// Enter Sales Order Value
		fwgui.fw_enter_data_into_text_field("Sales Order Value", "NA", "xpath", "//input[@aria-label='Find']", "Order #", 2000);
		
		// Enter Order ID
		fwgui.fw_enter_data_into_text_field("Sales Order Value", "NA", "xpath", "//input[@aria-label='Starting with']", crm_orderid, 2000);
		
		// Click on the Go
		fwgui.fw_click_element_using_javascript("Sales Go Button", "NA", "xpath", "//div[@class='AppletButtons siebui-applet-buttons']//button[@aria-label='Sales Orders:Go']", "", 2000);
		
		Thread.sleep(3000);
	}
	
	/**
	 * This Function saves the billingCycle , Purchase Date  and Override Price after order is completed
	 *
	 * @throws Exception
	 */
	
	public void T3_CRM_Save_BillingCycle_PurchaseDate_OverridePrice_AfterComplete() throws Exception {

		log.fw_writeLogEntry(
				"Executing T3_CRM_Save_BillingCycle_PurchaseDate_OverridePrice_AfterComplete of BusinessFunction Repository",
				"Info");
		String[] locator;
		
		// Call Search Order Method
		T3_CRM_Search_Order();
		
		// Click on the Order ID
		fwgui.fw_click_element_using_javascript("Sales Go Button", "NA", "xpath", "//*[@name='Order Number']", "", 2000);
		
		Thread.sleep(5000);
		
		// save the start date
		List<WebElement> elementsexpand = fwgui.fw_get_webelements_object("Expand Button", "NA", "css",
				"span[title='Sales Order:Show more']", "", 1000);
		Thread.sleep(5000);
		if (elementsexpand!=null && elementsexpand.size() >= 1) {
			//fwgui.fw_click_button("Expand Button", "NA", "css", "span[title='Sales Order:Show more']", "", 2000);
			fwgui.fw_click_element_using_javascript("Expand Button", "NA", "css", "span[title='Sales Order:Show more']", "", 2000);
		}

		billingCreatedDate = fwgui.fw_get_attribute_value("name", "s_1_1_114_0", "value", 2000);
		System.out.println("Billing created date = " + billingCreatedDate);

		// save the purchase date

		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = (Date) formatter.parse(billingCreatedDate);
		// add 2 years
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		Date newDate = cal.getTime();
		// Save in string
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
		purchaseDate = newFormat.format(newDate);
		System.out.println("Purchase date = billing date =" + purchaseDate);
		// overridePrice
		overridePrice = "10";
		
		// Click on the Line Item Tab
		fwgui.fw_click_element_using_javascript("Sales Go Button", "NA", "xpath", "//a[text()='Line Items']", "", 2000);
		Thread.sleep(2000);
		
		// save the end date
		String summary = null;
		// Get number of columns In table.
		List<WebElement> elements = fwgui.driver.findElements(By.tagName("table"));
		// fwgui.fw_get_webelements_object("Summary Tabele", "table", "NA",
		// "NA", "NA", 2000);
		// fwgui.driver.findElements(By.tagName("table"));
		for (WebElement element : elements) {
			if (element.getAttribute("summary").contains("Line Item")) {
				summary = element.getAttribute("summary");
			}
		}

		String columnxpath = "//table[@summary='" + summary + "']/tbody/tr[2]/td";
		String headertablexpath = "//div[@class='ui-jqgrid-view']/div[2]/div/table";

		int billingAccountColumn = getColumnNumber(headertablexpath, "Billing_Account");

		fwgui.fw_check_element_existence("billingAccount Column", "NA", "xpath",
				columnxpath + "[" + billingAccountColumn + "]", "200,1000", 19000);
		fwgui.fw_click_button("billingAccount column", "NA", "xpath", columnxpath + "[" + billingAccountColumn + "]",
				"NA", 3000);

		Thread.sleep(3000);

		// fetch the billing cycle
		int cycledate = Integer
				.parseInt(fwgui.fw_get_text("Bill cycle ", "NA", "id", "1_s_2_l_Bill_Cycle", "NA", 2000));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, cycledate);
		Date nextMonthDay = calendar.getTime();

		billEndDate = newFormat.format(nextMonthDay);

		System.out.println("billing end date  = " + billEndDate);

		fwgui.driver.navigate().back();

	}

	/**
	 * This Function fetches the stop time and pooling time from config file
	 * @param scenarioTimer
	 * @return
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	private int getStopTime(Timer_T3 scenarioTimer) throws IOException, InterruptedException {
		log.fw_writeLogEntry("This step is used to introduce pooling time .", "NA");
		int iStopTime = 0;
		Properties prop = new Properties();
		InputStream inputStream = null;
		inputStream = new FileInputStream("config\\config.properties");
		prop.load(inputStream);
		inputStream.close();
		iStopTime = Integer.parseInt(prop.getProperty("stoptime"));
		scenarioTimer.setPollingTime(Integer.parseInt(prop.getProperty("poolingTime")));
		return iStopTime;
	}
	
	/**
	 * This Function saves the billingCycle , Purchase Date  and Override Price
	 *
	 * @throws Exception
	 */
	public void T3_CRM_Save_BillingCycle_PurchaseDate_OverridePrice() throws Exception {

		log.fw_writeLogEntry(
				"Executing T3_CRM_Save_BillingCycle_PurchaseDate_OverridePrice of BusinessFunction Repository", "Info");
		String[] locator;

		List<WebElement> elementsexpand = fwgui.fw_get_webelements_object("Expand Button", "NA", "css",
				"span[title='Sales Order:Show more']", "", 1000);
		Thread.sleep(5000);
		if (elementsexpand!=null&&elementsexpand.size() >= 1) {
			fwgui.fw_click_button("Expand Button", "NA", "css", "span[title='Sales Order:Show more']", "", 2000);
		}

		// save the start date
		Thread.sleep(3000);
		billingCreatedDate = fwgui.fw_get_attribute_value("name", "s_1_1_114_0", "value", 1000);

		System.out.println("Billing created date = " + billingCreatedDate);

		// save the purchase date

		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = (Date) formatter.parse(billingCreatedDate);
		// add 2 years
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, 2);
		cal.add(Calendar.DATE, -5);
		Date newDate = cal.getTime();
		// Save in string
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
		purchaseDate = newFormat.format(newDate);
		System.out.println("Purchase date = billing date +2 yrs =" + purchaseDate);
		// overridePrice
		overridePrice = "10";

		// save the end date
		String summary = null;
		// Get number of columns In table.
		List<WebElement> elements = fwgui.driver.findElements(By.tagName("table"));
		for (WebElement element : elements) {
			if (element.getAttribute("summary").contains("Line Item")) {
				summary = element.getAttribute("summary");
			}
		}

		String columnxpath = "//table[@summary='" + "Line Items" + "']/tbody/tr[2]/td";
		String headertablexpath = "//div[@class='ui-jqgrid-view']/div[2]/div/table";

		int billingAccountColumn = getColumnNumber(headertablexpath, "Billing_Account");

		fwgui.fw_check_element_existence("billingAccount Column", "NA", "xpath",
				columnxpath + "[" + billingAccountColumn + "]", "200,1000", 1000);
		fwgui.fw_click_button("billingAccount column", "NA", "xpath", columnxpath + "[" + billingAccountColumn + "]",
				"NA", 3000);
		Thread.sleep(3000);

		// fetch the billing cycle
		int cycledate = Integer.parseInt(fwgui.fw_get_text("cycle Date", "NA", "id", "1_s_2_l_Bill_Cycle", "", 1000));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, cycledate);
		Date nextMonthDay = calendar.getTime();

		billEndDate = newFormat.format(nextMonthDay);

		System.out.println("billing end date  = " + billEndDate);

		fwgui.driver.navigate().back();

	}

	
	/**
	 * This function calls the validation method inside pooling loop
	 * 
	 * @param serviceAccountNumber
	 * @throws Exception
	 */
	public void T3_BRM_Validate_BillingCycle(String serviceAccountNumber) throws Exception {

		log.fw_writeLogEntry("Executing T3_BRM_Validate_BillingCycle of BusinessFunction Repository", "Info");

		Timer_T3 scenarioTimer = new Timer_T3();

		scenarioTimer.setStartTime(0);
		int iStopTime = getStopTime(scenarioTimer);
		boolean validate, AddressMatch = false;

		scenarioTimer.inTime();
		Thread.sleep(5000);

		List<String> querydetails = T3_BRM_Find_Query_Detail("BillingCycleQuery");

		while (!scenarioTimer.isTimeout(iStopTime)) {

			validate = T3_BRM_Verify_Billing_Cycle(querydetails, billingCreatedDate, billEndDate, serviceAccountNumber);
			if (validate) {
				// Exit if validation is true
				log.fw_writeLogEntry("Billing cycle matches successfully", "NA");

				AddressMatch = true;
				break;
			}
			// Wait till pooling time
			log.fw_writeLogEntry("Delay set to " + scenarioTimer.getPollingTime() + " seconds", "NA");

			scenarioTimer.delay(scenarioTimer.getPollingTime());
		}

		if (!AddressMatch) {
			throw new Exception("Billing Cycle did not match.");
		}

		log.fw_writeLogEntry("Step Passed :- T3_BRM_Validate_BillingCycle of BusinessFunction Repository", "NA");

	}

	/**
	 * This function appends the account Name with current date and time and saving the names
	 * 
	 * @param accountName
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public String T3_CRM_AccountName(String accountName) throws InterruptedException, IOException {
		// Append the accountName with current date and time
		log.fw_writeLogEntry("Appending the accountName with current date and time", "NA");
		Date dt = new Date();
		final DateFormat dateFormat = new SimpleDateFormat("ddMMYYYYHHmmss");
		accountName = accountName.replaceAll("_", dateFormat.format(dt));
		if (!accountName.contains("Opportunity")) {
			if (accountName.endsWith("2")) {
				billingAccount = accountName;
				log.fw_writeLogEntry("Creating Account -  " + accountName, "NA");
			} else if (accountName.endsWith("3")) {
				serviceAccount1 = accountName;
				log.fw_writeLogEntry("Creating Account -  " + accountName, "NA");
			} else if (accountName.endsWith("4")) {
				serviceAccount2 = accountName;
				log.fw_writeLogEntry("Creating Account -  " + accountName, "NA");
			} else {
				customerAccount = accountName;
				log.fw_writeLogEntry("Creating Account -  " + accountName, "NA");
			}
		}

		return accountName;
	}

	/**
	 * This Function is used to go to Billing Account to create  Service Account2 
	 * @throws Exception
	 */
	public void T3_CRM_GotoBillingAccountForServiceAccount2() throws Exception {
		Thread.sleep(5000);
		log.fw_writeLogEntry("Executing T3_CRM_GotoBillingAccountForServiceAccount2 of BusinessFunction Repository",
				"Info");
		log.fw_writeLogEntry("Go to Billing Account to create  Service Account2 ", "NA");

		fwgui.fw_click_button("Billing Account", "NA", "css", "td[title=" + billingAccount + "]", "NA", 5000);

	}

	/**
	 * This function fetches the service id and calls the validation method inside pooling loop
	 * 
	 * @param configuration_map_fullpath
	 * @param tab_name
	 * @param parameter
	 * @throws Exception
	 */
	public void T3_BRM_Validate_PurchaseCycleandOverridePrice(String configuration_map_fullpath, String tab_name,
			List<String> parameter) throws Exception {

		log.fw_writeLogEntry("Executing T3_BRM_Validate_PurchaseCycleandOverridePrice of BusinessFunction Repository",
				"Info");

		Timer_T3 scenarioTimer = new Timer_T3();

		scenarioTimer.setStartTime(0);
		int iStopTime = getStopTime(scenarioTimer);
		boolean date_Price_Match = false;
		scenarioTimer.inTime();
		Thread.sleep(5000);
		// Click on Setting button
		Thread.sleep(5000);

		fwgui.fw_check_element_existence("LineItem_LineItemsMenu", "NA", "css", "button[title='Line Items Menu']",
				"200,1000", 1000);

		fwgui.fw_event(configuration_map_fullpath, tab_name, "ClickButton", "LineItem_LineItemsMenu", "", "", "5000");
		// renumber
		Thread.sleep(5000);

		fwgui.fw_check_element_existence("Renumber", "NA", "partiallink", "Renumber", "200,1000", 1000);

		fwgui.fw_click_button("Renumber", "NA", "partiallink", "Renumber", "", 2000);
		// Thread.sleep(2000);
		WebDriverWait Alertwait = new WebDriverWait(fwgui.driver,
				5 /* timeout in seconds */);

		try {
			Alertwait.until(ExpectedConditions.alertIsPresent());
			fwgui.fw_accept_alert();

		} catch (TimeoutException eTO) {

		}

		String tablexpath = "//table[@summary='Line Items']";

		// divided Xpath In three parts to pass Row_count and Col_count
		// values.
		String first_part = "//table[@summary='Line Items']/tbody/tr[";
		String second_part = "]/td[";
		String third_part = "]";
		String headertablexpath = "//div[@class='ui-jqgrid-view']/div[2]/div/table";
		/* WebDriverWait waitTable = new WebDriverWait(fwgui.driver, 60); */
		try {
			fwgui.fw_check_element_existence("Table", "NA", "xpath", tablexpath, "200,1000", 60000);

		} catch (Exception e) {
			first_part = "//table[@id='s_3_l']/tbody/tr[";
			tablexpath = "//table[@id='s_3_l']";

			fwgui.fw_check_element_existence("TAble", "NA", "xpath", tablexpath, "200,1000", 1000);

		}

		WebElement table = (WebElement) fwgui.fw_get_webelements_object("Table", "NA", "xpath", tablexpath, "", 1000)
				.get(0);
		// Now get number of rows from the table
		int numOfRow = table.findElements(By.tagName("tr")).size();

		// take the 19th column values

		int serviceIdColumn = getColumnNumber(headertablexpath, "Service_Id");
		int productcolumn = getColumnNumber(headertablexpath, "Product");
		;
		String final_xpath_serviceId;
		String final_xpath_product;

		String product = null;
		int i;
		String serviceId;

		List<String> querydetails = T3_BRM_Find_Query_Detail("PurchaseDateAndOverridePriceQuery");

		while (!scenarioTimer.isTimeout(iStopTime)) {
			// Thread.sleep(5000);
			for (i = 2; i <= numOfRow; i++) {
				// Prepared final xpath of specific cell as per values of i and
				// j.
				final_xpath_serviceId = first_part + i + second_part + serviceIdColumn + third_part;
				final_xpath_product = first_part + i + second_part + productcolumn + third_part;
				System.out.println("final_xpath_product : " + final_xpath_product);
				
				fwgui.fw_check_element_existence("Table", "NA", "xpath", final_xpath_product, "200,1000", 1000);

				// Will retrieve value from located cell

				product = fwgui.fw_get_text("Product NAme", "NA", "xpath", final_xpath_product, "", 1000);
				System.out.println("product : " + product);

				for (String productName : parameter) {
					System.out.println("productName : " + productName);
					if (productName.equalsIgnoreCase(product)) {
						fwgui.fw_check_element_existence("Service ID", "NA", "xpath", final_xpath_serviceId, "200,1000",
								1000);
						serviceId = fwgui.fw_get_text("serviceId", "NA", "xpath", final_xpath_serviceId, "", 1000);
						System.out.println("ProductName" + productName);
						System.out.println("ServiceId" + serviceId);
						boolean validate = T3_BRM_Verify_PurchaseDateAndOverridePriceQuery(querydetails, purchaseDate,
								overridePrice, serviceId);
						if (validate) {
							// Exit if validation is true
							log.fw_writeLogEntry(
									"purchase Date and override Price matches successfully for " + productName, "NA");

							date_Price_Match = true;

						} else {// Wait till pooling time
							log.fw_writeLogEntry("purchase Date and override Price did not match  for " + productName,
									"NA");
							log.fw_writeLogEntry("Delay set to " + scenarioTimer.getPollingTime() + " seconds", "NA");

							scenarioTimer.delay(scenarioTimer.getPollingTime());
						}

					} else {
						System.out.println("Did not match");
					}

				}
			}
			if (date_Price_Match) {
				break;
			}
		}

		if (!date_Price_Match) {
			throw new Exception("purchase Date and override Price did not match.");
		}

		log.fw_writeLogEntry(
				"Step Passed :- T3_BRM_Validate_PurchaseCycleandOverridePrice of BusinessFunction Repository", "NA");

	}

	/**
	 * This function fetches the query details by passsing te query name 
	 * 
	 * @param userQueryName
	 * @return
	 * @throws Exception
	 */
	public List<String> T3_BRM_Find_Query_Detail(String userQueryName) throws Exception {
		Properties prop = new Properties();
		InputStream inputStream = null;
		inputStream = new FileInputStream("config\\config.properties");
		prop.load(inputStream);
		inputStream.close();
		String dataMapPath = prop.getProperty("DatabasePath");

		// using apache poi jar to read excel file
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(dataMapPath));
		XSSFSheet sheet = wb.getSheetAt(0);

		Row row;
		String QueryName;
		String DBConnectionString;
		String UserName;
		String Password;
		String Query;
		String SrNo;

		Iterator<Row> rowIterator = sheet.iterator();

		// to store data obtained from dataMap along with businessFunctionName
		// and arguments
		List<String> arguementList = new ArrayList<String>();

		// iterating over dataMAp excel file
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			if (row.getRowNum() != 0) {

				QueryName = row.getCell(Constants.queryNameCellValueinDataMap).getStringCellValue();
				DBConnectionString = row.getCell(Constants.dbConnectionStringCellValueinDataMap).getStringCellValue();
				UserName = row.getCell(Constants.userNameCellValueinDataMap).getStringCellValue();
				Password = row.getCell(Constants.passwordCellValueinDataMap).getStringCellValue();
				Query = row.getCell(Constants.queryCellValueinDataMap).getStringCellValue();

				if (QueryName.equalsIgnoreCase(userQueryName)) {

					arguementList.add(DBConnectionString);
					arguementList.add(UserName);
					arguementList.add(Password);
					arguementList.add(Query);

				}
			}
		}
		return arguementList;

	}

	/**
	 * This function validates the Billing cycle
	 * 
	 * @param queryDetails
	 * @param billStartDate
	 * @param billEndDate
	 * @param accountNumber
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public boolean T3_BRM_Verify_Billing_Cycle(List<String> queryDetails, String billStartDate, String billEndDate,
			String accountNumber) throws ClassNotFoundException, SQLException, ParseException {
		String acc = "'" + accountNumber + "'";
		String dbConnectionString = queryDetails.get(0);
		String userName = queryDetails.get(1);
		String password = queryDetails.get(2);
		String q1 = queryDetails.get(3);

		String query = q1 + acc;
		System.out.println(query);

		boolean flag = false;
		OracleDb db = new OracleDb();
		// Checking connection
		Connection conn = db.fw_DBOracle_MakeConnection(dbConnectionString, userName, password);
		// Fetching data from database
		ResultSet rs = db.fw_DBOracle_GetRecordSet(conn, query);
		String billStartDate_DB;
		String billEndDate_DB;
		// change the fetched start date to common format
		DateFormat formatter3 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		Date date3 = (Date) formatter3.parse(billStartDate);
		// save in string
		DateFormat formatter23 = new SimpleDateFormat("yyyy-MM-dd");
		billStartDate = formatter23.format(date3);

		try {
			while (rs.next()) { // change the fetched start date to common
								// format
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = (Date) formatter.parse(rs.getString(1));
				// save in string
				DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
				billStartDate_DB = formatter2.format(date);

				// change the fetched end date to common format
				Date date2 = (Date) formatter.parse(rs.getString(2));

				billEndDate_DB = formatter2.format(date2);

				System.out.println("Bill start date " + billStartDate);
				System.out.println("Bill start date DB " + billStartDate_DB);
				System.out.println("billEndDate" + billEndDate);
				System.out.println("billEndDate DB " + billEndDate_DB);

				if (billStartDate.equalsIgnoreCase(billStartDate_DB) && billEndDate.equalsIgnoreCase(billEndDate_DB)) {
					flag = true;
					System.out.println("Billing cycle verified successfully");
				}
			}
		} finally {
			db.fw_DBOracle_Closeconnection(conn);
		}
		return flag;
	}

	/**
	 * This function validates the Purchase date and override Price
	 * 
	 * @param queryDetails
	 * @param purchaseDate
	 * @param overridePrice
	 * @param accountNumber
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public boolean T3_BRM_Verify_PurchaseDateAndOverridePriceQuery(List<String> queryDetails, String purchaseDate,
			String overridePrice, String accountNumber) throws ClassNotFoundException, SQLException, ParseException {
		String sno = "'" + accountNumber + "'";
		String dbConnectionString = queryDetails.get(0);
		String userName = queryDetails.get(1);
		String password = queryDetails.get(2);
		String q1 = queryDetails.get(3);

		String query = q1 + sno;
		System.out.println(query);

		boolean flag = false;
		OracleDb db = new OracleDb();
		// Checking connection
		Connection conn = db.fw_DBOracle_MakeConnection(dbConnectionString, userName, password);
		// Fetching data from database
		ResultSet rs = db.fw_DBOracle_GetRecordSet(conn, query);
		String purchaseDate_DB;
		try {
			while (rs.next()) {
				// change the fetched start date to common format
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = (Date) formatter.parse(rs.getString(1));
				DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
				// save in string
				purchaseDate_DB = formatter2.format(date);
				Double overridePricefromDB = Double.parseDouble(rs.getString(2));
				System.out.println("purchaseDate" + purchaseDate);
				System.out.println("purchaseDate DB " + purchaseDate_DB);
				System.out.println("overridePrice" + overridePrice);
				System.out.println("overridePrice DB " + overridePricefromDB);

				if (purchaseDate.equalsIgnoreCase(purchaseDate_DB)
						&& overridePrice.equalsIgnoreCase(Integer.toString(overridePricefromDB.intValue()))) {
					System.out.println(rs.getString(1) + "," + rs.getString(2));
					flag = true;
					System.out.println("PurchaseDate and override price verified successfully");
				}
			}
		} finally {
			db.fw_DBOracle_Closeconnection(conn);
		}
		return flag;
	}
	
	public void T3_Verify_Plan_TaskName_BeforePO(String configuration_map_fullpath, List<String> parameter, String tab_name) throws Exception {

		String locator = "";
		log.fw_writeLogEntry("Executing T3_Verify_Plan_TaskName_PO of T3Functions.Java", "Info");
		log.fw_writeLogEntry("Verify the Plan Taskname PO in OSM. ", "NA");

		Thread.sleep(2000);
		if (parameter.isEmpty()) {
			log.fw_writeLogEntry("Step Failed :- No TaskName present in Function Data Map to verify the OSM. ", "NA");

		}
		String OSMtask = "";
		boolean taskfound = false;
		List<String> OpenTask = new ArrayList<String>();
		OpenTask.add(parameter.get(0));
		OpenTask.add(parameter.get(1));
		//for (int i = 2; i < parameter.size(); i++) {
		for (int i = 1; i <= parameter.size(); i++) {
			// Refresh and Open Task
			if (parameter.get(0).equalsIgnoreCase("SomProvisionOE")) {
				OpenTask = parameter;
			}
			T3_OSM_Refresh_OpenTask(configuration_map_fullpath, parameter, tab_name);

			// Validate Task Name
			fwgui.fw_event(configuration_map_fullpath, tab_name, "GetText", "OSM_TaskName", "", "", "2000");
			OSMtask = fwgui.return_get_text;
			taskfound = false;
			Thread.sleep(2000);
			
			String buildService_serviceProfile = null;
			if (productName.equalsIgnoreCase("E-Access") || productName.equalsIgnoreCase("OE Network")) {
				buildService_serviceProfile = "L3_PTP";
			} else {
				buildService_serviceProfile = "LBI";
			}
			
			
			for (String taskName : parameter) {
				taskName = taskName.trim();
				log.fw_writeLogEntry(taskName, "NA");
				if (taskName.contains("Validate Order Task") || taskName.contains("Validate Design Task")
						|| taskName.contains("SomProvision_NonOE") || taskName.contains("FulfillServiceDesignOrder")
						|| taskName.equals("")) {
					continue;
				}

				Boolean Status = false;
				fwgui.fw_event(configuration_map_fullpath, tab_name, "GetText", "OSM_TaskName", "", "", "1000");

				String taskNamefetched = fwgui.return_get_text;
				if (taskNamefetched.equalsIgnoreCase(taskName)) {
					Status = true;
				}

				if (Status) {
					taskfound = true;
					log.fw_writeLogEntry("TaskName " + taskName + " is  present ", "NA");

					if (taskName.equalsIgnoreCase("Build Service")) {
						fwgui.fw_event(configuration_map_fullpath, tab_name, "SelectListValueByVisibleText",
								"OSM_ServiceProfile", "L3_PTP", "", "3000");
					}
					
					if (taskName.equalsIgnoreCase("Validate Order Task")) {						
						
						//click for ValidateDesignISPReq flag
						locator = "//div[@id='aazone.root']/div[4]/div/div[2]/div/div[5]/table/tbody/tr[2]/td/div/div[2]/label/input";
						//wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));

						//WebElement element1 = DriverInstance.findElement(By.xpath(locator));
						//JavascriptExecutor executor1 = (JavascriptExecutor) DriverInstance;
						fwgui.fw_click_button("ValidateDesignISPReq", "NA", "xpath", locator, "NA", 2000);
						//executor1.executeScript("arguments[0].click();", element1);*/
							
							
							//click for ValidateDesignCoreReq
						locator = "//div[@id='group-1443083192:51']/div[12]/table/tbody/tr[2]/td/div/div[2]/label/input";
						
						WebDriverWait wait3 = new WebDriverWait(fwgui.driver, 190);
						wait3.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
						WebElement element = fwgui.driver.findElement(By.xpath(locator));
						JavascriptExecutor executor = (JavascriptExecutor) fwgui.driver;
						executor.executeScript("arguments[0].click();", element);
												
							//click for ValidateDesignCompxEng flag
						locator = "//div[@id='group-1443083192:51']/div[13]/table/tbody/tr[2]/td/div/div[2]/label/input";
						WebDriverWait wait5 = new WebDriverWait(fwgui.driver, 190);
						wait5.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
						WebElement element3 = fwgui.driver.findElement(By.xpath(locator));
						JavascriptExecutor executor3 = (JavascriptExecutor) fwgui.driver;
						executor3.executeScript("arguments[0].click();", element3);
						
						
						//click on save button
						locator = "saveButton";
						WebDriverWait wait4 = new WebDriverWait(fwgui.driver, 190);
						wait4.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator)));
						WebElement element2 = fwgui.driver.findElement(By.id(locator));
						JavascriptExecutor executor2 = (JavascriptExecutor) fwgui.driver;
						executor2.executeScript("arguments[0].click();", element2);
						
					}	
						
						
					if (taskName.equalsIgnoreCase("Validate Design Task")) {

						// Set 0 to GLID textbox

						List<WebElement> elements = fwgui.driver.findElements(By.tagName("textarea"));
						for (WebElement element : elements) {
							if (element.getText().equalsIgnoreCase("NOT_FOUND")) {
								element.clear();
								Thread.sleep(2000);
								element.sendKeys("0");
								Thread.sleep(2000);
							}
						}							
							
							
						//Click on ValidateOrderTask_ConstPO flag
						/*locator = fromObjectMap.get("ValidateOrderTask_ConstPO").split("=", 2);
						//WebDriverWait wait2 = new WebDriverWait(DriverInstance, 190);	
						Thread.sleep(10000);
						wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator[1])));
						WebElement element1 = DriverInstance.findElement(By.xpath(locator[1]));
						JavascriptExecutor executor1 = (JavascriptExecutor) DriverInstance;
						executor1.executeScript("arguments[0].click();", element1);*/
						
						
						//Click on ValidateOrderTask_EquipPO flag
						/*locator = fromObjectMap.get("ValidateOrderTask_EquipPO").split("=", 2);
						WebDriverWait wait3 = new WebDriverWait(DriverInstance, 190);
						wait3.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator[1])));
						WebElement element = DriverInstance.findElement(By.xpath(locator[1]));
						JavascriptExecutor executor = (JavascriptExecutor) DriverInstance;
						executor.executeScript("arguments[0].click();", element);*/
						
						
						////click on save button
						/*locator = fromObjectMap.get("Save").split("=", 2);
						WebDriverWait wait4 = new WebDriverWait(DriverInstance, 190);
						wait4.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator[1])));
						WebElement element2 = DriverInstance.findElement(By.id(locator[1]));
						JavascriptExecutor executor2 = (JavascriptExecutor) DriverInstance;
						executor2.executeScript("arguments[0].click();", element2);*/
						}
					}
					
					
					
					

					T3_OSM_Update_Task_Using_Update_Button(configuration_map_fullpath, tab_name);

					log.fw_writeLogEntry("Updating " + taskName + " ", "NA");
					break;
				}
			}
			if (!taskfound) {
				log.fw_writeLogEntry("Step Failed :- Task " + OSMtask + " is not present in functionDataMap list ","NA");
				throw new Exception("************Task " + OSMtask + " is not present in functionDataMap list ");
			}
		

		log.fw_writeLogEntry("Step Passed :- T3_Verify_Plan_TaskName of T3Functions.Java", "NA");

	}
	
	public void T3_Verify_Plan_TaskName_AfterPO(String configuration_map_fullpath, List<String> parameter, String tab_name)
			throws Exception {

		log.fw_writeLogEntry("Executing T3_Verify_Plan_TaskName_PO of T3Functions.Java", "Info");
		log.fw_writeLogEntry("Verify the Plan Taskname PO in OSM. ", "NA");

		Thread.sleep(2000);
		if (parameter.isEmpty()) {
			log.fw_writeLogEntry("Step Failed :- No TaskName present in Function Data Map to verify the OSM. ", "NA");

		}
		String OSMtask="";
		boolean taskfound = false;
		List<String> OpenTask = new ArrayList<String>();
		OpenTask.add(parameter.get(0));
		OpenTask.add(parameter.get(1));
		for (int i = 2; i < parameter.size(); i++) {
			// Refresh and Open Task
			if (parameter.get(0).equalsIgnoreCase("SomProvisionOE")) {
				OpenTask = parameter;
			}
			T3_OSM_Refresh_OpenTask(configuration_map_fullpath, parameter, tab_name);

			// Validate Task Name
			fwgui.fw_event(configuration_map_fullpath, tab_name, "GetText", "OSM_TaskName", "", "", "2000");
			OSMtask = fwgui.return_get_text;
			taskfound = false;
			Thread.sleep(2000);
			
			String buildService_serviceProfile = null;
			if (productName.equalsIgnoreCase("E-Access") || productName.equalsIgnoreCase("OE Network")) {
				buildService_serviceProfile = "L3_PTP";
			} else {
				buildService_serviceProfile = "LBI";
			}
			
			
			for (String taskName : parameter) {
				taskName = taskName.trim();
				log.fw_writeLogEntry(taskName, "NA");
				if (taskName.contains("PlanNetwork") || taskName.contains("ConstructSiteOrder")
						|| taskName.contains("SomProvision_NonOE") || taskName.contains("FulfillServiceDesignOrder")
						|| taskName.equals("")) {
					continue;
				}

				Boolean Status = false;
				fwgui.fw_event(configuration_map_fullpath, tab_name, "GetText", "OSM_TaskName", "", "", "1000");

				String taskNamefetched = fwgui.return_get_text;
				if (taskNamefetched.equalsIgnoreCase(taskName)) {
					Status = true;
				}

				if (Status) {
					taskfound = true;
					log.fw_writeLogEntry("TaskName " + taskName + " is  present ", "NA");

					if (taskName.equalsIgnoreCase("Build Service")) {
						fwgui.fw_event(configuration_map_fullpath, tab_name, "SelectListValueByVisibleText",
								"OSM_ServiceProfile", "L3_PTP", "", "3000");
					}
					
					
					
					
					
						if (taskName.equalsIgnoreCase("Validate Design Task")) {						
						
						//click for ValidateDesignISPReq flag
						/*locator = fromObjectMap.get("ValidateDesignISPReq2").split("=", 2);

						wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator[1])));

						WebElement element1 = DriverInstance.findElement(By.xpath(locator[1]));
						JavascriptExecutor executor1 = (JavascriptExecutor) DriverInstance;
						executor1.executeScript("arguments[0].click();", element1);*/
							
							
							//click for ValidateDesignCoreReq
						/*locator = fromObjectMap.get("ValidateDesignCoreReq2").split("=", 2);
						WebDriverWait wait3 = new WebDriverWait(DriverInstance, 190);
						wait3.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator[1])));
						WebElement element = DriverInstance.findElement(By.xpath(locator[1]));
						JavascriptExecutor executor = (JavascriptExecutor) DriverInstance;
						executor.executeScript("arguments[0].click();", element);
						
*/						
							//click for ValidateDesignCompxEng flag
						/*locator = fromObjectMap.get("ValidateDesignCompxEng2").split("=", 2);
						WebDriverWait wait5 = new WebDriverWait(DriverInstance, 190);
						wait5.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator[1])));
						WebElement element3 = DriverInstance.findElement(By.xpath(locator[1]));
						JavascriptExecutor executor3 = (JavascriptExecutor) DriverInstance;
						executor3.executeScript("arguments[0].click();", element3);*/
						
						
						//click on save button
						/*locator = fromObjectMap.get("Save").split("=", 2);
						WebDriverWait wait4 = new WebDriverWait(DriverInstance, 190);
						wait4.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator[1])));
						WebElement element2 = DriverInstance.findElement(By.id(locator[1]));
						JavascriptExecutor executor2 = (JavascriptExecutor) DriverInstance;
						executor2.executeScript("arguments[0].click();", element2);*/
						
						}
						
						
						
						if (taskName.equalsIgnoreCase("Validate Order Task")) {

							// Set 0 to GLID textbox

							List<WebElement> elements = fwgui.driver.findElements(By.tagName("textarea"));
							for (WebElement element : elements) {
								if (element.getText().equalsIgnoreCase("NOT_FOUND")) {
									element.clear();
									Thread.sleep(2000);
									element.sendKeys("0");
									Thread.sleep(2000);
								}
							}
							
							
							
							
							//Click on ValidateOrderTask_ConstPO flag
							/*locator = fromObjectMap.get("ValidateOrderTask_ConstPO2").split("=", 2);
							//WebDriverWait wait2 = new WebDriverWait(DriverInstance, 190);	
							Thread.sleep(10000);
							wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator[1])));
							WebElement element1 = DriverInstance.findElement(By.xpath(locator[1]));
							JavascriptExecutor executor1 = (JavascriptExecutor) DriverInstance;
							executor1.executeScript("arguments[0].click();", element1);*/
							
							
							//Click on ValidateOrderTask_EquipPO flag
							/*locator = fromObjectMap.get("ValidateOrderTask_EquipPO2").split("=", 2);
							WebDriverWait wait3 = new WebDriverWait(DriverInstance, 190);
							wait3.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator[1])));
							WebElement element = DriverInstance.findElement(By.xpath(locator[1]));
							JavascriptExecutor executor = (JavascriptExecutor) DriverInstance;
							executor.executeScript("arguments[0].click();", element);*/
							
							
							////click on save button
							/*locator = fromObjectMap.get("Save").split("=", 2);
							WebDriverWait wait4 = new WebDriverWait(DriverInstance, 190);
							wait4.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator[1])));
							WebElement element2 = DriverInstance.findElement(By.id(locator[1]));
							JavascriptExecutor executor2 = (JavascriptExecutor) DriverInstance;
							executor2.executeScript("arguments[0].click();", element2);*/
							}
						}
						
					
					
					

					T3_OSM_Update_Task_Using_Update_Button(configuration_map_fullpath, tab_name);

					log.fw_writeLogEntry("Updating " + taskName + " ", "NA");
					break;
				}
			}
			if (!taskfound) {
				log.fw_writeLogEntry("Step Failed :- Task " + OSMtask + " is not present in functionDataMap list ",
						"NA");
				throw new Exception("************Task " + OSMtask + " is not present in functionDataMap list ");
			}
		

		log.fw_writeLogEntry("Step Passed :- T3_Verify_Plan_TaskName of T3Functions.Java", "NA");

	}
	
	/**
	 * 
	 * @param configuration_map_fullpath
	 * @param tab_name
	 * @param noOfLoop
	 * @throws Exception
	 */
	public void T3_OSM_CompleteTasks(String configuration_map_fullpath, String tab_name,int noOfLoop) throws Exception
	{
		log.fw_writeLogEntry("Executing T3_Verify_Plan_TaskName_WithPO of T3Functions.Java", "Info");
		log.fw_writeLogEntry("Verify the Plan Taskname in OSM. ", "NA");
		String provisiontask ="";
		String orderState ="";
		Thread.sleep(2000);
		String tablexpath = "//div[@id='aazone.queryResults']/form/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table";
		// Grab the table
		WebElement table = fwgui.fw_get_webelements_object("OSM search table element", "NA", "xpath", tablexpath, "", 1000).get(0);

		// divided Xpath In three parts to pass Row_count and Col_count values.
		String first_part = "//div[@id='aazone.queryResults']/form/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[";
		String second_part = "]/td[";
		String third_part = "]";
		int taskColumn = 16;
		int orderStateColumn =9;
		String refreshButton = "//input[@value='Refresh']";
		boolean taskPresent =false;
		for(int i=1;i<=noOfLoop;i++)
		{
			for (int j=1;j<5;j++){
					fwgui.fw_click_element_using_javascript("Table Action", "NA", "xpath", refreshButton, "NA", 1000);
					Thread.sleep(2000);
			}			
			orderState = fwgui.fw_get_text("ProvisionTask Value", "NA", "xpath",
					first_part + 2 + second_part + orderStateColumn + third_part, "NA", 0);
			Thread.sleep(1000);
			provisiontask = fwgui.fw_get_text("ProvisionTask Value", "NA", "xpath",
					first_part + 2 + second_part + taskColumn + third_part, "NA", 0);
			System.out.println("orderState : "+orderState);
			taskPresent =false;
			if(orderState.equalsIgnoreCase("In Progress"))
			{
				taskPresent = true;
				Thread.sleep(2000);
				fwgui.fw_click_element_using_javascript("Table Action", "NA", "xpath", "//input[@name='move']", "NA", 1000);
				log.fw_writeLogEntry("Clicked on the move button. ", "NA");
				Thread.sleep(2000);
				if(provisiontask.equalsIgnoreCase("Build Service"))
				{
					fwgui.fw_select_from_a_list_by_visible_text("Select Service", "NA", "xpath", "//select[@tabIndex='29']", "L2_ATTACH", 1000);
				}
				T3_OSM_Update_Task_Using_Update_Button(configuration_map_fullpath, tab_name);
				log.fw_writeLogEntry("Clicked on the update button. ", "NA");
				Thread.sleep(2000);
				fwgui.fw_click_element_using_javascript("Table Action", "NA", "xpath", refreshButton, "NA", 1000);
				log.fw_writeLogEntry("Clicked on the refresh button. ", "NA");
				Thread.sleep(500);
				log.fw_writeLogEntry("TASK NAME :  "+provisiontask+ " is Completed", "NA");
			} else {
				
				System.out.println(" Go to : T3_CRM_ValidateOrderComplete");
				fwgui.fw_switch_to_driver("CHROME");
				
				//Refresh CRM before validating order complete
				log.fw_writeLogEntry("This step is used to refresh line items page", "NA");
				Thread.sleep(5000);

/*				// Click on Setting button
				fwgui.fw_check_element_existence("Line Items Menu", "NA", "xpath", "//button[@title='Line Items Menu']",
						"200,1000", 1000);

				fwgui.fw_event(configuration_map_fullpath, tab_name, "ClickJAVASCRIPT", "LineItem_LineItemsMenu", "", "",
						"7000");
				// RunQuery

				//fwgui.fw_check_element_existence("Run Query", "NA", "partiallink", "Run Query", "200,1000", 1000);

				//fwgui.fw_event(configuration_map_fullpath, tab_name, "ClickJAVASCRIPT", "RunQuery", "", "", "7000"); */
				
				T3_CRM_Search_Order();
				Thread.sleep(2000);				
				boolean flag = T3_CRM_ValidateOrderComplete();
				if(flag)
				{
					break;
				}
				
			}
		}
	}


	
}


package com.xavient.framework.util;

public class Constants {
	// paths
	public static final String APPIUMSUITE_XLS_PATH = System.getProperty("user.dir")+"\\executioninfo\\input-data\\AppiumSuite.xlsx";
	public static final String SUITEWEBAPPLICATION_XLS_PATH = System.getProperty("user.dir")+"\\executioninfo\\input-data\\webApplicationSuite.xlsx";
	public static final String SUITEB_XLS_PATH = System.getProperty("user.dir")+"\\executioninfo\\input-data\\SuiteB.xlsx";
	public static final String TESTSUITE_XLS_PATH = System.getProperty("user.dir")+"\\executioninfo\\input-data\\TestSuite.xlsx";
	public static final String PROPERTIES_FILE_PATH = System.getProperty("user.dir")+"//src//test//resources//project.properties";
	public static final String SCREENSHOT_PATH = System.getProperty("user.dir")+"\\executioninfo\\screenshots\\";
	public static final String SUREFIREREPORTS = "D:\\SUREFIREREPORTS\\";
	// sheet names
	public static final String TESTSUITE_SHEET = "TestSuite";
	public static final String TestData_SHEET = "TestData";
	public static final String TESTCASES_SHEET = "TestCases";
	public static final String KEYWORDS_SHEET = "Keywords";
	public static final String SCREENSHOTS_SHEET = "ScreenshotsLink";


	// col names
	public static final String SUITENAME_COL = "SuiteName";
	public static final String RUNMODE_COL = "Runmode";
	public static final String TESTCASENAME_COL = "TestCaseName";
	public static final String BROWSER_COL = "Browser";;
	public static final String PORTFOLIONAME_COL = "PortFolioName";
	public static final String CASE_COL = "Case";
	public static final String Data_COL = "Data";
	public static final String STOCKNAME_COL = "StockName";
	public static final String QUANTITY_COL = "Quantity";

	// runmodes
	public static final String RUNMODE_YES="Y";
	public static final String RUNMODE_NO="N";
	public static final Object ITERATION_COL = "Iteration";
	public static final String SKIP = "SKIP";
	
	
	public static final String PASS = "PASS";
	public static final String MOZILLA = "mozilla";
	public static final String CHROME = "chrome";
	public static final String IE = "IE";
	
	//error message
	public static final String GENERAL_ERROR = "ERROR - FAILED KEYWORD - ";
	public static final String OPENBROWSER_ERROR = "ERROR - FAILED TO OPEN BROWSER - ";
	public static final String NAVIGATE_ERROR = "ERROR - FAILING TO NAVIGATE - ";
	public static final String LOCATOR_ERROR = "ERROR - INVALID LOCATOR - ";
	public static final String FIND_ELEMENT_ERROR = "ERROR - UNABLE TO FIND ELEMET -  ";

	// failure
	public static final String ELEMENT_NOT_FOUND_FAILURE = "FAIL - ELEMENT NOT FOUND - ";
	public static final String TITLE_NOT_MATCHES_FAILURE = "FAIL - Titles do not match. Expected -  ";
	public static final String DEFAULT_LOGIN_FAILURE = "FAIL - Not able to Login with Deault Username/password ";
	public static final String CLICKANDWAIT_FAILURE = "FAIL - Could not click and wait - ";
	public static final String PORTFOLIONAMENOTPRESENT_FAILURE = "FAIL - Portfolio name not present  - ";
	public static final String DUPLICATE_FAILURE = "FAIL - Duplicate Element expected but not found";
	public static final String AJAX_COMAPNY_ERR = "FAIL - Could not select the company name - ";
	public static final String ADD_NEW_STOCK_FAILURE = "FAIL - Stock name not entered in table ";
	public static final String DATA_NOT_MATCH_FAILURE = "FAIL - DATA DOES NOT MATCH THE EXCPECTED ";
	public static final String ANDROID = "Android";
	public static final String FAIL="FAIL";

	
	
	
	
	
	

	

}

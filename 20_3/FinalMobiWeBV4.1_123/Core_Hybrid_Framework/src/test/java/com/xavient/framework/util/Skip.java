package com.xavient.framework.util;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.xavient.framework.util.Constants;

import com.xavient.framework.util.Xls_Reader;
public class Skip {

	
	static Logger Application_Log;
	static Properties prop;
	public static Method method[];
	public static WebDriver driver;
	static HashMap<String,WebDriver> map;

	static String currentTestCaseName;
	static String currentIteration;
	public static Hashtable<String, String> data1;
	public static Xls_Reader xls = new Xls_Reader(Constants.SUITEWEBAPPLICATION_XLS_PATH);
	public static Xls_Reader xls1 = new Xls_Reader(Constants.TESTSUITE_XLS_PATH);
	public static int commonrunmod=1;
	public static int iterno=0;
	
	
	
	//Skip at suite level
	
	public static void skipatsuitelevel(String suitename)
	{
		
		int irow = xls1.getRowCount(Constants.TESTSUITE_SHEET);
		xls1.setCellData(Constants.TESTSUITE_SHEET, 2,1,"RESULT ");
		for(int rNum1 =2; rNum1<=irow;rNum1++)
		{
			String runmode = xls1.getCellData(Constants.TESTSUITE_SHEET, 1, rNum1);
			String value = xls1.getCellData(Constants.TESTSUITE_SHEET, 0, rNum1);
			
			if(value.equalsIgnoreCase(suitename)&&runmode.equalsIgnoreCase(Constants.RUNMODE_NO))
			{
				boolean flag1=xls1.setCellData(Constants.TESTSUITE_SHEET, 2, rNum1,Constants.SKIP);
			
			}
		}
		
	}
	
	
	
	//Skip at test case level
	
	public static void skipattestlevel(String testid)
	{
		
		int irow = xls.getRowCount(Constants.TESTCASES_SHEET);
	boolean flag=xls.setCellData(Constants.TESTCASES_SHEET, 2,1,"RESULT ");
	System.out.println(flag);
		for(int rNum1 =2; rNum1<=irow;rNum1++)
		{
			String runmode = xls.getCellData(Constants.TESTCASES_SHEET, 1, rNum1);
			String value = xls.getCellData(Constants.TESTCASES_SHEET, 0, rNum1);
			if(value.equalsIgnoreCase(testid)&&runmode.equalsIgnoreCase(Constants.RUNMODE_NO))
			{
				boolean flag1=xls.setCellData(Constants.TESTCASES_SHEET, 2, rNum1,Constants.SKIP);
				System.out.println(flag1);
			
			}
		}
		
	}
	
	

	//Skip data level function to write Skip text on xls
	public static void skipfunctiondatalevel(String testid,int iteration_no,int iTestidFRow)
	
	{
	
	int commoncounter=0;
	
	int irow = xls.getRowCount(Constants.KEYWORDS_SHEET);

		
	int iCol = iteration_no + 4;
	xls.setCellData(Constants.KEYWORDS_SHEET, iCol,1,"RESULT "+iteration_no);
	//test id storage array
	String[] tcid= new String[irow+2];
	
	//array to read all test cases 
	//loop to store test ids.
	for(int rNum=2;rNum<=irow;rNum++)
	{
		String sVlue2 = xls.getCellData(Constants.KEYWORDS_SHEET, 0, rNum);
		
		 tcid[rNum] =sVlue2;
		 if(sVlue2.equalsIgnoreCase(testid))
			 commoncounter++;
	}

		int skiplimit = iTestidFRow+commoncounter;
		
		for(int setvlue=iTestidFRow; setvlue<skiplimit;setvlue++)
		
		{
			boolean mydataset =xls.setCellData(Constants.KEYWORDS_SHEET, iteration_no+4, setvlue,Constants.SKIP);
			System.out.println(mydataset+"---------------SKIP Function----------------- "+iteration_no);
		}
	
	}
	
		
	//Returns first row to of the testcase id
	
	public static int FRowtestcase(String testid)
	{
		
		int irow = xls.getRowCount(Constants.KEYWORDS_SHEET);
		//String[] tcid= new String[irow+2];
		int counter=0;
		//array to read all test cases 
		//loop to store test ids.
		for(int rNum=2;rNum<=irow;rNum++)
		{
			String sVlue2 = xls.getCellData(Constants.KEYWORDS_SHEET, 0, rNum);
			//System.out.println(sVlue2);
			 //tcid[rNum] =sVlue2;
			 if(sVlue2.equalsIgnoreCase(testid)){
				counter++; 
				break;
			 }
			 counter++;
		}
		System.out.println("counter    "+ counter);
		return counter+1;
		
	}
	
	
	//function to write test case name to Screeshot sheet.
	//screenshot
	
	public static void screen()
	{

		int irow =xls.getRowCount(Constants.TESTCASES_SHEET);
		
		for(int i=2;i<=irow;i++)
		{
			String testid = xls.getCellData(Constants.TESTCASES_SHEET, 0, i);
		System.out.println(testid);
		
		int testCaseRowNum=1;
		while(!xls.getCellData(Constants.TestData_SHEET, 0, testCaseRowNum).trim().toLowerCase().equals(testid.toLowerCase())){
			testCaseRowNum++;
		}
		System.out.println(testCaseRowNum+"----------------------------"+"Screen Function");
		int dataStartRowNum = testCaseRowNum +1;
		int rows =0;
		while(!xls.getCellData(Constants.TestData_SHEET, 0, dataStartRowNum+rows).trim().equals("")){
			rows++;
		}
		System.out.println("Total rows=========== "+ rows);
		
		xls.setCellData(Constants.SCREENSHOTS_SHEET,0,dataStartRowNum,testid);
	}
	}
	
	//function to write testid on Screenshot link sheet
	
	public static void skip1()
	{

		int irow =xls.getRowCount(Constants.TESTCASES_SHEET);
		String testid;
		for(int i=2;i<=irow;i++)
		{
			testid = xls.getCellData(Constants.TESTCASES_SHEET, 0, i);
		System.out.println(testid);
	
		int testCaseRowNum=1;
		while(!xls.getCellData(Constants.TestData_SHEET, 0, testCaseRowNum).trim().toLowerCase().equals(testid.toLowerCase())){
			testCaseRowNum++;
		}
		
		int dataStartRowNum = testCaseRowNum +1;
		int rowno = testCaseRowNum +1;
		int rows =0;
		while(!xls.getCellData(Constants.TestData_SHEET, 0, dataStartRowNum+rows).trim().equals("")){
			rows++;
			String myvalue =xls.getCellData(Constants.TestData_SHEET,1,rowno);
			System.out.println(myvalue);
			if(myvalue.equalsIgnoreCase(Constants.RUNMODE_NO))
			{
				String myvalue1 =xls.getCellData(Constants.TestData_SHEET,0,rowno);	
				System.out.println("Iteration no. "+myvalue1 );
				int intvalue = Integer.parseInt(myvalue1);
				skipfunctiondatalevel(testid,intvalue, FRowtestcase(testid));
			}
			rowno++;
		}
		
		}
	}
	
	
	
	
	//running the class in stand alone mode
	
	
	
	public static void main(String[] args) {
		
		//skip1();
		
	}

}

package com.xavient.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class CustomReports implements IReporter{

	static HashMap<Object, Object> testSuite;
	static HashMap<Object, Object> testTest;
	static HashMap<Object, Object> testPassed;
	static HashMap<Object, Object> testFailed;
	static HashMap<Object, Object> testSkiped;
	static final String dest = Reporting.getPath()+"//Reports//NewPDF.pdf";
	
	@Override
	public void generateReport(List<XmlSuite> xmlSuite, List<ISuite> iSuite, String s) {
		int suiteNo=1;
		int testNo=1;
		int failedName=1;
		int passedName=1;
		int skippedName=1;
		testSuite = new HashMap<Object,Object>();
		testTest = new HashMap<Object,Object>();
		testPassed = new HashMap<Object,Object>();
		testFailed = new HashMap<Object,Object>();
		testSkiped = new HashMap<Object,Object>();
		
		for(ISuite suite : iSuite)
		{
			testSuite.put("suite"+suiteNo, suite.getName());
			System.out.println("Suite Name : "+testSuite.get("suite"+suiteNo));
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for(String testName : suiteResults.keySet())
			{
				testTest.put("test"+testNo, testName);
				System.out.println("TESTS : "+testTest.get("test"+testNo));
				ISuiteResult suiteResult = suiteResults.get(testName);
				ITestContext testContext = suiteResult.getTestContext();
				testTest.put("testStart",testContext.getStartDate());
				testTest.put("testEnd",testContext.getEndDate());
				testFailed.put("failedno",testContext.getFailedTests().size());
				System.out.println("FAILED : " + testFailed.get("failedno"));
				IResultMap failedResult = testContext.getFailedTests();
				Set<ITestResult> testsFailed = failedResult.getAllResults();
				for(ITestResult testResult : testsFailed)
				{
					testFailed.put("failedname"+failedName, testResult.getName());
					testFailed.put("failederror"+failedName, testResult.getThrowable());
//					Reporting.CaptureScreen(testResult.getMethod().getMethodName());
					System.out.println("Failed Name : "+testFailed.get("failedname"+failedName));
					System.out.println("Error  : "+testFailed.get("failederror"+failedName));
					failedName++;
				}
				testPassed.put("passedno", testContext.getPassedTests().size());
				System.out.println("PASSED : " + testPassed.get("passedno"));
				IResultMap passResult = testContext.getPassedTests();
				Set<ITestResult> testsPassed = passResult.getAllResults();
				for(ITestResult testResult : testsPassed)
				{
					testPassed.put("passedname"+passedName, testResult.getName());
					System.out.println("Passed Name : "+testPassed.get("passedname"+passedName));
					passedName++;
				}
				testSkiped.put("skipedno", testContext.getSkippedTests().size());
				System.out.println("SKIPPED : " + testSkiped.get("skipedno"));
				IResultMap skipResult = testContext.getSkippedTests();
				Set<ITestResult> testsSkip = skipResult.getAllResults();
				for(ITestResult testResult : testsSkip)
				{
					testSkiped.put("skippedname"+skippedName, testResult.getName());
					try {
						testSkiped.put("skipError"+skippedName,testResult.getThrowable());
						System.out.println("Skipped Error : "+testSkiped.get("skipError"+skippedName));
					} catch (Exception e) {
						// TODO: handle exception
					}
					System.out.println("Skipped Name : "+testSkiped.get("skippedname"+skippedName));
					skippedName++;
				}
			}
		}
		try {
			PDFGenerate.createPDF(dest);
			System.out.println("PDF Report is created");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

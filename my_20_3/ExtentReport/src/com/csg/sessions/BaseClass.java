package com.csg.sessions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import javax.naming.spi.DirStateFactory.Result;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.csg.reports.PDFGenerate;
import com.csg.reports.Reporting;
import com.csg.reports.XMLUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.log.SysoCounter;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseClass  implements ISuiteListener {

	public static WebDriver driver;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static final String dest = Reporting.getPath()+"//Reports//NewPDF.pdf";

	@BeforeTest
	public void startExecute()
	{
		Reporting.deleteReport();
		extent = Reporting.Instance();
	}

	public void setUP(String testCaseName, String des)
	{
		test = extent.startTest(testCaseName,des);
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://osmsdp-qa.corp.chartercom.com:8001/OrderManagement/control/home");
		test.log(LogStatus.INFO, "OSM Application is launched");
	}

	@AfterTest
	public void stopExecution()
	{
		extent.endTest(test);
		extent.flush();
		//extent.close();
	}

	@AfterClass
	public void tearDown()
	{
		driver.quit();
	}

	@AfterMethod
	public void getStatus(ITestResult result1)
	{

		String result = " ";
		int newResult = 0;
		newResult = result1.getStatus();
		result =test.getRunStatus().toString();
		System.out.println("Result : "+result);
		if(result.equalsIgnoreCase("error")||result.equalsIgnoreCase("fail")||newResult==result1.FAILURE)
		{
			String img = test.addScreenCapture(Reporting.CaptureScreen(result1.getMethod().getMethodName()));
			System.out.println(img); 
			test.log(LogStatus.INFO, "Image", "Screenshot below : " + img);
		}
	}

	@AfterSuite
	public void getReport()
	{
		
		
/*		try {
			Thread.sleep(40000);
			PDFGenerate obj = new PDFGenerate();
			obj.createPDF(dest);
			System.out.println("Report is created");
		} catch (Exception e){
		}*/
	}

	public WebDriver getDriver()
	{
		return driver;
	}

	@Override
	public void onFinish(ISuite arg0) {
		XMLUtils obj = new XMLUtils();
		obj.readXML();
		
	}

	@Override
	public void onStart(ISuite arg0) {
		// TODO Auto-generated method stub
		
	}
}

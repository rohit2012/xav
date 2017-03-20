package com.csg.Testscripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.csg.reports.CustomReports;
import com.csg.reports.PDFListener;
import com.csg.sessions.BaseClass;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@Listeners(CustomReports.class)
public class OSMLogin extends BaseClass {

	WebDriver driver;
	ExtentReports extent;
	ExtentTest test;

	@BeforeClass
	public void initalizeObjects()
	{
		setUP("OSM Login", "Verify OSM Login");
		driver = BaseClass.driver;
		extent = BaseClass.extent;
		test = BaseClass.test;
	}
	
	@Test(priority =1)
	public void login()
	{
		try {
			
			driver.findElement(By.className("UserNameComponent")).sendKeys("Nkumar");
			test.log(LogStatus.PASS, "Enter the username into userfield");
			driver.findElement(By.className("PasswordComponent")).sendKeys("nkumar!1");
			test.log(LogStatus.PASS, "Enter the password into passwordfield");
			driver.findElement(By.className("LoginText")).click();
			test.log(LogStatus.PASS, "Clicked on Login button");
			boolean workListPresent = driver.findElement(By.xpath("//img[@alt='Go to Worklist']")).isDisplayed();
			if(workListPresent)
			{
				test.log(LogStatus.PASS, "User is able to log into Application.");
			}else
			{
				test.log(LogStatus.FAIL, "User is not able to log into Application.");
			}

		} catch (Exception e) {
			test.log(LogStatus.ERROR,"Error appears : "+e.getMessage());
		}
	}
}

package com.csg.Testscripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
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
public class OSMSearch extends BaseClass {

	WebDriver driver;
	ExtentReports extent;
	ExtentTest test;

	@BeforeClass
	public void initalizeObjects()
	{
		setUP("OSM Search", "Verify OSM Search");
		extent = BaseClass.extent;
		test = BaseClass.test;
		driver = BaseClass.driver;
	}

	@Test()
	public void searchOrderID()
	{
		try {

			driver.findElement(By.className("UserNameComponent")).sendKeys("Nkumar");
			test.log(LogStatus.PASS, "Enter the username into userfield");
			Reporter.log("Enter the username into userfield");
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
		try {
			driver.findElement(By.xpath("//img[@alt='Go to Worklist']")).click();
			test.log(LogStatus.PASS, "Clicked on the worklist button");
			driver.findElement(By.name("referenceFilter")).sendKeys("1200027058771014*");
			test.log(LogStatus.PASS, "Enter the Order ID");
			driver.findElement(By.xpath("//input[@value='Refresh1']")).click();
			test.log(LogStatus.PASS, "Clicked on the Refresh button");
			try {
				driver.findElement(By.name("move")).isDisplayed();
				test.log(LogStatus.PASS, "User is able to Search.");
			} catch (Exception e) {
				test.log(LogStatus.FAIL, "User is not able to Search.");
			}

		} catch (Exception e) {
			Assert.fail("EXCEPTION OCCURED : "+e.getMessage());
			test.log(LogStatus.ERROR,"Error appears : "+e.getMessage());
		}
	}

}

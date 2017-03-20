package com.test.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class OSMApplicaion {

	WebDriver driver;
	
	@BeforeClass
	public void setUP()
	{
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://osmsdp-qa.corp.chartercom.com:8001/OrderManagement/control/home");
	}
	
	@Test(priority =1)
	public void login()
	{
		driver.findElement(By.className("UserNameComponent")).sendKeys("Nkumar");
		driver.findElement(By.className("PasswordComponent")).sendKeys("nkumar!1");
		driver.findElement(By.className("LoginText")).click();
		boolean workListPresent = driver.findElement(By.xpath("//img[@alt='Go to Worklist']")).isDisplayed();
		Assert.assertTrue(workListPresent, "User is not able to log into Application.");
	}
	
	@Test(dependsOnMethods={"login"})
	public void searchOrderID()
	{
		driver.findElement(By.xpath("//img[@alt='Go to Worklist']")).click();
		driver.findElement(By.name("referenceFilter")).sendKeys("1200027058771014*");
		driver.findElement(By.xpath("//input[@value='Refresh']")).click();
		Boolean f = false;
		try {
			f = driver.findElement(By.name("move")).isDisplayed();
		} catch (Exception e) {
		}
		Assert.assertTrue(f, "No record displays");
	}
	
	@AfterClass
	public void tearDown()
	{
		driver.quit();
	}
}

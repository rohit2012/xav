/**
 * 
 */
package com.test.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
;/**
 * @author csingh5
 *
 */

@Listeners(com.test.java.PDFListener.class)
public class TestFF extends com.test.java.PDFListener{

	/**
	 * @param args
	 */
	//public static void main(String[] args) {
	
	
	
	@Test
	public void setUp(){
		
	System.out.println("Test");
		WebDriver driver =  new FirefoxDriver();
		driver.get("https://webapps.csgweb.com/Citrix/webapps/site/default.aspx");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(By.id("Enter user name")).sendKeys("charsgarg01");
		driver.findElement(By.name("passwd")).sendKeys("Charter2");
		driver.findElement(By.id("Log_On")).click();
		WebDriverWait w = new WebDriverWait(driver, 2);
		w.until(ExpectedConditions.alertIsPresent());
		
		driver.findElement(By.xpath("//img[@title='ACSR CCSL10']")).click();
		
	}

}

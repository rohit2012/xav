package com.test.webdriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SearchMacAddress {

	WebDriver driver;

	@BeforeClass
	public void seUP()
	{
		driver = new FirefoxDriver();
		driver.get("http://192.168.29.30:8100/adminui/index.jsp");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@BeforeMethod
	public void login()
	{
		driver.findElement(By.name("username")).sendKeys("team_tmo");
		driver.findElement(By.name("password")).sendKeys("automation");
		driver.findElement(By.xpath("//input[@type='submit']")).click();
	}

	@Test
	public void findVoiceMacAddress()
	{
		driver.findElement(By.linkText("Devices")).click();
		selectDropDown(By.name("pageSize"), "75");
		driver.findElement(By.name("SearchQuery")).clear();
		driver.findElement(By.name("SearchQuery")).sendKeys("1,6,30:*");
		driver.findElement(By.xpath("//table[@class='search']//input[@value='Search']")).click();
		String macID = getmacAddress();
		System.out.println("Used Mac : "+macID);
	}

	public String getmacAddress()
	{
		List<WebElement> list = driver.findElements(By.xpath("//table[@class='results']/tbody/tr"));
		System.out.println("List size : "+list.size());
		int j =1;
		String macID =null;
		String [] mac= null;
		String [] newMac= null;
		for(int i=7;i<=list.size()-1;i++)
		{
			String text = null;
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			try {
				//text = driver.findElement(By.xpath("//table[@class='results']/tbody/tr["+i+"]/td[4]")).getText();
				text = list.get(i).findElement(By.xpath("td[4]")).getText();
				System.out.println(j+". "+ text);
				if(text.equals("Unprovisioned"))
				{
					macID = list.get(i).findElement(By.xpath("td[2]")).getText();
					mac = macID.split("6");
					newMac = mac[1].split(",");
					break;
				}
				j++;
			} catch (Exception e) {
				// TODO: handle exception
			}


		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return newMac[1];
	}

	public void selectDropDown(By locate,String text)
	{
		Select sel = new Select(driver.findElement(locate));
		sel.selectByVisibleText(text);
	}

	@AfterClass
	public void tearDown()
	{
		driver.quit();
	}
}

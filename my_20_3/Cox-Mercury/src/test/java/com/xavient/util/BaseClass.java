package com.xavient.util;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import com.xavient.pages.DashBoardView;

public class BaseClass implements DashBoardView{
	static WebDriver driver;
	/**
	 * Method is initalizing driver with defined browser properties.
	 * @author AJameel
	 * @param browser
	 * @return driver Object
	 */
	public  WebDriver Browser_Selection(String browser)  {

		String path = System.getProperty("user.dir");
		System.out.println("driver path is"+path);
		if (browser.equalsIgnoreCase("firefox")) {	
			// create firefox instance
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.get(Properties_Reader.readProperty("URL"));

		}

		else if (browser.equalsIgnoreCase("Chrome")) {
			// set path to chromedriver.exe

			System.setProperty(
					"webdriver.chrome.driver",
					path
					+ Properties_Reader.readProperty("chrome_driver"));

			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.get(Properties_Reader.readProperty("URL"));

		}

		else if (browser.equalsIgnoreCase("ie")) {

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);

			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			System.setProperty("webdriver.ie.driver", path + Properties_Reader.readProperty("ie_driver"));
			driver = new InternetExplorerDriver(capabilities);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.get(Properties_Reader.readProperty("URL"));
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driver.findElement(IECertificate).click();

		}

		else {
			// If no browser passed throw exception
			try {
				throw new Exception("Browser is not correct");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return driver;
	}

	@AfterMethod
	public void getStatus(ITestResult result1) {
		try {
			int newResult = 0;
			newResult = result1.getStatus();
			if (newResult == result1.FAILURE) {
				Reporting.CaptureScreen(result1.getMethod().getMethodName());
			}
		} catch (Exception e) {
		}
		driver.close();
	}

}

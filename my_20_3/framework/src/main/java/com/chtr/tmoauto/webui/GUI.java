package com.chtr.tmoauto.webui;

import com.chtr.tmoauto.logging.*;
import com.chtr.tmoauto.util.DateTime;
import com.chtr.tmoauto.util.MSExcel;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.Select;
import org.webbitserver.helpers.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class GUI 
{
	 
	public static WebDriver driver;	
	public static WebDriver driverie;
	public static WebDriver driverchrome;
	
	public static Logging log = new Logging();
	public static DateTime dt = new DateTime();
	
	public static String window_handle = "";
	public static String return_get_text = "";
	public static Boolean return_existence;
	public static String out_object_testdata;
	public static String out_object_extrainfo = "";
	
	/**
	 * This function will terminate all processes.
	 * The input into this function is a list of processes comma delimited to be terminated.
	 * Example is "chrome.exe,excel.exe"
	 *  
	 * @param process_list
	 * @throws IOException
	 * @author Mark Elking
	 * @since 10/20/2016
	 * 
	 */
	
	public void fw_terminate_window_processes(String process_list) throws IOException 
	{
		
		String process_name = "";
		String[] process_list_arr = process_list.split(",");
		for (int x=0;x<process_list_arr.length;x++)
		{
			process_name = process_list_arr[x];
			Runtime.getRuntime().exec("taskkill /IM " + process_name + " /F");
		}
		
	}
	    
	/**
	 * This function launches a browser and defines Driver.
	 * The input required for this function is the browser type (example is IE or CHROME)
	 * 
	 * @param browsertype
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 10/26/2016
	 */
	
	public void fw_launch_browser(String browsertype) throws InterruptedException, IOException
	{
		String window_handle = "";
		
		String[] browsertype_arr = browsertype.split(",");
		browsertype = browsertype_arr[0];
		
		String proxytype = "NA";
		if (browsertype.contains(","))
		{
			proxytype = browsertype_arr[1];
		}
		
		String return_code = "";
		String failed_msg = "";
		
		if (browsertype.equalsIgnoreCase("IE"))
		{
			return_code = "0";
			System.setProperty("webdriver.ie.driver", "driver/IEDriverServer.exe");
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer(); 
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			// Added by Mark/Gaurav on 1/13/2017
			caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			// End of Added by Mark/Gaurav on 1/13/2017
			
			// Added by Mark/Gaurav/Atul/Rohit on 1/13/2017
			if (!proxytype.equals("NA"))
			{
				Proxy proxy=new Proxy();
				
				if (proxytype.equals("PROXY"))
				{
					proxy.setProxyType(ProxyType.DIRECT);
				}
				else if (proxytype.equals("NOPROXY"))
				{
					proxy.setNoProxy(null);
				}
				
				caps.setCapability(CapabilityType.PROXY, proxy);
				caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				
			}
			// End on Added by Mark/Gaurav/Atul/Rohit on 1/13/2017
			
			//driver = new InternetExplorerDriver(caps);
			driverie = new InternetExplorerDriver(caps);
			fw_switch_to_driver("IE");
			window_handle = fw_get_window_handle();
			fw_set_variable("LAUNCH_BROWSER_WINDOW_HANDLE", window_handle);
			
		}
		else if (browsertype.equalsIgnoreCase("CHROME"))
		{
			return_code = "0";
			System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
			DesiredCapabilities caps = DesiredCapabilities.chrome();
			
			// Added by Mark/Gaurav on 1/13/2017
			caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			// End of Added by Mark/Gaurav on 1/13/2017
			
			// Added by Mark/Gaurav/Atul/Rohit on 1/13/2017
			if (!proxytype.equals("NA"))
			{
				Proxy proxy=new Proxy();
				
				if (proxytype.equals("PROXY"))
				{
					proxy.setProxyType(ProxyType.DIRECT);
				}
				else if (proxytype.equals("NOPROXY"))
				{
					proxy.setNoProxy(null);
				}
				
				caps.setCapability(CapabilityType.PROXY, proxy);
				caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				
			}
			// End on Added by Mark/Gaurav/Atul/Rohit on 1/13/2017
			
			//driver = new ChromeDriver(caps);
			driverchrome = new ChromeDriver(caps);
			fw_switch_to_driver("CHROME");
			window_handle = fw_get_window_handle();
			fw_set_variable("LAUNCH_BROWSER_WINDOW_HANDLE", window_handle);
			
		}
		else 
		{		
			return_code = "1";
			failed_msg = " - Browser Type not defined.";	
		}
		
		log.fw_writeLogEntry("Launch Browser (Browser Type: " + browsertype + ")" + failed_msg, return_code);
		
	}

	/**
	 * 
	 * This function switches to driver.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/18/2017
	 * 
	 */
	
	public void fw_switch_to_driver(String browser_type) throws InterruptedException, IOException
	{
		
		if (browser_type.equalsIgnoreCase("IE"))
		{
			driver = driverie;
		}
		else if (browser_type.equalsIgnoreCase("CHROME"))
		{
			driver = driverchrome;
		}
		
		log.fw_writeLogEntry("Switch To Driver (Browser Type: " + browser_type + ")", "0");
		
	}
	
	/**
	 * 
	 * This function switches between frames.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/17/2017
	 * 
	 */
	
	public void fw_switch_frame(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException
	{
		
		WebElement webelement = driver.findElement(fw_get_element_object(locator, locatorvalue));		
		driver.switchTo().frame(webelement);
		
		log.fw_writeLogEntry("Switch Frame", "0");
		
	}
	
	/**
	 * This function closes window.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/16/2017
	 * 
	 */
	
	public void fw_close_window() throws InterruptedException, IOException 
	{
		
		// Store the current window handle
		String winHandleBefore = driver.getWindowHandle();

		// Perform the click operation that opens new window

		// Switch to new window opened
		String window_closed = "";
				
		for(String winHandle : driver.getWindowHandles()){
			window_closed = winHandle;
		    driver.switchTo().window(winHandle);
		}

		// Perform the actions on new window

		// Close the new window, if that window no more required
		driver.close();

		// Switch back to original browser (first window)
		driver.switchTo().window(winHandleBefore);

		// Continue with original browser (first window)
		
		log.fw_writeLogEntry("Close Window (Handle: " + window_closed + ")", "0");
		
	}

	/**
	 * This function gets window handle.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/24/2017
	 * 
	 */
	
	public String fw_get_window_handle() throws InterruptedException, IOException 
	{
		
		window_handle = driver.getWindowHandle();
		
		log.fw_writeLogEntry("Get Window Handle (Handle: " + window_handle + ")", "0");
		
		return window_handle;
		
	}
	
	/**
	 * This function switches to new window.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/24/2017
	 * 
	 */
	
	public void fw_switch_to_new_window() throws InterruptedException, IOException 
	{
		
		String current_window_handle = "";
		
		for(String winHandle : driver.getWindowHandles()){
		    driver.switchTo().window(winHandle);
		    current_window_handle = winHandle;
		}
		
		log.fw_writeLogEntry("Switch to Window (Handle: " + current_window_handle + ")", "0");
		
	}
	
	/**
	 * This function switches to window based on window handle.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/24/2017
	 * 
	 */
	
	public void fw_switch_to_window(String window_handle_value) throws InterruptedException, IOException 
	{
		
		driver.switchTo().window(window_handle_value);
		
		log.fw_writeLogEntry("Switch to Window (Handle: " + window_handle_value + ")", "0");
		
	}
	
	/**
	 * This function accepts an alert.
	 * 
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/19/2017
	 * 
	 */
	
	public void fw_accept_alert() throws InterruptedException, IOException 
	{
		
		driver.switchTo().alert().accept();
		
		log.fw_writeLogEntry("Accept Alert", "0");
		
	}
	
	/**
	 * This function navigates to a URL.
	 * 
	 * @param url
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 9/5/2016
	 * 
	 */
	
	public void fw_navigate_to_url(String url) throws InterruptedException, IOException 
	{
		
		driver.manage().window().maximize();
		driver.navigate().to(url);		
		
		log.fw_writeLogEntry("Navigate to URL (URL: " + url + ")", "0");
		
	}
	
	/**
	 * This function will login to SSO.
	 * 
	 * @param userid
	 * @param passid
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 2/23/2017
	 * 
	 */
	
	public void fw_login_to_SSO(String userid, String passid) throws InterruptedException, IOException 
	{
		
		String text_msg = "";
		
		String current_window_handle = "";
		for(String winHandle : driver.getWindowHandles())
		{
			driver.switchTo().window(winHandle);
		    current_window_handle = winHandle;
		}
		Thread.sleep(2000);
		
		String window_url = driver.switchTo().window(current_window_handle).getCurrentUrl();
		
		Thread.sleep(2000);
		
		if (window_url.contains("esso"))
		{
			//String after_window_handle = driver.switchTo().window(current_window_handle).getWindowHandle();
			
			fw_enter_data_into_text_field("LOGIN_Username", "input", "id", "username", userid, 0);
			fw_enter_data_into_text_field("LOGIN_Username", "input", "id", "password", passid, 0);
			fw_click_button("LOGIN_Username", "input", "name", "loginButton2", passid, 0);
			
			Thread.sleep(20000);
			
			String workspace_name = fw_get_workspace_name();
			String variables_path = workspace_name + "\\variables\\";	
			String before_window_handle = fw_get_value_from_file(variables_path + "LAUNCH_BROWSER_WINDOW_HANDLE");
			
			driver.switchTo().window(before_window_handle);
			
		}
		else
		{
			text_msg = " - SSO Login NOT Found.";
		}
		
		log.fw_writeLogEntry("Login to SSO (User ID: " + userid + ", Pass ID: " + passid + ")" + text_msg, "0");
		
	}
	
	/**
	 * This function enters data into a text field.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 10/26/2016
	 * 
	 */
	
	public void fw_enter_data_into_text_field(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
		
		// NEW
		if (fieldvalue.contains("FILE_"))
		{
			String workspace_name = fw_get_workspace_name();
			String variables_path = workspace_name + "\\variables\\";
			
			String[] tc_test_data_array = fieldvalue.split("_");
			String tc_test_data_filename = tc_test_data_array[1];			
			//fieldvalue = fw_get_value_from_file(tc_test_data_filename);
			fieldvalue = fw_get_value_from_file(variables_path + tc_test_data_filename);
		}
		// END OF NEW
				
		String text_msg = "";
		String ret_cd = "0";
		String found_flag = "";
		
		if (fieldvalue.contains("KEYTAB"))
		{
			String[] fieldvalue_arr = fieldvalue.split(",");
			int num_tabs = Integer.valueOf(fieldvalue_arr[1]);
			for (int r=1;r<num_tabs+1;r++)
			{
				driver.switchTo().activeElement().sendKeys(Keys.TAB);
			}
			found_flag = "YES";
		}
		else if (fieldvalue.contains("KEYENTER"))
		{
			driver.switchTo().activeElement().sendKeys(Keys.ENTER);
			found_flag = "YES";
		}
		else if (fieldvalue.contains("KEYDATA"))
		{
			
			driver.findElement(By.xpath(locatorvalue)).click();
					
			String[] fieldvalue_arr = fieldvalue.split(",");
			String key_data = fieldvalue_arr[1];
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].setAttribute('value','" + key_data + "');", driver.findElement(By.xpath(locatorvalue)));
			Thread.sleep(3000);
			//driver.switchTo().activeElement().sendKeys(Keys.TAB);
			//Thread.sleep(3000);
			//driver.switchTo().activeElement().sendKeys(Keys.NUMPAD9);
			
			/*
			driver.switchTo().activeElement().sendKeys(Keys.BACK_SPACE);
			Thread.sleep(1000);
			driver.switchTo().activeElement().sendKeys(Keys.NUMPAD9);
			driver.switchTo().activeElement().sendKeys(Keys.NUMPAD9);
			driver.switchTo().activeElement().sendKeys(Keys.NUMPAD9);
			Thread.sleep(1000);
			driver.findElement(By.tagName("body")).click();
			*/
			
			//Thread.sleep(1000);
			
			found_flag = "YES";
			
		}
		else if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
		{
			//comment
			if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
			{
				found_flag = "YES";
				
				if (fieldvalue.contains("NOCLEAR"))
				{
					String[] fieldvalue_arr = fieldvalue.split(",");
					String fieldvalue_output = fieldvalue_arr[1];
					driver.findElement(fw_get_element_object(locator, locatorvalue)).sendKeys(fieldvalue_output);
				}
				else
				{
					driver.findElement(fw_get_element_object(locator, locatorvalue)).clear();
					driver.findElement(fw_get_element_object(locator, locatorvalue)).sendKeys(fieldvalue);
				}
			}
		}
		else if (locator.equals("NA")) 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getText().trim();
				
				if (label.contains(locatorvalue))
				{
					found_flag = "YES";
					item.clear();
					item.sendKeys(fieldvalue);
					break;
				}
			}
		}
		else 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getAttribute(locator);
				
				if (label.contains(locatorvalue))
				{
					found_flag = "YES";
					item.clear();
					item.sendKeys(fieldvalue);
					break;
				}
			}
		}
		
		if (found_flag.equals("YES"))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Textbox NOT Found ****************";
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Enter Data into Textbox (Name: " + fieldname + ", Value: " + fieldvalue + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
		
	}
	
	/**
	 * This function selects checkbox.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/18/2017
	 * 
	 */
	
	public void fw_select_checkbox(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		String text_msg = "";
		String ret_cd = "0";
		String found_flag = "";
		
		if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
		{
			if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
			{
				found_flag = "YES";
				driver.findElement(fw_get_element_object(locator, locatorvalue)).click();
			}
		}
		else if (locator.equals("NA")) 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getText().trim();
				
				if (label.contains(locatorvalue))
				{
					found_flag = "YES";
					item.click();
					break;
				}
			}
		}
		else 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getAttribute(locator);
				
				if (label.contains(locatorvalue))
				{
					found_flag = "YES";
					item.click();
					break;
				}
			}
		}
		
		if (found_flag.equals("YES"))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Checkbox NOT Found ****************";
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Select Checkbox (Name: " + fieldname + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
		
	}

	/**
	 * This function clicks element using Javascript.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/17/2017
	 * 
	 */
	
	public void fw_click_element_using_javascript(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		WebElement webelement = driver.findElement(fw_get_element_object(locator, locatorvalue));
		JavascriptExecutor js0 = (JavascriptExecutor) driver;
		js0.executeScript("arguments[0].click();", webelement);
		
		log.fw_writeLogEntry("Click Element Using Javascript (Name: " + fieldname + ")", "0");
		
		Thread.sleep(milliseconds_to_wait);
		
	}

	/**
	 * This function will get a webelements object.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/18/2017
	 * 
	 */
	
	public List<WebElement> fw_get_webelements_object(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		String text_msg = "";
		String ret_cd = "0";
		String found_flag = "";
		List<WebElement> webelement = null;
		
		if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
		{
			if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
			{
				found_flag = "YES";
				webelement = driver.findElements(fw_get_element_object(locator, locatorvalue));
			}
		}
		else if (!locator.equals("NA"))  
		{
			found_flag = "YES";
			webelement = driver.findElements(By.tagName(tagname));
		}
		
		if (found_flag.equals("YES"))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** WebElements Object NOT Found ****************";
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Get WebElement Object (Name: " + fieldname + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
		
		return webelement;
		
	}
	
	// ******************************************************************************
	// *
	// * Name:        fw_get_element_object
	// * Author:      Gaurav Kumar
	// * Date:   	  11/16/2016
	// * Description: Method to get By object according to passed 
	// *              parameter values for locator
	// *
	// ******************************************************************************

	public By fw_get_element_object(String locName, String locValue) 
	{

		if (locName.equalsIgnoreCase("XPATH")) 
		{
			return By.xpath(locValue);
		}
		if (locName.equalsIgnoreCase("ID")) 
		{
			return By.id(locValue);
		}
		else if (locName.equalsIgnoreCase("CLASS")) 
		{
			return By.className(locValue);
		}
		else if (locName.equalsIgnoreCase("NAME")) 
		{
			return By.name(locValue);
		}
		else if (locName.equalsIgnoreCase("CSS")) 
		{
			return By.cssSelector(locValue);
		}
		else if (locName.equalsIgnoreCase("LINK")) 
		{
			return By.linkText(locValue);
		}
		else if (locName.equalsIgnoreCase("PARTIALLINK")) 
		{
			return By.partialLinkText(locValue);
		} 
		else 
		{
			System.out.println("Wrong entry ");
		}
		
		return null;

	}	
		
	/**
	 * This function clicks a button.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 10/26/2016
	 * 
	 */
	
	public void fw_click_button(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		String text_msg = "";
		String ret_cd = "0";
		String button_ready_flag = "";
		boolean button_isEmpty;
		boolean button_isEnabled;
		
		if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
		{
			for (int m=1;m<10;m++)
			{
				button_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
				button_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
				
				if(!button_isEmpty && button_isEnabled)
				{
					button_ready_flag = "YES";
					driver.findElement(fw_get_element_object(locator, locatorvalue)).click();
					break;
				}
				
				Thread.sleep(1000);
				
			}
		}
		else if (locator.equals("NA")) 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getText().trim();
				
				if (label.contains(locatorvalue))
				{
					button_isEnabled = item.isEnabled();
					if (button_isEnabled)
					{
						item.click();
						button_ready_flag = "YES";
					}
					else
					{
						button_ready_flag = "NO";
					}
					
					break;
				}
			}
		}
		else 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getAttribute(locator);
				
				if (label.contains(locatorvalue))
				{
					button_isEnabled = item.isEnabled();
					if (button_isEnabled)
					{
						item.click();
						button_ready_flag = "YES";
					}
					else
					{
						button_ready_flag = "NO";
					}
					
					break;
				}
			}
		}
		
		if (button_ready_flag.equals("YES"))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Button NOT Found or Button Disabled ****************";
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Click Button (Name: " + fieldname + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
		
	}

	/**
	 * This function clicks on a textbox field and enters data into that textbox.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 2/28/2017
	 * 
	 */
	
	public void fw_click_and_enter_data(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		// NEW
		if (fieldvalue.contains("FILE_"))
		{
			String workspace_name = fw_get_workspace_name();
			String variables_path = workspace_name + "\\variables\\";
			
			String[] tc_test_data_array = fieldvalue.split("_");
			String tc_test_data_filename = tc_test_data_array[1];			
			//fieldvalue = fw_get_value_from_file(tc_test_data_filename);
			fieldvalue = fw_get_value_from_file(variables_path + tc_test_data_filename);
		}
		// END OF NEW
				
		String text_msg = "";
		String ret_cd = "0";
		String field_ready_flag = "";
		boolean field_isEmpty;
		boolean field_isEnabled;
		
		if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
		{
			for (int m=1;m<10;m++)
			{
				field_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
				field_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
				
				if(!field_isEmpty && field_isEnabled)
				{
					field_ready_flag = "YES";
					driver.findElement(fw_get_element_object(locator, locatorvalue)).click();
					driver.switchTo().activeElement().sendKeys(fieldvalue);
					break;
				}
				
				Thread.sleep(1000);
				
			}
		}
		else if (locator.equals("NA")) 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getText().trim();
				
				if (label.contains(locatorvalue))
				{
					field_isEnabled = item.isEnabled();
					if (field_isEnabled)
					{
						item.click();
						driver.switchTo().activeElement().sendKeys(fieldvalue);
						field_ready_flag = "YES";
					}
					else
					{
						field_ready_flag = "NO";
					}
					
					break;
				}
			}
		}
		else 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getAttribute(locator);
				
				if (label.contains(locatorvalue))
				{
					field_isEnabled = item.isEnabled();
					if (field_isEnabled)
					{
						item.click();
						driver.switchTo().activeElement().sendKeys(fieldvalue);
						field_ready_flag = "YES";
					}
					else
					{
						field_ready_flag = "NO";
					}
					
					break;
				}
			}
		}
		
		if (field_ready_flag.equals("YES"))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Field NOT Found or Field Disabled ****************";
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Click and Enter Data into Textbox (Name: " + fieldname + ", Value: " + fieldvalue + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
		
	}
	
	/**
	 * This function selects a value from a list by value.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 10/29/2016
	 * 
	 */
	
	public void fw_select_from_a_list_by_value(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
				
		String text_msg = "";
		String ret_cd = "0";
		String found_flag = "";
		
		if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
		{
			if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
			{
				found_flag = "YES";
				WebElement element = driver.findElement(fw_get_element_object(locator, locatorvalue));
				Select listbox = new Select(element);
		        listbox.selectByValue(fieldvalue);
			}
		}
		else if (locator.equals("NA")) 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getText().trim();
				
				if (label.contains(fieldvalue))
				{
					found_flag = "YES";
					item.click();
					break;
				}
			}
		}
		else 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getAttribute(locator);
				
				if (label.contains(locatorvalue))
				{
					found_flag = "YES";
					WebElement element = item;
					Select listbox = new Select(element);
			        listbox.selectByValue(fieldvalue);
					break;
				}
			}
		}
		
		if (found_flag.equals("YES"))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Listbox NOT Found ****************";
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Select from a List By Value (Name: " + fieldname + ", Value: " + fieldvalue + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
		
	}

	/**
	 * This function selects a value from a list by visible text.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/17/2017
	 * 
	 */
	
	public void fw_select_from_a_list_by_visible_text(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{
				
		String text_msg = "";
		String ret_cd = "0";
		String found_flag = "";
		
		// NEW
		if (fieldvalue.contains("FILE_"))
		{
			String workspace_name = fw_get_workspace_name();
			String variables_path = workspace_name + "\\variables\\";
			
			String[] tc_test_data_array = fieldvalue.split("_");
			String tc_test_data_filename = tc_test_data_array[1];			
			fieldvalue = fw_get_value_from_file(variables_path + tc_test_data_filename);
		}
		// END OF NEW
				
		if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
		{
			if(!driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty())
			{
				found_flag = "YES";
				WebElement element = driver.findElement(fw_get_element_object(locator, locatorvalue));
				Select listbox = new Select(element);
		        listbox.selectByVisibleText(fieldvalue);
			}
		}
		else if (locator.equals("NA")) 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getText().trim();
				
				if (label.contains(fieldvalue))
				{
					found_flag = "YES";
					item.click();
					break;
				}
			}
		}
		else 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getAttribute(locator);
				
				if (label.contains(locatorvalue))
				{
					found_flag = "YES";
					WebElement element = item;
					Select listbox = new Select(element);
			        listbox.selectByVisibleText(fieldvalue);
					break;
				}
			}
		}
		
		if (found_flag.equals("YES"))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Listbox NOT Found ****************";
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Select from a List By Visible Text (Name: " + fieldname + ", Value: " + fieldvalue + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
		
	}
	
	//******************************************************************************
	//*
	//*	Name: 	fw_check_for_loading_page
	//*	Author: Mark Elking
	//*	Date: 	10/26/2016
	//*
	//******************************************************************************
	
	public String fw_check_for_loading_page(String tagname, String text_to_look_for, String number_loops, String milliseconds_to_wait_per_loop, String attribute_name) throws InterruptedException, IOException 
	{
		
		String text_msg = "";
		String ret_cd = "0";
		String found_flag = "no";
				
		String[] waitandtagname_arr = tagname.split("--");
		long initial_milliseconds_to_wait = 0;
		
		if (waitandtagname_arr.length == 2)
		{
			initial_milliseconds_to_wait = Long.valueOf(waitandtagname_arr[0]);
			Thread.sleep(initial_milliseconds_to_wait);
			tagname = waitandtagname_arr[1];	
		}
		else
		{
			tagname = waitandtagname_arr[0];
		}
		
		long ms_to_wait_per_loop;
		ms_to_wait_per_loop = Long.valueOf(milliseconds_to_wait_per_loop);
		
		int num_loops;
		num_loops = Integer.valueOf(number_loops);
		
		int x;
		long total_milliseconds = 0;
		
		if (attribute_name.equals("NA"))
		{
		
			for (x=1;x<num_loops;x++)
			{	
				
				//if (x == 1)
				//{
				Thread.sleep(ms_to_wait_per_loop);
				//}
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));		
				Iterator<WebElement> iter = rows.iterator();
				
				//Thread.sleep(1000);
				
				while (iter.hasNext()) {
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(text_to_look_for))
					{
						found_flag = "yes";
						total_milliseconds = ms_to_wait_per_loop * x;
						//x = num_loops+2;
						break;
					}
				}
			
				if (found_flag.equals("yes"))
				{
					break;
				}
				else
				{
					Thread.sleep(ms_to_wait_per_loop);
				}
			}
		
		}
		else
		{
			// Added by Mark on 1/14/2017
			
			for (x=1;x<num_loops;x++)
			{	
				
				//if (x == 1)
				//{
				//	Thread.sleep(ms_to_wait_per_loop);
				//}
				
				List<WebElement> rows = driver.findElements(By.tagName(tagname));		
				Iterator<WebElement> iter = rows.iterator();
				
				//Thread.sleep(1000);
				
				while (iter.hasNext()) {
					WebElement item = iter.next();
					String label = item.getAttribute(attribute_name).trim();
					
					if (label.contains(text_to_look_for))
					{
						found_flag = "yes";
						total_milliseconds = ms_to_wait_per_loop * x;
						break;
					}
				}
				
				if (found_flag.equals("yes"))
				{
					break;
				}
				else
				{
					Thread.sleep(ms_to_wait_per_loop);
				}
			
			}
			// End of Added by Mark on 1/14/2017
		}
		
		if (found_flag == "yes")
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Tag NOT Found ********************";
			ret_cd = "1";
		}
		
		log.fw_writeLogEntry("Check for Loading Page (FOUND, Tag: " + tagname + ", Text: " + text_to_look_for + ", # Loops: " + number_loops + ", Msecs to Wait per Loop: " + milliseconds_to_wait_per_loop + ", Msecs Elapsed: " + total_milliseconds + ", Attribute Name: " + attribute_name + ")" + text_msg, ret_cd);
		
		return found_flag;
		
	}

	/**
	 * This function gets text from page.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 12/01/2016
	 * 
	 */
	
	public String fw_get_text(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException {
		
		// NEW CODE
		
		return_get_text = "";
		String text_msg = "";
		String ret_cd = "0";
		String ready_flag = "";
		boolean field_isEmpty;
		boolean field_isEnabled;
		
		if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
		{
			for (int m=1;m<10;m++)
			{
				field_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
				field_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
				
				if(!field_isEmpty && field_isEnabled)
				{
					ready_flag = "YES";
					return_get_text = driver.findElement(fw_get_element_object(locator, locatorvalue)).getText();
					break;
				}
				
				Thread.sleep(1000);
				
			}
		}
		else if (locator.equals("NA")) 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getText().trim();
				
				if (label.contains(locatorvalue))
				{
					ready_flag = "YES";
					return_get_text = item.getText();
					break;
				}
			}
		}
		else 
		{
			
			List<WebElement> rows = driver.findElements(By.tagName(tagname));
			Iterator<WebElement> iter = rows.iterator();
			
			while (iter.hasNext()) 
			{
				WebElement item = iter.next();
				String label = item.getAttribute(locator);
				
				if (label.contains(locatorvalue))
				{
					ready_flag = "YES";
					return_get_text = item.getText();
					break;
				}
			}
		}
		
		if (ready_flag.equals("YES"))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Field NOT Found or Field Disabled ****************";
			ret_cd = "1";
		}
				
		log.fw_writeLogEntry("Get Text (Name: " + fieldname + ", Value: " + fieldvalue + ", Text: " + return_get_text + ")" + text_msg, ret_cd);
			
		Thread.sleep(milliseconds_to_wait);
		
		return return_get_text;
		
	}
	
	/**
	 * This function quits the driver.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 12/09/2016
	 * 
	 */
	
	public void fw_quit_driver() throws InterruptedException
	{
		if (driver != null)
		{
			driver.close();
			driver.quit();
		}
	}

	/**
	 * This function validates text.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 1/23/2017
	 * 
	 */
	
	public void fw_validate_text(String fieldname, String expected_text, String actual_text) throws InterruptedException, IOException
	{
		
		String ret_cd = "";
		String text_msg = "";
		
		if (actual_text.contains(expected_text))
		{
			ret_cd = "0";
		}
		else
		{
			text_msg = "*************** Expected Text NOT Contained in Actual Text ****************";
			ret_cd = "1";
		}
				
		log.fw_writeLogEntry("Validate Text (Name: " + fieldname + ", Expected: " + expected_text + ", Actual: " + actual_text + ")" + text_msg, ret_cd);
		
	}
	
	/**
	 * 
	 * * This function gets list of test cases to execute from spreadsheet.
	 * 
	 * @param input_filename
	 * @author Mark Elking
	 * @since 1/04/2017
	 * @return 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	 
	public String fw_get_list_of_test_cases_to_execute(String input_filename) throws IOException, InterruptedException
	{
		String workspace_name = fw_get_workspace_name();

		MSExcel xls = new MSExcel(workspace_name + "\\maps\\" + input_filename);
		String tab_name = "ExecParms";
		int Row = xls.getRowCount(tab_name);
		String ep_sheetname = "";
		String ep_value = "";
		String test_cases_to_execute_list = "";
		
		for (int x=2;x<Row+1;x++)
		{
			ep_sheetname = xls.getCellData(tab_name, "SheetName", x);
			ep_value = xls.getCellData(tab_name, "Value", x);
			
			if (ep_sheetname.startsWith("TC") && ep_value.contains("Y"))
			{
				test_cases_to_execute_list = test_cases_to_execute_list + ep_sheetname + ",";
			}
		}
		
		if (!test_cases_to_execute_list.isEmpty())
		{
			int test_cases_to_execute_list_len = test_cases_to_execute_list.length();
			test_cases_to_execute_list = test_cases_to_execute_list.substring(0, test_cases_to_execute_list_len-1);
		}
		
		return test_cases_to_execute_list;
		
	}
	
	/**
	 * This function takes object name and performs respective webui event on that object.
	 * 
	 * @param tab_name
	 * @param event_name
	 * @param in_object_key_name
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws ParseException 
	 * @since 12/27/2016
	 */
	
	public void fw_event (String configuration_map_fullpath, String tab_name, String tc_event_name, String tc_object_name, String tc_test_data, String object_to_look_for_after_object_event, String milliseconds_to_wait_after_object_event) throws InterruptedException, IOException, ParseException{
		
		String object_objectname = "";
		String object_tagnameattributevalue = "";
		String object_extrainfo = "";
		String object_tagname = "";
		String object_attribute = "";
		String object_attributevalue = "";
		
		String workspace_name = fw_get_workspace_name();
		String variables_path = workspace_name + "\\variables\\";
		String responses_path = workspace_name + "\\webservices\\responses";
		
		MSExcel xls = new MSExcel(configuration_map_fullpath);
				
		out_object_testdata = tc_test_data;
		
		int RowObjects = xls.getRowCount("Object");
		for (int y=2;y<RowObjects+1;y++)
		{
			object_objectname = xls.getCellData("Object", "ObjectName", y);
			object_tagnameattributevalue = xls.getCellData("Object", "TagName,Attribute,Value", y);
			object_extrainfo = xls.getCellData("Object", "ExtraInfo", y);
			
			if (object_objectname.equals(tc_object_name))
			{
				String[] object_tagnameattributevalue_arr = object_tagnameattributevalue.split(",");
				
				object_tagname = object_tagnameattributevalue_arr[0];
				object_attribute = object_tagnameattributevalue_arr[1];
				object_attributevalue = object_tagnameattributevalue_arr[2];
				break;
			}
			
		}
		
		if (tc_event_name.equals("XMLExecute"))
		{
			
			String[] tc_object_name_arr = tc_object_name.split("_");
			int arr_length = tc_object_name_arr.length;
		
			String webservice_name = "";
			String column_name = "";
			
			if (arr_length == 3)
			{
				webservice_name = tc_object_name_arr[1];
				column_name = tc_object_name_arr[2];
			}
			else
			{
				webservice_name = tc_object_name_arr[1];
				column_name = webservice_name;
			}
				
			int RowEnvironment = xls.getRowCount("Environment");
			String current_endpoint = "";
			String current_credentials = "";
			for (int h=2;h<RowEnvironment+1;h++)
			{
				String current_environment = xls.getCellData("Environment", "Environment", h);
				current_endpoint = xls.getCellData("Environment", column_name + "ENDPOINT", h);
				current_credentials = xls.getCellData("Environment", column_name + "CREDS", h);
				
				if (current_credentials.contains(":"))
				{
					String[] current_credentials_arr = current_credentials.split(":");
					String userid = current_credentials_arr[0];
					String passid = current_credentials_arr[1];
					fw_set_variable("WSUSERID", userid);
					fw_set_variable("WSPASSID", passid);
				}
				else
				{
					fw_set_variable("WSUSERID", "");
					fw_set_variable("WSPASSID", "");
				}
				
				if (current_environment.equals("UAT"))
				{
					break;
				}
				
			}
			
			
			// NEW
			int num_loops = 1;
			long wait_per_loop = 0;
			String execute_and_validate_flag = "";
			String text_to_look_for_in_response = "";
			
			if (object_to_look_for_after_object_event.contains("--"))
			{
				String[] objecttolookforafterobjectevent_arr = object_to_look_for_after_object_event.split("--");
				text_to_look_for_in_response = objecttolookforafterobjectevent_arr[0];
				num_loops = Integer.valueOf(objecttolookforafterobjectevent_arr[1]);
				wait_per_loop = Long.valueOf(objecttolookforafterobjectevent_arr[2]);
				
				execute_and_validate_flag = "YES";
				
				log.fw_writeLogEntry("", "NA");
				log.fw_writeLogEntry("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "NA");
				log.fw_writeLogEntry("Webservice Loop Check.......", "NA");
				log.fw_writeLogEntry("     Webservice:          " + webservice_name, "NA");
				log.fw_writeLogEntry("     # Loops:             " + num_loops, "NA");
				log.fw_writeLogEntry("     Msecs Wait per Loop: " + wait_per_loop, "NA");
				log.fw_writeLogEntry("", "NA");
				
			}
			// END OF NEW
						
			String return_code = "";
			String text_msg = "";
			String ret_cd = "";
			String text_to_look_for_returned = "";
			String return_code_output = "";
			int difference = 0;
			
			for (int r=0;r<num_loops;r++)
			{
				
				fw_execute_xml(webservice_name, current_endpoint, current_credentials, tc_test_data, 0);
				
				if (execute_and_validate_flag.equals("YES"))
				{
					text_msg = "";
					
					return_code_output = fw_validate_text_in_xml_response(webservice_name, text_to_look_for_in_response, "NO", 0);
					
					String[] return_code_arr = return_code_output.split("--");
					return_code = return_code_arr[0];
					text_to_look_for_returned = return_code_arr[1];
					
					if (return_code.equals("0"))
					{
						ret_cd = "0";
						log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + webservice_name + ", Text to Look for: " + text_to_look_for_returned + ")" + text_msg, ret_cd);
						break;
					}
					else
					{
						difference = num_loops - r;
						if (difference == 1)
						{
							ret_cd = "1";
							text_msg = "*****" + text_to_look_for_returned + " NOT found in Webservice response " + webservice_name + "***";
							log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + webservice_name + ", Text to Look for: " + text_to_look_for_returned + ")" + text_msg, ret_cd);

							log.fw_writeLogEntry("", "NA");
							log.fw_writeLogEntry("End of Webservice Loop Check.......", "NA");
							log.fw_writeLogEntry("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "NA");
							log.fw_writeLogEntry("", "NA");
						}
						else
						{
							ret_cd = "NA";
							text_msg = "*****" + text_to_look_for_returned + " NOT found in Webservice response " + webservice_name + "***";
							log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + webservice_name + ", Text to Look for: " + text_to_look_for_returned + ")" + text_msg, ret_cd);
							Thread.sleep(wait_per_loop);							
						}
					}
				}
			}
			
		}
		else if (tc_event_name.equals("XMLValidateTextinXMLResponse"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String webservice_name = tc_object_name_arr[1];
			
			fw_validate_text_in_xml_response(webservice_name, tc_test_data, "YES", 0);
		}
		else if (tc_event_name.equals("XMLValidateTextNOTinXMLResponse"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String webservice_name = tc_object_name_arr[1];
			
			fw_validate_text_notin_xml_response(webservice_name, tc_test_data, "YES", 0);
		}
		else if (tc_event_name.equals("XMLGetValueByTagname"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String webservice_name = tc_object_name_arr[1];
			
			fw_get_value_from_xml_based_on_tagname(webservice_name, tc_test_data, 0);
		}
		else if (tc_event_name.equals("XMLGetValueByMultipleTagnames"))
		{
			String[] tc_object_name_arr = tc_object_name.split("_");
			String webservice_name = tc_object_name_arr[1];
			
			fw_get_value_from_xml_based_on_multiple_tagnames(webservice_name, tc_test_data, 0);
		}
		else if (tc_event_name.equals("XMLCompleteTPV"))
		{			
			fw_complete_tpv(tc_test_data);
		}
		else if (tc_event_name.equals("IncrementValueByOne"))
		{			
			fw_increment_value_by_one(tc_test_data);
		}
		else if (tc_event_name.equals("GetCurrentDate"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String variable_name = tc_test_data_arr[0];
			String date_format = tc_test_data_arr[1];
			
			dt.fw_generate_datetime_current(variable_name, date_format);
		}
		else if (tc_event_name.equals("GetFutureDate"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String variable_name = tc_test_data_arr[0];
			String date_format = tc_test_data_arr[1];
			int number_of_days = Integer.valueOf(tc_test_data_arr[2]);
			
			dt.fw_generate_datetime_future(variable_name, date_format, number_of_days);
		}
		else if (tc_event_name.equals("SetVariable"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String variable_name = tc_test_data_arr[0];
			String variable_value = tc_test_data_arr[1];
			
			fw_set_variable(variable_name, variable_value);
		}
		else if (tc_event_name.equals("GetWindowHandle"))
		{
			fw_get_window_handle();
		}
		else if (tc_event_name.equals("LoginToSSO"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String user_id = tc_test_data_arr[0];
			String pass_id = tc_test_data_arr[1];
			
			fw_login_to_SSO(user_id, pass_id);
		}
		else if (tc_event_name.equals("SwitchToWindow"))
		{
			fw_switch_to_window(tc_test_data);
		}
		else if (tc_event_name.equals("SwitchToNewWindow"))
		{
			fw_switch_to_new_window();
		}
		else if (tc_event_name.equals("ValidateText"))
		{
			String[] tc_test_data_arr = tc_test_data.split(",");
			String expected_value = tc_test_data_arr[0];
			String actual_value = tc_test_data_arr[1];
			fw_validate_text(tc_object_name, expected_value, actual_value);
		}
		else if (tc_event_name.equals("LaunchBrowser"))
		{
			fw_launch_browser(tc_test_data);
		}
		else if (tc_event_name.equals("NavigateToURL"))
		{
			String[] url_column_header_arr = tc_object_name.split("_");
			String url_column_header_value = url_column_header_arr[1];
			String output_url_value = "";
			
			int EnvRowObjects = xls.getRowCount("Environment");
			for (int s=2;s<EnvRowObjects+1;s++)
			{
				String environment_value = xls.getCellData("Environment", "Environment", s);
				String url_value = xls.getCellData("Environment", url_column_header_value, s);
				
				if (environment_value.equals(tc_test_data))
				{
					output_url_value = url_value;		
					s=EnvRowObjects+2;
				}
			}
			
			fw_navigate_to_url(output_url_value);
		}
		else if (tc_event_name.equals("TerminateWindowProcesses"))
		{
			fw_terminate_window_processes(tc_test_data);	
		}
		else if (tc_event_name.equals("EnterDataTextbox"))
		{
			fw_enter_data_into_text_field(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("ClickAndEnterData"))
		{
			fw_click_and_enter_data(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("GetAttribute"))
		{
			fw_get_attribute_value(object_attribute, object_attributevalue, tc_test_data, 0);
		}
		else if (tc_event_name.equals("WriteLogHeader"))
		{
			log.fw_writeLogEntry(tc_test_data, "LOGHEADER");
		}
		else if (tc_event_name.equals("CheckForElementExistence"))
		{
			fw_check_element_existence(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("AcceptAlert"))
		{
			fw_accept_alert();
		}
		else if (tc_event_name.equals("SwitchToDriver"))
		{
			fw_switch_to_driver(tc_test_data);
		}
		else if (tc_event_name.equals("SelectCheckbox"))
		{
			fw_select_checkbox(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);
			
			if (!object_extrainfo.contains("NO"))
			{
				if (out_object_extrainfo.isEmpty())
				{
					out_object_extrainfo = object_extrainfo;
				}
				else
				{
					out_object_extrainfo = out_object_extrainfo + "," + object_extrainfo;
				}
			}	
		}
		else if (tc_event_name.equals("ClickJAVASCRIPT"))
		{
			fw_click_element_using_javascript(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);
			
			if (!object_extrainfo.contains("NO"))
			{
				if (out_object_extrainfo.isEmpty())
				{
					out_object_extrainfo = object_extrainfo;
				}
				else
				{
					out_object_extrainfo = out_object_extrainfo + "," + object_extrainfo;
				}
			}
			
		}
		else if (tc_event_name.equals("ClickButton"))
		{
			fw_click_button(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);
		}
		else if (tc_event_name.equals("SelectListValueByValue"))
		{
			fw_select_from_a_list_by_value(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("SelectListValueByVisibleText"))
		{
			fw_select_from_a_list_by_visible_text(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);	
		}
		else if (tc_event_name.equals("GetText"))
		{
			fw_get_text(tc_object_name, object_tagname, object_attribute, object_attributevalue, tc_test_data, 0);
		}
		else if (tc_event_name.equals("StopExecution"))
		{
			System.out.println(" ");
			System.out.println(" ***** Stopping Execution ****** ");
			System.out.println(" ");
			System.exit(0);
		}
		
		if (!object_to_look_for_after_object_event.equals("NA") && !object_to_look_for_after_object_event.isEmpty()) 
		{
			if (tc_event_name.equals("XMLExecute"))
			{
				
			}
			else
			{
				String[] objecttolookforafterobjectevent_arr = object_to_look_for_after_object_event.split(",");
				int arr_length = objecttolookforafterobjectevent_arr.length;
				
				String tagname = objecttolookforafterobjectevent_arr[0];
				String text_to_look_for = objecttolookforafterobjectevent_arr[1];
				String number_loops = objecttolookforafterobjectevent_arr[2];
				String milliseconds_to_wait_per_loop = objecttolookforafterobjectevent_arr[3];
				
				String attribute_name = "NA";
				if (arr_length == 5)
				{
					attribute_name = objecttolookforafterobjectevent_arr[4];
				}
				
				fw_check_for_loading_page(tagname, text_to_look_for, number_loops, milliseconds_to_wait_per_loop, attribute_name);
			}
		}

		if (!milliseconds_to_wait_after_object_event.equals("0") && !milliseconds_to_wait_after_object_event.isEmpty())
		{
			Thread.sleep(Long.valueOf(milliseconds_to_wait_after_object_event));		
		}
		
	}
	
	/**
	 *
	 * This function executes an XML Request.
	 *  
	 * @param fileInput
	 * @param endpoint
	 * @param creds
	 * @param in_string
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 12/30/2016
	 */
	
	public String fw_execute_xml(String fileInput, String endpoint, String creds, String in_string, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "0";
		//String found_flag = "";
		String outputString = "";
		
		if (!fileInput.isEmpty() && !endpoint.isEmpty())
		{
		
			String workspace_name = fw_get_workspace_name();
			String template_input_file = workspace_name + "\\webservices\\templates\\" + fileInput;
			
			//Code to make a webservice HTTP request
			String responseString = "";
			
			URL url = new URL(endpoint);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection)connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			
			// Get input file and convert to string
			
			String xmlInput = "";				
			FileReader fileReader = null;
			BufferedReader bfrReader = null;
			String strLine = null;
	
			fileReader = new FileReader(template_input_file);
			bfrReader = new BufferedReader(fileReader);
			while ((strLine = bfrReader.readLine()) != null)
			{
				
				String[] in_string_subs_arr = in_string.split("--");
				
				for (int f=0;f<in_string_subs_arr.length;f++)
				{
					String in_string_sub = in_string_subs_arr[f];
					
					String[] in_string_arr = in_string_sub.split(",");
					String in_string_variable_name = in_string_arr[0];
					String in_string_value = in_string_arr[1];
					
					if (strLine.contains(in_string_variable_name))
					{
						int pos_string = strLine.indexOf(in_string_variable_name);
						String first_str = strLine.substring(0, pos_string);
						int len_strLine = strLine.length();
						int len_in_string_variable_name = in_string_variable_name.length();
						int len_first_str = first_str.length();
						int start_pos = len_first_str + len_in_string_variable_name;
						String third_str = strLine.substring(start_pos, len_strLine);
						
						String sub_value = in_string_value;
						
						// Get dynamic value out of the file
						
						if (in_string_value.toUpperCase().contains("FILE"))
						{
							String[] in_string_value_arr = in_string_value.split("_");
							String in_variable_name = in_string_value_arr[1];
							
							String variable_file = workspace_name + "\\variables\\" + in_variable_name;
							String varInput = fw_get_value_from_file(variable_file);
							sub_value = varInput;
						}
						
						// End of Get dynamic value out of the file
						
						strLine = first_str + sub_value + third_str;
					}
				
				}
				
				xmlInput = xmlInput + strLine;
				
			}
			if (null != bfrReader)
			{
				bfrReader.close();
			}
			if (fileReader != null)
			{
				fileReader.close();
			}		
			
			// Write runtime request xml into output runtime request file

			String runtime_request_output_file = workspace_name + "\\webservices\\runtime\\requests\\" + fileInput;
			
			try(PrintWriter out = new PrintWriter(runtime_request_output_file))
			{
			    out.println(xmlInput);
			    out.close();
			}
			// End of Write runtime request xml into output runtime request file
	
			
			// Execute webservice
			
			byte[] buffer = new byte[xmlInput.length()];
			buffer = xmlInput.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();
			//httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			//httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConn.setRequestProperty("Content-Type", "text/xml");
			
			if (!creds.isEmpty())
			{
				String encodeCreds = "Basic " + new String(new Base64().encode(creds.getBytes()));
				httpConn.setRequestProperty("Authorization", encodeCreds);
			}
			
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			OutputStream out = httpConn.getOutputStream();
			//Write the content of the request to the outputstream of the HTTP Connection.
			out.write(b);
			out.close();
			//Ready with sending the request.
			 
			//Read the response.
			InputStreamReader isr =
			new InputStreamReader(httpConn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			 
			//Write the SOAP message response to a String.
			while ((responseString = in.readLine()) != null) {
			outputString = outputString + responseString;
			}
			

			// Write runtime request xml into output runtime request file
	
			String runtime_response_output_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
			
			try(PrintWriter outresponse = new PrintWriter(runtime_response_output_file))
			{
				outresponse.println(outputString);
				outresponse.close();
			}
			
			// End of Write runtime request xml into output runtime request file
			
			ret_cd = "0";
			
		}
		else
		{

			if (fileInput.isEmpty())
			{
				text_msg = "*************** fileInput is Not Defined ****************";
				ret_cd = "1";	
			}
			if (endpoint.isEmpty())
			{
				text_msg = "*************** endpoint is Not Defined ****************";
				ret_cd = "1";
			}
			/*if (creds.isEmpty())
			{
				text_msg = "*************** creds are Not Defined ****************";
				ret_cd = "1";
			}
			*/
			
		}
		
		log.fw_writeLogEntry("Execute XML (Name: " + fileInput + ", Value: " + in_string + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
		
		
		return outputString;
	
	}

	/**
	 *
	 * This function gets a value from an XML file based on tagname.
	 *  
	 * @param fileInput
	 * @param tagname_to_look_for
	 * @param milliseconds_to_wait
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 1/27/2017
	 */
	
	public void fw_get_value_from_xml_based_on_tagname(String fileInput, String tagname_to_look_for, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "0";
		
		String workspace_name = fw_get_workspace_name();
		String xml_response_input_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
		String xmlInput = fw_get_value_from_file(xml_response_input_file);
		
		// Find String
		
		String input_string = xmlInput;
		String current_character;
		String output_value = "";
		String start_index_found_flag = "";
		
		int pos_string = input_string.indexOf("<" + tagname_to_look_for + ">");
		int start_index = 0;
		int end_index = 0;
		
		if (pos_string == -1)
		{
			text_msg = "*****<" + tagname_to_look_for + "> NOT found in Webservice response " + fileInput + "***";
			ret_cd = "1";
		}
		else
		{
			for (int p=1;p<75;p++)
			{
				current_character = input_string.substring(pos_string + p, pos_string + p + 1);
				if (current_character.equals(">"))
				{
					start_index = pos_string + p + 1;
					start_index_found_flag = "yes";
				}
				
				if (current_character.equals("<") && start_index_found_flag.equals("yes"))
				{
					end_index = pos_string + p;
					output_value = input_string.substring(start_index, end_index);
					ret_cd = "0";
					break;
				}
			}
		}
		
		
		// End of Find String
		
		// Write variable value into variable file
		
		String runtime_request_output_file = workspace_name + "\\variables\\" + tagname_to_look_for;
		
		try(PrintWriter out = new PrintWriter(runtime_request_output_file))
		{
		    out.println(output_value);
		    out.close();
		}
		
		// End of Write variable value into variable file
		
		log.fw_writeLogEntry("Get Value from XML Response by Tag Name (XML Response File: " + fileInput + ", Tagname to Look For: " + tagname_to_look_for + ", Value: " + output_value + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
	
	}

	/**
	 *
	 * This function gets a value from an XML file based on multiple tagnames.
	 *  
	 * @param fileInput
	 * @param input_string
	 * @param milliseconds_to_wait
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 2/7/2017
	 */
	
	public void fw_get_value_from_xml_based_on_multiple_tagnames(String fileInput, String tagname_string, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "0";
		String output_string = "";
		
		String workspace_name = fw_get_workspace_name();
		String xml_response_input_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
		String xmlInput = fw_get_value_from_file(xml_response_input_file);
		
		// Find String
		
		String[] tagname_string_arr = tagname_string.split("&&");
		String tagname_list = tagname_string_arr[0];
		String tagname_output_file = tagname_string_arr[1];
		String[] tagname_list_arr = tagname_list.split(",");
		int tagname_list_len = tagname_list_arr.length;
		
		String xml_response_string = xmlInput;
		boolean continue_flag = true;
		int initial_pos_string = 0;
		int cur_pos_string = 1;
		String current_string = "";
		String tags_exceeded = "";
		
		while (continue_flag) 
		{
			
			tags_exceeded = "NO";
			
			for (int i=0;i<tagname_list_len;i++)
			{
				String[] tagname_arr = tagname_list_arr[i].split("--");
				String tagname_event = tagname_arr[0];
				String tagname_string_to_look_for = tagname_arr[1];
				
				// Substitute FILE_
				if (tagname_string_to_look_for.contains("FILE_"))
				{
					String[] tagname_string_to_look_for_arr = tagname_string_to_look_for.split("_");
					String text_to_validate_file = tagname_string_to_look_for_arr[1];
					
					String[] text_to_validate_file_arr = text_to_validate_file.split("##");
					String text_filename = text_to_validate_file_arr[0];
					String text_remaining = text_to_validate_file_arr[1];
					
					String variable_file = workspace_name + "\\variables\\" + text_filename;			

					FileReader fileReader2 = null;
					BufferedReader bfrReader2 = null;
					String strLine2;
					
					fileReader2 = new FileReader(variable_file);
					bfrReader2 = new BufferedReader(fileReader2);
					tagname_string_to_look_for = "";
					while ((strLine2 = bfrReader2.readLine()) != null)
					{	
						tagname_string_to_look_for = tagname_string_to_look_for + strLine2;	
					}
					if (null != bfrReader2)
					{
						bfrReader2.close();
					}
					if (fileReader2 != null)
					{
						fileReader2.close();
					}
					
					tagname_string_to_look_for = tagname_string_to_look_for + text_remaining;
					
				}
				// End of Substitute FILE_
				
				if (!tagname_event.contains("("))
				{
					cur_pos_string = xml_response_string.indexOf(tagname_string_to_look_for, cur_pos_string);
					initial_pos_string = cur_pos_string;
					if (cur_pos_string == -1)
					{
						continue_flag = false;
						ret_cd = "1";
						text_msg = tagname_string_to_look_for + " not found";
						break;
					}
				}
				else
				{
					int left_paren_pos = tagname_event.indexOf("(");
					int right_paren_pos = tagname_event.indexOf(")");
					String number_tags = tagname_event.substring(left_paren_pos + 1, right_paren_pos);
					int num_tags = Integer.valueOf(number_tags);
					int cur_tags = 0;
					int tagname_string_to_look_for_len = tagname_string_to_look_for.length();
					String count_tag_string = "";
					
					if (tagname_event.contains("(") && tagname_event.contains("SEARCHFORWARD"))
					{	
						for (int j=cur_pos_string;j<30000;j++)
						{
							current_string = xml_response_string.substring(j, j + tagname_string_to_look_for_len);
							count_tag_string = xml_response_string.substring(j, j + 2);
							
							if (current_string.equals(tagname_string_to_look_for))
							{
								cur_pos_string = j;
								break;
							}
							
							if (count_tag_string.equals("</"))
							{
								cur_tags = cur_tags + 1;
								if (cur_tags == num_tags)
								{
									tags_exceeded = "YES";
									ret_cd = "1";
									text_msg = "Forward Current tags greater than the number expected";
									break;
								}
							}
							
						}
					}
					else if (tagname_event.contains("(") && tagname_event.contains("SEARCHBACKWARD"))
					{
						for (int j=cur_pos_string;j>0;j--)
						{
							current_string = xml_response_string.substring(j, j + tagname_string_to_look_for_len);
							count_tag_string = xml_response_string.substring(j, j + 2);
							
							if (current_string.equals(tagname_string_to_look_for))
							{
								cur_pos_string = j;
								break;
							}
							
							if (count_tag_string.equals("</"))
							{
								cur_tags = cur_tags + 1;
								if (cur_tags == num_tags)
								{
									tags_exceeded = "YES";
									ret_cd = "1";
									text_msg = "Backward Current tags greater than the number expected";
									break;
								}
							}
							
						}
					}	
				}
				
			}
			
			if (tags_exceeded.equals("YES"))
			{
				cur_pos_string = initial_pos_string + 5;	
			}
			else
			{
			
				int pos_output_begin = xml_response_string.indexOf(">",cur_pos_string + 2);
				int pos_output_end = xml_response_string.indexOf("<",cur_pos_string + 2);
				output_string = xml_response_string.substring(pos_output_begin + 1, pos_output_end);
		
				// Write variable value into variable file
				String runtime_request_output_file = workspace_name + "\\variables\\" + tagname_output_file;
				
				try(PrintWriter out = new PrintWriter(runtime_request_output_file))
				{
				    out.println(output_string);
				    out.close();
				}
				// End of Write variable value into variable file
				
				ret_cd = "0";
				continue_flag=false;
				
			}
			
		}
		
		log.fw_writeLogEntry("Get Value from XML Response based on Multiple Tagnames (XML Response File: " + fileInput + ", Tagname String: " + tagname_string + ", Output File: " + tagname_output_file + ", Output String: " + output_string + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
	
	}
	
	/**
	 *
	 * This function get the content of a file and put into a string value.
	 *  
	 * @param file_name
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 2/5/2017
	 */

	public String fw_get_value_from_file(String file_name) throws IOException 
	{
		
		String variable_value = "";				
		FileReader fileReader = null;
		BufferedReader bfrReader = null;
		String strLine = null;
		
		fileReader = new FileReader(file_name);
		bfrReader = new BufferedReader(fileReader);
		while ((strLine = bfrReader.readLine()) != null)
		{	
			variable_value = variable_value + strLine;
		}
		if (null != bfrReader)
		{
			bfrReader.close();
		}
		if (fileReader != null)
		{
			fileReader.close();
		}		
		
		return variable_value;
		
	}
	
	/**
	 *
	 * This function gets workspace name.
	 *  
	 * @author Mark Elking
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @since 2/15/2017
	 */

	public String fw_get_workspace_name() throws IOException, InterruptedException 
	{
		
		String workspace_name = System.getProperty("user.dir");
		if (!workspace_name.contains("\\")) 
		{
			workspace_name.replace("\\","\\\\");	
		}
		
		return workspace_name;
		
	}
	
	/**
	 *
	 * This function validates text in XML response.
	 *  
	 * @param fileInput
	 * @param text_to_validate
	 * @param write_out_log_entry_flag
	 * @param milliseconds_to_wait
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 1/27/2017
	 */

	public String fw_validate_text_in_xml_response(String fileInput, String text_to_validate, String write_out_log_entry_flag, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "";
		
		String workspace_name = fw_get_workspace_name();
		String xml_response_input_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
		String xmlInput = fw_get_value_from_file(xml_response_input_file);
		
		// Build Text to Validate String
		
		//FileReader fileReader2 = null;
		//BufferedReader bfrReader2 = null;
		
		String[] text_to_validate_arr = text_to_validate.split(",");
		int text_to_validate_arr_length = text_to_validate_arr.length;
		String text_to_validate_current;
		
		String strLine2;
		String text_to_validate_final = "";
		
		for (int p=0;p<text_to_validate_arr_length;p++)
		{
			text_to_validate_current = text_to_validate_arr[p];
			
			//strLine2 = null;
			
			if (text_to_validate_current.contains("FILE_"))
			{
				String[] text_to_validate_current_arr = text_to_validate_current.split("_");
				String text_to_validate_file = text_to_validate_current_arr[1];
						
				String variable_file = workspace_name + "\\variables\\" + text_to_validate_file;			
				strLine2 = fw_get_value_from_file(variable_file);
				text_to_validate_final = text_to_validate_final + strLine2;
				
				/*
				fileReader2 = new FileReader(variable_file);
				bfrReader2 = new BufferedReader(fileReader2);
				while ((strLine2 = bfrReader2.readLine()) != null)
				{	
					text_to_validate_final = text_to_validate_final + strLine2;	
				}
				if (null != bfrReader2)
				{
					bfrReader2.close();
				}
				if (fileReader2 != null)
				{
					fileReader2.close();
				}
				*/
			}
			else
			{
				text_to_validate_final = text_to_validate_final + text_to_validate_current;
			}
		}
		
		// End of Build Text to Validate String
		
				
		
		// Find String
		
		int pos_string = xmlInput.indexOf(text_to_validate_final);
		
		if (pos_string == -1)
		{
			text_msg = "*****" + text_to_validate_final + " NOT found in Webservice response " + fileInput + "***";
			ret_cd = "1";
		}
		else
		{
			ret_cd = "0";
		}
		
		String return_string = ret_cd + "--" + text_to_validate_final;
		
		// End of Find String

		if (write_out_log_entry_flag.toUpperCase().equals("YES"))
		{
			log.fw_writeLogEntry("Validate Text in XML Response (XML Response File: " + fileInput + ", Text to Look for: " + text_to_validate_final + ")" + text_msg, ret_cd);
		}
		
		Thread.sleep(milliseconds_to_wait);
	
		return return_string;
		
	}

	/**
	 *
	 * This function validates text NOT in XML response.
	 *  
	 * @param fileInput
	 * @param text_to_validate
	 * @param write_out_log_entry_flag
	 * @param milliseconds_to_wait
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 1/27/2017
	 */

	public String fw_validate_text_notin_xml_response(String fileInput, String text_to_validate, String write_out_log_entry_flag, long milliseconds_to_wait) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String ret_cd = "";
		
		String workspace_name = fw_get_workspace_name();
		String xml_response_input_file = workspace_name + "\\webservices\\runtime\\responses\\" + fileInput;
		String xmlInput = fw_get_value_from_file(xml_response_input_file);
		
		// Build Text to Validate String
		
		//FileReader fileReader2 = null;
		//BufferedReader bfrReader2 = null;
		
		String[] text_to_validate_arr = text_to_validate.split(",");
		int text_to_validate_arr_length = text_to_validate_arr.length;
		String text_to_validate_current;
		
		String strLine2;
		String text_to_validate_final = "";
		
		for (int p=0;p<text_to_validate_arr_length;p++)
		{
			text_to_validate_current = text_to_validate_arr[p];
			
			//strLine2 = null;
			
			if (text_to_validate_current.contains("FILE_"))
			{
				String[] text_to_validate_current_arr = text_to_validate_current.split("_");
				String text_to_validate_file = text_to_validate_current_arr[1];
				
				String variable_file = workspace_name + "\\variables\\" + text_to_validate_file;			
				strLine2 = fw_get_value_from_file(variable_file);
				text_to_validate_final = text_to_validate_final + strLine2;
				
				/*
				fileReader2 = new FileReader(variable_file);
				bfrReader2 = new BufferedReader(fileReader2);
				while ((strLine2 = bfrReader2.readLine()) != null)
				{	
					text_to_validate_final = text_to_validate_final + strLine2;	
				}
				if (null != bfrReader2)
				{
					bfrReader2.close();
				}
				if (fileReader2 != null)
				{
					fileReader2.close();
				}
				*/
			}
			else
			{
				text_to_validate_final = text_to_validate_final + text_to_validate_current;
			}
		}
		
		// End of Build Text to Validate String
		
				
		
		// Find String
		
		int pos_string = xmlInput.indexOf(text_to_validate_final);
		
		if (pos_string == -1)
		{
			ret_cd = "0";
		}
		else
		{
			ret_cd = "1";
			text_msg = "*****" + text_to_validate_final + " unexpectedly found in Webservice response " + fileInput + "***";
		}
		
		String return_string = ret_cd + "--" + text_to_validate_final;
		
		// End of Find String

		if (write_out_log_entry_flag.toUpperCase().equals("YES"))
		{
			log.fw_writeLogEntry("Validate Text NOT in XML Response (XML Response File: " + fileInput + ", Text to Look for: " + text_to_validate_final + ")" + text_msg, ret_cd);
		}
		
		Thread.sleep(milliseconds_to_wait);
	
		return return_string;
		
	}
	
	/**
	 *
	 * This function sets a variable.
	 *  
	 * @param variable_name
	 * @param variable_value
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 1/30/2017
	 */
	
	public void fw_set_variable(String variable_name, String variable_value) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String output_string = "";
		String file_name = "";
		
		String workspace_name = fw_get_workspace_name();
		
		if (variable_value.contains("FILE_"))
		{
			
			String[] variable_value_arr = variable_value.split("--");
			
			int num_strings = variable_value_arr.length;
			String current_string = "";
			
			for (int y=0;y<num_strings;y++)
			{
				
				current_string = variable_value_arr[y];
			
				if (current_string.contains("FILE_"))
				{
					String[] current_string_arr = current_string.split("_");
					file_name = current_string_arr[1];
					
					String variable_file = workspace_name + "\\variables\\" + file_name;
					String varInput = fw_get_value_from_file(variable_file);

					output_string = output_string + varInput;
				
				}
				else
				{
					output_string = output_string + current_string;
				}
				
			}
		}
		else
		{
			output_string = variable_value;
		}		
		
		String variable_file = "";
		
		if (variable_name.equals("WORKSPACE"))
		{
			variable_file = "C:\\Temp\\" + variable_name;	
		}
		else
		{
			variable_file = workspace_name + "\\variables\\" + variable_name;	
		}
		
		try(PrintWriter out = new PrintWriter(variable_file))
		{
		    out.println(output_string);
		    out.close();
		}
		
		log.fw_writeLogEntry("Set Variable (Name: " + variable_name + ", Input Value: " + variable_value + ", Output Value: " + output_string + ")" + text_msg, "0");
	
	}
	
	/**
	 *
	 * This function completes TPV.
	 *  
	 * @param phone_number_file
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 2/8/2017
	 */
	
	public void fw_complete_tpv(String phone_number_file) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String input_value = "";
		
		String workspace_name = fw_get_workspace_name();
		//String completetpvexecutable = "C:\\Program Files\\Microsoft Office\\Office12\\excel.exe " + workspace_name + "\\webservices\\runtime\\tpv\\CompleteTPV.xls";
		String completetpvexecutable = workspace_name + "\\webservices\\tpv\\CompleteTPV.xls";
		String variable_file = workspace_name + "\\variables\\" + phone_number_file;
		input_value = fw_get_value_from_file(variable_file);
		
		String output_file = workspace_name + "\\webservices\\tpv\\completeTPVphone";
		
		try(PrintWriter out = new PrintWriter(output_file))
		{
		    out.println(input_value);
		    out.close();
		}
		
		try
		{    
			//Runtime.getRuntime().exec(completetpvexecutable);
			Desktop.getDesktop().open(new File(completetpvexecutable));
        }
		catch(IOException  e)
		{
			e.printStackTrace();
        }  
		
		log.fw_writeLogEntry("XML Complete TPV (Variable File: " + variable_file + ", Runtime File: " + output_file + ")" + text_msg, "0");
	
	}
	
	/**
	 *
	 * This function increments a value by 1.
	 *  
	 * @param variable_name
	 * @param variable_value
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 2/8/2017
	 */
	
	public void fw_increment_value_by_one(String file_name) throws MalformedURLException, IOException, InterruptedException 
	{

		String text_msg = "";
		String input_value = "";
		
		String workspace_name = fw_get_workspace_name();
		String variable_file = workspace_name + "\\variables\\" + file_name;
		input_value = fw_get_value_from_file(variable_file);
		
		int output_value = Integer.valueOf(input_value) + 1;
		
		try(PrintWriter out = new PrintWriter(variable_file))
		{
		    out.println(output_value);
		    out.close();
		}
		
		log.fw_writeLogEntry("Increment Value by 1 (Name: " + file_name + ", New Value: " + output_value + ")" + text_msg, "0");
	
	}
	
	/**
	 * This function checks to see if element exists.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		fieldname - any text value representing the field.  Example is "Address".
	 * 		tagname - the tagname used to help search for the object on the page.  Example is "input".
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		fieldvalue - the value to put into the field once the object has been identified.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param fieldname
	 * @param tagname
	 * @param locator
	 * @param locatorvalue
	 * @param fieldvalue
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/13/2017
	 * 
	 */
	
	public Boolean fw_check_element_existence(String fieldname, String tagname, String locator, String locatorvalue, String fieldvalue, long milliseconds_to_wait) throws InterruptedException, IOException 
	{

		int num_loops = 1;
		long wait_time_per_loop = 1000;
		
		if (!fieldvalue.equals(""))
		{
			String[] fieldvalue_arr = fieldvalue.split(",");
			num_loops = Integer.valueOf(fieldvalue_arr[0]);
			wait_time_per_loop = Long.valueOf(fieldvalue_arr[1]);
		}
		
		String text_msg = "";
		String ret_cd = "";
		String field_ready_flag = "";
		boolean field_isEmpty=true;
		boolean field_isEnabled=false;
		boolean field_isDisplayed=false;
		return_existence=false;
		
		if (locator.equalsIgnoreCase("name") || locator.equalsIgnoreCase("id") || locator.equalsIgnoreCase("class") || locator.equalsIgnoreCase("css") || locator.equalsIgnoreCase("xpath") || locator.equalsIgnoreCase("link") || locator.equalsIgnoreCase("partiallink"))
		{
			for (int m=1;m<num_loops;m++)
			{
				field_isEmpty = driver.findElements(fw_get_element_object(locator, locatorvalue)).isEmpty();
				field_isEnabled = driver.findElement(fw_get_element_object(locator, locatorvalue)).isEnabled();
				field_isDisplayed = driver.findElement(fw_get_element_object(locator, locatorvalue)).isDisplayed();
				
				if(!field_isEmpty && field_isEnabled && field_isDisplayed)
				{
					field_ready_flag = "YES";
					break;
				}
				else
				{
					Thread.sleep(wait_time_per_loop);
				}
				
			}
		}
		else if (locator.equals("NA")) 
		{
			
			for (int m=1;m<num_loops;m++)
			{
				List<WebElement> rows = driver.findElements(By.tagName(tagname));
				Iterator<WebElement> iter = rows.iterator();
				
				while (iter.hasNext()) {
					WebElement item = iter.next();
					String label = item.getText().trim();
					
					if (label.contains(locatorvalue))
					{
						field_isEmpty = false;
						field_isEnabled = item.isEnabled();
						field_isDisplayed = item.isDisplayed();
						
						if(field_isEnabled && field_isDisplayed)
						{
							field_ready_flag = "YES";
							break;
						}
					}
				}
				
				if (field_ready_flag.equals("YES"))
				{
					break;
				}
			}
		}
		
		if (field_ready_flag.equals("YES"))
		{
			ret_cd = "0";
			return_existence = true;
		}
		else
		{
			text_msg = "*************** Field NOT Found or Field Disabled or Field Not Displayed ****************";
			ret_cd = "1";
			return_existence = false;
		}
		
		log.fw_writeLogEntry("Check Element Existence (Name: " + fieldname + ", Value: " + fieldvalue + ", Element Exists? " + return_existence + ", isEmpty: " + field_isEmpty + ", isEnabled: " + field_isEnabled + ", isDisplayed: " + field_isDisplayed + ")" + text_msg, ret_cd);
		
		Thread.sleep(milliseconds_to_wait);
		
		return return_existence;
			
	}

	/**
	 * This function returns the attribute value for the given attribute name.
	 * The inputs required for this function are fieldname, tagname, attribute, attributevalue, fieldvalue and milliseconds to wait.
	 * 		locator - the locator used to help identify an object on the page.  Example is "id" or "name".
	 * 		locatorvalue - the locator used to help identify an object on the page.  Example is "phoneNumber".
	 * 		attribute_name - the attribute name of the object which you want the value for.
	 * 		milliseconds - the time to wait after the action has been performed on the specified object.
	 * 
	 * @param locator
	 * @param locatorvalue
	 * @param attribute_name
	 * @param milliseconds_to_wait
	 * @throws InterruptedException
	 * 
	 * @author Mark Elking
	 * @throws IOException 
	 * @since 1/15/2017
	 * 
	 */
	
	public String fw_get_attribute_value(String locator, String locatorvalue, String attribute_name, long milliseconds_to_wait) throws InterruptedException, IOException 
	{

		String attribute_value = "";
		
		if (locator.equals("name")) 
		{
			attribute_value = driver.findElement(By.name(locatorvalue)).getAttribute(attribute_name);
		}
		else if (locator.equals("id")) 
		{
			attribute_value = driver.findElement(By.id(locatorvalue)).getAttribute(attribute_name);
		}
		else if (locator.equals("class")) 
		{
			attribute_value = driver.findElement(By.className(locatorvalue)).getAttribute(attribute_name);
		}
		else if (locator.equals("css")) 
		{
			attribute_value = driver.findElement(By.cssSelector(locatorvalue)).getAttribute(attribute_name);
		}
		else if (locator.equals("xpath")) 
		{
			attribute_value = driver.findElement(By.xpath(locatorvalue)).getAttribute(attribute_name);
		}
		
		log.fw_writeLogEntry("     Get Attribute Value (Locator: " + locator + ", Locator Value: " + locatorvalue + ", Attribute Name: " + attribute_name + ", Attribute Value: " + attribute_value + ")", "NA");
		
		Thread.sleep(milliseconds_to_wait);
		
		return attribute_value;
		
	}
	
}


package com.chirag.seleniumframework.testscripts;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import com.chirag.seleniumframework.util.NewExcelUtility;

public class DriverClass {

	WebDriver driver;

	public void openBrowser()
	{
		ArrayList<Object> browserData = NewExcelUtility.readTestData("Browser");
		for(int i=0;i<=(browserData.size()/2)-1;i++)
		{
			if(browserData.get(i).equals("mozilla"))
			{
				System.out.println("Firefox");
			} else if(browserData.get(i).equals("Chrome"))
			{
				System.out.println("Chrome");
			}

		}
	}
	
	public void navigate()
	{
		System.out.println("navigate Browser");
	} 
	
	public void input()
	{
		System.out.println("input Browser");
	}
	
	public void submit()
	{
		System.out.println("submit Browser");
	}
	
	public void verifyGoogleSearch()
	{
		System.out.println("verifyGoogleSearch Browser");
	}
	

	public void closeBrowser()
	{
		System.out.println("Close Browser");
	}

}

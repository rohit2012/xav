package com.csg.Testscripts;

import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.csg.reports.CustomReports;
import com.csg.reports.PDFGenerate;

@Listeners(CustomReports.class)
public class OSMCheckStatus {

	@Test
	public void loginStatus()
	{
		Reporter.log("Enter the username into userfield");
		throw new SkipException("This test is skipped due to login");
	}
}

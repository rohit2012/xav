package com.csg.Testscripts;


import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.csg.reports.CustomReports;

@Listeners(CustomReports.class)
public class OSMLogout {

	@Test
	public void logoutStatus()
	{
		System.out.println("Passed");
	}
}


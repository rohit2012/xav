package com.xavient.framework.util;

import java.lang.reflect.Method;
import org.testng.annotations.DataProvider;

public class TestCaseDataProvider {
	
	@DataProvider(name="getDataForWebApplicationSuite")
	public static Object[][] getDataForSuiteA(Method m){
		System.out.println(m.getName());
		String testCase=m.getName();
		System.out.println(Constants.SUITEWEBAPPLICATION_XLS_PATH);
		Xls_Reader xls = new Xls_Reader(Constants.SUITEWEBAPPLICATION_XLS_PATH);
		return Utility.getData(testCase,xls);
	}
	

	@DataProvider(name="getDataForAppiumSuite")
	public static Object[][] getDataForAppiumSuite(Method m){
		System.out.println(m.getName());
		String testCase=m.getName();
		Xls_Reader xls = new Xls_Reader(Constants.APPIUMSUITE_XLS_PATH);
		return Utility.getData(testCase,xls);
	}
	
}

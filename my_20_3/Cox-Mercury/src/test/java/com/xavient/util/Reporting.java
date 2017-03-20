package com.xavient.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Reporting extends BaseClass{

	

	public static String CaptureScreen(String name)
	{
		String ImagesPath = getPath()+"\\Reports\\Screenshot\\";
		TakesScreenshot oScn = (TakesScreenshot)driver;
		File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
		//File oDest = new File(ImagesPath+currentTime()+".jpg");
		File oDest = new File(ImagesPath+name+".jpg");
		try {
			FileUtils.copyFile(oScnShot, oDest);
		} catch (IOException e) {System.out.println(e.getMessage());}
		System.out.println(ImagesPath+name+".jpg");
		//System.out.println("Screenshot/"+currentTime()+".jpg");
		//return ImagesPath+currentTime()+".jpg";
		return ImagesPath+name+".jpg";
	}
	
	public static String getPath()
	{
		File file =  new File("");
		String path = file.getAbsolutePath();
		System.out.println(path);
		return path;
	}
	
	public static String currentTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		Date date = new Date();
		return sdf.format(date);
	}
	
	public static void deleteReport()
	{
		try {
			File f =  new File(getPath()+"\\Reports\\ExtentReport.html");
			if(f.exists())
			{
				f.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
}

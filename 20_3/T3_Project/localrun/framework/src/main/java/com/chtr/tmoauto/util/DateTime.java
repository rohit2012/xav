package com.chtr.tmoauto.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.chtr.tmoauto.logging.Logging;

public class DateTime {

	public static String generated_date_time_output;
	public static Logging log = new Logging();
	
	/**
	 * This function generates a date and/or time in any format that you provide.
	 * Examples include the following:
	 * 
	 * 		yyyyMMddkkmmss 	yields 	20161123023155		
	 * 		kk:mm:ss a 		yields 	12:19:17 PM
	 * 
	 * @author Mark Elking
	 * @throws FileNotFoundException 
	 * @since 12/05/2016
	 */
	
	public static void fw_generate_datetime(String datetime_format) throws FileNotFoundException 
	{
				
		generated_date_time_output = "";
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(datetime_format);
		String formatteddate = df.format(date);
		generated_date_time_output = formatteddate;
		
	}
	
	/**
	 * This function generates a FUTURE date and/or time in any format that you provide.
	 * Examples include the following:
	 * 
	 * 		yyyyMMddkkmmss 	yields 	20161123023155		
	 * 		kk:mm:ss a 		yields 	12:19:17 PM
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 1/30/2017
	 */
	
	public static void fw_generate_datetime_future(String variable_name, String datetime_format, int number_days_in_future) throws ParseException, InterruptedException, IOException 
	{

		String text_msg = "";
		
		generated_date_time_output = "";
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(datetime_format);
		String formatteddate = df.format(date);
		Calendar c = Calendar.getInstance();
		c.setTime(df.parse(formatteddate));
		c.add(Calendar.DATE, number_days_in_future);  // number of days to add
		formatteddate = df.format(c.getTime());  // dt is now the new date
		
		
		String workspace_name = System.getProperty("user.dir");
		if (!workspace_name.contains("\\")) 
		{
			workspace_name.replace("\\","\\\\");	
		}
		
		// Write variable value into variable file
		
		String outputfile = workspace_name + "\\variables\\" + variable_name;
		
		try(PrintWriter out = new PrintWriter(outputfile))
		{
		    out.println(formatteddate);
		    out.close();
		}
		
		// End of Write variable value into variable file
		
		log.fw_writeLogEntry("Generate Future Date (Name: " + variable_name + ", Format: " + datetime_format + ", # Days in Future: " + number_days_in_future + ", Future Date: " + formatteddate + ")" + text_msg, "0");
		
	}
	
	/**
	 * This function generates a FUTURE date and/or time in any format that you provide.
	 * Examples include the following:
	 * 
	 * 		yyyyMMddkkmmss 	yields 	20161123023155		
	 * 		kk:mm:ss a 		yields 	12:19:17 PM
	 * 
	 * @author Mark Elking
	 * @throws ParseException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 1/30/2017
	 */
	
	public static void fw_generate_datetime_current(String variable_name, String datetime_format) throws ParseException, InterruptedException, IOException 
	{

		String text_msg = "";
		
		generated_date_time_output = "";
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat(datetime_format);
		String formatteddate = df.format(date);
		
		String workspace_name = System.getProperty("user.dir");
		if (!workspace_name.contains("\\")) 
		{
			workspace_name.replace("\\","\\\\");	
		}
		
		// Write variable value into variable file
		
		String outputfile = workspace_name + "\\variables\\" + variable_name;
		
		try(PrintWriter out = new PrintWriter(outputfile))
		{
		    out.println(formatteddate);
		    out.close();
		}
		
		// End of Write variable value into variable file
		
		log.fw_writeLogEntry("Generate Current Date (Name: " + variable_name + ", Format: " + datetime_format + ", Current Date: " + formatteddate + ")" + text_msg, "0");
		
	}
	
}

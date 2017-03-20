package com.chtr.tmoauto.logging;

import com.chtr.tmoauto.webui.GUI;
import com.chtr.tmoauto.util.MSExcel;
import com.chtr.tmoauto.util.ALM;
import com.chtr.tmoauto.util.DateTime;

import com.mercury.qualitycenter.otaclient.ClassFactory;
import com.mercury.qualitycenter.otaclient.ICommand;
import com.mercury.qualitycenter.otaclient.IRecordset;
import com.mercury.qualitycenter.otaclient.ITDConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

public class Logging 
{
 
	GUI comm = new GUI();
	
	public static ALM fwalm = new ALM();
	public static Logging log = new Logging();
	public static DateTime dt = new DateTime();
	
	public static int step_total_cnt = 0;
	public static int step_passed_cnt = 0;
	public static int step_failed_cnt = 0;
	public static int step_fatal_cnt = 0;
	public static int step_error_cnt = 0;
	public static int step_warn_cnt = 0;
	public static int step_info_cnt = 0;
	public static int step_debug_cnt = 0;
	
	public static String outputlogfile = "";
	
	public static String hpalm_url = "";
	public static String hpalm_domain = "";
	public static String hpalm_project = "";
	public static String hpalm_username = "";
	public static String hpalm_password = "";
	public static String hpalm_testsetname = "";
	public static String hpalm_testsetfolder = "";
	
	/**
	 * This function creates an output log text file in the logs directory.
	 * The input required for this function is the ALM test ID.
	 * The file gets the test steps from ALM for the corresponding test case
	 * and puts those test case design steps into the log file.
	 * 
	 * @param alm_test_id
	 * @param test_description
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 11/01/2016 
	 */
	
	public void fw_create_output_log_file(String alm_test_id, String input_filename) throws IOException, InterruptedException {
		
		step_total_cnt = 0;
		step_passed_cnt = 0;
		step_failed_cnt = 0;
		
		String workspace_name = comm.fw_get_workspace_name();
		comm.fw_set_variable("WORKSPACENAME", workspace_name);
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("yyyy-MM-dd");
		String date_value = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("kk:mm:ss a");
		String time_value = dt.generated_date_time_output;
		
		FileWriter fw;
		
		String outputfilename = "SELENIUM OUTPUT LOG FILE - TESTID" + alm_test_id + " - " + date_and_time + ".txt";
		
		outputlogfile = workspace_name + "\\logs\\" + outputfilename;
		
		fw = new FileWriter(outputlogfile,true);
		fw.close();
		
		log.fw_writeLogEntry("SELENIUM AUTOMATED OUTPUT FILE","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("Start Date: " + date_value,"NA");
		log.fw_writeLogEntry("Start Time: " + time_value,"NA");
		log.fw_writeLogEntry("","NA");
		
		//********************** Global *************************
		
		log.fw_set_global_parms(input_filename);
		
		//********************** ALM *************************
		
		log.fw_get_alm_config_parms(input_filename);
		comm.fw_set_variable("OUTPUTLOGFILE", outputfilename);
		
		Object runidvalue = fwalm.fw_update_hpalm_test_case_execution_status(null, alm_test_id, "Not Completed");
		String string_run_id_value = String.valueOf(runidvalue);
		comm.fw_set_variable("RUNIDVALUE", string_run_id_value);
		comm.fw_set_variable("ALMTESTID", alm_test_id);
		
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");			
		log.fw_writeLogEntry("","NA");
		
		ITDConnection itdc = ClassFactory.createTDConnection();
		itdc.initConnectionEx(hpalm_url);
		itdc.connectProjectEx(hpalm_domain, hpalm_project, hpalm_username, hpalm_password);		
		ICommand cmd = (itdc.command()).queryInterface(ICommand.class);  	
		
		// Get Test Case Details and Write to Log File
		
		log.fw_writeLogEntry("ALM DESIGN STEPS","NA");
		log.fw_writeLogEntry("","NA");

		cmd.commandText("select ds_step_name, ds_description, ds_expected from dessteps where ds_test_id = " + alm_test_id + " order by ds_step_order");
		IRecordset recordes =(cmd.execute()).queryInterface(IRecordset.class);
		
		//int loop_count = 0;
		
		String cur_ds_step_name = "";
		String cur_ds_description = "";
		String cur_ds_expected = "";
		
		recordes.first();
		for (int r=0 ;r<recordes.recordCount();r++)
		{
			//loop_count++;
			
			cur_ds_step_name = recordes.fieldValue(0).toString();
			cur_ds_description = recordes.fieldValue(1).toString();
			cur_ds_expected = recordes.fieldValue(2).toString();
			
			cur_ds_description = cur_ds_description.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
			cur_ds_expected = cur_ds_expected.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
			
			cur_ds_description = cur_ds_description.replaceAll("&quot;", "");
			cur_ds_expected = cur_ds_expected.replaceAll("&quot;", "");
			
			cur_ds_description = cur_ds_description.replaceAll("&nbsp;", "");
			cur_ds_expected = cur_ds_expected.replaceAll("&nbsp;", "");
			
			cur_ds_description = cur_ds_description.replaceAll("&amp;", "and");
			cur_ds_expected = cur_ds_expected.replaceAll("&amp;", "and");
			
			log.fw_writeLogEntry("Step Name:        " + cur_ds_step_name,"NA");
			log.fw_writeLogEntry("Description:     " + cur_ds_description,"NA");
			log.fw_writeLogEntry("Expected Result: " + cur_ds_expected,"NA");
			log.fw_writeLogEntry("","NA");
			
			recordes.next();

		}
		
		itdc.disconnectProject();
		
		
		
		// End of Get Test Case Details and Write to Log File
		
		log.fw_writeLogEntry("","NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("","NA");				
		log.fw_writeLogEntry("AUTOMATION EXECUTION STEPS","NA");
		log.fw_writeLogEntry("","NA");
		
		log.fw_writeLogEntry("Input filename: " + input_filename,"NA");
		log.fw_writeLogEntry(System.lineSeparator(), "NA");
		
		comm.fw_set_variable("EXITONFAIL", "");
		
	}		
	
	/**
	 * This function gets the ALM connection information within the 
	 * corresponding ALM spreadsheet in the project corresponding to
	 * the workspace which this test is being run from (i.e. Gateway).
	 * The ALM spreadsheet resides in the Gateway\config directory.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since 11/1/2016
	 * 
	 */
	
	public void fw_get_alm_config_parms(String in_filename) throws MalformedURLException, IOException, InterruptedException {
		
		String attribute_value;
		String value_value;
		
		String workspace_name = comm.fw_get_workspace_name();

		String alm_path = workspace_name + "\\maps\\" + in_filename;		
		MSExcel xls = new MSExcel(alm_path);
			
		int Rows = xls.getRowCount("ALM");
		
		for (int x=2;x<Rows+1;x++)
		{
			attribute_value = xls.getCellData("ALM", "Attribute", x);
			value_value = xls.getCellData("ALM", "Value", x);
			
			if (attribute_value.equals("HPALMURL"))
			{
				hpalm_url = value_value;
				comm.fw_set_variable("HPALMURL", hpalm_url);
			}
			if (attribute_value.equals("HPALMDomain"))
			{
				hpalm_domain = value_value;
				comm.fw_set_variable("HPALMDOMAIN", hpalm_domain);
			}
			if (attribute_value.equals("HPALMProject"))
			{
				hpalm_project = value_value;
				comm.fw_set_variable("HPALMPROJECT", hpalm_project);
			}
			if (attribute_value.equals("HPALMUsername"))
			{
				hpalm_username = value_value;
				comm.fw_set_variable("HPALMUSERNAME", hpalm_username);
			}
			if (attribute_value.equals("HPALMPassword"))
			{
				hpalm_password = value_value;
				comm.fw_set_variable("HPALMPASSWORD", hpalm_password);
			}
			if (attribute_value.equals("HPALMTestSet"))
			{
				hpalm_testsetname = value_value;
				comm.fw_set_variable("HPALMTESTSET", hpalm_testsetname);
			}
			if (attribute_value.equals("HPALMTestSetFolder"))
			{
				hpalm_testsetfolder = value_value;
				comm.fw_set_variable("HPALMTESTSETFOLDER", hpalm_testsetfolder);
			}
		}
						
	}

	/**
	 * This function sets the Global parms from the Global worksheet in the Map spreadsheet.
	 * 
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 2/21/2017
	 * 
	 */
	
	public void fw_set_global_parms(String in_filename) throws IOException, InterruptedException {
		
		String attribute_value;
		String value_value;
		
		String workspace_name = comm.fw_get_workspace_name();

		String file_path = workspace_name + "\\maps\\" + in_filename;		
		MSExcel xls = new MSExcel(file_path);
			
		int Rows = xls.getRowCount("Global");
		
		for (int x=2;x<Rows+1;x++)
		{
			attribute_value = xls.getCellData("Global", "Attribute", x);
			value_value = xls.getCellData("Global", "Value", x);
			
			if (attribute_value.equals("Debug"))
			{
				comm.fw_set_variable("Debug", value_value);
			}
		}
						
	}
	
	/**
	 * This function writes a log entry into an output file and displays output to 
	 * output console.
	 * 
	 * @param log_message
	 * @param rc_value (valid values include the following)
	 * 			0 - PASSED
	 * 			1 - FAILED
	 * 			NA - don't apply step status for the log entry, just write out the log message
	 * 			
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @since 9/3/2016
	 * 
	 */
	
	public void fw_writeLogEntry(String log_message, String rc_value) throws InterruptedException, IOException 
	{
		
		try 
		{
			
			String step_status = "";
			String file_log_entry;
			String systemout_log_entry;
			
			dt.fw_generate_datetime("kk:mm:ss a");
			
			FileWriter fw;
			String logging_file = outputlogfile;
			fw = new FileWriter(logging_file,true);
			
			if (rc_value == "LOGHEADER")
			{	
				int length_log_message = log_message.length();
				
				int length_border = length_log_message + 6;
				String border_string = "";
				String border_string2 = "";
				for (int s=1;s<length_border+1;s++)
				{
					border_string = border_string + "*";
					border_string2 = border_string2 + "-";
				}
				fw.write(System.lineSeparator());
				fw.write(border_string + System.lineSeparator());
				fw.write(border_string2 + System.lineSeparator());
				fw.write("   " + log_message + System.lineSeparator());
				fw.write(border_string2 + System.lineSeparator());
				fw.write(border_string + System.lineSeparator());
				fw.write(System.lineSeparator());
				
				System.out.println("");
				System.out.println(border_string);
				System.out.println(border_string2);
				System.out.println("   " + log_message);
				System.out.println(border_string2);
				System.out.println(border_string);
				System.out.println("");
			}
			else if (rc_value == "NA")
			{
				
				file_log_entry = log_message + System.lineSeparator();
				systemout_log_entry = log_message;
				
				fw.write(file_log_entry);
				System.out.println(systemout_log_entry);
				
			}
			else
			{
								
				step_total_cnt = step_total_cnt + 1;
				
				if (rc_value == "0")
				{
					step_passed_cnt = step_passed_cnt + 1;
					step_status = "PASSED";
				}
				if (rc_value == "1")
				{
					step_failed_cnt = step_failed_cnt + 1;
					step_status = "FAILED";
				}
				
				file_log_entry = "Step " + step_total_cnt + " - " + step_status + " - " + log_message + " - " + dt.generated_date_time_output + System.lineSeparator();
				systemout_log_entry = "Step " + step_total_cnt + " - " + step_status + " - " + log_message + " - " + dt.generated_date_time_output;
			
				fw.write(file_log_entry);
				System.out.println(systemout_log_entry);
				
			}
			
			fw.close();
		    		      
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}

	/**
	 * This function determines the overall test case execution status
	 * and writes out that result in the output log file.
	 * 
	 * @throws IOException
	 * @author Mark Elking
	 * @throws InterruptedException 
	 * @since 11/1/2016
	 * 
	 */
	
	public void fw_closedown_test() throws IOException, InterruptedException 
	{
		
		// Quit Driver
		
		String workspace_name = comm.fw_get_workspace_name();
		String variables_path = workspace_name + "\\variables\\";
		String debug_value = comm.fw_get_value_from_file(variables_path + "Debug");
		
		if (step_failed_cnt == 0)
		{	
			comm.fw_quit_driver();
		}
		else
		{
			if (debug_value.toUpperCase().equals("OFF"))
			{
				comm.fw_quit_driver();
			}
			else
			{
				System.out.println(" ");
				System.out.println(" ***** Stopping Execution because DEBUG is set to ON and there was a FAILURE to debug****** ");
				System.out.println(" ");
				System.exit(0);
			}
		}
		
		// Close Log File
		
		String overall_test_case_status = "";
		String hpalm_status = "";
		
		dt.fw_generate_datetime("yyyyMMddkkmmss");
		String date_and_time = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("yyyy-MM-dd");
		String date_value = dt.generated_date_time_output;
		
		dt.fw_generate_datetime("kk:mm:ss a");
		String time_value = dt.generated_date_time_output;
		
		if (step_failed_cnt == 0)
		{
			overall_test_case_status = "PASSED";	
			hpalm_status = "Passed";
		}
		else
		{
			overall_test_case_status = "FAILED";
			hpalm_status = "Failed";
		}
		
		log.fw_writeLogEntry(System.lineSeparator(),"NA");
		log.fw_writeLogEntry("    Total Steps : " + step_total_cnt,"NA");
		log.fw_writeLogEntry("    Total Passed: " + step_passed_cnt,"NA");
		log.fw_writeLogEntry("    Total Failed: " + step_failed_cnt,"NA");
		log.fw_writeLogEntry("OVERALL TEST CASE STATUS: " + overall_test_case_status,"NA");
		log.fw_writeLogEntry("--------------------------------------------","NA");
		log.fw_writeLogEntry("End Date: " + date_value,"NA");
		log.fw_writeLogEntry("End Time: " + time_value,"NA");
		
		//String workspace_name = comm.fw_get_workspace_name();
		//String variables_path = workspace_name + "\\variables\\";
		
		String runidvalue2 = comm.fw_get_value_from_file(variables_path + "RUNIDVALUE");
		Object runidval = runidvalue2;
		String almtestid = comm.fw_get_value_from_file(variables_path + "ALMTESTID");
		fwalm.fw_update_hpalm_test_case_execution_status(runidval, almtestid, hpalm_status);
		
		Thread.sleep(2000);
		
	}
	
}

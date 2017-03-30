package com.chtr.tmoauto.util;

import com.mercury.qualitycenter.otaclient.IBaseFactory;
import com.mercury.qualitycenter.otaclient.IList;
//import com.mercury.qualitycenter.otaclient.IProjectDescriptor;
import com.mercury.qualitycenter.otaclient.IRun;
import com.mercury.qualitycenter.otaclient.IRunFactory;
import com.mercury.qualitycenter.otaclient.ITSTest;
import com.mercury.qualitycenter.otaclient.ITestSet;
import com.mercury.qualitycenter.otaclient.ITestSetFolder;
import com.mercury.qualitycenter.otaclient.ITestSetTreeManager;
import com4j.Com4jObject;

import java.io.IOException;

import com.chtr.tmoauto.webui.GUI;
import com.mercury.qualitycenter.otaclient.ClassFactory;
//import com.mercury.qualitycenter.otaclient.ICommand;
//import com.mercury.qualitycenter.otaclient.IRecordset;
import com.mercury.qualitycenter.otaclient.ITDConnection;
import com.mercury.qualitycenter.otaclient.IAttachmentFactory;
import com.mercury.qualitycenter.otaclient.IAttachment;
import com.mercury.qualitycenter.otaclient.IExtendedStorage;

//import com.chtr.tmoauto.webui.GUI;

public class ALM {

	public static GUI comm = new GUI();
	
	public static Object fw_update_hpalm_test_case_execution_status(Object run_id_value, String strTestCaseId, String strStatus) throws IOException, InterruptedException {

 /*       ITDConnection connection=null;

        String workspace_name = comm.fw_get_workspace_name();
        String variables_path = workspace_name + "\\variables\\";
        
        String folderName= workspace_name + "\\logs\\";
        String fileName = comm.fw_get_value_from_file(variables_path + "OUTPUTLOGFILE");        
        String url = comm.fw_get_value_from_file(variables_path + "HPALMURL");
        String domain = comm.fw_get_value_from_file(variables_path + "HPALMDOMAIN");
        String project = comm.fw_get_value_from_file(variables_path + "HPALMPROJECT");
        String username = comm.fw_get_value_from_file(variables_path + "HPALMUSERNAME");
        String password = comm.fw_get_value_from_file(variables_path + "HPALMPASSWORD");
        String testsetfolder = comm.fw_get_value_from_file(variables_path + "HPALMTESTSETFOLDER");
        String testset = comm.fw_get_value_from_file(variables_path + "HPALMTESTSET");
		
        try{
          
           	//QC Connection
       	connection = ClassFactory.createTDConnection();
           	connection.initConnectionEx(url);
           
           	connection.initConnectionEx(url);
           	connection.connectProjectEx(domain, project, username, password);
           
           //To get the Test Set folder in Test Lab        
           ITestSetTreeManager objTestSetTreeManager = (connection.testSetTreeManager()).queryInterface(ITestSetTreeManager.class);
           ITestSetFolder objTestSetFolder =(objTestSetTreeManager.nodeByPath(testsetfolder)).queryInterface(ITestSetFolder.class);
                   
           IList tsTestList = objTestSetFolder.findTestSets(null, true, null);
                   
           for (int i=1;i<=tsTestList.count();i++) {
               Com4jObject comObj = (Com4jObject) tsTestList.item(i);
               ITestSet tst = comObj.queryInterface(ITestSet.class); 
                       
               if(tst.name().equalsIgnoreCase(testset)){
                           
                   IBaseFactory testFactory = tst.tsTestFactory().queryInterface(IBaseFactory.class);
             
                   IList testInstances = testFactory.newList("");
                               
                   //To get Test Case ID instances
                   for (Com4jObject testInstanceObj : testInstances){  
                       ITSTest testInstance = testInstanceObj.queryInterface(ITSTest.class);  
                       
                       //System.out.println("Test Name Value: " + testInstance.testName());
                       
                       //if(testInstance.testName().equalsIgnoreCase(strTestCaseId)){
                       if(testInstance.testId().equals(strTestCaseId)){
                           IRunFactory runfactory = testInstance.runFactory().queryInterface(IRunFactory.class);
                           
                           if (strStatus.equals("Not Completed"))
                           {
                        	   IRun run= runfactory.addItem("Selenium").queryInterface(IRun.class);
	                           run.status("Not Completed");
	                           run.post();
	                           run_id_value = run.id();
                           }
                           else
                           {
	                           IRun run = runfactory.item(run_id_value).queryInterface(IRun.class);
	                           run.status(strStatus);
	                           run.post();
		
		                        try
		                        {
		                        	IAttachmentFactory attachfac = run.attachments().queryInterface(IAttachmentFactory.class);
		                        	IAttachment attach = attachfac.addItem(fileName).queryInterface(IAttachment.class);
		                        	IExtendedStorage extAttach = attach.attachmentStorage().queryInterface(IExtendedStorage.class);
		                        	extAttach.clientPath(folderName);
		
		                        	extAttach.save(fileName, true);
		                        	attach.description("test");
			
		                        	attach.post();
		                        	attach.refresh();
		                        }
		                        catch(Exception e) 
		                        {
		                        	System.out.println("QC Exception : "+e.getMessage());
		                        }

                           }
                           
                           break;
                       }
                   } 
               }
           }
       }
       catch(Exception e)
       {
           System.out.println(e.getMessage());
       }
       finally
       {
    	   connection.disconnectProject();
           connection.releaseConnection();
       }
         
       return run_id_value;
     */  
		return run_id_value;
   } 
	 
}

package com.chtr.tmoauto.T3;

import java.util.ArrayList;
import java.util.List;
import com.chtr.tmoauto.logging.Logging;
import com.chtr.tmoauto.webui.GUI;
import com.chtr.tmoauto.util.MSExcel;

public class T3Tests {

	static T3Functions gf = new T3Functions();
	static Logging log = new Logging();
	static GUI fwgui = new GUI();
	public static String alm_test_id = "";
	static String orderid;
	static String serviceAccountNumber;

	/**
	 * This function is main entry into the execution of all T3 test cases.
	 * 
	 * @param args
	 * @author Richa Khandelwal
	 * @throws Exception
	 * @since 1/11/2017
	 */

	public static void main(String[] args) throws Exception {

		

		// Define Input Filename
		//test

		String configuration_map_filename = "T3ConfigurationMap.xlsm";

		String workspace_name = System.getProperty("user.dir");
		if (!workspace_name.contains("\\")) {
			workspace_name.replace("\\", "\\\\");
		}
		String input_filename = workspace_name + "\\maps\\" + configuration_map_filename;
		String configuration_map_fullpath = input_filename;
		String test_cases_to_execute_list = fwgui.fw_get_list_of_test_cases_to_execute(configuration_map_filename);
		String[] test_case_to_execute_list_arr = test_cases_to_execute_list.split(",");
		for (int x = 0; x < test_case_to_execute_list_arr.length; x++) {

			String worksheet_name = test_case_to_execute_list_arr[x];
			alm_test_id = worksheet_name.substring(2);
			log.fw_create_output_log_file(alm_test_id, configuration_map_filename);

			String tc_eventname = "";
			String tc_objectname = "";
			String tc_testdata = "";
			String millisecondstowaitafterobjectevent = "";
			String objecttolookforafterobjectevent = "";
			String objecttolookforafterobjectevent_comp = "";
			String millisecondstowaitafterobjectevent_comp = "";

			String tc_eventname_comp = "";
			String tc_objectname_comp = "";
			String tc_testdata_comp = "";

			MSExcel xls = new MSExcel(input_filename);

			int Row = xls.getRowCount(worksheet_name);
			int RowComponent = xls.getRowCount("Component");
			
			// Added by Rakesh/Gaurav on 2/24/2017
			String[] tc_objectname_comp_arr = new String[2];
			String tc_comp_name;
			// End of Added by Rakesh/Gaurav on 2/24/2017
			
			for (int y = 2; y < Row + 1; y++) {

				tc_eventname = xls.getCellData(worksheet_name, "TCEventName", y);
				tc_objectname = xls.getCellData(worksheet_name, "TCObjectName", y);
				tc_testdata = xls.getCellData(worksheet_name, "TCTestData", y);
				objecttolookforafterobjectevent = xls.getCellData(worksheet_name, "ObjectToLookForAfterObjectEvent", y);
				millisecondstowaitafterobjectevent = xls.getCellData(worksheet_name, "MillisendsToWaitAfterObjectEvent",
						y);

				if (tc_eventname.equals("Component")) {

					for (int w = 2; w < RowComponent + 1; w++) {
						tc_eventname_comp = xls.getCellData("Component", "TCEventName", w);
						tc_objectname_comp = xls.getCellData("Component", "TCObjectName", w);
						tc_testdata_comp = xls.getCellData("Component", "TCTestData", w);
						objecttolookforafterobjectevent_comp = xls.getCellData("Component",
								"ObjectToLookForAfterObjectEvent", w);
						millisecondstowaitafterobjectevent_comp = xls.getCellData("Component",
								"MillisendsToWaitAfterObjectEvent", w);
						
						// Updated by Rakesh/Gaurav on 2/24/2017
						tc_comp_name = "";
						if (tc_objectname_comp.contains("--")){							//check if there is -- in the step in component sheet
							tc_objectname_comp_arr = tc_objectname_comp.split("--");
							tc_comp_name = tc_objectname_comp_arr[0];
							tc_objectname_comp = tc_objectname_comp_arr[1];							
						}
						
						if (tc_objectname_comp.contains(tc_testdata + "_") || (tc_comp_name.contains(tc_testdata))) {						
						//if (tc_objectname_comp.contains(tc_testdata + "_")) {
						// End of Updated by Rakesh/Gaurav on 2/24/2017
							
							if (tc_objectname_comp.contains("CREATENEWPRODUCT_ProductName")){								
								if (tc_testdata_comp.contains("FILE_"))
								{
									String variables_path = workspace_name + "\\variables\\";
									
									String[] tc_test_data_array = tc_testdata_comp.split("_");
									String tc_test_data_filename = tc_test_data_array[1];
									gf.productName = fwgui.fw_get_value_from_file(variables_path + tc_test_data_filename);
								}															
							}							
							
							if (tc_objectname_comp.equals("OSMSEARCHORDERID_OSMQUERY_OrderNumberCharter")) {
								//orderid = "1-214118030";
								gf.crm_orderid = orderid;
								fwgui.fw_event(configuration_map_fullpath, worksheet_name, tc_eventname_comp, tc_objectname_comp, orderid,
										objecttolookforafterobjectevent_comp, millisecondstowaitafterobjectevent_comp);
							}else if (tc_objectname_comp.contains("T3_CRM_GotoAccount")){
								String AccountToClick = "";
								if (tc_testdata_comp.contains("FILE_"))
								{
									String variables_path = workspace_name + "\\variables\\";
									
									String[] tc_test_data_array = tc_testdata_comp.split("_");
									String tc_test_data_filename = tc_test_data_array[1];
									//gf.billingAccount = fwgui.fw_get_value_from_file(variables_path + tc_test_data_filename);
									AccountToClick = fwgui.fw_get_value_from_file(variables_path + tc_test_data_filename);
								}
								
								Thread.sleep(5000);
								log.fw_writeLogEntry("Go to Account " + AccountToClick, "NA");
								fwgui.fw_click_button("Billing Account", "NA", "css", "td[title=" + AccountToClick + "]", "NA", 5000);
							}else if (tc_objectname_comp.contains("T3_CRM_Select_Product_Line")) {
								gf.T3_CRM_Select_Product_Line(tc_testdata_comp);
							}else if (tc_objectname_comp.equals("COMPLETEACTVITIES_T3_CRM_Complete_All_Activities_And_Validate")) {
								gf.T3_CRM_Complete_All_Activities_And_Validate();
							}else if (tc_objectname_comp.equals("VALIDATIONREPORT_T3_CRM_ValidationReport")) {
								gf.T3_CRM_ValidationReport();
							}else{
								
								fwgui.fw_event(input_filename, "Component", tc_eventname_comp, tc_objectname_comp,
										tc_testdata_comp, objecttolookforafterobjectevent_comp,
										millisecondstowaitafterobjectevent_comp);
								
								if (tc_objectname_comp.contains("ORDERSUBMIT_OrderNumber")){								
									orderid = fwgui.return_get_text;							
								}
								
								if (tc_objectname_comp.equals("ORDERSUBMIT_ServiceAccountNumber")) {								
									serviceAccountNumber = fwgui.fw_get_attribute_value("name", "s_1_1_17_0", "value", 2000);
								}								
							}							
						}
					}
				} 
/*				else if (tc_objectname.equals("OpportunityName")) {
					String opportunityName = tc_testdata;
					opportunityName = gf.T3_CRM_AccountName(opportunityName);
					fwgui.fw_event(configuration_map_fullpath, worksheet_name, tc_eventname, tc_objectname,
							opportunityName, objecttolookforafterobjectevent, millisecondstowaitafterobjectevent);
				} */
				else if (tc_objectname.equals("UNIEndpointServiceAccnt1")) {
					String service_account = gf.serviceAccount1;
					fwgui.fw_event(configuration_map_fullpath, worksheet_name, tc_eventname, tc_objectname,
							service_account, objecttolookforafterobjectevent, millisecondstowaitafterobjectevent);

				} else if (tc_objectname.equals("UNIEndpointServiceAccnt2")) {
					String service_account = gf.serviceAccount2;
					fwgui.fw_event(configuration_map_fullpath, worksheet_name, tc_eventname, tc_objectname,
							service_account, objecttolookforafterobjectevent, millisecondstowaitafterobjectevent);
				}else if (tc_objectname.equals("CompleteOSMTask")) {
					gf.T3_OSM_CompleteTasks(configuration_map_fullpath, worksheet_name,500);
				}
				else if (tc_objectname.equals("T3_Verify_Plan_TaskName_BeforePO")) {
					String taskName = tc_testdata;
					List<String> parameter = new ArrayList<String>();
					for (String task : taskName.split(",")) {
						parameter.add(task);
					}
					gf.T3_Verify_Plan_TaskName_BeforePO(configuration_map_fullpath, parameter, worksheet_name);
				}else if (tc_objectname.equals("T3_Verify_Plan_TaskName_AfterPO")) {
					String taskName = tc_testdata;
					List<String> parameter = new ArrayList<String>();
					for (String task : taskName.split(",")) {
						parameter.add(task);
					}
					gf.T3_Verify_Plan_TaskName_AfterPO(configuration_map_fullpath, parameter, worksheet_name);
				} else if (tc_objectname.equals("T3_CRM_Verify_Milestone")) {

					String milestoneNames = tc_testdata;
					List<String> parameter = new ArrayList<String>();
					for (String milestone : milestoneNames.split(",")) {
						parameter.add(milestone);
					}
					gf.T3_CRM_Verify_Milestone(configuration_map_fullpath, worksheet_name, parameter);
				} else if (tc_objectname.equals("T3_CRM_ValidateOrderComplete")) {
					gf.T3_CRM_ValidateOrderComplete();
				} else if (tc_objectname.equals("T3_CRM_ValidateAsset")) {
					gf.T3_CRM_ValidateAsset();
				} else if (tc_objectname.equals("T3_CRM_Save_BillingCycle_PurchaseDate_OverridePrice_AfterComplete")) {
					gf.T3_CRM_Save_BillingCycle_PurchaseDate_OverridePrice_AfterComplete();
				} else if (tc_objectname.equals("T3_CRM_ValidationReport")) {
					gf.T3_CRM_ValidationReport();
				} else if (tc_objectname.equals("OrderNumber")) {
					fwgui.fw_event(configuration_map_fullpath, worksheet_name, tc_eventname, tc_objectname, tc_testdata,
							objecttolookforafterobjectevent, millisecondstowaitafterobjectevent);
					orderid = fwgui.return_get_text;
					System.out.println("Order no is "+orderid);
				} else if (tc_objectname.equals("ServiceAccountNumber")) {
					fwgui.fw_event(configuration_map_fullpath, worksheet_name, tc_eventname, tc_objectname, tc_testdata,
							objecttolookforafterobjectevent, millisecondstowaitafterobjectevent);
					serviceAccountNumber = fwgui.fw_get_attribute_value("name", "s_1_1_17_0", "value", 2000);
					System.out.println("Assigned service no " + serviceAccountNumber);
				} else if (tc_objectname.equals("T3_CRM_Complete_All_Activities_And_Validate")) {
					gf.T3_CRM_Complete_All_Activities_And_Validate();
				} else if (tc_objectname.equals("T3_CRM_Save_BillingCycle_PurchaseDate_OverridePrice")) {
					gf.T3_CRM_Save_BillingCycle_PurchaseDate_OverridePrice();
				} else if (tc_objectname.equals("T3_BRM_Validate_BillingCycle")) {
					
					gf.T3_BRM_Validate_BillingCycle(serviceAccountNumber);
				} 
				else if (tc_objectname.equals("T3_BRM_Validate_PurchaseCycleandOverridePrice")) {
					fwgui.fw_event(configuration_map_fullpath, worksheet_name, "NA",
							"T3_BRM_Validate_PurchaseCycleandOverridePrice", tc_testdata,
							objecttolookforafterobjectevent, millisecondstowaitafterobjectevent);
					String productName = fwgui.out_object_testdata;
					List<String> parameter = new ArrayList<String>();
					for (String product : productName.split(",")) {
						parameter.add(product);
					}
					gf.T3_BRM_Validate_PurchaseCycleandOverridePrice(configuration_map_fullpath, tc_eventname,
							parameter);
				}
				else {
					fwgui.fw_event(configuration_map_fullpath, worksheet_name, tc_eventname, tc_objectname, tc_testdata,
							objecttolookforafterobjectevent, millisecondstowaitafterobjectevent);

				}
			}
		}

	}

}

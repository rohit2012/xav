package com.xavient.pages;

import org.openqa.selenium.By;

public interface DashBoardView {
	/**
	 * Locators for All Pages
	 * 
	 */
	By pword = By.id("pword");
	By user_name = 	By.id("username");
	By submit_login = By.name("go");

	By alertBtn = By.xpath("//button[contains(@ng-click,'getNotification')]");
	By loginUserName = By.className("");
	By signOut = By.xpath("//i[@tooltip='LogOut']");

	By headerTitle = By.xpath("//div[@class=\"panel-heading\"]/h4/span");
	By IECertificate = By.id("overridelink");
	/*
	 Home Page 
	 */

	By View = By.cssSelector(".fa.fa-file-text-o");
	By View3 = By.xpath(".//*[contains(text(),'LOB Queue & Agent Overview With COE Agent Drilldown View')]");
	By Queue_And_Agent_Overview = By.xpath(".//*[contains(text(),'Queue And Agent Overview')]");
	By View15 = By.xpath("//*[@class='nav nav-pills nav-stacked']//*[contains(text(),'COE With Agent View')]");

	By Queue_Summary_EMC = By.xpath(".//*[contains(text(),'Queue Summary - EMC')]");
	By View2 = By.xpath(".//*[contains(text(),' LOB Summary View')]"); 

	By CumulativePerformance = By.xpath("//a[text()='Cumulative Performance']");
	By View5 = By.xpath("//a[contains(text(),'Forecast Staffing By COE And Partner View')]");
	
	/*
	 view 3 - Table Names
	 */
	By view3_curr_data = By.xpath(".//*[contains(text(),'Current Data')]");
	By view3_today_data = By.xpath(".//*[contains(text(),\"Today's Data\")]");
	By view3_curr_agent_stats_tbl = By.xpath(".//*[contains(text(),'Current Agents Statistics for all COEs')]");
	By view3_agent_details = By.xpath(".//*[@id='tableCtrl']/div/div[3]/div/div[1]/div/h4");

	String view3_curr_data_table  = ".//*[@id='VIEW_3_table-first1']/thead/tr/th"; 
	String view3_Agent_table_data_start = "//div[@class='ui-grid-header-cell-row']/descendant::Div[@class='ui-grid-cell-contents']" ;
	String view3_Agent_table_data_end = "/span[1]";
	String view3_today_data_table = ".//*[@id='VIEW_3_table-second1']/thead/tr/th"; 
	String view3_curr_agent_stats_col = ".//*[@id='VIEW_3_table-third1']/thead/tr/th";	
	String view3_Agent_table_data_sort_arrow = "/span[2]";

	/*
	 view 3 - Line Chart.
	 */
	By view3_line_graph_title = By.xpath(".//*[contains(text(),'Current Agent Statistics For all COEs')]");
	By view3_line_graph_header = By.xpath(".//*[contains(text(),'Agent AUX State Detail')]");
	By view3_line_graph_y_axis = By.xpath(".//*[contains(text(),'Agent Count')]");
	//By view3_line_graph_x_axis = By.xpath(".//*[contains(text(),'Agents Break ')]/parent::*/parent::*/child::*/child::*");
	By view3_graph_x_axis = By.xpath("//*[contains(text(),'Agents Break ')]/parent::*/parent::*/child::*");


	/*
	 view 3 - Bar Chart.
	 */
	By view3_bar_graph_title = By.xpath(".//*[contains(text(),'Current Agent Statistics For all COEs')]");
	By view3_bar_graph_header = By.xpath(".//*[contains(text(),'Agent AUX State Detail')]");
	By view3_bar_graph_y_axis = By.xpath(".//*[contains(text(),'Agent Count')]");
	By view3_bar_graph_x_axis = By.xpath(".//*[contains(text(),'Agents Break ')]/parent::*/parent::*/child::*/child::*");
	/*
	 * View 3 - Pie Chart
	 */
	By view3_piechart_graph_header = By.xpath("//h4[text()='Agent Status']");
	By view3_piechart_labels = By.xpath("//*[text()='Agents Available']/parent::*/parent::*/parent::*/child::*/child::*[3]");
	By view3_piechart_Total_Agents_label =  By.xpath("//label[text()='Total Agents']");
	By view3_piechart_Agents_Staffed_label =  By.xpath("//label[text()='Agents Staffed']");
	/*
	 * 	Tip locator
	 */
	By popOutToolTip = By.id("fullScrExpand");
	By pauseToolTip = By.id("clickPause");
	By tabularViewToolTip = By.xpath(".//*[@id='fullscrnhide']/i[@tooltip=\"Tabular View\"]");
	By lineChartToolTip = By.xpath(".//*[@id='ChartCombineLine']");
	By barGraphToolTip = By.xpath(".//*[@id='ChartCombineBar']");
	By saveMyViewToolTip = By.id("saveMyView");
	By chartCombineStackToolTip = By.id("ChartCombineStack");
	
	/*
	 * Pagination
	 */
	By pagerFirst = By.className("ui-grid-pager-first");
	By pagerPrevious = By.className("ui-grid-pager-previous");
	By pagerCurrentPage = By.xpath("//input[contains(@class,'ui-grid-pager-control-input')]");
	By pagerMaxPages = By.xpath("//span[contains(@class,'ui-grid-pager-max-pages-number')]");
	By pagerNext = By.className("ui-grid-pager-next");
	By pagerLast = By.className("ui-grid-pager-last");
	By pagerPageDrop = By.xpath("//div[contains(@class,'ui-grid-pager-row-count-picker')]/select1");
	By pagerPageDropText = By.xpath("//div[contains(@class,'ui-grid-pager-row-count-picker')]/span");
	By pagerGridCount = By.xpath("//div[@class=\"ui-grid-pager-count\"]/span");

	By searchTextBox = By.xpath("//input[contains(@placeholder,'Search')]");
	By noRecordData = By.xpath("//div[@class='alert alert-danger noDataGrid']");

	/*
	 * Filter
	 */
	By filterBtn = By.xpath("//i[@tooltip=\"Filter\"1]");
	By filterTxt = By.xpath("//div[@class='filter-panel']/descendant::div[@class='filter-body']//following-sibling::label");

	By organizationFilterTxt = By.xpath("//div[@class='filter-panel']/child::*/div[@class='row']/div/label[text()='Organization']");
	By coeFilterTxt = By.xpath("//div[@class='filter-panel']/child::*/div[@class='row']/div/label[text()='COE']");
	By lobFilterTxt = By.xpath("//div[@class='filter-panel']/child::*/div[@class='row']/div/label[text()='LOB']");
	By subLobFilterTxt = By.xpath("//div[@class='filter-panel']/child::*/div[@class='row']/div/label[text()='Sub LOB']");
	By functionalGroupsFilterTxt = By.xpath("//div[@class='filter-panel']/child::*/div[@class='row']/div/label[text()='Functional Groups']");
	By subFunctionalGroupsFilterTxt = By.xpath("//div[@class='filter-panel']/child::*/div[@class='row']/div/label[text()='Sub Functional Groups']");
	By languageFilterTxt = By.xpath("//div[@class='filter-panel']/child::*/div[@class='row']/div/label[text()='Language']");
	By timeZoneFilterTxt = By.xpath("//div[@class='filter-panel']/child::*/div[@class='row']/div/label[text()='Time Zone']");

	//Retrieve value using attribute-label
	By organizationFilterList = By.xpath(".//*[@id='orgHandller']");
	By coeFilterList = By.xpath("//select[@id='coeHandller']");
	By lobFilterList = By.xpath("//select[@id='lobHandller']");
	By subLobFilterList = By.xpath("//select[@id='subLobListHandller']");
	By functionalGroupsFilterList = By.xpath(".//*[@id='funGroupHandller']");
	By subFunctionalGroupsFilterList = By.xpath(".//*[@id='subFunGroupHandller']");
	By languageFilterList = By.xpath(".//*[@id='langHandller']");
	By timeZoneFilterList = By.xpath(".//*[@id='tzHandller']");
	By customerRegionList = By.xpath("//select[@id='regionHandller']"); 
	By functionHandllerFilterList= By.xpath("//select[@id='functionHandller']");
	By startTimeHandllerFilterList= By.xpath("//select[@id='startTimeHandller']");
	By endTimeHandllerFilterList= By.xpath("//select[@id='endTimeHandller']");
	By functionFilterList = By.xpath("//select[@id='functionHandller']"); 

	/*
	 * View 15
	 * 
	 */
	By view15_today_data = By.xpath("//*[@id='tableCtrl']//h4");
	String view15_today_data_table  = "//table[@id='VIEW_15_table-first1']//th"; 
	By view15_current_data = By.xpath("//h4[text()='Current Data']");
	String view15_current_data_table  = "//*[@id='VIEW_15_table-second1']//th"; 
	By view15_Half_Hour_data = By.xpath("//h4[text()='Half Hour Data']");
	String view15_Half_Hour_data_table  = "//*[@id='VIEW_15_table-third1']//th";
	By view15_Agents_Statistics_data = By.xpath("//h4[text()='Agents Statistics for all COEs']");
	String view15_Agents_Statistics_data_table  = "//*[@id='VIEW_15_table-four1']//th";
	By view15_Site_Detail_data = By.xpath("//h4[text()='Site Detail']");
	String view15_Site_Detail_data_table  = "//*[@id='VIEW_15_table-fifth1']//th";
	By view15_Agent_Detail_data = By.xpath("//h4[text()='Agent Detail']");
	String view15_Agent_Detail_data_table  = "//*[@class='ui-grid-cell-contents']/span[1]";

	/*
	 * view 15 - Line Chart.
	 */
	
	//view 1
	
		By Queue_Summary = By.xpath(".//*[contains(text(),'Queue Summary')]");	
		By View1 = By.xpath(".//*[contains(text(),'Residential EMC Summary View')]");
	
	
	/*
	 * View 3 table data
	 */
	By view3_agent_details_data = By.xpath("//div[@class='ui-grid-canvas']/div");
	
	/*
	 view2 - Line Chart.
	 */
	By view2_line_graph_header = By.xpath(".//a[contains(text(),'  LOB Summary View')]");
	By view2_AgentCount_y_axis_label = By.xpath(".//*[contains(text(),'Agent Count')]");
	By view2_AgentCount_x_axis = By.xpath(".//*[name()='svg']//*[name()='g']//*[contains(text(),'Current Calls Offered')]/parent::*/parent::*/child::*");
	By view2_AgentCount_y_axis = By.xpath(".//*[name()='svg']//*[name()='g']//*[contains(text(),'Agent Count')]/parent::*/parent::*/child::*");
	
	
	By view2_Percentage_y_axis_label = By.xpath(".//*[contains(text(),'Percentage')]");
	By view2_Percentage_x_axis = By.xpath(".//*[name()='svg']//*[name()='g']//*[contains(text(),'ABN')]/parent::*/parent::*/child::*");
	By view2_Percentage_y_axis = By.xpath(".//*[name()='svg']//*[name()='g']//*[contains(text(),'Percentage')]/parent::*/parent::*/child::*");
	
	By view2_Time_y_axis_label = By.xpath("//*[name()='svg']//*[name()='g']//*[contains(text(),'Time')]");
	By view2_Time_x_axis = By.xpath(".//*[name()='svg']//*[name()='g']//*[contains(text(),'AWBA')]/parent::*/parent::*/child::*");
	By view2_Time_y_axis = By.xpath(".//*[name()='svg']//*[name()='g']//*[contains(text(),'Time')]/parent::*/parent::*/child::*");
	
	By lst_chart_dd =By.xpath(".//*[@id='div_first']/div[1]/div/div[2]/ul/li");
	By icon_lst_chart_dd= By.xpath("//*[@id='div_first']/div[1]/div/div[2]/button");
	

	//View 1 Table name
	 By view1_residential_summary = By.xpath(".//*[contains(text(),'Residential EMC Summary View')]");
	 String view1_residential_summary_col = ".//*[@id='exRowTable_wrapper']//table[@class='fht-table table dataTable tree alert-notification']/thead/tr/th";

//View 1 Line Chart
	By view1_line_agentCount_x_axis=By.xpath("//*[name()='svg']//*[name()='g']//*[text()='Calls']/parent::*/parent::*/child::*");
	By view1_line_percentage_x_axis=By.xpath("//*[name()='svg']//*[name()='g']//*[text()='Current']/../parent::*/child::*");
	By view1_line_time_x_axis=By.xpath("//*[name()='svg']//*[name()='g']//*[text()='Waiting']/../parent::*/child::*");

//View 1 Bar Graph	
	By view1_bar_agentCount_x_axis=By.xpath("//*[name()='svg']//*[name()='g']//*[text()='Calls']/parent::*/parent::*/child::*");
	By view1_bar_percentage_x_axis=By.xpath("//*[name()='svg']//*[name()='g']//*[text()='Current']/../parent::*/child::*");
    By view1_bar_time_x_axis=By.xpath("//*[name()='svg']//*[name()='g']//*[text()='Waiting']/../parent::*/child::*");
    
 //View 1 Drill Down
    String view1DrillStart= "//*[@id='VIEW_1_table-first']/tbody/tr[";
    String view1DrillEnd= "]/td[1]/span";
    String view1_table_name="//*[@id='VIEW_1_table-first']/tbody/tr";
    

	/*

	 * View 3 Table Data in rows.
	 */
	
	
	By view3_Current_table_data_val = By.xpath(".//*[@id='VIEW_3_table-first1_0']/td");
	By view3_todays_table_data_val = By.xpath(".//*[@id='VIEW_3_table-second1_0']/td");
	By view3_COEs_table_data_val = By.xpath(".//*[@id='VIEW_3_table-third1_0']/td");

	/*
	 * View 5 Stack Chart
	 */
	By view5_AgentCount_x_axis_label = By.xpath(".//*[@id='div_first_VIEW_5_chartdiv']//*[name()='svg']//*[name()='g'][1]//*[name()='text'][contains(@text-anchor, 'middle')]");
	//By view5_AgentCount_x_axis_label = By.xpath(".//*[name()='svg']//*[name()='g']//*[contains(text(),'Current Calls Offered')]/parent::*/parent::*/child::*");

	By view5_AgentCount_y_axis_label = By.xpath(".//*[contains(text(),'Agent Count')]");
	By view5_AgentCount_sub_label = By.xpath(".//*[@id='div_first_VIEW_5_chartdiv_legend']/*[name()='svg']//*[name()='tspan']");
	
	By view5_Percentage_x_axis_label = By.xpath(".//*[@id='div_first_VIEW_5_chartdivNew']//*[name()='svg']//*[name()='g'][1]//*[name()='text'][contains(@text-anchor, 'middle')]");
	By view5_Percentage_y_axis_label = By.xpath(".//*[contains(text(),'Percentage')]");
	By view5_Percentage_sub_label = By.xpath(".//*[@id='div_first_VIEW_5_chartdivNew_legend']/*[name()='svg']//*[name()='tspan']");

	By view5_Time_x_axis_label = By.xpath(".//*[@id='div_first_VIEW_5_chartdivTime']//*[name()='svg']//*[name()='g'][1]//*[name()='text'][contains(@text-anchor, 'middle')]");
	By view5_Time_y_axis_label = By.xpath("//*[name()='svg']//*[name()='g']//*[contains(text(),'Time')]");
	By view5_Time_sub_label = By.xpath(".//*[@id='div_first_VIEW_5_chartdivTime_legend']/*[name()='svg']//*[name()='tspan']");
	By view5_ForecastStaffing_table = By.xpath(".//*[@id='exRowTable_wrapper']/descendant::thead[1]/tr/th");
	By loader = By.xpath("//span[@class='fa fa-refresh fa-spin fa-4x']");


	By lst_column_customization = By.cssSelector(".custPanelScroll div:nth-child(2) p input");
	By lnk_cc_more =By.xpath("//*[@id='addColumns']");
	
	By img_column_cust =By.cssSelector("i.dropdown-toggle.fa.fa-th");
	
	By tbl_View2 = By.xpath(".//*[@class='fht-table table dataTable tree alert-notification']//tr/th");

	// View 15 Column Customization
	By todaydata_columnCustomization = By.xpath("//div[@tableviewid='table-first1']/i");
	By view15_today_data_col  = By.xpath("//table[@id='VIEW_15_table-first1']//th");
	By currentdata_columnCustomization = By.xpath("//div[@tableviewid='table-second1']/i");
	By view15_current_data_col  = By.xpath("//*[@id='VIEW_15_table-second1']//th");
	By halfhourtdata_columnCustomization = By.xpath("//div[@tableviewid='table-third1']/i");
	By view15_halfhour_data_col  = By.xpath("//*[@id='VIEW_15_table-third1']//th");
	By view15_halfhour_lnk_cc_more =By.xpath("//div[@tableviewid='table-third1']//a[@id='addColumns']");
	By Agent_data_columnCustomization = By.xpath("//div[@tableviewid='table-four1']/i");
	By view15_Agent_data_col  = By.xpath("//*[@id='VIEW_15_table-four1']//th");
	By view15_Agent_lnk_cc_more =By.xpath("//div[@tableviewid='table-four1']//a[@id='addColumns']");
}

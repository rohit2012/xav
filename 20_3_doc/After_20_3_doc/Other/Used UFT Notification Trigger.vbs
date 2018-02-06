'Write Sheet's full path here
strPath = "\\XEN-705\Automation\Utilities Under Modification\Charter Machine Assignment Utility\Charter Remote and Virtual Machines Detail.xlsm"
 
 'Write the macro name including module
strMacro = "GetUFTUsed_NotificationSheet"
 
 'Create an Excel instance and set visibility of the instance
Set objApp = CreateObject("Excel.Application") 
objApp.Visible = False
 
 'Open workbook; Run Macro; Save Workbook with changes; Close; Quit Excel
Set wbToRun = objApp.Workbooks.Open(strPath) 
objApp.Run strMacro 
wbToRun.Save 
'wbToRun.Close 
objApp.Quit 
Set objApp = Nothing
Set wbToRun = Nothing
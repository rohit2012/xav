'Write Sheet's full path here
strPath ="\\xtwinno1slem\Automation\Others\Utilities\Charter Links\Charter Links.xls"
 
 'Write the macro name including module
strMacro = "CommandButton1_Click"
 
 'Create an Excel instance and set visibility of the instance
Set objApp = CreateObject("Excel.Application") 
objApp.Visible = False
 
 'Open workbook; Run Macro; Save Workbook with changes; Close; Quit Excel
Set wbToRun = objApp.Workbooks.Open(strPath) 
objApp.Run strMacro 
wbToRun.Close 
objApp.Quit 
Set objApp = Nothing
Set wbToRun = Nothing
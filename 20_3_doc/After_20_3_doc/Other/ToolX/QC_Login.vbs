'Function Dim test()
on error resume next
Dim Arg, qcID, qcPWD, qcDomain, qcProject
Set Arg = WScript.Arguments 
   qcURL = "https://alm.corp.chartercom.com/qcbin"
   qcID = Arg(0)
   qcPWD = Arg(1)
   qcDomain = Arg(2)
   qcProject = Arg(3)
   
   'Display a message in Status bar
 'Application.StatusBar = "Connecting to Quality Center.. Wait..."
'Create a Connection object to connect to Quality Center
	Set tdConnection = CreateObject("TDApiOle80.TDConnection")
'Initialise the Quality center connection
   tdConnection.InitConnectionEx qcURL
'Authenticating with username and password
   tdConnection.Login qcID, qcPWD
'connecting to the domain and project
   tdConnection.Connect qcDomain, qcProject
'On successfull login display message in Status bar
  'Application.StatusBar = "........QC Connection is done Successfully"
  Set objShell = WScript.CreateObject("WScript.Shell")
  Set exWb = GetObject("C:\Users\rjain1\Desktop\Toolx\ToolX.xlsm")
  If tdConnection.LoggedIn = True Then      
      exWb.Worksheets("Sheet2").Cells(2, 1).Value=1
	  'WScript.quit (1)  
	else	
	  exWb.Worksheets("Sheet2").Cells(2, 1).Value=0
	 WScript.quit (1)      
  End If
set  Arg = Nothing
'Set tdConnection = Nothing
'Set objShell=Nothing
'Set exWb = Nothing
'End Function
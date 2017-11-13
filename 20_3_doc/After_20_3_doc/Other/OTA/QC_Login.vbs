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
  If tdConnection.LoggedIn = True Then
      Msgbox "Connected Successfully"
	else	
	
      'objEnv("ConnFlag") = "Fale"	
	  msgbox "Invalid Credentials"
  End If

set  Arg=nothing
'End Function

---------------------------
Microsoft Excel
---------------------------
Set tdConnection = CreateObject("TDApiOle80.TDConnection")
tdConnection.InitConnectionEx 
tdConnection.Login usr,pwd
tdConnection.Connect dmn , prj
If tdConnection.LoggedIn = True Then
Msgbox "Connected Successfully"
else
msgbox "Invalid Credentials"
End If"
---------------------------
OK   
---------------------------

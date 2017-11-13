on error resume next
Dim Arg, qcID, qcPWD, qcDomain, qcProject,i 
i=1
Set Arg = WScript.Arguments 
   qcURL = "https://alm.corp.chartercom.com/qcbin"
   qcID = Arg(0)
   qcPWD = Arg(1)
   qcDomain = Arg(2)
   qcProject = Arg(3)
   
   Set exWb = GetObject("C:\Users\kkaushik\Desktop\ToolX1\ToolX\ToolX.xlsm")
   nPath = exWb.Worksheets("Sheet2").Cells(2, 2).Value
   Test_Set_Name = exWb.Worksheets("Sheet2").Cells(2, 3).Value
   
   msgbox npath
   msgbox Test_Set_Name
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
  Set exWb = GetObject("C:\Users\kkaushik\Desktop\ToolX1\ToolX\ToolX.xlsm")
  If tdConnection.LoggedIn = True Then      
     ' Get the test set tree manager from the test set factory
     'tdconnection is the global TDConnection object.
     Set TSetFact = tdConnection.TestSetFactory
     Set tsTreeMgr = tdConnection.testsettreemanager
 	 ' Get the test set folder passed as an argument to the example code
     Set tsFolder = tsTreeMgr.NodeByPath(nPath)
	 If tsFolder Is Nothing Then  
        Msgbox nPath & "Folder Not Exist"
	  else 
	    Set tsList = tsFolder.FindTestSets(Test_Set_Name)
		If tsList Is Nothing Then
		   Msgbox "Test Set not exist"
		  else

exWb.Worksheets("ALM_Status").Cells(1, 1).Value = "Test Case ID"
exWb.Worksheets("ALM_Status").Cells(2, 1).Value = "Status"
exWb.Worksheets("ALM_Status").Cells(3, 1).Value = "Test Case Name"

		    For Each testsetfound In tsList
				Set tsFolder = testsetfound.TestSetFolder
				Set tsTestFactory = testsetfound.tsTestFactory
				Set tsTestList = tsTestFactory.NewList("")
                			
					For Each tsTest In tsTestList
					    exWb.Worksheets("ALM_Status").Cells(i, 1).Value = tsTest.testid
						exWb.Worksheets("ALM_Status").Cells(i, 2).Value = tsTest.Status
						exWb.Worksheets("ALM_Status").Cells(i, 3).Value = tsTest.Name
						i=i+1
						'testrunname = "Test Case name"
						'If tsTest.Name = "Test case Name" Then

					'--------------------------------------------Accesss the Run Factory --------------------------------------------------------------------
					'Set RunFactory = tsTest.RunFactory
					'Set obj_theRun = RunFactory.AddItem(CStr(testrunname))
					'obj_theRun.Status = "Passed" '-- Status to be updated
					'obj_theRun.Post
					'End If
					Next 
			Next 
' 
		End If
exWb.Save
     End If  
	else	
	  exWb.Worksheets("Sheet2").Cells(2, 1).Value=0
	 WScript.quit (1)      
  End If
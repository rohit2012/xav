Public Row
Public sInput
Public myxl, mysheet, mysheet3
Public MachineDict
Public WshShell
Public ProjectSelected 
Public j

Call CopyingAFileWhenNotFound

Function CopyingAFileWhenNotFound()

'This is Your File Name which you want to Copy
sFile = "Charter Remote and Virtual Machines Detail.xlsm"

TodaysDate = Replace(Date(), "/","-")

'Change to match the source folder path
sSFolder = "\\XEN-705\Automation\Utilities Under Modification\Charter Machine Assignment Utility\" & TodaysDate & "\"

'Change to match the destination folder path
sDFolder = "\\XEN-705\Automation\Utilities Under Modification\Charter Machine Assignment Utility\"

'Create Object
Set FSO = CreateObject("Scripting.FileSystemObject")

'Checking If File Is Located in the Source Folder
If Not FSO.FileExists(sDFolder & sFile) Then
	Set objFolder = FSO.GetFolder(sSFolder)

	On Error Resume Next

	Set objFiles = objFolder.Files

	dtmEarliestDate = DateAdd("s", 1, Now())
	For Each objFile In objFiles
		If FSO.objFile.mostRecent.DateLastModified < CDate(dtmEarliestDate)  Then
			FSO.CopyFile objfile, sFile
			Call Remote_Machine_Utility
	Exit For
		Else
			msgbox"File not found"
		End If
	Next

Else
	Call Remote_Machine_Utility
End If

End Function

Function Remote_Machine_Utility()

Set WshShell = WScript.CreateObject("WScript.Shell")
Set myxl = CreateObject("excel.application")
Set MachineDict = CreateObject("Scripting.Dictionary")
Set myxlWorkbook = myxl.Workbooks.Open ("\\XEN-705\Automation\Utilities Under Modification\Charter Machine Assignment Utility\Charter Remote and Virtual Machines Detail.xlsm")
myxl.ActiveSheet.Unprotect "auto"
myxl.Application.Visible = False

Set mysheet = myxl.ActiveWorkbook.Worksheets("Sheet1")
 
'Get the max row occupied in the excel file
Row = mysheet.UsedRange.Rows.Count

'Get the max column occupied in the excel file
Col = mysheet.UsedRange.Columns.Count

Do While x=0
sInput = InputBox("Enter 1 to Get List of Available RDPs" & vbNewLine & vbNewLine & "Enter 2 to Get List of Available VMs" & vbNewLine & vbNewLine & "Enter 3 to Get List of Available RDPs & VMs" & vbNewLine & vbNewLine & "Enter 4 to Get List of Currently Used RDPs & VMs"& vbNewLine & vbNewLine & "Enter 5 to Relase Your RDPs & VMs ", "Selection for Availability")
    If sInput = ""Then
Exit Do
   elseIf sInput = "1"Then
Exit Do
   elseIf sInput = "2"Then
Exit Do
   elseIf sInput = "3"Then
Exit Do
   elseIf sInput = "4"Then
Exit Do
   elseIf sInput = "5"Then
Exit Do
   End If
Loop
if sInput <>"" Then
'To read the data from the entire Excel file
For i = 2 To Row
    UsingBy = mysheet.Cells(i, 6).Value
    MachineType = mysheet.Cells(i, 5).Value

    Select Case sInput
        Case 1, 2

            If UsingBy = "" And (MachineType = "RDP" And sInput = 1) Then
                If Not MachineDict.Exists("LIST OF AVAILABLE ") Then
                    MachineDict.Add "LIST OF AVAILABLE ", MachineType
                End If
                MachineDict.Add mysheet.Cells(i, 3).Value, mysheet.Cells(i, 4).Value

            ElseIf UsingBy = "" And (MachineType = "VM" And sInput = 2) Then
                If Not MachineDict.Exists("LIST OF AVAILABLE ") Then
                    MachineDict.Add "LIST OF AVAILABLE ", MachineType
                End If
                MachineDict.Add mysheet.Cells(i, 3).Value, mysheet.Cells(i, 4).Value
            End If
            
        Case 3
            If UsingBy = "" And (MachineType = "VM" Or MachineType = "RDP") Then
                If Not MachineDict.Exists("LIST OF AVAILABLE ") Then
                    MachineDict.Add "LIST OF AVAILABLE ", "RDPs & VMs"
                End If
                MachineDict.Add mysheet.Cells(i, 3).Value, mysheet.Cells(i, 4).Value
            End If
        
       Case 4
            If UsingBy <> "" Then
                   If Not MachineDict.Exists("LIST OF USED ") Then
                    MachineDict.Add "LIST OF USED ", "RDPs & VMs - Used By - UFT [YES/NO] - Date"
                End If
                MachineDict.Add mysheet.Cells(i, 3).Value, mysheet.Cells(i, 4).Value & " - " & UsingBy & " - "&"UFT"&" [ " & mysheet.Cells(i, 8).Value & " ] - "&"[ "& mysheet.Cells(i, 9).Value & " ]"
            End If
        
        Case 5
            GetValue = UpdateMachineDetails()
            If GetValue = "RELEASED" Then
                Exit For
            End If
    End Select
Next
If i > Row AND (sInput = 1 Or sInput = 2 Or sInput = 3 Or sInput = 4) Then
	Call ShowAvailability
End If
End If
 
'Close Excel
myxl.Application.DisplayAlerts = False
myxl.ActiveSheet.Protect "auto" 
On Error Resume Next
myxlWorkbook.Save "\\XEN-705\Automation\Utilities Under Modification\Charter Machine Assignment Utility\Charter Remote and Virtual Machines Detail.xlsm"
myxlWorkbook.Close 
myxl.Application.Quit
Set mysheet3 = Nothing
Set mysheet = Nothing
Set myxlWorkbook = Nothing
Set myxl = Nothing
MachineDict.RemoveAll
Set MachineDict = Nothing
End Function


Function ShowAvailability()
    Allitems = MachineDict.Items ' Get the items.
    Allkeys = MachineDict.Keys ' Get the keys.
    For k = 0 To MachineDict.Count - 1
        MachinesAvailaibilityList = MachinesAvailaibilityList & vbNewLine & Allkeys(k) & vbTab & Allitems(k)
    Next


    ' Open notepad 
    
    WshShell.Run "notepad", 9

    ' Give Notepad time to load
    WScript.Sleep 1000 

    WshShell.SendKeys MachinesAvailaibilityList

    ' Give Notepad time to load
    WScript.Sleep 1000 

  If sInput = 1 Or sInput = 2 Or sInput = 3 Then     
 Do While y = 0 	
if sInput = 1 then
    sInputAssignMachine = InputBox("Enter 1 to assign Available RDPs on your name Else Enter 0", "Assign RDPs..")
Elseif sInput = 2 then
    sInputAssignMachine = InputBox("Enter 1 to assign Available VMs on your name Else Enter 0", "Assign VMs..")
Elseif sInput = 3 then
    sInputAssignMachine = InputBox("Enter 1 to assign Available RDPs / VMs on your name Else Enter 0", "Assign RDPs / VMs..")	
End IF	
     If sInputAssignMachine = "" Then
               'Closing Notepad
	WshShell.SendKeys "%({F4})", True
    	WshShell.SendKeys "{TAB}"
    	WshShell.SendKeys "{ENTER}"
    	set WshShell = nothing  
 Exit Do
    elseIf sInputAssignMachine = "1" Then
	ExitFlag = False
	For i = 0 to 4
	    
	    If Not myxl.ActiveWorkbook.ReadOnly Then 
    		Call UpdateMachineDetails
		
		'Save the Workbook
		myxl.ActiveWorkbook.Save
		ExitFlag = True
		Exit For
	    Else
		If i = 4 Then
			Set objShell = CreateObject("Wscript.Shell")
			intReturn = objShell.Popup("File is still opened/locked by another automation user, Please try after some time", 3, "Kindly try after some time...")
			Set objShell = Nothing
			Set intReturn = Nothing
			'Closing Notepad
			WshShell.SendKeys "%({F4})", True
    			WshShell.SendKeys "{TAB}"
    			WshShell.SendKeys "{ENTER}"
    			set WshShell = nothing 
Exit Do
    		Else
		 	Set objShell = CreateObject("Wscript.Shell")
			intReturn = objShell.Popup("File is currently being in use for machine assignment for others " & vbNewLine & vbNewLine & "Please wait and DO NOT attempt to RUN the utility until you see the Next Popup Message", 4, "Auto Closure Popup....Please wait")
			Set objShell = Nothing
			Set intReturn = Nothing
		    	WScript.Sleep 15000
		End If
	    End If  
	Next	
Exit Do
    elseIf sInputAssignMachine = "0" Then
	'Closing Notepad
	WshShell.SendKeys "%({F4})", True
    	WshShell.SendKeys "{TAB}"
    	WshShell.SendKeys "{ENTER}"
    	set WshShell = nothing  
Exit Do 
    End If
Loop
End If
End Function

Public Function ListDailog  

Dim bullet
Dim response
bullet = Chr(10) & "   " & Chr(149) & " "
Do
    ProjectSelected1 = InputBox("Please enter the number that corresponds to your project:" & Chr(10) & bullet & "1. NEXT Gen" & bullet & "2. DEVICE MANAGEMENT" & bullet & "3. SAT" & bullet & "4. TN CLEANUP" & bullet & "5. MANUAL" & bullet & "6. OTHER" & Chr(10), "Select Project Option for ")
    If IsNumeric(ProjectSelected1) Then Exit Do 'Detect value response.
    MsgBox "You must enter a numeric value.", 48, "Invalid Entry"
Loop
If ProjectSelected1 = 1 then
	ProjectSelected   = "NEXT Gen"
ElseIf ProjectSelected1 = 2 then
	ProjectSelected   = "DEVICE MANAGEMENT"
ElseIf ProjectSelected1 = 3 then
	ProjectSelected   = "SAT"
Elseif ProjectSelected1 = 4 then
	ProjectSelected   = "TN CLEANUP"
Elseif ProjectSelected1 = 5 then
	ProjectSelected   = "MANUAL"
Elseif ProjectSelected1 = 6 then
	ProjectSelected   = "OTHER"
End If
ProjectSelected1  = ""
End Function


Public Function UpdateMachineDetails()
	ExitFlag  = False
	If CInt(sInput) = 1 Then
		BoxName = InputBox("Enter Last 4 digit/characters of Remote Machine" & vbNewLine & "For multiple, Separate them by , (Comma) " & vbNewLine & vbNewLine & " eg. 1HK3, R31L ...", "Enter Remote 		Machine Information...")
	Elseif CInt(sInput) = 2 Then
		BoxName = InputBox("Enter Last 4 digit/characters of Virtual Machine" & vbNewLine & "For multiple, Separate them by , (Comma) " & vbNewLine & vbNewLine & " eg. M001, M002 ...", "Enter Virtual Machine Information...")
	Elseif CInt(sInput) = 3 or CInt(sInput) = 5 Then
		BoxName = InputBox("Enter Last 4 digit/characters of Remote Machine/Virtual Machine" & vbNewLine & "For multiple, Separate them by , (Comma) " & vbNewLine & vbNewLine & " eg. R31L, 1HK2, M001, M002 ...", 					"Enter Remote / Virtual Machine Information...")
	End IF
If BoxName <> "" Then
	If CInt(sInput) <> 5 Then
	
   		'Closing Notepad
    		WScript.Sleep 500
    		WshShell.SendKeys "%({F4})", True
    		WshShell.SendKeys "{TAB}"
    		WshShell.SendKeys "{ENTER}"
    		set WshShell = nothing
    		Name = InputBox("Please Enter Your First Name", "Automation Developer Name")
		ResourceName = UCase(Name)
		Set mysheet3 = myxl.ActiveWorkbook.Worksheets("Sheet3")
		myxl.ActiveSheet.Unprotect "auto"
		myxl.Application.Visible = False
		For i = 2 To 70
   			FullName = InStr(1, mysheet3.Cells(i,1).Value, ResourceName)
  			if FullName <> "0" then
				ResourceNameUsed = mysheet3.Cells(i,1).Value 
				Project = mySheet3.Cells(i,2).Value
		Exit For
  			Else
  			End If
 		Next
		
	End If

	BoxNameArr = Split(BoxName, ",", -1, 1)

	For j = 0 To UBound(BoxNameArr)
            For m = 1 To Row
	Set mysheet3 = myxl.ActiveWorkbook.Worksheets("Sheet1")
		myxl.ActiveSheet.Unprotect "auto"
		myxl.Application.Visible = False
                If InStr(1, mysheet.Cells(m, 3).Value, UCase(Trim(BoxNameArr(j))), 1) > 0 Then
                    If mysheet.Cells(m, 6).Value <> "" And (sInput = 1 Or sInput = 2 Or sInput = 3 Or sInput = 4) Then
			Set objShell = CreateObject("Wscript.Shell")
			intReturn = objShell.Popup(UCase(BoxNameArr(j)) & " Remote/Virtual Machine is already ASSIGNED to " & mysheet.Cells(m, 6).Value, 3, "Kindly assign another machine...")
			Set objShell = Nothing
			Set intReturn = Nothing
                        'MsgBox UCase(BoxNameArr(j)) & " Remote/Virtual Machine is already ASSIGNED to " & mysheet.Cells(m, 6).Value
                        Flag = True
                    ElseIf mysheet.Cells(m, 6).Value <> "" And CInt(sInput) = 5 Then
		For i = 0 to 4
	    		If Not myxl.ActiveWorkbook.ReadOnly Then 
                        		mysheet.Cells(m, 6).Value = ""
	          			mysheet.Cells(m, 7).Value = ""
	          			mysheet.Cells(m, 8).Value= ""
	          			mysheet.Cells(m, 9).Value = ""
	          			mysheet.Cells(m, 10).Value = ""
                        			Flag = True
                        			ExitFlag = True
				'Save the Workbook
				myxl.ActiveWorkbook.Save
				Set objShell = CreateObject("Wscript.Shell")
				intReturn = objShell.Popup(UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) have been RELEASED", 3, "Machine Released...")
				Set objShell = Nothing
				Set intReturn = Nothing
				'MsgBox UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) have been RELEASED"
		Exit For
			Else
				If i = 4 Then
					Flag = True
					Set objShell = CreateObject("Wscript.Shell")
				  	intReturn = objShell.Popup("File is still opened/locked by another automation user, Please try after some time" & vbNewLine & vbNewLine & "Your " & UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) have not been RELEASED", 5, "Kindly try after some time...")
					Set objShell = Nothing
					Set intReturn = Nothing
					
    				Else
		 			Set objShell = CreateObject("Wscript.Shell")
					intReturn = objShell.Popup("File is currently being in use for machine release for others " & vbNewLine & vbNewLine & "Please wait and DO NOT attempt to RUN the utility until you see the Next Popup Message", 4, "Auto Closure Popup....Please wait")
					Set objShell = Nothing
					Set intReturn = Nothing
		    			WScript.Sleep 10000
				End If
	    		End If  
		Next	
                    ElseIf mysheet.Cells(m, 6).Value = "" And CInt(sInput) = 5 Then
				Set objShell = CreateObject("Wscript.Shell")
				intReturn = objShell.Popup(UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) is NOT ASSIGNED to ANY User", 3, "Machine Not Assigned...")
				Set objShell = Nothing
				Set intReturn = Nothing
                        'MsgBox UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) is NOT ASSIGNED to ANY User"
                        Flag = True
                        ExitFlag = True
                    Else 
	      Do While z= 0
	
		sInput = InputBox("Enter 1 If You Need UFT Licence with Your Machine" & vbNewLine & vbNewLine & "Enter 2 If You Don't Need UFT Licence with Your Machine", "Need UFT For " & UCase(BoxNameArr(j)))
	     
			If sInput = "" Then
				Flag = True
				myxl.ActiveWorkbook.Save
				Set objShell = CreateObject("Wscript.Shell")
				intReturn = objShell.Popup(UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) have not been ASSIGNED on Your Name", 3, "Machine Not Assigned...")
				Set objShell = Nothing
				Set intReturn = Nothing				
				'MsgBox UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) have not been ASSIGNED on Your Name"
				Exit For
	      Exit Do		
			ElseIf sInput = "1" Then
				Call ListDailog
				mysheet.Cells(m, 6).Value = UCase(ResourceNameUsed)
				if ProjectSelected = "" then
				mysheet.Cells(m, 7).Value= UCase(Project)
				Else
				mysheet.Cells(m, 7).Value= UCase(ProjectSelected)
				End If
				mysheet.Cells(m, 8).Value = "YES"
				mysheet.Cells(m, 9).Value = Date
				mysheet.Cells(m, 10).Value = Time 
				
				Flag = True
		Exit Do		
			ElseIf sInput = "2" Then
				Call ListDailog
				mysheet.Cells(m, 6).Value = UCase(ResourceNameUsed)
				if ProjectSelected = "" then
				mysheet.Cells(m, 7).Value= UCase(Project)
				Else
				mysheet.Cells(m, 7).Value= UCase(ProjectSelected)
				End If
				mysheet.Cells(m, 8).Value = "NO"
				mysheet.Cells(m, 9).Value = Date
				mysheet.Cells(m, 10).Value = Time 

				Flag = True
	      Exit Do		
			End If
	      Loop
			    
		If mysheet.Cells(m, 6).Value = "" And mysheet.Cells(m, 7).Value = "" Then
				mysheet.Cells(m, 8).Value= ""
				mysheet.Cells(m, 9).Value= ""
				mysheet.Cells(m, 10).Value= ""
				Flag = True
				myxl.ActiveWorkbook.Save
				Set objShell = CreateObject("Wscript.Shell")
				intReturn = objShell.Popup(UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) have not been ASSIGNED on the name Of " & ResourceNameUsed & " with project : " & ProjectSelected, 3, "Machine Not Assigned...")
				Set objShell = Nothing
				Set intReturn = Nothing	
				'MsgBox UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) have not been ASSIGNED on Your Name"
				Exit For
		End If
			myxl.ActiveWorkbook.Save
				Set objShell = CreateObject("Wscript.Shell")
				intReturn = objShell.Popup(UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) have been ASSIGNED on the name Of " & ResourceNameUsed & " with project : " & ProjectSelected , 3, UCase(BoxNameArr(j)) & "Machine is Assigned...")
				Set objShell = Nothing
				Set intReturn = Nothing	
                        'MsgBox UCase(BoxNameArr(j)) & " Remote/Virtual Machine(s) have been ASSIGNED on Your Name"
			
                     End If
                End If
            	Next

            If Flag <> True Then
				Set objShell = CreateObject("Wscript.Shell")
				intReturn = objShell.Popup(UCase(BoxNameArr(j)) & " Remote / Virtual Machine Not Found", 3, "Machine not found...")
				Set objShell = Nothing
				Set intReturn = Nothing
                'MsgBox "Remote / Virtual Machine Not Found"
                Exit For
            End If
	    If j>UBound(BoxNameArr) Then
		If ExitFlag = True Then
			Exit For
		End If
	    End If

            
	Next

	If (mysheet.Cells(m, 6).Value = "" Or mysheet.Cells(m, 6).Value = "") And CInt(sInput) = 5 Then
    		UpdateMachineDetails = "RELEASED"
	End If
Else
		
		'Closing Notepad
		UpdateMachineDetails = "RELEASED"
		Flag = True

    		set WshShell = nothing
	
End If
End Function




Call Outlook
Sub Outlook()
    Set objSysInfo = CreateObject("ADSystemInfo")
    strUser = objSysInfo.UserName
    Set objUser = GetObject("LDAP://" & strUser)
    strFullName = objUser.Get("displayName")
    
    Call UserName(Resource)
   Call ReturnResource(ResourceName, Resource, EmailResource)
   Set objOutlook = CreateObject("Outlook.Application")
   Set objMail = objOutlook.CreateItem(0)
   
  UserNames = "Abhishek Gola <agola@xavient.com>; Ali Mahdi <amahdi@xavient.com>; Aliya Tabassum <atabassum@xavient.com>; Ashutosh Kumar (CHR) <akumar19@xavient.com>; Chandan Panigrahi <cpanigrahi@xavient.com>; Chirag Singh <csingh5@xavient.com>; Deepika Singh <dsingh6@xavient.com>; Gangineni Vihar <gvihar@xavient.com>; Haroon Zia <hzia@xavient.com>; Irfan Ahamad <IAhamad1@xavient.com>; Jogender <jogender@xavient.com>; Kamal Ahmad Ansari (kansari@xavient.com); Kunal Kaushik <kkaushik@xavient.com>; Mohd Arshad (marshad@xavient.com); Mohd Wasim <mwasim@xavient.com>; Nimish Kumar <nkumar9@xavient.com>; Nishant Makkar <NMakkar@xavient.com>; Nisha Kumari <nkumari@xavient.com>; Rohit Singh Gaharwar (rgaharwar@xavient.com); Sarsij Kumar <sarsijk@xavient.com>; Shadab Hasan <shasan@xavient.com>; S M Shahzad Raza (sraza1@xavient.com); Shivam Shrivastava (sshrivastava@xavient.com); Soumen Roy <sroy@xavient.com>; Tasaduq Mohi Ud Din Mir (tmir@xavient.com); Vibhor Jain <vjain@xavient.com>; Kush Gautam <kgautam@xavient.com>; Md Tarab Akhtar <makhtar2@xavient.com>; Mahima Agrawal <magrawal1@xavient.com>; Harish Chandra Kandpal <hkandpal@xavient.com>; Mohd Razi Ahmad <mrahmad@xavient.com>; Ankur Rana <arana@xavient.com>; Prasun Kumar Jha <pjha2@xavient.com>; Ayaz Hasan <ahasan1@xavient.com>; Mohd Sharib Islam <mislam@xavient.com>; Ande Sharath Chandra <achandra1@xavient.com>; Mahammad Sikandar <msikandar@xavient.com>; Amaan Hasan <ahasan2@xavient.com>; Darpan Sharma <DSharma2@xavient.com>; Gaurav Sharma (CHR) <gsharma7@xavient.com>"
   
LogonUser = strFullName & " <" & EmailResource & ">;"
   MailtoUser = Replace(UserNames, LogonUser, "")
   TextfilePath = "Z:\Others\Utilities\UtilityforClarityMail\TimeEntry_By_ClarityUtility.txt"
   objMail.To = MailtoUser
   objMail.CC = "Nishant Kumar Yadav <nkyadav@xavient.com>; Rashid Mustafa <rmustafa@xavient.com>; Syed Amir Ali Naqvi <saanaqvi@xavient.com>"
   objMail.Subject = "Clarity Time Tracking Sheet"
   Call fnReadData(TextfilePath, fnReadData1)
   fnReadData1 = Replace(fnReadData1, "<ResourceName>", strFullName)
   objMail.body = fnReadData1
   objMail.Display   'To display message
    wscript.sleep 6000
    'objMail.Send
    Set objMail = Nothing
    Set objOutlook = Nothing
End Sub
   

'Function for Reading data from Notepad
Sub fnReadData(sNote, fnReadData1)
     'NotePad file already exists with data, we need to get all the present data
     Dim objFSO, objPad
     Set objFSO = CreateObject("Scripting.FileSystemObject")
     'Opening and Reading the Data
     Set objPad = objFSO.OpenTextFile(sNote)
     sData = objPad.ReadAll()
     'Destroying the objects
     Set objFSO = Nothing
     Set objPad = Nothing
     fnReadData1 = sData
End Sub

Sub UserName(Resource)
 Set wshShell = CreateObject("WScript.Shell")
 strUserName = wshShell.ExpandEnvironmentStrings("%USERNAME%")
 Resource = strUserName
End Sub

Sub ReturnResource(ResourceName, Resource, EmailResource)
    Set xlApp = CreateObject("Excel.Application")
    xlApp.Visible = False
    Set xlBook = xlApp.Workbooks.Open("Z:\Others\Charter Team Contact Details.xlsx")
    Set xlSheet = xlBook.Worksheets("Sheet1")
    columncount = xlSheet.UsedRange.Columns.Count
    RowCount = xlSheet.UsedRange.Rows.Count
    Set mysheet = xlApp.ActiveWorkbook.Worksheets("Sheet1")
    For column_number = 1 To columncount
        column_header = mysheet.Cells(1, column_number).Value
        If InStr(column_header, "Xavient") > 0 Then
        For j = 1 To RowCount
          EmailResource = mysheet.Cells(j, column_number).Value
          If InStr(EmailResource, Resource) > 0 Then
              ResourceName = mysheet.Range("C" & j).Value
              Exit For
          End If
        Next
        End If
    Next
  xlBook.Close
  xlApp.Quit
    
End Sub

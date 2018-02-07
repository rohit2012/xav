Option Explicit

Dim app 'As Object

Call OpenAsReadOnly
 
Sub OpenAsReadOnly()

   On Error Resume Next
   Set app = GetObject(, "Excel.Application")
   If IsEmpty(app) Then Set app = CreateObject("Excel.Application")

   With app.Workbooks.Open(WScript.Arguments(0))
      If Not .ReadOnly Then .ChangeFileAccess 3 'xlReadOnly
      app.Visible = True
   End With

End Sub
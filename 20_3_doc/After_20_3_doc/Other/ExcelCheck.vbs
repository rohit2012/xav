processname = "EXCEL.EXE"
query = "SELECT * FROM Win32_Process WHERE Name='" & processname & "'"

Set fso = CreateObject("Scripting.FileSystemObject")
Set f = fso.OpenTextFile("D:\list.txt")

Do Until f.AtEndOfStream
  skipServer = False
  server = f.ReadLine

  On Error Resume Next
  Set wmi = GetObject("winmgmts://" & server &"/root/cimv2")
  If Err Then
    WScript.Echo "Error " & Err.Number & " connecting to " & server ": " _
      & Err.Description
    On Error Goto 0
    skipServer = True
  End If
  On Error Goto 0

  If Not skipServer Then
    found = "not "
    For Each process in wmi.ExecQuery(query)
      found = ""
      Exit For
    Next

    WScript.Echo "Excel.EXE " & found & "running on " & server & "."
  End If
Loop

f.Close
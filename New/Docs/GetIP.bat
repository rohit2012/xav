@echo off

for /f %%a in (C:\Users\svc_automation\Desktop\Get_IP\hosts.txt) do call :process %%a

goto :eof
 

:process

set hostname=%1

for /f "tokens=4 delims=: " %%r in ('ping -n 1 %hostname%^|find /i "Statistics"') do echo %hostname% %%r >> C:\Users\svc_automation\Desktop\Get_IP\output.txt
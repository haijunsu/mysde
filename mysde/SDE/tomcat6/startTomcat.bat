@echo off
call setEnv.bat
net start %SERVICE_NAME%
@echo on

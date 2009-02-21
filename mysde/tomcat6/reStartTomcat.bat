@echo off
call setEnv.bat
net stop %SERVICE_NAME%
net start %SERVICE_NAME%
@echo on

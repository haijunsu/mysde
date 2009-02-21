@echo off
call setEnv.bat
net stop %SERVICE_NAME%
@echo on

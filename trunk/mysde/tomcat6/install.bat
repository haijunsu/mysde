@echo off
call setEnv.bat
call setconf.bat
call %CATALINA_HOME%\bin\service.bat install %SERVICE_NAME%
echo Starting %SERVICE_NAME% ...
net start %SERVICE_NAME%
@echo on

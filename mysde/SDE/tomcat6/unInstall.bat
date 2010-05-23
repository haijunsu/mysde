@echo off
call setEnv.bat
echo Stoping %SERVICE_NAME% ...
net stop %SERVICE_NAME%
call %CATALINA_HOME%\bin\service.bat remove %SERVICE_NAME%
@echo on

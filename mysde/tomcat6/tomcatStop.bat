@echo off
call setEnv.bat
call %CATALINA_HOME%\bin\shutdown.bat
@echo on

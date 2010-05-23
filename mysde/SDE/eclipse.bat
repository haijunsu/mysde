@echo off
call setenv.bat
start %APP_HOME%\eclipse\eclipse -vm "%JAVA_HOME%\bin\javaw.exe" -data "%WORK_HOME%"\workspace


@echo off
rem set path=d:\developer\servers\apache2\bin;%path%
call ..\setenv.bat
httpd -n ApacheForHaijun -k restart
@echo on

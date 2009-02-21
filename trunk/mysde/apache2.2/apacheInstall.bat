@ echo off
rem set path=d:\developer\servers\apache2\bin;%path%
call ..\setenv.bat
call setconf.bat
httpd -d "%CD%" -E "%CD%\logs\start_error.log" -n ApacheForHaijun -k install
httpd -n ApacheForHaijun -k start
echo on

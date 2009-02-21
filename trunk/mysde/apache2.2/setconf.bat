@echo off
md conf
md logs
xcopy /Y /E /Q %APACHE_HOME%\conf conf
set SERVER_NAME=localhost
set SERVER_PORT=80
echo set SERVER_ROOT=%APACHE_HOME%> tmp
echo set DOCUMENT_ROOT=%CD%\htdocs>> tmp
echo set WORK_ROOT=%WORK_HOME%>> tmp
sed 's/\\\\/\\\\\\//g' tmp> tmp.bat
call tmp.bat
sed 's/@SERVER_ROOT@/%SERVER_ROOT%/g' httpd.conf.2.2 > httpd.tmp
sed 's/@DOCUMENT_ROOT@/%DOCUMENT_ROOT%/g' httpd.tmp > httpd.tmp1
sed 's/@WORK_ROOT@/%WORK_ROOT%/g' httpd.tmp1> httpd.tmp 
sed 's/@SERVER_NAME@/%SERVER_NAME%/g' httpd.tmp> httpd.tmp1
sed 's/@PORT@/%SERVER_PORT%/g' httpd.tmp1> conf/httpd.conf
del tmp*
del httpd.tmp*



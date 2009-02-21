@echo off
md conf
md tmp
md data
xcopy /Y /E /Q %MYSQL_HOME%\data data
set SERVER_PORT=3306
echo set MYSQL_ROOT=%MYSQL_HOME%>> temp
echo set WORK_ROOT=%WORK_HOME%>> temp
sed 's/\\\\/\\\\\\//g' temp> temp.bat
call temp.bat
sed 's/@PORT@/%SERVER_PORT%/g' my.ini.orig > mysql.temp
sed 's/@MYSQL_ROOT@/%MYSQL_ROOT%/g' mysql.temp > mysql.temp1
sed 's/@WORK_HOME@/%WORK_ROOT%/g' mysql.temp1> mysql.temp 
sed 's/@SERVER_NAME@/%SERVER_NAME%/g' mysql.temp> mysql.temp1
sed 's/@PORT@/%SERVER_PORT%/g' mysql.temp1> conf/my.ini
del temp*
del mysql.temp*



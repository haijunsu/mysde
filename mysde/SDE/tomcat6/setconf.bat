@echo off
call setEnv.bat
md conf
md conf\Catalina
md conf\Catalina\localhost\
md temp
md webapps
copy /Y  %TOMCAT_HOME%\conf conf
copy /Y tomcat-users.xml conf
sed 's/@HTTP_PORT@/%HTTP_PORT%/g' server.xml.templete > server.tmp
sed 's/@SERVER_PORT@/%SERVER_PORT%/g' server.tmp > server.tmp1
sed 's/@AJP_PORT@/%AJP_PORT%/g' server.tmp1 > server.tmp
sed 's/@SSL_PORT@/%SSL_PORT%/g' server.tmp > conf\server.xml
del server.tmp*
sed 's/ start / jpda start /g' %TOMCAT_HOME%\bin\startup.bat > tomcatDebug.bat


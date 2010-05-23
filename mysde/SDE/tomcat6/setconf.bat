@echo off
call setEnv.bat
md conf
md conf\catalina
md conf\catalina\localhost\
md temp
md webapps
md webapps\manager
md webapps\host-manager
md webapps\ROOT
copy /Y  %TOMCAT_HOME%\conf conf
copy /Y tomcat-users.xml conf
sed 's/@HTTP_PORT@/%HTTP_PORT%/g' server.xml.templete > server.tmp
sed 's/@SERVER_PORT@/%SERVER_PORT%/g' server.tmp > server.tmp1
sed 's/@AJP_PORT@/%AJP_PORT%/g' server.tmp1 > server.tmp
sed 's/@SSL_PORT@/%SSL_PORT%/g' server.tmp > conf\server.xml
del server.tmp*
sed 's/ start / jpda start /g' %TOMCAT_HOME%\bin\startup.bat > tomcatDebug.bat
rem echo ^<Context path="/" reloadable="true" docBase="%TOMCAT_HOME%\webapps\ROOT"/^> >conf\catalina\localhost\Root.xml
rem echo ^<Context path="/manager" reloadable="true" docBase="%TOMCAT_HOME%\webapps\manager"/^> >conf\catalina\localhost\manager.xml
rem echo ^<Context path="/host-manager" reloadable="true" docBase="%TOMCAT_HOME%\webapps\host-manager"/^> >conf\catalina\localhost\host-manager.xml
xcopy /Y /E /Q %TOMCAT_HOME%\webapps\manager webapps\manager
xcopy /Y /E /Q %TOMCAT_HOME%\webapps\host-manager webapps\host-manager
xcopy /Y /E /Q %TOMCAT_HOME%\webapps\ROOT webapps\ROOT

echo ^<Context path="/docs" reloadable="true" docBase="%TOMCAT_HOME%\webapps\docs"/^> >conf\catalina\localhost\docs.xml
echo ^<Context path="/examples" reloadable="true" docBase="%TOMCAT_HOME%\webapps\examples"/^> >conf\catalina\localhost\examples.xml

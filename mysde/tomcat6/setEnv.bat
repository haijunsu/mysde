echo setting Env ...
call ../setEnv.bat
rem Define server ports
set HTTP_PORT=9000
set SSL_PORT=9001
set JPDA_PORT=9002
set JMX_PORT=9003
set SERVER_PORT=9004
set AJP_PORT=9005

rem Define JDPA
set JPDA_TRANSPORT=dt_socket
set JPDA_ADDRESS=%JPDA_PORT%

set CATALINA_HOME=%TOMCAT_HOME%
set CATALINA_BASE=%cd%
set SERVICE_NAME=HaijunTomcatService

if not "%HAS_SET_CATALINA%" == "" goto end
set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote 
set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.port=%JMX_PORT%
set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.ssl=false 
set CATALINA_OPTS=%CATALINA_OPTS% -Dcom.sun.management.jmxremote.authenticate=false
set HAS_SET_CATALINA=true

:end


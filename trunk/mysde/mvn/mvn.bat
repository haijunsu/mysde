@echo off
call %MAVEN_HOME%\bin\mvn.bat -Dorg.apache.maven.user-settings=%WORK_HOME%\mvn\conf\settings.xml %*

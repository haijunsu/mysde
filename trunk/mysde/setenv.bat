@echo off
if not "%HAS_RUN%" == "" goto end
title Haijun Dos
echo Setting environment ....
REM set base home
set APP_HOME=F:\haijun\apps
set WORK_HOME=F:\haijun\work
set FREE_COMMANDER_HOME=F:\haijun\FreeCommander
set CYGWIN_HOME=C:\WINDOWS\system32\bsh

set DEV_HOME=%WORK_HOME%\dev
set USERPATH=%WORK_HOME%

REM set APPs Home
set APACHE_HOME=%APP_HOME%\Apache2.2
set MAVEN_HOME=%APP_HOME%\apache-maven-2.0.9
set TOMCAT_HOME=%APP_HOME%\apache-tomcat-6.0.18
set JAVA_HOME=%APP_HOME%\jdk\jdk1.6.0_12
set SVN_HOME=%APP_HOME%\svn-win32-1.5.5
set MYSQL_HOME=%APP_HOME%\mysql-5.1.31-win32
set PGSQL_HOME=%APP_HOME%\pgsql-8.3.1

set path=%CYGWIN_HOME%\bin;%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%MYSQL_HOME%\bin;%PGSQL_HOME%\bin;%SVN_HOME%\bin;%APP_HOME%\dostools;%APACHE_HOME%\bin;%FREE_COMMANDER_HOME%\tools\vim\vim72;%WORK_HOME%;%path%

rem doskey ls=dir $*
rem doskey rm=del $*
rem doskey cp=copy $*
rem doskey mv=move $*
rem doskey vi=vim $*
rem doskey scp=pscp $*
rem %WORK_HOME:~0,2%$T is used to change disk partition.
doskey cdhome=%WORK_HOME:~0,2%$Tcd %WORK_HOME%
doskey cddev=%DEV_HOME:~0,2%$Tcd %DEV_HOME%

set HAS_RUN=true

echo Done! Have a good time!
:end


@echo off
if not "%HAS_RUN%" == "" goto end
title Haijun Dos
echo Setting environment ....
REM set base home
set APP_HOME=c:\apps
set WORK_HOME=c:\mywork

REM dos commands
set GNUWIN32_HOME=%APP_HOME%\GnuWin32

set DEV_HOME=%WORK_HOME%\dev
set USERPATH=%WORK_HOME%
set PLEXUS_NEXUS_WORK = %WORK_HOME%\m2

REM set APPs Home
set APACHE_HOME=%APP_HOME%\Apache2.2
set MAVEN_HOME=%APP_HOME%\apache-maven-2.2.1
set TOMCAT_HOME=%APP_HOME%\apache-tomcat-6.0.18
set JAVA_HOME=%APP_HOME%\jdk\jdk1.6.0_12
set SVN_HOME=%APP_HOME%\svn-win32-1.5.5
set MYSQL_HOME=%APP_HOME%\mysql-5.1.31-win32
set PGSQL_HOME=%APP_HOME%\pgsql-8.3.1


set path=%GNUWIN32_HOME%\coreutils\bin;%GNUWIN32_HOME%\utils\bin;%GNUWIN32_HOME%\otherutils\bin;%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%MYSQL_HOME%\bin;%PGSQL_HOME%\bin;%SVN_HOME%\bin;%APP_HOME%\dostools;%APACHE_HOME%\bin;%WORK_HOME%;%path%

rem doskey ls=dir $*
rem doskey rm=del $*
rem doskey cp=copy $*
rem doskey mv=move $*
doskey vi=vim $*
doskey scp=pscp $*
doskey cdhome=%WORK_HOME:~0,2%$Tcd %WORK_HOME%
doskey cddev=%DEV_HOME:~0,2%$Tcd %DEV_HOME%
doskey ll=ls -altr $*

set HAS_RUN=true

echo Done! Have a good time!
:end
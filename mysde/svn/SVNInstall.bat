@echo off
echo Creating SVNService . . .
sc create svnservice binpath= "\"%SVN_HOME%\bin\svnserve.exe\" --service -r %WORK_HOME%\svn\repos" displayname= "SVNService" depend= Tcpip start= auto
echo Starting SVNService . . .
net start SVNService

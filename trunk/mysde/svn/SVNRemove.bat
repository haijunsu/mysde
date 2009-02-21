@echo off
echo Stopping SVNService . . .
net stop SVNService
echo Removing SVNService . . .
sc delete svnservice

@echo off
mkdir conf
mkdir local
mkdir local\repo
call ../setenv.bat
echo set LOCAL_REPO=%WORK_HOME%\mvn>> temp
sed 's/\\\\/\\\\\\//g' temp> temp.bat
call temp.bat
rm temp*
sed 's/@LOCAL_REPO@/%LOCAL_REPO%/g' settings.xml > conf/settings.xml

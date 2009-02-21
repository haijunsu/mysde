@echo off
echo Install MySQL to your computer. . .
call ../setenv.bat
call setconf.bat
mysqld --install MySQL --defaults-file=%cd%/conf/my.ini
echo Starting MySQL Service . . .
net start MySQL
echo on

@echo off
call ..\setenv.bat
echo stopping MySQL service. . .
net stop MySQL
echo Remove MySQL from your computer. . .
mysqld --remove
echo on

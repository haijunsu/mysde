@echo off
rem net user postgres pgsql8@tt /ADD /EXPIRES:NEVER /PASSWORDCHG:NO
rem net localgroup users postgres /delete
rem runas /noprofile /env /user:postgres "initdb -D data -E UTF8 --no-locale -A md5 -U root --pwfile=rootpass.txt"
call ../setEnv.bat
initdb -D data -E UTF8 --no-locale -A md5 -U root --pwfile=rootpass.txt
echo on


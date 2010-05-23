@echo off
rem runas /noprofile /env /user:postgres "pg_ctl start -w -D data -l pgsql.log"
call ../setEnv.bat
pg_ctl start -w -D %WORK_HOME%\pgsql\data -l pgsql.log
echo on


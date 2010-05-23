@echo off
call ../setEnv.bat
pg_ctl -D data -l pgsql.log stop -m smart
echo on


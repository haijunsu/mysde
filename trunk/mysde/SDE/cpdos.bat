@echo off
echo @echo off >dos.bat
echo cmd.exe /F:ON /k %cd%\setenv.bat>>dos.bat
copy dos.bat %windir%
del dos.bat
echo Done!

@echo off 
setlocal enableDelayedExpansion 
set WD=C:\Users\danie\Desktop
set MYDIR=C:\Assignment\Material_assignment
for /F %%x in ('dir /B/D %MYDIR%\input') do (
  set INPUT=%MYDIR%\input\%%x
  set OUTPUT=%MYDIR%\output\summary.csv
  echo %MYDIR%\input\%%x
  echo %MYDIR%\output\summary.csv
  java -jar %WD%\OMA.jar -i !INPUT! -o !OUTPUT!
  echo --------- Stampato file !INPUT! ------------------
)
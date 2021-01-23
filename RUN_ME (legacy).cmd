@ECHO OFF
cls
color 0a

set filename=%1
set prefix=..\Data\
SET PATH=C:\Program Files\Java\jdk1.8.0_271\bin
cd src
javac *.java
IF "%filename%" == "" GOTO NOINPUT

:INPUT
echo "INPUT"
set pth=%prefix%%filename%
java Runner %pth%
GOTO CLEANUP

:NOINPUT
echo "NO INPUT"
java Runner ..\Data\dashboard2020F.txt
GOTO CLEANUP

:CLEANUP
echo "CLEAN"
del *.class
pause
cd ..
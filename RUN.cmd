@ECHO OFF
color 0a

set prefix=..\Data\
:: change this to a path that contains a Java version 8 JDK if it's not the default
:: this is for JavaFX to work (Java 11 does not automatically include JavaFX)
:: SET PATH=C:\Program Files\Java\jdk1.8.0_271\bin

:START
cls
cd src
javac *.java

ECHO "1. Fall 2020"
ECHO "2. Spring 2021"
ECHO "3. Quit"

set /P op="Enter your choice: "
echo %op%
IF %op%==3 GOTO Q
IF %op%==2 GOTO S21
IF %op%==1 GOTO F20

:F20
echo F20
java Runner ..\Data\dashboard2020F.txt
del *.class
pause
cd ..
GOTO START

:S21
echo S21
java Runner ..\Data\dashboard2021S.txt
del *.class
pause
cd ..
GOTO START

:Q
echo "QUIT"
QUIT

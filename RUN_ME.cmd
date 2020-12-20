@echo off
cd src
javac *.java
java Runner ..\Data\dashboard.txt
del *.class
pause
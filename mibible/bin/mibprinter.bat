@echo off
REM
REM browser.bat: runs the mibible browser
REM

REM set MIBIBLE_HOME variable
set MIBIBLE_HOME=..

REM set CLASSPATH variable
set CLASSPATH=%MIBIBLE_HOME%\lib\mibible-printer-0.1.0.jar;%MIBIBLE_HOME%\lib\mibble-parser-2.8.jar;%MIBIBLE_HOME%\lib\mibble-mibs-2.8.jar;%MIBIBLE_HOME%\lib\grammatica-bin-1.4.jar;%MIBIBLE_HOME%\lib\snmp4_13.jar

REM run the mibible browser
java com.googlecode.mibible.printer.MibPrinter %1 %2 %3 %4 %5 %6 %7 %8 %9

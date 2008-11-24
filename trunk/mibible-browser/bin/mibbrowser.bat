@echo off
REM
REM browser.bat: runs the mibible browser
REM

REM set MIBIBLE_HOME variable
set MIBIBLE_HOME=..

REM set CLASSPATH variable
set CLASSPATH=%MIBIBLE_HOME%\lib\mibible-browser-0.3.0beta.jar;%MIBIBLE_HOME%\lib\mibble-parser-2.8.jar;%MIBIBLE_HOME%\lib\mibble-mibs-2.8.jar;%MIBIBLE_HOME%\lib\grammatica-bin-1.4.jar;%MIBIBLE_HOME%\lib\snmp4_13.jar;%MIBIBLE_HOME%\lib\h2-1.1.103.jar

REM run the mibible browser
java com.googlecode.mibible.browser.MibBrowser

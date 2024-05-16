@echo off
setlocal

REM Set the servlet API path
set "SERVLET_API=%cd%\lib\servlet-api.jar"

REM 
set "src=%cd%\src"

REM 
set "OUTPUT_DIR=bin"
set "JAR_FILE=myLib\framework.jar"

REM 
for /R "%src%" %%f in (*.java) do (
    javac -cp "%SERVLET_API%;%src%" -d "%OUTPUT_DIR%" "%%f"
)

REM
cd /d %OUTPUT_DIR%

REM Create the JAR fil
jar cvf "%JAR_FILE%" *

echo JAR creation completed.
pause



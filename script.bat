@echo off
setlocal

REM Set the servlet API path
set "SERVLET_API=%cd%\lib\servlet-api.jar"

REM Set the Paranamer path
set "PARANAMER=%cd%\lib\paranamer-2.8.jar"

REM Set the source directory
set "src=%cd%\src"

REM Set the output directory
set "OUTPUT_DIR=bin"
set "JAR_FILE=myfw\framework.jar"

REM Compile the Java files
for /R "%src%" %%f in (*.java) do (
    javac -cp "%SERVLET_API%;%PARANAMER%;%src%" -d "%OUTPUT_DIR%" "%%f"
)

REM Change to the output directory
cd /d %OUTPUT_DIR%

REM Create the JAR file
jar cvf "%JAR_FILE%" *

echo JAR creation completed.
pause

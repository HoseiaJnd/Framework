@echo off
REM compilation
javac -cp "C:/apache-tomcat-10.1.12/lib/srvlet-api.jar" -d . *.java

REM transformer en fichier jar
jar cvf framework.jar *.class
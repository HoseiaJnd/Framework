@echo off
setlocal

REM Définir le chemin vers l'API servlet
set "SERVLET_API=%cd%\lib\servlet-api.jar"

REM Chemin vers le répertoire source
set "src=%cd%\src"

REM Répertoire de sortie pour les fichiers .class
set "OUTPUT_DIR=bin"

REM Chemin complet du fichier JAR à créer
set "JAR_FILE=myfw\framework.jar"

REM Compilation des fichiers source .java
for /R "%src%" %%f in (*.java) do (
    javac -cp "%SERVLET_API%;%src%" -d "%OUTPUT_DIR%" "%%f"
)

REM Se déplacer vers le répertoire de sortie
cd /d %OUTPUT_DIR%

REM Créer le fichier JAR
jar cvf "%JAR_FILE%" *

echo Création du JAR terminée.
pause

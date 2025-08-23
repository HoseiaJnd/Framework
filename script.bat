cmd /k "@echo off
REM Script de compilation avec Maven

REM Vérifier si Maven est installé
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven n'est pas installé ou n'est pas dans le PATH
    echo Veuillez installer Maven et l'ajouter au PATH
    exit /b 1
)

REM Nettoyer et compiler le projet avec Maven
echo Compilation du projet avec Maven...
call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors de la compilation
    exit /b 1
)

REM Copier le jar dans le dossier de destination
echo Copie du fichier JAR...
if not exist "dist" mkdir dist
copy /Y "target\FrameworkJND.jar" "dist\FrameworkJND.jar"

echo.
echo Compilation terminée avec succès!
echo Le fichier JAR se trouve dans le dossier dist\"

pause



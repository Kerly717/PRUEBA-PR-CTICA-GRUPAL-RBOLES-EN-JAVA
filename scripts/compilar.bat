@echo off
cd /d "%~dp0.."
if not exist out mkdir out
dir /s /b src\*.java > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
del fuentes.txt
echo Compilacion correcta.

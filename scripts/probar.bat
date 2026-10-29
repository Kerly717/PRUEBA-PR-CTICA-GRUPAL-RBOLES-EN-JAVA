@echo off
call "%~dp0compilar.bat"
cd /d "%~dp0.."
chcp 65001 >nul
java -Dfile.encoding=UTF-8 -cp out uta.pruebas.PruebasRunner

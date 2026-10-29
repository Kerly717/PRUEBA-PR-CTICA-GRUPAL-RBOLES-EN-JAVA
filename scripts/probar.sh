#!/usr/bin/env bash
# Compila y ejecuta las pruebas (código de salida 0 = todas pasan)
set -e
"$(dirname "$0")/compilar.sh"
cd "$(dirname "$0")/.."
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out uta.pruebas.PruebasRunner

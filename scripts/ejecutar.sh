#!/usr/bin/env bash
# Menú interactivo. Con "--demo" ejecuta los casos de aceptación.
set -e
"$(dirname "$0")/compilar.sh"
cd "$(dirname "$0")/.."
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out uta.app.Main "$@"

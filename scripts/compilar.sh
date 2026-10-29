#!/usr/bin/env bash
# Compila todo en ./out (requiere JDK 17+)
set -e
cd "$(dirname "$0")/.."
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
echo "Compilación correcta."

#!/bin/bash
set -e
cd "$(dirname "$0")"
SRC_DIR="src"
OUT_DIR="out"
MAIN_CLASS="com.expensetracker.Main"
mkdir -p "$OUT_DIR"
find "$SRC_DIR" -name "*.java" > sources.txt
javac -d "$OUT_DIR" @sources.txt
rm sources.txt
java -cp "$OUT_DIR" "$MAIN_CLASS"

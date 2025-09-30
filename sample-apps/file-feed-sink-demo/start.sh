#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "$0")" && pwd)"
MODULE_DIR="$DIR"
PID_FILE="$MODULE_DIR/.run.pid"
LOG_FILE="$MODULE_DIR/.run.log"

usage() {
  echo "Usage: $0 [--bg] [--input <path>] [--output <path>]"
  echo "  --bg           Run in background (writes PID to $PID_FILE)"
  echo "  --input PATH   Input file path (default: ./data/input.txt)"
  echo "  --output PATH  Output file path (default: ./data/output.txt)"
}

BG=0
INPUT="$MODULE_DIR/data/input.txt"
OUTPUT="$MODULE_DIR/data/output.txt"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --bg) BG=1; shift ;;
    --input) INPUT="$2"; shift 2 ;;
    --output) OUTPUT="$2"; shift 2 ;;
    -h|--help) usage; exit 0 ;;
    *) echo "Unknown arg: $1"; usage; exit 1 ;;
  esac
done

# Ensure output dir exists
mkdir -p "$(dirname "$OUTPUT")"

# Build just this module
pushd "$MODULE_DIR" >/dev/null
mvn -q -DskipTests package
popd >/dev/null

# Run shaded fat jar
JAR="$MODULE_DIR/target/file-feed-sink-demo-1.0-SNAPSHOT-jar-with-dependencies.jar"
if [[ ! -f "$JAR" ]]; then
  echo "ERROR: Shaded jar not found at $JAR. Did the build succeed?" >&2
  exit 1
fi

CMD=("${JAVA_HOME:-${java_home:-}}${JAVA_HOME:+/bin}/java" --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED -jar "$JAR" "$INPUT" "$OUTPUT")

if [[ $BG -eq 1 ]]; then
  echo "Starting in background..."
  nohup "${CMD[@]}" >"$LOG_FILE" 2>&1 &
  echo $! > "$PID_FILE"
  echo "Started. PID $(cat "$PID_FILE"). Logs: $LOG_FILE"
else
  echo "Running in foreground..."
  "${CMD[@]}"
fi

#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "$0")" && pwd)"
MODULE_DIR="$DIR"
PID_FILE="$MODULE_DIR/.run.pid"
LOG_FILE="$MODULE_DIR/.run.log"

usage() {
  echo "Usage: $0 [--bg] [--input <path>] [--checkpoint <path>]"
  echo "  --bg              Run in background (writes PID to $PID_FILE)"
  echo "  --input PATH      Input file path (default: ./data/input.txt)"
  echo "  --checkpoint PATH Checkpoint file path (default: ./data/checkpoint.txt)"
}

BG=0
INPUT="$MODULE_DIR/data/input.txt"
CHECKPOINT="$MODULE_DIR/data/checkpoint.txt"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --bg) BG=1; shift ;;
    --input) INPUT="$2"; shift 2 ;;
    --checkpoint) CHECKPOINT="$2"; shift 2 ;;
    -h|--help) usage; exit 0 ;;
    *) echo "Unknown arg: $1"; usage; exit 1 ;;
  esac
done

# Ensure dirs exist
mkdir -p "$(dirname "$INPUT")"
mkdir -p "$(dirname "$CHECKPOINT")"

# Build just this module
pushd "$MODULE_DIR" >/dev/null
mvn -q -DskipTests package
popd >/dev/null

# Run shaded fat jar
JAR="$MODULE_DIR/target/state-checkpoint-demo-1.0-SNAPSHOT-jar-with-dependencies.jar"
if [[ ! -f "$JAR" ]]; then
  # fallback to wildcard in case version differs
  JAR_CANDIDATE=("$MODULE_DIR"/target/state-checkpoint-demo-*-jar-with-dependencies.jar)
  if [[ -f "${JAR_CANDIDATE[0]}" ]]; then
    JAR="${JAR_CANDIDATE[0]}"
  else
    echo "ERROR: Shaded jar not found at $JAR. Did the build succeed?" >&2
    exit 1
  fi
fi

CMD=("${JAVA_HOME:-${java_home:-}}${JAVA_HOME:+/bin}/java" --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED -jar "$JAR" "$INPUT" "$CHECKPOINT")

if [[ $BG -eq 1 ]]; then
  echo "Starting in background..."
  nohup "${CMD[@]}" >"$LOG_FILE" 2>&1 &
  echo $! > "$PID_FILE"
  echo "Started. PID $(cat "$PID_FILE"). Logs: $LOG_FILE"
else
  echo "Running in foreground..."
  "${CMD[@]}"
fi

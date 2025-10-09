#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "$0")" && pwd)"
PID_FILE="$DIR/.run.pid"

if [[ ! -f "$PID_FILE" ]]; then
  echo "No PID file found at $PID_FILE"
  exit 0
fi

PID=$(cat "$PID_FILE")
if ps -p "$PID" >/dev/null 2>&1; then
  echo "Stopping process $PID ..."
  kill "$PID" || true
  sleep 1
  if ps -p "$PID" >/dev/null 2>&1; then
    echo "Force killing $PID ..."
    kill -9 "$PID" || true
  fi
  echo "Stopped."
else
  echo "Process $PID not running."
fi
rm -f "$PID_FILE"

#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "$0")" && pwd)"
PID_FILE="$DIR/.run.pid"

if [[ ! -f "$PID_FILE" ]]; then
  echo "No PID file found at $PID_FILE. Is the app running in background?"
  exit 1
fi

PID=$(cat "$PID_FILE")
if ps -p "$PID" > /dev/null 2>&1; then
  echo "Stopping process $PID..."
  kill "$PID"
  # Wait briefly
  for i in {1..10}; do
    if ps -p "$PID" > /dev/null 2>&1; then
      sleep 0.2
    else
      break
    fi
  done
  if ps -p "$PID" > /dev/null 2>&1; then
    echo "Process did not stop, sending SIGKILL"
    kill -9 "$PID" || true
  fi
  echo "Stopped."
else
  echo "Process $PID not running."
fi
rm -f "$PID_FILE"

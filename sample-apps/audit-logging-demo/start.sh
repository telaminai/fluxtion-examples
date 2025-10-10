#!/usr/bin/env bash
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

mvn -q -DskipTests package

JAVA_OPTS=(
  "--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED"
  "-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager"
  "-Dlog4j.configurationFile=${SCRIPT_DIR}/src/main/resources/log4j2.yaml"
)

JAR="target/audit-logging-demo-1.0-SNAPSHOT-jar-with-dependencies.jar"

exec java "${JAVA_OPTS[@]}" -jar "$JAR"

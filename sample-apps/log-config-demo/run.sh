#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$DIR"

# Build
mvn -q -DskipTests package

JAVA_OPTS=(
  "--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED"
  "-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager"
  "-Dlog4j.configurationFile=${DIR}/log4j2.yaml"
)

JAR_FILE=$(ls target/log-config-demo-*-jar-with-dependencies.jar | head -n 1)

exec java "${JAVA_OPTS[@]}" -jar "$JAR_FILE"

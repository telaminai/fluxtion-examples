@echo off
setlocal enabledelayedexpansion

set DIR=%~dp0
pushd %DIR%

REM Build
mvn -q -DskipTests package

for /f %%i in ('dir /b target\log-config-demo-*-jar-with-dependencies.jar') do set JAR_FILE=target\%%i

set JAVA_OPTS=--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager -Dlog4j.configurationFile=%DIR
\log4j2.yaml

java %JAVA_OPTS% -jar %JAR_FILE%

popd
endlocal

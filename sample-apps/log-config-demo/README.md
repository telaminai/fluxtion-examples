# Log Config Demo

This sample app demonstrates how logging is configured in Fluxtion examples using Log4j2, and how to bridge java.util.logging (JUL) to Log4j2.

What it shows:
- Using a Log4j2 YAML configuration file (log4j2.yaml)
- Directing JUL to Log4j2 via the system property: `-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager`
- A simple DataFlow that reads lines from a file and logs them

## Project layout
- src/main/java/com/telamin/fluxtion/example/sampleapps/logging/LogConfigDemoRunner.java
- log4j2.yaml (copied from the reference module; located at the module root)
- data/input.txt (sample input)
- data/log (log outputs written here)
- run.sh / run.bat helper scripts

## How logging is controlled
- The Log4j2 configuration is provided in `log4j2.yaml` at the module root.
  - Console appender prints messages to STDOUT using a minimal pattern
  - AppLog writes to `data/log/app.log`
  - ErrorLog writes to `data/log/error.log`
  - EventAudit writes YAML-like entries to `data/log/auditLog.yaml`
- Logger levels:
  - `com.telamin` is set to DEBUG in this sample
  - Root is INFO

## Bridging java.util.logging (JUL) to Log4j2
To ensure any JUL-based logs are routed through Log4j2, the system property below must be set:

```
-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager
```

In the sample code, the line is present but commented out:

```java
// System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
```

Note: Passing the property on the command line (via the scripts below) is recommended so it applies before classes are initialized.

## Maven libraries

add the following maven dependencies to your project to make use of the Log4j2 YAML configuration, select the version 
that matches your Log4j2 version:

```xml
<!-- Log4j2: JUL bridge, API, and Core implementation -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-jul</artifactId>
    <version>2.23.1</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.23.1</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.23.1</version>
</dependency>
<!-- YAML support for Log4j2 configuration files -->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
    <version>2.17.2</version>
</dependency>
```

## Running the sample
Requirements: Java 21+, Maven

- Linux/macOS:
  ```bash
  ./run.sh
  ```
- Windows:
  ```bat
  run.bat
  ```

What the scripts do:
- Build the module with Maven
- Run the app with:
  - `--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED`
  - `-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager`
  - `-Dlog4j.configurationFile=log4j2.yaml`

## Expected output
- Console shows lines like `flow in:alpha`, `flow in:beta`, `flow in:gamma`
- Log files are created in `data/log/` per the configuration

## Notes
- You can adjust logger levels and appenders in `log4j2.yaml` to suit your needs.
- If you move the config file, remember to update `-Dlog4j.configurationFile` in the run scripts.

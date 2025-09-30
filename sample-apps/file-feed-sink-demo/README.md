# File Feed → DataFlow → File Sink Demo

This sample demonstrates an end-to-end Fluxtion application that reads lines from a file, processes them using a
DataFlow pipeline, and writes the transformed output to another file.

It uses the following Fluxtion components:

- FileEventFeed: streams lines from an input file as events
- DataFlow: subscribes to the feed, logs and transforms each event (upper-case)
- FileMessageSink: writes the transformed result to an output file

Code is inspired by getting-started TutorialPart5.

## Project layout

- src/main/java/com/telamin/fluxtion/example/sampleapps/FileFeedSinkDemo.java – the main app
- data/input.txt – example input data
- data/output.txt – output file (created when the app runs)
- start.sh – helper script to build and run the app
- stop.sh – stops the background run started by start.sh --bg

## Prerequisites

- Java 21+
- Maven 3.8+

## Build and run

From this module directory:

- Build the shaded (fat) jar:
  mvn -DskipTests package

- Foreground run (reads data/input.txt and writes data/output.txt):
  ./start.sh

- Background run, with optional input/output overrides:
  ./start.sh --bg --input ./data/input.txt --output ./data/output.txt
  # Later stop it:
  ./stop.sh

- Run the fat jar directly (without the helper script):
  java --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED -jar target/file-feed-sink-demo-1.0-SNAPSHOT-jar-with-dependencies.jar ./data/input.txt ./data/output.txt

## What it does

- Reads each line from the input file once (ReadStrategy.EARLIEST)
- Logs to console: read file in:<line>
- Transforms to upper-case
- Logs to console: write file out:<TRANSFORMED>
- Writes the transformed line to the output file

Edit data/input.txt and re-run to see new output generated to data/output.txt.

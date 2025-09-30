# Simple deployed DataFlow application: File Feed Sink Demo

This sample demonstrates an end-to-end Fluxtion application that reads lines from a file, processes them through a
DataFlow pipeline, and writes the transformed output to another file.

It uses the following Fluxtion components:

- FileEventFeed: streams lines from an input file as events
- DataFlow: subscribes to the feed, logs and transforms each event (upper-case)
- FileMessageSink: writes the transformed result to an output file

Code is inspired by getting-started TutorialPart5.

## Deployable fat-jar application

This module builds a shaded (fat) jar that contains the app and all of its runtime dependencies. The jar is a
self-contained, deployable, and executable application:

- You can copy the single jar to another machine and run it with java -jar.
- No need to have Maven on the target machine, only a Java 21+ runtime.
- The app accepts two optional arguments: <inputFile> <outputFile>.

## Project layout

- src/main/java/com/telamin/fluxtion/example/sampleapps/FileFeedSinkDemo.java – the main app
- data/input.txt – example input data
- data/output.txt – output file (created when the app runs)
- start.sh – helper script to build and run the app
- stop.sh – stops the background run started by start.sh --bg

## Prerequisites

- Java 21+
- Maven 3.8+ (build time only)

## Build the fat jar

From this module directory:

- Build the shaded (fat) jar:
  mvn -DskipTests package 
- Artifact produced: target/file-feed-sink-demo-1.0-SNAPSHOT-jar-with-dependencies.jar

## Ways to run

- Foreground run (reads data/input.txt and writes data/output.txt):
  ./start.sh

- Background run, with optional input/output overrides:
  ./start.sh --bg --input ./data/input.txt --output ./data/output.txt

### Later stop it:
  ./stop.sh

- Run the fat jar directly (without the helper script):
  java --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED -jar
  target/file-feed-sink-demo-1.0-SNAPSHOT-jar-with-dependencies.jar ./data/input.txt ./data/output.txt

Note: the --add-opens option is required because the runtime uses jdk.internal.misc.Unsafe via Agrona.

## What you will see on the console

When run in the foreground, the app logs each input line as it is processed and again when it is written to the output
file. Example output for the sample input:

```console
Running in foreground...
read file in:hello world
write file out:HELLO WORLD
read file in:fluxtion is cool
write file out:FLUXTION IS COOL
read file in:end to end demo
write file out:END TO END DEMO
```

## Live demo: tail the output and append to the input

You can observe the pipeline working in real-time by tailing the output file and appending to the input file.

- Terminal 1: start the app (foreground or background)
  ```console
  ./start.sh --bg
  ```

- Terminal 2: tail the output file
  ```console 
  tail -f ./data/output.txt`
  ```

- Terminal 3: append new lines to the input file
  ```console 
  echo "new event one" >> ./data/input.txt
  echo "another line"   >> ./data/input.txt`
  ```

As you append, the FileEventFeed sees new lines, the DataFlow runs (upper-casing them), and the FileMessageSink pushes
the
results into data/output.txt. The tail command will show the transformed lines appearing in real time:

```console
HELLO WORLD
FLUXTION IS COOL
END TO END DEMO
NEW EVENT ONE
ANOTHER LINE
```

If you prefer not to run in the background, you can run in the foreground and observe the console logs directly while
also tailing the output file in another terminal.

# Fluxtion Examples

A curated set of small, focused examples that demonstrate how to build high‑performance, type‑safe, event‑driven
dataflows with Fluxtion.

Fluxtion is a Java library for composing real‑time streaming logic as declarative DataFlows. It lets you wire sources,
transformations, joins, windows, and sinks into a single, efficient runtime graph that you can push events into with
minimal overhead.

These examples help you:

- Get a feel for the DataFlow API in minutes
- Learn common patterns (map, filter, groupBy, windowing, triggers)
- See how to join multiple feeds, manage state, and aggregate over time
- Start from hello‑world and progress to more realistic snippets you can reuse

If you are new to Fluxtion, start with the getting-started module, then dip into the reference examples when you need a
feature in isolation.

## Repository layout

- getting-started
    - Quick introductions and progressive tutorials that build intuition fast.
    - Includes:
        - HelloFluxtion – the smallest possible DataFlow that subscribes to strings, transforms, and prints.
        - Quickstart examples – e.g. GroupBy + sliding windows for live aggregates.
        - Front‑page snippets – small, readable demonstrations of joining multiple feeds, windowing, and trigger
          handling.
        - Tutorial series – Part 1–5, stepping through essential concepts.
- reference
    - Focused, bite‑sized samples for one feature at a time. Useful as a cookbook.
    - Areas covered:
        - functional: map, filter, flatMap, merge, sinks, default values, re‑entrant events
        - groupby: keying, joins (inner/outer), reduce, toList/toSet, sliding/tumbling windows
        - windowing: sliding and tumbling windows, trigger patterns
        - trigger: update/publish/reset mechanics
        - node: wrapping functions, subscribing to nodes, push patterns
        - eventfeed: simple runner for file‑based inputs

## Quick start

Prerequisites:

- Java 17+ (the examples include headers for JDK 25 and are compatible with current LTS)
- Maven 3.9+

Build everything:

- mvn -q -DskipTests package

Build just the getting-started module (and its dependencies):

- mvn -q -pl getting-started -am -DskipTests package

Run from your IDE:

- Open this project in IntelliJ IDEA (or your IDE of choice)
- Navigate to a class with a main method (for example,
  getting-started/src/main/java/com/telamin/fluxtion/example/HelloFluxtion.java)
- Click Run

Optional: Run with JBang (several example files include //DEPS and //JAVA headers):

- jbang getting-started/src/main/java/com/telamin/fluxtion/example/HelloFluxtion.java

Note: If you prefer Maven exec, add the maven-exec-plugin to the module’s pom or run from your IDE for the simplest
experience.

## Try a few examples

HelloFluxtion (subscribe → map → console):

- File: getting-started/src/main/java/com/telamin/fluxtion/example/HelloFluxtion.java
- What it shows: the minimal pipeline – subscribe to String, uppercase it, print it. Great for verifying your toolchain.

Average speed by make with sliding window (groupBy + windowing):

- File: getting-started/src/main/java/com/telamin/fluxtion/example/quickstart/GroupByWindowExample.java
- What it shows: grouping by a key and maintaining a sliding time window with bucketed aggregation, then publishing to a
  sink.

Joining multiple feeds (multi‑join):

- File: getting-started/src/main/java/com/telamin/fluxtion/example/frontpage/multijoin/MultiFeedJoinExample.java
- What it shows: joining heterogeneous streams and applying small user functions.

Windowing and triggers:

- Files: getting-started/src/main/java/com/telamin/fluxtion/example/frontpage/windowing/WindowExample.java,
  .../triggering/TriggerExample.java
- What they show: rolling/tumbling windows and trigger‑driven publishing.

Tutorial series:

- Files: getting-started/src/main/java/com/telamin/fluxtion/example/tutorial/TutorialPart1.java through
  TutorialPart5.java
- What they show: a guided path from basic event handling to stateful joins and time‑based processing.

Reference cookbook (single‑feature snippets):

- Directory: reference/src/main/java/com/telamin/fluxtion/example/reference
- How to use: scan the sub‑packages (functional, groupby, windowing, trigger, node, eventfeed) and open a class that
  matches the feature you need; most are small and self‑contained.

## Tips for running

- Most examples have a public static void main so you can run them directly from your IDE.
- Some quickstart files include JBang headers (//DEPS, //JAVA) so you can run them without setting up a full build.
- If you run long‑lived examples (e.g., ones scheduling events), stop them from your IDE when done.

## Learn more

- Fluxtion repository: https://github.com/telamin/fluxtion
- Fluxtion README and docs: https://github.com/telamin/fluxtion#readme
- Issues and discussions: use the Fluxtion repository for questions, ideas, or problems you find while using these
  examples.

## License

This repository is licensed under GPL-3.0-only (see LICENSE).


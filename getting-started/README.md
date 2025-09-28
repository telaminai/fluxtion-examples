# Getting started examples

This module contains simple, self‑contained examples to help you learn Fluxtion’s DataFlow API quickly. Start here if
you’re new: run a tiny hello‑world flow, then explore short snippets for windowing, triggers, and multi‑feed joins. A
tutorial series (Part 1–5) builds concepts step by step.

## Examples

- Hello, Fluxtion: the minimal subscribe → map → print
  - File: [src/main/java/com/telamin/fluxtion/example/HelloFluxtion.java](src/main/java/com/telamin/fluxtion/example/HelloFluxtion.java)
- Quickstart: average speed by make with sliding window (groupBy + windowing)
  - File: [src/main/java/com/telamin/fluxtion/example/quickstart/GroupByWindowExample.java](src/main/java/com/telamin/fluxtion/example/quickstart/GroupByWindowExample.java)
- Front‑page snippets
  - Windowing: [WindowExample.java](src/main/java/com/telamin/fluxtion/example/frontpage/windowing/WindowExample.java)
  - Triggering: [TriggerExample.java](src/main/java/com/telamin/fluxtion/example/frontpage/triggering/TriggerExample.java)
  - Multi‑join: [MultiFeedJoinExample.java](src/main/java/com/telamin/fluxtion/example/frontpage/multijoin/MultiFeedJoinExample.java)
- Tutorials (progressive walkthrough)
  - [TutorialPart1.java](src/main/java/com/telamin/fluxtion/example/tutorial/TutorialPart1.java) — Basics: build a tiny flow to compute per-symbol running net quantity.
  - [TutorialPart2.java](src/main/java/com/telamin/fluxtion/example/tutorial/TutorialPart2.java) — Sliding windows: per-symbol rolling average with thresholded alerts.
  - [TutorialPart3.java](src/main/java/com/telamin/fluxtion/example/tutorial/TutorialPart3.java) — Mix DSL with an imperative stateful node and lifecycle @Start/@Stop.
  - [TutorialPart4.java](src/main/java/com/telamin/fluxtion/example/tutorial/TutorialPart4.java) — Embed a DataFlow in a microservice with HTTP health/metrics and latency alerts.
  - [TutorialPart5.java](src/main/java/com/telamin/fluxtion/example/tutorial/TutorialPart5.java) — Wire file feeds and sinks: read from a file, transform, and write to a file.

## How to run

- Most classes have a public static void main; run directly from your IDE.
- Some files include JBang headers (//DEPS, //JAVA), so you can run with: jbang <path-to-file>

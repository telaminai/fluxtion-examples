# Event Replay Demo

## Overview

This example demonstrates Fluxtion's event replay mechanism - a powerful capability for recording and replaying event
streams with deterministic, data-driven time. The replay mechanism enables developers to capture production event
streams and replay them in development environments with **exact** reproduction of production behavior, including
time-dependent logic.

## What is Event Replay?

Event replay is an event sourcing pattern where:

1. **Recording phase**: Events flowing through the system are captured to an event log along with their timestamps
2. **Replay phase**: The recorded events are fed back into a fresh processor instance, recreating the exact same
   behavior

The key innovation is **data-driven time** - instead of using wall clock time during replay, the processor's Clock is
set to the recorded timestamp of each event. This ensures time-dependent logic executes deterministically during replay.

## Why Event Replay is Useful

Event replay solves critical production support and testing challenges:

### 1. **Debug Production Issues in Your IDE**

- Capture event logs from production systems experiencing issues
- Replay those exact events locally with full IDE debugging capabilities
- Set breakpoints, inspect variables, and step through the exact logic that ran in production
- **No need to guess** what happened by inspecting log files

### 2. **Test Changes Against Real Production Data**

- Validate that bug fixes work against the actual event stream that caused the problem
- Verify that enhancements don't break existing behavior with real-world data
- Regression test using historical event logs

### 3. **Deterministic Time for Reproducible Results**

- Time-dependent business logic (timeouts, scheduling, aggregations) behaves identically on replay
- No flaky tests due to timing issues
- Audit logs from replay exactly match production audit logs

### 4. **Reduced Support Costs**

- Diagnose and fix production issues offline without direct production access
- Share reproducible test cases with the development team
- Validate fixes before deploying to production

## AOT Compilation Advantages for Replay

This example uses **AOT (Ahead-of-Time) compilation** to generate the event processor at build time. This provides
critical advantages for replay:

### Static Dataflow Graph

The dataflow graph is compiled into concrete Java code (`GlobalPnlProcessor.java`) at build time. This means:

- The **exact same logic** runs in production and during replay
- No dynamic graph construction that might differ between environments
- No risk of configuration differences between test and production

### Debuggable Production Code

Because the processor is generated as normal Java source code:

- You can set breakpoints directly in the generated event handlers
- Step through the exact dispatch logic that ran in production
- Inspect the complete call stack and variable states
- The generated code is readable and understandable

### No Dynamic Reconstruction

Traditional dynamic dataflow systems reconstruct graphs at runtime, which can lead to:

- Different behavior if configuration or dependencies have changed
- Inability to reproduce production issues if the dynamic construction differs
- Complex debugging as the runtime graph structure may not match production

With AOT compilation, the graph structure is **frozen at compile time**, guaranteeing identical behavior during replay.

## Clocks and Data-Driven Time

### The Clock Abstraction

Fluxtion provides a `Clock` interface that nodes can inject to access time:

```java
public class GlobalPnl implements NamedNode {
    public Clock clock = Clock.DEFAULT_CLOCK;

    @OnTrigger
    public boolean calculate() {
        String time = dateFormat.format(new Date(clock.getProcessTime()));
        // ... business logic using time
    }
}
```

### Default Behavior: Wall Clock Time

By default, `Clock.DEFAULT_CLOCK` returns system wall clock time - the actual current time.

### Replay Behavior: Data-Driven Time

During replay, `YamlReplayRunner` automatically:

1. Replaces the default clock with a synthetic clock strategy
2. Before each event is dispatched, sets the clock to the recorded timestamp of that event
3. Ensures all time-dependent logic sees the historical time, not current time

This is why the replay output shows **identical timestamps** to the original run, even when replayed hours or days
later.

### Time is Part of the Event Stream

The recorded event log captures time as data:

```yaml
- eventTime: 1696857658794
  event: !com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate
    bookName: "book1"
    amount: 200
```

During replay, `eventTime` becomes the clock value, making time deterministic and controllable.

## Key Classes

### Recording Classes

#### `YamlReplayRecordWriter`

An auditor that captures events to a YAML event log. Features:

- Sees events **before** any business logic processes them
- Records each event with its timestamp
- Supports whitelist/blacklist/all filtering of event types
- Can write to any `Writer` (file, string, network stream)

Configuration in `GlobalPnlAOTGraphBuilder`:

```java
processorConfig.addAuditor(
    new YamlReplayRecordWriter().classWhiteList(PnlUpdate.class), YamlReplayRecordWriter.DEFAULT_NAME);
```

#### `GlobalPnlAOTGraphBuilder`

The AOT builder that defines the dataflow graph structure:

- Creates `BookPnl` nodes for different trading books
- Wires them into a `GlobalPnl` aggregator
- Injects the `YamlReplayRecordWriter` auditor
- Specifies the generated class name and package

### Replay Classes

#### `YamlReplayRunner`

Reads a YAML event log and replays events into a processor:

```java
YamlReplayRunner.newSession(
        new StringReader(eventLog), 
        newGlobalPnlProcessor())
    .callInit()      // Initialize the processor
    .callStart()     // Call start lifecycle
    .runReplay();    // Replay all events with data-driven time
```

Features:

- Automatically sets synthetic clock strategy
- Sets clock time before each event dispatch
- Calls lifecycle methods (init, start) before replay

### Business Logic Classes

#### `PnlUpdate`

The event class carrying P&L updates:

```java
public class PnlUpdate implements Event {
    String bookName;  // Which trading book
    int amount;       // P&L amount

    @Override
    public String filterString() {
        return bookName;  // Routes events to correct BookPnl
    }
}
```

#### `BookPnl`

Handles P&L updates for a specific trading book:

```java
public class BookPnl implements NamedNode {
    private final String bookName;
    private transient int pnl;

    @OnEventHandler(filterVariable = "bookName")
    public boolean pnlUpdate(PnlUpdate pnlUpdate) {
        pnl = pnlUpdate.getAmount();
        return true;  // Trigger downstream nodes
    }
}
```

The `filterVariable = "bookName"` ensures each `BookPnl` only processes events for its book.

#### `GlobalPnl`

Aggregates P&L across all books and prints results:

```java
public class GlobalPnl implements NamedNode {
    public Clock clock = Clock.DEFAULT_CLOCK;
    private final List<BookPnl> bookPnlList;

    @OnTrigger
    public boolean calculate() {
        String time = dateFormat.format(new Date(clock.getProcessTime()));
        int pnl = bookPnlList.stream().mapToInt(BookPnl::getPnl).sum();
        System.out.println(time + "," + pnl);
        return true;
    }
}
```

Uses the injected `Clock` to format timestamps, ensuring deterministic time during replay.

### Generated Class

#### `GlobalPnlProcessor` (generated)

The AOT-compiled event processor containing:

- Concrete event handler methods for each `BookPnl` instance
- Efficient event dispatch logic
- Clock management
- Auditor integration
- Lifecycle methods

This is the class that runs in both production and replay scenarios.

## How the Demo Works

### `GeneraEventLogMain`

The main class demonstrates both recording and replay:

```java
public static void main(String[] args) {
    StringWriter eventLog = new StringWriter();

    // PHASE 1: Record events
    System.out.println("CAPTURE RUN:");
    generateEventLog(eventLog);

    // PHASE 2: Replay events
    System.out.println("\nREPLAY RUN:");
    runReplay(eventLog.toString());
}
```

#### Recording Phase

```java
private static void generateEventLog(Writer writer) {
    GlobalPnlProcessor processor = new GlobalPnlProcessor();
    processor.init();

    // Get the auditor and configure it to write to our writer
    YamlReplayRecordWriter auditor =
            processor.getAuditorById(YamlReplayRecordWriter.DEFAULT_NAME);
    auditor.setTargetWriter(writer);

    processor.start();
    processor.onEvent(new PnlUpdate("book1", 200));
    Thread.sleep(250);  // Real time passes
    processor.onEvent(new PnlUpdate("bookAAA", 55));
}
```

#### Replay Phase

```java
private static void runReplay(String eventLog) {
    YamlReplayRunner.newSession(
                    new StringReader(eventLog),
                    new GlobalPnlProcessor())
            .callInit()
            .callStart()
            .runReplay();
}
```

## Sample Output

When running the demo, you'll see identical output from both runs:

```
CAPTURE RUN:
time,globalPnl
14:40:58.794,200
14:40:59.075,255

REPLAY RUN:
time,globalPnl
14:40:58.794,200
14:40:59.075,255
```

**Notice**: The timestamps are identical! Even though replay happens later, the data-driven clock ensures the same
timestamps appear.

## Project Structure

```
replay-events/
├── src/main/java/com/telamin/fluxtion/example/compile/replay/replay/
│   ├── GlobalPnlAOTGraphBuilder.java  # AOT builder definition
│   ├── GeneraEventLogMain.java        # Demo: record & replay
│   ├── PnlUpdate.java                 # Event class
│   ├── BookPnl.java                   # Per-book P&L handler
│   ├── GlobalPnl.java                 # Aggregate P&L calculator
│   └── generated/
│       └── GlobalPnlProcessor.java    # AOT-generated processor
└── pom.xml
```

## Build and Run

### Generate the AOT Processor

First, generate the `GlobalPnlProcessor` by running the builder:

```bash
cd /Users/greghiggins/IdeaProjects/telamin/fluxtion-examples/compiler/replay-events
mvn -q compile exec:java -Dexec.mainClass="com.telamin.fluxtion.example.compile.replay.replay.GlobalPnlAOTGraphBuilder"
```

This creates `src/main/java/.../generated/GlobalPnlProcessor.java`.

### Run the Demo

```bash
mvn -q compile exec:java -Dexec.mainClass="com.telamin.fluxtion.example.compile.replay.replay.GeneraEventLogMain"
```

You'll see the capture run followed by the replay run with identical outputs.

## Debugging with Replay Files

One of the most powerful uses of event replay is debugging production issues:

1. **Capture production events**: Deploy with `YamlReplayRecordWriter` to a file sink
2. **Retrieve the event log**: Copy the YAML file to your development machine
3. **Create a test case**: Load the file and replay into your processor
4. **Debug in IDE**:
    - Set breakpoints in your business logic (e.g., `BookPnl.pnlUpdate`)
    - Set breakpoints in the generated code (e.g., `GlobalPnlProcessor.handleEvent`)
    - Step through the exact event sequence that occurred in production
    - Inspect all variable states and call stacks

### Example Test Case

```java

@Test
public void debugProductionIssue() throws Exception {
    // Load captured production events
    Reader eventLog = new FileReader("production-issue-2025-10-09.yaml");

    GlobalPnlProcessor processor = new GlobalPnlProcessor();

    // Set breakpoint on next line and step into
    YamlReplayRunner.newSession(eventLog, processor)
            .callInit()
            .callStart()
            .runReplay();

    // Assertions to verify fix
    GlobalPnl globalPnl = processor.getNodeById("globalPnl");
    assertEquals(expectedValue, globalPnl.getCurrentPnl());
}
```

Because the AOT-compiled processor is plain Java code, you can step through every event dispatch and business logic
invocation exactly as it happened in production.

## Static Dataflow Benefits

The AOT compilation approach provides unique advantages:

### Identical Logic Guarantee

- The `.class` files deployed to production are the same as in your development environment
- No configuration files that might differ
- No dynamic graph assembly that might vary
- What you debug is **exactly** what ran in production

### Performance

- No runtime graph construction overhead
- Direct method calls instead of reflection
- JIT-friendly code patterns

### Maintainability

- Generated code is readable and can be inspected
- Easy to understand the dispatch logic
- Refactoring tools work on generated code

## Event Log Format

The YAML event log format is human-readable:

```yaml
- eventTime: 1696857658794
  event: !com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate
    bookName: "book1"
    amount: 200
- eventTime: 1696857659075
  event: !com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate
    bookName: "bookAAA"
    amount: 55
```

Each record contains:

- `eventTime`: The wall clock time when the event was processed (in milliseconds since epoch)
- `event`: The event object serialized to YAML with its class type

## Extending the Example

You can extend this example for your own use cases:

### Different Event Types

Add more event classes and handlers:

```java
processorConfig.addAuditor(
    new YamlReplayRecordWriter().classWhiteList(PnlUpdate .class, TradeEvent .class, MarketData .class),
    YamlReplayRecordWriter.DEFAULT_NAME);
```

### File-Based Recording

Write events to a file instead of memory:

```java
FileWriter fileWriter = new FileWriter("events-" + LocalDate.now() + ".yaml");
yamlReplayRecordWriter.setTargetWriter(fileWriter);
```

### Custom Replay Formats

For production systems, consider:

- Binary formats for performance (Avro, Protobuf)
- Compressed storage
- Database or blob storage backends
- Streaming replay from Kafka or other message systems

The `YamlReplayRecordWriter` and `YamlReplayRunner` serve as reference implementations you can customize.

## Learn More

- [Fluxtion AOT Compiler Documentation](../../fluxtion/docs/compiler/)
- [Clock and Time Management](../../fluxtion/docs/runtime/clocks-and-time.md)
- [Event Sourcing and Replay (Reference)](../../fluxtion/docs/not-used/reference/replay.md)
- [Unit Testing DataFlow](../../fluxtion/docs/how-to/unit-testing-dataflow.md)

## License

This example is part of the Fluxtion Examples project.

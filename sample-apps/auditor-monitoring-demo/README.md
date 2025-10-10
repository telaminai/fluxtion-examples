# Auditor Monitoring Demo

This sample shows how to monitor a DataFlow using the Auditing framework. It demonstrates:

- Implementing a custom Auditor (implements com.telamin.fluxtion.runtime.audit.Auditor)
- Tracking per-node invocation counts and per-event latency
- Publishing metrics to an external monitoring service (a simple OpenTelemetry-like publisher backed by Log4j2)
- Adding the auditor via EventProcessorConfig in an AOT graph builder (imperative builder)

Important: This demo focuses on auditors as general-purpose monitoring hooks. Auditors are not limited to logging; they can emit metrics, perform persistence, profiling, external notifications, and more.

## Project layout

- [MonitoringAuditor.java](src/main/java/com/telamin/fluxtion/example/sampleapps/auditmon/MonitoringAuditor.java)
- [SimpleOtelPublisher.java](src/main/java/com/telamin/fluxtion/example/sampleapps/auditmon/SimpleOtelPublisher.java)
- [AuditorMonitoringDemoRunner.java](src/main/java/com/telamin/fluxtion/example/sampleapps/auditmon/AuditorMonitoringDemoRunner.java)
- [AuditorMonitoringAOTGraphBuilder.java](src/main/java/com/telamin/fluxtion/example/sampleapps/auditmon/AuditorMonitoringAOTGraphBuilder.java)

## How it works

- MonitoringAuditor implements Auditor and receives callbacks for:
  - nodeRegistered: each node added to the graph during init
  - eventReceived: before any node processes the event
  - nodeInvoked: for each node visited on the execution path (auditInvocations returns true)
  - processingComplete: after the event finishes processing
- The auditor updates counters and measures per-event latency (micros) and publishes to a MonitoringPublisher.
- SimpleOtelPublisher is a tiny adapter that prints "otel.*" lines via Log4j2.

## Building and running (builder-based)

Do not use the functional DataFlow API for these demos. The DemoRunner compiles the AOT builder and runs it:

```java
AuditorMonitoringAOTGraphBuilder builder = new AuditorMonitoringAOTGraphBuilder();
CloneableDataFlow<?> df = Fluxtion.compile(builder::buildGraph, builder::configureGeneration);
df.init();
// drive events on df
```

The builder adds both the CalcNode and the MonitoringAuditor, and enables event audit logging so auditor callbacks receive full lifecycle information.

Builder (excerpt):

```java
public class AuditorMonitoringAOTGraphBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig processorConfig) {
        processorConfig.addNode(new AuditorMonitoringDemoRunner.CalcNode());
        processorConfig.addAuditor(new MonitoringAuditor(new SimpleOtelPublisher()), "monitoringAuditor");
        processorConfig.addEventAudit(EventLogControlEvent.LogLevel.INFO);
    }
}
```

## Actual output (from running ./start.sh)

Captured on our run (values will vary):

```
09:43:03.024 [main] INFO  com.telamin.fluxtion.example.sampleapps.auditmon.SimpleOtelPublisher - otel.counter name=node.registered value=1
09:43:03.025 [main] INFO  com.telamin.fluxtion.example.sampleapps.auditmon.SimpleOtelPublisher - otel.counter name=node.registered value=1
09:43:03.025 [main] INFO  com.telamin.fluxtion.example.sampleapps.auditmon.SimpleOtelPublisher - otel.counter name=node.registered value=1
09:43:03.025 [main] INFO  com.telamin.fluxtion.example.sampleapps.auditmon.SimpleOtelPublisher - otel.counter name=node.registered value=1
09:43:03.027 [main] INFO  com.telamin.fluxtion.example.sampleapps.auditmon.SimpleOtelPublisher - otel.timer name=event.duration.micros micros=128242006092
09:43:03.027 [main] INFO  com.telamin.fluxtion.example.sampleapps.auditmon.SimpleOtelPublisher - otel.gauge name=invocations.subscriptionManager value=0.0
09:43:03.027 [main] INFO  com.telamin.fluxtion.example.sampleapps.auditmon.SimpleOtelPublisher - otel.gauge name=invocations.callbackDispatcher value=0.0
09:43:03.027 [main] INFO  com.telamin.fluxtion.example.sampleapps.auditmon.SimpleOtelPublisher - otel.gauge name=invocations.context value=0.0
09:43:03.027 [main] INFO  com.telamin.fluxtion.example.sampleapps.auditmon.SimpleOtelPublisher - otel.gauge name=invocations.calcNode_0 value=0.0
```

## Why auditors?

- General-purpose monitoring: capture metrics (counters, timers, gauges) per node or per event.
- System integration: forward measurements to external tools (OpenTelemetry exporters, Prometheus pushgateway, statsd, custom HTTP services).
- Rich lifecycle view: with auditInvocations() = true, receive callbacks for every node visited (tracing-like visibility) regardless of structured logging.
- SLOs and alerts: emit counters and latency histograms for alerting; track error rates and slow paths.
- Persistence and replay: implement auditors that persist state or write replayable YAML/JSON traces for reproducibility.
- Profiling and diagnostics: build auditors to measure per-node timings, hotspots, and call sequences.
- Auditors can also be nodes, receiving services and injections just like any other component.

## Run

From the module directory:

```
./start.sh
```

Or via Maven:

```
mvn -q -DskipTests exec:java -Dexec.mainClass=com.telamin.fluxtion.example.sampleapps.auditmon.AuditorMonitoringDemoRunner \
  -Dlog4j.configurationFile=src/main/resources/log4j2.yaml \
  -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager
```

## References

- com.fluxtion.dataflow.node.audit.RegistrationListenerTest and MyNodeAudit example
- com.fluxtion.dataflow.replay.YamlReplayRecordWriter (itself an Auditor)
- DataFlow audit configuration methods: com.telamin.fluxtion.runtime.DataFlow#setAuditXXXXXX

package com.telamin.fluxtion.example.sampleapps.auditmon;

import com.telamin.fluxtion.runtime.audit.Auditor;
import com.telamin.fluxtion.runtime.annotations.builder.FluxtionIgnore;
import com.telamin.fluxtion.runtime.event.Event;
import com.telamin.fluxtion.runtime.lifecycle.Lifecycle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple auditor that tracks per-node invocation counts and total processing latency
 * per event and publishes results to a monitoring publisher (e.g., OpenTelemetry).
 */
public class MonitoringAuditor implements Auditor, Lifecycle {

    public interface MonitoringPublisher {
        void publishCounter(String name, long value);
        void publishGauge(String name, double value);
        void publishTimer(String name, long micros);
    }

    private MonitoringPublisher publisher;
    @FluxtionIgnore
    private final Map<String, Long> nodeCounts = new ConcurrentHashMap<>();
    private long eventStartMicros;

    // Default constructor required by AOT codegen
    public MonitoringAuditor() {
        this.publisher = new SimpleOtelPublisher();
    }

    public MonitoringAuditor(MonitoringPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void nodeRegistered(Object node, String nodeName) {
        nodeCounts.put(nodeName, 0L);
        publisher.publishCounter("node.registered", 1);
    }

    @Override
    public void eventReceived(Event event) {
        eventStartMicros = System.nanoTime() / 1_000; // micros
        publisher.publishCounter("event.received", 1);
    }

    @Override
    public boolean auditInvocations() {
        return true; // get nodeInvoked callbacks
    }

    @Override
    public void nodeInvoked(Object node, String nodeName, String methodName, Object event) {
        nodeCounts.merge(nodeName, 1L, Long::sum);
        publisher.publishCounter("node.invoked." + nodeName, 1);
    }

    @Override
    public void processingComplete() {
        long durMicros = (System.nanoTime() / 1_000) - eventStartMicros;
        publisher.publishTimer("event.duration.micros", durMicros);
        // publish counts snapshot
        nodeCounts.forEach((n, c) -> publisher.publishGauge("invocations." + n, c));
    }
}

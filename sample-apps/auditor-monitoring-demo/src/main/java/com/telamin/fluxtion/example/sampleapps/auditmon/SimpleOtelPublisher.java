package com.telamin.fluxtion.example.sampleapps.auditmon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleOtelPublisher implements MonitoringAuditor.MonitoringPublisher {
    private static final Logger LOG = LogManager.getLogger(SimpleOtelPublisher.class);

    @Override
    public void publishCounter(String name, long value) {
        LOG.info("otel.counter name={} value={}", name, value);
    }

    @Override
    public void publishGauge(String name, double value) {
        LOG.info("otel.gauge name={} value={}", name, value);
    }

    @Override
    public void publishTimer(String name, long micros) {
        LOG.info("otel.timer name={} micros={}", name, micros);
    }
}

package com.telamin.fluxtion.example.sampleapps.auditmon;

import com.telamin.fluxtion.Fluxtion;
import com.telamin.fluxtion.builder.compile.config.FluxtionCompilerConfig;
import com.telamin.fluxtion.builder.compile.config.FluxtionGraphBuilder;
import com.telamin.fluxtion.builder.generation.config.EventProcessorConfig;
import com.telamin.fluxtion.runtime.audit.EventLogControlEvent;

/**
 * AOT graph builder using imperative registration. Mirrors the runtime demo but
 * uses EventProcessorConfig to add nodes and auditors.
 */
public class AuditorMonitoringAOTGraphBuilder implements FluxtionGraphBuilder {

    public static void main(String[] args) {
        AuditorMonitoringAOTGraphBuilder builder = new AuditorMonitoringAOTGraphBuilder();
        Fluxtion.compile(builder::buildGraph, builder::configureGeneration);
    }

    @Override
    public void buildGraph(EventProcessorConfig processorConfig) {
        CalcNode calc = new CalcNode();
        // Register calculation node
        processorConfig.addNode(calc);
        // Register the monitoring auditor as a first-class auditor
        processorConfig.addAuditor(new MonitoringAuditor(new SimpleOtelPublisher()), "monitoringAuditor");
        // Optionally enable event audit log records at INFO to see traversal
        processorConfig.addEventAudit(EventLogControlEvent.LogLevel.INFO);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("AuditorMonitoringProcessor");
        compilerConfig.setPackageName("com.telamin.fluxtion.example.sampleapps.auditmon.generated");
    }
}

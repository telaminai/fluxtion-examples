package com.telamin.fluxtion.example.sampleapps.audit;

import com.fluxtion.dataflow.Fluxtion;
import com.fluxtion.dataflow.compiler.config.FluxtionCompilerConfig;
import com.fluxtion.dataflow.compiler.config.FluxtionGraphBuilder;
import com.telamin.fluxtion.builder.generation.config.EventProcessorConfig;
import com.telamin.fluxtion.runtime.CloneableDataFlow;
import com.telamin.fluxtion.runtime.audit.EventLogControlEvent;

/**
 * AOT graph builder using imperative node registration. This is the correct style for AOT:
 * add nodes explicitly and attach auditors via EventProcessorConfig.
 */
public class AuditLoggingAOTGraphBuilder implements FluxtionGraphBuilder {

    public static void main(String[] args) {
        AuditLoggingAOTGraphBuilder builder = new AuditLoggingAOTGraphBuilder();
        Fluxtion.compile(builder::buildGraph, builder::configureGeneration);
    }

    @Override
    public void buildGraph(EventProcessorConfig processorConfig) {
        EnricherNode enricher = new EnricherNode();
        RiskCheckNode risk = new RiskCheckNode(enricher);
        FailWhenIdNode failer = new FailWhenIdNode("FAIL");

        // set instance names easier for logging
        enricher.setName("enricher");
        risk.setName("riskChecker");
        failer.setName("simulatedFailer");

        // Explicitly register nodes in the graph
        processorConfig.addNode(enricher);
        processorConfig.addNode(risk);
        processorConfig.addNode(failer);
        // Add an event audit logger at INFO level so EventLogger records are produced
        processorConfig.addEventAudit();//EventLogControlEvent.LogLevel.INFO);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("AuditLoggingProcessor");
        compilerConfig.setPackageName("com.telamin.fluxtion.example.sampleapps.audit.generated");
    }
}

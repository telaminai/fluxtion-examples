package com.telamin.fluxtion.example.compile.replay.replay;

import com.telamin.fluxtion.Fluxtion;
import com.telamin.fluxtion.builder.compile.config.FluxtionCompilerConfig;
import com.telamin.fluxtion.builder.compile.config.FluxtionGraphBuilder;
import com.telamin.fluxtion.builder.generation.config.EventProcessorConfig;
import com.telamin.fluxtion.builder.replay.YamlReplayRecordWriter;

import java.util.Arrays;

public class GlobalPnlAOTGraphBuilder implements FluxtionGraphBuilder {

    public static void main(String[] args) {
        GlobalPnlAOTGraphBuilder builder = new GlobalPnlAOTGraphBuilder();
        Fluxtion.compile(builder::buildGraph, builder::configureGeneration);
    }

    @Override
    public void buildGraph(EventProcessorConfig processorConfig) {
        processorConfig.addNode(
                new GlobalPnl(Arrays.asList(
                        new BookPnl("book1"),
                        new BookPnl("bookAAA"),
                        new BookPnl("book_XYZ")
                ))
        );
        //Inject an auditor will see events before any node
        processorConfig.addAuditor(
                new YamlReplayRecordWriter().classWhiteList(PnlUpdate.class),
                YamlReplayRecordWriter.DEFAULT_NAME);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("GlobalPnlProcessor");
        compilerConfig.setPackageName("com.telamin.fluxtion.example.compile.replay.replay.generated");
    }
}

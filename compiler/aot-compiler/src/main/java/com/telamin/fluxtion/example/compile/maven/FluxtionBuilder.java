/*
 * SPDX-File Copyright: © 2026.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.compile.maven;

import com.telamin.fluxtion.builder.compile.config.FluxtionCompilerConfig;
import com.telamin.fluxtion.builder.compile.config.FluxtionGraphBuilder;
import com.telamin.fluxtion.builder.generation.config.EventProcessorConfig;
import com.telamin.fluxtion.example.compile.aot.node.PriceLadderPublisher;

public class FluxtionBuilder implements FluxtionGraphBuilder {

    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        eventProcessorConfig.addNode(new PriceLadderPublisher());
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setPackageName("com.telamin.fluxtion.example.compile.maven.generated");
        compilerConfig.setClassName("PriceLadderProcessorAot");
    }
}

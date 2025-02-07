/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.node;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.builder.generation.config.EventProcessorConfig;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.annotations.OnEventHandler;
import com.fluxtion.dataflow.runtime.flowfunction.helpers.Collectors;
import lombok.Getter;
import lombok.ToString;

public class SubscribeToNodeSample {

    @Getter
    @ToString
    public static class MyComplexNode {
        private String in;

        @OnEventHandler
        public boolean stringUpdate(String in) {
            this.in = in;
            return true;
        }
    }

    public static void buildGraph(EventProcessorConfig processorConfig) {

    }

    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribeToNode(new MyComplexNode())
                .console("node triggered -> {}")
                .map(MyComplexNode::getIn)
                .aggregate(Collectors.listFactory(4))
                .console("last 4 elements:{}\n")
                .build();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");
        processor.onEvent("E");
        processor.onEvent("F");
    }
}

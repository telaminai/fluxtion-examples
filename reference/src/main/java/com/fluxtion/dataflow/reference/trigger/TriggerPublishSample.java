/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.trigger;

import com.fluxtion.dataflow.reference.node.SubscribeToNodeSample;
import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.flowfunction.helpers.Collectors;

public class TriggerPublishSample {

    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribeToNode(new SubscribeToNodeSample.MyComplexNode())
                .console("node triggered -> {}")
                .map(SubscribeToNodeSample.MyComplexNode::getIn)
                .aggregate(Collectors.listFactory(4))
                .publishTrigger(DataFlowBuilder.subscribeToSignal("publishMe"))
                .console("last 4 elements:{}")
                .build();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");
        processor.onEvent("E");
        processor.onEvent("F");

        processor.publishSignal("publishMe");
        processor.publishSignal("publishMe");
        processor.publishSignal("publishMe");
    }
}

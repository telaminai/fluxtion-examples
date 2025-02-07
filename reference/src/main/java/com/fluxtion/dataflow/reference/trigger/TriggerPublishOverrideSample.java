/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.trigger;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.reference.node.SubscribeToNodeSample;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.flowfunction.helpers.Collectors;

public class TriggerPublishOverrideSample {

    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribeToNode(new SubscribeToNodeSample.MyComplexNode())
                .console("node triggered -> {}")
                .map(SubscribeToNodeSample.MyComplexNode::getIn)
                .aggregate(Collectors.listFactory(4))
                .publishTriggerOverride(DataFlowBuilder.subscribeToSignal("publishMe"))
                .console("last 4 elements:{}\n")
                .build();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");

        processor.publishSignal("publishMe");
        processor.onEvent("E");
        processor.onEvent("F");
        processor.onEvent("G");
        processor.onEvent("H");

        processor.publishSignal("publishMe");
    }
}

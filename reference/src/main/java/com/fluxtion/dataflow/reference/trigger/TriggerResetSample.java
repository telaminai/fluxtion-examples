/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.trigger;


import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.reference.node.SubscribeToNodeSample;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.flowfunction.helpers.Collectors;

public class TriggerResetSample {

    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribeToNode(new SubscribeToNodeSample.MyComplexNode())
                .console("node triggered -> {}")
                .map(SubscribeToNodeSample.MyComplexNode::getIn)
                .aggregate(Collectors.listFactory(4))
                .resetTrigger(DataFlowBuilder.subscribeToSignal("resetMe").console("\n--- resetTrigger ---"))
                .console("last 4 elements:{}")
                .build();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");

        processor.publishSignal("resetMe");
        processor.onEvent("E");
        processor.onEvent("F");
    }
}

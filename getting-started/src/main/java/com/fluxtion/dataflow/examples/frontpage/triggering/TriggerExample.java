/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

//REPOS repsy-fluxtion=https://repo.repsy.io/mvn/fluxtion/fluxtion-public
//DEPS com.fluxtion.dataflow:dataflow-builder:1.0.0
//COMPILE_OPTIONS -proc:full
package com.fluxtion.dataflow.examples.frontpage.triggering;


import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.flowfunction.helpers.Aggregates;

public class TriggerExample {
    public static void main(String[] args) {
        DataFlow sumDataFlow = DataFlowBuilder.subscribe(Integer.class)
                .aggregate(Aggregates.intSumFactory())
                .resetTrigger(DataFlowBuilder.subscribeToSignal("resetTrigger"))
                .filter(i -> i != 0)
                .publishTriggerOverride(DataFlowBuilder.subscribeToSignal("publishSumTrigger"))
                .console("Current sun:{}")
                .build();

        sumDataFlow.onEvent(10);
        sumDataFlow.onEvent(50);
        sumDataFlow.onEvent(32);
        //publish
        sumDataFlow.publishSignal("publishSumTrigger");

        //reset sum
        sumDataFlow.publishSignal("resetTrigger");

        //new sum
        sumDataFlow.onEvent(8);
        sumDataFlow.onEvent(17);
        //publish
        sumDataFlow.publishSignal("publishSumTrigger");
    }
}

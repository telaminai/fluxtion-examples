/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

//DEPS com.telamin.fluxtion:fluxtion-builder:0.9.6
//JAVA 25

package com.telamin.fluxtion.example.frontpage.triggering;


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

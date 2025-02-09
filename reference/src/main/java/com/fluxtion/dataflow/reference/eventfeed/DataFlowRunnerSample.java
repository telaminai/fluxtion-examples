/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.eventfeed;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.agentrunner.DataFlowAgentRunner;

public class DataFlowRunnerSample {
    public static void main(String[] args) {
        DataFlow dataFlow = DataFlowBuilder.subscribe(String.class)
                .console("received {}")
                .build();

        DataFlowAgentRunner runner = new DataFlowAgentRunner();
        runner.addDataFlow(dataFlow);
        runner.addFeed(new MyEventFeed("myFeed"));

        runner.start();
    }
}

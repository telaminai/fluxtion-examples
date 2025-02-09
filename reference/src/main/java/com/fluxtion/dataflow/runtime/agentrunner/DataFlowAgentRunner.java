/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.runtime.agentrunner;

import com.fluxtion.agrona.concurrent.AgentRunner;
import com.fluxtion.agrona.concurrent.BackoffIdleStrategy;
import com.fluxtion.agrona.concurrent.UnsafeBuffer;
import com.fluxtion.agrona.concurrent.status.AtomicCounter;
import com.fluxtion.dataflow.runtime.DataFlow;

public class DataFlowAgentRunner {

    private final AtomicCounter errorCounter = new AtomicCounter(new UnsafeBuffer(new byte[4096]), 0);
    private final DataFlowAgent agent = new DataFlowAgent("dataFlowIOAgentRunner");
    private final AgentRunner agentRunner = new AgentRunner(
            new BackoffIdleStrategy(10, 10, 1_000, 1_000_000),
            this::errorHandler,
            errorCounter,
            agent
    );

    public DataFlowAgentRunner addFeed(EventFeedAgent feed) {
        start();

        //TODO use status to determine whether to progress
//        agent.status()
//        while (!agentRunner.isClosed()) {
//            System.out.println("waiting for feed");
//        }
        agent.addFeed(feed);
        return this;
    }

    public DataFlowAgentRunner removeFeed(EventFeedAgent feed) {
        start();
        agent.removeFeed(feed);
        return this;
    }

    public DataFlowAgentRunner addDataFlow(DataFlow dataFlow) {
        agent.addDataFlow(dataFlow);
        return this;
    }

    public DataFlowAgentRunner removeDataFlow(DataFlow dataFlow) {
        agent.removeDataFlow(dataFlow);
        return this;
    }

    public DataFlowAgentRunner start() {
        if (!agentRunner.isClosed()) {
            AgentRunner.startOnThread(agentRunner);
        }
        return this;
    }

    public DataFlowAgentRunner stop() {
        if (!agentRunner.isClosed()) {
            agentRunner.close();
        }
        return this;
    }

    private void errorHandler(Throwable throwable) {
    }

}

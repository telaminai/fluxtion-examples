/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.runtime.agentrunner;

import com.fluxtion.agrona.concurrent.*;
import com.fluxtion.agrona.concurrent.status.AtomicCounter;
import com.fluxtion.dataflow.runtime.DataFlow;
import lombok.extern.java.Log;

@Log
public class DataFlowAgentRunner {

    private final AtomicCounter errorCounter = new AtomicCounter(new UnsafeBuffer(new byte[4096]), 0);
    private final DataFlowAgent agent = new DataFlowAgent("dataFlowIOAgentRunner");
    private final AgentRunner agentRunner;

    public DataFlowAgentRunner() {
        this(new BackoffIdleStrategy(10, 10, 1_000_000, 1_000_000_000));
    }

    public DataFlowAgentRunner(IdleStrategy idleStrategy) {
        this.agentRunner = new AgentRunner(
                idleStrategy,
                this::errorHandler,
                errorCounter,
                agent
        );
    }

    public DataFlowAgentRunner addFeed(EventFeedAgent feed) {
        start();
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
        while (!agentRunner.isClosed()) {
            AgentRunner.startOnThread(agentRunner);
        }
        while (agent.status() != DynamicCompositeAgent.Status.ACTIVE) {
            System.out.println("Waiting for the agent to be started...");
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
        log.severe(throwable::getMessage);
        throwable.printStackTrace(System.out);
    }
}
/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.runtime.eventfeed;

import com.fluxtion.agrona.concurrent.*;
import com.fluxtion.agrona.concurrent.status.AtomicCounter;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.annotations.feature.Experimental;
import lombok.extern.java.Log;


@Experimental
@Log
public class EventFeedBroker {

    private final AtomicCounter errorCounter = new AtomicCounter(new UnsafeBuffer(new byte[4096]), 0);
    private final DataFlowAgent agent = new DataFlowAgent("dataFlowIOAgentRunner");
    private final AgentRunner agentRunner;

    public EventFeedBroker() {
        this(new BackoffIdleStrategy(10, 10, 1_000_000, 1_000_000_000));
    }

    public EventFeedBroker(IdleStrategy idleStrategy) {
        this.agentRunner = new AgentRunner(
                idleStrategy,
                this::errorHandler,
                errorCounter,
                agent
        );
    }

    public EventFeedBroker addFeed(EventFeedAgent feed) {
        start();
        agent.addFeed(feed);
        return this;
    }

    public EventFeedBroker removeFeed(EventFeedAgent feed) {
        start();
        agent.removeFeed(feed);
        return this;
    }

    public EventFeedBroker addDataFlow(DataFlow dataFlow) {
        agent.addDataFlow(dataFlow);
        return this;
    }

    public EventFeedBroker removeDataFlow(DataFlow dataFlow) {
        agent.removeDataFlow(dataFlow);
        return this;
    }

    public EventFeedBroker start() {
        log.info("Starting DataFlowAgentRunner");
        while (!agentRunner.isClosed()) {
            log.info("Waiting for DataFlowAgentRunner to be started");
            AgentRunner.startOnThread(agentRunner);
        }
        while (agent.status() != DynamicCompositeAgent.Status.ACTIVE) {
            System.out.println("Waiting for the agent to be started...");
        }
        return this;
    }

    public EventFeedBroker stop() {
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
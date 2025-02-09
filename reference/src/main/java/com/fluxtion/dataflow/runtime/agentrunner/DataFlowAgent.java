/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.runtime.agentrunner;

import com.fluxtion.agrona.concurrent.DynamicCompositeAgent;
import com.fluxtion.agrona.concurrent.OneToOneConcurrentArrayQueue;
import com.fluxtion.dataflow.runtime.DataFlow;

class DataFlowAgent extends DynamicCompositeAgent {

    private final OneToOneConcurrentArrayQueue<EventFeedAgent> feedToAddList;
    private final OneToOneConcurrentArrayQueue<EventFeedAgent> feedToRemove;
    private final OneToOneConcurrentArrayQueue<EventFeedAgent> feeds;
    private final OneToOneConcurrentArrayQueue<DataFlow> dataFlowsToAdd;
    private final OneToOneConcurrentArrayQueue<DataFlow> dataFlowsToRemove;
    private final OneToOneConcurrentArrayQueue<DataFlow> dataFlows;

    public DataFlowAgent(String roleName) {
        super(roleName);
        feedToAddList = new OneToOneConcurrentArrayQueue<>(128);
        feedToRemove = new OneToOneConcurrentArrayQueue<>(128);
        feeds = new OneToOneConcurrentArrayQueue<>(128);
        dataFlowsToAdd = new OneToOneConcurrentArrayQueue<>(128);
        dataFlowsToRemove = new OneToOneConcurrentArrayQueue<>(128);
        dataFlows = new OneToOneConcurrentArrayQueue<>(128);
    }

    public void addFeed(EventFeedAgent feed) {
        do {
            tryAdd(feed);
        } while (!hasAddAgentCompleted());
        feedToAddList.add(feed);
    }

    public void removeFeed(EventFeedAgent feed) {
        do {
            tryRemove(feed);
        } while (hasRemoveAgentCompleted());
        feedToRemove.add(feed);
    }

    public void addDataFlow(DataFlow dataFlow) {
        dataFlowsToAdd.add(dataFlow);
    }

    public void removeDataFlow(DataFlow dataFlow) {
        dataFlowsToRemove.add(dataFlow);
    }

    @Override
    public int doWork() throws Exception {
        checkForRegistrationUpdates();
        return super.doWork();
    }

    @Override
    public void onClose() {
        checkForRegistrationUpdates();
        super.onClose();
    }

    private void checkForRegistrationUpdates() {
        feedToAddList.drain(feed -> {
            feeds.add(feed);
            dataFlows.forEach(dataFlow -> dataFlow.addEventFeed(feed));
        });

        feedToRemove.drain(feed -> {
            feeds.remove(feed);
            dataFlows.forEach(dataFlow -> dataFlow.removeEventFeed(feed));
        });

        dataFlowsToAdd.drain(dataFlow -> {
            dataFlows.add(dataFlow);
            feeds.forEach(feed -> feed.registerSubscriber(dataFlow));
        });

        dataFlowsToRemove.drain(dataFlow -> {
            dataFlows.remove(dataFlow);
            feeds.forEach(feed -> feed.removeAllSubscriptions(dataFlow));
        });
    }
}

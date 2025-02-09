/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.runtime.agentrunner;

import com.fluxtion.agrona.collections.ArrayUtil;
import com.fluxtion.agrona.concurrent.DynamicCompositeAgent;
import com.fluxtion.agrona.concurrent.OneToOneConcurrentArrayQueue;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.input.NamedFeed;
import lombok.extern.java.Log;

@Log
class DataFlowAgent extends DynamicCompositeAgent {

    private final OneToOneConcurrentArrayQueue<EventFeedAgent> feedToAddList;
    private final OneToOneConcurrentArrayQueue<EventFeedAgent> feedToRemove;
    private EventFeedAgent[] feeds;
    private final OneToOneConcurrentArrayQueue<DataFlow> dataFlowsToAdd;
    private final OneToOneConcurrentArrayQueue<DataFlow> dataFlowsToRemove;
    private DataFlow[] dataFlows;

    public DataFlowAgent(String roleName) {
        super(roleName);
        feedToAddList = new OneToOneConcurrentArrayQueue<>(128);
        feedToRemove = new OneToOneConcurrentArrayQueue<>(128);
        feeds = new EventFeedAgent[0];
        dataFlowsToAdd = new OneToOneConcurrentArrayQueue<>(128);
        dataFlowsToRemove = new OneToOneConcurrentArrayQueue<>(128);
        dataFlows = new DataFlow[0];
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
            feeds = ArrayUtil.add(feeds, feed);

            for (DataFlow dataFlow : dataFlows) {
                log.info(() -> "adding eventFeed " + feed + " to dataflow " + dataFlow);
                dataFlow.registerService(feed, NamedFeed.class);
            }
        });

        feedToRemove.drain(feed -> {
            feeds = ArrayUtil.remove(feeds, feed);

            for (DataFlow dataFlow : dataFlows) {
                log.info(() -> "removing eventFeed " + feed + " to dataflow " + dataFlow);
                dataFlow.removeEventFeed(feed);
            }
        });

        dataFlowsToAdd.drain(dataFlow -> {
            dataFlows = ArrayUtil.add(dataFlows, dataFlow);

            for (EventFeedAgent feed : feeds) {
                log.info(() -> "register subscriber " + dataFlow + " with feed " + feed);
                feed.registerSubscriber(dataFlow);
            }
        });

        dataFlowsToRemove.drain(dataFlow -> {
            dataFlows = ArrayUtil.remove(dataFlows, dataFlow);

            for (EventFeedAgent feed : feeds) {
                log.info(() -> "deregister subscriber " + dataFlow + " with feed " + feed);
                feed.removeAllSubscriptions(dataFlow);
            }
        });
    }
}

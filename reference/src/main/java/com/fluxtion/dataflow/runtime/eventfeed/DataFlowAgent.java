/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.runtime.eventfeed;

import com.fluxtion.agrona.collections.ArrayUtil;
import com.fluxtion.agrona.concurrent.DynamicCompositeAgent;
import com.fluxtion.agrona.concurrent.OneToOneConcurrentArrayQueue;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.annotations.feature.Experimental;
import com.fluxtion.dataflow.runtime.input.NamedFeed;
import lombok.extern.java.Log;


@Experimental
@Log
class DataFlowAgent extends DynamicCompositeAgent {

    private final OneToOneConcurrentArrayQueue<EventFeedAgent> feedToAddList;
    private final OneToOneConcurrentArrayQueue<EventFeedAgent> feedToRemoveList;
    private EventFeedAgent[] feeds;
    private final OneToOneConcurrentArrayQueue<DataFlow> dataFlowsToAdd;
    private final OneToOneConcurrentArrayQueue<DataFlow> dataFlowsToRemove;
    private DataFlow[] dataFlows;

    public DataFlowAgent(String roleName) {
        super(roleName);
        feedToAddList = new OneToOneConcurrentArrayQueue<>(128);
        feedToRemoveList = new OneToOneConcurrentArrayQueue<>(128);
        feeds = new EventFeedAgent[0];
        dataFlowsToAdd = new OneToOneConcurrentArrayQueue<>(128);
        dataFlowsToRemove = new OneToOneConcurrentArrayQueue<>(128);
        dataFlows = new DataFlow[0];
    }

    public void addFeed(EventFeedAgent feed) {
        feedToAddList.add(feed);
    }

    public void removeFeed(EventFeedAgent feed) {
        do {
            tryRemove(feed);
        } while (hasRemoveAgentCompleted());
        feedToRemoveList.add(feed);
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
        log.finest("Checking for registration updates");
        EventFeedAgent feedToAdd = feedToAddList.poll();
        if (feedToAdd != null) {
            tryAdd(feedToAdd);
            feeds = ArrayUtil.add(feeds, feedToAdd);

            for (DataFlow dataFlow : dataFlows) {
                log.info(() -> "adding eventFeed " + feedToAdd + " to dataflow " + dataFlow);
                dataFlow.registerService(feedToAdd, NamedFeed.class);
            }
        }

        EventFeedAgent feedToRemove = feedToRemoveList.poll();

        if (feedToRemove != null) {
            feeds = ArrayUtil.remove(feeds, feedToRemove);

            for (DataFlow dataFlow : dataFlows) {
                log.info(() -> "removing eventFeed " + feedToRemove + " to dataflow " + dataFlow);
                dataFlow.removeEventFeed(feedToRemove);
            }
        }

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

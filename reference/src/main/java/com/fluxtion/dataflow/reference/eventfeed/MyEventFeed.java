/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.eventfeed;


import com.fluxtion.agrona.collections.ArrayUtil;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.agentrunner.EventFeedAgent;
import com.fluxtion.dataflow.runtime.event.NamedFeedEventImpl;
import com.fluxtion.dataflow.runtime.node.EventSubscription;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MyEventFeed implements EventFeedAgent {

    private final String roleName;
    private ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<>();
    private DataFlow[] dataFlows = new DataFlow[0];
    private final NamedFeedEventImpl<String> namedFeedEvent;

    public MyEventFeed(String roleName) {
        this.roleName = roleName;
        namedFeedEvent = new NamedFeedEventImpl<>(roleName);
    }

    public void publish(String message) {
        messages.add(message);
    }

    @Override
    public void registerSubscriber(DataFlow dataFlow) {
        System.out.println("registerSubscriber " + dataFlow);
    }

    @Override
    public void subscribe(DataFlow dataFlow, EventSubscription<?> eventSubscription) {
        System.out.println(roleName() + " subscription request " + dataFlow + "/" + eventSubscription);
        dataFlows = ArrayUtil.add(dataFlows, dataFlow);
    }

    @Override
    public void unSubscribe(DataFlow dataFlow, EventSubscription<?> eventSubscription) {
        System.out.println(roleName() + " unSubscribe request " + dataFlow + "/" + eventSubscription);
        dataFlows = ArrayUtil.remove(dataFlows, dataFlow);
    }

    @Override
    public void removeAllSubscriptions(DataFlow dataFlow) {
        System.out.println("removeAllSubscriptions " + dataFlow);
        dataFlows = ArrayUtil.remove(dataFlows, dataFlow);
    }

    @Override
    public int doWork() throws Exception {
        String message = messages.poll();
        int count = 0;
        while (message != null) {
            System.out.println("publish " + message);
            for (DataFlow dataFlow : dataFlows) {
                namedFeedEvent.data(message);
                dataFlow.onEvent(namedFeedEvent);
            }
            message = messages.poll();
            count = 1;
        }
        return count;
    }

    @Override
    public String roleName() {
        return roleName;
    }
}

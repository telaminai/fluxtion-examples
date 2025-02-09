/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.eventfeed;

import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.agentrunner.EventFeedAgent;
import com.fluxtion.dataflow.runtime.node.EventSubscription;

public class MyEventFeed implements EventFeedAgent {

    private final String roleName;

    public MyEventFeed(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public void registerSubscriber(DataFlow dataFlow) {

    }

    @Override
    public void subscribe(DataFlow dataFlow, EventSubscription<?> eventSubscription) {

    }

    @Override
    public void unSubscribe(DataFlow dataFlow, EventSubscription<?> eventSubscription) {

    }

    @Override
    public void removeAllSubscriptions(DataFlow dataFlow) {

    }

    @Override
    public int doWork() throws Exception {
        System.out.println("MyEventFeed: doWork");
        return 0;
    }

    @Override
    public String roleName() {
        return roleName;
    }
}

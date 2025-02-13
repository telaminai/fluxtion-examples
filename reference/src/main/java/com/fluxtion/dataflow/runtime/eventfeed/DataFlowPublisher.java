/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.runtime.eventfeed;

import com.fluxtion.agrona.collections.ArrayUtil;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.event.NamedFeedEvent;
import com.fluxtion.dataflow.runtime.event.NamedFeedEventImpl;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;

@Log4j2
public class DataFlowPublisher<T> {

    private DataFlow[] dataFlows = new DataFlow[0];
    private final String feedName;

    public DataFlowPublisher(String feedName) {
        this.feedName = feedName;
    }

    public void addDataFlowReceiver(DataFlow dataFlow) {
        for (DataFlow dataFlow1 : dataFlows) {
            if (dataFlow1.equals(dataFlow)) {
                return;
            }
        }
        dataFlows = ArrayUtil.add(dataFlows, dataFlow);
    }

    public void removeDataFlowReceiver(DataFlow dataFlow) {
        dataFlows = ArrayUtil.remove(dataFlows, dataFlow);
    }

    public void dispatchCachedEventLog() {
        log.info("Dispatching cached event log");
        for (DataFlow dataFlow : dataFlows) {

        }
    }

    public List<NamedFeedEvent> getEventLog() {
        return Collections.emptyList();
    }

    public void setCacheEventLog(boolean cacheEventLog) {

    }

    public void publish(T event) {
        NamedFeedEventImpl<T> namedEvent = new NamedFeedEventImpl<>(feedName, event);
        log.info("Publishing event " + namedEvent);
        for (DataFlow dataFlow : dataFlows) {
            log.info("Publishing event " + namedEvent + " to dataflow " + dataFlow);
            dataFlow.onEvent(namedEvent);
        }
    }

    public void cache(T event) {
        NamedFeedEventImpl<T> namedEvent = new NamedFeedEventImpl<>(feedName, event);
        log.info("Caching event " + namedEvent);
        for (DataFlow dataFlow : dataFlows) {
            log.info("Publishing cache event " + namedEvent + " to dataflow " + dataFlow);
            dataFlow.onEvent(namedEvent);
        }
    }
}

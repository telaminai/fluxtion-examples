/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.filtering;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.event.Signal;

public class StaticFiltering {

    public static class MyNode {
        @OnEventHandler(filterString = "CLEAR_SIGNAL")
        public boolean allClear(Signal<String> signalToProcess) {
            System.out.println("allClear [" + signalToProcess + "]");
            return true;
        }

        @OnEventHandler(filterString = "ALERT_SIGNAL")
        public boolean alertSignal(Signal<String> signalToProcess) {
            System.out.println("alertSignal [" + signalToProcess + "]");
            return true;
        }

        @OnEventHandler()
        public boolean anySignal(Signal<String> signalToProcess) {
            System.out.println("anySignal [" + signalToProcess + "]");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new MyNode())
                .build();

        processor.onEvent(new Signal<>("ALERT_SIGNAL", "power failure"));
        System.out.println();
        processor.onEvent(new Signal<>("CLEAR_SIGNAL", "power restored"));
        System.out.println();
        processor.onEvent(new Signal<>("HEARTBEAT_SIGNAL", "heartbeat message"));
    }
}
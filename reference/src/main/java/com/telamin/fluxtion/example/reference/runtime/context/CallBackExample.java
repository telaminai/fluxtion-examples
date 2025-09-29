/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.runtime.context;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.annotations.OnTrigger;
import com.telamin.fluxtion.runtime.annotations.builder.Inject;
import com.telamin.fluxtion.runtime.callback.EventDispatcher;
import lombok.Data;

public class CallBackExample {
    public static class MyCallbackNode {

        @Inject
        public EventDispatcher eventDispatcher;

        @OnEventHandler
        public boolean processString(String event) {
            System.out.println("MyCallbackNode::processString - " + event);
            for (String item : event.split(",")) {
                eventDispatcher.processAsNewEventCycle(Integer.parseInt(item));
            }
            return true;
        }

        @OnEventHandler
        public boolean processInteger(Integer event) {
            System.out.println("MyCallbackNode::processInteger - " + event);
            return false;
        }

    }

    @Data
    public static class IntegerHandler {

        private final MyCallbackNode myCallbackNode;

        @OnEventHandler
        public boolean processInteger(Integer event) {
            System.out.println("IntegerHandler::processInteger - " + event + "\n");
            return true;
        }

        @OnTrigger
        public boolean triggered() {
            System.out.println("IntegerHandler::triggered\n");
            return false;
        }

    }

    public static void main(String[] args) {
        MyCallbackNode myCallbackNode = new MyCallbackNode();
        IntegerHandler intHandler = new IntegerHandler(myCallbackNode);

        var processor = DataFlowBuilder
                .subscribeToNode(intHandler)
                .build();

        processor.onEvent("20,45,89");
    }
}
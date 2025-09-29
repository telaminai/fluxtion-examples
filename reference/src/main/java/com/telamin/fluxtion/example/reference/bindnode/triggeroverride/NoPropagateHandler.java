/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.triggeroverride;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.annotations.OnTrigger;

public class NoPropagateHandler {
    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::handleStringEvent received:" + stringToProcess);
            return true;
        }

        @OnEventHandler(propagate = false)
        public boolean handleIntEvent(int intToProcess) {
            System.out.println("MyNode::handleIntEvent received:" + intToProcess);
            return true;
        }
    }

    public static class Child {
        private final MyNode myNode;

        public Child(MyNode myNode) {
            this.myNode = myNode;
        }

        @OnTrigger
        public boolean triggered(){
            System.out.println("Child:triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new Child(new MyNode()))
                .build();

        processor.onEvent("test");
        System.out.println();
        processor.onEvent(200);
    }
}
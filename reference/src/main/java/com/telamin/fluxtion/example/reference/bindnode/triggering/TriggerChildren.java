/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.triggering;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.annotations.OnTrigger;

public class TriggerChildren {
    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("received:" + stringToProcess);
            return true;
        }
    }

    public static class MyNode2 {
        @OnEventHandler
        public boolean handleStringEvent(int intToProcess) {
            System.out.println("received:" + intToProcess);
            return true;
        }
    }

    public static class Child {
        private final MyNode myNode;
        private final MyNode2 myNode2;

        public Child(MyNode myNode, MyNode2 myNode2) {
            this.myNode = myNode;
            this.myNode2 = myNode2;
        }

        @OnTrigger
        public boolean triggered() {
            System.out.println("Child:triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new Child(new MyNode(), new MyNode2()))
                .build();

        processor.onEvent("test");
        System.out.println();
        processor.onEvent(200);
    }
}
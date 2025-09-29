/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.triggeroverride;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.annotations.OnTrigger;

public class InvertDirtyTrigger {
    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(int intToProcess) {
            boolean propagate = intToProcess > 100;
            System.out.println("conditional propagate:" + propagate);
            return propagate;
        }
    }


    public static class Child {
        private final MyNode myNode;

        public Child(MyNode myNode) {
            this.myNode = myNode;
        }

        @OnTrigger
        public boolean triggered() {
            System.out.println("Child:triggered");
            return true;
        }
    }

    public static class NonDirtyChild {
        private final MyNode myNode;

        public NonDirtyChild(MyNode myNode) {
            this.myNode = myNode;
        }

        @OnTrigger(dirty = false)
        public boolean triggered() {
            System.out.println("NonDirtyChild:triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        MyNode myNode = new MyNode();
        DataFlowBuilder.subscribeToNode(new Child(myNode)).build();
        var processor = DataFlowBuilder
                .subscribeToNode( new NonDirtyChild(myNode))
                .build();

        processor.onEvent("test");
        System.out.println();
        processor.onEvent(200);
        System.out.println();
        processor.onEvent(50);
    }
}
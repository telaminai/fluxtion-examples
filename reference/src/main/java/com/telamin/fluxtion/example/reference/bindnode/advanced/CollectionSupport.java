/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.advanced;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.FilterId;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.annotations.OnParentUpdate;
import com.telamin.fluxtion.runtime.annotations.OnTrigger;
import com.telamin.fluxtion.runtime.event.Signal;

public class CollectionSupport {
    public static class MyNode {
        @FilterId
        private final String filter;
        private final String name;

        public MyNode(String filter, String name) {
            this.filter = filter;
            this.name = name;
        }

        @OnEventHandler
        public boolean handleIntSignal(Signal.IntSignal intSignal) {
            System.out.printf("MyNode-%s::handleIntSignal - %s%n", filter, intSignal.getValue());
            return true;
        }
    }

    public static class Child {
        private final MyNode[] nodes;
        private int updateCount;

        public Child(MyNode... nodes) {
            this.nodes = nodes;
        }

        @OnParentUpdate
        public void parentUpdated(MyNode updatedNode) {
            updateCount++;
            System.out.printf("parentUpdated '%s'%n", updatedNode.name);
        }

        @OnTrigger
        public boolean triggered() {
            System.out.printf("Child::triggered updateCount:%d%n%n", updateCount);
            updateCount = 0;
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder.subscribeToNode(new Child(
                new MyNode("A", "a_1"),
                new MyNode("A", "a_2"),
                new MyNode("B", "b_1")))
                .build();

        processor.publishIntSignal("A", 10);
        processor.publishIntSignal("B", 25);
        processor.publishIntSignal("A", 12);
        processor.publishIntSignal("C", 200);
    }
}
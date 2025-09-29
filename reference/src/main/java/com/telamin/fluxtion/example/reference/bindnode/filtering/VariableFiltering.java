/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.filtering;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.event.Signal;

public class VariableFiltering {
    public static class MyNode {
        private final String name;

        public MyNode(String name) {
            this.name = name;
        }


        @OnEventHandler(filterVariable = "name")
        public boolean handleIntSignal(Signal.IntSignal intSignal) {
            System.out.printf("MyNode-%s::handleIntSignal - %s%n", name, intSignal.getValue());
            return true;
        }
    }

    public static void main(String[] args) {
        DataFlowBuilder.subscribeToNode(new MyNode("A"));
        var processor = DataFlowBuilder
                .subscribeToNode(new MyNode("B"))
                .build();

        processor.publishIntSignal("A", 22);
        processor.publishIntSignal("B", 45);
        processor.publishIntSignal("C", 100);
    }
}
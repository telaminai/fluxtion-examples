/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.subscribe;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;

public class MultipleEventTypes {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("String received:" + stringToProcess);
            return true;
        }

        @OnEventHandler
        public boolean handleIntEvent(int intToProcess) {
            System.out.println("Int received:" + intToProcess);
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new MyNode())
                .build();

        processor.onEvent("TEST");
        processor.onEvent(16);
    }
}
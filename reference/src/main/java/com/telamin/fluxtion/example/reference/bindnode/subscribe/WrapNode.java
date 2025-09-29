/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.subscribe;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;

public class WrapNode {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("received:" + stringToProcess);
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new MyNode())
                .build();

        processor.onEvent("TEST");
    }
}

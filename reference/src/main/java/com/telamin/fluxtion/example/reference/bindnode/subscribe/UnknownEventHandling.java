/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.subscribe;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;

import java.util.Collections;

public class UnknownEventHandling {

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new MyNode())
                .build();

        //set an unknown event handler
        processor.setUnKnownEventHandler(e -> System.out.println("Unregistered event type -> " + e.getClass().getName()));
        processor.onEvent("TEST");

        //handled by unKnownEventHandler
        processor.onEvent(Collections.emptyList());
    }

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("received:" + stringToProcess);
            return true;
        }
    }
}

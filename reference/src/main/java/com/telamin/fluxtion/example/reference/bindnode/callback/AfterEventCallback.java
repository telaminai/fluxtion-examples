/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.callback;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.AfterEvent;
import com.telamin.fluxtion.runtime.annotations.Initialise;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;

public class AfterEventCallback {
    public static class MyNode {
        @Initialise
        public void init(){
            System.out.println("MyNode::init");
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::handleStringEvent received:" + stringToProcess);
            return true;
        }

        @AfterEvent
        public void afterEvent(){
            System.out.println("MyNode::afterEvent");
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new MyNode())
                .build();

        System.out.println();
        processor.onEvent("TEST");
        System.out.println();
        processor.onEvent(23);
    }
}
/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.triggering;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.annotations.OnTrigger;
import com.telamin.fluxtion.runtime.annotations.PushReference;

public class PushTrigger {
    public static class MyNode {
        @PushReference
        private final PushTarget pushTarget;

        public MyNode(PushTarget pushTarget) {
            this.pushTarget = pushTarget;
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::handleStringEvent " + stringToProcess);
            if (stringToProcess.startsWith("PUSH")) {
                pushTarget.myValue = stringToProcess;
                return true;
            }
            return false;
        }
    }

    public static class PushTarget {
        public String myValue;

        @OnTrigger
        public boolean onTrigger() {
            System.out.println("PushTarget::onTrigger -> myValue:'" + myValue + "'");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new MyNode(new PushTarget()))
                .build();

        processor.onEvent("PUSH - test 1");
        System.out.println();
        processor.onEvent("ignore me - XXXXX");
        System.out.println();
        processor.onEvent("PUSH - test 2");
    }
}
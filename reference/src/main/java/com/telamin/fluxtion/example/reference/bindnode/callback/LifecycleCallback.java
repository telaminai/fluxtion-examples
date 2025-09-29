/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.callback;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.Initialise;
import com.telamin.fluxtion.runtime.annotations.Start;
import com.telamin.fluxtion.runtime.annotations.Stop;
import com.telamin.fluxtion.runtime.annotations.TearDown;

public class LifecycleCallback {
    public static class MyNode {

        @Initialise
        public void myInitMethod() {
            System.out.println("Initialise");
        }

        @Start
        public void myStartMethod() {
            System.out.println("Start");
        }

        @Stop
        public void myStopMethod() {
            System.out.println("Stop");
        }

        @TearDown
        public void myTearDownMethod() {
            System.out.println("TearDown");
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new MyNode())
                .build();

        //init is implicitly called
        processor.start();
        processor.stop();
        processor.tearDown();
    }
}

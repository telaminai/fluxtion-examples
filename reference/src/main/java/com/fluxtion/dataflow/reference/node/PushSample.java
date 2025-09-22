/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.node;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;

public class PushSample {
    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribe(String.class)
                .push(new MyPushTarget()::updated)
                .build();

        processor.onEvent("AAA");
        processor.onEvent("BBB");
    }

    public static class MyPushTarget {
        public void updated(String in) {
            System.out.println("received push: " + in);
        }
    }
}

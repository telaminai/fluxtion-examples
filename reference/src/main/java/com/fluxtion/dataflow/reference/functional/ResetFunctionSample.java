/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.flowfunction.Stateful;

public class ResetFunctionSample {

    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribe(String.class)
                .map(new MyResetSum()::increment)
                .resetTrigger(DataFlowBuilder.subscribeToSignal("resetMe"))
                .console("count:{}")
                .build();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");

        processor.publishSignal("resetMe");
        processor.onEvent("E");
        processor.onEvent("F");
    }

    public static class MyResetSum implements Stateful<Integer> {
        public int count = 0;

        public int increment(Object o) {
            return ++count;
        }

        @Override
        public Integer reset() {
            System.out.println("--- RESET CALLED ---");
            count = 0;
            return count;
        }
    }
}
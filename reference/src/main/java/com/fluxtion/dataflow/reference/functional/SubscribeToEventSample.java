/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.runtime.DataFlow;

public class SubscribeToEventSample {
    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribe(String.class)
                .console("string in {}")
                .build();

        processor.onEvent("AAA");
        processor.onEvent("BBB");
    }
}

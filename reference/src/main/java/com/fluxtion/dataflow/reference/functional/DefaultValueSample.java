/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;


import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;

public class DefaultValueSample {
    public static void main(String[] args) {
        var strings = DataFlowBuilder.subscribe(String.class).defaultValue("200");
        var ints = DataFlowBuilder.subscribe(Integer.class);
        DataFlow processor = DataFlowBuilder.mapBiFunction((a, b) -> Integer.parseInt(a) + b, strings, ints)
                .console("biMap with default value ans: {}")
                .build();
        processor.onEvent(55);
    }
}

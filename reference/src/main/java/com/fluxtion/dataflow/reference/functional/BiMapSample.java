/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;

import com.fluxtion.dataflow.builder.DataFlowBuilder;

public class BiMapSample {
    public static void main(String[] args) {
        var strings = DataFlowBuilder.subscribe(String.class);
        var ints = DataFlowBuilder.subscribe(Integer.class);
        var processor = DataFlowBuilder.mapBiFunction((a, b) -> Integer.parseInt(a) + b, strings, ints)
                .console("biMap ans: {}")
                .build();

        processor.onEvent("500");
        processor.onEvent(55);
    }
}

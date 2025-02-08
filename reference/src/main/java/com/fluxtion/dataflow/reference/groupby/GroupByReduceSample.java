/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.groupby;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.runtime.flowfunction.helpers.Aggregates;

public class GroupByReduceSample {

    public static void main(String[] args) {
        var processor = DataFlowBuilder.subscribe(Integer.class)
                .groupBy(i -> i % 2 == 0 ? "evens" : "odds", Aggregates.intSumFactory())
                .console("ODD/EVEN sum:{}")
                .reduceValues(Aggregates.intSumFactory())
                .console("REDUCED sum:{}\n")
                .build();

        processor.onEvent(1);
        processor.onEvent(2);
        processor.onEvent(5);
        processor.onEvent(7);
        processor.onEvent(2);
    }
}
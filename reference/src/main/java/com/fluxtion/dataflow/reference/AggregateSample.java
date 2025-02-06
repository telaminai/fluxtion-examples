/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 *
 */

//REPOS repsy-fluxtion=https://repo.repsy.io/mvn/fluxtion/fluxtion-public
//DEPS com.fluxtion.dataflow:dataflow-builder:1.0.0
//COMPILE_OPTIONS -proc:full

package com.fluxtion.dataflow.reference;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.runtime.flowfunction.helpers.Collectors;

public class AggregateSample {

    public record ResetList() { }

    public static void main(String[] args) {
        var resetSignal = DataFlowBuilder.subscribe(ResetList.class).console("\n--- RESET ---");

        var processor = DataFlowBuilder
                .subscribe(String.class)
                .aggregate(Collectors.listFactory(3))
                .resetTrigger(resetSignal).
                console("ROLLING list: {}").build();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");
        processor.onEvent("E");

        processor.onEvent(new ResetList());
        processor.onEvent("P");
        processor.onEvent("Q");
        processor.onEvent("R");

        processor.onEvent(new ResetList());
        processor.onEvent("XX");
        processor.onEvent("YY");
    }
}

/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.flowfunction.helpers.Mappers;

public class MergeSample {
    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.merge(
                        DataFlowBuilder.subscribe(Long.class).console("long : {}"),
                        DataFlowBuilder.subscribe(String.class).console("string : {}").map(Mappers::parseLong),
                        DataFlowBuilder.subscribe(Integer.class).console("int : {}").map(Integer::longValue))
                .console("MERGED FLOW -> {}")
                .build();

        processor.onEvent(1234567890835L);
        processor.onEvent("9994567890835");
        processor.onEvent(123);
    }
}

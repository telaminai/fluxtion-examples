/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.runtime.DataFlow;

import java.util.Arrays;

public class FlatMapSample {

    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribe(String.class)
                .console("\ncsv in [{}]")
                .flatMap(s -> Arrays.asList(s.split(",")))
                .console("flattened item [{}]")
                .build();

        processor.onEvent("A,B,C");
        processor.onEvent("2,3,5,7,11");
    }
}

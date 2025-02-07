/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.runtime.DataFlow;

public class FilterSample {
    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribe(Integer.class)
                .filter(i -> i > 10)
                .console("int {} > 10 ")
                .build();

        processor.onEvent(1);
        processor.onEvent(17);
        processor.onEvent(4);
    }
}

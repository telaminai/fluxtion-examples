/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;

import com.fluxtion.dataflow.builder.DataFlowBuilder;

public class ReEntrantEventSample {
    
    public static void main(String[] args) {
        DataFlowBuilder.subscribeToIntSignal("myIntSignal")
                .mapToObj(d -> "intValue:" + d)
                .console("republish re-entrant [{}]")
                .processAsNewGraphEvent();

        var processor = DataFlowBuilder.subscribe(String.class)
                .console("received [{}]")
                .build();

        processor.publishSignal("myIntSignal", 256);
    }
}
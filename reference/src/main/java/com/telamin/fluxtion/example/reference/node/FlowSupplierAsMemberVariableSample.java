/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.node;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.annotations.OnTrigger;
import com.telamin.fluxtion.runtime.flowfunction.FlowSupplier;

public class FlowSupplierAsMemberVariableSample {

    public static void main(String[] args) {
        FlowSupplier<String> stringFlow = DataFlowBuilder.subscribe(String.class).flowSupplier();
        DataFlow processor = DataFlowBuilder
                .subscribeToNode(new MyFlowHolder(stringFlow))
                .build();

        processor.onEvent("test");
    }

    public record MyFlowHolder(FlowSupplier<String> flowSupplier) {
        @OnTrigger
        public boolean onTrigger() {
            //FlowSupplier is used at runtime to access the current value of the data flow
            System.out.println("triggered by data flow -> " + flowSupplier.get().toUpperCase());
            return true;
        }
    }
}

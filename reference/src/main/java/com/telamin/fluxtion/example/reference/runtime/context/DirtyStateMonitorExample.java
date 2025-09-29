/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.runtime.context;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.annotations.OnTrigger;
import com.telamin.fluxtion.runtime.annotations.builder.Inject;
import com.telamin.fluxtion.runtime.callback.DirtyStateMonitor;
import com.telamin.fluxtion.runtime.flowfunction.FlowSupplier;
import com.telamin.fluxtion.runtime.node.NamedNode;

public class DirtyStateMonitorExample {
    public static class TriggeredChild implements NamedNode {
        @Inject
        public DirtyStateMonitor dirtyStateMonitor;
        private final FlowSupplier<Integer> intDataFlow;

        public TriggeredChild(FlowSupplier<Integer> intDataFlow) {
            this.intDataFlow = intDataFlow;
        }

        @OnTrigger
        public boolean triggeredChild() {
            System.out.println("TriggeredChild -> " + intDataFlow.get());
            return true;
        }

        public void printDirtyStat() {
            System.out.println("\nintDataFlow dirtyState:" + dirtyStateMonitor.isDirty(intDataFlow));
        }

        public void markDirty() {
            dirtyStateMonitor.markDirty(intDataFlow);
            System.out.println("\nmark dirty intDataFlow dirtyState:" + dirtyStateMonitor.isDirty(intDataFlow));
        }

        @Override
        public String getName() {
            return "triggeredChild";
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        var intFlow = DataFlowBuilder.subscribe(Integer.class).flowSupplier();

        var processor = DataFlowBuilder
                .subscribeToNode(new TriggeredChild(intFlow))
                .build();

        TriggeredChild triggeredChild = processor.getNodeById("triggeredChild");

        processor.onEvent(2);
        processor.onEvent(4);

        //NOTHING HAPPENS
        triggeredChild.printDirtyStat();
        processor.triggerCalculation();

        //MARK DIRTY
        triggeredChild.markDirty();
        processor.triggerCalculation();
    }
}

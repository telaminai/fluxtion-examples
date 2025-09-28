/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.node;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;

public class MapNodeSupplierSample {
    public static void main(String[] args) {
        MyPushTarget myPushTarget = new MyPushTarget();
        DataFlow processor = DataFlowBuilder.subscribe(String.class)
                .push(myPushTarget::updated)
                .mapFromSupplier(myPushTarget::received)
                .console("Received - [{}]")
                .build();

        processor.onEvent("AAA");
        processor.onEvent("BBB");
    }

    public static class MyPushTarget {
        private String store = " ";
        public void updated(String in) {
            store += "'" + in + "' ";
        }

        public String received() {
            return store;
        }
    }
}

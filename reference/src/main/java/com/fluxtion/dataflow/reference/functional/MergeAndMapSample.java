/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;


import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.builder.flowfunction.MergeAndMapFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import lombok.Data;

import java.util.Date;

public class MergeAndMapSample {
    public static void main(String[] args) {
        DataFlow processor = MergeAndMapFlowBuilder.of(MyData::new)
                .required(DataFlowBuilder.subscribe(String.class), MyData::setCustomer)
                .required(DataFlowBuilder.subscribe(Date.class), MyData::setDate)
                .required(DataFlowBuilder.subscribe(Integer.class), MyData::setId)
                .dataFlow()
                .console("new customer : {}")
                .build();

        processor.onEvent(new Date());
        processor.onEvent("John Doe");
        //only publishes when the last required flow is received
        processor.onEvent(123);
    }

    @Data
    public static class MyData {
        private String customer;
        private Date date;
        private int id;
    }
}

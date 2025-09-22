/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.functional;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;

import java.util.function.Consumer;

public class SinkExample {

    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribeToIntSignal("myIntSignal")
                .mapToObj(d -> "intValue:" + d)
                .sink("mySink")//CREATE A SINK IN THE PROCESSOR
                .build();

        //ADDING SINK CONSUMER
        processor.addSink("mySink", (Consumer<String>) System.out::println);

        processor.publishSignal("myIntSignal", 10);
        processor.publishSignal("myIntSignal", 256);

        //REMOVING SINK CONSUMER
        processor.removeSink("mySink");
        processor.publishSignal("myIntSignal", 512);
    }
}
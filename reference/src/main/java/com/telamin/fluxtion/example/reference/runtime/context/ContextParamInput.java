/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.runtime.context;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.annotations.Start;
import com.telamin.fluxtion.runtime.annotations.builder.Inject;
import com.telamin.fluxtion.runtime.context.DataFlowContext;

public class ContextParamInput {

    public static class ContextParamReader {
        @Inject
        public DataFlowContext context;

        @Start
        public void start() {
            System.out.println("myContextParam1 -> " + context.getContextProperty("myContextParam1"));
            System.out.println("myContextParam2 -> " + context.getContextProperty("myContextParam2"));
            System.out.println();
        }

        @OnEventHandler
        public boolean handleEvent(String eventToProcess) {
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new ContextParamReader())
                .build();

        processor.addContextParameter("myContextParam1", "[param1: update 1]");
        processor.start();

        processor.addContextParameter("myContextParam1", "[param1: update 2]");
        processor.addContextParameter("myContextParam2", "[param2: update 1]");
        processor.start();
    }
}

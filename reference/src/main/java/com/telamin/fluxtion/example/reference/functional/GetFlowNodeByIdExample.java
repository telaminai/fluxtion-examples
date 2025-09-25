/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.functional;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.flowfunction.helpers.Mappers;

public class GetFlowNodeByIdExample {
    public static void main(String[] args) throws NoSuchFieldException {
        DataFlow processor = DataFlowBuilder.subscribe(String.class)
                .filter(s -> s.equalsIgnoreCase("monday"))
                //ID START - this makes the wrapped value accessible via the id
                .mapToInt(Mappers.count()).id("MondayChecker")
                //ID END
                .console("Monday is triggered")
                .build();

        processor.onEvent("Monday");
        processor.onEvent("Tuesday");
        processor.onEvent("Wednesday");

        //ACCESS THE WRAPPED VALUE BY ITS ID
        Integer mondayCheckerCount = processor.getStreamed("MondayChecker");
        System.out.println("Monday count:" + mondayCheckerCount + "\n");

        //ACCESS THE WRAPPED VALUE BY ITS ID
        processor.onEvent("Monday");
        mondayCheckerCount = processor.getStreamed("MondayChecker");
        System.out.println("Monday count:" + mondayCheckerCount);
    }
}

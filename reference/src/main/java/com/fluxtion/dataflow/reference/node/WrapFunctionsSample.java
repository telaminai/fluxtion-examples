/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.node;


import com.fluxtion.dataflow.reference.MyFunctions;
import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;

public class WrapFunctionsSample {

    public static void main(String[] args) {
        //STATEFUL FUNCTIONS
        MyFunctions myFunctions = new MyFunctions();

        var stringFlow = DataFlowBuilder.subscribe(String.class).console("input: '{}'");

        var charCount = stringFlow
                .map(myFunctions::totalCharCount)
                .console("charCountAggregate: {}");

        var upperCharCount = stringFlow
                .map(myFunctions::totalUpperCaseCharCount)
                .console("upperCharCountAggregate: {}");

        DataFlowBuilder.mapBiFunction(new MyFunctions.SimpleMath()::updatePercentage, upperCharCount, charCount)
                .console("percentage chars upperCase all words:{}");

        //STATELESS FUNCTION
        DataFlow processor = DataFlowBuilder
                .mapBiFunction(MyFunctions::wordUpperCasePercentage,
                        stringFlow.map(MyFunctions::upperCaseCharCount).console("charCourWord:{}"),
                        stringFlow.map(MyFunctions::charCount).console("upperCharCountWord:{}"))
                .console("percentage chars upperCase this word:{}\n")
                .build();

        processor.onEvent("test ME");
        processor.onEvent("and AGAIN");
        processor.onEvent("ALL CAPS");
    }
}

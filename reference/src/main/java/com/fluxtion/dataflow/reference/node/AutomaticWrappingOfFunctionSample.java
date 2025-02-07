/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.node;


import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.reference.MyFunctions;
import com.fluxtion.dataflow.reference.MyFunctions.SimpleMath;
import com.fluxtion.dataflow.runtime.DataFlow;

public class AutomaticWrappingOfFunctionSample {

    public static void main(String[] args) {
        //STATEFUL FUNCTIONS
        MyFunctions myFunctions = new MyFunctions();
        SimpleMath simpleMath = new SimpleMath();

        //BUILD THE GRAPH WITH DSL
        var stringFlow = DataFlowBuilder.subscribe(String.class).console("\ninput: '{}'");

        var charCount = stringFlow.map(myFunctions::totalCharCount)
                .console("charCount: {}");

        var upperCharCount = stringFlow.map(myFunctions::totalUpperCaseCharCount)
                .console("upperCharCount: {}");

        DataFlowBuilder.mapBiFunction(simpleMath::updatePercentage, upperCharCount, charCount)
                .console("percentage chars upperCase all words:{}");

        //STATELESS FUNCTION
        DataFlow processor = DataFlowBuilder.mapBiFunction(MyFunctions::wordUpperCasePercentage, upperCharCount, charCount)
                .console("percentage chars upperCase this word:{}")
                .build();
        processor.onEvent("test ME");
        processor.onEvent("and AGAIN");
    }

    private static void basicMap() {
        var stringFlow = DataFlowBuilder.subscribe(String.class);
        stringFlow.map(String::toLowerCase);
        stringFlow.mapToInt(s -> s.length() / 2);
    }
}

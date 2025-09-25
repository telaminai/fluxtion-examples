/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

//DEPS com.telamin.fluxtion:fluxtion-builder:0.9.5
//JAVA 25

package com.telamin.fluxtion.example;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;

public class HelloFluxtion {
    public static void main(String[] args) {
        DataFlow dataFlow = DataFlowBuilder
                .subscribe(String.class)           // accept String events
                .map(String::toUpperCase)          // transform
                .console("msg:{}")
                .build();// print to console

        dataFlow.onEvent("hello");  // prints: msg:HELLO
        dataFlow.onEvent("world");  // prints: msg:WORLD
    }
}
/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

//DEPS com.telamin.fluxtion:fluxtion-builder:0.9.6
//JAVA 21
//JAVA_OPTIONS --add-opens java.base/jdk.internal.misc=ALL-UNNAMED
package com.telamin.fluxtion.example.tutorial;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.connector.DataFlowConnector;
import com.telamin.fluxtion.runtime.connector.FileEventFeed;
import com.telamin.fluxtion.runtime.connector.FileMessageSink;
import com.telamin.fluxtion.runtime.eventfeed.ReadStrategy;

public class TutorialPart5 {
    public static void main(String[] args) throws InterruptedException {

        FileEventFeed myFileFeed = new FileEventFeed(
                "./tutorial4-input.txt",
                "myFeed",
                ReadStrategy.EARLIEST);

        DataFlow dataFlow = DataFlowBuilder.subscribeToFeed("myFeed", String.class)
                .console("read file in:{}")
                .map(String::toUpperCase)
                .console("write file out:{}\n")
                .sink("output")
                .build();

        FileMessageSink outputFile = new FileMessageSink("./tutorial4-output.txt");

        DataFlowConnector runner = new DataFlowConnector();
        runner.addDataFlow(dataFlow);
        runner.addFeed(myFileFeed);
        runner.addSink("output", outputFile);

        runner.start();
    }
}
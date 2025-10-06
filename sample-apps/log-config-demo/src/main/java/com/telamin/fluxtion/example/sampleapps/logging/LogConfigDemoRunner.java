/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.sampleapps.logging;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.connector.DataFlowConnector;
import com.telamin.fluxtion.runtime.connector.FileEventFeed;
import com.telamin.fluxtion.runtime.eventfeed.ReadStrategy;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

@Log4j2
public class LogConfigDemoRunner {
    public static void main(String[] args) throws InterruptedException {
        // Route JUL (java.util.logging) through Log4j2
        // Uncomment the following line to use Log4j2 always
        //System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

        FileEventFeed myFileFeed = new FileEventFeed(
                "./data/input.txt",
                "myFeed",
                ReadStrategy.EARLIEST);

        DataFlow dataFlow = DataFlowBuilder.subscribeToFeed("myFeed", String.class)
                .peek(s -> log.info("flow in:{}", s))
                .build();

        DataFlowConnector runner = new DataFlowConnector();
        runner.addDataFlow(dataFlow);
        runner.addFeed(myFileFeed);

        runner.start();

        TimeUnit.SECONDS.sleep(2);

        log.info("Shutting down manually...");
        runner.stop();
    }
}

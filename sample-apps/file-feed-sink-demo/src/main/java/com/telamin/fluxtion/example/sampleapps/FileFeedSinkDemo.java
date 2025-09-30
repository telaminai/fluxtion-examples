/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.telamin.fluxtion.example.sampleapps;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.connector.DataFlowConnector;
import com.telamin.fluxtion.runtime.connector.FileEventFeed;
import com.telamin.fluxtion.runtime.connector.FileMessageSink;
import com.telamin.fluxtion.runtime.eventfeed.ReadStrategy;

/**
 * Simple end-to-end example wiring a FileEventFeed (source) to a DataFlow and out to a FileMessageSink (sink).
 *
 * Input lines are read from data/input.txt, transformed to upper-case and written to data/output.txt.
 */
public class FileFeedSinkDemo {
    public static void main(String[] args) throws Exception {
        String inputPath = args.length > 0 ? args[0] : "./data/input.txt";
        String outputPath = args.length > 1 ? args[1] : "./data/output.txt";

        // Create file feed that replays from the start of the file
        FileEventFeed inputFileFeed = new FileEventFeed(
                inputPath,
                "fileFeed",
                ReadStrategy.EARLIEST
        );

        // Build a simple dataflow
        DataFlow dataFlow = DataFlowBuilder.subscribeToFeed("fileFeed", String.class)
                .console("read file in:{}")
                .map(String::toUpperCase)
                .console("write file out:{}\n")
                .sink("fileSink")
                .build();

        // File sink to write transformed messages
        FileMessageSink outputFileSink = new FileMessageSink(outputPath);

        // Wire together and start
        DataFlowConnector runner = new DataFlowConnector();
        runner.addDataFlow(dataFlow);
        runner.addFeed(inputFileFeed);
        runner.addSink("fileSink", outputFileSink);

        runner.start();
    }
}

/*
 * SPDX-FileCopyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */
package com.telamin.fluxtion.example.sampleapps.state;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.connector.DataFlowConnector;
import com.telamin.fluxtion.runtime.connector.FileEventFeed;
import com.telamin.fluxtion.runtime.eventfeed.ReadStrategy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates simple state persistence using a user-managed checkpoint filter.
 *
 * The app wires a FileEventFeed source into a DataFlow pipeline. Each incoming line
 * is assigned a monotonically increasing line number (starting at 1). A UserFilter consults
 * a CheckpointFilter to decide whether to pass the event downstream (skip anything already
 * processed in previous runs). Events that pass are printed to the console, and then the
 * CheckpointFilter persists the line number as the last processed id.
 */
public class StateCheckpointDemo {
    public static void main(String[] args) throws Exception {
        String inputPath = args.length > 0 ? args[0] : "./data/input.txt";
        String checkpointPath = args.length > 1 ? args[1] : "./data/checkpoint.txt";

        // Ensure data dir exists if running locally from module root
        Path input = Path.of(inputPath);
        Files.createDirectories(input.getParent());
        if (!Files.exists(input)) {
            Files.createFile(input);
        }

        // Create a file feed that replays from the start; we filter with our checkpoint
        FileEventFeed inputFileFeed = new FileEventFeed(
                inputPath,
                "fileFeed",
                ReadStrategy.EARLIEST
        );

        // Stateful services used by the dataflow
        CheckpointFilter checkpoint = new CheckpointFilter(checkpointPath);
        UserFilter userFilter = new UserFilter(checkpoint);

        // Maintain a line counter inside the pipeline to tag each line with a line number
        AtomicInteger lineCounter = new AtomicInteger(0);

        DataFlow dataFlow = DataFlowBuilder.subscribeToFeed("fileFeed", String.class)
                .map(s -> new LineEvent(lineCounter.incrementAndGet(), s))
                // user-provided filter: only allow lines with id greater than checkpoint
                .filter(userFilter::allow)
                // print the event
                .console("EVENT: {}")
                // persist the id to the checkpoint after successful processing
                .peek(le -> checkpoint.store(le.id()))
                .build();

        // Wire together and start the connector
        DataFlowConnector runner = new DataFlowConnector();
        runner.addDataFlow(dataFlow);
        runner.addFeed(inputFileFeed);

        runner.start();

        // Optional convenience: if the input file is empty, generate a line every 5 seconds
        // to make the demo interactive without external tooling.
        // This matches the requirement to generate a line every 5 seconds on start.
        Thread generator = new Thread(() -> {
            try {
                while (true) {
                    TimeUnit.SECONDS.sleep(3);
                    DemoFileGenerator.appendLine(inputPath);
                }
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "demo-file-generator");
        generator.setDaemon(true);
        generator.start();

        // Keep the app alive
        while (true) {
            Thread.sleep(Duration.ofMinutes(5).toMillis());
        }
    }
}

/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.windowing;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.flowfunction.helpers.Aggregates;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SlidingWindowSample {

    @SneakyThrows
    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribe(Integer.class)
                .slidingAggregate(Aggregates.intSumFactory(), 300, 4)
                .console("current sliding 1.2 second sum:{} timeDelta:%dt")
                .build();

        Random rand = new Random();

        try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.scheduleAtFixedRate(
                    () -> processor.onEvent(rand.nextInt(100)),
                    10, 10, TimeUnit.MILLISECONDS);
            Thread.sleep(4_000);
        }
    }
}


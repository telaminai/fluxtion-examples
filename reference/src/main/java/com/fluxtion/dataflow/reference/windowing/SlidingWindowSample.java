/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.flowfunction.helpers.Aggregates;
import lombok.SneakyThrows;

@SneakyThrows
void main() {
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


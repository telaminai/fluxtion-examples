/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.runtime.clock;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.time.Clock;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.atomic.LongAdder;

public class ClockExample {

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new TimeLogger())
                .build();

        //PRINT CURRENT TIME
        processor.onEvent(DateFormat.getDateTimeInstance());

        //USE A SYNTHETIC STRATEGY TO SET TIME FOR THE PROCESSOR CLOCK
        LongAdder syntheticTime = new LongAdder();
        processor.setClockStrategy(syntheticTime::longValue);

        //SET A NEW TIME - GOING BACK IN TIME!!
        syntheticTime.add(1_000_000_000);
        processor.onEvent(DateFormat.getDateTimeInstance());

        //SET A NEW TIME - BACK TO THE FUTURE
        syntheticTime.add(1_800_000_000_000L);
        processor.onEvent(DateFormat.getDateTimeInstance());
    }


    public static class TimeLogger {
        public Clock wallClock = Clock.DEFAULT_CLOCK;

        @OnEventHandler
        public boolean publishTime(DateFormat dateFormat) {
            System.out.println("time " + dateFormat.format(new Date(wallClock.getWallClockTime())));
            return true;
        }
    }
}

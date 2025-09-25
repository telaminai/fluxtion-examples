/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.aggregate;

import com.telamin.fluxtion.runtime.flowfunction.aggregate.AggregateFlowFunction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateRangeAggregate implements AggregateFlowFunction<LocalDate, String, DateRangeAggregate> {
    private LocalDate startDate;
    private LocalDate endDate;
    private String message;
    private final transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String reset() {
        System.out.println("--- RESET ---");
        startDate = null;
        endDate = null;
        message = null;
        return get();
    }

    @Override
    public String get() {
        return message;
    }

    @Override
    public String aggregate(LocalDate input) {
        startDate = startDate == null ? input : startDate;
        endDate = endDate == null ? input : endDate;
        if (input.isBefore(startDate)) {
            startDate = input;
        } else if (input.isAfter(endDate)) {
            endDate = input;
        } else {
            //RETURN NULL -> NO CHANGE NOTIFICATIONS FIRED
            return null;
        }
        message = formatter.format(startDate) + " - " + formatter.format(endDate);
        return message;
    }
}

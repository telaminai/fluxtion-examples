/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.groupby;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.flowfunction.groupby.GroupBy;
import com.telamin.fluxtion.runtime.flowfunction.groupby.GroupByKey;
import com.telamin.fluxtion.runtime.flowfunction.helpers.Aggregates;

public class GroupByMapKeySample {

    public record Pupil(int year, String sex, String name) { }

    public static void main(String[] args) {
        DataFlow processor = DataFlowBuilder.subscribe(Pupil.class)
                .groupByFieldsAggregate(Aggregates.countFactory(), Pupil::year, Pupil::sex)
                .mapKeys(GroupByKey::getKey)//MAPS KEYS
                .map(GroupBy::toMap)
                .console("{}")
                .build();

        processor.onEvent(new Pupil(2015, "Female", "Bob"));
        processor.onEvent(new Pupil(2013, "Male", "Ashkay"));
        processor.onEvent(new Pupil(2013, "Male", "Channing"));
        processor.onEvent(new Pupil(2013, "Female", "Chelsea"));
        processor.onEvent(new Pupil(2013, "Female", "Tamsin"));
        processor.onEvent(new Pupil(2013, "Female", "Ayola"));
        processor.onEvent(new Pupil(2015, "Female", "Sunita"));
    }
}
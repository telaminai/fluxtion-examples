/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.reference.groupby;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.builder.flowfunction.JoinFlowBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.flowfunction.groupby.GroupBy;
import com.telamin.fluxtion.runtime.flowfunction.helpers.Tuples;

import java.util.List;
import java.util.stream.Collectors;

public class GroupByJoinSample {

    public record Pupil(int year, String school, String name) {}

    public record School(String name) {}

    public static void main(String[] args) {
        var pupils = DataFlowBuilder.subscribe(Pupil.class).groupByToList(Pupil::school);
        var schools = DataFlowBuilder.subscribe(School.class).groupBy(School::name);

        DataFlow processor = JoinFlowBuilder.innerJoin(schools, pupils)
                .mapValues(Tuples.mapTuple(GroupByJoinSample::prettyPrint))
                .map(GroupBy::toMap)
                .console()
                .build();

        //register some schools
        processor.onEvent(new School("RGS"));
        processor.onEvent(new School("Belles"));

        //register some pupils
        processor.onEvent(new Pupil(2015, "RGS", "Bob"));
        processor.onEvent(new Pupil(2013, "RGS", "Ashkay"));
        processor.onEvent(new Pupil(2013, "Belles", "Channing"));
        processor.onEvent(new Pupil(2013, "RGS", "Chelsea"));
        processor.onEvent(new Pupil(2013, "Belles", "Tamsin"));
        processor.onEvent(new Pupil(2013, "Belles", "Ayola"));
        processor.onEvent(new Pupil(2015, "Belles", "Sunita"));
    }

    private static String prettyPrint(School schoolName, List<Pupil> pupils) {
        return pupils.stream().map(Pupil::name).collect(Collectors.joining(",", "pupils[", "]"));
    }
}
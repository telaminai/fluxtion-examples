/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.groupby;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.builder.flowfunction.MultiJoinBuilder;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.flowfunction.groupby.GroupBy;
import lombok.Data;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

public class MultiJoinSample {

    public static void main(String[] args) {
        var ageDataFlow = DataFlowBuilder.groupBy(Age::getName);
        var genderDataFlow = DataFlowBuilder.groupBy(Gender::getName);
        var nationalityDataFlow = DataFlowBuilder.groupBy(Nationality::getName);
        var dependentDataFlow = DataFlowBuilder.groupByToList(Dependent::getGuardianName);

        DataFlow processor = MultiJoinBuilder.builder(String.class, MergedData::new)
                .addJoin(ageDataFlow, MergedData::setAge)
                .addJoin(genderDataFlow, MergedData::setGender)
                .addJoin(nationalityDataFlow, MergedData::setNationality)
                .addOptionalJoin(dependentDataFlow, MergedData::setDependent)
                .dataFlow()
                .mapValues(MergedData::formattedString)
                .map(GroupBy::toMap)
                .console("multi join result : {}")
                .build();


        processor.onEvent(new Age("greg", 47));
        processor.onEvent(new Gender("greg", "male"));
        processor.onEvent(new Nationality("greg", "UK"));
        //update
        processor.onEvent(new Age("greg", 55));
        //new record
        processor.onEvent(new Age("tim", 47));
        processor.onEvent(new Gender("tim", "male"));
        processor.onEvent(new Nationality("tim", "UK"));

        processor.onEvent(new Dependent("greg", "ajay"));
        processor.onEvent(new Dependent("greg", "sammy"));

    }

    @Data
    public static class MergedData {
        private Age age;
        private Gender gender;
        private Nationality nationality;
        private List<Dependent> dependent;

        public String formattedString() {
            String dependentString = " no dependents";
            if (dependent != null) {
                dependentString = dependent.stream()
                        .map(Dependent::getDependentName)
                        .collect(Collectors.joining(", ", " guardian for: [", "]"));
            }
            return age.getAge() + " " + gender.getSex() + " " + nationality.getCountry() + dependentString;
        }
    }

    @Value
    public static class Age {
        String name;
        int age;
    }

    @Value
    public static class Gender {
        String name;
        String sex;
    }


    @Value
    public static class Nationality {
        String name;
        String country;
    }

    @Value
    public static class Dependent {
        String guardianName;
        String dependentName;
    }
}

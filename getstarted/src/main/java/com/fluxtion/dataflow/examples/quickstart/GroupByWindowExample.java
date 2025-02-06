//REPOS repsy-fluxtion=https://repo.repsy.io/mvn/fluxtion/fluxtion-public
//DEPS com.fluxtion.dataflow:dataflow-builder:1.0.0
//COMPILE_OPTIONS -proc:full
package com.fluxtion.dataflow.examples.quickstart;

import com.fluxtion.dataflow.builder.DataFlowBuilder;
import com.fluxtion.dataflow.runtime.DataFlow;
import com.fluxtion.dataflow.runtime.flowfunction.groupby.GroupBy;
import com.fluxtion.dataflow.runtime.flowfunction.helpers.Aggregates;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//Calculates the average speed by manufacturer in a sliding window of 2 seconds with a 500 millisecond bucket size
public class GroupByWindowExample {
    record CarTracker(String make, double speed) {}
    static String[] makes = new String[]{"BMW", "Ford", "Honda", "Jeep", "VW"};

    public static void main(String[] args) {
        System.out.println("building DataFlow::avgSpeedByMake...");
        //build the DataFlow
        DataFlow avgSpeedByMake = DataFlowBuilder.subscribe(CarTracker.class)
                .groupBySliding(
                        CarTracker::make, //key
                        CarTracker::speed, //value
                        Aggregates.doubleAverageFactory(), //avg function per bucket
                        500, 4) //4 buckets 500 millis each
                .mapValues(v -> "avgSpeed-" + v.intValue() + " km/h")
                .map(GroupBy::toMap)
                .sink("average car speed")
                .build();

        //register an output sink with the DataFlow
        avgSpeedByMake.addSink("average car speed", System.out::println);

        //send data from an unbounded real-time feed to the DataFlow
        System.out.println("publishing events to DataFlow::avgSpeedByMake...");
        Random random = new Random();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                () -> avgSpeedByMake.onEvent(new CarTracker(makes[random.nextInt(makes.length)], random.nextDouble(100))),
                100, 400, TimeUnit.MILLISECONDS);
    }
}

/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.compile.aot.jmh;


import com.telamin.fluxtion.example.compile.aot.DemoPriceCalculatorMain;
import com.telamin.fluxtion.example.compile.aot.generated.PriceLadderProcessor;
import com.telamin.fluxtion.example.compile.aot.node.PriceDistributor;
import com.telamin.fluxtion.example.compile.aot.pricer.PriceLadder;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

public class PriceLadderBenchmark {


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 2)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void avgTime_BranchingProcessor(PriceLadderState priceLadderState, Blackhole blackhole) throws InterruptedException {
        priceLadderState.priceProcessor.newPriceLadder(priceLadderState.nextPriceLadder());
        blackhole.consume(priceLadderState.priceDistributor.getPriceLadder());
    }

//    @Benchmark
//    @Fork(value = 1, warmups = 1)
//    @Warmup(iterations = 2)
//    @BenchmarkMode(Mode.Throughput)
//    @Measurement(iterations = 2)
//    @OutputTimeUnit(TimeUnit.SECONDS)
//    public void throughPut_No_BranchingProcessor(PriceLadderState priceLadderState, Blackhole blackhole) throws InterruptedException {
//        priceLadderState.priceProcessorNoBranch.newPriceLadder(priceLadderState.nextPriceLadder());
//        blackhole.consume(priceLadderState.priceDistributor.getPriceLadder());
//    }

//    @Benchmark
//    @Fork(value = 1, warmups = 1)
//    @Warmup(iterations = 2)
//    @BenchmarkMode(Mode.AverageTime)
//    @Measurement(iterations = 2)
//    @OutputTimeUnit(TimeUnit.NANOSECONDS)
//    public void avgTime_No_BranchingProcessor(PriceLadderState priceLadderState, Blackhole blackhole) throws InterruptedException {
//        priceLadderState.priceProcessor.newPriceLadder(priceLadderState.nextPriceLadder());
//        blackhole.consume(priceLadderState.priceDistributor.getPriceLadder());
//    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 2)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void throughPut_BranchingProcessor(PriceLadderState priceLadderState, Blackhole blackhole) throws InterruptedException {
        priceLadderState.priceProcessor.newPriceLadder(priceLadderState.nextPriceLadder());
        blackhole.consume(priceLadderState.priceDistributor.getPriceLadder());
    }

    @State(Scope.Thread)
    public static class PriceLadderState {

        private PriceLadderProcessor priceProcessor;
        private PriceDistributor priceDistributor;
        private PriceLadder[] priceLadders;
        private final int ladderCount = 10_000;
        private int ladderPointer = 0;

        @Setup(Level.Trial)
        public void doSetup() {
            System.out.println("Do Setup");
            priceLadders = DemoPriceCalculatorMain.generateRandomPriceLadders(ladderCount);
            priceDistributor = new PriceDistributor();

            //branching processor
            priceProcessor = new PriceLadderProcessor();
            priceProcessor.init();
            priceProcessor.setPriceDistributor(priceDistributor);
        }

        public PriceLadder nextPriceLadder() {
            int pointer = ladderPointer++ % ladderCount;
            return priceLadders[pointer];
        }

        @TearDown(Level.Iteration)
        public void doTearDown() {
            System.out.println("TearDown - generate new random price ladders");
            priceLadders = DemoPriceCalculatorMain.generateRandomPriceLadders(ladderCount);
        }

    }
}

package com.fluxtion.dataflow.examples.frontpage.multijoin.userfunc;

public record MachineProfileEvent(String id,
                                  LocationCode locationCode,
                                  double maxTempAlarm,
                                  double maxAvgTempAlarm) {
}

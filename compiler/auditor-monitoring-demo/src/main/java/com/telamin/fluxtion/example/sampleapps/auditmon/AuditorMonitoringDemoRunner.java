package com.telamin.fluxtion.example.sampleapps.auditmon;

import com.fluxtion.dataflow.Fluxtion;
import com.telamin.fluxtion.example.sampleapps.auditmon.generated.AuditorMonitoringProcessor;
import com.telamin.fluxtion.runtime.CloneableDataFlow;
import com.telamin.fluxtion.runtime.DataFlow;

public class AuditorMonitoringDemoRunner {

    public static void main(String[] args) throws Exception {
        DataFlow df = new AuditorMonitoringProcessor();
        df.init();

        // Drive some events
        df.onEvent(new Trade("t1", "AAPL", 100, 210.5));
        df.onEvent(new Trade("t2", "MSFT", 250, 320.1));
        df.onEvent(new Trade("t3", "NVDA", 10, 950.0));
    }
}

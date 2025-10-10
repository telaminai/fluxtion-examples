package com.telamin.fluxtion.example.sampleapps.audit;

import com.telamin.fluxtion.example.sampleapps.audit.generated.AuditLoggingProcessor;
import com.telamin.fluxtion.runtime.DataFlow;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Log4j2
public class AuditLoggingDemoRunner {

    public static void main(String[] args) throws Exception {
        // Generation has been moved to AuditLoggingAOTGraphBuilder; this runner just runs the generated processor
        DataFlow df = new AuditLoggingProcessor();

        // Send audit logs to JUL; we bridge JUL -> Log4j2 via log4j-jul and log4j2.yaml config
        Logger logger = LogManager.getLogger("dataflow.order");
        df.setAuditLogProcessor(logger::info);

        // Optional: human-readable ISO timestamp formatter
        df.setAuditTimeFormatter((sb, epochMillis) ->
                sb.append(DateTimeFormatter.ISO_OFFSET_DATE_TIME
                        .format(Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()))));
        
        // Drive some events and demonstrate exception handling + last audit record recovery
        try {
            df.onEvent(new OrderEvent("o1", "AAPL", 100, 210.50));
            df.onEvent(new OrderEvent("o2", "MSFT", 5000, 320.10));
            // This one triggers FailWhenIdNode to throw
            df.onEvent(new OrderEvent("FAIL", "TSLA", 1, 1000.00));
        } catch (Throwable e) {

            log.error("Exception processing FAIL: {}", String.valueOf(e));
            log.error("Last audit record (partial):\n{}", df.getLastAuditLogRecord());
        }
    }
}

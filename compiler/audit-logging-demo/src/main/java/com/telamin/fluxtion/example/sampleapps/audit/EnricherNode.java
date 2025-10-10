package com.telamin.fluxtion.example.sampleapps.audit;

import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.node.NamedBaseNode;

/**
 * Example node that logs structured key/value pairs using the injected EventLogger.
 */
public class EnricherNode extends NamedBaseNode {

    private EnrichedOrder lastEnriched;

    @OnEventHandler
    public void onOrder(OrderEvent order) {
        double notional = order.qty() * order.price();
        lastEnriched = new EnrichedOrder(order, notional);
        // Structured logging: node-local KV pairs aggregated by EventLogManager
        auditLog.info("event", order.toString())
                .debug("calc", "enrich")
                .info("symbol", order.symbol())
                .info("notional", String.valueOf(notional))
                .info("enrichedNotional", lastEnriched.notional);
    }

    public EnrichedOrder getLastEnriched() {
        return lastEnriched;
    }
}

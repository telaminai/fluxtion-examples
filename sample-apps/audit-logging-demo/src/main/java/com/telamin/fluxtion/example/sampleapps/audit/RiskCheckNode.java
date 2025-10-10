package com.telamin.fluxtion.example.sampleapps.audit;

import com.telamin.fluxtion.runtime.annotations.OnTrigger;
import com.telamin.fluxtion.runtime.node.NamedBaseNode;

public class RiskCheckNode extends NamedBaseNode {

    private final EnricherNode enricherNode;

    public RiskCheckNode(EnricherNode enricherNode) {
        this.enricherNode = enricherNode;
    }

    @OnTrigger
    public boolean check() {
        EnrichedOrder enriched = enricherNode.getLastEnriched();
        boolean ok = enriched.notional < 1_000_000; // trivial limit
        auditLog.info("calc", "risk")
                .info("orderId", enriched.source.id())
                .info("notional", String.valueOf(enriched.notional))
                .info("limit.ok", ok);
        return ok;
    }
}

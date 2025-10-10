/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.sampleapps.audit;

import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.node.NamedBaseNode;

/**
 * A node that simulates a failure for a specific OrderEvent id to demonstrate
 * recovery diagnostics using DataFlow#getLastAuditLogRecord().
 */
public class FailWhenIdNode extends NamedBaseNode {

    private final String failId;

    public FailWhenIdNode() {
        this("FAIL");
    }

    public FailWhenIdNode(String failId) {
        this.failId = failId;
    }

    @OnEventHandler
    public void onOrder(OrderEvent order) {
        // Add some audit breadcrumbs then throw for the configured id
        auditLog.info("calc", "fail-check").info("orderId", order.id());
        if (failId.equals(order.id())) {
            auditLog.error("failure", "triggering simulated exception");
            throw new RuntimeException("Simulated processing failure for order id=" + order.id());
        }
    }
}

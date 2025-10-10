/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.sampleapps.audit;

public class EnrichedOrder {
    public final OrderEvent source;
    public final double notional;

    public EnrichedOrder(OrderEvent source, double notional) {
        this.source = source;
        this.notional = notional;
    }

    @Override
    public String toString() {
        return "EnrichedOrder{" + source + ", notional=" + notional + "}";
    }
}

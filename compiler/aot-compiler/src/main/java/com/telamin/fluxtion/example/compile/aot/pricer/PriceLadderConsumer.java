/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.compile.aot.pricer;

public interface PriceLadderConsumer {
    default boolean newPriceLadder(PriceLadder priceLadder) {
        return false;
    }
}

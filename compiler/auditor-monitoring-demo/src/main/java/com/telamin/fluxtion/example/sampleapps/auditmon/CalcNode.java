/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.sampleapps.auditmon;

import com.telamin.fluxtion.runtime.annotations.OnEventHandler;

public class CalcNode {

    private Trade trade;

    @OnEventHandler
    public boolean value(Trade newTrade) {
        boolean update = trade == null || !trade.equals(newTrade);
        trade = newTrade;
        return update;
    }
}

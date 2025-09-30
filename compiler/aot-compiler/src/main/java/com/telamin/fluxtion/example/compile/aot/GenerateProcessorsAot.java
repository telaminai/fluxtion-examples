/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.compile.aot;

import com.fluxtion.dataflow.Fluxtion;
import com.telamin.fluxtion.example.compile.aot.node.PriceLadderPublisher;

public class GenerateProcessorsAot {
    public static void main(String[] args) {
        Fluxtion.compileAot(
                "com.telamin.fluxtion.example.compile.aot.generated",
                "PriceLadderProcessor",
                new PriceLadderPublisher());
    }
}

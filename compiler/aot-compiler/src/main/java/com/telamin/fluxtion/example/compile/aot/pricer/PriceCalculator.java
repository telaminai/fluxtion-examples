/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.compile.aot.pricer;


import com.telamin.fluxtion.example.compile.aot.node.PriceDistributor;

public interface PriceCalculator {
    default void setSkew(int skew){}
    default void setLevels(int maxLevels){}
    default void setPriceDistributor(PriceDistributor priceDistributor){}
}

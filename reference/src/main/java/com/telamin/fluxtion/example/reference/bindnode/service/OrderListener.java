/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.service;

import org.jgrapht.alg.util.VertexDegreeComparator;

public interface OrderListener {
    void newOrder(String order);

    void orderComplete(String order);
}

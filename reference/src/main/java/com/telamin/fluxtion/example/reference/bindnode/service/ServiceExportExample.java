/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.service;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.ExportService;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;

public class ServiceExportExample {

    public static class MyNode implements @ExportService OrderListener {

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::handleStringEvent received:" + stringToProcess);
            return true;
        }

        @Override
        public void newOrder(String order) {
            System.out.println("MyNode::newOrder received:'" + order + "'");
        }

        @Override
        public void orderComplete(String order) {
            System.out.println("MyNode::orderComplete received:'" + order + "'");
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new ServiceExportExample.MyNode())
                .build();

        OrderListener orderListener = processor.getExportedService();

        orderListener.newOrder("test-order-1");
        orderListener.orderComplete("test-order-2");
    }
}

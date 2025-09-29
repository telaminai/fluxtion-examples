/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.telamin.fluxtion.example.reference.bindnode.advanced;

import com.telamin.fluxtion.builder.DataFlowBuilder;
import com.telamin.fluxtion.runtime.annotations.OnBatchEnd;
import com.telamin.fluxtion.runtime.annotations.OnBatchPause;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.lifecycle.BatchHandler;

public class BatchSupport {
    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode event received:" + stringToProcess);
            return true;
        }

        @OnBatchPause
        public void batchPause(){
            System.out.println("MyNode::batchPause");
        }

        @OnBatchEnd
        public void batchEnd(){
            System.out.println("MyNode::batchEnd");
        }
    }

    public static void main(String[] args) {
        var processor = DataFlowBuilder
                .subscribeToNode(new MyNode())
                .build();

        processor.onEvent("test");

        //use BatchHandler service
        BatchHandler batchHandler = (BatchHandler)processor;
        batchHandler.batchPause();
        batchHandler.batchEnd();
    }
}

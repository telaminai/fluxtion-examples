package dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.helpers.Collectors;

public class TriggerUpdateSample {

    public static void buildGraph(EventProcessorConfig processorConfig) {
        DataFlowBuilder.subscribeToNode(new SubscribeToNodeSample.MyComplexNode())
                .console("node triggered -> {}")
                .map(SubscribeToNodeSample.MyComplexNode::getIn)
                .aggregate(Collectors.listFactory(4))
                .updateTrigger(DataFlowBuilder.subscribeToSignal("updateMe"))
                .console("last 4 elements:{}\n");
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(TriggerUpdateSample::buildGraph);
        processor.init();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.publishSignal("updateMe");

        processor.onEvent("D");
        processor.onEvent("E");
        processor.onEvent("F");
        processor.publishSignal("updateMe");
    }
}

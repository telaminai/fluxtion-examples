package dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;

public class GroupByToListSample {

    public record ResetList() {
    }

    public static void buildGraph(EventProcessorConfig processorConfig) {
        var resetSignal = DataFlow.subscribe(ResetList.class).console("\n--- RESET ---");

        DataFlow.subscribe(Integer.class)
                .groupByToList(i -> i % 2 == 0 ? "evens" : "odds")
                .resetTrigger(resetSignal)
                .map(GroupBy::toMap)
                .console("ODD/EVEN map:{}");
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(GroupByToListSample::buildGraph);
        processor.init();
        processor.onEvent(1);
        processor.onEvent(2);
        processor.onEvent(5);
        processor.onEvent(7);
        processor.onEvent(2);
        processor.onEvent(new ResetList());
    }
}

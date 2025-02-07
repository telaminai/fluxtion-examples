package dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.helpers.Collectors;

public class TumblingTriggerSample {

    public record ClearCart() { }

    public record GoToCheckout() { }

    public static void buildGraph(EventProcessorConfig processorConfig) {
        var resetSignal = DataFlowBuilder.subscribe(ClearCart.class).console("\n--- CLEAR CART ---");
        var publishSignal = DataFlowBuilder.subscribe(GoToCheckout.class).console("\n--- CHECKOUT CART ---");

        DataFlowBuilder.subscribe(String.class)
                .aggregate(Collectors.listFactory(3))
                .resetTrigger(resetSignal)
                .publishTriggerOverride(publishSignal)
                .filter(l -> !l.isEmpty())
                .console("CURRENT CART: {}");
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(TumblingTriggerSample::buildGraph);
        processor.init();
        processor.onEvent("Gloves");
        processor.onEvent("Toothpaste");
        processor.onEvent("Towel");
        processor.onEvent("Plug");
        processor.onEvent("Mirror");
        processor.onEvent("Drill");
        processor.onEvent("Salt");

        processor.onEvent(new ClearCart());
        processor.onEvent("Apples");
        processor.onEvent("Camera");

        processor.onEvent(new GoToCheckout());
    }
}

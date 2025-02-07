package dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

public class PushSample {
    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c ->
                DataFlowBuilder.subscribe(String.class)
                        .push(new MyPushTarget()::updated)
        );
        processor.init();

        processor.onEvent("AAA");
        processor.onEvent("BBB");
    }

    public static class MyPushTarget {
        public void updated(String in) {
            System.out.println("received push: " + in);
        }
    }
}

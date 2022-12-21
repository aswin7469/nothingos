package java.util.stream;

import java.util.Spliterator;

interface TerminalOp<E_IN, R> {
    <P_IN> R evaluateSequential(PipelineHelper<E_IN> pipelineHelper, Spliterator<P_IN> spliterator);

    int getOpFlags() {
        return 0;
    }

    StreamShape inputShape() {
        return StreamShape.REFERENCE;
    }

    <P_IN> R evaluateParallel(PipelineHelper<E_IN> pipelineHelper, Spliterator<P_IN> spliterator) {
        if (Tripwire.ENABLED) {
            Tripwire.trip(getClass(), "{0} triggering TerminalOp.evaluateParallel serial default");
        }
        return evaluateSequential(pipelineHelper, spliterator);
    }
}

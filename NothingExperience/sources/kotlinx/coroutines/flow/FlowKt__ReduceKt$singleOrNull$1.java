package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo14009k = 3, mo14010mv = {1, 6, 0}, mo14012xi = 48)
@DebugMetadata(mo14735c = "kotlinx.coroutines.flow.FlowKt__ReduceKt", mo14736f = "Reduce.kt", mo14737i = {0, 0}, mo14738l = {183}, mo14739m = "singleOrNull", mo14740n = {"result", "collector$iv"}, mo14741s = {"L$0", "L$1"})
/* compiled from: Reduce.kt */
final class FlowKt__ReduceKt$singleOrNull$1<T> extends ContinuationImpl {
    Object L$0;
    Object L$1;
    int label;
    /* synthetic */ Object result;

    FlowKt__ReduceKt$singleOrNull$1(Continuation<? super FlowKt__ReduceKt$singleOrNull$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt.singleOrNull((Flow) null, this);
    }
}

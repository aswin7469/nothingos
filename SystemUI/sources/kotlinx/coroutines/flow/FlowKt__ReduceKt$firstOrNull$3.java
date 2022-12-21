package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;

@Metadata(mo64988k = 3, mo64989mv = {1, 5, 1}, mo64991xi = 48)
@DebugMetadata(mo65240c = "kotlinx.coroutines.flow.FlowKt__ReduceKt", mo65241f = "Reduce.kt", mo65242i = {0, 0}, mo65243l = {183}, mo65244m = "firstOrNull", mo65245n = {"result", "collector$iv"}, mo65246s = {"L$0", "L$1"})
/* compiled from: Reduce.kt */
final class FlowKt__ReduceKt$firstOrNull$3<T> extends ContinuationImpl {
    Object L$0;
    Object L$1;
    int label;
    /* synthetic */ Object result;

    FlowKt__ReduceKt$firstOrNull$3(Continuation<? super FlowKt__ReduceKt$firstOrNull$3> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt.firstOrNull((Flow) null, (Function2) null, this);
    }
}

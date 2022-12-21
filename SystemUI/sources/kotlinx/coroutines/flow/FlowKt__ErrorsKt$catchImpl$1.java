package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo64988k = 3, mo64989mv = {1, 5, 1}, mo64991xi = 48)
@DebugMetadata(mo65240c = "kotlinx.coroutines.flow.FlowKt__ErrorsKt", mo65241f = "Errors.kt", mo65242i = {0}, mo65243l = {227}, mo65244m = "catchImpl", mo65245n = {"fromDownstream"}, mo65246s = {"L$0"})
/* compiled from: Errors.kt */
final class FlowKt__ErrorsKt$catchImpl$1<T> extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;

    FlowKt__ErrorsKt$catchImpl$1(Continuation<? super FlowKt__ErrorsKt$catchImpl$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt.catchImpl((Flow) null, (FlowCollector) null, this);
    }
}

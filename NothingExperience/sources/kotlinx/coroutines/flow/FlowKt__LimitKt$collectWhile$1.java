package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;

@Metadata(mo14009k = 3, mo14010mv = {1, 6, 0}, mo14012xi = 176)
@DebugMetadata(mo14735c = "kotlinx.coroutines.flow.FlowKt__LimitKt", mo14736f = "Limit.kt", mo14737i = {0}, mo14738l = {137}, mo14739m = "collectWhile", mo14740n = {"collector"}, mo14741s = {"L$0"})
/* compiled from: Limit.kt */
final class FlowKt__LimitKt$collectWhile$1<T> extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;

    FlowKt__LimitKt$collectWhile$1(Continuation<? super FlowKt__LimitKt$collectWhile$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt__LimitKt.collectWhile((Flow) null, (Function2) null, this);
    }
}

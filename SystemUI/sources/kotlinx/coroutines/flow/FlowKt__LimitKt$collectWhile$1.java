package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;

@Metadata(mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
@DebugMetadata(mo65296c = "kotlinx.coroutines.flow.FlowKt__LimitKt", mo65297f = "Limit.kt", mo65298i = {0}, mo65299l = {138}, mo65300m = "collectWhile", mo65301n = {"collector"}, mo65302s = {"L$0"})
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

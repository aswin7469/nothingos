package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function3;

@Metadata(mo14009k = 3, mo14010mv = {1, 6, 0}, mo14012xi = 48)
@DebugMetadata(mo14735c = "kotlinx.coroutines.flow.FlowKt__EmittersKt", mo14736f = "Emitters.kt", mo14737i = {0}, mo14738l = {216}, mo14739m = "invokeSafely$FlowKt__EmittersKt", mo14740n = {"cause"}, mo14741s = {"L$0"})
/* compiled from: Emitters.kt */
final class FlowKt__EmittersKt$invokeSafely$1<T> extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;

    FlowKt__EmittersKt$invokeSafely$1(Continuation<? super FlowKt__EmittersKt$invokeSafely$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt((FlowCollector) null, (Function3) null, (Throwable) null, this);
    }
}

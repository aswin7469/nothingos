package kotlinx.coroutines.debug.internal;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.sequences.SequenceScope;

@Metadata(mo14009k = 3, mo14010mv = {1, 6, 0}, mo14012xi = 48)
@DebugMetadata(mo14735c = "kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl", mo14736f = "DebugCoroutineInfoImpl.kt", mo14737i = {}, mo14738l = {80}, mo14739m = "yieldFrames", mo14740n = {}, mo14741s = {})
/* compiled from: DebugCoroutineInfoImpl.kt */
final class DebugCoroutineInfoImpl$yieldFrames$1 extends ContinuationImpl {
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ DebugCoroutineInfoImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    DebugCoroutineInfoImpl$yieldFrames$1(DebugCoroutineInfoImpl debugCoroutineInfoImpl, Continuation<? super DebugCoroutineInfoImpl$yieldFrames$1> continuation) {
        super(continuation);
        this.this$0 = debugCoroutineInfoImpl;
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.yieldFrames((SequenceScope<? super StackTraceElement>) null, (CoroutineStackFrame) null, this);
    }
}

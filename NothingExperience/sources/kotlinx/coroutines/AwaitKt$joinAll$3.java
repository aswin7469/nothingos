package kotlinx.coroutines;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo14009k = 3, mo14010mv = {1, 6, 0}, mo14012xi = 48)
@DebugMetadata(mo14735c = "kotlinx.coroutines.AwaitKt", mo14736f = "Await.kt", mo14737i = {}, mo14738l = {66}, mo14739m = "joinAll", mo14740n = {}, mo14741s = {})
/* compiled from: Await.kt */
final class AwaitKt$joinAll$3 extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;

    AwaitKt$joinAll$3(Continuation<? super AwaitKt$joinAll$3> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return AwaitKt.joinAll((Collection<? extends Job>) null, (Continuation<? super Unit>) this);
    }
}

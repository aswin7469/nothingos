package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
@DebugMetadata(mo65296c = "kotlinx.coroutines.flow.SubscribedFlowCollector", mo65297f = "Share.kt", mo65298i = {0}, mo65299l = {410, 414}, mo65300m = "onSubscription", mo65301n = {"safeCollector"}, mo65302s = {"L$1"})
/* compiled from: Share.kt */
final class SubscribedFlowCollector$onSubscription$1 extends ContinuationImpl {
    Object L$0;
    Object L$1;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ SubscribedFlowCollector<T> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SubscribedFlowCollector$onSubscription$1(SubscribedFlowCollector<T> subscribedFlowCollector, Continuation<? super SubscribedFlowCollector$onSubscription$1> continuation) {
        super(continuation);
        this.this$0 = subscribedFlowCollector;
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.onSubscription(this);
    }
}

package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlinx.coroutines.channels.ReceiveChannel;

@Metadata(mo64988k = 3, mo64989mv = {1, 5, 1}, mo64991xi = 48)
@DebugMetadata(mo65240c = "kotlinx.coroutines.flow.FlowKt__ChannelsKt", mo65241f = "Channels.kt", mo65242i = {0, 0, 0}, mo65243l = {51, 62}, mo65244m = "emitAllImpl$FlowKt__ChannelsKt", mo65245n = {"$this$emitAllImpl", "channel", "consume"}, mo65246s = {"L$0", "L$1", "Z$0"})
/* compiled from: Channels.kt */
final class FlowKt__ChannelsKt$emitAllImpl$1<T> extends ContinuationImpl {
    Object L$0;
    Object L$1;
    boolean Z$0;
    int label;
    /* synthetic */ Object result;

    FlowKt__ChannelsKt$emitAllImpl$1(Continuation<? super FlowKt__ChannelsKt$emitAllImpl$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt__ChannelsKt.emitAllImpl$FlowKt__ChannelsKt((FlowCollector) null, (ReceiveChannel) null, false, this);
    }
}

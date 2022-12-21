package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function1;

@Metadata(mo14009k = 3, mo14010mv = {1, 6, 0}, mo14012xi = 176)
@DebugMetadata(mo14735c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", mo14736f = "Channels.common.kt", mo14737i = {0, 0}, mo14738l = {129}, mo14739m = "consumeEach", mo14740n = {"action", "channel$iv"}, mo14741s = {"L$0", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$consumeEach$3<E> extends ContinuationImpl {
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    /* synthetic */ Object result;

    ChannelsKt__Channels_commonKt$consumeEach$3(Continuation<? super ChannelsKt__Channels_commonKt$consumeEach$3> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return ChannelsKt__Channels_commonKt.consumeEach((BroadcastChannel) null, (Function1) null, (Continuation<? super Unit>) this);
    }
}

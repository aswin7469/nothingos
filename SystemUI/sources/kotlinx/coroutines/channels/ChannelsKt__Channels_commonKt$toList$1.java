package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
@DebugMetadata(mo65296c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", mo65297f = "Channels.common.kt", mo65298i = {0, 0}, mo65299l = {148}, mo65300m = "toList", mo65301n = {"$this$toList_u24lambda_u2d3", "$this$consume$iv$iv"}, mo65302s = {"L$1", "L$2"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$toList$1<E> extends ContinuationImpl {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    /* synthetic */ Object result;

    ChannelsKt__Channels_commonKt$toList$1(Continuation<? super ChannelsKt__Channels_commonKt$toList$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return ChannelsKt.toList((ReceiveChannel) null, this);
    }
}

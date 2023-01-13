package kotlinx.coroutines.channels;

import java.util.Comparator;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
@DebugMetadata(mo65296c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", mo65297f = "Deprecated.kt", mo65298i = {0, 0, 1, 1}, mo65299l = {356, 358}, mo65300m = "maxWith", mo65301n = {"comparator", "iterator", "iterator", "max"}, mo65302s = {"L$0", "L$2", "L$2", "L$3"})
/* compiled from: Deprecated.kt */
final class ChannelsKt__DeprecatedKt$maxWith$1<E> extends ContinuationImpl {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    /* synthetic */ Object result;

    ChannelsKt__DeprecatedKt$maxWith$1(Continuation<? super ChannelsKt__DeprecatedKt$maxWith$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return ChannelsKt__DeprecatedKt.maxWith((ReceiveChannel) null, (Comparator) null, this);
    }
}

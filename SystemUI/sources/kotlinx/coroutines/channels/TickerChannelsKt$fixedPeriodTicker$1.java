package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo64988k = 3, mo64989mv = {1, 5, 1}, mo64991xi = 48)
@DebugMetadata(mo65240c = "kotlinx.coroutines.channels.TickerChannelsKt", mo65241f = "TickerChannels.kt", mo65242i = {0, 1}, mo65243l = {84, 88, 94, 96}, mo65244m = "fixedPeriodTicker", mo65245n = {"deadline", "delayNs"}, mo65246s = {"J$1", "J$1"})
/* compiled from: TickerChannels.kt */
final class TickerChannelsKt$fixedPeriodTicker$1 extends ContinuationImpl {
    long J$0;
    long J$1;
    Object L$0;
    int label;
    /* synthetic */ Object result;

    TickerChannelsKt$fixedPeriodTicker$1(Continuation<? super TickerChannelsKt$fixedPeriodTicker$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return TickerChannelsKt.fixedPeriodTicker(0, 0, (SendChannel<? super Unit>) null, this);
    }
}

package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo14009k = 3, mo14010mv = {1, 6, 0}, mo14012xi = 48)
@DebugMetadata(mo14735c = "kotlinx.coroutines.channels.TickerChannelsKt", mo14736f = "TickerChannels.kt", mo14737i = {0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3}, mo14738l = {84, 88, 94, 96}, mo14739m = "fixedPeriodTicker", mo14740n = {"channel", "delayMillis", "deadline", "channel", "deadline", "delayNs", "channel", "deadline", "delayNs", "channel", "deadline", "delayNs"}, mo14741s = {"L$0", "J$0", "J$1", "L$0", "J$0", "J$1", "L$0", "J$0", "J$1", "L$0", "J$0", "J$1"})
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

package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.time.Duration;
import kotlinx.coroutines.DelayKt;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\n"}, mo65043d2 = {"<anonymous>", "", "T", "emittedItem"}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: Delay.kt */
final class FlowKt__DelayKt$debounce$3 extends Lambda implements Function1<T, Long> {
    final /* synthetic */ Function1<T, Duration> $timeout;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    FlowKt__DelayKt$debounce$3(Function1<? super T, Duration> function1) {
        super(1);
        this.$timeout = function1;
    }

    public final long invoke(T t) {
        return DelayKt.m5436toDelayMillisLRDsOJo(this.$timeout.invoke(t).m5321unboximpl());
    }
}

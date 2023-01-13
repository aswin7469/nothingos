package kotlinx.coroutines;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¨\u0006\u0004"}, mo65043d2 = {"DefaultDelay", "Lkotlinx/coroutines/Delay;", "getDefaultDelay", "()Lkotlinx/coroutines/Delay;", "kotlinx-coroutines-core"}, mo65044k = 2, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: DefaultExecutor.kt */
public final class DefaultExecutorKt {
    private static final Delay DefaultDelay = DefaultExecutor.INSTANCE;

    public static final Delay getDefaultDelay() {
        return DefaultDelay;
    }
}

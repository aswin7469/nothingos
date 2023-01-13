package kotlinx.coroutines.debug.internal;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0006\n\u0000\n\u0002\u0010\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, mo65043d2 = {"<anonymous>", ""}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: DebugProbesImpl.kt */
final class DebugProbesImpl$startWeakRefCleanerThread$1 extends Lambda implements Function0<Unit> {
    public static final DebugProbesImpl$startWeakRefCleanerThread$1 INSTANCE = new DebugProbesImpl$startWeakRefCleanerThread$1();

    DebugProbesImpl$startWeakRefCleanerThread$1() {
        super(0);
    }

    public final void invoke() {
        DebugProbesImpl.callerInfoCache.runWeakRefQueueCleaningLoopUntilInterrupted();
    }
}

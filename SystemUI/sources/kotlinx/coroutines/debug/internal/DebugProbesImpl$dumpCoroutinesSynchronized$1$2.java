package kotlinx.coroutines.debug.internal;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.debug.internal.DebugProbesImpl;

@Metadata(mo65042d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003H\n"}, mo65043d2 = {"<anonymous>", "", "it", "Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;"}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: DebugProbesImpl.kt */
final class DebugProbesImpl$dumpCoroutinesSynchronized$1$2 extends Lambda implements Function1<DebugProbesImpl.CoroutineOwner<?>, Boolean> {
    public static final DebugProbesImpl$dumpCoroutinesSynchronized$1$2 INSTANCE = new DebugProbesImpl$dumpCoroutinesSynchronized$1$2();

    DebugProbesImpl$dumpCoroutinesSynchronized$1$2() {
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return Boolean.valueOf(invoke((DebugProbesImpl.CoroutineOwner<?>) (DebugProbesImpl.CoroutineOwner) obj));
    }

    public final boolean invoke(DebugProbesImpl.CoroutineOwner<?> coroutineOwner) {
        return !DebugProbesImpl.INSTANCE.isFinished(coroutineOwner);
    }
}

package com.android.systemui.flags;

import com.android.systemui.flags.FlagListenable;
import kotlin.Metadata;
import kotlin.jvm.internal.Ref;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0006\u001a\u00020\u0007H\u0016R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005¨\u0006\b"}, mo64987d2 = {"com/android/systemui/flags/FlagManager$dispatchListenersAndMaybeRestart$suppressRestartList$1$event$1", "Lcom/android/systemui/flags/FlagListenable$FlagEvent;", "flagId", "", "getFlagId", "()I", "requestNoRestart", "", "shared_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.flags.FlagManager$dispatchListenersAndMaybeRestart$suppressRestartList$1$event$1 */
/* compiled from: FlagManager.kt */
public final class C2108xfff10460 implements FlagListenable.FlagEvent {
    final /* synthetic */ Ref.BooleanRef $didRequestNoRestart;
    private final int flagId;

    C2108xfff10460(int i, Ref.BooleanRef booleanRef) {
        this.$didRequestNoRestart = booleanRef;
        this.flagId = i;
    }

    public int getFlagId() {
        return this.flagId;
    }

    public void requestNoRestart() {
        this.$didRequestNoRestart.element = true;
    }
}

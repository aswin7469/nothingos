package com.android.keyguard.clock;

import com.android.keyguard.clock.ClockManager;
import com.android.systemui.plugins.ClockPlugin;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ClockManager$AvailableClocks$$ExternalSyntheticLambda2 implements Supplier {
    public final /* synthetic */ ClockManager.AvailableClocks f$0;
    public final /* synthetic */ ClockPlugin f$1;

    public /* synthetic */ ClockManager$AvailableClocks$$ExternalSyntheticLambda2(ClockManager.AvailableClocks availableClocks, ClockPlugin clockPlugin) {
        this.f$0 = availableClocks;
        this.f$1 = clockPlugin;
    }

    public final Object get() {
        return this.f$0.mo26608xc832a94a(this.f$1);
    }
}

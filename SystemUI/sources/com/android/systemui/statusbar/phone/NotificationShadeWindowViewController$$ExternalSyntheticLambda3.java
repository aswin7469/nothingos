package com.android.systemui.statusbar.phone;

import com.android.systemui.lowlightclock.LowLightClockController;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationShadeWindowViewController$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ NotificationShadeWindowViewController$$ExternalSyntheticLambda3(boolean z) {
        this.f$0 = z;
    }

    public final void accept(Object obj) {
        ((LowLightClockController) obj).showLowLightClock(this.f$0);
    }
}

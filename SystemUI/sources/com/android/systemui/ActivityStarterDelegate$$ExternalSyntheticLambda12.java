package com.android.systemui;

import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda12 implements Consumer {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda12(Runnable runnable) {
        this.f$0 = runnable;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).postQSRunnableDismissingKeyguard(this.f$0);
    }
}

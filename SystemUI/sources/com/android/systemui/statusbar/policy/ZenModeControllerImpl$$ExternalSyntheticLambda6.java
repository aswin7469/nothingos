package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ZenModeControllerImpl$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ ZenModeControllerImpl$$ExternalSyntheticLambda6(boolean z) {
        this.f$0 = z;
    }

    public final void accept(Object obj) {
        ((ZenModeController.Callback) obj).onZenAvailableChanged(this.f$0);
    }
}

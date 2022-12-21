package com.android.systemui.statusbar.policy;

import android.app.NotificationManager;
import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ZenModeControllerImpl$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ NotificationManager.Policy f$0;

    public /* synthetic */ ZenModeControllerImpl$$ExternalSyntheticLambda5(NotificationManager.Policy policy) {
        this.f$0 = policy;
    }

    public final void accept(Object obj) {
        ((ZenModeController.Callback) obj).onConsolidatedPolicyChanged(this.f$0);
    }
}

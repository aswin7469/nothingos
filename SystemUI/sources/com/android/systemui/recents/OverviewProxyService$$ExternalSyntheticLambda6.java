package com.android.systemui.recents;

import android.os.IBinder;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$$ExternalSyntheticLambda6 implements IBinder.DeathRecipient {
    public final /* synthetic */ OverviewProxyService f$0;

    public /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda6(OverviewProxyService overviewProxyService) {
        this.f$0 = overviewProxyService;
    }

    public final void binderDied() {
        this.f$0.cleanupAfterDeath();
    }
}

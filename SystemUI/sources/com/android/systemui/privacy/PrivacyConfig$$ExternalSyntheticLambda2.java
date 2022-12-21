package com.android.systemui.privacy;

import java.lang.ref.WeakReference;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PrivacyConfig$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PrivacyConfig f$0;
    public final /* synthetic */ WeakReference f$1;

    public /* synthetic */ PrivacyConfig$$ExternalSyntheticLambda2(PrivacyConfig privacyConfig, WeakReference weakReference) {
        this.f$0 = privacyConfig;
        this.f$1 = weakReference;
    }

    public final void run() {
        PrivacyConfig.m2866addCallback$lambda4(this.f$0, this.f$1);
    }
}

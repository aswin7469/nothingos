package com.android.systemui.privacy;

import java.lang.ref.WeakReference;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PrivacyConfig$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PrivacyConfig f$0;
    public final /* synthetic */ WeakReference f$1;

    public /* synthetic */ PrivacyConfig$$ExternalSyntheticLambda1(PrivacyConfig privacyConfig, WeakReference weakReference) {
        this.f$0 = privacyConfig;
        this.f$1 = weakReference;
    }

    public final void run() {
        PrivacyConfig.m2873removeCallback$lambda6(this.f$0, this.f$1);
    }
}

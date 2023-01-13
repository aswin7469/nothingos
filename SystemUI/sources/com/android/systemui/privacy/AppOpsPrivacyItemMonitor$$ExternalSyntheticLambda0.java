package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyItemMonitor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AppOpsPrivacyItemMonitor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PrivacyItemMonitor.Callback f$0;

    public /* synthetic */ AppOpsPrivacyItemMonitor$$ExternalSyntheticLambda0(PrivacyItemMonitor.Callback callback) {
        this.f$0 = callback;
    }

    public final void run() {
        AppOpsPrivacyItemMonitor.m2867dispatchOnPrivacyItemsChanged$lambda8(this.f$0);
    }
}

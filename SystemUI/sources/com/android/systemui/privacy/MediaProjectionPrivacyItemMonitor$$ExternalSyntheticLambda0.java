package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyItemMonitor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaProjectionPrivacyItemMonitor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PrivacyItemMonitor.Callback f$0;

    public /* synthetic */ MediaProjectionPrivacyItemMonitor$$ExternalSyntheticLambda0(PrivacyItemMonitor.Callback callback) {
        this.f$0 = callback;
    }

    public final void run() {
        MediaProjectionPrivacyItemMonitor.m2864dispatchOnPrivacyItemsChanged$lambda6(this.f$0);
    }
}
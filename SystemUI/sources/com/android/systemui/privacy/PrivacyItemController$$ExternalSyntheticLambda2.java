package com.android.systemui.privacy;

import com.android.systemui.util.concurrency.DelayableExecutor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PrivacyItemController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PrivacyItemController f$0;
    public final /* synthetic */ DelayableExecutor f$1;

    public /* synthetic */ PrivacyItemController$$ExternalSyntheticLambda2(PrivacyItemController privacyItemController, DelayableExecutor delayableExecutor) {
        this.f$0 = privacyItemController;
        this.f$1 = delayableExecutor;
    }

    public final void run() {
        PrivacyItemController.m2879updateListAndNotifyChanges$lambda2(this.f$0, this.f$1);
    }
}

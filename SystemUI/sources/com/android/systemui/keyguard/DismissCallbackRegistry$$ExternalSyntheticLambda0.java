package com.android.systemui.keyguard;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DismissCallbackRegistry$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DismissCallbackWrapper f$0;

    public /* synthetic */ DismissCallbackRegistry$$ExternalSyntheticLambda0(DismissCallbackWrapper dismissCallbackWrapper) {
        this.f$0 = dismissCallbackWrapper;
    }

    public final void run() {
        this.f$0.notifyDismissSucceeded();
    }
}

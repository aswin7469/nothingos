package com.android.systemui.biometrics;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AuthController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ AuthController f$0;

    public /* synthetic */ AuthController$$ExternalSyntheticLambda2(AuthController authController) {
        this.f$0 = authController;
    }

    public final void run() {
        this.f$0.cancelIfOwnerIsNotInForeground();
    }
}

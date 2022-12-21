package com.android.systemui.statusbar.phone;

import android.view.View;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UnlockedScreenOffAnimationController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ UnlockedScreenOffAnimationController f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ View f$2;

    public /* synthetic */ UnlockedScreenOffAnimationController$$ExternalSyntheticLambda2(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, Runnable runnable, View view) {
        this.f$0 = unlockedScreenOffAnimationController;
        this.f$1 = runnable;
        this.f$2 = view;
    }

    public final void run() {
        UnlockedScreenOffAnimationController.m3194animateInKeyguard$lambda2(this.f$0, this.f$1, this.f$2);
    }
}

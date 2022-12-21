package com.android.systemui.unfold;

import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UnfoldProgressProvider$addListener$1$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ShellUnfoldProgressProvider.UnfoldListener f$0;
    public final /* synthetic */ float f$1;

    public /* synthetic */ UnfoldProgressProvider$addListener$1$$ExternalSyntheticLambda2(ShellUnfoldProgressProvider.UnfoldListener unfoldListener, float f) {
        this.f$0 = unfoldListener;
        this.f$1 = f;
    }

    public final void run() {
        UnfoldProgressProvider$addListener$1.m3280onTransitionProgress$lambda1(this.f$0, this.f$1);
    }
}

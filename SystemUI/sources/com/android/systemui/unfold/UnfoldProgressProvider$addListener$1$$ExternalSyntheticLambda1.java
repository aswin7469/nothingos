package com.android.systemui.unfold;

import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UnfoldProgressProvider$addListener$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ShellUnfoldProgressProvider.UnfoldListener f$0;

    public /* synthetic */ UnfoldProgressProvider$addListener$1$$ExternalSyntheticLambda1(ShellUnfoldProgressProvider.UnfoldListener unfoldListener) {
        this.f$0 = unfoldListener;
    }

    public final void run() {
        UnfoldProgressProvider$addListener$1.m3284onTransitionFinished$lambda2(this.f$0);
    }
}

package com.android.systemui.animation;

import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AnimatedDialog$$ExternalSyntheticLambda1 implements View.OnLayoutChangeListener {
    public final /* synthetic */ Window f$0;
    public final /* synthetic */ FrameLayout f$1;

    public /* synthetic */ AnimatedDialog$$ExternalSyntheticLambda1(Window window, FrameLayout frameLayout) {
        this.f$0 = window;
        this.f$1 = frameLayout;
    }

    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        AnimatedDialog.m2546start$lambda1(this.f$0, this.f$1, view, i, i2, i3, i4, i5, i6, i7, i8);
    }
}

package com.android.systemui.animation;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.animation.ViewHierarchyAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ViewHierarchyAnimator$Companion$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ViewGroup f$0;
    public final /* synthetic */ View f$1;

    public /* synthetic */ ViewHierarchyAnimator$Companion$$ExternalSyntheticLambda1(ViewGroup viewGroup, View view) {
        this.f$0 = viewGroup;
        this.f$1 = view;
    }

    public final void run() {
        ViewHierarchyAnimator.Companion.m2553animateRemoval$lambda1(this.f$0, this.f$1);
    }
}

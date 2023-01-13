package com.android.systemui.animation;

import android.animation.ValueAnimator;
import android.view.View;
import com.android.systemui.animation.ViewHierarchyAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ViewHierarchyAnimator$Companion$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ View f$0;
    public final /* synthetic */ float[] f$1;

    public /* synthetic */ ViewHierarchyAnimator$Companion$$ExternalSyntheticLambda0(View view, float[] fArr) {
        this.f$0 = view;
        this.f$1 = fArr;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ViewHierarchyAnimator.Companion.m2552animateRemoval$lambda0(this.f$0, this.f$1, valueAnimator);
    }
}

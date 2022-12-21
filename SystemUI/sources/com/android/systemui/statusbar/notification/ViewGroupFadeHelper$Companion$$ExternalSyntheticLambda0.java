package com.android.systemui.statusbar.notification;

import android.animation.ValueAnimator;
import android.view.ViewGroup;
import com.android.systemui.statusbar.notification.ViewGroupFadeHelper;
import java.util.Set;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ViewGroupFadeHelper$Companion$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ViewGroup f$0;
    public final /* synthetic */ Set f$1;

    public /* synthetic */ ViewGroupFadeHelper$Companion$$ExternalSyntheticLambda0(ViewGroup viewGroup, Set set) {
        this.f$0 = viewGroup;
        this.f$1 = set;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ViewGroupFadeHelper.Companion.m3094fadeOutAllChildrenExcept$lambda1$lambda0(this.f$0, this.f$1, valueAnimator);
    }
}

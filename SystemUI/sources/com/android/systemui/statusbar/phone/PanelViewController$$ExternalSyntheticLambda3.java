package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PanelViewController$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PanelViewController f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ ValueAnimator f$4;

    public /* synthetic */ PanelViewController$$ExternalSyntheticLambda3(PanelViewController panelViewController, float f, float f2, float f3, ValueAnimator valueAnimator) {
        this.f$0 = panelViewController;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = f3;
        this.f$4 = valueAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo44944x3aa4d246(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
    }
}

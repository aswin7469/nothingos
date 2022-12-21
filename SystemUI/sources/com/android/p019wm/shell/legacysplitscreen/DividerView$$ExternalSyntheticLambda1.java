package com.android.p019wm.shell.legacysplitscreen;

import android.animation.ValueAnimator;
import com.android.internal.policy.DividerSnapAlgorithm;

/* renamed from: com.android.wm.shell.legacysplitscreen.DividerView$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DividerView$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ DividerView f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ DividerSnapAlgorithm.SnapTarget f$2;

    public /* synthetic */ DividerView$$ExternalSyntheticLambda1(DividerView dividerView, boolean z, DividerSnapAlgorithm.SnapTarget snapTarget) {
        this.f$0 = dividerView;
        this.f$1 = z;
        this.f$2 = snapTarget;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo49619xdb116471(this.f$1, this.f$2, valueAnimator);
    }
}

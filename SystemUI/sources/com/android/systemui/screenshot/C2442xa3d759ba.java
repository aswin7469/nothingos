package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import com.android.systemui.screenshot.DraggableConstraintLayout;

/* renamed from: com.android.systemui.screenshot.DraggableConstraintLayout$SwipeDismissHandler$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2442xa3d759ba implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ DraggableConstraintLayout.SwipeDismissHandler f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;

    public /* synthetic */ C2442xa3d759ba(DraggableConstraintLayout.SwipeDismissHandler swipeDismissHandler, float f, float f2) {
        this.f$0 = swipeDismissHandler;
        this.f$1 = f;
        this.f$2 = f2;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.mo37324xb69fa77f(this.f$1, this.f$2, valueAnimator);
    }
}

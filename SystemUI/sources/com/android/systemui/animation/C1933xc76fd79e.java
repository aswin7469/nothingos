package com.android.systemui.animation;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.view.View;
import com.android.systemui.animation.AnimatedDialog;

/* renamed from: com.android.systemui.animation.AnimatedDialog$AnimatedBoundsLayoutListener$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C1933xc76fd79e implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ Rect f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ int f$5;
    public final /* synthetic */ int f$6;
    public final /* synthetic */ int f$7;
    public final /* synthetic */ int f$8;
    public final /* synthetic */ View f$9;

    public /* synthetic */ C1933xc76fd79e(Rect rect, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, View view) {
        this.f$0 = rect;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
        this.f$4 = i4;
        this.f$5 = i5;
        this.f$6 = i6;
        this.f$7 = i7;
        this.f$8 = i8;
        this.f$9 = view;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        AnimatedDialog.AnimatedBoundsLayoutListener.m2542onLayoutChange$lambda2$lambda1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, valueAnimator);
    }
}

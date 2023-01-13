package com.android.systemui.media.dialog;

import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;

/* renamed from: com.android.systemui.media.dialog.MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2224x9ea0901 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GradientDrawable f$0;
    public final /* synthetic */ GradientDrawable f$1;

    public /* synthetic */ C2224x9ea0901(GradientDrawable gradientDrawable, GradientDrawable gradientDrawable2) {
        this.f$0 = gradientDrawable;
        this.f$1 = gradientDrawable2;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        MediaOutputBaseAdapter.MediaDeviceBaseViewHolder.lambda$animateCornerAndVolume$0(this.f$0, this.f$1, valueAnimator);
    }
}

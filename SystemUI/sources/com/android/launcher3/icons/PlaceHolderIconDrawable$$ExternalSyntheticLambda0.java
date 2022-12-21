package com.android.launcher3.icons;

import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.ColorUtils;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PlaceHolderIconDrawable$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ Drawable f$1;

    public /* synthetic */ PlaceHolderIconDrawable$$ExternalSyntheticLambda0(int i, Drawable drawable) {
        this.f$0 = i;
        this.f$1 = drawable;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$1.setColorFilter(new PorterDuffColorFilter(ColorUtils.setAlphaComponent(this.f$0, ((Integer) valueAnimator.getAnimatedValue()).intValue()), PorterDuff.Mode.SRC_ATOP));
    }
}

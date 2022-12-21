package com.android.systemui.statusbar;

import android.view.animation.Interpolator;
import com.android.systemui.animation.Interpolators;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/statusbar/LiftReveal;", "Lcom/android/systemui/statusbar/LightRevealEffect;", "()V", "FADE_END_COLOR_OUT_THRESHOLD", "", "INTERPOLATOR", "Landroid/view/animation/Interpolator;", "kotlin.jvm.PlatformType", "OVAL_INITIAL_BOTTOM_PERCENT", "OVAL_INITIAL_TOP_PERCENT", "OVAL_INITIAL_WIDTH_PERCENT", "WIDEN_OVAL_THRESHOLD", "setRevealAmountOnScrim", "", "amount", "scrim", "Lcom/android/systemui/statusbar/LightRevealScrim;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LightRevealScrim.kt */
public final class LiftReveal implements LightRevealEffect {
    private static final float FADE_END_COLOR_OUT_THRESHOLD = 0.85f;
    public static final LiftReveal INSTANCE = new LiftReveal();
    private static final Interpolator INTERPOLATOR = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
    private static final float OVAL_INITIAL_BOTTOM_PERCENT = 1.2f;
    private static final float OVAL_INITIAL_TOP_PERCENT = 1.1f;
    private static final float OVAL_INITIAL_WIDTH_PERCENT = 0.5f;
    private static final float WIDEN_OVAL_THRESHOLD = 0.35f;

    private LiftReveal() {
    }

    public void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(lightRevealScrim, "scrim");
        float interpolation = INTERPOLATOR.getInterpolation(f);
        float percentPastThreshold = LightRevealEffect.Companion.getPercentPastThreshold(interpolation, WIDEN_OVAL_THRESHOLD);
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - LightRevealEffect.Companion.getPercentPastThreshold(f, FADE_END_COLOR_OUT_THRESHOLD));
        lightRevealScrim.setRevealGradientBounds((((float) lightRevealScrim.getWidth()) * 0.25f) + (((float) (-lightRevealScrim.getWidth())) * percentPastThreshold), (((float) lightRevealScrim.getHeight()) * OVAL_INITIAL_TOP_PERCENT) - (((float) lightRevealScrim.getHeight()) * interpolation), (((float) lightRevealScrim.getWidth()) * 0.75f) + (((float) lightRevealScrim.getWidth()) * percentPastThreshold), (((float) lightRevealScrim.getHeight()) * OVAL_INITIAL_BOTTOM_PERCENT) + (((float) lightRevealScrim.getHeight()) * interpolation));
    }
}

package com.android.systemui.statusbar;

import android.view.animation.Interpolator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.LightRevealEffect;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class LiftReveal implements LightRevealEffect {
    @NotNull
    public static final LiftReveal INSTANCE = new LiftReveal();
    private static final Interpolator INTERPOLATOR = Interpolators.FAST_OUT_SLOW_IN_REVERSE;

    private LiftReveal() {
    }

    @Override // com.android.systemui.statusbar.LightRevealEffect
    public void setRevealAmountOnScrim(float f, @NotNull LightRevealScrim scrim) {
        Intrinsics.checkNotNullParameter(scrim, "scrim");
        float interpolation = INTERPOLATOR.getInterpolation(f);
        LightRevealEffect.Companion companion = LightRevealEffect.Companion;
        float percentPastThreshold = companion.getPercentPastThreshold(interpolation, 0.35f);
        scrim.setRevealGradientEndColorAlpha(1.0f - companion.getPercentPastThreshold(f, 0.85f));
        scrim.setRevealGradientBounds((scrim.getWidth() * 0.25f) + ((-scrim.getWidth()) * percentPastThreshold), (scrim.getHeight() * 1.1f) - (scrim.getHeight() * interpolation), (scrim.getWidth() * 0.75f) + (scrim.getWidth() * percentPastThreshold), (scrim.getHeight() * 1.2f) + (scrim.getHeight() * interpolation));
    }
}

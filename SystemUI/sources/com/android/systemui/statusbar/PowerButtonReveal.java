package com.android.systemui.statusbar;

import com.android.systemui.animation.Interpolators;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class PowerButtonReveal implements LightRevealEffect {
    private final float OFF_SCREEN_START_AMOUNT = 0.05f;
    private final float WIDTH_INCREASE_MULTIPLIER = 1.25f;
    private final float powerButtonY;

    public PowerButtonReveal(float f) {
        this.powerButtonY = f;
    }

    public final float getPowerButtonY() {
        return this.powerButtonY;
    }

    @Override // com.android.systemui.statusbar.LightRevealEffect
    public void setRevealAmountOnScrim(float f, @NotNull LightRevealScrim scrim) {
        Intrinsics.checkNotNullParameter(scrim, "scrim");
        float interpolation = Interpolators.FAST_OUT_SLOW_IN_REVERSE.getInterpolation(f);
        scrim.setRevealGradientEndColorAlpha(1.0f - LightRevealEffect.Companion.getPercentPastThreshold(interpolation, 0.5f));
        scrim.setRevealGradientBounds((scrim.getWidth() * (this.OFF_SCREEN_START_AMOUNT + 1.0f)) - ((scrim.getWidth() * this.WIDTH_INCREASE_MULTIPLIER) * interpolation), getPowerButtonY() - (scrim.getHeight() * interpolation), (scrim.getWidth() * (this.OFF_SCREEN_START_AMOUNT + 1.0f)) + (scrim.getWidth() * this.WIDTH_INCREASE_MULTIPLIER * interpolation), getPowerButtonY() + (scrim.getHeight() * interpolation));
    }
}

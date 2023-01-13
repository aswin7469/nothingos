package com.android.systemui.statusbar;

import com.android.systemui.animation.Interpolators;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u0016R\u000e\u0010\u0005\u001a\u00020\u0003XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003XD¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/statusbar/PowerButtonReveal;", "Lcom/android/systemui/statusbar/LightRevealEffect;", "powerButtonY", "", "(F)V", "OFF_SCREEN_START_AMOUNT", "WIDTH_INCREASE_MULTIPLIER", "getPowerButtonY", "()F", "setRevealAmountOnScrim", "", "amount", "scrim", "Lcom/android/systemui/statusbar/LightRevealScrim;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LightRevealScrim.kt */
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

    public void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(lightRevealScrim, "scrim");
        float interpolation = Interpolators.FAST_OUT_SLOW_IN_REVERSE.getInterpolation(f);
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - LightRevealEffect.Companion.getPercentPastThreshold(interpolation, 0.5f));
        lightRevealScrim.setInterpolatedRevealAmount(interpolation);
        lightRevealScrim.setRevealGradientBounds((((float) lightRevealScrim.getWidth()) * (this.OFF_SCREEN_START_AMOUNT + 1.0f)) - ((((float) lightRevealScrim.getWidth()) * this.WIDTH_INCREASE_MULTIPLIER) * interpolation), this.powerButtonY - (((float) lightRevealScrim.getHeight()) * interpolation), (((float) lightRevealScrim.getWidth()) * (this.OFF_SCREEN_START_AMOUNT + 1.0f)) + (((float) lightRevealScrim.getWidth()) * this.WIDTH_INCREASE_MULTIPLIER * interpolation), this.powerButtonY + (((float) lightRevealScrim.getHeight()) * interpolation));
    }
}

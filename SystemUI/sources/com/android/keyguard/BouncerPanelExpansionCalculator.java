package com.android.keyguard;

import android.util.MathUtils;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0007¨\u0006\n"}, mo64987d2 = {"Lcom/android/keyguard/BouncerPanelExpansionCalculator;", "", "()V", "aboutToShowBouncerProgress", "", "fraction", "getDreamAlphaScaledExpansion", "getDreamYPositionScaledExpansion", "getKeyguardClockScaledExpansion", "showBouncerProgress", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BouncerPanelExpansionCalculator.kt */
public final class BouncerPanelExpansionCalculator {
    public static final BouncerPanelExpansionCalculator INSTANCE = new BouncerPanelExpansionCalculator();

    @JvmStatic
    public static final float getDreamYPositionScaledExpansion(float f) {
        if (f >= 0.98f) {
            return 1.0f;
        }
        if (((double) f) < 0.93d) {
            return 0.0f;
        }
        return (f - 0.93f) / 0.05f;
    }

    @JvmStatic
    public static final float showBouncerProgress(float f) {
        if (f >= 0.9f) {
            return 1.0f;
        }
        if (((double) f) < 0.6d) {
            return 0.0f;
        }
        return (f - 0.6f) / 0.3f;
    }

    private BouncerPanelExpansionCalculator() {
    }

    @JvmStatic
    public static final float aboutToShowBouncerProgress(float f) {
        return MathUtils.constrain((f - 0.9f) / 0.1f, 0.0f, 1.0f);
    }

    @JvmStatic
    public static final float getKeyguardClockScaledExpansion(float f) {
        return MathUtils.constrain((f - 0.7f) / 0.3f, 0.0f, 1.0f);
    }

    @JvmStatic
    public static final float getDreamAlphaScaledExpansion(float f) {
        return MathUtils.constrain((f - 0.94f) / 0.06f, 0.0f, 1.0f);
    }
}

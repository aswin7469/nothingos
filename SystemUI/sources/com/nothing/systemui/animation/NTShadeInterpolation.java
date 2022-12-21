package com.nothing.systemui.animation;

import android.util.MathUtils;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;

@Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0007¨\u0006\t"}, mo64987d2 = {"Lcom/nothing/systemui/animation/NTShadeInterpolation;", "", "()V", "getNotificationScrimAlpha", "", "process", "forNotification", "", "splitNotification", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTShadeInterpolation.kt */
public final class NTShadeInterpolation {
    public static final NTShadeInterpolation INSTANCE = new NTShadeInterpolation();

    private NTShadeInterpolation() {
    }

    @JvmStatic
    public static final float getNotificationScrimAlpha(float f, boolean z, boolean z2) {
        float f2;
        if (z2) {
            if (z) {
                f2 = MathUtils.constrainedMap(0.0f, 1.0f, 0.4f, 1.0f, f);
            } else {
                f2 = MathUtils.constrainedMap(0.0f, 1.0f, 0.3f, 0.8f, f);
            }
        } else if (z) {
            f2 = MathUtils.constrainedMap(0.0f, 1.0f, 0.3f, 1.0f, f);
        } else {
            f2 = MathUtils.constrainedMap(0.0f, 1.0f, 0.0f, 0.5f, f);
        }
        float f3 = (f2 * 1.2f) - 0.2f;
        if (f3 <= 0.0f) {
            return 0.0f;
        }
        float f4 = 1.0f - f3;
        double d = (double) 1.0f;
        return (float) (d - (((double) 0.5f) * (d - Math.cos((double) ((3.14159f * f4) * f4)))));
    }
}

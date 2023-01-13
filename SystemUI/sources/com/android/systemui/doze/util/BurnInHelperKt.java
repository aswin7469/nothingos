package com.android.systemui.doze.util;

import android.util.MathUtils;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\u001a\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n\u001a\u0006\u0010\u000b\u001a\u00020\u0001\u001a\u0006\u0010\f\u001a\u00020\u0001\u001a \u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u0010"}, mo65043d2 = {"BURN_IN_PREVENTION_PERIOD_PROGRESS", "", "BURN_IN_PREVENTION_PERIOD_SCALE", "BURN_IN_PREVENTION_PERIOD_X", "BURN_IN_PREVENTION_PERIOD_Y", "MILLIS_PER_MINUTES", "getBurnInOffset", "", "amplitude", "xAxis", "", "getBurnInProgressOffset", "getBurnInScale", "zigzag", "x", "period", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: BurnInHelper.kt */
public final class BurnInHelperKt {
    private static final float BURN_IN_PREVENTION_PERIOD_PROGRESS = 89.0f;
    private static final float BURN_IN_PREVENTION_PERIOD_SCALE = 181.0f;
    private static final float BURN_IN_PREVENTION_PERIOD_X = 83.0f;
    private static final float BURN_IN_PREVENTION_PERIOD_Y = 521.0f;
    private static final float MILLIS_PER_MINUTES = 60000.0f;

    public static final int getBurnInOffset(int i, boolean z) {
        return (int) zigzag(((float) System.currentTimeMillis()) / MILLIS_PER_MINUTES, (float) i, z ? BURN_IN_PREVENTION_PERIOD_X : BURN_IN_PREVENTION_PERIOD_Y);
    }

    public static final float getBurnInProgressOffset() {
        return zigzag(((float) System.currentTimeMillis()) / MILLIS_PER_MINUTES, 1.0f, BURN_IN_PREVENTION_PERIOD_PROGRESS);
    }

    public static final float getBurnInScale() {
        return zigzag(((float) System.currentTimeMillis()) / MILLIS_PER_MINUTES, 0.2f, BURN_IN_PREVENTION_PERIOD_SCALE) + 0.8f;
    }

    private static final float zigzag(float f, float f2, float f3) {
        float f4 = (float) 2;
        float f5 = (f % f3) / (f3 / f4);
        if (f5 > 1.0f) {
            f5 = f4 - f5;
        }
        return MathUtils.lerp(0.0f, f2, f5);
    }
}

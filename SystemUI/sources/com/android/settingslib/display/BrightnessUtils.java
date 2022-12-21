package com.android.settingslib.display;

import android.util.MathUtils;

public class BrightnessUtils {

    /* renamed from: A */
    private static final float f245A = 0.17883277f;

    /* renamed from: B */
    private static final float f246B = 0.28466892f;

    /* renamed from: C */
    private static final float f247C = 0.5599107f;
    public static final int GAMMA_SPACE_MAX = 65535;
    public static final int GAMMA_SPACE_MIN = 0;

    /* renamed from: R */
    private static final float f248R = 0.5f;

    public static final int convertGammaToLinear(int i, int i2, int i3) {
        float f;
        float norm = MathUtils.norm(0.0f, 65535.0f, (float) i);
        if (norm <= 0.5f) {
            f = MathUtils.sq(norm / 0.5f);
        } else {
            f = MathUtils.exp((norm - f247C) / f245A) + f246B;
        }
        return Math.round(MathUtils.lerp(i2, i3, f / 12.0f));
    }

    public static final float convertGammaToLinearFloat(int i, float f, float f2) {
        float f3;
        float norm = MathUtils.norm(0.0f, 65535.0f, (float) i);
        if (norm <= 0.5f) {
            f3 = MathUtils.sq(norm / 0.5f);
        } else {
            f3 = MathUtils.exp((norm - f247C) / f245A) + f246B;
        }
        return MathUtils.lerp(f, f2, MathUtils.constrain(f3, 0.0f, 12.0f) / 12.0f);
    }

    public static final int convertLinearToGamma(int i, int i2, int i3) {
        return convertLinearToGammaFloat((float) i, (float) i2, (float) i3);
    }

    public static final int convertLinearToGammaFloat(float f, float f2, float f3) {
        float f4;
        float norm = MathUtils.norm(f2, f3, f) * 12.0f;
        if (norm <= 1.0f) {
            f4 = MathUtils.sqrt(norm) * 0.5f;
        } else {
            f4 = (MathUtils.log(norm - f246B) * f245A) + f247C;
        }
        return Math.round(MathUtils.lerp(0, 65535, f4));
    }
}

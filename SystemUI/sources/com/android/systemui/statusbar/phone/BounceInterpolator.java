package com.android.systemui.statusbar.phone;

import android.view.animation.Interpolator;

public class BounceInterpolator implements Interpolator {
    private static final float SCALE_FACTOR = 7.5625f;

    public float getInterpolation(float f) {
        float f2 = f * 1.1f;
        if (f2 < 0.36363637f) {
            return SCALE_FACTOR * f2 * f2;
        }
        if (f2 < 0.72727275f) {
            float f3 = f2 - 0.54545456f;
            return (SCALE_FACTOR * f3 * f3) + 0.75f;
        } else if (f2 >= 0.90909094f) {
            return 1.0f;
        } else {
            float f4 = f2 - 0.8181818f;
            return (SCALE_FACTOR * f4 * f4) + 0.9375f;
        }
    }
}

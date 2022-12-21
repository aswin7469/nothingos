package com.nothing.systemui.settings.brightness;

import android.util.MathUtils;
import com.nothing.systemui.util.NTLogUtil;

public class BrightnessControllerEx {
    private static final float HALF_PERCENTAGE = 0.5f;
    private static final float HUNDRED_PERCENTAGE = 1.0f;
    private static final float NT_HALF_PERCENTAGE = 0.4f;
    private static final String TAG = "BrightnessControllerEx";
    private int mMotionAction = 1;

    public float convertToNTSliderValForAutoBrightness(int i) {
        float f = ((float) i) / 65535.0f;
        return (f <= NT_HALF_PERCENTAGE ? (f / NT_HALF_PERCENTAGE) * 0.5f : (((f - NT_HALF_PERCENTAGE) / 0.6f) * 0.5f) + 0.5f) * 65535.0f;
    }

    public float convertToNTSliderValForManual(int i) {
        float f = ((float) i) / 65535.0f;
        return (f <= 0.5f ? (f * NT_HALF_PERCENTAGE) / 0.5f : (((f - 0.5f) / 0.5f) * 0.6f) + NT_HALF_PERCENTAGE) * 65535.0f;
    }

    public int calculateSliderVal(float f, float f2, float f3, int i) {
        int lerpInv = (int) (MathUtils.lerpInv(f, f2, f3) * 65535.0f);
        if (lerpInv == ((int) convertToNTSliderValForManual(i))) {
            NTLogUtil.m1680d(TAG, "NT slider: The value in the slider is equal to the value on the current brightness");
            return -1;
        }
        int convertToNTSliderValForAutoBrightness = (int) convertToNTSliderValForAutoBrightness(lerpInv);
        NTLogUtil.m1680d(TAG, "NT slider animateSliderTo: " + convertToNTSliderValForAutoBrightness);
        return convertToNTSliderValForAutoBrightness;
    }

    public void setMotionAction(int i) {
        this.mMotionAction = i;
    }

    public boolean isSliderTouched() {
        int i = this.mMotionAction;
        return i == 2 || i == 0;
    }
}

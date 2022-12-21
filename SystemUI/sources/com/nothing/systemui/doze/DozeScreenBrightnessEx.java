package com.nothing.systemui.doze;

import com.android.systemui.doze.DozeMachine;

public class DozeScreenBrightnessEx {
    public static final int LEVEL_0 = 24;
    public static final int LEVEL_1 = 148;
    public static final int LEVEL_2 = 255;
    private static final int MAX_BRIGHTNESS = 255;

    public static boolean updateDozeBrightness(DozeMachine.Service service, int i, int i2) {
        if (i == -1) {
            i = i2;
        }
        if (i == 0) {
            i = 24;
        } else if (i == 30) {
            i = 148;
        } else if (i == 5000) {
            i = 255;
        }
        boolean z = i > 0;
        if (z) {
            service.setDozeScreenBrightness(i);
        }
        return z;
    }
}

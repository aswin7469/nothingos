package com.nothing.settings.display.colors;

import android.util.Log;

public class ColorTemperatureUtils {
    public static int convertValueToProgress(int i, int i2, int i3, int i4) {
        float f;
        int i5 = (i2 + i3) / 2;
        if (i > i3 || i < i2) {
            Log.e("ColorTemperatureUtils", "convertValueToProgress realValue + " + i + " invalid ");
            i = i4;
        }
        if (i < i4) {
            float f2 = ((float) (i5 - i2)) / ((float) (i4 - i2));
            float f3 = (float) i2;
            f = (((float) i) * f2) + (f3 - (f3 * f2));
        } else {
            float f4 = ((float) (i3 - i5)) / ((float) (i3 - i4));
            float f5 = (float) i3;
            f = (((float) i) * f4) + (f5 - (f5 * f4));
        }
        return ((int) f) - i2;
    }

    public static int convertProgressToValue(int i, int i2, int i3, int i4) {
        float f;
        int i5 = (i3 + i2) / 2;
        if (i > i3 - i2 || i < 0) {
            Log.e("ColorTemperatureUtils", "convertValueToProgress progress " + i + " invalid!");
            i = i5 - i2;
        }
        int i6 = i + i2;
        if (i6 < i5) {
            float f2 = ((float) (i4 - i2)) / ((float) (i5 - i2));
            float f3 = (float) i2;
            f = (((float) i6) * f2) + (f3 - (f3 * f2));
        } else {
            float f4 = ((float) (i3 - i4)) / ((float) (i3 - i5));
            float f5 = (float) i3;
            f = (((float) i6) * f4) + (f5 - (f5 * f4));
        }
        return (int) f;
    }
}

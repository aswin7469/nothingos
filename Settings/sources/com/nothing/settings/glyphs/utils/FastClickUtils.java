package com.nothing.settings.glyphs.utils;

public class FastClickUtils {
    private static long lastClickTime;

    public static boolean isFastClick() {
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = currentTimeMillis - lastClickTime <= 1000;
        lastClickTime = currentTimeMillis;
        return z;
    }
}

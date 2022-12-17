package com.nothing.settings.display.aod;

public class AodUtils {
    public static int getHour(String str) {
        return Integer.valueOf(str.substring(0, 2)).intValue();
    }

    public static int getMin(String str) {
        return Integer.valueOf(str.substring(2, 4)).intValue();
    }
}

package com.nothing.experience.internalapi;

import android.os.SystemProperties;

public class SystemPropertiesWrapper {
    public static String get(String str) {
        return SystemProperties.get(str);
    }

    public static String get(String str, String str2) {
        return SystemProperties.get(str, str2);
    }
}

package com.nothing.experience.utils;

import android.util.Log;

public class LogUtil {
    private static final String DEBUG_TAG = "NTExperience";

    /* renamed from: d */
    public static void m42d(String str) {
        if (Log.isLoggable(DEBUG_TAG, 3)) {
            Log.d(DEBUG_TAG, str);
        }
    }

    public static void printStackTrace(Exception exc) {
        if (exc != null) {
            exc.printStackTrace();
        }
    }

    public static boolean isLoggable() {
        return Log.isLoggable(DEBUG_TAG, 3);
    }

    public static boolean isTestEnv() {
        return isLoggable() && !SoftwareInfoUtil.isLocked();
    }
}

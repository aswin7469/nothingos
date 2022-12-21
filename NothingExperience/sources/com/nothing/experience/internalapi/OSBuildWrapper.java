package com.nothing.experience.internalapi;

import android.os.Build;

public class OSBuildWrapper {
    public static boolean isDebuggable() {
        return Build.isDebuggable();
    }
}

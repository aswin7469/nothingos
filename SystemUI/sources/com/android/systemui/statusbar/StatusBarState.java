package com.android.systemui.statusbar;

public class StatusBarState {
    public static final int KEYGUARD = 1;
    public static final int SHADE = 0;
    public static final int SHADE_LOCKED = 2;

    public static String toString(int i) {
        if (i == 0) {
            return "SHADE";
        }
        if (i != 1) {
            return i != 2 ? "UNKNOWN: " + i : "SHADE_LOCKED";
        }
        return "KEYGUARD";
    }
}

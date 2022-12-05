package com.android.systemui.statusbar;
/* loaded from: classes.dex */
public class StatusBarState {
    public static String toShortString(int i) {
        if (i != 0) {
            if (i == 1) {
                return "KGRD";
            }
            if (i == 2) {
                return "SHD_LCK";
            }
            if (i == 3) {
                return "FS_USRSW";
            }
            return "bad_value_" + i;
        }
        return "SHD";
    }
}
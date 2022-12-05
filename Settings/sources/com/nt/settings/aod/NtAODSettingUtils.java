package com.nt.settings.aod;
/* loaded from: classes2.dex */
public class NtAODSettingUtils {
    public static int getHour(String str) {
        return Integer.valueOf(str.substring(0, 2)).intValue();
    }

    public static int getMin(String str) {
        return Integer.valueOf(str.substring(2, 4)).intValue();
    }
}

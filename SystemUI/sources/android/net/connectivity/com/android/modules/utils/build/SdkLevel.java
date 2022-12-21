package android.net.connectivity.com.android.modules.utils.build;

import android.os.Build;

public final class SdkLevel {
    public static boolean isAtLeastR() {
        return true;
    }

    public static boolean isAtLeastS() {
        return true;
    }

    public static boolean isAtLeastSv2() {
        return true;
    }

    public static boolean isAtLeastT() {
        return true;
    }

    private SdkLevel() {
    }

    private static boolean isAtLeastPreReleaseCodename(String str) {
        if (!"REL".equals(Build.VERSION.CODENAME) && Build.VERSION.CODENAME.compareTo(str) >= 0) {
            return true;
        }
        return false;
    }
}

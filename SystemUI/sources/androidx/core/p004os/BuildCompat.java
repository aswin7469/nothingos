package androidx.core.p004os;

import android.os.Build;
import java.util.Locale;

/* renamed from: androidx.core.os.BuildCompat */
public class BuildCompat {

    /* renamed from: androidx.core.os.BuildCompat$PrereleaseSdkCheck */
    public @interface PrereleaseSdkCheck {
    }

    @Deprecated
    public static boolean isAtLeastN() {
        return true;
    }

    @Deprecated
    public static boolean isAtLeastNMR1() {
        return true;
    }

    @Deprecated
    public static boolean isAtLeastO() {
        return true;
    }

    @Deprecated
    public static boolean isAtLeastOMR1() {
        return true;
    }

    @Deprecated
    public static boolean isAtLeastP() {
        return true;
    }

    @Deprecated
    public static boolean isAtLeastQ() {
        return true;
    }

    @Deprecated
    public static boolean isAtLeastR() {
        return true;
    }

    @Deprecated
    public static boolean isAtLeastS() {
        return true;
    }

    @Deprecated
    public static boolean isAtLeastSv2() {
        return true;
    }

    public static boolean isAtLeastT() {
        return true;
    }

    private BuildCompat() {
    }

    protected static boolean isAtLeastPreReleaseCodename(String str, String str2) {
        if (!"REL".equals(str2) && str2.toUpperCase(Locale.ROOT).compareTo(str.toUpperCase(Locale.ROOT)) >= 0) {
            return true;
        }
        return false;
    }

    public static boolean isAtLeastU() {
        return isAtLeastPreReleaseCodename("UpsideDownCake", Build.VERSION.CODENAME);
    }
}

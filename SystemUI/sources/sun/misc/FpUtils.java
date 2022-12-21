package sun.misc;

import android.net.wifi.WifiManager;
import jdk.internal.math.DoubleConsts;
import jdk.internal.math.FloatConsts;

public class FpUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    private FpUtils() {
    }

    @Deprecated
    public static int getExponent(double d) {
        return Math.getExponent(d);
    }

    @Deprecated
    public static int getExponent(float f) {
        return Math.getExponent(f);
    }

    @Deprecated
    public static double rawCopySign(double d, double d2) {
        return Math.copySign(d, d2);
    }

    @Deprecated
    public static float rawCopySign(float f, float f2) {
        return Math.copySign(f, f2);
    }

    @Deprecated
    public static boolean isFinite(double d) {
        return Double.isFinite(d);
    }

    @Deprecated
    public static boolean isFinite(float f) {
        return Float.isFinite(f);
    }

    public static boolean isInfinite(double d) {
        return Double.isInfinite(d);
    }

    public static boolean isInfinite(float f) {
        return Float.isInfinite(f);
    }

    public static boolean isNaN(double d) {
        return Double.isNaN(d);
    }

    public static boolean isNaN(float f) {
        return Float.isNaN(f);
    }

    public static boolean isUnordered(double d, double d2) {
        return isNaN(d) || isNaN(d2);
    }

    public static boolean isUnordered(float f, float f2) {
        return isNaN(f) || isNaN(f2);
    }

    public static int ilogb(double d) {
        int exponent = getExponent(d);
        if (exponent != -1023) {
            if (exponent != 1024) {
                return exponent;
            }
            return isNaN(d) ? 1073741824 : 268435456;
        } else if (d == 0.0d) {
            return -268435456;
        } else {
            long doubleToRawLongBits = Double.doubleToRawLongBits(d) & DoubleConsts.SIGNIF_BIT_MASK;
            while (doubleToRawLongBits < WifiManager.WIFI_FEATURE_TRUST_ON_FIRST_USE) {
                doubleToRawLongBits *= 2;
                exponent--;
            }
            return exponent + 1;
        }
    }

    public static int ilogb(float f) {
        int exponent = getExponent(f);
        if (exponent != -127) {
            if (exponent != 128) {
                return exponent;
            }
            return isNaN(f) ? 1073741824 : 268435456;
        } else if (f == 0.0f) {
            return -268435456;
        } else {
            int floatToRawIntBits = Float.floatToRawIntBits(f) & FloatConsts.SIGNIF_BIT_MASK;
            while (floatToRawIntBits < 8388608) {
                floatToRawIntBits *= 2;
                exponent--;
            }
            return exponent + 1;
        }
    }

    @Deprecated
    public static double scalb(double d, int i) {
        return Math.scalb(d, i);
    }

    @Deprecated
    public static float scalb(float f, int i) {
        return Math.scalb(f, i);
    }

    @Deprecated
    public static double nextAfter(double d, double d2) {
        return Math.nextAfter(d, d2);
    }

    @Deprecated
    public static float nextAfter(float f, double d) {
        return Math.nextAfter(f, d);
    }

    @Deprecated
    public static double nextUp(double d) {
        return Math.nextUp(d);
    }

    @Deprecated
    public static float nextUp(float f) {
        return Math.nextUp(f);
    }

    @Deprecated
    public static double nextDown(double d) {
        return Math.nextDown(d);
    }

    @Deprecated
    public static double nextDown(float f) {
        return (double) Math.nextDown(f);
    }

    @Deprecated
    public static double copySign(double d, double d2) {
        return StrictMath.copySign(d, d2);
    }

    @Deprecated
    public static float copySign(float f, float f2) {
        return StrictMath.copySign(f, f2);
    }

    @Deprecated
    public static double ulp(double d) {
        return Math.ulp(d);
    }

    @Deprecated
    public static float ulp(float f) {
        return Math.ulp(f);
    }

    @Deprecated
    public static double signum(double d) {
        return Math.signum(d);
    }

    @Deprecated
    public static float signum(float f) {
        return Math.signum(f);
    }
}

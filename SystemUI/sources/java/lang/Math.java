package java.lang;

import android.icu.lang.UCharacter;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import java.math.BigDecimal;
import java.util.Random;
import jdk.internal.math.DoubleConsts;
import jdk.internal.math.FloatConsts;

public final class Math {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final double DEGREES_TO_RADIANS = 0.017453292519943295d;

    /* renamed from: E */
    public static final double f534E = 2.718281828459045d;

    /* renamed from: PI */
    public static final double f535PI = 3.141592653589793d;
    private static final double RADIANS_TO_DEGREES = 57.29577951308232d;
    private static final long negativeZeroDoubleBits = Double.doubleToRawLongBits(-0.0d);
    private static final long negativeZeroFloatBits = ((long) Float.floatToRawIntBits(-0.0f));
    static double twoToTheDoubleScaleDown = powerOfTwoD(-512);
    static double twoToTheDoubleScaleUp = powerOfTwoD(512);

    public static native double IEEEremainder(double d, double d2);

    public static int abs(int i) {
        return i < 0 ? -i : i;
    }

    public static long abs(long j) {
        return j < 0 ? -j : j;
    }

    public static native double acos(double d);

    public static native double asin(double d);

    public static native double atan(double d);

    public static native double atan2(double d, double d2);

    public static native double cbrt(double d);

    public static native double ceil(double d);

    public static native double cos(double d);

    public static native double cosh(double d);

    public static native double exp(double d);

    public static native double expm1(double d);

    public static native double floor(double d);

    public static native double hypot(double d, double d2);

    public static native double log(double d);

    public static native double log10(double d);

    public static native double log1p(double d);

    public static int max(int i, int i2) {
        return i >= i2 ? i : i2;
    }

    public static long max(long j, long j2) {
        return j >= j2 ? j : j2;
    }

    public static int min(int i, int i2) {
        return i <= i2 ? i : i2;
    }

    public static long min(long j, long j2) {
        return j <= j2 ? j : j2;
    }

    public static long multiplyFull(int i, int i2) {
        return ((long) i) * ((long) i2);
    }

    public static long multiplyHigh(long j, long j2) {
        if (j < 0 || j2 < 0) {
            long j3 = j >> 32;
            long j4 = j & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
            long j5 = j2 >> 32;
            long j6 = j2 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
            long j7 = (j6 * j3) + ((j4 * j6) >>> 32);
            return (j3 * j5) + (j7 >> 32) + (((j7 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + (j4 * j5)) >> 32);
        }
        long j8 = j >>> 32;
        long j9 = j2 >>> 32;
        long j10 = j & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j11 = j2 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        long j12 = j8 * j9;
        long j13 = j10 * j11;
        return (((j13 >>> 32) + ((((j8 + j10) * (j9 + j11)) - j12) - j13)) >>> 32) + j12;
    }

    public static native double pow(double d, double d2);

    public static native double rint(double d);

    public static native double sin(double d);

    public static native double sinh(double d);

    public static native double sqrt(double d);

    public static native double tan(double d);

    public static native double tanh(double d);

    public static double toDegrees(double d) {
        return d * RADIANS_TO_DEGREES;
    }

    public static double toRadians(double d) {
        return d * DEGREES_TO_RADIANS;
    }

    private Math() {
    }

    public static int round(float f) {
        int floatToRawIntBits = Float.floatToRawIntBits(f);
        int i = 149 - ((2139095040 & floatToRawIntBits) >> 23);
        if ((i & -32) != 0) {
            return (int) f;
        }
        int i2 = (8388607 & floatToRawIntBits) | 8388608;
        if (floatToRawIntBits < 0) {
            i2 = -i2;
        }
        return ((i2 >> i) + 1) >> 1;
    }

    public static long round(double d) {
        long doubleToRawLongBits = Double.doubleToRawLongBits(d);
        long j = 1074 - ((DoubleConsts.EXP_BIT_MASK & doubleToRawLongBits) >> 52);
        if ((-64 & j) != 0) {
            return (long) d;
        }
        long j2 = (DoubleConsts.SIGNIF_BIT_MASK & doubleToRawLongBits) | WifiManager.WIFI_FEATURE_TRUST_ON_FIRST_USE;
        if (doubleToRawLongBits < 0) {
            j2 = -j2;
        }
        return ((j2 >> ((int) j)) + 1) >> 1;
    }

    private static final class RandomNumberGeneratorHolder {
        static final Random randomNumberGenerator = new Random();

        private RandomNumberGeneratorHolder() {
        }
    }

    public static double random() {
        return RandomNumberGeneratorHolder.randomNumberGenerator.nextDouble();
    }

    public static void setRandomSeedInternal(long j) {
        RandomNumberGeneratorHolder.randomNumberGenerator.setSeed(j);
    }

    public static int randomIntInternal() {
        return RandomNumberGeneratorHolder.randomNumberGenerator.nextInt();
    }

    public static long randomLongInternal() {
        return RandomNumberGeneratorHolder.randomNumberGenerator.nextLong();
    }

    public static int addExact(int i, int i2) {
        int i3 = i + i2;
        if (((i ^ i3) & (i2 ^ i3)) >= 0) {
            return i3;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long addExact(long j, long j2) {
        long j3 = j + j2;
        if (((j ^ j3) & (j2 ^ j3)) >= 0) {
            return j3;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int subtractExact(int i, int i2) {
        int i3 = i - i2;
        if (((i ^ i3) & (i2 ^ i)) >= 0) {
            return i3;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long subtractExact(long j, long j2) {
        long j3 = j - j2;
        if (((j ^ j3) & (j2 ^ j)) >= 0) {
            return j3;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int multiplyExact(int i, int i2) {
        long j = ((long) i) * ((long) i2);
        int i3 = (int) j;
        if (((long) i3) == j) {
            return i3;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long multiplyExact(long j, int i) {
        return multiplyExact(j, (long) i);
    }

    public static long multiplyExact(long j, long j2) {
        long j3 = j * j2;
        if (((abs(j) | abs(j2)) >>> 31) == 0 || ((j2 == 0 || j3 / j2 == j) && (j != Long.MIN_VALUE || j2 != -1))) {
            return j3;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int incrementExact(int i) {
        if (i != Integer.MAX_VALUE) {
            return i + 1;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long incrementExact(long j) {
        if (j != Long.MAX_VALUE) {
            return j + 1;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int decrementExact(int i) {
        if (i != Integer.MIN_VALUE) {
            return i - 1;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long decrementExact(long j) {
        if (j != Long.MIN_VALUE) {
            return j - 1;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int negateExact(int i) {
        if (i != Integer.MIN_VALUE) {
            return -i;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long negateExact(long j) {
        if (j != Long.MIN_VALUE) {
            return -j;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int toIntExact(long j) {
        int i = (int) j;
        if (((long) i) == j) {
            return i;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static int floorDiv(int i, int i2) {
        int i3 = i / i2;
        return ((i ^ i2) >= 0 || i2 * i3 == i) ? i3 : i3 - 1;
    }

    public static long floorDiv(long j, int i) {
        return floorDiv(j, (long) i);
    }

    public static long floorDiv(long j, long j2) {
        long j3 = j / j2;
        return ((j ^ j2) >= 0 || j2 * j3 == j) ? j3 : j3 - 1;
    }

    public static int floorMod(int i, int i2) {
        return i - (floorDiv(i, i2) * i2);
    }

    public static int floorMod(long j, int i) {
        return (int) (j - (floorDiv(j, i) * ((long) i)));
    }

    public static long floorMod(long j, long j2) {
        return j - (floorDiv(j, j2) * j2);
    }

    public static float abs(float f) {
        return Float.intBitsToFloat(Float.floatToRawIntBits(f) & Integer.MAX_VALUE);
    }

    public static double abs(double d) {
        return Double.longBitsToDouble(Double.doubleToRawLongBits(d) & Long.MAX_VALUE);
    }

    public static float max(float f, float f2) {
        if (f != f) {
            return f;
        }
        if (f == 0.0f && f2 == 0.0f && ((long) Float.floatToRawIntBits(f)) == negativeZeroFloatBits) {
            return f2;
        }
        return f >= f2 ? f : f2;
    }

    public static double max(double d, double d2) {
        if (d != d) {
            return d;
        }
        if (d == 0.0d && d2 == 0.0d && Double.doubleToRawLongBits(d) == negativeZeroDoubleBits) {
            return d2;
        }
        return d >= d2 ? d : d2;
    }

    public static float min(float f, float f2) {
        if (f != f) {
            return f;
        }
        if (f == 0.0f && f2 == 0.0f && ((long) Float.floatToRawIntBits(f2)) == negativeZeroFloatBits) {
            return f2;
        }
        return f <= f2 ? f : f2;
    }

    public static double min(double d, double d2) {
        if (d != d) {
            return d;
        }
        if (d == 0.0d && d2 == 0.0d && Double.doubleToRawLongBits(d2) == negativeZeroDoubleBits) {
            return d2;
        }
        return d <= d2 ? d : d2;
    }

    public static double fma(double d, double d2, double d3) {
        if (Double.isNaN(d) || Double.isNaN(d2) || Double.isNaN(d3)) {
            return Double.NaN;
        }
        boolean isInfinite = Double.isInfinite(d);
        boolean isInfinite2 = Double.isInfinite(d2);
        boolean isInfinite3 = Double.isInfinite(d3);
        if (!isInfinite && !isInfinite2 && !isInfinite3) {
            BigDecimal multiply = new BigDecimal(d).multiply(new BigDecimal(d2));
            if (d3 == 0.0d) {
                return (d == 0.0d || d2 == 0.0d) ? (d * d2) + d3 : multiply.doubleValue();
            }
            return multiply.add(new BigDecimal(d3)).doubleValue();
        } else if ((isInfinite && d2 == 0.0d) || (isInfinite2 && d == 0.0d)) {
            return Double.NaN;
        } else {
            double d4 = d * d2;
            return (!Double.isInfinite(d4) || isInfinite || isInfinite2) ? d4 + d3 : d3;
        }
    }

    public static float fma(float f, float f2, float f3) {
        if (!Float.isFinite(f) || !Float.isFinite(f2) || !Float.isFinite(f3)) {
            return (float) fma((double) f, (double) f2, (double) f3);
        }
        double d = (double) f;
        if (d != 0.0d) {
            double d2 = (double) f2;
            if (d2 != 0.0d) {
                return new BigDecimal(d * d2).add(new BigDecimal((double) f3)).floatValue();
            }
        }
        return (f * f2) + f3;
    }

    public static double ulp(double d) {
        int exponent = getExponent(d);
        if (exponent == -1023) {
            return Double.MIN_VALUE;
        }
        if (exponent == 1024) {
            return abs(d);
        }
        int i = exponent - 52;
        if (i >= -1022) {
            return powerOfTwoD(i);
        }
        return Double.longBitsToDouble(1 << (i + 1074));
    }

    public static float ulp(float f) {
        int exponent = getExponent(f);
        if (exponent == -127) {
            return Float.MIN_VALUE;
        }
        if (exponent == 128) {
            return abs(f);
        }
        int i = exponent - 23;
        if (i >= -126) {
            return powerOfTwoF(i);
        }
        return Float.intBitsToFloat(1 << (i + 149));
    }

    public static double signum(double d) {
        return (d == 0.0d || Double.isNaN(d)) ? d : copySign(1.0d, d);
    }

    public static float signum(float f) {
        return (f == 0.0f || Float.isNaN(f)) ? f : copySign(1.0f, f);
    }

    public static double copySign(double d, double d2) {
        return Double.longBitsToDouble((Double.doubleToRawLongBits(d) & Long.MAX_VALUE) | (Double.doubleToRawLongBits(d2) & Long.MIN_VALUE));
    }

    public static float copySign(float f, float f2) {
        return Float.intBitsToFloat((Float.floatToRawIntBits(f) & Integer.MAX_VALUE) | (Float.floatToRawIntBits(f2) & Integer.MIN_VALUE));
    }

    public static int getExponent(float f) {
        return ((Float.floatToRawIntBits(f) & FloatConsts.EXP_BIT_MASK) >> 23) - 127;
    }

    public static int getExponent(double d) {
        return (int) (((Double.doubleToRawLongBits(d) & DoubleConsts.EXP_BIT_MASK) >> 52) - 1023);
    }

    public static double nextAfter(double d, double d2) {
        int i = (d > d2 ? 1 : (d == d2 ? 0 : -1));
        long j = 1;
        if (i > 0) {
            if (d == 0.0d) {
                return -4.9E-324d;
            }
            long doubleToRawLongBits = Double.doubleToRawLongBits(d);
            if (doubleToRawLongBits > 0) {
                j = -1;
            }
            return Double.longBitsToDouble(doubleToRawLongBits + j);
        } else if (d >= d2) {
            return i == 0 ? d2 : d + d2;
        } else {
            long doubleToRawLongBits2 = Double.doubleToRawLongBits(d + 0.0d);
            if (doubleToRawLongBits2 < 0) {
                j = -1;
            }
            return Double.longBitsToDouble(doubleToRawLongBits2 + j);
        }
    }

    public static float nextAfter(float f, double d) {
        double d2 = (double) f;
        int i = (d2 > d ? 1 : (d2 == d ? 0 : -1));
        int i2 = 1;
        if (i > 0) {
            if (f == 0.0f) {
                return -1.4E-45f;
            }
            int floatToRawIntBits = Float.floatToRawIntBits(f);
            if (floatToRawIntBits > 0) {
                i2 = -1;
            }
            return Float.intBitsToFloat(floatToRawIntBits + i2);
        } else if (d2 >= d) {
            return i == 0 ? (float) d : f + ((float) d);
        } else {
            int floatToRawIntBits2 = Float.floatToRawIntBits(f + 0.0f);
            if (floatToRawIntBits2 < 0) {
                i2 = -1;
            }
            return Float.intBitsToFloat(floatToRawIntBits2 + i2);
        }
    }

    public static double nextUp(double d) {
        if (d >= Double.POSITIVE_INFINITY) {
            return d;
        }
        long doubleToRawLongBits = Double.doubleToRawLongBits(d + 0.0d);
        return Double.longBitsToDouble(doubleToRawLongBits + (doubleToRawLongBits >= 0 ? 1 : -1));
    }

    public static float nextUp(float f) {
        if (f >= Float.POSITIVE_INFINITY) {
            return f;
        }
        int floatToRawIntBits = Float.floatToRawIntBits(f + 0.0f);
        return Float.intBitsToFloat(floatToRawIntBits + (floatToRawIntBits >= 0 ? 1 : -1));
    }

    public static double nextDown(double d) {
        if (Double.isNaN(d) || d == Double.NEGATIVE_INFINITY) {
            return d;
        }
        int i = (d > 0.0d ? 1 : (d == 0.0d ? 0 : -1));
        if (i == 0) {
            return -4.9E-324d;
        }
        return Double.longBitsToDouble(Double.doubleToRawLongBits(d) + (i > 0 ? -1 : 1));
    }

    public static float nextDown(float f) {
        if (Float.isNaN(f) || f == Float.NEGATIVE_INFINITY) {
            return f;
        }
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i == 0) {
            return -1.4E-45f;
        }
        return Float.intBitsToFloat(Float.floatToRawIntBits(f) + (i > 0 ? -1 : 1));
    }

    public static double scalb(double d, int i) {
        int i2;
        int i3;
        double d2;
        if (i < 0) {
            i2 = max(i, -2099);
            d2 = twoToTheDoubleScaleDown;
            i3 = -512;
        } else {
            i2 = min(i, 2099);
            d2 = twoToTheDoubleScaleUp;
            i3 = 512;
        }
        int i4 = (i2 >> 8) >>> 23;
        int i5 = ((i2 + i4) & 511) - i4;
        double powerOfTwoD = d * powerOfTwoD(i5);
        for (int i6 = i2 - i5; i6 != 0; i6 -= i3) {
            powerOfTwoD *= d2;
        }
        return powerOfTwoD;
    }

    public static float scalb(float f, int i) {
        return (float) (((double) f) * powerOfTwoD(max(min(i, (int) UCharacter.UnicodeBlock.SOYOMBO_ID), -278)));
    }

    static double powerOfTwoD(int i) {
        return Double.longBitsToDouble(((((long) i) + 1023) << 52) & DoubleConsts.EXP_BIT_MASK);
    }

    static float powerOfTwoF(int i) {
        return Float.intBitsToFloat(((i + 127) << 23) & FloatConsts.EXP_BIT_MASK);
    }
}

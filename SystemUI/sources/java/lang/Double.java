package java.lang;

import jdk.internal.math.DoubleConsts;
import jdk.internal.math.FloatingDecimal;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import sun.util.locale.LanguageTag;

public final class Double extends Number implements Comparable<Double> {
    public static final int BYTES = 8;
    public static final int MAX_EXPONENT = 1023;
    public static final double MAX_VALUE = Double.MAX_VALUE;
    public static final int MIN_EXPONENT = -1022;
    public static final double MIN_NORMAL = Double.MIN_NORMAL;
    public static final double MIN_VALUE = Double.MIN_VALUE;
    public static final double NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
    public static final double NaN = Double.NaN;
    public static final double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    public static final int SIZE = 64;
    public static final Class<Double> TYPE = Class.getPrimitiveClass("double");
    private static final long serialVersionUID = -9172774392245257468L;
    private final double value;

    public static native long doubleToRawLongBits(double d);

    public static boolean isInfinite(double d) {
        return d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY;
    }

    public static boolean isNaN(double d) {
        return d != d;
    }

    public static native double longBitsToDouble(long j);

    public static double sum(double d, double d2) {
        return d + d2;
    }

    public static String toString(double d) {
        return FloatingDecimal.toJavaFormatString(d);
    }

    public static String toHexString(double d) {
        String str;
        int i;
        if (!isFinite(d)) {
            return toString(d);
        }
        StringBuilder sb = new StringBuilder(24);
        if (Math.copySign(1.0d, d) == -1.0d) {
            sb.append(LanguageTag.SEP);
        }
        sb.append("0x");
        double abs = Math.abs(d);
        if (abs == 0.0d) {
            sb.append("0.0p0");
        } else {
            boolean z = abs < Double.MIN_NORMAL;
            long doubleToLongBits = (doubleToLongBits(abs) & DoubleConsts.SIGNIF_BIT_MASK) | LockFreeTaskQueueCore.FROZEN_MASK;
            sb.append(z ? "0." : "1.");
            String substring = Long.toHexString(doubleToLongBits).substring(3, 16);
            if (substring.equals("0000000000000")) {
                str = "0";
            } else {
                str = substring.replaceFirst("0{1,12}$", "");
            }
            sb.append(str);
            sb.append('p');
            if (z) {
                i = -1022;
            } else {
                i = Math.getExponent(abs);
            }
            sb.append(i);
        }
        return sb.toString();
    }

    public static Double valueOf(String str) throws NumberFormatException {
        return new Double(parseDouble(str));
    }

    public static Double valueOf(double d) {
        return new Double(d);
    }

    public static double parseDouble(String str) throws NumberFormatException {
        return FloatingDecimal.parseDouble(str);
    }

    public static boolean isFinite(double d) {
        return Math.abs(d) <= Double.MAX_VALUE;
    }

    @Deprecated(since = "9")
    public Double(double d) {
        this.value = d;
    }

    @Deprecated(since = "9")
    public Double(String str) throws NumberFormatException {
        this.value = parseDouble(str);
    }

    public boolean isNaN() {
        return isNaN(this.value);
    }

    public boolean isInfinite() {
        return isInfinite(this.value);
    }

    public String toString() {
        return toString(this.value);
    }

    public byte byteValue() {
        return (byte) ((int) this.value);
    }

    public short shortValue() {
        return (short) ((int) this.value);
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return (float) this.value;
    }

    public double doubleValue() {
        return this.value;
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public static int hashCode(double d) {
        long doubleToLongBits = doubleToLongBits(d);
        return (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
    }

    public boolean equals(Object obj) {
        return (obj instanceof Double) && doubleToLongBits(((Double) obj).value) == doubleToLongBits(this.value);
    }

    public static long doubleToLongBits(double d) {
        if (!isNaN(d)) {
            return doubleToRawLongBits(d);
        }
        return 9221120237041090560L;
    }

    public int compareTo(Double d) {
        return compare(this.value, d.value);
    }

    public static int compare(double d, double d2) {
        if (d < d2) {
            return -1;
        }
        if (d > d2) {
            return 1;
        }
        int i = (doubleToLongBits(d) > doubleToLongBits(d2) ? 1 : (doubleToLongBits(d) == doubleToLongBits(d2) ? 0 : -1));
        if (i == 0) {
            return 0;
        }
        if (i < 0) {
            return -1;
        }
        return 1;
    }

    public static double max(double d, double d2) {
        return Math.max(d, d2);
    }

    public static double min(double d, double d2) {
        return Math.min(d, d2);
    }
}

package java.lang;

import jdk.internal.math.FloatingDecimal;

public final class Float extends Number implements Comparable<Float> {
    public static final int BYTES = 4;
    public static final int MAX_EXPONENT = 127;
    public static final float MAX_VALUE = Float.MAX_VALUE;
    public static final int MIN_EXPONENT = -126;
    public static final float MIN_NORMAL = Float.MIN_NORMAL;
    public static final float MIN_VALUE = Float.MIN_VALUE;
    public static final float NEGATIVE_INFINITY = Float.NEGATIVE_INFINITY;
    public static final float NaN = Float.NaN;
    public static final float POSITIVE_INFINITY = Float.POSITIVE_INFINITY;
    public static final int SIZE = 32;
    public static final Class<Float> TYPE = Class.getPrimitiveClass("float");
    private static final long serialVersionUID = -2671257302660747028L;
    private final float value;

    public static native int floatToRawIntBits(float f);

    public static native float intBitsToFloat(int i);

    public static boolean isInfinite(float f) {
        return f == Float.POSITIVE_INFINITY || f == Float.NEGATIVE_INFINITY;
    }

    public static boolean isNaN(float f) {
        return f != f;
    }

    public static float sum(float f, float f2) {
        return f + f2;
    }

    public static String toString(float f) {
        return FloatingDecimal.toJavaFormatString(f);
    }

    public static String toHexString(float f) {
        if (Math.abs(f) >= Float.MIN_NORMAL || f == 0.0f) {
            return Double.toHexString((double) f);
        }
        return Double.toHexString(Math.scalb((double) f, -896)).replaceFirst("p-1022$", "p-126");
    }

    public static Float valueOf(String str) throws NumberFormatException {
        return new Float(parseFloat(str));
    }

    public static Float valueOf(float f) {
        return new Float(f);
    }

    public static float parseFloat(String str) throws NumberFormatException {
        return FloatingDecimal.parseFloat(str);
    }

    public static boolean isFinite(float f) {
        return Math.abs(f) <= Float.MAX_VALUE;
    }

    @Deprecated(since = "9")
    public Float(float f) {
        this.value = f;
    }

    @Deprecated(since = "9")
    public Float(double d) {
        this.value = (float) d;
    }

    @Deprecated(since = "9")
    public Float(String str) throws NumberFormatException {
        this.value = parseFloat(str);
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
        return this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public static int hashCode(float f) {
        return floatToIntBits(f);
    }

    public boolean equals(Object obj) {
        return (obj instanceof Float) && floatToIntBits(((Float) obj).value) == floatToIntBits(this.value);
    }

    public static int floatToIntBits(float f) {
        if (!isNaN(f)) {
            return floatToRawIntBits(f);
        }
        return 2143289344;
    }

    public int compareTo(Float f) {
        return compare(this.value, f.value);
    }

    public static int compare(float f, float f2) {
        if (f < f2) {
            return -1;
        }
        if (f > f2) {
            return 1;
        }
        int floatToIntBits = floatToIntBits(f);
        int floatToIntBits2 = floatToIntBits(f2);
        if (floatToIntBits == floatToIntBits2) {
            return 0;
        }
        if (floatToIntBits < floatToIntBits2) {
            return -1;
        }
        return 1;
    }

    public static float max(float f, float f2) {
        return Math.max(f, f2);
    }

    public static float min(float f, float f2) {
        return Math.min(f, f2);
    }
}

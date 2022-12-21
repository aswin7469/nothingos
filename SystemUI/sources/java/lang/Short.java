package java.lang;

import android.net.TrafficStats;
import kotlin.UShort;

public final class Short extends Number implements Comparable<Short> {
    public static final int BYTES = 2;
    public static final short MAX_VALUE = Short.MAX_VALUE;
    public static final short MIN_VALUE = Short.MIN_VALUE;
    public static final int SIZE = 16;
    public static final Class<Short> TYPE = Class.getPrimitiveClass("short");
    private static final long serialVersionUID = 7515723908773894738L;
    private final short value;

    public static int compare(short s, short s2) {
        return s - s2;
    }

    public static int hashCode(short s) {
        return s;
    }

    public static short reverseBytes(short s) {
        return (short) ((s << 8) | ((65280 & s) >> 8));
    }

    public static int toUnsignedInt(short s) {
        return s & UShort.MAX_VALUE;
    }

    public static long toUnsignedLong(short s) {
        return ((long) s) & 65535;
    }

    public static String toString(short s) {
        return Integer.toString(s, 10);
    }

    public static short parseShort(String str, int i) throws NumberFormatException {
        int parseInt = Integer.parseInt(str, i);
        if (parseInt >= -32768 && parseInt <= 32767) {
            return (short) parseInt;
        }
        throw new NumberFormatException("Value out of range. Value:\"" + str + "\" Radix:" + i);
    }

    public static short parseShort(String str) throws NumberFormatException {
        return parseShort(str, 10);
    }

    public static Short valueOf(String str, int i) throws NumberFormatException {
        return valueOf(parseShort(str, i));
    }

    public static Short valueOf(String str) throws NumberFormatException {
        return valueOf(str, 10);
    }

    private static class ShortCache {
        static final Short[] cache = new Short[256];

        private ShortCache() {
        }

        static {
            int i = 0;
            while (true) {
                Short[] shArr = cache;
                if (i < shArr.length) {
                    shArr[i] = new Short((short) (i + TrafficStats.TAG_NETWORK_STACK_IMPERSONATION_RANGE_START));
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public static Short valueOf(short s) {
        if (s < -128 || s > 127) {
            return new Short(s);
        }
        return ShortCache.cache[s + 128];
    }

    public static Short decode(String str) throws NumberFormatException {
        int intValue = Integer.decode(str).intValue();
        if (intValue >= -32768 && intValue <= 32767) {
            return valueOf((short) intValue);
        }
        throw new NumberFormatException("Value " + intValue + " out of range from input " + str);
    }

    @Deprecated(since = "9")
    public Short(short s) {
        this.value = s;
    }

    @Deprecated(since = "9")
    public Short(String str) throws NumberFormatException {
        this.value = parseShort(str, 10);
    }

    public byte byteValue() {
        return (byte) this.value;
    }

    public short shortValue() {
        return this.value;
    }

    public int intValue() {
        return this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return (float) this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public String toString() {
        return Integer.toString(this.value);
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Short) || this.value != ((Short) obj).shortValue()) {
            return false;
        }
        return true;
    }

    public int compareTo(Short sh) {
        return compare(this.value, sh.value);
    }

    public static int compareUnsigned(short s, short s2) {
        return toUnsignedInt(s) - toUnsignedInt(s2);
    }
}

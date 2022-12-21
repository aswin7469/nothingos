package java.lang;

import android.net.TrafficStats;
import libcore.util.HexEncoding;

public final class Byte extends Number implements Comparable<Byte> {
    public static final int BYTES = 1;
    public static final byte MAX_VALUE = Byte.MAX_VALUE;
    public static final byte MIN_VALUE = Byte.MIN_VALUE;
    public static final int SIZE = 8;
    public static final Class<Byte> TYPE = Class.getPrimitiveClass("byte");
    private static final long serialVersionUID = -7183698231559129828L;
    private final byte value;

    public static int compare(byte b, byte b2) {
        return b - b2;
    }

    public static int hashCode(byte b) {
        return b;
    }

    public static int toUnsignedInt(byte b) {
        return b & 255;
    }

    public static long toUnsignedLong(byte b) {
        return ((long) b) & 255;
    }

    public static String toString(byte b) {
        return Integer.toString(b, 10);
    }

    private static class ByteCache {
        static final Byte[] cache = new Byte[256];

        private ByteCache() {
        }

        static {
            int i = 0;
            while (true) {
                Byte[] bArr = cache;
                if (i < bArr.length) {
                    bArr[i] = new Byte((byte) (i + TrafficStats.TAG_NETWORK_STACK_IMPERSONATION_RANGE_START));
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public static Byte valueOf(byte b) {
        return ByteCache.cache[b + 128];
    }

    public static byte parseByte(String str, int i) throws NumberFormatException {
        int parseInt = Integer.parseInt(str, i);
        if (parseInt >= -128 && parseInt <= 127) {
            return (byte) parseInt;
        }
        throw new NumberFormatException("Value out of range. Value:\"" + str + "\" Radix:" + i);
    }

    public static byte parseByte(String str) throws NumberFormatException {
        return parseByte(str, 10);
    }

    public static Byte valueOf(String str, int i) throws NumberFormatException {
        return valueOf(parseByte(str, i));
    }

    public static Byte valueOf(String str) throws NumberFormatException {
        return valueOf(str, 10);
    }

    public static Byte decode(String str) throws NumberFormatException {
        int intValue = Integer.decode(str).intValue();
        if (intValue >= -128 && intValue <= 127) {
            return valueOf((byte) intValue);
        }
        throw new NumberFormatException("Value " + intValue + " out of range from input " + str);
    }

    @Deprecated(since = "9")
    public Byte(byte b) {
        this.value = b;
    }

    @Deprecated(since = "9")
    public Byte(String str) throws NumberFormatException {
        this.value = parseByte(str, 10);
    }

    public byte byteValue() {
        return this.value;
    }

    public short shortValue() {
        return (short) this.value;
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
        if (!(obj instanceof Byte) || this.value != ((Byte) obj).byteValue()) {
            return false;
        }
        return true;
    }

    public int compareTo(Byte b) {
        return compare(this.value, b.value);
    }

    public static int compareUnsigned(byte b, byte b2) {
        return toUnsignedInt(b) - toUnsignedInt(b2);
    }

    public static String toHexString(byte b, boolean z) {
        return HexEncoding.encodeToString(b, z);
    }
}

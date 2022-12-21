package java.lang;

import android.net.TrafficStats;
import java.math.BigInteger;
import java.util.Objects;
import sun.util.locale.LanguageTag;

public final class Long extends Number implements Comparable<Long> {
    public static final int BYTES = 8;
    public static final long MAX_VALUE = Long.MAX_VALUE;
    public static final long MIN_VALUE = Long.MIN_VALUE;
    public static final int SIZE = 64;
    public static final Class<Long> TYPE = Class.getPrimitiveClass("long");
    private static final long serialVersionUID = 4290774380558885855L;
    private final long value;

    public static int bitCount(long j) {
        long j2 = j - ((j >>> 1) & 6148914691236517205L);
        long j3 = (j2 & 3689348814741910323L) + ((j2 >>> 2) & 3689348814741910323L);
        long j4 = 1085102592571150095L & (j3 + (j3 >>> 4));
        long j5 = j4 + (j4 >>> 8);
        long j6 = j5 + (j5 >>> 16);
        return ((int) (j6 + (j6 >>> 32))) & 127;
    }

    public static int compare(long j, long j2) {
        int i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
        if (i < 0) {
            return -1;
        }
        return i == 0 ? 0 : 1;
    }

    public static int hashCode(long j) {
        return (int) (j ^ (j >>> 32));
    }

    public static long lowestOneBit(long j) {
        return j & (-j);
    }

    public static int numberOfTrailingZeros(long j) {
        int i;
        if (j == 0) {
            return 64;
        }
        int i2 = (int) j;
        if (i2 != 0) {
            i = 31;
        } else {
            i2 = (int) (j >>> 32);
            i = 63;
        }
        int i3 = i2 << 16;
        if (i3 != 0) {
            i -= 16;
            i2 = i3;
        }
        int i4 = i2 << 8;
        if (i4 != 0) {
            i -= 8;
            i2 = i4;
        }
        int i5 = i2 << 4;
        if (i5 != 0) {
            i -= 4;
            i2 = i5;
        }
        int i6 = i2 << 2;
        if (i6 != 0) {
            i -= 2;
            i2 = i6;
        }
        return i - ((i2 << 1) >>> 31);
    }

    public static long reverseBytes(long j) {
        long j2 = ((j >>> 8) & 71777214294589695L) | ((j & 71777214294589695L) << 8);
        return (j2 >>> 48) | (j2 << 48) | ((j2 & 4294901760L) << 16) | (4294901760L & (j2 >>> 16));
    }

    public static long rotateLeft(long j, int i) {
        return (j >>> (-i)) | (j << i);
    }

    public static long rotateRight(long j, int i) {
        return (j << (-i)) | (j >>> i);
    }

    public static int signum(long j) {
        return (int) (((-j) >>> 63) | (j >> 63));
    }

    static int stringSize(long j) {
        int i;
        if (j >= 0) {
            j = -j;
            i = 0;
        } else {
            i = 1;
        }
        long j2 = -10;
        for (int i2 = 1; i2 < 19; i2++) {
            if (j > j2) {
                return i2 + i;
            }
            j2 *= 10;
        }
        return i + 19;
    }

    public static long sum(long j, long j2) {
        return j + j2;
    }

    public static String toString(long j, int i) {
        if (i < 2 || i > 36) {
            i = 10;
        }
        if (i == 10) {
            return toString(j);
        }
        byte[] bArr = new byte[65];
        boolean z = j < 0;
        int i2 = 64;
        if (!z) {
            j = -j;
        }
        while (j <= ((long) (-i))) {
            long j2 = (long) i;
            bArr[i2] = (byte) Integer.digits[(int) (-(j % j2))];
            j /= j2;
            i2--;
        }
        bArr[i2] = (byte) Integer.digits[(int) (-j)];
        if (z) {
            i2--;
            bArr[i2] = 45;
        }
        return new String(bArr, i2, 65 - i2);
    }

    public static String toUnsignedString(long j, int i) {
        if (j >= 0) {
            return toString(j, i);
        }
        if (i == 2) {
            return toBinaryString(j);
        }
        if (i == 4) {
            return toUnsignedString0(j, 2);
        }
        if (i == 8) {
            return toOctalString(j);
        }
        if (i == 10) {
            long j2 = (j >>> 1) / 5;
            return toString(j2) + (j - (10 * j2));
        } else if (i == 16) {
            return toHexString(j);
        } else {
            if (i != 32) {
                return toUnsignedBigInteger(j).toString(i);
            }
            return toUnsignedString0(j, 5);
        }
    }

    private static BigInteger toUnsignedBigInteger(long j) {
        if (j >= 0) {
            return BigInteger.valueOf(j);
        }
        return BigInteger.valueOf(Integer.toUnsignedLong((int) (j >>> 32))).shiftLeft(32).add(BigInteger.valueOf(Integer.toUnsignedLong((int) j)));
    }

    public static String toHexString(long j) {
        return toUnsignedString0(j, 4);
    }

    public static String toOctalString(long j) {
        return toUnsignedString0(j, 3);
    }

    public static String toBinaryString(long j) {
        return toUnsignedString0(j, 1);
    }

    static String toUnsignedString0(long j, int i) {
        int max = Math.max(((64 - numberOfLeadingZeros(j)) + (i - 1)) / i, 1);
        byte[] bArr = new byte[max];
        formatUnsignedLong0(j, i, bArr, 0, max);
        return new String(bArr);
    }

    static void formatUnsignedLong0(long j, int i, byte[] bArr, int i2, int i3) {
        int i4 = i3 + i2;
        int i5 = (1 << i) - 1;
        do {
            i4--;
            bArr[i4] = (byte) Integer.digits[((int) j) & i5];
            j >>>= i;
        } while (i4 > i2);
    }

    public static String toString(long j) {
        int stringSize = stringSize(j);
        byte[] bArr = new byte[stringSize];
        getChars(j, stringSize, bArr);
        return new String(bArr);
    }

    public static String toUnsignedString(long j) {
        return toUnsignedString(j, 10);
    }

    static int getChars(long j, int i, byte[] bArr) {
        boolean z = j < 0;
        if (!z) {
            j = -j;
        }
        while (j <= -2147483648L) {
            long j2 = j / 100;
            int i2 = (int) ((100 * j2) - j);
            int i3 = i - 1;
            bArr[i3] = Integer.DigitOnes[i2];
            i = i3 - 1;
            bArr[i] = Integer.DigitTens[i2];
            j = j2;
        }
        int i4 = (int) j;
        while (i4 <= -100) {
            int i5 = i4 / 100;
            int i6 = (i5 * 100) - i4;
            int i7 = i - 1;
            bArr[i7] = Integer.DigitOnes[i6];
            i = i7 - 1;
            bArr[i] = Integer.DigitTens[i6];
            i4 = i5;
        }
        int i8 = i4 / 10;
        int i9 = i - 1;
        bArr[i9] = (byte) (((i8 * 10) - i4) + 48);
        if (i8 < 0) {
            i9--;
            bArr[i9] = (byte) (48 - i8);
        }
        if (!z) {
            return i9;
        }
        int i10 = i9 - 1;
        bArr[i10] = 45;
        return i10;
    }

    static int getChars(long j, int i, char[] cArr) {
        boolean z = j < 0;
        if (!z) {
            j = -j;
        }
        while (j <= -2147483648L) {
            long j2 = j / 100;
            int i2 = (int) ((100 * j2) - j);
            int i3 = i - 1;
            cArr[i3] = (char) Integer.DigitOnes[i2];
            i = i3 - 1;
            cArr[i] = (char) Integer.DigitTens[i2];
            j = j2;
        }
        int i4 = (int) j;
        while (i4 <= -100) {
            int i5 = i4 / 100;
            int i6 = (i5 * 100) - i4;
            int i7 = i - 1;
            cArr[i7] = (char) Integer.DigitOnes[i6];
            i = i7 - 1;
            cArr[i] = (char) Integer.DigitTens[i6];
            i4 = i5;
        }
        int i8 = i4 / 10;
        int i9 = i - 1;
        cArr[i9] = (char) (((i8 * 10) - i4) + 48);
        if (i8 < 0) {
            i9--;
            cArr[i9] = (char) (48 - i8);
        }
        if (!z) {
            return i9;
        }
        int i10 = i9 - 1;
        cArr[i10] = '-';
        return i10;
    }

    public static long parseLong(String str, int i) throws NumberFormatException {
        int i2;
        String str2 = str;
        int i3 = i;
        if (str2 == null) {
            throw new NumberFormatException("null");
        } else if (i3 < 2) {
            throw new NumberFormatException("radix " + i3 + " less than Character.MIN_RADIX");
        } else if (i3 <= 36) {
            int length = str.length();
            if (length > 0) {
                boolean z = false;
                char charAt = str2.charAt(0);
                long j = -9223372036854775807L;
                if (charAt < '0') {
                    i2 = 1;
                    if (charAt == '-') {
                        j = Long.MIN_VALUE;
                        z = true;
                    } else if (charAt != '+') {
                        throw NumberFormatException.forInputString(str);
                    }
                    if (length == 1) {
                        throw NumberFormatException.forInputString(str);
                    }
                } else {
                    i2 = 0;
                }
                long j2 = (long) i3;
                long j3 = j / j2;
                long j4 = 0;
                while (i2 < length) {
                    int i4 = i2 + 1;
                    int digit = Character.digit(str2.charAt(i2), i3);
                    if (digit < 0 || j4 < j3) {
                        throw NumberFormatException.forInputString(str);
                    }
                    long j5 = j4 * j2;
                    long j6 = (long) digit;
                    if (j5 >= j + j6) {
                        j4 = j5 - j6;
                        i2 = i4;
                    } else {
                        throw NumberFormatException.forInputString(str);
                    }
                }
                return z ? j4 : -j4;
            }
            throw NumberFormatException.forInputString(str);
        } else {
            throw new NumberFormatException("radix " + i3 + " greater than Character.MAX_RADIX");
        }
    }

    public static long parseLong(CharSequence charSequence, int i, int i2, int i3) throws NumberFormatException {
        int i4;
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        CharSequence charSequence2 = (CharSequence) Objects.requireNonNull(charSequence);
        if (i5 < 0 || i5 > i6 || i6 > charSequence2.length()) {
            throw new IndexOutOfBoundsException();
        } else if (i7 < 2) {
            throw new NumberFormatException("radix " + i7 + " less than Character.MIN_RADIX");
        } else if (i7 > 36) {
            throw new NumberFormatException("radix " + i7 + " greater than Character.MAX_RADIX");
        } else if (i5 < i6) {
            char charAt = charSequence2.charAt(i5);
            boolean z = false;
            long j = -9223372036854775807L;
            if (charAt < '0') {
                if (charAt == '-') {
                    z = true;
                    j = Long.MIN_VALUE;
                } else if (charAt != '+') {
                    throw NumberFormatException.forCharSequence(charSequence2, i5, i6, i5);
                }
                i4 = i5 + 1;
            } else {
                i4 = i5;
            }
            if (i4 < i6) {
                long j2 = (long) i7;
                long j3 = j / j2;
                long j4 = 0;
                while (i4 < i6) {
                    int digit = Character.digit(charSequence2.charAt(i4), i7);
                    if (digit < 0 || j4 < j3) {
                        throw NumberFormatException.forCharSequence(charSequence2, i5, i6, i4);
                    }
                    long j5 = j4 * j2;
                    long j6 = j2;
                    long j7 = (long) digit;
                    if (j5 >= j + j7) {
                        i4++;
                        j4 = j5 - j7;
                        j2 = j6;
                    } else {
                        throw NumberFormatException.forCharSequence(charSequence2, i5, i6, i4);
                    }
                }
                return z ? j4 : -j4;
            }
            throw NumberFormatException.forCharSequence(charSequence2, i5, i6, i4);
        } else {
            throw new NumberFormatException("");
        }
    }

    public static long parseLong(String str) throws NumberFormatException {
        return parseLong(str, 10);
    }

    public static long parseUnsignedLong(String str, int i) throws NumberFormatException {
        if (str != null) {
            int length = str.length();
            if (length <= 0) {
                throw NumberFormatException.forInputString(str);
            } else if (str.charAt(0) == '-') {
                throw new NumberFormatException(String.format("Illegal leading minus sign on unsigned string %s.", str));
            } else if (length <= 12 || (i == 10 && length <= 18)) {
                return parseLong(str, i);
            } else {
                int i2 = length - 1;
                long parseLong = parseLong(str, 0, i2, i);
                int digit = Character.digit(str.charAt(i2), i);
                if (digit >= 0) {
                    long j = (((long) i) * parseLong) + ((long) digit);
                    int i3 = i * ((int) (parseLong >>> 57));
                    if (i3 < 128 && (j < 0 || i3 < 92)) {
                        return j;
                    }
                    throw new NumberFormatException(String.format("String value %s exceeds range of unsigned long.", str));
                }
                throw new NumberFormatException("Bad digit at end of " + str);
            }
        } else {
            throw new NumberFormatException("null");
        }
    }

    public static long parseUnsignedLong(CharSequence charSequence, int i, int i2, int i3) throws NumberFormatException {
        CharSequence charSequence2 = (CharSequence) Objects.requireNonNull(charSequence);
        if (i < 0 || i > i2 || i2 > charSequence2.length()) {
            throw new IndexOutOfBoundsException();
        }
        int i4 = i2 - i;
        if (i4 <= 0) {
            throw NumberFormatException.forInputString("");
        } else if (charSequence2.charAt(i) == '-') {
            throw new NumberFormatException(String.format("Illegal leading minus sign on unsigned string %s.", charSequence2.subSequence(i, i4 + i)));
        } else if (i4 <= 12 || (i3 == 10 && i4 <= 18)) {
            return parseLong(charSequence2, i, i4 + i, i3);
        } else {
            int i5 = i4 + i;
            int i6 = i5 - 1;
            long parseLong = parseLong(charSequence2, i, i6, i3);
            int digit = Character.digit(charSequence2.charAt(i6), i3);
            if (digit >= 0) {
                long j = (((long) i3) * parseLong) + ((long) digit);
                int i7 = i3 * ((int) (parseLong >>> 57));
                if (i7 < 128 && (j < 0 || i7 < 92)) {
                    return j;
                }
                throw new NumberFormatException(String.format("String value %s exceeds range of unsigned long.", charSequence2.subSequence(i, i5)));
            }
            throw new NumberFormatException("Bad digit at end of " + charSequence2.subSequence(i, i5));
        }
    }

    public static long parseUnsignedLong(String str) throws NumberFormatException {
        return parseUnsignedLong(str, 10);
    }

    public static Long valueOf(String str, int i) throws NumberFormatException {
        return valueOf(parseLong(str, i));
    }

    public static Long valueOf(String str) throws NumberFormatException {
        return valueOf(parseLong(str, 10));
    }

    private static class LongCache {
        static final Long[] cache = new Long[256];

        private LongCache() {
        }

        static {
            int i = 0;
            while (true) {
                Long[] lArr = cache;
                if (i < lArr.length) {
                    lArr[i] = new Long((long) (i + TrafficStats.TAG_NETWORK_STACK_IMPERSONATION_RANGE_START));
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public static Long valueOf(long j) {
        if (j < -128 || j > 127) {
            return new Long(j);
        }
        return LongCache.cache[((int) j) + 128];
    }

    public static Long decode(String str) throws NumberFormatException {
        String str2;
        int i;
        if (!str.isEmpty()) {
            int i2 = 0;
            char charAt = str.charAt(0);
            boolean z = true;
            if (charAt == '-') {
                i2 = 1;
            } else if (charAt == '+') {
                z = false;
                i2 = 1;
            } else {
                z = false;
            }
            int i3 = 16;
            if (str.startsWith("0x", i2) || str.startsWith("0X", i2)) {
                i2 += 2;
            } else if (str.startsWith("#", i2)) {
                i2++;
            } else if (!str.startsWith("0", i2) || str.length() <= (i = i2 + 1)) {
                i3 = 10;
            } else {
                int i4 = i;
                i3 = 8;
                i2 = i4;
            }
            if (str.startsWith(LanguageTag.SEP, i2) || str.startsWith("+", i2)) {
                throw new NumberFormatException("Sign character in wrong position");
            }
            try {
                Long valueOf = valueOf(str.substring(i2), i3);
                if (z) {
                    return valueOf(-valueOf.longValue());
                }
                return valueOf;
            } catch (NumberFormatException unused) {
                if (z) {
                    str2 = LanguageTag.SEP + str.substring(i2);
                } else {
                    str2 = str.substring(i2);
                }
                return valueOf(str2, i3);
            }
        } else {
            throw new NumberFormatException("Zero length string");
        }
    }

    @Deprecated(since = "9")
    public Long(long j) {
        this.value = j;
    }

    @Deprecated(since = "9")
    public Long(String str) throws NumberFormatException {
        this.value = parseLong(str, 10);
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
        return this.value;
    }

    public float floatValue() {
        return (float) this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public String toString() {
        return toString(this.value);
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Long) || this.value != ((Long) obj).longValue()) {
            return false;
        }
        return true;
    }

    public static Long getLong(String str) {
        return getLong(str, (Long) null);
    }

    public static Long getLong(String str, long j) {
        Long l = getLong(str, (Long) null);
        return l == null ? valueOf(j) : l;
    }

    public static Long getLong(String str, Long l) {
        String str2;
        try {
            str2 = System.getProperty(str);
        } catch (IllegalArgumentException | NullPointerException unused) {
            str2 = null;
        }
        if (str2 != null) {
            try {
                return decode(str2);
            } catch (NumberFormatException unused2) {
            }
        }
        return l;
    }

    public int compareTo(Long l) {
        return compare(this.value, l.value);
    }

    public static int compareUnsigned(long j, long j2) {
        return compare(j - Long.MIN_VALUE, j2 - Long.MIN_VALUE);
    }

    public static long divideUnsigned(long j, long j2) {
        if (j2 < 0) {
            return compareUnsigned(j, j2) < 0 ? 0 : 1;
        }
        if (j > 0) {
            return j / j2;
        }
        return toUnsignedBigInteger(j).divide(toUnsignedBigInteger(j2)).longValue();
    }

    public static long remainderUnsigned(long j, long j2) {
        if (j > 0 && j2 > 0) {
            return j % j2;
        }
        if (compareUnsigned(j, j2) < 0) {
            return j;
        }
        return toUnsignedBigInteger(j).remainder(toUnsignedBigInteger(j2)).longValue();
    }

    public static long highestOneBit(long j) {
        return j & (-9223372036854775808 >>> numberOfLeadingZeros(j));
    }

    public static int numberOfLeadingZeros(long j) {
        int i = (int) (j >>> 32);
        if (i == 0) {
            return Integer.numberOfLeadingZeros((int) j) + 32;
        }
        return Integer.numberOfLeadingZeros(i);
    }

    public static long reverse(long j) {
        long j2 = ((j >>> 1) & 6148914691236517205L) | ((j & 6148914691236517205L) << 1);
        long j3 = ((j2 >>> 2) & 3689348814741910323L) | ((j2 & 3689348814741910323L) << 2);
        return reverseBytes(((j3 >>> 4) & 1085102592571150095L) | ((j3 & 1085102592571150095L) << 4));
    }

    public static long max(long j, long j2) {
        return Math.max(j, j2);
    }

    public static long min(long j, long j2) {
        return Math.min(j, j2);
    }
}

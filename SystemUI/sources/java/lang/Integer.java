package java.lang;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import androidx.core.view.MotionEventCompat;
import java.time.Year;
import java.util.Objects;
import jdk.internal.misc.C4593VM;
import sun.util.locale.LanguageTag;

public final class Integer extends Number implements Comparable<Integer> {
    public static final int BYTES = 4;
    static final byte[] DigitOnes = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
    static final byte[] DigitTens = {48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57};
    public static final int MAX_VALUE = Integer.MAX_VALUE;
    public static final int MIN_VALUE = Integer.MIN_VALUE;
    public static final int SIZE = 32;
    private static final String[] SMALL_NEG_VALUES = new String[100];
    private static final String[] SMALL_NONNEG_VALUES = new String[100];
    public static final Class<Integer> TYPE = Class.getPrimitiveClass("int");
    static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final long serialVersionUID = 1360826667806852920L;
    static final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, Year.MAX_VALUE, Integer.MAX_VALUE};
    private final int value;

    public static int bitCount(int i) {
        int i2 = i - ((i >>> 1) & 1431655765);
        int i3 = (i2 & 858993459) + ((i2 >>> 2) & 858993459);
        int i4 = 252645135 & (i3 + (i3 >>> 4));
        int i5 = i4 + (i4 >>> 8);
        return (i5 + (i5 >>> 16)) & 63;
    }

    public static int compare(int i, int i2) {
        if (i < i2) {
            return -1;
        }
        return i == i2 ? 0 : 1;
    }

    public static int hashCode(int i) {
        return i;
    }

    public static int lowestOneBit(int i) {
        return i & (-i);
    }

    public static int numberOfLeadingZeros(int i) {
        int i2;
        if (i <= 0) {
            return i == 0 ? 32 : 0;
        }
        if (i >= 65536) {
            i >>>= 16;
            i2 = 15;
        } else {
            i2 = 31;
        }
        if (i >= 256) {
            i2 -= 8;
            i >>>= 8;
        }
        if (i >= 16) {
            i2 -= 4;
            i >>>= 4;
        }
        if (i >= 4) {
            i2 -= 2;
            i >>>= 2;
        }
        return i2 - (i >>> 1);
    }

    public static int numberOfTrailingZeros(int i) {
        int i2;
        if (i == 0) {
            return 32;
        }
        int i3 = i << 16;
        if (i3 != 0) {
            int i4 = i3;
            i2 = 15;
            i = i4;
        } else {
            i2 = 31;
        }
        int i5 = i << 8;
        if (i5 != 0) {
            i2 -= 8;
            i = i5;
        }
        int i6 = i << 4;
        if (i6 != 0) {
            i2 -= 4;
            i = i6;
        }
        int i7 = i << 2;
        if (i7 != 0) {
            i2 -= 2;
            i = i7;
        }
        return i2 - ((i << 1) >>> 31);
    }

    public static int reverseBytes(int i) {
        return (i >>> 24) | (i << 24) | ((i & MotionEventCompat.ACTION_POINTER_INDEX_MASK) << 8) | (65280 & (i >>> 8));
    }

    public static int rotateLeft(int i, int i2) {
        return (i >>> (-i2)) | (i << i2);
    }

    public static int rotateRight(int i, int i2) {
        return (i << (-i2)) | (i >>> i2);
    }

    public static int signum(int i) {
        return ((-i) >>> 31) | (i >> 31);
    }

    static int stringSize(int i) {
        int i2;
        if (i >= 0) {
            i = -i;
            i2 = 0;
        } else {
            i2 = 1;
        }
        int i3 = -10;
        for (int i4 = 1; i4 < 10; i4++) {
            if (i > i3) {
                return i4 + i2;
            }
            i3 *= 10;
        }
        return i2 + 10;
    }

    public static int sum(int i, int i2) {
        return i + i2;
    }

    public static long toUnsignedLong(int i) {
        return ((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
    }

    public static String toString(int i, int i2) {
        if (i2 < 2 || i2 > 36) {
            i2 = 10;
        }
        if (i2 == 10) {
            return toString(i);
        }
        byte[] bArr = new byte[33];
        boolean z = i < 0;
        int i3 = 32;
        if (!z) {
            i = -i;
        }
        while (i <= (-i2)) {
            bArr[i3] = (byte) digits[-(i % i2)];
            i /= i2;
            i3--;
        }
        bArr[i3] = (byte) digits[-i];
        if (z) {
            i3--;
            bArr[i3] = 45;
        }
        return new String(bArr, i3, 33 - i3);
    }

    public static String toUnsignedString(int i, int i2) {
        return Long.toUnsignedString(toUnsignedLong(i), i2);
    }

    public static String toHexString(int i) {
        return toUnsignedString0(i, 4);
    }

    public static String toOctalString(int i) {
        return toUnsignedString0(i, 3);
    }

    public static String toBinaryString(int i) {
        return toUnsignedString0(i, 1);
    }

    private static String toUnsignedString0(int i, int i2) {
        int max = Math.max(((32 - numberOfLeadingZeros(i)) + (i2 - 1)) / i2, 1);
        byte[] bArr = new byte[max];
        formatUnsignedInt(i, i2, bArr, 0, max);
        return new String(bArr);
    }

    static void formatUnsignedInt(int i, int i2, char[] cArr, int i3, int i4) {
        int i5 = i4 + i3;
        int i6 = (1 << i2) - 1;
        do {
            i5--;
            cArr[i5] = digits[i & i6];
            i >>>= i2;
        } while (i5 > i3);
    }

    static void formatUnsignedInt(int i, int i2, byte[] bArr, int i3, int i4) {
        int i5 = i4 + i3;
        int i6 = (1 << i2) - 1;
        do {
            i5--;
            bArr[i5] = (byte) digits[i & i6];
            i >>>= i2;
        } while (i5 > i3);
    }

    public static String toString(int i) {
        String str;
        String str2;
        boolean z = i < 0;
        if (!z ? i < 100 : i > -100) {
            String[] strArr = z ? SMALL_NEG_VALUES : SMALL_NONNEG_VALUES;
            if (z) {
                i = -i;
                if (strArr[i] == null) {
                    if (i < 10) {
                        str2 = new String(new byte[]{45, DigitOnes[i]});
                    } else {
                        str2 = new String(new byte[]{45, DigitTens[i], DigitOnes[i]});
                    }
                    strArr[i] = str2;
                }
            } else if (strArr[i] == null) {
                if (i < 10) {
                    str = new String(new byte[]{DigitOnes[i]});
                } else {
                    str = new String(new byte[]{DigitTens[i], DigitOnes[i]});
                }
                strArr[i] = str;
            }
            return strArr[i];
        }
        int stringSize = stringSize(i);
        byte[] bArr = new byte[stringSize];
        getChars(i, stringSize, bArr);
        return new String(bArr);
    }

    public static String toUnsignedString(int i) {
        return Long.toString(toUnsignedLong(i));
    }

    static int getChars(int i, int i2, byte[] bArr) {
        boolean z = i < 0;
        if (!z) {
            i = -i;
        }
        while (i <= -100) {
            int i3 = i / 100;
            int i4 = (i3 * 100) - i;
            int i5 = i2 - 1;
            bArr[i5] = DigitOnes[i4];
            i2 = i5 - 1;
            bArr[i2] = DigitTens[i4];
            i = i3;
        }
        int i6 = i / 10;
        int i7 = i2 - 1;
        bArr[i7] = (byte) (((i6 * 10) - i) + 48);
        if (i6 < 0) {
            i7--;
            bArr[i7] = (byte) (48 - i6);
        }
        if (!z) {
            return i7;
        }
        int i8 = i7 - 1;
        bArr[i8] = 45;
        return i8;
    }

    static int getChars(int i, int i2, char[] cArr) {
        boolean z = i < 0;
        if (!z) {
            i = -i;
        }
        while (i <= -100) {
            int i3 = i / 100;
            int i4 = (i3 * 100) - i;
            int i5 = i2 - 1;
            cArr[i5] = (char) DigitOnes[i4];
            i2 = i5 - 1;
            cArr[i2] = (char) DigitTens[i4];
            i = i3;
        }
        int i6 = i / 10;
        int i7 = i2 - 1;
        cArr[i7] = (char) (((i6 * 10) - i) + 48);
        if (i6 < 0) {
            i7--;
            cArr[i7] = (char) (48 - i6);
        }
        if (!z) {
            return i7;
        }
        int i8 = i7 - 1;
        cArr[i8] = '-';
        return i8;
    }

    public static int parseInt(String str, int i) throws NumberFormatException {
        int i2;
        boolean z;
        if (str == null) {
            throw new NumberFormatException("s == null");
        } else if (i < 2) {
            throw new NumberFormatException("radix " + i + " less than Character.MIN_RADIX");
        } else if (i <= 36) {
            int length = str.length();
            if (length > 0) {
                int i3 = 0;
                char charAt = str.charAt(0);
                int i4 = -2147483647;
                if (charAt < '0') {
                    i2 = 1;
                    if (charAt == '-') {
                        i4 = Integer.MIN_VALUE;
                        z = true;
                    } else if (charAt == '+') {
                        z = false;
                    } else {
                        throw NumberFormatException.forInputString(str);
                    }
                    if (length == 1) {
                        throw NumberFormatException.forInputString(str);
                    }
                } else {
                    z = false;
                    i2 = 0;
                }
                int i5 = i4 / i;
                while (i2 < length) {
                    int i6 = i2 + 1;
                    int digit = Character.digit(str.charAt(i2), i);
                    if (digit < 0 || i3 < i5) {
                        throw NumberFormatException.forInputString(str);
                    }
                    int i7 = i3 * i;
                    if (i7 >= i4 + digit) {
                        i3 = i7 - digit;
                        i2 = i6;
                    } else {
                        throw NumberFormatException.forInputString(str);
                    }
                }
                return z ? i3 : -i3;
            }
            throw NumberFormatException.forInputString(str);
        } else {
            throw new NumberFormatException("radix " + i + " greater than Character.MAX_RADIX");
        }
    }

    public static int parseInt(CharSequence charSequence, int i, int i2, int i3) throws NumberFormatException {
        int i4;
        boolean z;
        CharSequence charSequence2 = (CharSequence) Objects.requireNonNull(charSequence);
        if (i < 0 || i > i2 || i2 > charSequence2.length()) {
            throw new IndexOutOfBoundsException();
        } else if (i3 < 2) {
            throw new NumberFormatException("radix " + i3 + " less than Character.MIN_RADIX");
        } else if (i3 > 36) {
            throw new NumberFormatException("radix " + i3 + " greater than Character.MAX_RADIX");
        } else if (i < i2) {
            char charAt = charSequence2.charAt(i);
            int i5 = 0;
            int i6 = -2147483647;
            if (charAt < '0') {
                if (charAt == '-') {
                    z = true;
                    i6 = Integer.MIN_VALUE;
                } else if (charAt == '+') {
                    z = false;
                } else {
                    throw NumberFormatException.forCharSequence(charSequence2, i, i2, i);
                }
                i4 = i + 1;
                if (i4 == i2) {
                    throw NumberFormatException.forCharSequence(charSequence2, i, i2, i4);
                }
            } else {
                i4 = i;
                z = false;
            }
            int i7 = i6 / i3;
            while (i4 < i2) {
                int digit = Character.digit(charSequence2.charAt(i4), i3);
                if (digit < 0 || i5 < i7) {
                    throw NumberFormatException.forCharSequence(charSequence2, i, i2, i4);
                }
                int i8 = i5 * i3;
                if (i8 >= i6 + digit) {
                    i4++;
                    i5 = i8 - digit;
                } else {
                    throw NumberFormatException.forCharSequence(charSequence2, i, i2, i4);
                }
            }
            return z ? i5 : -i5;
        } else {
            throw NumberFormatException.forInputString("");
        }
    }

    public static int parseInt(String str) throws NumberFormatException {
        return parseInt(str, 10);
    }

    public static int parseUnsignedInt(String str, int i) throws NumberFormatException {
        if (str != null) {
            int length = str.length();
            if (length <= 0) {
                throw NumberFormatException.forInputString(str);
            } else if (str.charAt(0) == '-') {
                throw new NumberFormatException(String.format("Illegal leading minus sign on unsigned string %s.", str));
            } else if (length <= 5 || (i == 10 && length <= 9)) {
                return parseInt(str, i);
            } else {
                long parseLong = Long.parseLong(str, i);
                if ((-4294967296L & parseLong) == 0) {
                    return (int) parseLong;
                }
                throw new NumberFormatException(String.format("String value %s exceeds range of unsigned int.", str));
            }
        } else {
            throw new NumberFormatException("null");
        }
    }

    public static int parseUnsignedInt(CharSequence charSequence, int i, int i2, int i3) throws NumberFormatException {
        CharSequence charSequence2 = (CharSequence) Objects.requireNonNull(charSequence);
        if (i < 0 || i > i2 || i2 > charSequence2.length()) {
            throw new IndexOutOfBoundsException();
        }
        int i4 = i2 - i;
        if (i4 <= 0) {
            throw new NumberFormatException("");
        } else if (charSequence2.charAt(i) == '-') {
            throw new NumberFormatException(String.format("Illegal leading minus sign on unsigned string %s.", charSequence2));
        } else if (i4 <= 5 || (i3 == 10 && i4 <= 9)) {
            return parseInt(charSequence2, i, i4 + i, i3);
        } else {
            long parseLong = Long.parseLong(charSequence2, i, i4 + i, i3);
            if ((-4294967296L & parseLong) == 0) {
                return (int) parseLong;
            }
            throw new NumberFormatException(String.format("String value %s exceeds range of unsigned int.", charSequence2));
        }
    }

    public static int parseUnsignedInt(String str) throws NumberFormatException {
        return parseUnsignedInt(str, 10);
    }

    public static Integer valueOf(String str, int i) throws NumberFormatException {
        return valueOf(parseInt(str, i));
    }

    public static Integer valueOf(String str) throws NumberFormatException {
        return valueOf(parseInt(str, 10));
    }

    private static class IntegerCache {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        static Integer[] archivedCache = null;
        static final Integer[] cache = archivedCache;
        static final int high;
        static final int low = -128;

        static {
            Class<Integer> cls = Integer.class;
            String savedProperty = C4593VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
            int i = 127;
            if (savedProperty != null) {
                try {
                    i = Math.min(Math.max(Integer.parseInt(savedProperty), 127), 2147483518);
                } catch (NumberFormatException unused) {
                }
            }
            high = i;
            int i2 = -128;
            int i3 = (i - -128) + 1;
            Integer[] numArr = archivedCache;
            if (numArr == null || i3 > numArr.length) {
                Integer[] numArr2 = new Integer[i3];
                int i4 = 0;
                while (i4 < i3) {
                    numArr2[i4] = new Integer(i2);
                    i4++;
                    i2++;
                }
                archivedCache = numArr2;
            }
        }

        private IntegerCache() {
        }
    }

    public static Integer valueOf(int i) {
        if (i < -128 || i > IntegerCache.high) {
            return new Integer(i);
        }
        return IntegerCache.cache[i + 128];
    }

    @Deprecated(since = "9")
    public Integer(int i) {
        this.value = i;
    }

    @Deprecated(since = "9")
    public Integer(String str) throws NumberFormatException {
        this.value = parseInt(str, 10);
    }

    public byte byteValue() {
        return (byte) this.value;
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
        return toString(this.value);
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Integer) || this.value != ((Integer) obj).intValue()) {
            return false;
        }
        return true;
    }

    public static Integer getInteger(String str) {
        return getInteger(str, (Integer) null);
    }

    public static Integer getInteger(String str, int i) {
        Integer integer = getInteger(str, (Integer) null);
        return integer == null ? valueOf(i) : integer;
    }

    public static Integer getInteger(String str, Integer num) {
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
        return num;
    }

    public static Integer decode(String str) throws NumberFormatException {
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
                Integer valueOf = valueOf(str.substring(i2), i3);
                if (z) {
                    return valueOf(-valueOf.intValue());
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

    public int compareTo(Integer num) {
        return compare(this.value, num.value);
    }

    public static int compareUnsigned(int i, int i2) {
        return compare(i - 2147483648, i2 - 2147483648);
    }

    public static int divideUnsigned(int i, int i2) {
        return (int) (toUnsignedLong(i) / toUnsignedLong(i2));
    }

    public static int remainderUnsigned(int i, int i2) {
        return (int) (toUnsignedLong(i) % toUnsignedLong(i2));
    }

    public static int highestOneBit(int i) {
        return i & (Integer.MIN_VALUE >>> numberOfLeadingZeros(i));
    }

    public static int reverse(int i) {
        int i2 = ((i >>> 1) & 1431655765) | ((i & 1431655765) << 1);
        int i3 = ((i2 >>> 2) & 858993459) | ((i2 & 858993459) << 2);
        return reverseBytes(((i3 >>> 4) & 252645135) | ((i3 & 252645135) << 4));
    }

    public static int max(int i, int i2) {
        return Math.max(i, i2);
    }

    public static int min(int i, int i2) {
        return Math.min(i, i2);
    }
}

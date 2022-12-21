package libcore.util;

import android.annotation.SystemApi;
import kotlin.UShort;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class FP16 {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short EPSILON = 5120;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int EXPONENT_BIAS = 15;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int EXPONENT_SHIFT = 10;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int EXPONENT_SIGNIFICAND_MASK = 32767;
    private static final float FP32_DENORMAL_FLOAT = Float.intBitsToFloat(FP32_DENORMAL_MAGIC);
    private static final int FP32_DENORMAL_MAGIC = 1056964608;
    private static final int FP32_EXPONENT_BIAS = 127;
    private static final int FP32_EXPONENT_SHIFT = 23;
    private static final int FP32_QNAN_MASK = 4194304;
    private static final int FP32_SHIFTED_EXPONENT_MASK = 255;
    private static final int FP32_SIGNIFICAND_MASK = 8388607;
    private static final int FP32_SIGN_SHIFT = 31;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short LOWEST_VALUE = -1025;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int MAX_EXPONENT = 15;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short MAX_VALUE = 31743;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int MIN_EXPONENT = -14;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short MIN_NORMAL = 1024;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short MIN_VALUE = 1;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short NEGATIVE_INFINITY = -1024;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short NEGATIVE_ZERO = Short.MIN_VALUE;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short NaN = 32256;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short POSITIVE_INFINITY = 31744;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final short POSITIVE_ZERO = 0;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int SHIFTED_EXPONENT_MASK = 31;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int SIGNIFICAND_MASK = 1023;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int SIGN_MASK = 32768;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int SIGN_SHIFT = 15;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int SIZE = 16;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean isInfinite(short s) {
        return (s & Short.MAX_VALUE) == 31744;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean isNaN(short s) {
        return (s & Short.MAX_VALUE) > 31744;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean isNormalized(short s) {
        short s2 = s & POSITIVE_INFINITY;
        return (s2 == 0 || s2 == 31744) ? false : true;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static short trunc(short s) {
        int i;
        short s2 = s & UShort.MAX_VALUE;
        short s3 = s2 & Short.MAX_VALUE;
        if (s3 < 15360) {
            i = 32768;
        } else {
            if (s3 < 25600) {
                i = ~((1 << (25 - (s3 >> 10))) - 1);
            }
            return (short) s2;
        }
        s2 &= i;
        return (short) s2;
    }

    private FP16() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int compare(short s, short s2) {
        if (less(s, s2)) {
            return -1;
        }
        if (greater(s, s2)) {
            return 1;
        }
        if (isNaN(s)) {
            s = 32256;
        }
        if (isNaN(s2)) {
            s2 = 32256;
        }
        if (s == s2) {
            return 0;
        }
        if (s < s2) {
            return -1;
        }
        return 1;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static short rint(short s) {
        short s2 = s & UShort.MAX_VALUE;
        short s3 = s2 & Short.MAX_VALUE;
        if (s3 < 15360) {
            s2 &= 32768;
            if (s3 > 14336) {
                s2 |= 15360;
            }
        } else if (s3 < 25600) {
            int i = 25 - (s3 >> 10);
            s2 = (s2 + ((1 << (i - 1)) - ((~(s3 >> i)) & 1))) & (~((1 << i) - 1));
        }
        if (isNaN((short) s2)) {
            s2 |= NaN;
        }
        return (short) s2;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static short ceil(short s) {
        short s2 = s & UShort.MAX_VALUE;
        short s3 = s2 & Short.MAX_VALUE;
        int i = 1;
        if (s3 < 15360) {
            short s4 = 32768 & s2;
            int i2 = ~(s2 >> 15);
            if (s3 == 0) {
                i = 0;
            }
            s2 = ((-(i2 & i)) & 15360) | s4;
        } else if (s3 < 25600) {
            int i3 = (1 << (25 - (s3 >> 10))) - 1;
            s2 = (s2 + (i3 & ((s2 >> 15) - 1))) & (~i3);
        }
        if (isNaN((short) s2)) {
            s2 |= NaN;
        }
        return (short) s2;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static short floor(short s) {
        short s2 = UShort.MAX_VALUE;
        short s3 = s & UShort.MAX_VALUE;
        short s4 = s3 & Short.MAX_VALUE;
        if (s4 < 15360) {
            short s5 = s3 & 32768;
            if (s3 <= 32768) {
                s2 = 0;
            }
            s3 = (s2 & 15360) | s5;
        } else if (s4 < 25600) {
            int i = (1 << (25 - (s4 >> 10))) - 1;
            s3 = (s3 + ((-(s3 >> 15)) & i)) & (~i);
        }
        if (isNaN((short) s3)) {
            s3 |= NaN;
        }
        return (short) s3;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static short min(short s, short s2) {
        if (isNaN(s) || isNaN(s2)) {
            return NaN;
        }
        if ((s & Short.MAX_VALUE) == 0 && (s2 & Short.MAX_VALUE) == 0) {
            return (s & 32768) != 0 ? s : s2;
        }
        return ((s & 32768) != 0 ? 32768 - (s & UShort.MAX_VALUE) : s & UShort.MAX_VALUE) < ((s2 & 32768) != 0 ? 32768 - (65535 & s2) : s2 & UShort.MAX_VALUE) ? s : s2;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static short max(short s, short s2) {
        if (isNaN(s) || isNaN(s2)) {
            return NaN;
        }
        if ((s & Short.MAX_VALUE) == 0 && (s2 & Short.MAX_VALUE) == 0) {
            return (s & 32768) != 0 ? s2 : s;
        }
        return ((s & 32768) != 0 ? 32768 - (s & UShort.MAX_VALUE) : s & UShort.MAX_VALUE) > ((s2 & 32768) != 0 ? 32768 - (65535 & s2) : s2 & UShort.MAX_VALUE) ? s : s2;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean less(short s, short s2) {
        if (isNaN(s) || isNaN(s2)) {
            return false;
        }
        short s3 = s & 32768;
        int i = s & UShort.MAX_VALUE;
        if (s3 != 0) {
            i = 32768 - i;
        }
        if (i < ((s2 & 32768) != 0 ? 32768 - (s2 & UShort.MAX_VALUE) : s2 & UShort.MAX_VALUE)) {
            return true;
        }
        return false;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean lessEquals(short s, short s2) {
        if (isNaN(s) || isNaN(s2)) {
            return false;
        }
        short s3 = s & 32768;
        int i = s & UShort.MAX_VALUE;
        if (s3 != 0) {
            i = 32768 - i;
        }
        if (i <= ((s2 & 32768) != 0 ? 32768 - (s2 & UShort.MAX_VALUE) : s2 & UShort.MAX_VALUE)) {
            return true;
        }
        return false;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean greater(short s, short s2) {
        if (isNaN(s) || isNaN(s2)) {
            return false;
        }
        short s3 = s & 32768;
        int i = s & UShort.MAX_VALUE;
        if (s3 != 0) {
            i = 32768 - i;
        }
        if (i > ((s2 & 32768) != 0 ? 32768 - (s2 & UShort.MAX_VALUE) : s2 & UShort.MAX_VALUE)) {
            return true;
        }
        return false;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean greaterEquals(short s, short s2) {
        if (isNaN(s) || isNaN(s2)) {
            return false;
        }
        short s3 = s & 32768;
        int i = s & UShort.MAX_VALUE;
        if (s3 != 0) {
            i = 32768 - i;
        }
        if (i >= ((s2 & 32768) != 0 ? 32768 - (s2 & UShort.MAX_VALUE) : s2 & UShort.MAX_VALUE)) {
            return true;
        }
        return false;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean equals(short s, short s2) {
        if (isNaN(s) || isNaN(s2)) {
            return false;
        }
        if (s == s2 || ((s | s2) & Short.MAX_VALUE) == 0) {
            return true;
        }
        return false;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static float toFloat(short s) {
        int i;
        int i2;
        int i3;
        short s2 = s & UShort.MAX_VALUE;
        short s3 = 32768 & s2;
        int i4 = (s2 >>> 10) & 31;
        short s4 = s2 & 1023;
        if (i4 != 0) {
            int i5 = s4 << 13;
            if (i4 == 31) {
                i3 = 255;
                if (i5 != 0) {
                    i5 |= 4194304;
                }
            } else {
                i3 = (i4 - 15) + 127;
            }
            int i6 = i3;
            i2 = i5;
            i = i6;
        } else if (s4 != 0) {
            float intBitsToFloat = Float.intBitsToFloat(s4 + FP32_DENORMAL_MAGIC) - FP32_DENORMAL_FLOAT;
            return s3 == 0 ? intBitsToFloat : -intBitsToFloat;
        } else {
            i = 0;
            i2 = 0;
        }
        return Float.intBitsToFloat((i << 23) | (s3 << 16) | i2);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static short toHalf(float f) {
        int floatToRawIntBits = Float.floatToRawIntBits(f);
        int i = floatToRawIntBits >>> 31;
        int i2 = (floatToRawIntBits >>> 23) & 255;
        int i3 = floatToRawIntBits & 8388607;
        int i4 = 31;
        int i5 = 0;
        if (i2 != 255) {
            int i6 = (i2 - 127) + 15;
            if (i6 < 31) {
                if (i6 > 0) {
                    i5 = i3 >> 13;
                    if ((i3 & 8191) + (i5 & 1) > 4096) {
                        i5++;
                    }
                    i4 = i6;
                } else if (i6 < -10) {
                    i4 = 0;
                } else {
                    int i7 = i3 | 8388608;
                    int i8 = 14 - i6;
                    int i9 = i7 >> i8;
                    if ((i7 & ((1 << i8) - 1)) + (i9 & 1) > (1 << (i8 - 1))) {
                        i9++;
                    }
                    i4 = 0;
                    i5 = i9;
                }
            }
        } else if (i3 != 0) {
            i5 = 512;
        }
        return (short) ((i << 15) | ((i4 << 10) + i5));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String toHexString(short s) {
        StringBuilder sb = new StringBuilder();
        short s2 = s & UShort.MAX_VALUE;
        int i = s2 >>> 15;
        int i2 = (s2 >>> 10) & 31;
        short s3 = s2 & 1023;
        if (i2 != 31) {
            if (i == 1) {
                sb.append('-');
            }
            if (i2 != 0) {
                sb.append("0x1.");
                sb.append(Integer.toHexString(s3).replaceFirst("0{2,}$", ""));
                sb.append('p');
                sb.append(Integer.toString(i2 - 15));
            } else if (s3 == 0) {
                sb.append("0x0.0p0");
            } else {
                sb.append("0x0.");
                sb.append(Integer.toHexString(s3).replaceFirst("0{2,}$", ""));
                sb.append("p-14");
            }
        } else if (s3 == 0) {
            if (i != 0) {
                sb.append('-');
            }
            sb.append("Infinity");
        } else {
            sb.append("NaN");
        }
        return sb.toString();
    }
}

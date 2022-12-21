package jdk.internal.util;

import jdk.internal.misc.Unsafe;

public class ArraysSupport {
    private static final boolean BIG_ENDIAN = false;
    public static final int LOG2_ARRAY_BOOLEAN_INDEX_SCALE = exactLog2(Unsafe.ARRAY_BOOLEAN_INDEX_SCALE);
    public static final int LOG2_ARRAY_BYTE_INDEX_SCALE = exactLog2(Unsafe.ARRAY_BYTE_INDEX_SCALE);
    public static final int LOG2_ARRAY_CHAR_INDEX_SCALE = exactLog2(Unsafe.ARRAY_CHAR_INDEX_SCALE);
    public static final int LOG2_ARRAY_DOUBLE_INDEX_SCALE = exactLog2(Unsafe.ARRAY_DOUBLE_INDEX_SCALE);
    public static final int LOG2_ARRAY_FLOAT_INDEX_SCALE = exactLog2(Unsafe.ARRAY_FLOAT_INDEX_SCALE);
    public static final int LOG2_ARRAY_INT_INDEX_SCALE = exactLog2(Unsafe.ARRAY_INT_INDEX_SCALE);
    public static final int LOG2_ARRAY_LONG_INDEX_SCALE = exactLog2(Unsafe.ARRAY_LONG_INDEX_SCALE);
    public static final int LOG2_ARRAY_SHORT_INDEX_SCALE = exactLog2(Unsafe.ARRAY_SHORT_INDEX_SCALE);
    private static final int LOG2_BYTE_BIT_SIZE = exactLog2(8);

    /* renamed from: U */
    static final Unsafe f832U = Unsafe.getUnsafe();

    private static int exactLog2(int i) {
        if (((i - 1) & i) == 0) {
            return Integer.numberOfTrailingZeros(i);
        }
        throw new Error("data type scale not a power of two");
    }

    private ArraysSupport() {
    }

    public static int vectorizedMismatch(Object obj, long j, Object obj2, long j2, int i, int i2) {
        int i3 = LOG2_ARRAY_LONG_INDEX_SCALE - i2;
        int i4 = 0;
        while (i4 < (i >> i3)) {
            long j3 = ((long) i4) << LOG2_ARRAY_LONG_INDEX_SCALE;
            Unsafe unsafe = f832U;
            long longUnaligned = unsafe.getLongUnaligned(obj, j + j3);
            long longUnaligned2 = unsafe.getLongUnaligned(obj2, j3 + j2);
            if (longUnaligned != longUnaligned2) {
                return (i4 << i3) + (Long.numberOfTrailingZeros(longUnaligned ^ longUnaligned2) >> (LOG2_BYTE_BIT_SIZE + i2));
            }
            i4++;
        }
        int i5 = i4 << i3;
        int i6 = i - i5;
        int i7 = LOG2_ARRAY_INT_INDEX_SCALE;
        if (i2 >= i7) {
            return ~i6;
        }
        int i8 = 1 << (i7 - i2);
        if (i6 >= i8) {
            long j4 = ((long) i4) << LOG2_ARRAY_LONG_INDEX_SCALE;
            Unsafe unsafe2 = f832U;
            int intUnaligned = unsafe2.getIntUnaligned(obj, j + j4);
            int intUnaligned2 = unsafe2.getIntUnaligned(obj2, j2 + j4);
            if (intUnaligned != intUnaligned2) {
                return i5 + (Integer.numberOfTrailingZeros(intUnaligned ^ intUnaligned2) >> (LOG2_BYTE_BIT_SIZE + i2));
            }
            i6 -= i8;
        }
        return ~i6;
    }

    public static int mismatch(boolean[] zArr, boolean[] zArr2, int i) {
        int i2 = 0;
        if (i > 7) {
            if (zArr[0] != zArr2[0]) {
                return 0;
            }
            int vectorizedMismatch = vectorizedMismatch(zArr, (long) Unsafe.ARRAY_BOOLEAN_BASE_OFFSET, zArr2, (long) Unsafe.ARRAY_BOOLEAN_BASE_OFFSET, i, LOG2_ARRAY_BOOLEAN_INDEX_SCALE);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i2 = i - (~vectorizedMismatch);
        }
        while (i2 < i) {
            if (zArr[i2] != zArr2[i2]) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public static int mismatch(boolean[] zArr, int i, boolean[] zArr2, int i2, int i3) {
        int i4 = 0;
        if (i3 > 7) {
            if (zArr[i] != zArr2[i2]) {
                return 0;
            }
            int vectorizedMismatch = vectorizedMismatch(zArr, (long) (Unsafe.ARRAY_BOOLEAN_BASE_OFFSET + i), zArr2, (long) (Unsafe.ARRAY_BOOLEAN_BASE_OFFSET + i2), i3, LOG2_ARRAY_BOOLEAN_INDEX_SCALE);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i4 = i3 - (~vectorizedMismatch);
        }
        while (i4 < i3) {
            if (zArr[i + i4] != zArr2[i2 + i4]) {
                return i4;
            }
            i4++;
        }
        return -1;
    }

    public static int mismatch(byte[] bArr, byte[] bArr2, int i) {
        int i2 = 0;
        if (i > 7) {
            if (bArr[0] != bArr2[0]) {
                return 0;
            }
            int vectorizedMismatch = vectorizedMismatch(bArr, (long) Unsafe.ARRAY_BYTE_BASE_OFFSET, bArr2, (long) Unsafe.ARRAY_BYTE_BASE_OFFSET, i, LOG2_ARRAY_BYTE_INDEX_SCALE);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i2 = i - (~vectorizedMismatch);
        }
        while (i2 < i) {
            if (bArr[i2] != bArr2[i2]) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public static int mismatch(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        int i4 = 0;
        if (i3 > 7) {
            if (bArr[i] != bArr2[i2]) {
                return 0;
            }
            int vectorizedMismatch = vectorizedMismatch(bArr, (long) (Unsafe.ARRAY_BYTE_BASE_OFFSET + i), bArr2, (long) (Unsafe.ARRAY_BYTE_BASE_OFFSET + i2), i3, LOG2_ARRAY_BYTE_INDEX_SCALE);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i4 = i3 - (~vectorizedMismatch);
        }
        while (i4 < i3) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return i4;
            }
            i4++;
        }
        return -1;
    }

    public static int mismatch(char[] cArr, char[] cArr2, int i) {
        int i2 = 0;
        if (i > 3) {
            if (cArr[0] != cArr2[0]) {
                return 0;
            }
            int vectorizedMismatch = vectorizedMismatch(cArr, (long) Unsafe.ARRAY_CHAR_BASE_OFFSET, cArr2, (long) Unsafe.ARRAY_CHAR_BASE_OFFSET, i, LOG2_ARRAY_CHAR_INDEX_SCALE);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i2 = i - (~vectorizedMismatch);
        }
        while (i2 < i) {
            if (cArr[i2] != cArr2[i2]) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public static int mismatch(char[] cArr, int i, char[] cArr2, int i2, int i3) {
        int i4 = 0;
        if (i3 > 3) {
            if (cArr[i] != cArr2[i2]) {
                return 0;
            }
            int i5 = Unsafe.ARRAY_CHAR_BASE_OFFSET;
            int i6 = LOG2_ARRAY_CHAR_INDEX_SCALE;
            long j = (long) (Unsafe.ARRAY_CHAR_BASE_OFFSET + (i2 << i6));
            char[] cArr3 = cArr;
            int vectorizedMismatch = vectorizedMismatch(cArr3, (long) (i5 + (i << i6)), cArr2, j, i3, i6);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i4 = i3 - (~vectorizedMismatch);
        }
        while (i4 < i3) {
            if (cArr[i + i4] != cArr2[i2 + i4]) {
                return i4;
            }
            i4++;
        }
        return -1;
    }

    public static int mismatch(short[] sArr, short[] sArr2, int i) {
        int i2 = 0;
        if (i > 3) {
            if (sArr[0] != sArr2[0]) {
                return 0;
            }
            int vectorizedMismatch = vectorizedMismatch(sArr, (long) Unsafe.ARRAY_SHORT_BASE_OFFSET, sArr2, (long) Unsafe.ARRAY_SHORT_BASE_OFFSET, i, LOG2_ARRAY_SHORT_INDEX_SCALE);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i2 = i - (~vectorizedMismatch);
        }
        while (i2 < i) {
            if (sArr[i2] != sArr2[i2]) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public static int mismatch(short[] sArr, int i, short[] sArr2, int i2, int i3) {
        int i4 = 0;
        if (i3 > 3) {
            if (sArr[i] != sArr2[i2]) {
                return 0;
            }
            int i5 = Unsafe.ARRAY_SHORT_BASE_OFFSET;
            int i6 = LOG2_ARRAY_SHORT_INDEX_SCALE;
            long j = (long) (Unsafe.ARRAY_SHORT_BASE_OFFSET + (i2 << i6));
            short[] sArr3 = sArr;
            int vectorizedMismatch = vectorizedMismatch(sArr3, (long) (i5 + (i << i6)), sArr2, j, i3, i6);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i4 = i3 - (~vectorizedMismatch);
        }
        while (i4 < i3) {
            if (sArr[i + i4] != sArr2[i2 + i4]) {
                return i4;
            }
            i4++;
        }
        return -1;
    }

    public static int mismatch(int[] iArr, int[] iArr2, int i) {
        int i2 = 0;
        if (i > 1) {
            if (iArr[0] != iArr2[0]) {
                return 0;
            }
            int vectorizedMismatch = vectorizedMismatch(iArr, (long) Unsafe.ARRAY_INT_BASE_OFFSET, iArr2, (long) Unsafe.ARRAY_INT_BASE_OFFSET, i, LOG2_ARRAY_INT_INDEX_SCALE);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i2 = i - (~vectorizedMismatch);
        }
        while (i2 < i) {
            if (iArr[i2] != iArr2[i2]) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public static int mismatch(int[] iArr, int i, int[] iArr2, int i2, int i3) {
        int i4 = 0;
        if (i3 > 1) {
            if (iArr[i] != iArr2[i2]) {
                return 0;
            }
            int i5 = Unsafe.ARRAY_INT_BASE_OFFSET;
            int i6 = LOG2_ARRAY_INT_INDEX_SCALE;
            long j = (long) (Unsafe.ARRAY_INT_BASE_OFFSET + (i2 << i6));
            int[] iArr3 = iArr;
            int vectorizedMismatch = vectorizedMismatch(iArr3, (long) (i5 + (i << i6)), iArr2, j, i3, i6);
            if (vectorizedMismatch >= 0) {
                return vectorizedMismatch;
            }
            i4 = i3 - (~vectorizedMismatch);
        }
        while (i4 < i3) {
            if (iArr[i + i4] != iArr2[i2 + i4]) {
                return i4;
            }
            i4++;
        }
        return -1;
    }

    public static int mismatch(float[] fArr, float[] fArr2, int i) {
        return mismatch(fArr, 0, fArr2, 0, i);
    }

    public static int mismatch(float[] fArr, int i, float[] fArr2, int i2, int i3) {
        int i4 = 0;
        if (i3 > 1) {
            if (Float.floatToRawIntBits(fArr[i]) == Float.floatToRawIntBits(fArr2[i2])) {
                int i5 = Unsafe.ARRAY_FLOAT_BASE_OFFSET;
                int i6 = LOG2_ARRAY_FLOAT_INDEX_SCALE;
                long j = (long) (Unsafe.ARRAY_FLOAT_BASE_OFFSET + (i2 << i6));
                float[] fArr3 = fArr;
                i4 = vectorizedMismatch(fArr3, (long) (i5 + (i << i6)), fArr2, j, i3, i6);
            }
            if (i4 < 0) {
                i4 = i3 - (~i4);
            } else if (!Float.isNaN(fArr[i + i4]) || !Float.isNaN(fArr2[i2 + i4])) {
                return i4;
            } else {
                i4++;
            }
        }
        while (i4 < i3) {
            if (Float.floatToIntBits(fArr[i + i4]) != Float.floatToIntBits(fArr2[i2 + i4])) {
                return i4;
            }
            i4++;
        }
        return -1;
    }

    public static int mismatch(long[] jArr, long[] jArr2, int i) {
        if (i == 0) {
            return -1;
        }
        if (jArr[0] != jArr2[0]) {
            return 0;
        }
        int vectorizedMismatch = vectorizedMismatch(jArr, (long) Unsafe.ARRAY_LONG_BASE_OFFSET, jArr2, (long) Unsafe.ARRAY_LONG_BASE_OFFSET, i, LOG2_ARRAY_LONG_INDEX_SCALE);
        if (vectorizedMismatch >= 0) {
            return vectorizedMismatch;
        }
        return -1;
    }

    public static int mismatch(long[] jArr, int i, long[] jArr2, int i2, int i3) {
        if (i3 == 0) {
            return -1;
        }
        if (jArr[i] != jArr2[i2]) {
            return 0;
        }
        int i4 = Unsafe.ARRAY_LONG_BASE_OFFSET;
        int i5 = LOG2_ARRAY_LONG_INDEX_SCALE;
        long[] jArr3 = jArr;
        long[] jArr4 = jArr2;
        int vectorizedMismatch = vectorizedMismatch(jArr3, (long) (i4 + (i << i5)), jArr4, (long) (Unsafe.ARRAY_LONG_BASE_OFFSET + (i2 << i5)), i3, i5);
        if (vectorizedMismatch >= 0) {
            return vectorizedMismatch;
        }
        return -1;
    }

    public static int mismatch(double[] dArr, double[] dArr2, int i) {
        return mismatch(dArr, 0, dArr2, 0, i);
    }

    public static int mismatch(double[] dArr, int i, double[] dArr2, int i2, int i3) {
        int i4;
        if (i3 == 0) {
            return -1;
        }
        if (Double.doubleToRawLongBits(dArr[i]) == Double.doubleToRawLongBits(dArr2[i2])) {
            int i5 = Unsafe.ARRAY_DOUBLE_BASE_OFFSET;
            int i6 = LOG2_ARRAY_DOUBLE_INDEX_SCALE;
            long j = (long) (Unsafe.ARRAY_DOUBLE_BASE_OFFSET + (i2 << i6));
            double[] dArr3 = dArr;
            i4 = vectorizedMismatch(dArr3, (long) (i5 + (i << i6)), dArr2, j, i3, i6);
        } else {
            i4 = 0;
        }
        if (i4 >= 0) {
            if (!Double.isNaN(dArr[i + i4]) || !Double.isNaN(dArr2[i2 + i4])) {
                return i4;
            }
            do {
                i4++;
                if (i4 < i3) {
                }
            } while (Double.doubleToLongBits(dArr[i + i4]) == Double.doubleToLongBits(dArr2[i2 + i4]));
            return i4;
        }
        return -1;
    }
}

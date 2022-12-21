package java.math;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import java.util.Arrays;

class MutableBigInteger {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int KNUTH_POW2_THRESH_LEN = 6;
    static final int KNUTH_POW2_THRESH_ZEROS = 3;
    static final MutableBigInteger ONE = new MutableBigInteger(1);
    int intLen;
    int offset;
    int[] value;

    static int inverseMod32(int i) {
        int i2 = (2 - (i * i)) * i;
        int i3 = i2 * (2 - (i * i2));
        int i4 = i3 * (2 - (i * i3));
        return i4 * (2 - (i * i4));
    }

    static long inverseMod64(long j) {
        long j2 = (2 - (j * j)) * j;
        long j3 = j2 * (2 - (j * j2));
        long j4 = j3 * (2 - (j * j3));
        long j5 = j4 * (2 - (j * j4));
        return j5 * (2 - (j * j5));
    }

    private boolean unsignedLongCompare(long j, long j2) {
        return j + Long.MIN_VALUE > j2 + Long.MIN_VALUE;
    }

    MutableBigInteger() {
        this.offset = 0;
        this.value = new int[1];
        this.intLen = 0;
    }

    MutableBigInteger(int i) {
        this.offset = 0;
        int[] iArr = new int[1];
        this.value = iArr;
        this.intLen = 1;
        iArr[0] = i;
    }

    MutableBigInteger(int[] iArr) {
        this.offset = 0;
        this.value = iArr;
        this.intLen = iArr.length;
    }

    MutableBigInteger(BigInteger bigInteger) {
        this.offset = 0;
        this.intLen = bigInteger.mag.length;
        this.value = Arrays.copyOf(bigInteger.mag, this.intLen);
    }

    MutableBigInteger(MutableBigInteger mutableBigInteger) {
        this.offset = 0;
        int i = mutableBigInteger.intLen;
        this.intLen = i;
        int[] iArr = mutableBigInteger.value;
        int i2 = mutableBigInteger.offset;
        this.value = Arrays.copyOfRange(iArr, i2, i + i2);
    }

    private void ones(int i) {
        if (i > this.value.length) {
            this.value = new int[i];
        }
        Arrays.fill(this.value, -1);
        this.offset = 0;
        this.intLen = i;
    }

    private int[] getMagnitudeArray() {
        int i = this.offset;
        if (i <= 0) {
            int[] iArr = this.value;
            if (iArr.length == this.intLen) {
                return iArr;
            }
        }
        return Arrays.copyOfRange(this.value, i, this.intLen + i);
    }

    private long toLong() {
        int i = this.intLen;
        if (i == 0) {
            return 0;
        }
        int[] iArr = this.value;
        int i2 = this.offset;
        long j = ((long) iArr[i2]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        return i == 2 ? (j << 32) | (((long) iArr[i2 + 1]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) : j;
    }

    /* access modifiers changed from: package-private */
    public BigInteger toBigInteger(int i) {
        if (this.intLen == 0 || i == 0) {
            return BigInteger.ZERO;
        }
        return new BigInteger(getMagnitudeArray(), i);
    }

    /* access modifiers changed from: package-private */
    public BigInteger toBigInteger() {
        normalize();
        return toBigInteger(isZero() ^ true ? 1 : 0);
    }

    /* access modifiers changed from: package-private */
    public BigDecimal toBigDecimal(int i, int i2) {
        long j;
        if (this.intLen == 0 || i == 0) {
            return BigDecimal.zeroValueOf(i2);
        }
        int[] magnitudeArray = getMagnitudeArray();
        int length = magnitudeArray.length;
        int i3 = magnitudeArray[0];
        if (length > 2 || (i3 < 0 && length == 2)) {
            return new BigDecimal(new BigInteger(magnitudeArray, i), Long.MIN_VALUE, i2, 0);
        }
        if (length == 2) {
            j = ((((long) i3) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) << 32) | (((long) magnitudeArray[1]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
        } else {
            j = ((long) i3) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        }
        if (i == -1) {
            j = -j;
        }
        return BigDecimal.valueOf(j, i2);
    }

    /* access modifiers changed from: package-private */
    public long toCompactValue(int i) {
        if (this.intLen == 0 || i == 0) {
            return 0;
        }
        int[] magnitudeArray = getMagnitudeArray();
        int length = magnitudeArray.length;
        int i2 = magnitudeArray[0];
        if (length > 2) {
            return Long.MIN_VALUE;
        }
        if (i2 < 0 && length == 2) {
            return Long.MIN_VALUE;
        }
        long j = length == 2 ? ((((long) i2) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) << 32) | (((long) magnitudeArray[1]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) : ((long) i2) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        return i == -1 ? -j : j;
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.intLen = 0;
        this.offset = 0;
        int length = this.value.length;
        for (int i = 0; i < length; i++) {
            this.value[i] = 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void reset() {
        this.intLen = 0;
        this.offset = 0;
    }

    /* access modifiers changed from: package-private */
    public final int compare(MutableBigInteger mutableBigInteger) {
        int i = mutableBigInteger.intLen;
        int i2 = this.intLen;
        if (i2 < i) {
            return -1;
        }
        if (i2 > i) {
            return 1;
        }
        int[] iArr = mutableBigInteger.value;
        int i3 = this.offset;
        int i4 = mutableBigInteger.offset;
        while (i3 < this.intLen + this.offset) {
            int i5 = this.value[i3] - 2147483648;
            int i6 = iArr[i4] - 2147483648;
            if (i5 < i6) {
                return -1;
            }
            if (i5 > i6) {
                return 1;
            }
            i3++;
            i4++;
        }
        return 0;
    }

    private int compareShifted(MutableBigInteger mutableBigInteger, int i) {
        int i2 = mutableBigInteger.intLen;
        int i3 = this.intLen - i;
        if (i3 < i2) {
            return -1;
        }
        if (i3 > i2) {
            return 1;
        }
        int[] iArr = mutableBigInteger.value;
        int i4 = this.offset;
        int i5 = mutableBigInteger.offset;
        while (i4 < this.offset + i3) {
            int i6 = this.value[i4] - 2147483648;
            int i7 = iArr[i5] - 2147483648;
            if (i6 < i7) {
                return -1;
            }
            if (i6 > i7) {
                return 1;
            }
            i4++;
            i5++;
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public final int compareHalf(MutableBigInteger mutableBigInteger) {
        int i;
        int i2;
        MutableBigInteger mutableBigInteger2 = mutableBigInteger;
        int i3 = mutableBigInteger2.intLen;
        int i4 = this.intLen;
        if (i4 <= 0) {
            return i3 <= 0 ? 0 : -1;
        }
        if (i4 > i3) {
            return 1;
        }
        if (i4 < i3 - 1) {
            return -1;
        }
        int[] iArr = mutableBigInteger2.value;
        if (i4 == i3) {
            i2 = 0;
            i = 0;
        } else if (iArr[0] != 1) {
            return -1;
        } else {
            i2 = Integer.MIN_VALUE;
            i = 1;
        }
        int[] iArr2 = this.value;
        int i5 = this.offset;
        while (i5 < this.offset + i4) {
            int i6 = i + 1;
            int i7 = iArr[i];
            long j = ((long) ((i7 >>> 1) + i2)) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
            int i8 = i5 + 1;
            int i9 = ((((long) iArr2[i5]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) > j ? 1 : ((((long) iArr2[i5]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) == j ? 0 : -1));
            if (i9 != 0) {
                return i9 < 0 ? -1 : 1;
            }
            i5 = i8;
            i2 = (i7 & 1) << 31;
            i = i6;
        }
        return i2 == 0 ? 0 : -1;
    }

    private final int getLowestSetBit() {
        int i = this.intLen;
        if (i == 0) {
            return -1;
        }
        int i2 = i - 1;
        while (i2 > 0 && this.value[this.offset + i2] == 0) {
            i2--;
        }
        int i3 = this.value[this.offset + i2];
        if (i3 == 0) {
            return -1;
        }
        return (((this.intLen - 1) - i2) << 5) + Integer.numberOfTrailingZeros(i3);
    }

    private final int getInt(int i) {
        return this.value[this.offset + i];
    }

    private final long getLong(int i) {
        return ((long) this.value[this.offset + i]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
    }

    /* access modifiers changed from: package-private */
    public final void normalize() {
        int i = this.intLen;
        int i2 = 0;
        if (i == 0) {
            this.offset = 0;
            return;
        }
        int i3 = this.offset;
        if (this.value[i3] == 0) {
            int i4 = i + i3;
            do {
                i3++;
                if (i3 >= i4 || this.value[i3] != 0) {
                    int i5 = this.offset;
                    int i6 = i3 - i5;
                    int i7 = this.intLen - i6;
                    this.intLen = i7;
                }
                i3++;
                break;
            } while (this.value[i3] != 0);
            int i52 = this.offset;
            int i62 = i3 - i52;
            int i72 = this.intLen - i62;
            this.intLen = i72;
            if (i72 != 0) {
                i2 = i52 + i62;
            }
            this.offset = i2;
        }
    }

    private final void ensureCapacity(int i) {
        if (this.value.length < i) {
            this.value = new int[i];
            this.offset = 0;
            this.intLen = i;
        }
    }

    /* access modifiers changed from: package-private */
    public int[] toIntArray() {
        int[] iArr = new int[this.intLen];
        for (int i = 0; i < this.intLen; i++) {
            iArr[i] = this.value[this.offset + i];
        }
        return iArr;
    }

    /* access modifiers changed from: package-private */
    public void setInt(int i, int i2) {
        this.value[this.offset + i] = i2;
    }

    /* access modifiers changed from: package-private */
    public void setValue(int[] iArr, int i) {
        this.value = iArr;
        this.intLen = i;
        this.offset = 0;
    }

    /* access modifiers changed from: package-private */
    public void copyValue(MutableBigInteger mutableBigInteger) {
        int i = mutableBigInteger.intLen;
        if (this.value.length < i) {
            this.value = new int[i];
        }
        System.arraycopy((Object) mutableBigInteger.value, mutableBigInteger.offset, (Object) this.value, 0, i);
        this.intLen = i;
        this.offset = 0;
    }

    /* access modifiers changed from: package-private */
    public void copyValue(int[] iArr) {
        int length = iArr.length;
        if (this.value.length < length) {
            this.value = new int[length];
        }
        System.arraycopy((Object) iArr, 0, (Object) this.value, 0, length);
        this.intLen = length;
        this.offset = 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isOne() {
        return this.intLen == 1 && this.value[this.offset] == 1;
    }

    /* access modifiers changed from: package-private */
    public boolean isZero() {
        return this.intLen == 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isEven() {
        int i = this.intLen;
        return i == 0 || (this.value[(this.offset + i) - 1] & 1) == 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isOdd() {
        return !isZero() && (this.value[(this.offset + this.intLen) - 1] & 1) == 1;
    }

    /* access modifiers changed from: package-private */
    public boolean isNormal() {
        int i = this.intLen;
        int i2 = this.offset;
        int i3 = i + i2;
        int[] iArr = this.value;
        if (i3 > iArr.length) {
            return false;
        }
        if (i == 0) {
            return true;
        }
        if (iArr[i2] != 0) {
            return true;
        }
        return false;
    }

    public String toString() {
        return toBigInteger(1).toString();
    }

    /* access modifiers changed from: package-private */
    public void safeRightShift(int i) {
        if (i / 32 >= this.intLen) {
            reset();
        } else {
            rightShift(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void rightShift(int i) {
        int i2 = this.intLen;
        if (i2 != 0) {
            int i3 = i >>> 5;
            int i4 = i & 31;
            this.intLen = i2 - i3;
            if (i4 != 0) {
                if (i4 >= BigInteger.bitLengthForInt(this.value[this.offset])) {
                    primitiveLeftShift(32 - i4);
                    this.intLen--;
                    return;
                }
                primitiveRightShift(i4);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void safeLeftShift(int i) {
        if (i > 0) {
            leftShift(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void leftShift(int i) {
        int i2;
        if (this.intLen != 0) {
            int i3 = i >>> 5;
            int i4 = i & 31;
            int bitLengthForInt = 32 - BigInteger.bitLengthForInt(this.value[this.offset]);
            if (i <= bitLengthForInt) {
                primitiveLeftShift(i4);
                return;
            }
            int i5 = this.intLen + i3 + 1;
            if (i4 <= bitLengthForInt) {
                i5--;
            }
            int[] iArr = this.value;
            if (iArr.length < i5) {
                int[] iArr2 = new int[i5];
                for (int i6 = 0; i6 < this.intLen; i6++) {
                    iArr2[i6] = this.value[this.offset + i6];
                }
                setValue(iArr2, i5);
            } else if (iArr.length - this.offset >= i5) {
                int i7 = 0;
                while (true) {
                    int i8 = this.intLen;
                    if (i7 >= i5 - i8) {
                        break;
                    }
                    this.value[this.offset + i8 + i7] = 0;
                    i7++;
                }
            } else {
                int i9 = 0;
                while (true) {
                    i2 = this.intLen;
                    if (i9 >= i2) {
                        break;
                    }
                    int[] iArr3 = this.value;
                    iArr3[i9] = iArr3[this.offset + i9];
                    i9++;
                }
                while (i2 < i5) {
                    this.value[i2] = 0;
                    i2++;
                }
                this.offset = 0;
            }
            this.intLen = i5;
            if (i4 != 0) {
                if (i4 <= bitLengthForInt) {
                    primitiveLeftShift(i4);
                } else {
                    primitiveRightShift(32 - i4);
                }
            }
        }
    }

    private int divadd(int[] iArr, int[] iArr2, int i) {
        long j = 0;
        for (int length = iArr.length - 1; length >= 0; length--) {
            int i2 = length + i;
            long j2 = (((long) iArr[length]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + (UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER & ((long) iArr2[i2])) + j;
            iArr2[i2] = (int) j2;
            j = j2 >>> 32;
        }
        return (int) j;
    }

    private int mulsub(int[] iArr, int[] iArr2, int i, int i2, int i3) {
        long j = ((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        int i4 = i3 + i2;
        int i5 = i2 - 1;
        long j2 = 0;
        while (i5 >= 0) {
            long j3 = ((((long) iArr2[i5]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) * j) + j2;
            long j4 = ((long) iArr[i4]) - j3;
            int i6 = i4 - 1;
            iArr[i4] = (int) j4;
            j2 = ((long) ((j4 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) > (((long) (~((int) j3))) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) ? 1 : 0)) + (j3 >>> 32);
            i5--;
            i4 = i6;
        }
        return (int) j2;
    }

    private int mulsubBorrow(int[] iArr, int[] iArr2, int i, int i2, int i3) {
        long j = ((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        int i4 = i3 + i2;
        int i5 = i2 - 1;
        long j2 = 0;
        while (i5 >= 0) {
            long j3 = ((((long) iArr2[i5]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) * j) + j2;
            int i6 = i4 - 1;
            j2 = ((long) (((((long) iArr[i4]) - j3) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) > (((long) (~((int) j3))) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) ? 1 : 0)) + (j3 >>> 32);
            i5--;
            i4 = i6;
        }
        return (int) j2;
    }

    private final void primitiveRightShift(int i) {
        int[] iArr = this.value;
        int i2 = 32 - i;
        int i3 = (this.offset + this.intLen) - 1;
        int i4 = iArr[i3];
        while (true) {
            int i5 = this.offset;
            if (i3 > i5) {
                int i6 = iArr[i3 - 1];
                iArr[i3] = (i4 >>> i) | (i6 << i2);
                i3--;
                i4 = i6;
            } else {
                iArr[i5] = iArr[i5] >>> i;
                return;
            }
        }
    }

    private final void primitiveLeftShift(int i) {
        int[] iArr = this.value;
        int i2 = 32 - i;
        int i3 = this.offset;
        int i4 = iArr[i3];
        int i5 = (this.intLen + i3) - 1;
        while (i3 < i5) {
            int i6 = i3 + 1;
            int i7 = iArr[i6];
            iArr[i3] = (i4 << i) | (i7 >>> i2);
            i3 = i6;
            i4 = i7;
        }
        int i8 = (this.offset + this.intLen) - 1;
        iArr[i8] = iArr[i8] << i;
    }

    private BigInteger getLower(int i) {
        if (isZero()) {
            return BigInteger.ZERO;
        }
        int i2 = 1;
        if (this.intLen < i) {
            return toBigInteger(1);
        }
        while (i > 0 && this.value[(this.offset + this.intLen) - i] == 0) {
            i--;
        }
        if (i <= 0) {
            i2 = 0;
        }
        int[] iArr = this.value;
        int i3 = this.offset;
        int i4 = this.intLen;
        return new BigInteger(Arrays.copyOfRange(iArr, (i3 + i4) - i, i3 + i4), i2);
    }

    private void keepLower(int i) {
        int i2 = this.intLen;
        if (i2 >= i) {
            this.offset += i2 - i;
            this.intLen = i;
        }
    }

    /* access modifiers changed from: package-private */
    public void add(MutableBigInteger mutableBigInteger) {
        MutableBigInteger mutableBigInteger2 = mutableBigInteger;
        int i = this.intLen;
        int i2 = mutableBigInteger2.intLen;
        int i3 = i > i2 ? i : i2;
        int[] iArr = this.value;
        if (iArr.length < i3) {
            iArr = new int[i3];
        }
        int length = iArr.length - 1;
        long j = 0;
        while (i > 0 && i2 > 0) {
            int i4 = i - 1;
            int i5 = i2 - 1;
            long j2 = (((long) this.value[this.offset + i4]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + (((long) mutableBigInteger2.value[mutableBigInteger2.offset + i5]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + j;
            iArr[length] = (int) j2;
            j = j2 >>> 32;
            length--;
            i = i4;
            i2 = i5;
        }
        while (i > 0) {
            i--;
            if (j != 0 || iArr != this.value || length != this.offset + i) {
                long j3 = (((long) this.value[this.offset + i]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + j;
                iArr[length] = (int) j3;
                j = j3 >>> 32;
                length--;
            } else {
                return;
            }
        }
        while (i2 > 0) {
            i2--;
            long j4 = (((long) mutableBigInteger2.value[mutableBigInteger2.offset + i2]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + j;
            iArr[length] = (int) j4;
            j = j4 >>> 32;
            length--;
        }
        if (j > 0) {
            i3++;
            if (iArr.length < i3) {
                int[] iArr2 = new int[i3];
                System.arraycopy((Object) iArr, 0, (Object) iArr2, 1, iArr.length);
                iArr2[0] = 1;
                iArr = iArr2;
            } else {
                iArr[length] = 1;
            }
        }
        this.value = iArr;
        this.intLen = i3;
        this.offset = iArr.length - i3;
    }

    /* access modifiers changed from: package-private */
    public void addShifted(MutableBigInteger mutableBigInteger, int i) {
        MutableBigInteger mutableBigInteger2 = mutableBigInteger;
        if (!mutableBigInteger.isZero()) {
            int i2 = this.intLen;
            int i3 = mutableBigInteger2.intLen + i;
            int i4 = i2 > i3 ? i2 : i3;
            int[] iArr = this.value;
            if (iArr.length < i4) {
                iArr = new int[i4];
            }
            int length = iArr.length - 1;
            long j = 0;
            while (i2 > 0 && i3 > 0) {
                i2--;
                i3--;
                int i5 = mutableBigInteger2.offset;
                int i6 = i3 + i5;
                int[] iArr2 = mutableBigInteger2.value;
                long j2 = (((long) this.value[this.offset + i2]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + (((long) (i6 < iArr2.length ? iArr2[i5 + i3] : 0)) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + j;
                iArr[length] = (int) j2;
                length--;
                j = j2 >>> 32;
            }
            while (i2 > 0) {
                i2--;
                if (j != 0 || iArr != this.value || length != this.offset + i2) {
                    long j3 = (((long) this.value[this.offset + i2]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + j;
                    iArr[length] = (int) j3;
                    length--;
                    j = j3 >>> 32;
                } else {
                    return;
                }
            }
            while (i3 > 0) {
                i3--;
                int i7 = mutableBigInteger2.offset;
                int i8 = i3 + i7;
                int[] iArr3 = mutableBigInteger2.value;
                long j4 = (((long) (i8 < iArr3.length ? iArr3[i7 + i3] : 0)) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + j;
                iArr[length] = (int) j4;
                j = j4 >>> 32;
                length--;
            }
            if (j > 0) {
                i4++;
                if (iArr.length < i4) {
                    int[] iArr4 = new int[i4];
                    System.arraycopy((Object) iArr, 0, (Object) iArr4, 1, iArr.length);
                    iArr4[0] = 1;
                    iArr = iArr4;
                } else {
                    iArr[length] = 1;
                }
            }
            this.value = iArr;
            this.intLen = i4;
            this.offset = iArr.length - i4;
        }
    }

    /* access modifiers changed from: package-private */
    public void addDisjoint(MutableBigInteger mutableBigInteger, int i) {
        if (!mutableBigInteger.isZero()) {
            int i2 = this.intLen;
            int i3 = mutableBigInteger.intLen + i;
            int i4 = i2 > i3 ? i2 : i3;
            int[] iArr = this.value;
            if (iArr.length < i4) {
                iArr = new int[i4];
            } else {
                Arrays.fill(iArr, this.offset + i2, iArr.length, 0);
            }
            int length = iArr.length - 1;
            System.arraycopy((Object) this.value, this.offset, (Object) iArr, (length + 1) - i2, i2);
            int i5 = i3 - i2;
            int i6 = length - i2;
            int min = Math.min(i5, mutableBigInteger.value.length - mutableBigInteger.offset);
            int i7 = i6 + 1;
            int i8 = i7 - i5;
            System.arraycopy((Object) mutableBigInteger.value, mutableBigInteger.offset, (Object) iArr, i8, min);
            for (int i9 = i8 + min; i9 < i7; i9++) {
                iArr[i9] = 0;
            }
            this.value = iArr;
            this.intLen = i4;
            this.offset = iArr.length - i4;
        }
    }

    /* access modifiers changed from: package-private */
    public void addLower(MutableBigInteger mutableBigInteger, int i) {
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger(mutableBigInteger);
        int i2 = mutableBigInteger2.offset;
        int i3 = mutableBigInteger2.intLen;
        if (i2 + i3 >= i) {
            mutableBigInteger2.offset = (i2 + i3) - i;
            mutableBigInteger2.intLen = i;
        }
        mutableBigInteger2.normalize();
        add(mutableBigInteger2);
    }

    /* access modifiers changed from: package-private */
    public int subtract(MutableBigInteger mutableBigInteger) {
        MutableBigInteger mutableBigInteger2;
        MutableBigInteger mutableBigInteger3;
        int[] iArr = this.value;
        int compare = compare(mutableBigInteger);
        if (compare == 0) {
            reset();
            return 0;
        }
        if (compare < 0) {
            mutableBigInteger3 = mutableBigInteger;
            mutableBigInteger2 = this;
        } else {
            mutableBigInteger2 = mutableBigInteger;
            mutableBigInteger3 = this;
        }
        int i = mutableBigInteger3.intLen;
        if (iArr.length < i) {
            iArr = new int[i];
        }
        int i2 = mutableBigInteger2.intLen;
        int length = iArr.length - 1;
        long j = 0;
        int i3 = i;
        while (i2 > 0) {
            i3--;
            i2--;
            j = ((((long) mutableBigInteger3.value[mutableBigInteger3.offset + i3]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) - (((long) mutableBigInteger2.value[mutableBigInteger2.offset + i2]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER)) - ((long) ((int) (-(j >> 32))));
            iArr[length] = (int) j;
            length--;
        }
        while (i3 > 0) {
            i3--;
            j = (((long) mutableBigInteger3.value[mutableBigInteger3.offset + i3]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) - ((long) ((int) (-(j >> 32))));
            iArr[length] = (int) j;
            length--;
        }
        this.value = iArr;
        this.intLen = i;
        this.offset = iArr.length - i;
        normalize();
        return compare;
    }

    private int difference(MutableBigInteger mutableBigInteger) {
        MutableBigInteger mutableBigInteger2;
        MutableBigInteger mutableBigInteger3;
        int compare = compare(mutableBigInteger);
        if (compare == 0) {
            return 0;
        }
        if (compare < 0) {
            mutableBigInteger2 = this;
            mutableBigInteger3 = mutableBigInteger;
        } else {
            mutableBigInteger3 = this;
            mutableBigInteger2 = mutableBigInteger;
        }
        int i = mutableBigInteger3.intLen;
        int i2 = mutableBigInteger2.intLen;
        long j = 0;
        while (i2 > 0) {
            i--;
            i2--;
            int[] iArr = mutableBigInteger3.value;
            int i3 = mutableBigInteger3.offset;
            j = ((((long) iArr[i3 + i]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) - (UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER & ((long) mutableBigInteger2.value[mutableBigInteger2.offset + i2]))) - ((long) ((int) (-(j >> 32))));
            iArr[i3 + i] = (int) j;
        }
        while (i > 0) {
            i--;
            int[] iArr2 = mutableBigInteger3.value;
            int i4 = mutableBigInteger3.offset;
            j = (((long) iArr2[i4 + i]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) - ((long) ((int) (-(j >> 32))));
            iArr2[i4 + i] = (int) j;
        }
        mutableBigInteger3.normalize();
        return compare;
    }

    /* access modifiers changed from: package-private */
    public void multiply(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        MutableBigInteger mutableBigInteger3 = this;
        MutableBigInteger mutableBigInteger4 = mutableBigInteger;
        MutableBigInteger mutableBigInteger5 = mutableBigInteger2;
        int i = mutableBigInteger3.intLen;
        int i2 = mutableBigInteger4.intLen;
        int i3 = i + i2;
        if (mutableBigInteger5.value.length < i3) {
            mutableBigInteger5.value = new int[i3];
        }
        mutableBigInteger5.offset = 0;
        mutableBigInteger5.intLen = i3;
        int i4 = i2 - 1;
        int i5 = i3 - 1;
        int i6 = i4;
        long j = 0;
        while (i6 >= 0) {
            long j2 = ((((long) mutableBigInteger4.value[mutableBigInteger4.offset + i6]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) * (((long) mutableBigInteger3.value[(i - 1) + mutableBigInteger3.offset]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER)) + j;
            mutableBigInteger5.value[i5] = (int) j2;
            j = j2 >>> 32;
            i6--;
            i5--;
            mutableBigInteger3 = this;
            mutableBigInteger4 = mutableBigInteger;
        }
        mutableBigInteger5.value[i - 1] = (int) j;
        int i7 = i - 2;
        while (i7 >= 0) {
            int i8 = i2 + i7;
            int i9 = i4;
            long j3 = 0;
            while (i9 >= 0) {
                MutableBigInteger mutableBigInteger6 = mutableBigInteger;
                int i10 = i2;
                long j4 = (((long) mutableBigInteger6.value[mutableBigInteger6.offset + i9]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) * (((long) this.value[this.offset + i7]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
                int[] iArr = mutableBigInteger5.value;
                long j5 = j4 + (((long) iArr[i8]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + j3;
                iArr[i8] = (int) j5;
                j3 = j5 >>> 32;
                i9--;
                i8--;
                i4 = i4;
                i2 = i10;
            }
            int i11 = i4;
            mutableBigInteger5.value[i7] = (int) j3;
            i7--;
            i2 = i2;
        }
        mutableBigInteger2.normalize();
    }

    /* access modifiers changed from: package-private */
    public void mul(int i, MutableBigInteger mutableBigInteger) {
        if (i == 1) {
            mutableBigInteger.copyValue(this);
        } else if (i == 0) {
            mutableBigInteger.clear();
        } else {
            long j = ((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
            int[] iArr = mutableBigInteger.value;
            int length = iArr.length;
            int i2 = this.intLen;
            if (length < i2 + 1) {
                iArr = new int[(i2 + 1)];
            }
            long j2 = 0;
            for (int i3 = i2 - 1; i3 >= 0; i3--) {
                long j3 = ((((long) this.value[this.offset + i3]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) * j) + j2;
                iArr[i3 + 1] = (int) j3;
                j2 = j3 >>> 32;
            }
            if (j2 == 0) {
                mutableBigInteger.offset = 1;
                mutableBigInteger.intLen = this.intLen;
            } else {
                mutableBigInteger.offset = 0;
                mutableBigInteger.intLen = this.intLen + 1;
                iArr[0] = (int) j2;
            }
            mutableBigInteger.value = iArr;
        }
    }

    /* access modifiers changed from: package-private */
    public int divideOneWord(int i, MutableBigInteger mutableBigInteger) {
        int i2;
        int i3;
        long j = ((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        int i4 = this.intLen;
        int i5 = 1;
        if (i4 == 1) {
            long j2 = ((long) this.value[this.offset]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
            int i6 = (int) (j2 / j);
            int i7 = (int) (j2 - (((long) i6) * j));
            mutableBigInteger.value[0] = i6;
            if (i6 == 0) {
                i5 = 0;
            }
            mutableBigInteger.intLen = i5;
            mutableBigInteger.offset = 0;
            return i7;
        }
        if (mutableBigInteger.value.length < i4) {
            mutableBigInteger.value = new int[i4];
        }
        mutableBigInteger.offset = 0;
        mutableBigInteger.intLen = i4;
        int numberOfLeadingZeros = Integer.numberOfLeadingZeros(i);
        int i8 = this.value[this.offset];
        long j3 = ((long) i8) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        if (j3 < j) {
            mutableBigInteger.value[0] = 0;
        } else {
            int i9 = (int) (j3 / j);
            mutableBigInteger.value[0] = i9;
            i8 = (int) (j3 - (((long) i9) * j));
            j3 = ((long) i8) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        }
        int i10 = this.intLen;
        while (true) {
            i10--;
            if (i10 <= 0) {
                break;
            }
            long j4 = (j3 << 32) | (((long) this.value[(this.offset + this.intLen) - i10]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
            if (j4 >= 0) {
                i3 = (int) (j4 / j);
                i2 = (int) (j4 - (((long) i3) * j));
            } else {
                long divWord = divWord(j4, i);
                i2 = (int) (divWord >>> 32);
                i3 = (int) (divWord & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
            }
            mutableBigInteger.value[this.intLen - i10] = i3;
            i8 = i2;
            j3 = ((long) i2) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        }
        mutableBigInteger.normalize();
        return numberOfLeadingZeros > 0 ? i8 % i : i8;
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger divide(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        return divide(mutableBigInteger, mutableBigInteger2, true);
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger divide(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2, boolean z) {
        int i = mutableBigInteger.intLen;
        if (i < 80 || this.intLen - i < 40) {
            return divideKnuth(mutableBigInteger, mutableBigInteger2, z);
        }
        return divideAndRemainderBurnikelZiegler(mutableBigInteger, mutableBigInteger2);
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger divideKnuth(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        return divideKnuth(mutableBigInteger, mutableBigInteger2, true);
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger divideKnuth(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2, boolean z) {
        int min;
        if (mutableBigInteger.intLen == 0) {
            throw new ArithmeticException("BigInteger divide by zero");
        } else if (this.intLen == 0) {
            mutableBigInteger2.offset = 0;
            mutableBigInteger2.intLen = 0;
            if (z) {
                return new MutableBigInteger();
            }
            return null;
        } else {
            int compare = compare(mutableBigInteger);
            if (compare < 0) {
                mutableBigInteger2.offset = 0;
                mutableBigInteger2.intLen = 0;
                if (z) {
                    return new MutableBigInteger(this);
                }
                return null;
            } else if (compare == 0) {
                int[] iArr = mutableBigInteger2.value;
                mutableBigInteger2.intLen = 1;
                iArr[0] = 1;
                mutableBigInteger2.offset = 0;
                if (z) {
                    return new MutableBigInteger();
                }
                return null;
            } else {
                mutableBigInteger2.clear();
                if (mutableBigInteger.intLen == 1) {
                    int divideOneWord = divideOneWord(mutableBigInteger.value[mutableBigInteger.offset], mutableBigInteger2);
                    if (!z) {
                        return null;
                    }
                    if (divideOneWord == 0) {
                        return new MutableBigInteger();
                    }
                    return new MutableBigInteger(divideOneWord);
                } else if (this.intLen < 6 || (min = Math.min(getLowestSetBit(), mutableBigInteger.getLowestSetBit())) < 96) {
                    return divideMagnitude(mutableBigInteger, mutableBigInteger2, z);
                } else {
                    MutableBigInteger mutableBigInteger3 = new MutableBigInteger(this);
                    MutableBigInteger mutableBigInteger4 = new MutableBigInteger(mutableBigInteger);
                    mutableBigInteger3.rightShift(min);
                    mutableBigInteger4.rightShift(min);
                    MutableBigInteger divideKnuth = mutableBigInteger3.divideKnuth(mutableBigInteger4, mutableBigInteger2);
                    divideKnuth.leftShift(min);
                    return divideKnuth;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger divideAndRemainderBurnikelZiegler(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        int i = this.intLen;
        int i2 = mutableBigInteger.intLen;
        mutableBigInteger2.intLen = 0;
        mutableBigInteger2.offset = 0;
        if (i < i2) {
            return this;
        }
        int numberOfLeadingZeros = 1 << (32 - Integer.numberOfLeadingZeros(i2 / 80));
        int i3 = (((i2 + numberOfLeadingZeros) - 1) / numberOfLeadingZeros) * numberOfLeadingZeros;
        long j = ((long) i3) * 32;
        int max = (int) Math.max(0, j - mutableBigInteger.bitLength());
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(mutableBigInteger);
        mutableBigInteger3.safeLeftShift(max);
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger(this);
        mutableBigInteger4.safeLeftShift(max);
        int bitLength = (int) ((mutableBigInteger4.bitLength() + j) / j);
        if (bitLength < 2) {
            bitLength = 2;
        }
        MutableBigInteger block = mutableBigInteger4.getBlock(bitLength - 1, bitLength, i3);
        int i4 = bitLength - 2;
        MutableBigInteger block2 = mutableBigInteger4.getBlock(i4, bitLength, i3);
        block2.addDisjoint(block, i3);
        MutableBigInteger mutableBigInteger5 = new MutableBigInteger();
        while (i4 > 0) {
            MutableBigInteger divide2n1n = block2.divide2n1n(mutableBigInteger3, mutableBigInteger5);
            MutableBigInteger block3 = mutableBigInteger4.getBlock(i4 - 1, bitLength, i3);
            block3.addDisjoint(divide2n1n, i3);
            mutableBigInteger2.addShifted(mutableBigInteger5, i4 * i3);
            i4--;
            block2 = block3;
        }
        MutableBigInteger divide2n1n2 = block2.divide2n1n(mutableBigInteger3, mutableBigInteger5);
        mutableBigInteger2.add(mutableBigInteger5);
        divide2n1n2.rightShift(max);
        return divide2n1n2;
    }

    private MutableBigInteger divide2n1n(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        int i = mutableBigInteger.intLen;
        if (i % 2 != 0 || i < 80) {
            return divideKnuth(mutableBigInteger, mutableBigInteger2);
        }
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(this);
        int i2 = i / 2;
        mutableBigInteger3.safeRightShift(i2 * 32);
        keepLower(i2);
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger();
        addDisjoint(mutableBigInteger3.divide3n2n(mutableBigInteger, mutableBigInteger4), i2);
        MutableBigInteger divide3n2n = divide3n2n(mutableBigInteger, mutableBigInteger2);
        mutableBigInteger2.addDisjoint(mutableBigInteger4, i2);
        return divide3n2n;
    }

    private MutableBigInteger divide3n2n(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        MutableBigInteger mutableBigInteger3;
        int i = mutableBigInteger.intLen / 2;
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger(this);
        int i2 = i * 32;
        mutableBigInteger4.safeRightShift(i2);
        MutableBigInteger mutableBigInteger5 = new MutableBigInteger(mutableBigInteger);
        mutableBigInteger5.safeRightShift(i2);
        BigInteger lower = mutableBigInteger.getLower(i);
        if (compareShifted(mutableBigInteger, i) < 0) {
            mutableBigInteger4 = mutableBigInteger4.divide2n1n(mutableBigInteger5, mutableBigInteger2);
            mutableBigInteger3 = new MutableBigInteger(mutableBigInteger2.toBigInteger().multiply(lower));
        } else {
            mutableBigInteger2.ones(i);
            mutableBigInteger4.add(mutableBigInteger5);
            mutableBigInteger5.leftShift(i2);
            mutableBigInteger4.subtract(mutableBigInteger5);
            mutableBigInteger3 = new MutableBigInteger(lower);
            mutableBigInteger3.leftShift(i2);
            mutableBigInteger3.subtract(new MutableBigInteger(lower));
        }
        mutableBigInteger4.leftShift(i2);
        mutableBigInteger4.addLower(this, i);
        while (mutableBigInteger4.compare(mutableBigInteger3) < 0) {
            mutableBigInteger4.add(mutableBigInteger);
            mutableBigInteger2.subtract(ONE);
        }
        mutableBigInteger4.subtract(mutableBigInteger3);
        return mutableBigInteger4;
    }

    private MutableBigInteger getBlock(int i, int i2, int i3) {
        int i4 = i * i3;
        int i5 = this.intLen;
        if (i4 >= i5) {
            return new MutableBigInteger();
        }
        int i6 = i == i2 + -1 ? i5 : (i + 1) * i3;
        if (i6 > i5) {
            return new MutableBigInteger();
        }
        int[] iArr = this.value;
        int i7 = this.offset;
        return new MutableBigInteger(Arrays.copyOfRange(iArr, (i7 + i5) - i6, (i7 + i5) - i4));
    }

    /* access modifiers changed from: package-private */
    public long bitLength() {
        int i = this.intLen;
        if (i == 0) {
            return 0;
        }
        return (((long) i) * 32) - ((long) Integer.numberOfLeadingZeros(this.value[this.offset]));
    }

    /* access modifiers changed from: package-private */
    public long divide(long j, MutableBigInteger mutableBigInteger) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i == 0) {
            throw new ArithmeticException("BigInteger divide by zero");
        } else if (this.intLen == 0) {
            mutableBigInteger.offset = 0;
            mutableBigInteger.intLen = 0;
            return 0;
        } else {
            if (i < 0) {
                j = -j;
            }
            int i2 = (int) (j >>> 32);
            mutableBigInteger.clear();
            if (i2 == 0) {
                return ((long) divideOneWord((int) j, mutableBigInteger)) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
            }
            return divideLongMagnitude(j, mutableBigInteger).toLong();
        }
    }

    private static void copyAndShift(int[] iArr, int i, int i2, int[] iArr2, int i3, int i4) {
        int i5 = 32 - i4;
        int i6 = iArr[i];
        int i7 = 0;
        while (i7 < i2 - 1) {
            i++;
            int i8 = iArr[i];
            iArr2[i3 + i7] = (i6 << i4) | (i8 >>> i5);
            i7++;
            i6 = i8;
        }
        iArr2[(i3 + i2) - 1] = i6 << i4;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x01e8  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x01f3  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x021c  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x029c  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x02ac A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x01c8 A[EDGE_INSN: B:88:0x01c8->B:49:0x01c8 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:91:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.math.MutableBigInteger divideMagnitude(java.math.MutableBigInteger r34, java.math.MutableBigInteger r35, boolean r36) {
        /*
            r33 = this;
            r6 = r33
            r0 = r34
            r7 = r35
            int[] r1 = r0.value
            int r2 = r0.offset
            r1 = r1[r2]
            int r14 = java.lang.Integer.numberOfLeadingZeros(r1)
            int r15 = r0.intLen
            r5 = 0
            r4 = 1
            if (r14 <= 0) goto L_0x0080
            int[] r1 = new int[r15]
            int[] r8 = r0.value
            int r9 = r0.offset
            r12 = 0
            r10 = r15
            r11 = r1
            r13 = r14
            copyAndShift(r8, r9, r10, r11, r12, r13)
            int[] r0 = r6.value
            int r2 = r6.offset
            r0 = r0[r2]
            int r0 = java.lang.Integer.numberOfLeadingZeros(r0)
            if (r0 < r14) goto L_0x004b
            int r0 = r6.intLen
            int r0 = r0 + r4
            int[] r11 = new int[r0]
            java.math.MutableBigInteger r0 = new java.math.MutableBigInteger
            r0.<init>((int[]) r11)
            int r2 = r6.intLen
            r0.intLen = r2
            r0.offset = r4
            int[] r8 = r6.value
            int r9 = r6.offset
            int r10 = r6.intLen
            r12 = 1
            r13 = r14
            copyAndShift(r8, r9, r10, r11, r12, r13)
            goto L_0x00a5
        L_0x004b:
            int r0 = r6.intLen
            int r0 = r0 + 2
            int[] r0 = new int[r0]
            java.math.MutableBigInteger r2 = new java.math.MutableBigInteger
            r2.<init>((int[]) r0)
            int r3 = r6.intLen
            int r3 = r3 + r4
            r2.intLen = r3
            r2.offset = r4
            int r3 = r6.offset
            int r8 = 32 - r14
            r9 = r4
            r10 = r5
        L_0x0063:
            int r11 = r6.intLen
            int r12 = r11 + 1
            if (r9 >= r12) goto L_0x0078
            int[] r11 = r6.value
            r11 = r11[r3]
            int r10 = r10 << r14
            int r12 = r11 >>> r8
            r10 = r10 | r12
            r0[r9] = r10
            int r9 = r9 + 1
            int r3 = r3 + r4
            r10 = r11
            goto L_0x0063
        L_0x0078:
            int r11 = r11 + r4
            int r3 = r10 << r14
            r0[r11] = r3
            r9 = r1
            r8 = r2
            goto L_0x00a7
        L_0x0080:
            int[] r1 = r0.value
            int r0 = r0.offset
            int r2 = r0 + r15
            int[] r1 = java.util.Arrays.copyOfRange((int[]) r1, (int) r0, (int) r2)
            java.math.MutableBigInteger r0 = new java.math.MutableBigInteger
            int r2 = r6.intLen
            int r2 = r2 + r4
            int[] r2 = new int[r2]
            r0.<init>((int[]) r2)
            int[] r2 = r6.value
            int r3 = r6.offset
            int[] r8 = r0.value
            int r9 = r6.intLen
            java.lang.System.arraycopy((java.lang.Object) r2, (int) r3, (java.lang.Object) r8, (int) r4, (int) r9)
            int r2 = r6.intLen
            r0.intLen = r2
            r0.offset = r4
        L_0x00a5:
            r8 = r0
            r9 = r1
        L_0x00a7:
            int r0 = r8.intLen
            int r1 = r0 - r15
            int r10 = r1 + 1
            int[] r1 = r7.value
            int r1 = r1.length
            if (r1 >= r10) goto L_0x00b8
            int[] r1 = new int[r10]
            r7.value = r1
            r7.offset = r5
        L_0x00b8:
            r7.intLen = r10
            int[] r11 = r7.value
            int r1 = r8.intLen
            if (r1 != r0) goto L_0x00c9
            r8.offset = r5
            int[] r0 = r8.value
            r0[r5] = r5
            int r1 = r1 + r4
            r8.intLen = r1
        L_0x00c9:
            r12 = r9[r5]
            long r0 = (long) r12
            r16 = 4294967295(0xffffffff, double:2.1219957905E-314)
            long r18 = r0 & r16
            r13 = r9[r4]
            r3 = r5
        L_0x00d6:
            int r2 = r10 + -1
            r20 = -2147483648(0xffffffff80000000, float:-0.0)
            r21 = -1
            r22 = 32
            if (r3 >= r2) goto L_0x01c8
            int[] r2 = r8.value
            int r4 = r8.offset
            int r23 = r3 + r4
            r5 = r2[r23]
            int r0 = r5 + r20
            int r23 = r3 + 1
            int r4 = r23 + r4
            r1 = r2[r4]
            if (r5 != r12) goto L_0x0101
            int r5 = r5 + r1
            int r1 = r5 + r20
            if (r1 >= r0) goto L_0x00f9
            r1 = 1
            goto L_0x00fa
        L_0x00f9:
            r1 = 0
        L_0x00fa:
            r34 = r10
            r26 = r11
            r4 = r21
            goto L_0x0129
        L_0x0101:
            long r4 = (long) r5
            long r4 = r4 << r22
            long r1 = (long) r1
            long r1 = r1 & r16
            long r1 = r1 | r4
            r4 = 0
            int r4 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r4 < 0) goto L_0x011a
            long r4 = r1 / r18
            int r4 = (int) r4
            r34 = r10
            r26 = r11
            long r10 = (long) r4
            long r10 = r10 * r18
            long r1 = r1 - r10
            goto L_0x0127
        L_0x011a:
            r34 = r10
            r26 = r11
            long r1 = divWord(r1, r12)
            long r4 = r1 & r16
            int r4 = (int) r4
            long r1 = r1 >>> r22
        L_0x0127:
            int r5 = (int) r1
            r1 = 0
        L_0x0129:
            if (r4 != 0) goto L_0x0137
            r30 = r12
            r31 = r13
            r27 = r14
            r28 = r15
            r13 = 1
            r14 = 0
            goto L_0x01b4
        L_0x0137:
            if (r1 != 0) goto L_0x017c
            int[] r1 = r8.value
            int r2 = r3 + 2
            int r10 = r8.offset
            int r2 = r2 + r10
            r1 = r1[r2]
            long r1 = (long) r1
            long r1 = r1 & r16
            long r10 = (long) r5
            long r10 = r10 & r16
            long r24 = r10 << r22
            r27 = r14
            r28 = r15
            long r14 = r24 | r1
            r29 = r8
            long r7 = (long) r13
            long r7 = r7 & r16
            r30 = r12
            r31 = r13
            long r12 = (long) r4
            long r12 = r12 & r16
            long r12 = r12 * r7
            boolean r5 = r6.unsignedLongCompare(r12, r14)
            if (r5 == 0) goto L_0x0186
            int r4 = r4 + -1
            long r10 = r10 + r18
            int r5 = (int) r10
            long r10 = (long) r5
            long r10 = r10 & r16
            int r5 = (r10 > r18 ? 1 : (r10 == r18 ? 0 : -1))
            if (r5 < 0) goto L_0x0186
            long r12 = r12 - r7
            long r7 = r10 << r22
            long r1 = r1 | r7
            boolean r1 = r6.unsignedLongCompare(r12, r1)
            if (r1 == 0) goto L_0x0186
            int r4 = r4 + -1
            goto L_0x0186
        L_0x017c:
            r29 = r8
            r30 = r12
            r31 = r13
            r27 = r14
            r28 = r15
        L_0x0186:
            r7 = r4
            r8 = r29
            int[] r1 = r8.value
            int r2 = r8.offset
            int r4 = r3 + r2
            r5 = 0
            r1[r4] = r5
            int r10 = r3 + r2
            r11 = r0
            r0 = r33
            r2 = r9
            r12 = r3
            r3 = r7
            r13 = 1
            r4 = r28
            r14 = r5
            r5 = r10
            int r0 = r0.mulsub(r1, r2, r3, r4, r5)
            int r0 = r0 + r20
            if (r0 <= r11) goto L_0x01b2
            int[] r0 = r8.value
            int r1 = r8.offset
            int r1 = r23 + r1
            r6.divadd(r9, r0, r1)
            int r7 = r7 + -1
        L_0x01b2:
            r26[r12] = r7
        L_0x01b4:
            r10 = r34
            r7 = r35
            r4 = r13
            r5 = r14
            r3 = r23
            r11 = r26
            r14 = r27
            r15 = r28
            r12 = r30
            r13 = r31
            goto L_0x00d6
        L_0x01c8:
            r34 = r10
            r26 = r11
            r30 = r12
            r31 = r13
            r27 = r14
            r28 = r15
            r13 = r4
            r14 = r5
            int[] r0 = r8.value
            int r1 = r8.offset
            int r3 = r2 + r1
            r3 = r0[r3]
            int r7 = r3 + r20
            int r10 = r34 + r1
            r0 = r0[r10]
            r1 = r30
            if (r3 != r1) goto L_0x01f3
            int r3 = r3 + r0
            int r0 = r3 + r20
            if (r0 >= r7) goto L_0x01ef
            r5 = r13
            goto L_0x01f0
        L_0x01ef:
            r5 = r14
        L_0x01f0:
            r0 = r21
            goto L_0x021a
        L_0x01f3:
            long r3 = (long) r3
            long r3 = r3 << r22
            long r10 = (long) r0
            long r10 = r10 & r16
            long r3 = r3 | r10
            r10 = 0
            int r0 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1))
            if (r0 < 0) goto L_0x020a
            long r0 = r3 / r18
            int r0 = (int) r0
            long r10 = (long) r0
            long r10 = r10 * r18
            long r3 = r3 - r10
            int r3 = (int) r3
            r5 = r14
            goto L_0x021a
        L_0x020a:
            long r0 = divWord(r3, r1)
            long r3 = r0 & r16
            int r3 = (int) r3
            long r0 = r0 >>> r22
            int r0 = (int) r0
            r5 = r14
            r32 = r3
            r3 = r0
            r0 = r32
        L_0x021a:
            if (r0 == 0) goto L_0x029a
            if (r5 != 0) goto L_0x025b
            int[] r1 = r8.value
            int r10 = r34 + 1
            int r4 = r8.offset
            int r10 = r10 + r4
            r1 = r1[r10]
            long r4 = (long) r1
            long r4 = r4 & r16
            long r10 = (long) r3
            long r10 = r10 & r16
            long r12 = r10 << r22
            long r12 = r12 | r4
            r1 = r31
            long r14 = (long) r1
            long r14 = r14 & r16
            r34 = r2
            long r1 = (long) r0
            long r1 = r1 & r16
            long r1 = r1 * r14
            boolean r3 = r6.unsignedLongCompare(r1, r12)
            if (r3 == 0) goto L_0x025d
            int r0 = r0 + -1
            long r10 = r10 + r18
            int r3 = (int) r10
            long r10 = (long) r3
            long r10 = r10 & r16
            int r3 = (r10 > r18 ? 1 : (r10 == r18 ? 0 : -1))
            if (r3 < 0) goto L_0x025d
            long r1 = r1 - r14
            long r10 = r10 << r22
            long r3 = r10 | r4
            boolean r1 = r6.unsignedLongCompare(r1, r3)
            if (r1 == 0) goto L_0x025d
            int r0 = r0 + -1
            goto L_0x025d
        L_0x025b:
            r34 = r2
        L_0x025d:
            r10 = r0
            int[] r1 = r8.value
            int r0 = r8.offset
            int r2 = r34 + r0
            r3 = 0
            r1[r2] = r3
            if (r36 == 0) goto L_0x0278
            int r5 = r34 + r0
            r0 = r33
            r11 = r34
            r2 = r9
            r3 = r10
            r4 = r28
            int r0 = r0.mulsub(r1, r2, r3, r4, r5)
            goto L_0x0286
        L_0x0278:
            r11 = r34
            int r5 = r11 + r0
            r0 = r33
            r2 = r9
            r3 = r10
            r4 = r28
            int r0 = r0.mulsubBorrow(r1, r2, r3, r4, r5)
        L_0x0286:
            int r0 = r0 + r20
            if (r0 <= r7) goto L_0x0298
            if (r36 == 0) goto L_0x0296
            int[] r0 = r8.value
            int r2 = r11 + 1
            int r1 = r8.offset
            int r2 = r2 + r1
            r6.divadd(r9, r0, r2)
        L_0x0296:
            int r10 = r10 + -1
        L_0x0298:
            r26[r11] = r10
        L_0x029a:
            if (r36 == 0) goto L_0x02a6
            if (r27 <= 0) goto L_0x02a3
            r0 = r27
            r8.rightShift(r0)
        L_0x02a3:
            r8.normalize()
        L_0x02a6:
            r35.normalize()
            if (r36 == 0) goto L_0x02ac
            goto L_0x02ad
        L_0x02ac:
            r8 = 0
        L_0x02ad:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.MutableBigInteger.divideMagnitude(java.math.MutableBigInteger, java.math.MutableBigInteger, boolean):java.math.MutableBigInteger");
    }

    private MutableBigInteger divideLongMagnitude(long j, MutableBigInteger mutableBigInteger) {
        long j2;
        int i;
        int i2;
        int i3;
        boolean z;
        int[] iArr;
        int i4;
        boolean z2;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        long j3;
        MutableBigInteger mutableBigInteger2 = mutableBigInteger;
        boolean z3 = true;
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(new int[(this.intLen + 1)]);
        System.arraycopy((Object) this.value, this.offset, (Object) mutableBigInteger3.value, 1, this.intLen);
        int i11 = this.intLen;
        mutableBigInteger3.intLen = i11;
        mutableBigInteger3.offset = 1;
        int i12 = (i11 - 2) + 1;
        boolean z4 = false;
        if (mutableBigInteger2.value.length < i12) {
            mutableBigInteger2.value = new int[i12];
            mutableBigInteger2.offset = 0;
        }
        mutableBigInteger2.intLen = i12;
        int[] iArr2 = mutableBigInteger2.value;
        int numberOfLeadingZeros = Long.numberOfLeadingZeros(j);
        if (numberOfLeadingZeros > 0) {
            j2 = j << numberOfLeadingZeros;
            mutableBigInteger3.leftShift(numberOfLeadingZeros);
        } else {
            j2 = j;
        }
        int i13 = mutableBigInteger3.intLen;
        if (i13 == i11) {
            mutableBigInteger3.offset = 0;
            mutableBigInteger3.value[0] = 0;
            mutableBigInteger3.intLen = i13 + 1;
        }
        char c = ' ';
        int i14 = (int) (j2 >>> 32);
        long j4 = ((long) i14) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        int i15 = (int) (j2 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
        int i16 = 0;
        while (i16 < i12) {
            int[] iArr3 = mutableBigInteger3.value;
            int i17 = mutableBigInteger3.offset;
            int i18 = iArr3[i16 + i17];
            int i19 = i18 - 2147483648;
            int i20 = i16 + 1;
            int i21 = iArr3[i20 + i17];
            if (i18 == i14) {
                i2 = i18 + i21;
                z = i2 + Integer.MIN_VALUE < i19 ? z3 : z4;
                i3 = -1;
                i = i12;
            } else {
                i = i12;
                long j5 = (((long) i18) << c) | (((long) i21) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
                if (j5 >= 0) {
                    i10 = (int) (j5 / j4);
                    j3 = j5 - (((long) i10) * j4);
                } else {
                    long divWord = divWord(j5, i14);
                    i10 = (int) (divWord & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
                    j3 = divWord >>> c;
                }
                i3 = i10;
                i2 = (int) j3;
                z = z4;
            }
            if (i3 == 0) {
                z2 = z4;
                iArr = iArr2;
                i5 = i14;
                i4 = i15;
            } else {
                if (!z) {
                    long j6 = ((long) mutableBigInteger3.value[i16 + 2 + mutableBigInteger3.offset]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
                    iArr = iArr2;
                    long j7 = ((long) i2) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
                    i6 = i14;
                    long j8 = (j7 << c) | j6;
                    i9 = i19;
                    long j9 = ((long) i15) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
                    i7 = i16;
                    i8 = i15;
                    long j10 = (((long) i3) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) * j9;
                    if (unsignedLongCompare(j10, j8)) {
                        i3--;
                        long j11 = ((long) ((int) (j7 + j4))) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
                        if (j11 >= j4) {
                            c = ' ';
                            if (unsignedLongCompare(j10 - j9, (j11 << 32) | j6)) {
                                i3--;
                            }
                        }
                    }
                    c = ' ';
                } else {
                    i9 = i19;
                    i7 = i16;
                    i8 = i15;
                    iArr = iArr2;
                    i6 = i14;
                }
                int i22 = i3;
                int[] iArr4 = mutableBigInteger3.value;
                int i23 = mutableBigInteger3.offset;
                z2 = false;
                iArr4[i7 + i23] = 0;
                int i24 = i7;
                i4 = i8;
                if (mulsubLong(iArr4, i6, i8, i22, i7 + i23) - 2147483648 > i9) {
                    i5 = i6;
                    divaddLong(i5, i4, mutableBigInteger3.value, i20 + mutableBigInteger3.offset);
                    i22--;
                } else {
                    i5 = i6;
                }
                iArr[i24] = i22;
            }
            z4 = z2;
            i15 = i4;
            i16 = i20;
            i12 = i;
            iArr2 = iArr;
            z3 = true;
            i14 = i5;
        }
        if (numberOfLeadingZeros > 0) {
            mutableBigInteger3.rightShift(numberOfLeadingZeros);
        }
        mutableBigInteger.normalize();
        mutableBigInteger3.normalize();
        return mutableBigInteger3;
    }

    private int divaddLong(int i, int i2, int[] iArr, int i3) {
        int i4 = i3 + 1;
        iArr[i4] = (int) ((((long) i2) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + (((long) iArr[i4]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER));
        long j = (((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + (((long) iArr[i3]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) + 0;
        iArr[i3] = (int) j;
        return (int) (j >>> 32);
    }

    private int mulsubLong(int[] iArr, int i, int i2, int i3, int i4) {
        long j = ((long) i3) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        int i5 = i4 + 2;
        long j2 = (((long) i2) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) * j;
        long j3 = ((long) iArr[i5]) - j2;
        int i6 = i5 - 1;
        iArr[i5] = (int) j3;
        int i7 = 1;
        long j4 = ((((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) * j) + (j2 >>> 32) + ((long) ((j3 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) > (((long) (~((int) j2))) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) ? 1 : 0));
        long j5 = ((long) iArr[i6]) - j4;
        iArr[i6] = (int) j5;
        long j6 = j4 >>> 32;
        if ((j5 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) <= (((long) (~((int) j4))) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER)) {
            i7 = 0;
        }
        return (int) (j6 + ((long) i7));
    }

    static long divWord(long j, int i) {
        long j2 = ((long) i) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
        if (j2 == 1) {
            return (((long) ((int) j)) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) | 0;
        }
        long j3 = (j >>> 1) / (j2 >>> 1);
        long j4 = j - (j3 * j2);
        while (j4 < 0) {
            j4 += j2;
            j3--;
        }
        while (j4 >= j2) {
            j4 -= j2;
            j3++;
        }
        return (j4 << 32) | (j3 & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger sqrt() {
        if (isZero()) {
            return new MutableBigInteger(0);
        }
        int[] iArr = this.value;
        if (iArr.length == 1 && (((long) iArr[0]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) < 4) {
            return ONE;
        }
        if (bitLength() <= 63) {
            long longValueExact = new BigInteger(this.value, 1).longValueExact();
            long floor = (long) Math.floor(Math.sqrt((double) longValueExact));
            while (true) {
                long j = ((longValueExact / floor) + floor) / 2;
                if (j >= floor) {
                    return new MutableBigInteger(new int[]{(int) (floor >>> 32), (int) (floor & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER)});
                }
                floor = j;
            }
        } else {
            int bitLength = (int) bitLength();
            if (((long) bitLength) == bitLength()) {
                int i = bitLength - 63;
                if (i % 2 == 1) {
                    i++;
                }
                MutableBigInteger mutableBigInteger = new MutableBigInteger(this);
                mutableBigInteger.rightShift(i);
                mutableBigInteger.normalize();
                MutableBigInteger mutableBigInteger2 = new MutableBigInteger(BigInteger.valueOf((long) Math.ceil(Math.sqrt(new BigInteger(mutableBigInteger.value, 1).doubleValue()))).mag);
                mutableBigInteger2.leftShift(i / 2);
                MutableBigInteger mutableBigInteger3 = new MutableBigInteger();
                while (true) {
                    divide(mutableBigInteger2, mutableBigInteger3, false);
                    mutableBigInteger3.add(mutableBigInteger2);
                    mutableBigInteger3.rightShift(1);
                    if (mutableBigInteger3.compare(mutableBigInteger2) >= 0) {
                        return mutableBigInteger2;
                    }
                    mutableBigInteger2.copyValue(mutableBigInteger3);
                    mutableBigInteger3.reset();
                }
            } else {
                throw new ArithmeticException("bitLength() integer overflow");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger hybridGCD(MutableBigInteger mutableBigInteger) {
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        while (true) {
            MutableBigInteger mutableBigInteger3 = mutableBigInteger;
            MutableBigInteger mutableBigInteger4 = this;
            this = mutableBigInteger3;
            int i = this.intLen;
            if (i == 0) {
                return mutableBigInteger4;
            }
            if (Math.abs(mutableBigInteger4.intLen - i) < 2) {
                return mutableBigInteger4.binaryGCD(this);
            }
            mutableBigInteger = mutableBigInteger4.divide(this, mutableBigInteger2);
        }
    }

    private MutableBigInteger binaryGCD(MutableBigInteger mutableBigInteger) {
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        int lowestSetBit = getLowestSetBit();
        int lowestSetBit2 = mutableBigInteger.getLowestSetBit();
        if (lowestSetBit < lowestSetBit2) {
            lowestSetBit2 = lowestSetBit;
        }
        if (lowestSetBit2 != 0) {
            rightShift(lowestSetBit2);
            mutableBigInteger.rightShift(lowestSetBit2);
        }
        boolean z = lowestSetBit2 == lowestSetBit;
        MutableBigInteger mutableBigInteger3 = z ? mutableBigInteger : this;
        int i = z ? -1 : 1;
        while (true) {
            int lowestSetBit3 = mutableBigInteger3.getLowestSetBit();
            if (lowestSetBit3 < 0) {
                break;
            }
            mutableBigInteger3.rightShift(lowestSetBit3);
            if (i > 0) {
                this = mutableBigInteger3;
            } else {
                mutableBigInteger = mutableBigInteger3;
            }
            if (this.intLen >= 2 || mutableBigInteger.intLen >= 2) {
                i = this.difference(mutableBigInteger);
                if (i == 0) {
                    break;
                }
                mutableBigInteger3 = i >= 0 ? this : mutableBigInteger;
            } else {
                mutableBigInteger2.value[0] = binaryGcd(this.value[this.offset], mutableBigInteger.value[mutableBigInteger.offset]);
                mutableBigInteger2.intLen = 1;
                mutableBigInteger2.offset = 0;
                if (lowestSetBit2 > 0) {
                    mutableBigInteger2.leftShift(lowestSetBit2);
                }
                return mutableBigInteger2;
            }
        }
        if (lowestSetBit2 > 0) {
            this.leftShift(lowestSetBit2);
        }
        return this;
    }

    static int binaryGcd(int i, int i2) {
        if (i2 == 0) {
            return i;
        }
        if (i == 0) {
            return i2;
        }
        int numberOfTrailingZeros = Integer.numberOfTrailingZeros(i);
        int numberOfTrailingZeros2 = Integer.numberOfTrailingZeros(i2);
        int i3 = i >>> numberOfTrailingZeros;
        int i4 = i2 >>> numberOfTrailingZeros2;
        if (numberOfTrailingZeros >= numberOfTrailingZeros2) {
            numberOfTrailingZeros = numberOfTrailingZeros2;
        }
        while (i3 != i4) {
            if (i3 - 2147483648 > Integer.MIN_VALUE + i4) {
                int i5 = i3 - i4;
                i3 = i5 >>> Integer.numberOfTrailingZeros(i5);
            } else {
                int i6 = i4 - i3;
                i4 = i6 >>> Integer.numberOfTrailingZeros(i6);
            }
        }
        return i3 << numberOfTrailingZeros;
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger mutableModInverse(MutableBigInteger mutableBigInteger) {
        if (mutableBigInteger.isOdd()) {
            return modInverse(mutableBigInteger);
        }
        if (!isEven()) {
            int lowestSetBit = mutableBigInteger.getLowestSetBit();
            MutableBigInteger mutableBigInteger2 = new MutableBigInteger(mutableBigInteger);
            mutableBigInteger2.rightShift(lowestSetBit);
            if (mutableBigInteger2.isOne()) {
                return modInverseMP2(lowestSetBit);
            }
            MutableBigInteger modInverse = modInverse(mutableBigInteger2);
            MutableBigInteger modInverseMP2 = modInverseMP2(lowestSetBit);
            MutableBigInteger modInverseBP2 = modInverseBP2(mutableBigInteger2, lowestSetBit);
            MutableBigInteger modInverseMP22 = mutableBigInteger2.modInverseMP2(lowestSetBit);
            MutableBigInteger mutableBigInteger3 = new MutableBigInteger();
            MutableBigInteger mutableBigInteger4 = new MutableBigInteger();
            MutableBigInteger mutableBigInteger5 = new MutableBigInteger();
            modInverse.leftShift(lowestSetBit);
            modInverse.multiply(modInverseBP2, mutableBigInteger5);
            modInverseMP2.multiply(mutableBigInteger2, mutableBigInteger3);
            mutableBigInteger3.multiply(modInverseMP22, mutableBigInteger4);
            mutableBigInteger5.add(mutableBigInteger4);
            return mutableBigInteger5.divide(mutableBigInteger, mutableBigInteger3);
        }
        throw new ArithmeticException("BigInteger not invertible.");
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger modInverseMP2(int i) {
        if (isEven()) {
            throw new ArithmeticException("Non-invertible. (GCD != 1)");
        } else if (i > 64) {
            return euclidModInverse(i);
        } else {
            int inverseMod32 = inverseMod32(this.value[(this.offset + this.intLen) - 1]);
            if (i < 33) {
                if (i != 32) {
                    inverseMod32 &= (1 << i) - 1;
                }
                return new MutableBigInteger(inverseMod32);
            }
            int[] iArr = this.value;
            int i2 = this.offset;
            int i3 = this.intLen;
            long j = ((long) iArr[(i2 + i3) - 1]) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
            if (i3 > 1) {
                j |= ((long) iArr[(i2 + i3) - 2]) << 32;
            }
            long j2 = ((long) inverseMod32) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
            long j3 = j2 * (2 - (j * j2));
            if (i != 64) {
                j3 &= (1 << i) - 1;
            }
            MutableBigInteger mutableBigInteger = new MutableBigInteger(new int[2]);
            int[] iArr2 = mutableBigInteger.value;
            iArr2[0] = (int) (j3 >>> 32);
            iArr2[1] = (int) j3;
            mutableBigInteger.intLen = 2;
            mutableBigInteger.normalize();
            return mutableBigInteger;
        }
    }

    static MutableBigInteger modInverseBP2(MutableBigInteger mutableBigInteger, int i) {
        return fixup(new MutableBigInteger(1), new MutableBigInteger(mutableBigInteger), i);
    }

    private MutableBigInteger modInverse(MutableBigInteger mutableBigInteger) {
        int i;
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger(mutableBigInteger);
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(this);
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger(mutableBigInteger2);
        SignedMutableBigInteger signedMutableBigInteger = new SignedMutableBigInteger(1);
        SignedMutableBigInteger signedMutableBigInteger2 = new SignedMutableBigInteger();
        if (mutableBigInteger3.isEven()) {
            i = mutableBigInteger3.getLowestSetBit();
            mutableBigInteger3.rightShift(i);
            signedMutableBigInteger2.leftShift(i);
        } else {
            i = 0;
        }
        while (!mutableBigInteger3.isOne()) {
            if (!mutableBigInteger3.isZero()) {
                if (mutableBigInteger3.compare(mutableBigInteger4) < 0) {
                    MutableBigInteger mutableBigInteger5 = mutableBigInteger3;
                    mutableBigInteger3 = mutableBigInteger4;
                    mutableBigInteger4 = mutableBigInteger5;
                    SignedMutableBigInteger signedMutableBigInteger3 = signedMutableBigInteger2;
                    signedMutableBigInteger2 = signedMutableBigInteger;
                    signedMutableBigInteger = signedMutableBigInteger3;
                }
                if (((mutableBigInteger3.value[(mutableBigInteger3.offset + mutableBigInteger3.intLen) - 1] ^ mutableBigInteger4.value[(mutableBigInteger4.offset + mutableBigInteger4.intLen) - 1]) & 3) == 0) {
                    mutableBigInteger3.subtract(mutableBigInteger4);
                    signedMutableBigInteger.signedSubtract(signedMutableBigInteger2);
                } else {
                    mutableBigInteger3.add(mutableBigInteger4);
                    signedMutableBigInteger.signedAdd(signedMutableBigInteger2);
                }
                int lowestSetBit = mutableBigInteger3.getLowestSetBit();
                mutableBigInteger3.rightShift(lowestSetBit);
                signedMutableBigInteger2.leftShift(lowestSetBit);
                i += lowestSetBit;
            } else {
                throw new ArithmeticException("BigInteger not invertible.");
            }
        }
        if (signedMutableBigInteger.compare(mutableBigInteger2) >= 0) {
            signedMutableBigInteger.copyValue(signedMutableBigInteger.divide(mutableBigInteger2, new MutableBigInteger()));
        }
        if (signedMutableBigInteger.sign < 0) {
            signedMutableBigInteger.signedAdd(mutableBigInteger2);
        }
        return fixup(signedMutableBigInteger, mutableBigInteger2, i);
    }

    static MutableBigInteger fixup(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2, int i) {
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger();
        int i2 = -inverseMod32(mutableBigInteger2.value[(mutableBigInteger2.offset + mutableBigInteger2.intLen) - 1]);
        int i3 = i >> 5;
        for (int i4 = 0; i4 < i3; i4++) {
            mutableBigInteger2.mul(mutableBigInteger.value[(mutableBigInteger.offset + mutableBigInteger.intLen) - 1] * i2, mutableBigInteger3);
            mutableBigInteger.add(mutableBigInteger3);
            mutableBigInteger.intLen--;
        }
        int i5 = i & 31;
        if (i5 != 0) {
            mutableBigInteger2.mul((i2 * mutableBigInteger.value[(mutableBigInteger.offset + mutableBigInteger.intLen) - 1]) & ((1 << i5) - 1), mutableBigInteger3);
            mutableBigInteger.add(mutableBigInteger3);
            mutableBigInteger.rightShift(i5);
        }
        return mutableBigInteger.compare(mutableBigInteger2) >= 0 ? mutableBigInteger.divide(mutableBigInteger2, new MutableBigInteger()) : mutableBigInteger;
    }

    /* access modifiers changed from: package-private */
    public MutableBigInteger euclidModInverse(int i) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(1);
        mutableBigInteger.leftShift(i);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger(mutableBigInteger);
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(this);
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger();
        MutableBigInteger divide = mutableBigInteger.divide(mutableBigInteger3, mutableBigInteger4);
        MutableBigInteger mutableBigInteger5 = new MutableBigInteger(mutableBigInteger4);
        MutableBigInteger mutableBigInteger6 = new MutableBigInteger(1);
        MutableBigInteger mutableBigInteger7 = new MutableBigInteger();
        while (!divide.isOne()) {
            mutableBigInteger3 = mutableBigInteger3.divide(divide, mutableBigInteger4);
            if (mutableBigInteger3.intLen != 0) {
                if (mutableBigInteger4.intLen == 1) {
                    mutableBigInteger5.mul(mutableBigInteger4.value[mutableBigInteger4.offset], mutableBigInteger7);
                } else {
                    mutableBigInteger4.multiply(mutableBigInteger5, mutableBigInteger7);
                }
                mutableBigInteger6.add(mutableBigInteger7);
                if (mutableBigInteger3.isOne()) {
                    return mutableBigInteger6;
                }
                divide = divide.divide(mutableBigInteger3, mutableBigInteger7);
                if (divide.intLen != 0) {
                    if (mutableBigInteger7.intLen == 1) {
                        mutableBigInteger6.mul(mutableBigInteger7.value[mutableBigInteger7.offset], mutableBigInteger4);
                    } else {
                        mutableBigInteger7.multiply(mutableBigInteger6, mutableBigInteger4);
                    }
                    mutableBigInteger5.add(mutableBigInteger4);
                } else {
                    throw new ArithmeticException("BigInteger not invertible.");
                }
            } else {
                throw new ArithmeticException("BigInteger not invertible.");
            }
        }
        mutableBigInteger2.subtract(mutableBigInteger5);
        return mutableBigInteger2;
    }
}

package jdk.internal.math;

import java.math.BigInteger;
import java.util.Arrays;

public class FDBigInteger {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final long[] LONG_5_POW = {1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125, 6103515625L, 30517578125L, 152587890625L, 762939453125L, 3814697265625L, 19073486328125L, 95367431640625L, 476837158203125L, 2384185791015625L, 11920928955078125L, 59604644775390625L, 298023223876953125L, 1490116119384765625L};
    private static final long LONG_MASK = 4294967295L;
    private static final int MAX_FIVE_POW = 340;
    private static final FDBigInteger[] POW_5_CACHE = new FDBigInteger[340];
    static final int[] SMALL_5_POW = {1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125};
    public static final FDBigInteger ZERO;
    private int[] data;
    private boolean isImmutable = false;
    private int nWords;
    private int offset;

    static {
        int i = 0;
        while (true) {
            int[] iArr = SMALL_5_POW;
            if (i >= iArr.length) {
                break;
            }
            FDBigInteger fDBigInteger = new FDBigInteger(new int[]{iArr[i]}, 0);
            fDBigInteger.makeImmutable();
            POW_5_CACHE[i] = fDBigInteger;
            i++;
        }
        FDBigInteger fDBigInteger2 = POW_5_CACHE[i - 1];
        while (i < 340) {
            FDBigInteger[] fDBigIntegerArr = POW_5_CACHE;
            fDBigInteger2 = fDBigInteger2.mult(5);
            fDBigIntegerArr[i] = fDBigInteger2;
            fDBigInteger2.makeImmutable();
            i++;
        }
        FDBigInteger fDBigInteger3 = new FDBigInteger(new int[0], 0);
        ZERO = fDBigInteger3;
        fDBigInteger3.makeImmutable();
    }

    private FDBigInteger(int[] iArr, int i) {
        this.data = iArr;
        this.offset = i;
        this.nWords = iArr.length;
        trimLeadingZeros();
    }

    public FDBigInteger(long j, char[] cArr, int i, int i2) {
        int i3 = 0;
        int[] iArr = new int[Math.max((i2 + 8) / 9, 2)];
        this.data = iArr;
        iArr[0] = (int) j;
        iArr[1] = (int) (j >>> 32);
        this.offset = 0;
        this.nWords = 2;
        int i4 = i2 - 5;
        while (i < i4) {
            int i5 = i + 5;
            int i6 = cArr[i] - '0';
            i++;
            while (i < i5) {
                i6 = ((i6 * 10) + cArr[i]) - 48;
                i++;
            }
            multAddMe(100000, i6);
        }
        int i7 = 1;
        while (i < i2) {
            i3 = ((i3 * 10) + cArr[i]) - 48;
            i7 *= 10;
            i++;
        }
        if (i7 != 1) {
            multAddMe(i7, i3);
        }
        trimLeadingZeros();
    }

    public static FDBigInteger valueOfPow52(int i, int i2) {
        if (i == 0) {
            return valueOfPow2(i2);
        }
        if (i2 == 0) {
            return big5pow(i);
        }
        int[] iArr = SMALL_5_POW;
        if (i >= iArr.length) {
            return big5pow(i).leftShift(i2);
        }
        int i3 = iArr[i];
        int i4 = i2 >> 5;
        int i5 = i2 & 31;
        if (i5 == 0) {
            return new FDBigInteger(new int[]{i3}, i4);
        }
        return new FDBigInteger(new int[]{i3 << i5, i3 >>> (32 - i5)}, i4);
    }

    public static FDBigInteger valueOfMulPow52(long j, int i, int i2) {
        int[] iArr;
        long j2 = j;
        int i3 = i;
        int i4 = i2;
        int i5 = (int) j2;
        int i6 = (int) (j2 >>> 32);
        int i7 = i4 >> 5;
        int i8 = i4 & 31;
        if (i3 != 0) {
            int[] iArr2 = SMALL_5_POW;
            if (i3 < iArr2.length) {
                long j3 = ((long) iArr2[i3]) & 4294967295L;
                long j4 = (((long) i5) & 4294967295L) * j3;
                int i9 = (int) j4;
                long j5 = ((((long) i6) & 4294967295L) * j3) + (j4 >>> 32);
                int i10 = (int) j5;
                int i11 = (int) (j5 >>> 32);
                if (i8 == 0) {
                    return new FDBigInteger(new int[]{i9, i10, i11}, i7);
                }
                int i12 = 32 - i8;
                return new FDBigInteger(new int[]{i9 << i8, (i9 >>> i12) | (i10 << i8), (i10 >>> i12) | (i11 << i8), i11 >>> i12}, i7);
            }
            FDBigInteger big5pow = big5pow(i);
            if (i6 == 0) {
                int i13 = big5pow.nWords;
                iArr = new int[(i13 + 1 + (i4 != 0 ? 1 : 0))];
                mult(big5pow.data, i13, i5, iArr);
            } else {
                int i14 = big5pow.nWords;
                int[] iArr3 = new int[(i14 + 2 + (i4 != 0 ? 1 : 0))];
                mult(big5pow.data, i14, i5, i6, iArr3);
                iArr = iArr3;
            }
            return new FDBigInteger(iArr, big5pow.offset).leftShift(i4);
        } else if (i4 == 0) {
            return new FDBigInteger(new int[]{i5, i6}, 0);
        } else if (i8 == 0) {
            return new FDBigInteger(new int[]{i5, i6}, i7);
        } else {
            int i15 = 32 - i8;
            return new FDBigInteger(new int[]{i5 << i8, (i5 >>> i15) | (i6 << i8), i6 >>> i15}, i7);
        }
    }

    private static FDBigInteger valueOfPow2(int i) {
        return new FDBigInteger(new int[]{1 << (i & 31)}, i >> 5);
    }

    private void trimLeadingZeros() {
        int i = this.nWords;
        if (i > 0) {
            int i2 = i - 1;
            if (this.data[i2] == 0) {
                while (i2 > 0 && this.data[i2 - 1] == 0) {
                    i2--;
                }
                this.nWords = i2;
                if (i2 == 0) {
                    this.offset = 0;
                }
            }
        }
    }

    public int getNormalizationBias() {
        int i = this.nWords;
        if (i != 0) {
            int numberOfLeadingZeros = Integer.numberOfLeadingZeros(this.data[i - 1]);
            return numberOfLeadingZeros < 4 ? numberOfLeadingZeros + 28 : numberOfLeadingZeros - 4;
        }
        throw new IllegalArgumentException("Zero value cannot be normalized");
    }

    private static void leftShift(int[] iArr, int i, int[] iArr2, int i2, int i3, int i4) {
        while (i > 0) {
            int i5 = iArr[i - 1];
            iArr2[i] = (i4 << i2) | (i5 >>> i3);
            i--;
            i4 = i5;
        }
        iArr2[0] = i4 << i2;
    }

    public FDBigInteger leftShift(int i) {
        int i2;
        int[] iArr;
        int i3;
        int[] iArr2;
        if (!(i == 0 || (i2 = this.nWords) == 0)) {
            int i4 = i >> 5;
            int i5 = i & 31;
            if (!this.isImmutable) {
                if (i5 != 0) {
                    int i6 = 32 - i5;
                    int[] iArr3 = this.data;
                    int i7 = 0;
                    int i8 = iArr3[0];
                    if ((i8 << i5) == 0) {
                        while (true) {
                            i3 = this.nWords;
                            if (i7 >= i3 - 1) {
                                break;
                            }
                            int i9 = i8 >>> i6;
                            int[] iArr4 = this.data;
                            int i10 = i7 + 1;
                            int i11 = iArr4[i10];
                            iArr4[i7] = i9 | (i11 << i5);
                            i7 = i10;
                            i8 = i11;
                        }
                        int i12 = i8 >>> i6;
                        this.data[i7] = i12;
                        if (i12 == 0) {
                            this.nWords = i3 - 1;
                        }
                        this.offset++;
                    } else {
                        int i13 = i2 - 1;
                        int i14 = iArr3[i13];
                        int i15 = i14 >>> i6;
                        if (i15 != 0) {
                            if (i2 == iArr3.length) {
                                iArr = new int[(i2 + 1)];
                                this.data = iArr;
                            } else {
                                iArr = iArr3;
                            }
                            this.nWords = i2 + 1;
                            iArr[i2] = i15;
                        } else {
                            iArr = iArr3;
                        }
                        leftShift(iArr3, i13, iArr, i5, i6, i14);
                    }
                }
                this.offset += i4;
            } else if (i5 == 0) {
                return new FDBigInteger(Arrays.copyOf(this.data, i2), this.offset + i4);
            } else {
                int i16 = 32 - i5;
                int i17 = i2 - 1;
                int[] iArr5 = this.data;
                int i18 = iArr5[i17];
                int i19 = i18 >>> i16;
                if (i19 != 0) {
                    iArr2 = new int[(i2 + 1)];
                    iArr2[i2] = i19;
                } else {
                    iArr2 = new int[i2];
                }
                int[] iArr6 = iArr2;
                leftShift(iArr5, i17, iArr6, i5, i16, i18);
                return new FDBigInteger(iArr6, this.offset + i4);
            }
        }
        return this;
    }

    private int size() {
        return this.nWords + this.offset;
    }

    public int quoRemIteration(FDBigInteger fDBigInteger) throws IllegalArgumentException {
        FDBigInteger fDBigInteger2 = this;
        FDBigInteger fDBigInteger3 = fDBigInteger;
        int size = size();
        int size2 = fDBigInteger.size();
        if (size < size2) {
            int[] iArr = fDBigInteger2.data;
            int multAndCarryBy10 = multAndCarryBy10(iArr, fDBigInteger2.nWords, iArr);
            if (multAndCarryBy10 != 0) {
                int[] iArr2 = fDBigInteger2.data;
                int i = fDBigInteger2.nWords;
                fDBigInteger2.nWords = i + 1;
                iArr2[i] = multAndCarryBy10;
            } else {
                trimLeadingZeros();
            }
            return 0;
        } else if (size <= size2) {
            long j = (((long) fDBigInteger2.data[fDBigInteger2.nWords - 1]) & 4294967295L) / (((long) fDBigInteger3.data[fDBigInteger3.nWords - 1]) & 4294967295L);
            long j2 = 0;
            if (fDBigInteger2.multDiffMe(j, fDBigInteger3) != 0) {
                int i2 = fDBigInteger3.offset - fDBigInteger2.offset;
                int[] iArr3 = fDBigInteger3.data;
                int[] iArr4 = fDBigInteger2.data;
                long j3 = 0;
                while (j3 == j2) {
                    int i3 = 0;
                    int i4 = i2;
                    while (i4 < fDBigInteger2.nWords) {
                        long j4 = j3 + (((long) iArr4[i4]) & 4294967295L) + (((long) iArr3[i3]) & 4294967295L);
                        iArr4[i4] = (int) j4;
                        j3 = j4 >>> 32;
                        i3++;
                        i4++;
                        fDBigInteger2 = this;
                        iArr3 = iArr3;
                    }
                    j--;
                    j2 = 0;
                    fDBigInteger2 = this;
                    iArr3 = iArr3;
                }
            }
            int[] iArr5 = this.data;
            multAndCarryBy10(iArr5, this.nWords, iArr5);
            trimLeadingZeros();
            return (int) j;
        } else {
            throw new IllegalArgumentException("disparate values");
        }
    }

    public FDBigInteger multBy10() {
        int i = this.nWords;
        if (i == 0) {
            return this;
        }
        if (this.isImmutable) {
            int[] iArr = new int[(i + 1)];
            iArr[i] = multAndCarryBy10(this.data, i, iArr);
            return new FDBigInteger(iArr, this.offset);
        }
        int[] iArr2 = this.data;
        int multAndCarryBy10 = multAndCarryBy10(iArr2, i, iArr2);
        if (multAndCarryBy10 != 0) {
            int i2 = this.nWords;
            int[] iArr3 = this.data;
            if (i2 == iArr3.length) {
                if (iArr3[0] == 0) {
                    int i3 = i2 - 1;
                    this.nWords = i3;
                    System.arraycopy((Object) iArr3, 1, (Object) iArr3, 0, i3);
                    this.offset++;
                } else {
                    this.data = Arrays.copyOf(iArr3, iArr3.length + 1);
                }
            }
            int[] iArr4 = this.data;
            int i4 = this.nWords;
            this.nWords = i4 + 1;
            iArr4[i4] = multAndCarryBy10;
        } else {
            trimLeadingZeros();
        }
        return this;
    }

    public FDBigInteger multByPow52(int i, int i2) {
        int i3 = this.nWords;
        if (i3 == 0) {
            return this;
        }
        if (i != 0) {
            int i4 = i2 != 0 ? 1 : 0;
            int[] iArr = SMALL_5_POW;
            if (i < iArr.length) {
                int[] iArr2 = new int[(i3 + 1 + i4)];
                mult(this.data, i3, iArr[i], iArr2);
                this = new FDBigInteger(iArr2, this.offset);
            } else {
                FDBigInteger big5pow = big5pow(i);
                int[] iArr3 = new int[(this.nWords + big5pow.size() + i4)];
                mult(this.data, this.nWords, big5pow.data, big5pow.nWords, iArr3);
                this = new FDBigInteger(iArr3, this.offset + big5pow.offset);
            }
        }
        return this.leftShift(i2);
    }

    private static void mult(int[] iArr, int i, int[] iArr2, int i2, int[] iArr3) {
        int i3 = i2;
        int i4 = i;
        for (int i5 = 0; i5 < i4; i5++) {
            long j = ((long) iArr[i5]) & 4294967295L;
            long j2 = 0;
            for (int i6 = 0; i6 < i3; i6++) {
                int i7 = i5 + i6;
                long j3 = j2 + (((long) iArr3[i7]) & 4294967295L) + ((((long) iArr2[i6]) & 4294967295L) * j);
                iArr3[i7] = (int) j3;
                j2 = j3 >>> 32;
            }
            iArr3[i5 + i3] = (int) j2;
        }
    }

    public FDBigInteger leftInplaceSub(FDBigInteger fDBigInteger) {
        FDBigInteger fDBigInteger2 = this;
        FDBigInteger fDBigInteger3 = fDBigInteger;
        if (fDBigInteger2.isImmutable) {
            fDBigInteger2 = new FDBigInteger((int[]) fDBigInteger2.data.clone(), fDBigInteger2.offset);
        }
        int i = fDBigInteger3.offset - fDBigInteger2.offset;
        int[] iArr = fDBigInteger3.data;
        int[] iArr2 = fDBigInteger2.data;
        int i2 = fDBigInteger3.nWords;
        int i3 = fDBigInteger2.nWords;
        int i4 = 0;
        if (i < 0) {
            int i5 = i3 - i;
            if (i5 < iArr2.length) {
                int i6 = -i;
                System.arraycopy((Object) iArr2, 0, (Object) iArr2, i6, i3);
                Arrays.fill(iArr2, 0, i6, 0);
            } else {
                int[] iArr3 = new int[i5];
                System.arraycopy((Object) iArr2, 0, (Object) iArr3, -i, i3);
                fDBigInteger2.data = iArr3;
                iArr2 = iArr3;
            }
            fDBigInteger2.offset = fDBigInteger3.offset;
            fDBigInteger2.nWords = i5;
            i = 0;
            i3 = i5;
        }
        long j = 0;
        while (i4 < i2 && i < i3) {
            long j2 = ((((long) iArr2[i]) & 4294967295L) - (((long) iArr[i4]) & 4294967295L)) + j;
            iArr2[i] = (int) j2;
            j = j2 >> 32;
            i4++;
            i++;
        }
        while (j != 0 && i < i3) {
            long j3 = (((long) iArr2[i]) & 4294967295L) + j;
            iArr2[i] = (int) j3;
            j = j3 >> 32;
            i++;
        }
        fDBigInteger2.trimLeadingZeros();
        return fDBigInteger2;
    }

    public FDBigInteger rightInplaceSub(FDBigInteger fDBigInteger) {
        if (fDBigInteger.isImmutable) {
            fDBigInteger = new FDBigInteger((int[]) fDBigInteger.data.clone(), fDBigInteger.offset);
        }
        int i = this.offset - fDBigInteger.offset;
        int[] iArr = fDBigInteger.data;
        int[] iArr2 = this.data;
        int i2 = fDBigInteger.nWords;
        int i3 = this.nWords;
        if (i < 0) {
            if (i3 < iArr.length) {
                int i4 = -i;
                System.arraycopy((Object) iArr, 0, (Object) iArr, i4, i2);
                Arrays.fill(iArr, 0, i4, 0);
            } else {
                int[] iArr3 = new int[i3];
                System.arraycopy((Object) iArr, 0, (Object) iArr3, -i, i2);
                fDBigInteger.data = iArr3;
                iArr = iArr3;
            }
            fDBigInteger.offset = this.offset;
            i = 0;
        } else {
            int i5 = i3 + i;
            if (i5 >= iArr.length) {
                iArr = Arrays.copyOf(iArr, i5);
                fDBigInteger.data = iArr;
            }
        }
        int i6 = 0;
        long j = 0;
        while (i6 < i) {
            long j2 = (0 - (4294967295L & ((long) iArr[i6]))) + j;
            iArr[i6] = (int) j2;
            j = j2 >> 32;
            i6++;
        }
        for (int i7 = 0; i7 < i3; i7++) {
            long j3 = ((((long) iArr2[i7]) & 4294967295L) - (((long) iArr[i6]) & 4294967295L)) + j;
            iArr[i6] = (int) j3;
            j = j3 >> 32;
            i6++;
        }
        fDBigInteger.nWords = i6;
        fDBigInteger.trimLeadingZeros();
        return fDBigInteger;
    }

    private static int checkZeroTail(int[] iArr, int i) {
        while (i > 0) {
            i--;
            if (iArr[i] != 0) {
                return 1;
            }
        }
        return 0;
    }

    public int cmp(FDBigInteger fDBigInteger) {
        int i = this.nWords;
        int i2 = this.offset + i;
        int i3 = fDBigInteger.nWords;
        int i4 = fDBigInteger.offset + i3;
        if (i2 > i4) {
            return 1;
        }
        if (i2 < i4) {
            return -1;
        }
        while (i > 0 && i3 > 0) {
            i--;
            int i5 = this.data[i];
            i3--;
            int i6 = fDBigInteger.data[i3];
            if (i5 != i6) {
                if ((((long) i5) & 4294967295L) < (4294967295L & ((long) i6))) {
                    return -1;
                }
                return 1;
            }
        }
        if (i > 0) {
            return checkZeroTail(this.data, i);
        }
        if (i3 > 0) {
            return -checkZeroTail(fDBigInteger.data, i3);
        }
        return 0;
    }

    public int cmpPow52(int i, int i2) {
        if (i != 0) {
            return cmp(big5pow(i).leftShift(i2));
        }
        int i3 = i2 >> 5;
        int i4 = i2 & 31;
        int i5 = this.nWords;
        int i6 = this.offset + i5;
        int i7 = i3 + 1;
        if (i6 > i7) {
            return 1;
        }
        if (i6 < i7) {
            return -1;
        }
        int[] iArr = this.data;
        int i8 = iArr[i5 - 1];
        int i9 = 1 << i4;
        if (i8 == i9) {
            return checkZeroTail(iArr, i5 - 1);
        }
        if ((((long) i8) & 4294967295L) < (4294967295L & ((long) i9))) {
            return -1;
        }
        return 1;
    }

    public int addAndCmp(FDBigInteger fDBigInteger, FDBigInteger fDBigInteger2) {
        int i;
        int i2;
        FDBigInteger fDBigInteger3;
        FDBigInteger fDBigInteger4;
        int size = fDBigInteger.size();
        int size2 = fDBigInteger2.size();
        if (size >= size2) {
            i2 = size;
            i = size2;
            fDBigInteger4 = fDBigInteger;
            fDBigInteger3 = fDBigInteger2;
        } else {
            i = size;
            i2 = size2;
            fDBigInteger3 = fDBigInteger;
            fDBigInteger4 = fDBigInteger2;
        }
        int size3 = size();
        if (i2 == 0) {
            return size3 == 0 ? 0 : 1;
        }
        if (i == 0) {
            return cmp(fDBigInteger4);
        }
        if (i2 > size3) {
            return -1;
        }
        int i3 = i2 + 1;
        if (i3 < size3) {
            return 1;
        }
        long j = ((long) fDBigInteger4.data[fDBigInteger4.nWords - 1]) & 4294967295L;
        if (i == i2) {
            j += ((long) fDBigInteger3.data[fDBigInteger3.nWords - 1]) & 4294967295L;
        }
        long j2 = j >>> 32;
        if (j2 == 0) {
            long j3 = j + 1;
            if ((j3 >>> 32) == 0) {
                if (i2 < size3) {
                    return 1;
                }
                long j4 = ((long) this.data[this.nWords - 1]) & 4294967295L;
                if (j4 < j) {
                    return -1;
                }
                if (j4 > j3) {
                    return 1;
                }
            }
        } else if (i3 > size3) {
            return -1;
        } else {
            long j5 = ((long) this.data[this.nWords - 1]) & 4294967295L;
            if (j5 < j2) {
                return -1;
            }
            if (j5 > j2 + 1) {
                return 1;
            }
        }
        return cmp(fDBigInteger4.add(fDBigInteger3));
    }

    public void makeImmutable() {
        this.isImmutable = true;
    }

    private FDBigInteger mult(int i) {
        int i2 = this.nWords;
        if (i2 == 0) {
            return this;
        }
        int[] iArr = new int[(i2 + 1)];
        mult(this.data, i2, i, iArr);
        return new FDBigInteger(iArr, this.offset);
    }

    private FDBigInteger mult(FDBigInteger fDBigInteger) {
        if (this.nWords == 0) {
            return this;
        }
        if (size() == 1) {
            return fDBigInteger.mult(this.data[0]);
        }
        if (fDBigInteger.nWords == 0) {
            return fDBigInteger;
        }
        if (fDBigInteger.size() == 1) {
            return mult(fDBigInteger.data[0]);
        }
        int i = this.nWords;
        int i2 = fDBigInteger.nWords;
        int[] iArr = new int[(i + i2)];
        mult(this.data, i, fDBigInteger.data, i2, iArr);
        return new FDBigInteger(iArr, this.offset + fDBigInteger.offset);
    }

    private FDBigInteger add(FDBigInteger fDBigInteger) {
        int i;
        int i2;
        FDBigInteger fDBigInteger2;
        FDBigInteger fDBigInteger3;
        int size = size();
        int size2 = fDBigInteger.size();
        if (size >= size2) {
            i2 = size;
            i = size2;
            fDBigInteger3 = this;
            fDBigInteger2 = fDBigInteger;
        } else {
            i = size;
            i2 = size2;
            fDBigInteger2 = this;
            fDBigInteger3 = fDBigInteger;
        }
        int[] iArr = new int[(i2 + 1)];
        int i3 = 0;
        long j = 0;
        while (i3 < i) {
            int i4 = fDBigInteger3.offset;
            long j2 = i3 < i4 ? 0 : ((long) fDBigInteger3.data[i3 - i4]) & 4294967295L;
            int i5 = fDBigInteger2.offset;
            long j3 = j + j2 + (i3 < i5 ? 0 : ((long) fDBigInteger2.data[i3 - i5]) & 4294967295L);
            iArr[i3] = (int) j3;
            j = j3 >> 32;
            i3++;
        }
        while (i3 < i2) {
            int i6 = fDBigInteger3.offset;
            long j4 = j + (i3 < i6 ? 0 : ((long) fDBigInteger3.data[i3 - i6]) & 4294967295L);
            iArr[i3] = (int) j4;
            j = j4 >> 32;
            i3++;
        }
        iArr[i2] = (int) j;
        return new FDBigInteger(iArr, 0);
    }

    private void multAddMe(int i, int i2) {
        int i3;
        long j = ((long) i) & 4294967295L;
        int[] iArr = this.data;
        long j2 = ((((long) iArr[0]) & 4294967295L) * j) + (((long) i2) & 4294967295L);
        iArr[0] = (int) j2;
        long j3 = j2 >>> 32;
        int i4 = 1;
        while (true) {
            i3 = this.nWords;
            if (i4 >= i3) {
                break;
            }
            int[] iArr2 = this.data;
            long j4 = j3 + ((((long) iArr2[i4]) & 4294967295L) * j);
            iArr2[i4] = (int) j4;
            j3 = j4 >>> 32;
            i4++;
        }
        if (j3 != 0) {
            int[] iArr3 = this.data;
            this.nWords = i3 + 1;
            iArr3[i3] = (int) j3;
        }
    }

    private long multDiffMe(long j, FDBigInteger fDBigInteger) {
        FDBigInteger fDBigInteger2 = fDBigInteger;
        long j2 = 0;
        if (j != 0) {
            int i = fDBigInteger2.offset - this.offset;
            int i2 = 0;
            if (i >= 0) {
                int[] iArr = fDBigInteger2.data;
                int[] iArr2 = this.data;
                while (i2 < fDBigInteger2.nWords) {
                    long j3 = j2 + ((((long) iArr2[i]) & 4294967295L) - ((((long) iArr[i2]) & 4294967295L) * j));
                    iArr2[i] = (int) j3;
                    j2 = j3 >> 32;
                    i2++;
                    i++;
                }
            } else {
                int i3 = -i;
                int[] iArr3 = new int[(this.nWords + i3)];
                int[] iArr4 = fDBigInteger2.data;
                int i4 = 0;
                int i5 = 0;
                while (i4 < i3 && i5 < fDBigInteger2.nWords) {
                    long j4 = j2 - ((((long) iArr4[i5]) & 4294967295L) * j);
                    iArr3[i4] = (int) j4;
                    j2 = j4 >> 32;
                    i5++;
                    i4++;
                }
                int[] iArr5 = this.data;
                while (i5 < fDBigInteger2.nWords) {
                    long j5 = j2 + ((((long) iArr5[i2]) & 4294967295L) - ((((long) iArr4[i5]) & 4294967295L) * j));
                    iArr3[i4] = (int) j5;
                    j2 = j5 >> 32;
                    i5++;
                    i2++;
                    i4++;
                    i3 = i3;
                }
                int i6 = i3;
                this.nWords += i6;
                this.offset -= i6;
                this.data = iArr3;
            }
        }
        return j2;
    }

    private static int multAndCarryBy10(int[] iArr, int i, int[] iArr2) {
        long j = 0;
        for (int i2 = 0; i2 < i; i2++) {
            long j2 = ((((long) iArr[i2]) & 4294967295L) * 10) + j;
            iArr2[i2] = (int) j2;
            j = j2 >>> 32;
        }
        return (int) j;
    }

    private static void mult(int[] iArr, int i, int i2, int[] iArr2) {
        long j = ((long) i2) & 4294967295L;
        long j2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            long j3 = ((((long) iArr[i3]) & 4294967295L) * j) + j2;
            iArr2[i3] = (int) j3;
            j2 = j3 >>> 32;
        }
        iArr2[i] = (int) j2;
    }

    private static void mult(int[] iArr, int i, int i2, int i3, int[] iArr2) {
        int i4 = i;
        long j = ((long) i2) & 4294967295L;
        long j2 = 0;
        int i5 = 0;
        long j3 = 0;
        for (int i6 = 0; i6 < i4; i6++) {
            long j4 = ((((long) iArr[i6]) & 4294967295L) * j) + j3;
            iArr2[i6] = (int) j4;
            j3 = j4 >>> 32;
        }
        iArr2[i4] = (int) j3;
        long j5 = ((long) i3) & 4294967295L;
        while (i5 < i4) {
            int i7 = i5 + 1;
            long j6 = (((long) iArr2[i7]) & 4294967295L) + ((((long) iArr[i5]) & 4294967295L) * j5) + j2;
            iArr2[i7] = (int) j6;
            j2 = j6 >>> 32;
            i5 = i7;
        }
        iArr2[i4 + 1] = (int) j2;
    }

    private static FDBigInteger big5pow(int i) {
        if (i < 340) {
            return POW_5_CACHE[i];
        }
        return big5powRec(i);
    }

    private static FDBigInteger big5powRec(int i) {
        if (i < 340) {
            return POW_5_CACHE[i];
        }
        int i2 = i >> 1;
        int i3 = i - i2;
        FDBigInteger big5powRec = big5powRec(i2);
        int[] iArr = SMALL_5_POW;
        if (i3 < iArr.length) {
            return big5powRec.mult(iArr[i3]);
        }
        return big5powRec.mult(big5powRec(i3));
    }

    public String toHexString() {
        int i = this.nWords;
        if (i == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder((i + this.offset) * 8);
        for (int i2 = this.nWords - 1; i2 >= 0; i2--) {
            String hexString = Integer.toHexString(this.data[i2]);
            for (int length = hexString.length(); length < 8; length++) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        for (int i3 = this.offset; i3 > 0; i3--) {
            sb.append("00000000");
        }
        return sb.toString();
    }

    public BigInteger toBigInteger() {
        int i = (this.nWords * 4) + 1;
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < this.nWords; i2++) {
            int i3 = this.data[i2];
            int i4 = i - (i2 * 4);
            bArr[i4 - 1] = (byte) i3;
            bArr[i4 - 2] = (byte) (i3 >> 8);
            bArr[i4 - 3] = (byte) (i3 >> 16);
            bArr[i4 - 4] = (byte) (i3 >> 24);
        }
        return new BigInteger(bArr).shiftLeft(this.offset * 32);
    }

    public String toString() {
        return toBigInteger().toString();
    }
}

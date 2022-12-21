package java.util;

import android.net.TrafficStats;
import com.qti.extphone.SignalStrength;

final class DualPivotQuicksort {
    private static final int COUNTING_SORT_THRESHOLD_FOR_BYTE = 29;
    private static final int COUNTING_SORT_THRESHOLD_FOR_SHORT_OR_CHAR = 3200;
    private static final int INSERTION_SORT_THRESHOLD = 47;
    private static final int MAX_RUN_COUNT = 67;
    private static final int NUM_BYTE_VALUES = 256;
    private static final int NUM_CHAR_VALUES = 65536;
    private static final int NUM_SHORT_VALUES = 65536;
    private static final int QUICKSORT_THRESHOLD = 286;

    private DualPivotQuicksort() {
    }

    static void sort(int[] iArr, int i, int i2, int[] iArr2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int[] iArr3 = iArr;
        int i8 = i;
        int i9 = i2;
        int[] iArr4 = iArr2;
        int i10 = 1;
        if (i9 - i8 < 286) {
            sort(iArr3, i8, i9, true);
            return;
        }
        int[] iArr5 = new int[68];
        int i11 = 0;
        iArr5[0] = i8;
        int i12 = i8;
        int i13 = 0;
        while (i12 < i9) {
            while (i12 < i9) {
                int i14 = i12 + 1;
                if (iArr3[i12] != iArr3[i14]) {
                    break;
                }
                i12 = i14;
            }
            if (i12 == i9) {
                break;
            }
            int i15 = iArr3[i12];
            int i16 = iArr3[i12 + 1];
            if (i15 < i16) {
                do {
                    i12++;
                    if (i12 > i9) {
                        break;
                    }
                } while (iArr3[i12 - 1] > iArr3[i12]);
            } else if (i15 > i16) {
                do {
                    i12++;
                    if (i12 > i9 || iArr3[i12 - 1] < iArr3[i12]) {
                        int i17 = iArr5[i13] - 1;
                        int i18 = i12;
                    }
                    i12++;
                    break;
                } while (iArr3[i12 - 1] < iArr3[i12]);
                int i172 = iArr5[i13] - 1;
                int i182 = i12;
                while (true) {
                    i172++;
                    i182--;
                    if (i172 >= i182) {
                        break;
                    }
                    int i19 = iArr3[i172];
                    iArr3[i172] = iArr3[i182];
                    iArr3[i182] = i19;
                }
            }
            int i20 = iArr5[i13];
            if (i20 > i8 && iArr3[i20] >= iArr3[i20 - 1]) {
                i13--;
            }
            i13++;
            if (i13 == 67) {
                sort(iArr3, i8, i9, true);
                return;
            }
            iArr5[i13] = i12;
        }
        if (i13 != 0) {
            if (i13 != 1 || iArr5[i13] <= i9) {
                int i21 = i9 + 1;
                if (iArr5[i13] < i21) {
                    i13++;
                    iArr5[i13] = i21;
                }
                byte b = 0;
                int i22 = 1;
                while (true) {
                    i22 <<= 1;
                    if (i22 >= i13) {
                        break;
                    }
                    b = (byte) (b ^ 1);
                }
                int i23 = i21 - i8;
                if (iArr4 == null || i4 < i23 || i3 + i23 > iArr4.length) {
                    iArr4 = new int[i23];
                    i5 = 0;
                } else {
                    i5 = i3;
                }
                if (b == 0) {
                    System.arraycopy((Object) iArr3, i8, (Object) iArr4, i5, i23);
                    i6 = i5 - i8;
                    i7 = 0;
                    int[] iArr6 = iArr4;
                    iArr4 = iArr3;
                    iArr3 = iArr6;
                } else {
                    i7 = i5 - i8;
                    i6 = 0;
                }
                while (i13 > i10) {
                    int i24 = i11;
                    for (int i25 = 2; i25 <= i13; i25 += 2) {
                        int i26 = iArr5[i25];
                        int i27 = iArr5[i25 - 1];
                        int i28 = iArr5[i25 - 2];
                        int i29 = i27;
                        int i30 = i28;
                        while (i28 < i26) {
                            if (i29 >= i26 || (i30 < i27 && iArr3[i30 + i6] <= iArr3[i29 + i6])) {
                                iArr4[i28 + i7] = iArr3[i30 + i6];
                                i30++;
                            } else {
                                iArr4[i28 + i7] = iArr3[i29 + i6];
                                i29++;
                            }
                            i28++;
                        }
                        i24++;
                        iArr5[i24] = i26;
                    }
                    if ((i13 & 1) != 0) {
                        int i31 = iArr5[i13 - 1];
                        int i32 = i21;
                        while (true) {
                            i32--;
                            if (i32 < i31) {
                                break;
                            }
                            iArr4[i32 + i7] = iArr3[i32 + i6];
                        }
                        i24++;
                        iArr5[i24] = i21;
                    }
                    i13 = i24;
                    i11 = 0;
                    i10 = 1;
                    int[] iArr7 = iArr4;
                    iArr4 = iArr3;
                    iArr3 = iArr7;
                    int i33 = i6;
                    i6 = i7;
                    i7 = i33;
                }
            }
        }
    }

    private static void sort(int[] iArr, int i, int i2, boolean z) {
        int i3;
        int i4;
        int i5;
        int i6 = (i2 - i) + 1;
        if (i6 >= 47) {
            int i7 = (i6 >> 3) + (i6 >> 6) + 1;
            int i8 = (i + i2) >>> 1;
            int i9 = i8 - i7;
            int i10 = i9 - i7;
            int i11 = i8 + i7;
            int i12 = i7 + i11;
            int i13 = iArr[i9];
            int i14 = iArr[i10];
            if (i13 < i14) {
                iArr[i9] = i14;
                iArr[i10] = i13;
            }
            int i15 = iArr[i8];
            int i16 = iArr[i9];
            if (i15 < i16) {
                iArr[i8] = i16;
                iArr[i9] = i15;
                int i17 = iArr[i10];
                if (i15 < i17) {
                    iArr[i9] = i17;
                    iArr[i10] = i15;
                }
            }
            int i18 = iArr[i11];
            int i19 = iArr[i8];
            if (i18 < i19) {
                iArr[i11] = i19;
                iArr[i8] = i18;
                int i20 = iArr[i9];
                if (i18 < i20) {
                    iArr[i8] = i20;
                    iArr[i9] = i18;
                    int i21 = iArr[i10];
                    if (i18 < i21) {
                        iArr[i9] = i21;
                        iArr[i10] = i18;
                    }
                }
            }
            int i22 = iArr[i12];
            int i23 = iArr[i11];
            if (i22 < i23) {
                iArr[i12] = i23;
                iArr[i11] = i22;
                int i24 = iArr[i8];
                if (i22 < i24) {
                    iArr[i11] = i24;
                    iArr[i8] = i22;
                    int i25 = iArr[i9];
                    if (i22 < i25) {
                        iArr[i8] = i25;
                        iArr[i9] = i22;
                        int i26 = iArr[i10];
                        if (i22 < i26) {
                            iArr[i9] = i26;
                            iArr[i10] = i22;
                        }
                    }
                }
            }
            int i27 = iArr[i10];
            int i28 = iArr[i9];
            if (i27 == i28 || i28 == (i4 = iArr[i8]) || i4 == (i5 = iArr[i11]) || i5 == iArr[i12]) {
                int i29 = iArr[i8];
                int i30 = i;
                int i31 = i30;
                int i32 = i2;
                while (i30 <= i32) {
                    int i33 = iArr[i30];
                    if (i33 != i29) {
                        if (i33 < i29) {
                            iArr[i30] = iArr[i31];
                            iArr[i31] = i33;
                            i31++;
                        } else {
                            while (true) {
                                i3 = iArr[i32];
                                if (i3 <= i29) {
                                    break;
                                }
                                i32--;
                            }
                            if (i3 < i29) {
                                iArr[i30] = iArr[i31];
                                iArr[i31] = iArr[i32];
                                i31++;
                            } else {
                                iArr[i30] = i29;
                            }
                            iArr[i32] = i33;
                            i32--;
                        }
                    }
                    i30++;
                }
                sort(iArr, i, i31 - 1, z);
                sort(iArr, i32 + 1, i2, false);
                return;
            }
            iArr[i9] = iArr[i];
            iArr[i11] = iArr[i2];
            int i34 = i;
            do {
                i34++;
            } while (iArr[i34] < i28);
            int i35 = i2;
            do {
                i35--;
            } while (iArr[i35] > i5);
            int i36 = i34 - 1;
            loop9:
            while (true) {
                i36++;
                if (i36 > i35) {
                    break;
                }
                int i37 = iArr[i36];
                if (i37 < i28) {
                    iArr[i36] = iArr[i34];
                    iArr[i34] = i37;
                    i34++;
                } else if (i37 > i5) {
                    while (true) {
                        int i38 = iArr[i35];
                        if (i38 > i5) {
                            int i39 = i35 - 1;
                            if (i35 == i36) {
                                i35 = i39;
                                break loop9;
                            }
                            i35 = i39;
                        } else {
                            if (i38 < i28) {
                                iArr[i36] = iArr[i34];
                                iArr[i34] = iArr[i35];
                                i34++;
                            } else {
                                iArr[i36] = i38;
                            }
                            iArr[i35] = i37;
                            i35--;
                        }
                    }
                } else {
                    continue;
                }
            }
            int i40 = i34 - 1;
            iArr[i] = iArr[i40];
            iArr[i40] = i28;
            int i41 = i35 + 1;
            iArr[i2] = iArr[i41];
            iArr[i41] = i5;
            sort(iArr, i, i34 - 2, z);
            sort(iArr, i35 + 2, i2, false);
            if (i34 < i10 && i12 < i35) {
                while (iArr[i34] == i28) {
                    i34++;
                }
                while (iArr[i35] == i5) {
                    i35--;
                }
                int i42 = i34 - 1;
                loop13:
                while (true) {
                    i42++;
                    if (i42 > i35) {
                        break;
                    }
                    int i43 = iArr[i42];
                    if (i43 == i28) {
                        iArr[i42] = iArr[i34];
                        iArr[i34] = i43;
                        i34++;
                    } else if (i43 == i5) {
                        while (true) {
                            int i44 = iArr[i35];
                            if (i44 == i5) {
                                int i45 = i35 - 1;
                                if (i35 == i42) {
                                    i35 = i45;
                                    break loop13;
                                }
                                i35 = i45;
                            } else {
                                if (i44 == i28) {
                                    iArr[i42] = iArr[i34];
                                    iArr[i34] = i28;
                                    i34++;
                                } else {
                                    iArr[i42] = i44;
                                }
                                iArr[i35] = i43;
                                i35--;
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            sort(iArr, i34, i35, false);
        } else if (z) {
            int i46 = i;
            while (i46 < i2) {
                int i47 = i46 + 1;
                int i48 = iArr[i47];
                while (true) {
                    int i49 = iArr[i46];
                    if (i48 >= i49) {
                        break;
                    }
                    iArr[i46 + 1] = i49;
                    int i50 = i46 - 1;
                    if (i46 == i) {
                        i46 = i50;
                        break;
                    }
                    i46 = i50;
                }
                iArr[i46 + 1] = i48;
                i46 = i47;
            }
        } else {
            while (i < i2) {
                i++;
                if (iArr[i] < iArr[i - 1]) {
                    while (true) {
                        int i51 = i + 1;
                        if (i51 > i2) {
                            break;
                        }
                        int i52 = iArr[i];
                        int i53 = iArr[i51];
                        if (i52 < i53) {
                            int i54 = i53;
                            i53 = i52;
                            i52 = i54;
                        }
                        while (true) {
                            i--;
                            int i55 = iArr[i];
                            if (i52 >= i55) {
                                break;
                            }
                            iArr[i + 2] = i55;
                        }
                        int i56 = i + 1;
                        iArr[i56 + 1] = i52;
                        while (true) {
                            i56--;
                            int i57 = iArr[i56];
                            if (i53 >= i57) {
                                break;
                            }
                            iArr[i56 + 1] = i57;
                        }
                        iArr[i56 + 1] = i53;
                        i = i51 + 1;
                    }
                    int i58 = iArr[i2];
                    while (true) {
                        i2--;
                        int i59 = iArr[i2];
                        if (i58 < i59) {
                            iArr[i2 + 1] = i59;
                        } else {
                            iArr[i2 + 1] = i58;
                            return;
                        }
                    }
                }
            }
        }
    }

    static void sort(long[] jArr, int i, int i2, long[] jArr2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        long[] jArr3 = jArr;
        int i8 = i;
        int i9 = i2;
        long[] jArr4 = jArr2;
        if (i9 - i8 < 286) {
            sort(jArr3, i8, i9, true);
            return;
        }
        int[] iArr = new int[68];
        iArr[0] = i8;
        int i10 = i8;
        int i11 = 0;
        while (i10 < i9) {
            while (i10 < i9) {
                int i12 = i10 + 1;
                if (jArr3[i10] != jArr3[i12]) {
                    break;
                }
                i10 = i12;
            }
            if (i10 == i9) {
                break;
            }
            long j = jArr3[i10];
            long j2 = jArr3[i10 + 1];
            if (j < j2) {
                do {
                    i10++;
                    if (i10 > i9) {
                        break;
                    }
                } while (jArr3[i10 - 1] > jArr3[i10]);
            } else if (j > j2) {
                do {
                    i10++;
                    if (i10 > i9 || jArr3[i10 - 1] < jArr3[i10]) {
                        int i13 = iArr[i11] - 1;
                        int i14 = i10;
                    }
                    i10++;
                    break;
                } while (jArr3[i10 - 1] < jArr3[i10]);
                int i132 = iArr[i11] - 1;
                int i142 = i10;
                while (true) {
                    i132++;
                    i142--;
                    if (i132 >= i142) {
                        break;
                    }
                    long j3 = jArr3[i132];
                    jArr3[i132] = jArr3[i142];
                    jArr3[i142] = j3;
                }
            }
            int i15 = iArr[i11];
            if (i15 > i8 && jArr3[i15] >= jArr3[i15 - 1]) {
                i11--;
            }
            i11++;
            if (i11 == 67) {
                sort(jArr3, i8, i9, true);
                return;
            }
            iArr[i11] = i10;
        }
        if (i11 != 0) {
            if (i11 != 1 || iArr[i11] <= i9) {
                int i16 = i9 + 1;
                if (iArr[i11] < i16) {
                    i11++;
                    iArr[i11] = i16;
                }
                byte b = 0;
                int i17 = 1;
                while (true) {
                    i17 <<= 1;
                    if (i17 >= i11) {
                        break;
                    }
                    b = (byte) (b ^ 1);
                }
                int i18 = i16 - i8;
                if (jArr4 == null || i4 < i18 || i3 + i18 > jArr4.length) {
                    jArr4 = new long[i18];
                    i5 = 0;
                } else {
                    i5 = i3;
                }
                if (b == 0) {
                    System.arraycopy((Object) jArr3, i8, (Object) jArr4, i5, i18);
                    i6 = i5 - i8;
                    i7 = 0;
                    long[] jArr5 = jArr4;
                    jArr4 = jArr3;
                    jArr3 = jArr5;
                } else {
                    i7 = i5 - i8;
                    i6 = 0;
                }
                while (i11 > 1) {
                    int i19 = 0;
                    for (int i20 = 2; i20 <= i11; i20 += 2) {
                        int i21 = iArr[i20];
                        int i22 = iArr[i20 - 1];
                        int i23 = iArr[i20 - 2];
                        int i24 = i22;
                        int i25 = i23;
                        while (i23 < i21) {
                            if (i24 >= i21 || (i25 < i22 && jArr3[i25 + i6] <= jArr3[i24 + i6])) {
                                jArr4[i23 + i7] = jArr3[i25 + i6];
                                i25++;
                            } else {
                                jArr4[i23 + i7] = jArr3[i24 + i6];
                                i24++;
                            }
                            i23++;
                        }
                        i19++;
                        iArr[i19] = i21;
                    }
                    if ((i11 & 1) != 0) {
                        int i26 = iArr[i11 - 1];
                        int i27 = i16;
                        while (true) {
                            i27--;
                            if (i27 < i26) {
                                break;
                            }
                            jArr4[i27 + i7] = jArr3[i27 + i6];
                        }
                        i19++;
                        iArr[i19] = i16;
                    }
                    i11 = i19;
                    long[] jArr6 = jArr4;
                    jArr4 = jArr3;
                    jArr3 = jArr6;
                    int i28 = i6;
                    i6 = i7;
                    i7 = i28;
                }
            }
        }
    }

    private static void sort(long[] jArr, int i, int i2, boolean z) {
        long j;
        long[] jArr2 = jArr;
        int i3 = i;
        int i4 = i2;
        boolean z2 = z;
        int i5 = (i4 - i3) + 1;
        if (i5 >= 47) {
            int i6 = (i5 >> 3) + (i5 >> 6) + 1;
            int i7 = (i3 + i4) >>> 1;
            int i8 = i7 - i6;
            int i9 = i8 - i6;
            int i10 = i7 + i6;
            int i11 = i6 + i10;
            long j2 = jArr2[i8];
            long j3 = jArr2[i9];
            if (j2 < j3) {
                jArr2[i8] = j3;
                jArr2[i9] = j2;
            }
            long j4 = jArr2[i7];
            long j5 = jArr2[i8];
            if (j4 < j5) {
                jArr2[i7] = j5;
                jArr2[i8] = j4;
                long j6 = jArr2[i9];
                if (j4 < j6) {
                    jArr2[i8] = j6;
                    jArr2[i9] = j4;
                }
            }
            long j7 = jArr2[i10];
            long j8 = jArr2[i7];
            if (j7 < j8) {
                jArr2[i10] = j8;
                jArr2[i7] = j7;
                long j9 = jArr2[i8];
                if (j7 < j9) {
                    jArr2[i7] = j9;
                    jArr2[i8] = j7;
                    long j10 = jArr2[i9];
                    if (j7 < j10) {
                        jArr2[i8] = j10;
                        jArr2[i9] = j7;
                    }
                }
            }
            long j11 = jArr2[i11];
            long j12 = jArr2[i10];
            if (j11 < j12) {
                jArr2[i11] = j12;
                jArr2[i10] = j11;
                long j13 = jArr2[i7];
                if (j11 < j13) {
                    jArr2[i10] = j13;
                    jArr2[i7] = j11;
                    long j14 = jArr2[i8];
                    if (j11 < j14) {
                        jArr2[i7] = j14;
                        jArr2[i8] = j11;
                        long j15 = jArr2[i9];
                        if (j11 < j15) {
                            jArr2[i8] = j15;
                            jArr2[i9] = j11;
                        }
                    }
                }
            }
            long j16 = jArr2[i9];
            long j17 = jArr2[i8];
            if (j16 != j17) {
                long j18 = jArr2[i7];
                if (j17 != j18) {
                    long j19 = jArr2[i10];
                    if (!(j18 == j19 || j19 == jArr2[i11])) {
                        jArr2[i8] = jArr2[i3];
                        jArr2[i10] = jArr2[i4];
                        int i12 = i3;
                        do {
                            i12++;
                        } while (jArr2[i12] < j17);
                        int i13 = i4;
                        do {
                            i13--;
                        } while (jArr2[i13] > j19);
                        int i14 = i12 - 1;
                        loop9:
                        while (true) {
                            i14++;
                            if (i14 > i13) {
                                break;
                            }
                            long j20 = jArr2[i14];
                            if (j20 < j17) {
                                jArr2[i14] = jArr2[i12];
                                jArr2[i12] = j20;
                                i12++;
                            } else if (j20 > j19) {
                                while (true) {
                                    long j21 = jArr2[i13];
                                    if (j21 > j19) {
                                        int i15 = i13 - 1;
                                        if (i13 == i14) {
                                            i13 = i15;
                                            break loop9;
                                        }
                                        i13 = i15;
                                    } else {
                                        if (j21 < j17) {
                                            jArr2[i14] = jArr2[i12];
                                            jArr2[i12] = jArr2[i13];
                                            i12++;
                                        } else {
                                            jArr2[i14] = j21;
                                        }
                                        jArr2[i13] = j20;
                                        i13--;
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                        int i16 = i12 - 1;
                        jArr2[i3] = jArr2[i16];
                        jArr2[i16] = j17;
                        int i17 = i13 + 1;
                        jArr2[i4] = jArr2[i17];
                        jArr2[i17] = j19;
                        sort(jArr2, i3, i12 - 2, z2);
                        sort(jArr2, i13 + 2, i4, false);
                        if (i12 < i9 && i11 < i13) {
                            while (jArr2[i12] == j17) {
                                i12++;
                            }
                            while (jArr2[i13] == j19) {
                                i13--;
                            }
                            int i18 = i12 - 1;
                            loop13:
                            while (true) {
                                i18++;
                                if (i18 > i13) {
                                    break;
                                }
                                long j22 = jArr2[i18];
                                if (j22 == j17) {
                                    jArr2[i18] = jArr2[i12];
                                    jArr2[i12] = j22;
                                    i12++;
                                } else if (j22 == j19) {
                                    while (true) {
                                        long j23 = jArr2[i13];
                                        if (j23 == j19) {
                                            int i19 = i13 - 1;
                                            if (i13 == i18) {
                                                i13 = i19;
                                                break loop13;
                                            }
                                            i13 = i19;
                                        } else {
                                            if (j23 == j17) {
                                                jArr2[i18] = jArr2[i12];
                                                jArr2[i12] = j17;
                                                i12++;
                                            } else {
                                                jArr2[i18] = j23;
                                            }
                                            jArr2[i13] = j22;
                                            i13--;
                                        }
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                        sort(jArr2, i12, i13, false);
                        return;
                    }
                }
            }
            long j24 = jArr2[i7];
            int i20 = i3;
            int i21 = i20;
            int i22 = i4;
            while (i20 <= i22) {
                long j25 = jArr2[i20];
                if (j25 != j24) {
                    if (j25 < j24) {
                        jArr2[i20] = jArr2[i21];
                        jArr2[i21] = j25;
                        i21++;
                    } else {
                        while (true) {
                            j = jArr2[i22];
                            if (j <= j24) {
                                break;
                            }
                            i22--;
                        }
                        if (j < j24) {
                            jArr2[i20] = jArr2[i21];
                            jArr2[i21] = jArr2[i22];
                            i21++;
                        } else {
                            jArr2[i20] = j24;
                        }
                        jArr2[i22] = j25;
                        i22--;
                    }
                }
                i20++;
            }
            sort(jArr2, i3, i21 - 1, z2);
            sort(jArr2, i22 + 1, i4, false);
        } else if (z2) {
            int i23 = i3;
            while (i23 < i4) {
                int i24 = i23 + 1;
                long j26 = jArr2[i24];
                while (true) {
                    long j27 = jArr2[i23];
                    if (j26 >= j27) {
                        break;
                    }
                    jArr2[i23 + 1] = j27;
                    int i25 = i23 - 1;
                    if (i23 == i3) {
                        i23 = i25;
                        break;
                    }
                    i23 = i25;
                }
                jArr2[i23 + 1] = j26;
                i23 = i24;
            }
        } else {
            while (i3 < i4) {
                i3++;
                if (jArr2[i3] < jArr2[i3 - 1]) {
                    while (true) {
                        int i26 = i3 + 1;
                        if (i26 > i4) {
                            break;
                        }
                        long j28 = jArr2[i3];
                        long j29 = jArr2[i26];
                        if (j28 < j29) {
                            long j30 = j28;
                            j28 = j29;
                            j29 = j30;
                        }
                        while (true) {
                            i3--;
                            long j31 = jArr2[i3];
                            if (j28 >= j31) {
                                break;
                            }
                            jArr2[i3 + 2] = j31;
                        }
                        int i27 = i3 + 1;
                        jArr2[i27 + 1] = j28;
                        while (true) {
                            i27--;
                            long j32 = jArr2[i27];
                            if (j29 >= j32) {
                                break;
                            }
                            jArr2[i27 + 1] = j32;
                        }
                        jArr2[i27 + 1] = j29;
                        i3 = i26 + 1;
                    }
                    long j33 = jArr2[i4];
                    while (true) {
                        i4--;
                        long j34 = jArr2[i4];
                        if (j33 < j34) {
                            jArr2[i4 + 1] = j34;
                        } else {
                            jArr2[i4 + 1] = j33;
                            return;
                        }
                    }
                }
            }
        }
    }

    static void sort(short[] sArr, int i, int i2, short[] sArr2, int i3, int i4) {
        int i5;
        if (i2 - i > 3200) {
            int i6 = 65536;
            int[] iArr = new int[65536];
            int i7 = i - 1;
            while (true) {
                i7++;
                if (i7 > i2) {
                    break;
                }
                int i8 = sArr[i7] - SignalStrength.INVALID;
                iArr[i8] = iArr[i8] + 1;
            }
            int i9 = i2 + 1;
            while (i9 > i) {
                do {
                    i6--;
                    i5 = iArr[i6];
                } while (i5 == 0);
                short s = (short) (i6 + SignalStrength.INVALID);
                do {
                    i9--;
                    sArr[i9] = s;
                    i5--;
                } while (i5 > 0);
            }
            return;
        }
        doSort(sArr, i, i2, sArr2, i3, i4);
    }

    private static void doSort(short[] sArr, int i, int i2, short[] sArr2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        short[] sArr3 = sArr;
        int i8 = i;
        int i9 = i2;
        short[] sArr4 = sArr2;
        int i10 = 1;
        if (i9 - i8 < 286) {
            sort(sArr3, i8, i9, true);
            return;
        }
        int[] iArr = new int[68];
        int i11 = 0;
        iArr[0] = i8;
        int i12 = i8;
        int i13 = 0;
        while (i12 < i9) {
            while (i12 < i9) {
                int i14 = i12 + 1;
                if (sArr3[i12] != sArr3[i14]) {
                    break;
                }
                i12 = i14;
            }
            if (i12 == i9) {
                break;
            }
            short s = sArr3[i12];
            short s2 = sArr3[i12 + 1];
            if (s < s2) {
                do {
                    i12++;
                    if (i12 > i9) {
                        break;
                    }
                } while (sArr3[i12 - 1] > sArr3[i12]);
            } else if (s > s2) {
                do {
                    i12++;
                    if (i12 > i9 || sArr3[i12 - 1] < sArr3[i12]) {
                        int i15 = iArr[i13] - 1;
                        int i16 = i12;
                    }
                    i12++;
                    break;
                } while (sArr3[i12 - 1] < sArr3[i12]);
                int i152 = iArr[i13] - 1;
                int i162 = i12;
                while (true) {
                    i152++;
                    i162--;
                    if (i152 >= i162) {
                        break;
                    }
                    short s3 = sArr3[i152];
                    sArr3[i152] = sArr3[i162];
                    sArr3[i162] = s3;
                }
            }
            int i17 = iArr[i13];
            if (i17 > i8 && sArr3[i17] >= sArr3[i17 - 1]) {
                i13--;
            }
            i13++;
            if (i13 == 67) {
                sort(sArr3, i8, i9, true);
                return;
            }
            iArr[i13] = i12;
        }
        if (i13 != 0) {
            if (i13 != 1 || iArr[i13] <= i9) {
                int i18 = i9 + 1;
                if (iArr[i13] < i18) {
                    i13++;
                    iArr[i13] = i18;
                }
                byte b = 0;
                int i19 = 1;
                while (true) {
                    i19 <<= 1;
                    if (i19 >= i13) {
                        break;
                    }
                    b = (byte) (b ^ 1);
                }
                int i20 = i18 - i8;
                if (sArr4 == null || i4 < i20 || i3 + i20 > sArr4.length) {
                    sArr4 = new short[i20];
                    i5 = 0;
                } else {
                    i5 = i3;
                }
                if (b == 0) {
                    System.arraycopy((Object) sArr3, i8, (Object) sArr4, i5, i20);
                    i6 = i5 - i8;
                    i7 = 0;
                    short[] sArr5 = sArr4;
                    sArr4 = sArr3;
                    sArr3 = sArr5;
                } else {
                    i7 = i5 - i8;
                    i6 = 0;
                }
                while (i13 > i10) {
                    int i21 = i11;
                    for (int i22 = 2; i22 <= i13; i22 += 2) {
                        int i23 = iArr[i22];
                        int i24 = iArr[i22 - 1];
                        int i25 = iArr[i22 - 2];
                        int i26 = i24;
                        int i27 = i25;
                        while (i25 < i23) {
                            if (i26 >= i23 || (i27 < i24 && sArr3[i27 + i6] <= sArr3[i26 + i6])) {
                                sArr4[i25 + i7] = sArr3[i27 + i6];
                                i27++;
                            } else {
                                sArr4[i25 + i7] = sArr3[i26 + i6];
                                i26++;
                            }
                            i25++;
                        }
                        i21++;
                        iArr[i21] = i23;
                    }
                    if ((i13 & 1) != 0) {
                        int i28 = iArr[i13 - 1];
                        int i29 = i18;
                        while (true) {
                            i29--;
                            if (i29 < i28) {
                                break;
                            }
                            sArr4[i29 + i7] = sArr3[i29 + i6];
                        }
                        i21++;
                        iArr[i21] = i18;
                    }
                    i13 = i21;
                    i11 = 0;
                    i10 = 1;
                    short[] sArr6 = sArr4;
                    sArr4 = sArr3;
                    sArr3 = sArr6;
                    int i30 = i6;
                    i6 = i7;
                    i7 = i30;
                }
            }
        }
    }

    private static void sort(short[] sArr, int i, int i2, boolean z) {
        short s;
        short s2;
        short s3;
        int i3 = (i2 - i) + 1;
        if (i3 >= 47) {
            int i4 = (i3 >> 3) + (i3 >> 6) + 1;
            int i5 = (i + i2) >>> 1;
            int i6 = i5 - i4;
            int i7 = i6 - i4;
            int i8 = i5 + i4;
            int i9 = i4 + i8;
            short s4 = sArr[i6];
            short s5 = sArr[i7];
            if (s4 < s5) {
                sArr[i6] = s5;
                sArr[i7] = s4;
            }
            short s6 = sArr[i5];
            short s7 = sArr[i6];
            if (s6 < s7) {
                sArr[i5] = s7;
                sArr[i6] = s6;
                short s8 = sArr[i7];
                if (s6 < s8) {
                    sArr[i6] = s8;
                    sArr[i7] = s6;
                }
            }
            short s9 = sArr[i8];
            short s10 = sArr[i5];
            if (s9 < s10) {
                sArr[i8] = s10;
                sArr[i5] = s9;
                short s11 = sArr[i6];
                if (s9 < s11) {
                    sArr[i5] = s11;
                    sArr[i6] = s9;
                    short s12 = sArr[i7];
                    if (s9 < s12) {
                        sArr[i6] = s12;
                        sArr[i7] = s9;
                    }
                }
            }
            short s13 = sArr[i9];
            short s14 = sArr[i8];
            if (s13 < s14) {
                sArr[i9] = s14;
                sArr[i8] = s13;
                short s15 = sArr[i5];
                if (s13 < s15) {
                    sArr[i8] = s15;
                    sArr[i5] = s13;
                    short s16 = sArr[i6];
                    if (s13 < s16) {
                        sArr[i5] = s16;
                        sArr[i6] = s13;
                        short s17 = sArr[i7];
                        if (s13 < s17) {
                            sArr[i6] = s17;
                            sArr[i7] = s13;
                        }
                    }
                }
            }
            short s18 = sArr[i7];
            short s19 = sArr[i6];
            if (s18 == s19 || s19 == (s2 = sArr[i5]) || s2 == (s3 = sArr[i8]) || s3 == sArr[i9]) {
                short s20 = sArr[i5];
                int i10 = i;
                int i11 = i10;
                int i12 = i2;
                while (i10 <= i12) {
                    short s21 = sArr[i10];
                    if (s21 != s20) {
                        if (s21 < s20) {
                            sArr[i10] = sArr[i11];
                            sArr[i11] = s21;
                            i11++;
                        } else {
                            while (true) {
                                s = sArr[i12];
                                if (s <= s20) {
                                    break;
                                }
                                i12--;
                            }
                            if (s < s20) {
                                sArr[i10] = sArr[i11];
                                sArr[i11] = sArr[i12];
                                i11++;
                            } else {
                                sArr[i10] = s20;
                            }
                            sArr[i12] = s21;
                            i12--;
                        }
                    }
                    i10++;
                }
                sort(sArr, i, i11 - 1, z);
                sort(sArr, i12 + 1, i2, false);
                return;
            }
            sArr[i6] = sArr[i];
            sArr[i8] = sArr[i2];
            int i13 = i;
            do {
                i13++;
            } while (sArr[i13] < s19);
            int i14 = i2;
            do {
                i14--;
            } while (sArr[i14] > s3);
            int i15 = i13 - 1;
            loop9:
            while (true) {
                i15++;
                if (i15 > i14) {
                    break;
                }
                short s22 = sArr[i15];
                if (s22 < s19) {
                    sArr[i15] = sArr[i13];
                    sArr[i13] = s22;
                    i13++;
                } else if (s22 > s3) {
                    while (true) {
                        short s23 = sArr[i14];
                        if (s23 > s3) {
                            int i16 = i14 - 1;
                            if (i14 == i15) {
                                i14 = i16;
                                break loop9;
                            }
                            i14 = i16;
                        } else {
                            if (s23 < s19) {
                                sArr[i15] = sArr[i13];
                                sArr[i13] = sArr[i14];
                                i13++;
                            } else {
                                sArr[i15] = s23;
                            }
                            sArr[i14] = s22;
                            i14--;
                        }
                    }
                } else {
                    continue;
                }
            }
            int i17 = i13 - 1;
            sArr[i] = sArr[i17];
            sArr[i17] = s19;
            int i18 = i14 + 1;
            sArr[i2] = sArr[i18];
            sArr[i18] = s3;
            sort(sArr, i, i13 - 2, z);
            sort(sArr, i14 + 2, i2, false);
            if (i13 < i7 && i9 < i14) {
                while (sArr[i13] == s19) {
                    i13++;
                }
                while (sArr[i14] == s3) {
                    i14--;
                }
                int i19 = i13 - 1;
                loop13:
                while (true) {
                    i19++;
                    if (i19 > i14) {
                        break;
                    }
                    short s24 = sArr[i19];
                    if (s24 == s19) {
                        sArr[i19] = sArr[i13];
                        sArr[i13] = s24;
                        i13++;
                    } else if (s24 == s3) {
                        while (true) {
                            short s25 = sArr[i14];
                            if (s25 == s3) {
                                int i20 = i14 - 1;
                                if (i14 == i19) {
                                    i14 = i20;
                                    break loop13;
                                }
                                i14 = i20;
                            } else {
                                if (s25 == s19) {
                                    sArr[i19] = sArr[i13];
                                    sArr[i13] = s19;
                                    i13++;
                                } else {
                                    sArr[i19] = s25;
                                }
                                sArr[i14] = s24;
                                i14--;
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            sort(sArr, i13, i14, false);
        } else if (z) {
            int i21 = i;
            while (i21 < i2) {
                int i22 = i21 + 1;
                short s26 = sArr[i22];
                while (true) {
                    short s27 = sArr[i21];
                    if (s26 >= s27) {
                        break;
                    }
                    sArr[i21 + 1] = s27;
                    int i23 = i21 - 1;
                    if (i21 == i) {
                        i21 = i23;
                        break;
                    }
                    i21 = i23;
                }
                sArr[i21 + 1] = s26;
                i21 = i22;
            }
        } else {
            while (i < i2) {
                i++;
                if (sArr[i] < sArr[i - 1]) {
                    while (true) {
                        int i24 = i + 1;
                        if (i24 > i2) {
                            break;
                        }
                        short s28 = sArr[i];
                        short s29 = sArr[i24];
                        if (s28 < s29) {
                            short s30 = s29;
                            s29 = s28;
                            s28 = s30;
                        }
                        while (true) {
                            i--;
                            short s31 = sArr[i];
                            if (s28 >= s31) {
                                break;
                            }
                            sArr[i + 2] = s31;
                        }
                        int i25 = i + 1;
                        sArr[i25 + 1] = s28;
                        while (true) {
                            i25--;
                            short s32 = sArr[i25];
                            if (s29 >= s32) {
                                break;
                            }
                            sArr[i25 + 1] = s32;
                        }
                        sArr[i25 + 1] = s29;
                        i = i24 + 1;
                    }
                    short s33 = sArr[i2];
                    while (true) {
                        i2--;
                        short s34 = sArr[i2];
                        if (s33 < s34) {
                            sArr[i2 + 1] = s34;
                        } else {
                            sArr[i2 + 1] = s33;
                            return;
                        }
                    }
                }
            }
        }
    }

    static void sort(char[] cArr, int i, int i2, char[] cArr2, int i3, int i4) {
        int i5;
        if (i2 - i > 3200) {
            int i6 = 65536;
            int[] iArr = new int[65536];
            int i7 = i - 1;
            while (true) {
                i7++;
                if (i7 > i2) {
                    break;
                }
                char c = cArr[i7];
                iArr[c] = iArr[c] + 1;
            }
            int i8 = i2 + 1;
            while (i8 > i) {
                do {
                    i6--;
                    i5 = iArr[i6];
                } while (i5 == 0);
                char c2 = (char) i6;
                do {
                    i8--;
                    cArr[i8] = c2;
                    i5--;
                } while (i5 > 0);
            }
            return;
        }
        doSort(cArr, i, i2, cArr2, i3, i4);
    }

    private static void doSort(char[] cArr, int i, int i2, char[] cArr2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        char[] cArr3 = cArr;
        int i8 = i;
        int i9 = i2;
        char[] cArr4 = cArr2;
        int i10 = 1;
        if (i9 - i8 < 286) {
            sort(cArr3, i8, i9, true);
            return;
        }
        int[] iArr = new int[68];
        int i11 = 0;
        iArr[0] = i8;
        int i12 = i8;
        int i13 = 0;
        while (i12 < i9) {
            while (i12 < i9) {
                int i14 = i12 + 1;
                if (cArr3[i12] != cArr3[i14]) {
                    break;
                }
                i12 = i14;
            }
            if (i12 == i9) {
                break;
            }
            char c = cArr3[i12];
            char c2 = cArr3[i12 + 1];
            if (c < c2) {
                do {
                    i12++;
                    if (i12 > i9) {
                        break;
                    }
                } while (cArr3[i12 - 1] > cArr3[i12]);
            } else if (c > c2) {
                do {
                    i12++;
                    if (i12 > i9 || cArr3[i12 - 1] < cArr3[i12]) {
                        int i15 = iArr[i13] - 1;
                        int i16 = i12;
                    }
                    i12++;
                    break;
                } while (cArr3[i12 - 1] < cArr3[i12]);
                int i152 = iArr[i13] - 1;
                int i162 = i12;
                while (true) {
                    i152++;
                    i162--;
                    if (i152 >= i162) {
                        break;
                    }
                    char c3 = cArr3[i152];
                    cArr3[i152] = cArr3[i162];
                    cArr3[i162] = c3;
                }
            }
            int i17 = iArr[i13];
            if (i17 > i8 && cArr3[i17] >= cArr3[i17 - 1]) {
                i13--;
            }
            i13++;
            if (i13 == 67) {
                sort(cArr3, i8, i9, true);
                return;
            }
            iArr[i13] = i12;
        }
        if (i13 != 0) {
            if (i13 != 1 || iArr[i13] <= i9) {
                int i18 = i9 + 1;
                if (iArr[i13] < i18) {
                    i13++;
                    iArr[i13] = i18;
                }
                byte b = 0;
                int i19 = 1;
                while (true) {
                    i19 <<= 1;
                    if (i19 >= i13) {
                        break;
                    }
                    b = (byte) (b ^ 1);
                }
                int i20 = i18 - i8;
                if (cArr4 == null || i4 < i20 || i3 + i20 > cArr4.length) {
                    cArr4 = new char[i20];
                    i5 = 0;
                } else {
                    i5 = i3;
                }
                if (b == 0) {
                    System.arraycopy((Object) cArr3, i8, (Object) cArr4, i5, i20);
                    i6 = i5 - i8;
                    i7 = 0;
                    char[] cArr5 = cArr4;
                    cArr4 = cArr3;
                    cArr3 = cArr5;
                } else {
                    i7 = i5 - i8;
                    i6 = 0;
                }
                while (i13 > i10) {
                    int i21 = i11;
                    for (int i22 = 2; i22 <= i13; i22 += 2) {
                        int i23 = iArr[i22];
                        int i24 = iArr[i22 - 1];
                        int i25 = iArr[i22 - 2];
                        int i26 = i24;
                        int i27 = i25;
                        while (i25 < i23) {
                            if (i26 >= i23 || (i27 < i24 && cArr3[i27 + i6] <= cArr3[i26 + i6])) {
                                cArr4[i25 + i7] = cArr3[i27 + i6];
                                i27++;
                            } else {
                                cArr4[i25 + i7] = cArr3[i26 + i6];
                                i26++;
                            }
                            i25++;
                        }
                        i21++;
                        iArr[i21] = i23;
                    }
                    if ((i13 & 1) != 0) {
                        int i28 = iArr[i13 - 1];
                        int i29 = i18;
                        while (true) {
                            i29--;
                            if (i29 < i28) {
                                break;
                            }
                            cArr4[i29 + i7] = cArr3[i29 + i6];
                        }
                        i21++;
                        iArr[i21] = i18;
                    }
                    i13 = i21;
                    i11 = 0;
                    i10 = 1;
                    char[] cArr6 = cArr4;
                    cArr4 = cArr3;
                    cArr3 = cArr6;
                    int i30 = i6;
                    i6 = i7;
                    i7 = i30;
                }
            }
        }
    }

    private static void sort(char[] cArr, int i, int i2, boolean z) {
        char c;
        char c2;
        char c3;
        int i3 = (i2 - i) + 1;
        if (i3 >= 47) {
            int i4 = (i3 >> 3) + (i3 >> 6) + 1;
            int i5 = (i + i2) >>> 1;
            int i6 = i5 - i4;
            int i7 = i6 - i4;
            int i8 = i5 + i4;
            int i9 = i4 + i8;
            char c4 = cArr[i6];
            char c5 = cArr[i7];
            if (c4 < c5) {
                cArr[i6] = c5;
                cArr[i7] = c4;
            }
            char c6 = cArr[i5];
            char c7 = cArr[i6];
            if (c6 < c7) {
                cArr[i5] = c7;
                cArr[i6] = c6;
                char c8 = cArr[i7];
                if (c6 < c8) {
                    cArr[i6] = c8;
                    cArr[i7] = c6;
                }
            }
            char c9 = cArr[i8];
            char c10 = cArr[i5];
            if (c9 < c10) {
                cArr[i8] = c10;
                cArr[i5] = c9;
                char c11 = cArr[i6];
                if (c9 < c11) {
                    cArr[i5] = c11;
                    cArr[i6] = c9;
                    char c12 = cArr[i7];
                    if (c9 < c12) {
                        cArr[i6] = c12;
                        cArr[i7] = c9;
                    }
                }
            }
            char c13 = cArr[i9];
            char c14 = cArr[i8];
            if (c13 < c14) {
                cArr[i9] = c14;
                cArr[i8] = c13;
                char c15 = cArr[i5];
                if (c13 < c15) {
                    cArr[i8] = c15;
                    cArr[i5] = c13;
                    char c16 = cArr[i6];
                    if (c13 < c16) {
                        cArr[i5] = c16;
                        cArr[i6] = c13;
                        char c17 = cArr[i7];
                        if (c13 < c17) {
                            cArr[i6] = c17;
                            cArr[i7] = c13;
                        }
                    }
                }
            }
            char c18 = cArr[i7];
            char c19 = cArr[i6];
            if (c18 == c19 || c19 == (c2 = cArr[i5]) || c2 == (c3 = cArr[i8]) || c3 == cArr[i9]) {
                char c20 = cArr[i5];
                int i10 = i;
                int i11 = i10;
                int i12 = i2;
                while (i10 <= i12) {
                    char c21 = cArr[i10];
                    if (c21 != c20) {
                        if (c21 < c20) {
                            cArr[i10] = cArr[i11];
                            cArr[i11] = c21;
                            i11++;
                        } else {
                            while (true) {
                                c = cArr[i12];
                                if (c <= c20) {
                                    break;
                                }
                                i12--;
                            }
                            if (c < c20) {
                                cArr[i10] = cArr[i11];
                                cArr[i11] = cArr[i12];
                                i11++;
                            } else {
                                cArr[i10] = c20;
                            }
                            cArr[i12] = c21;
                            i12--;
                        }
                    }
                    i10++;
                }
                sort(cArr, i, i11 - 1, z);
                sort(cArr, i12 + 1, i2, false);
                return;
            }
            cArr[i6] = cArr[i];
            cArr[i8] = cArr[i2];
            int i13 = i;
            do {
                i13++;
            } while (cArr[i13] < c19);
            int i14 = i2;
            do {
                i14--;
            } while (cArr[i14] > c3);
            int i15 = i13 - 1;
            loop9:
            while (true) {
                i15++;
                if (i15 > i14) {
                    break;
                }
                char c22 = cArr[i15];
                if (c22 < c19) {
                    cArr[i15] = cArr[i13];
                    cArr[i13] = c22;
                    i13++;
                } else if (c22 > c3) {
                    while (true) {
                        char c23 = cArr[i14];
                        if (c23 > c3) {
                            int i16 = i14 - 1;
                            if (i14 == i15) {
                                i14 = i16;
                                break loop9;
                            }
                            i14 = i16;
                        } else {
                            if (c23 < c19) {
                                cArr[i15] = cArr[i13];
                                cArr[i13] = cArr[i14];
                                i13++;
                            } else {
                                cArr[i15] = c23;
                            }
                            cArr[i14] = c22;
                            i14--;
                        }
                    }
                } else {
                    continue;
                }
            }
            int i17 = i13 - 1;
            cArr[i] = cArr[i17];
            cArr[i17] = c19;
            int i18 = i14 + 1;
            cArr[i2] = cArr[i18];
            cArr[i18] = c3;
            sort(cArr, i, i13 - 2, z);
            sort(cArr, i14 + 2, i2, false);
            if (i13 < i7 && i9 < i14) {
                while (cArr[i13] == c19) {
                    i13++;
                }
                while (cArr[i14] == c3) {
                    i14--;
                }
                int i19 = i13 - 1;
                loop13:
                while (true) {
                    i19++;
                    if (i19 > i14) {
                        break;
                    }
                    char c24 = cArr[i19];
                    if (c24 == c19) {
                        cArr[i19] = cArr[i13];
                        cArr[i13] = c24;
                        i13++;
                    } else if (c24 == c3) {
                        while (true) {
                            char c25 = cArr[i14];
                            if (c25 == c3) {
                                int i20 = i14 - 1;
                                if (i14 == i19) {
                                    i14 = i20;
                                    break loop13;
                                }
                                i14 = i20;
                            } else {
                                if (c25 == c19) {
                                    cArr[i19] = cArr[i13];
                                    cArr[i13] = c19;
                                    i13++;
                                } else {
                                    cArr[i19] = c25;
                                }
                                cArr[i14] = c24;
                                i14--;
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            sort(cArr, i13, i14, false);
        } else if (z) {
            int i21 = i;
            while (i21 < i2) {
                int i22 = i21 + 1;
                char c26 = cArr[i22];
                while (true) {
                    char c27 = cArr[i21];
                    if (c26 >= c27) {
                        break;
                    }
                    cArr[i21 + 1] = c27;
                    int i23 = i21 - 1;
                    if (i21 == i) {
                        i21 = i23;
                        break;
                    }
                    i21 = i23;
                }
                cArr[i21 + 1] = c26;
                i21 = i22;
            }
        } else {
            while (i < i2) {
                i++;
                if (cArr[i] < cArr[i - 1]) {
                    while (true) {
                        int i24 = i + 1;
                        if (i24 > i2) {
                            break;
                        }
                        char c28 = cArr[i];
                        char c29 = cArr[i24];
                        if (c28 < c29) {
                            char c30 = c29;
                            c29 = c28;
                            c28 = c30;
                        }
                        while (true) {
                            i--;
                            char c31 = cArr[i];
                            if (c28 >= c31) {
                                break;
                            }
                            cArr[i + 2] = c31;
                        }
                        int i25 = i + 1;
                        cArr[i25 + 1] = c28;
                        while (true) {
                            i25--;
                            char c32 = cArr[i25];
                            if (c29 >= c32) {
                                break;
                            }
                            cArr[i25 + 1] = c32;
                        }
                        cArr[i25 + 1] = c29;
                        i = i24 + 1;
                    }
                    char c33 = cArr[i2];
                    while (true) {
                        i2--;
                        char c34 = cArr[i2];
                        if (c33 < c34) {
                            cArr[i2 + 1] = c34;
                        } else {
                            cArr[i2 + 1] = c33;
                            return;
                        }
                    }
                }
            }
        }
    }

    static void sort(byte[] bArr, int i, int i2) {
        int i3;
        if (i2 - i > 29) {
            int i4 = 256;
            int[] iArr = new int[256];
            int i5 = i - 1;
            while (true) {
                i5++;
                if (i5 > i2) {
                    break;
                }
                int i6 = bArr[i5] + 128;
                iArr[i6] = iArr[i6] + 1;
            }
            int i7 = i2 + 1;
            while (i7 > i) {
                do {
                    i4--;
                    i3 = iArr[i4];
                } while (i3 == 0);
                byte b = (byte) (i4 + TrafficStats.TAG_NETWORK_STACK_IMPERSONATION_RANGE_START);
                do {
                    i7--;
                    bArr[i7] = b;
                    i3--;
                } while (i3 > 0);
            }
            return;
        }
        int i8 = i;
        while (i8 < i2) {
            int i9 = i8 + 1;
            byte b2 = bArr[i9];
            while (true) {
                byte b3 = bArr[i8];
                if (b2 >= b3) {
                    break;
                }
                bArr[i8 + 1] = b3;
                int i10 = i8 - 1;
                if (i8 == i) {
                    i8 = i10;
                    break;
                }
                i8 = i10;
            }
            bArr[i8 + 1] = b2;
            i8 = i9;
        }
    }

    static void sort(float[] fArr, int i, int i2, float[] fArr2, int i3, int i4) {
        while (i <= i2 && Float.isNaN(fArr[i2])) {
            i2--;
        }
        int i5 = i2;
        while (true) {
            i2--;
            if (i2 < i) {
                break;
            }
            float f = fArr[i2];
            if (f != f) {
                fArr[i2] = fArr[i5];
                fArr[i5] = f;
                i5--;
            }
        }
        doSort(fArr, i, i5, fArr2, i3, i4);
        int i6 = i5;
        while (i < i6) {
            int i7 = (i + i6) >>> 1;
            if (fArr[i7] < 0.0f) {
                i = i7 + 1;
            } else {
                i6 = i7;
            }
        }
        while (i <= i5 && Float.floatToRawIntBits(fArr[i]) < 0) {
            i++;
        }
        int i8 = i - 1;
        while (true) {
            i++;
            if (i <= i5) {
                float f2 = fArr[i];
                if (f2 == 0.0f) {
                    if (Float.floatToRawIntBits(f2) < 0) {
                        fArr[i] = 0.0f;
                        i8++;
                        fArr[i8] = -0.0f;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private static void doSort(float[] fArr, int i, int i2, float[] fArr2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        float[] fArr3 = fArr;
        int i8 = i;
        int i9 = i2;
        float[] fArr4 = fArr2;
        if (i9 - i8 < 286) {
            sort(fArr3, i8, i9, true);
            return;
        }
        int[] iArr = new int[68];
        iArr[0] = i8;
        int i10 = i8;
        int i11 = 0;
        while (i10 < i9) {
            while (i10 < i9) {
                int i12 = i10 + 1;
                if (fArr3[i10] != fArr3[i12]) {
                    break;
                }
                i10 = i12;
            }
            if (i10 == i9) {
                break;
            }
            float f = fArr3[i10];
            float f2 = fArr3[i10 + 1];
            if (f < f2) {
                do {
                    i10++;
                    if (i10 > i9) {
                        break;
                    }
                } while (fArr3[i10 - 1] > fArr3[i10]);
            } else if (f > f2) {
                do {
                    i10++;
                    if (i10 > i9 || fArr3[i10 - 1] < fArr3[i10]) {
                        int i13 = iArr[i11] - 1;
                        int i14 = i10;
                    }
                    i10++;
                    break;
                } while (fArr3[i10 - 1] < fArr3[i10]);
                int i132 = iArr[i11] - 1;
                int i142 = i10;
                while (true) {
                    i132++;
                    i142--;
                    if (i132 >= i142) {
                        break;
                    }
                    float f3 = fArr3[i132];
                    fArr3[i132] = fArr3[i142];
                    fArr3[i142] = f3;
                }
            }
            int i15 = iArr[i11];
            if (i15 > i8 && fArr3[i15] >= fArr3[i15 - 1]) {
                i11--;
            }
            i11++;
            if (i11 == 67) {
                sort(fArr3, i8, i9, true);
                return;
            }
            iArr[i11] = i10;
        }
        if (i11 != 0) {
            if (i11 != 1 || iArr[i11] <= i9) {
                int i16 = i9 + 1;
                if (iArr[i11] < i16) {
                    i11++;
                    iArr[i11] = i16;
                }
                byte b = 0;
                int i17 = 1;
                while (true) {
                    i17 <<= 1;
                    if (i17 >= i11) {
                        break;
                    }
                    b = (byte) (b ^ 1);
                }
                int i18 = i16 - i8;
                if (fArr4 == null || i4 < i18 || i3 + i18 > fArr4.length) {
                    fArr4 = new float[i18];
                    i5 = 0;
                } else {
                    i5 = i3;
                }
                if (b == 0) {
                    System.arraycopy((Object) fArr3, i8, (Object) fArr4, i5, i18);
                    i6 = i5 - i8;
                    i7 = 0;
                    float[] fArr5 = fArr4;
                    fArr4 = fArr3;
                    fArr3 = fArr5;
                } else {
                    i7 = i5 - i8;
                    i6 = 0;
                }
                while (i11 > 1) {
                    int i19 = 0;
                    for (int i20 = 2; i20 <= i11; i20 += 2) {
                        int i21 = iArr[i20];
                        int i22 = iArr[i20 - 1];
                        int i23 = iArr[i20 - 2];
                        int i24 = i22;
                        int i25 = i23;
                        while (i23 < i21) {
                            if (i24 >= i21 || (i25 < i22 && fArr3[i25 + i6] <= fArr3[i24 + i6])) {
                                fArr4[i23 + i7] = fArr3[i25 + i6];
                                i25++;
                            } else {
                                fArr4[i23 + i7] = fArr3[i24 + i6];
                                i24++;
                            }
                            i23++;
                        }
                        i19++;
                        iArr[i19] = i21;
                    }
                    if ((i11 & 1) != 0) {
                        int i26 = iArr[i11 - 1];
                        int i27 = i16;
                        while (true) {
                            i27--;
                            if (i27 < i26) {
                                break;
                            }
                            fArr4[i27 + i7] = fArr3[i27 + i6];
                        }
                        i19++;
                        iArr[i19] = i16;
                    }
                    i11 = i19;
                    float[] fArr6 = fArr4;
                    fArr4 = fArr3;
                    fArr3 = fArr6;
                    int i28 = i6;
                    i6 = i7;
                    i7 = i28;
                }
            }
        }
    }

    private static void sort(float[] fArr, int i, int i2, boolean z) {
        float f;
        int i3 = (i2 - i) + 1;
        if (i3 >= 47) {
            int i4 = (i3 >> 3) + (i3 >> 6) + 1;
            int i5 = (i + i2) >>> 1;
            int i6 = i5 - i4;
            int i7 = i6 - i4;
            int i8 = i5 + i4;
            int i9 = i4 + i8;
            float f2 = fArr[i6];
            float f3 = fArr[i7];
            if (f2 < f3) {
                fArr[i6] = f3;
                fArr[i7] = f2;
            }
            float f4 = fArr[i5];
            float f5 = fArr[i6];
            if (f4 < f5) {
                fArr[i5] = f5;
                fArr[i6] = f4;
                float f6 = fArr[i7];
                if (f4 < f6) {
                    fArr[i6] = f6;
                    fArr[i7] = f4;
                }
            }
            float f7 = fArr[i8];
            float f8 = fArr[i5];
            if (f7 < f8) {
                fArr[i8] = f8;
                fArr[i5] = f7;
                float f9 = fArr[i6];
                if (f7 < f9) {
                    fArr[i5] = f9;
                    fArr[i6] = f7;
                    float f10 = fArr[i7];
                    if (f7 < f10) {
                        fArr[i6] = f10;
                        fArr[i7] = f7;
                    }
                }
            }
            float f11 = fArr[i9];
            float f12 = fArr[i8];
            if (f11 < f12) {
                fArr[i9] = f12;
                fArr[i8] = f11;
                float f13 = fArr[i5];
                if (f11 < f13) {
                    fArr[i8] = f13;
                    fArr[i5] = f11;
                    float f14 = fArr[i6];
                    if (f11 < f14) {
                        fArr[i5] = f14;
                        fArr[i6] = f11;
                        float f15 = fArr[i7];
                        if (f11 < f15) {
                            fArr[i6] = f15;
                            fArr[i7] = f11;
                        }
                    }
                }
            }
            float f16 = fArr[i7];
            float f17 = fArr[i6];
            if (f16 != f17) {
                float f18 = fArr[i5];
                if (f17 != f18) {
                    float f19 = fArr[i8];
                    if (!(f18 == f19 || f19 == fArr[i9])) {
                        fArr[i6] = fArr[i];
                        fArr[i8] = fArr[i2];
                        int i10 = i;
                        do {
                            i10++;
                        } while (fArr[i10] < f17);
                        int i11 = i2;
                        do {
                            i11--;
                        } while (fArr[i11] > f19);
                        int i12 = i10 - 1;
                        loop9:
                        while (true) {
                            i12++;
                            if (i12 > i11) {
                                break;
                            }
                            float f20 = fArr[i12];
                            if (f20 < f17) {
                                fArr[i12] = fArr[i10];
                                fArr[i10] = f20;
                                i10++;
                            } else if (f20 > f19) {
                                while (true) {
                                    float f21 = fArr[i11];
                                    if (f21 > f19) {
                                        int i13 = i11 - 1;
                                        if (i11 == i12) {
                                            i11 = i13;
                                            break loop9;
                                        }
                                        i11 = i13;
                                    } else {
                                        if (f21 < f17) {
                                            fArr[i12] = fArr[i10];
                                            fArr[i10] = fArr[i11];
                                            i10++;
                                        } else {
                                            fArr[i12] = f21;
                                        }
                                        fArr[i11] = f20;
                                        i11--;
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                        int i14 = i10 - 1;
                        fArr[i] = fArr[i14];
                        fArr[i14] = f17;
                        int i15 = i11 + 1;
                        fArr[i2] = fArr[i15];
                        fArr[i15] = f19;
                        sort(fArr, i, i10 - 2, z);
                        sort(fArr, i11 + 2, i2, false);
                        if (i10 < i7 && i9 < i11) {
                            while (fArr[i10] == f17) {
                                i10++;
                            }
                            while (fArr[i11] == f19) {
                                i11--;
                            }
                            int i16 = i10 - 1;
                            loop13:
                            while (true) {
                                i16++;
                                if (i16 > i11) {
                                    break;
                                }
                                float f22 = fArr[i16];
                                if (f22 == f17) {
                                    fArr[i16] = fArr[i10];
                                    fArr[i10] = f22;
                                    i10++;
                                } else if (f22 == f19) {
                                    while (true) {
                                        float f23 = fArr[i11];
                                        if (f23 == f19) {
                                            int i17 = i11 - 1;
                                            if (i11 == i16) {
                                                i11 = i17;
                                                break loop13;
                                            }
                                            i11 = i17;
                                        } else {
                                            if (f23 == f17) {
                                                fArr[i16] = fArr[i10];
                                                fArr[i10] = fArr[i11];
                                                i10++;
                                            } else {
                                                fArr[i16] = f23;
                                            }
                                            fArr[i11] = f22;
                                            i11--;
                                        }
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                        sort(fArr, i10, i11, false);
                        return;
                    }
                }
            }
            float f24 = fArr[i5];
            int i18 = i;
            int i19 = i18;
            int i20 = i2;
            while (i18 <= i20) {
                float f25 = fArr[i18];
                if (f25 != f24) {
                    if (f25 < f24) {
                        fArr[i18] = fArr[i19];
                        fArr[i19] = f25;
                        i19++;
                    } else {
                        while (true) {
                            f = fArr[i20];
                            if (f <= f24) {
                                break;
                            }
                            i20--;
                        }
                        if (f < f24) {
                            fArr[i18] = fArr[i19];
                            fArr[i19] = fArr[i20];
                            i19++;
                        } else {
                            fArr[i18] = f;
                        }
                        fArr[i20] = f25;
                        i20--;
                    }
                }
                i18++;
            }
            sort(fArr, i, i19 - 1, z);
            sort(fArr, i20 + 1, i2, false);
        } else if (z) {
            int i21 = i;
            while (i21 < i2) {
                int i22 = i21 + 1;
                float f26 = fArr[i22];
                while (true) {
                    float f27 = fArr[i21];
                    if (f26 >= f27) {
                        break;
                    }
                    fArr[i21 + 1] = f27;
                    int i23 = i21 - 1;
                    if (i21 == i) {
                        i21 = i23;
                        break;
                    }
                    i21 = i23;
                }
                fArr[i21 + 1] = f26;
                i21 = i22;
            }
        } else {
            while (i < i2) {
                i++;
                if (fArr[i] < fArr[i - 1]) {
                    while (true) {
                        int i24 = i + 1;
                        if (i24 > i2) {
                            break;
                        }
                        float f28 = fArr[i];
                        float f29 = fArr[i24];
                        if (f28 < f29) {
                            float f30 = f29;
                            f29 = f28;
                            f28 = f30;
                        }
                        while (true) {
                            i--;
                            float f31 = fArr[i];
                            if (f28 >= f31) {
                                break;
                            }
                            fArr[i + 2] = f31;
                        }
                        int i25 = i + 1;
                        fArr[i25 + 1] = f28;
                        while (true) {
                            i25--;
                            float f32 = fArr[i25];
                            if (f29 >= f32) {
                                break;
                            }
                            fArr[i25 + 1] = f32;
                        }
                        fArr[i25 + 1] = f29;
                        i = i24 + 1;
                    }
                    float f33 = fArr[i2];
                    while (true) {
                        i2--;
                        float f34 = fArr[i2];
                        if (f33 < f34) {
                            fArr[i2 + 1] = f34;
                        } else {
                            fArr[i2 + 1] = f33;
                            return;
                        }
                    }
                }
            }
        }
    }

    static void sort(double[] dArr, int i, int i2, double[] dArr2, int i3, int i4) {
        while (i <= i2 && Double.isNaN(dArr[i2])) {
            i2--;
        }
        int i5 = i2;
        while (true) {
            i2--;
            if (i2 < i) {
                break;
            }
            double d = dArr[i2];
            if (d != d) {
                dArr[i2] = dArr[i5];
                dArr[i5] = d;
                i5--;
            }
        }
        doSort(dArr, i, i5, dArr2, i3, i4);
        int i6 = i5;
        while (i < i6) {
            int i7 = (i + i6) >>> 1;
            if (dArr[i7] < 0.0d) {
                i = i7 + 1;
            } else {
                i6 = i7;
            }
        }
        while (i <= i5 && Double.doubleToRawLongBits(dArr[i]) < 0) {
            i++;
        }
        int i8 = i - 1;
        while (true) {
            i++;
            if (i <= i5) {
                double d2 = dArr[i];
                if (d2 == 0.0d) {
                    if (Double.doubleToRawLongBits(d2) < 0) {
                        dArr[i] = 0.0d;
                        i8++;
                        dArr[i8] = -0.0d;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private static void doSort(double[] dArr, int i, int i2, double[] dArr2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        double[] dArr3 = dArr;
        int i8 = i;
        int i9 = i2;
        double[] dArr4 = dArr2;
        if (i9 - i8 < 286) {
            sort(dArr3, i8, i9, true);
            return;
        }
        int[] iArr = new int[68];
        iArr[0] = i8;
        int i10 = i8;
        int i11 = 0;
        while (i10 < i9) {
            while (i10 < i9) {
                int i12 = i10 + 1;
                if (dArr3[i10] != dArr3[i12]) {
                    break;
                }
                i10 = i12;
            }
            if (i10 == i9) {
                break;
            }
            double d = dArr3[i10];
            double d2 = dArr3[i10 + 1];
            if (d < d2) {
                do {
                    i10++;
                    if (i10 > i9) {
                        break;
                    }
                } while (dArr3[i10 - 1] > dArr3[i10]);
            } else if (d > d2) {
                do {
                    i10++;
                    if (i10 > i9 || dArr3[i10 - 1] < dArr3[i10]) {
                        int i13 = iArr[i11] - 1;
                        int i14 = i10;
                    }
                    i10++;
                    break;
                } while (dArr3[i10 - 1] < dArr3[i10]);
                int i132 = iArr[i11] - 1;
                int i142 = i10;
                while (true) {
                    i132++;
                    i142--;
                    if (i132 >= i142) {
                        break;
                    }
                    double d3 = dArr3[i132];
                    dArr3[i132] = dArr3[i142];
                    dArr3[i142] = d3;
                }
            }
            int i15 = iArr[i11];
            if (i15 > i8 && dArr3[i15] >= dArr3[i15 - 1]) {
                i11--;
            }
            i11++;
            if (i11 == 67) {
                sort(dArr3, i8, i9, true);
                return;
            }
            iArr[i11] = i10;
        }
        if (i11 != 0) {
            if (i11 != 1 || iArr[i11] <= i9) {
                int i16 = i9 + 1;
                if (iArr[i11] < i16) {
                    i11++;
                    iArr[i11] = i16;
                }
                byte b = 0;
                int i17 = 1;
                while (true) {
                    i17 <<= 1;
                    if (i17 >= i11) {
                        break;
                    }
                    b = (byte) (b ^ 1);
                }
                int i18 = i16 - i8;
                if (dArr4 == null || i4 < i18 || i3 + i18 > dArr4.length) {
                    dArr4 = new double[i18];
                    i5 = 0;
                } else {
                    i5 = i3;
                }
                if (b == 0) {
                    System.arraycopy((Object) dArr3, i8, (Object) dArr4, i5, i18);
                    i6 = i5 - i8;
                    i7 = 0;
                    double[] dArr5 = dArr4;
                    dArr4 = dArr3;
                    dArr3 = dArr5;
                } else {
                    i7 = i5 - i8;
                    i6 = 0;
                }
                while (i11 > 1) {
                    int i19 = 0;
                    for (int i20 = 2; i20 <= i11; i20 += 2) {
                        int i21 = iArr[i20];
                        int i22 = iArr[i20 - 1];
                        int i23 = iArr[i20 - 2];
                        int i24 = i22;
                        int i25 = i23;
                        while (i23 < i21) {
                            if (i24 >= i21 || (i25 < i22 && dArr3[i25 + i6] <= dArr3[i24 + i6])) {
                                dArr4[i23 + i7] = dArr3[i25 + i6];
                                i25++;
                            } else {
                                dArr4[i23 + i7] = dArr3[i24 + i6];
                                i24++;
                            }
                            i23++;
                        }
                        i19++;
                        iArr[i19] = i21;
                    }
                    if ((i11 & 1) != 0) {
                        int i26 = iArr[i11 - 1];
                        int i27 = i16;
                        while (true) {
                            i27--;
                            if (i27 < i26) {
                                break;
                            }
                            dArr4[i27 + i7] = dArr3[i27 + i6];
                        }
                        i19++;
                        iArr[i19] = i16;
                    }
                    i11 = i19;
                    double[] dArr6 = dArr4;
                    dArr4 = dArr3;
                    dArr3 = dArr6;
                    int i28 = i6;
                    i6 = i7;
                    i7 = i28;
                }
            }
        }
    }

    private static void sort(double[] dArr, int i, int i2, boolean z) {
        double d;
        double[] dArr2 = dArr;
        int i3 = i;
        int i4 = i2;
        boolean z2 = z;
        int i5 = (i4 - i3) + 1;
        if (i5 >= 47) {
            int i6 = (i5 >> 3) + (i5 >> 6) + 1;
            int i7 = (i3 + i4) >>> 1;
            int i8 = i7 - i6;
            int i9 = i8 - i6;
            int i10 = i7 + i6;
            int i11 = i6 + i10;
            double d2 = dArr2[i8];
            double d3 = dArr2[i9];
            if (d2 < d3) {
                dArr2[i8] = d3;
                dArr2[i9] = d2;
            }
            double d4 = dArr2[i7];
            double d5 = dArr2[i8];
            if (d4 < d5) {
                dArr2[i7] = d5;
                dArr2[i8] = d4;
                double d6 = dArr2[i9];
                if (d4 < d6) {
                    dArr2[i8] = d6;
                    dArr2[i9] = d4;
                }
            }
            double d7 = dArr2[i10];
            double d8 = dArr2[i7];
            if (d7 < d8) {
                dArr2[i10] = d8;
                dArr2[i7] = d7;
                double d9 = dArr2[i8];
                if (d7 < d9) {
                    dArr2[i7] = d9;
                    dArr2[i8] = d7;
                    double d10 = dArr2[i9];
                    if (d7 < d10) {
                        dArr2[i8] = d10;
                        dArr2[i9] = d7;
                    }
                }
            }
            double d11 = dArr2[i11];
            double d12 = dArr2[i10];
            if (d11 < d12) {
                dArr2[i11] = d12;
                dArr2[i10] = d11;
                double d13 = dArr2[i7];
                if (d11 < d13) {
                    dArr2[i10] = d13;
                    dArr2[i7] = d11;
                    double d14 = dArr2[i8];
                    if (d11 < d14) {
                        dArr2[i7] = d14;
                        dArr2[i8] = d11;
                        double d15 = dArr2[i9];
                        if (d11 < d15) {
                            dArr2[i8] = d15;
                            dArr2[i9] = d11;
                        }
                    }
                }
            }
            double d16 = dArr2[i9];
            double d17 = dArr2[i8];
            if (d16 != d17) {
                double d18 = dArr2[i7];
                if (d17 != d18) {
                    double d19 = dArr2[i10];
                    if (!(d18 == d19 || d19 == dArr2[i11])) {
                        dArr2[i8] = dArr2[i3];
                        dArr2[i10] = dArr2[i4];
                        int i12 = i3;
                        do {
                            i12++;
                        } while (dArr2[i12] < d17);
                        int i13 = i4;
                        do {
                            i13--;
                        } while (dArr2[i13] > d19);
                        int i14 = i12 - 1;
                        loop9:
                        while (true) {
                            i14++;
                            if (i14 > i13) {
                                break;
                            }
                            double d20 = dArr2[i14];
                            if (d20 < d17) {
                                dArr2[i14] = dArr2[i12];
                                dArr2[i12] = d20;
                                i12++;
                            } else if (d20 > d19) {
                                while (true) {
                                    double d21 = dArr2[i13];
                                    if (d21 > d19) {
                                        int i15 = i13 - 1;
                                        if (i13 == i14) {
                                            i13 = i15;
                                            break loop9;
                                        }
                                        i13 = i15;
                                    } else {
                                        if (d21 < d17) {
                                            dArr2[i14] = dArr2[i12];
                                            dArr2[i12] = dArr2[i13];
                                            i12++;
                                        } else {
                                            dArr2[i14] = d21;
                                        }
                                        dArr2[i13] = d20;
                                        i13--;
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                        int i16 = i12 - 1;
                        dArr2[i3] = dArr2[i16];
                        dArr2[i16] = d17;
                        int i17 = i13 + 1;
                        dArr2[i4] = dArr2[i17];
                        dArr2[i17] = d19;
                        sort(dArr2, i3, i12 - 2, z2);
                        sort(dArr2, i13 + 2, i4, false);
                        if (i12 < i9 && i11 < i13) {
                            while (dArr2[i12] == d17) {
                                i12++;
                            }
                            while (dArr2[i13] == d19) {
                                i13--;
                            }
                            int i18 = i12 - 1;
                            loop13:
                            while (true) {
                                i18++;
                                if (i18 > i13) {
                                    break;
                                }
                                double d22 = dArr2[i18];
                                if (d22 == d17) {
                                    dArr2[i18] = dArr2[i12];
                                    dArr2[i12] = d22;
                                    i12++;
                                } else if (d22 == d19) {
                                    while (true) {
                                        double d23 = dArr2[i13];
                                        if (d23 == d19) {
                                            int i19 = i13 - 1;
                                            if (i13 == i18) {
                                                i13 = i19;
                                                break loop13;
                                            }
                                            i13 = i19;
                                        } else {
                                            if (d23 == d17) {
                                                dArr2[i18] = dArr2[i12];
                                                dArr2[i12] = dArr2[i13];
                                                i12++;
                                            } else {
                                                dArr2[i18] = d23;
                                            }
                                            dArr2[i13] = d22;
                                            i13--;
                                        }
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                        sort(dArr2, i12, i13, false);
                        return;
                    }
                }
            }
            double d24 = dArr2[i7];
            int i20 = i3;
            int i21 = i20;
            int i22 = i4;
            while (i20 <= i22) {
                double d25 = dArr2[i20];
                if (d25 != d24) {
                    if (d25 < d24) {
                        dArr2[i20] = dArr2[i21];
                        dArr2[i21] = d25;
                        i21++;
                    } else {
                        while (true) {
                            d = dArr2[i22];
                            if (d <= d24) {
                                break;
                            }
                            i22--;
                        }
                        if (d < d24) {
                            dArr2[i20] = dArr2[i21];
                            dArr2[i21] = dArr2[i22];
                            i21++;
                        } else {
                            dArr2[i20] = d;
                        }
                        dArr2[i22] = d25;
                        i22--;
                    }
                }
                i20++;
            }
            sort(dArr2, i3, i21 - 1, z2);
            sort(dArr2, i22 + 1, i4, false);
        } else if (z2) {
            int i23 = i3;
            while (i23 < i4) {
                int i24 = i23 + 1;
                double d26 = dArr2[i24];
                while (true) {
                    double d27 = dArr2[i23];
                    if (d26 >= d27) {
                        break;
                    }
                    dArr2[i23 + 1] = d27;
                    int i25 = i23 - 1;
                    if (i23 == i3) {
                        i23 = i25;
                        break;
                    }
                    i23 = i25;
                }
                dArr2[i23 + 1] = d26;
                i23 = i24;
            }
        } else {
            while (i3 < i4) {
                i3++;
                if (dArr2[i3] < dArr2[i3 - 1]) {
                    while (true) {
                        int i26 = i3 + 1;
                        if (i26 > i4) {
                            break;
                        }
                        double d28 = dArr2[i3];
                        double d29 = dArr2[i26];
                        if (d28 < d29) {
                            double d30 = d28;
                            d28 = d29;
                            d29 = d30;
                        }
                        while (true) {
                            i3--;
                            double d31 = dArr2[i3];
                            if (d28 >= d31) {
                                break;
                            }
                            dArr2[i3 + 2] = d31;
                        }
                        int i27 = i3 + 1;
                        dArr2[i27 + 1] = d28;
                        while (true) {
                            i27--;
                            double d32 = dArr2[i27];
                            if (d29 >= d32) {
                                break;
                            }
                            dArr2[i27 + 1] = d32;
                        }
                        dArr2[i27 + 1] = d29;
                        i3 = i26 + 1;
                    }
                    double d33 = dArr2[i4];
                    while (true) {
                        i4--;
                        double d34 = dArr2[i4];
                        if (d33 < d34) {
                            dArr2[i4 + 1] = d34;
                        } else {
                            dArr2[i4 + 1] = d33;
                            return;
                        }
                    }
                }
            }
        }
    }
}

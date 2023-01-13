package java.util;

import java.lang.reflect.Array;

class TimSort<T> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
    private static final int MIN_GALLOP = 7;
    private static final int MIN_MERGE = 32;

    /* renamed from: a */
    private final T[] f706a;

    /* renamed from: c */
    private final Comparator<? super T> f707c;
    private int minGallop = 7;
    private final int[] runBase;
    private final int[] runLen;
    private int stackSize = 0;
    private T[] tmp;
    private int tmpBase;
    private int tmpLen;

    private static int minRunLength(int i) {
        int i2 = 0;
        while (i >= 32) {
            i2 |= i & 1;
            i >>= 1;
        }
        return i + i2;
    }

    private TimSort(T[] tArr, Comparator<? super T> comparator, T[] tArr2, int i, int i2) {
        this.f706a = tArr;
        this.f707c = comparator;
        int length = tArr.length;
        int i3 = length < 512 ? length >>> 1 : 256;
        if (tArr2 == null || i2 < i3 || i + i3 > tArr2.length) {
            this.tmp = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i3);
            this.tmpBase = 0;
            this.tmpLen = i3;
        } else {
            this.tmp = tArr2;
            this.tmpBase = i;
            this.tmpLen = i2;
        }
        int i4 = length < 120 ? 5 : length < 1542 ? 10 : length < 119151 ? 24 : 49;
        this.runBase = new int[i4];
        this.runLen = new int[i4];
    }

    static <T> void sort(T[] tArr, int i, int i2, Comparator<? super T> comparator, T[] tArr2, int i3, int i4) {
        int i5 = i2 - i;
        if (i5 >= 2) {
            if (i5 < 32) {
                binarySort(tArr, i, i2, countRunAndMakeAscending(tArr, i, i2, comparator) + i, comparator);
                return;
            }
            TimSort timSort = new TimSort(tArr, comparator, tArr2, i3, i4);
            int minRunLength = minRunLength(i5);
            do {
                int countRunAndMakeAscending = countRunAndMakeAscending(tArr, i, i2, comparator);
                if (countRunAndMakeAscending < minRunLength) {
                    int i6 = i5 <= minRunLength ? i5 : minRunLength;
                    binarySort(tArr, i, i + i6, countRunAndMakeAscending + i, comparator);
                    countRunAndMakeAscending = i6;
                }
                timSort.pushRun(i, countRunAndMakeAscending);
                timSort.mergeCollapse();
                i += countRunAndMakeAscending;
                i5 -= countRunAndMakeAscending;
            } while (i5 != 0);
            timSort.mergeForceCollapse();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:0:0x0000, code lost:
        if (r8 == r6) goto L_0x0002;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001e, code lost:
        r2 = r8 - r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0020, code lost:
        if (r2 == 1) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0023, code lost:
        if (r2 == 2) goto L_0x002b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0025, code lost:
        java.lang.System.arraycopy((java.lang.Object) r5, r1, (java.lang.Object) r5, r1 + 1, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002b, code lost:
        r5[r1 + 2] = r5[r1 + 1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0033, code lost:
        r5[r1 + 1] = r5[r1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0039, code lost:
        r5[r1] = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003c, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r8 = r8 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        if (r8 >= r7) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0006, code lost:
        r0 = r5[r8];
        r1 = r6;
        r2 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000b, code lost:
        if (r1 >= r2) goto L_0x001e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000d, code lost:
        r3 = (r1 + r2) >>> 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0017, code lost:
        if (r9.compare(r0, r5[r3]) >= 0) goto L_0x001b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0019, code lost:
        r2 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001b, code lost:
        r1 = r3 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> void binarySort(T[] r5, int r6, int r7, int r8, java.util.Comparator<? super T> r9) {
        /*
            if (r8 != r6) goto L_0x0004
        L_0x0002:
            int r8 = r8 + 1
        L_0x0004:
            if (r8 >= r7) goto L_0x003c
            r0 = r5[r8]
            r1 = r6
            r2 = r8
        L_0x000a:
            r3 = 1
            if (r1 >= r2) goto L_0x001e
            int r4 = r1 + r2
            int r3 = r4 >>> 1
            r4 = r5[r3]
            int r4 = r9.compare(r0, r4)
            if (r4 >= 0) goto L_0x001b
            r2 = r3
            goto L_0x000a
        L_0x001b:
            int r1 = r3 + 1
            goto L_0x000a
        L_0x001e:
            int r2 = r8 - r1
            if (r2 == r3) goto L_0x0033
            r3 = 2
            if (r2 == r3) goto L_0x002b
            int r3 = r1 + 1
            java.lang.System.arraycopy((java.lang.Object) r5, (int) r1, (java.lang.Object) r5, (int) r3, (int) r2)
            goto L_0x0039
        L_0x002b:
            int r2 = r1 + 2
            int r3 = r1 + 1
            r3 = r5[r3]
            r5[r2] = r3
        L_0x0033:
            int r2 = r1 + 1
            r3 = r5[r1]
            r5[r2] = r3
        L_0x0039:
            r5[r1] = r0
            goto L_0x0002
        L_0x003c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TimSort.binarySort(java.lang.Object[], int, int, int, java.util.Comparator):void");
    }

    private static <T> int countRunAndMakeAscending(T[] tArr, int i, int i2, Comparator<? super T> comparator) {
        int i3 = i + 1;
        if (i3 == i2) {
            return 1;
        }
        int i4 = i3 + 1;
        if (comparator.compare(tArr[i3], tArr[i]) < 0) {
            while (i4 < i2 && comparator.compare(tArr[i4], tArr[i4 - 1]) < 0) {
                i4++;
            }
            reverseRange(tArr, i, i4);
        } else {
            while (i4 < i2 && comparator.compare(tArr[i4], tArr[i4 - 1]) >= 0) {
                i4++;
            }
        }
        return i4 - i;
    }

    private static void reverseRange(Object[] objArr, int i, int i2) {
        int i3 = i2 - 1;
        while (i < i3) {
            Object obj = objArr[i];
            objArr[i] = objArr[i3];
            objArr[i3] = obj;
            i3--;
            i++;
        }
    }

    private void pushRun(int i, int i2) {
        int[] iArr = this.runBase;
        int i3 = this.stackSize;
        iArr[i3] = i;
        this.runLen[i3] = i2;
        this.stackSize = i3 + 1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0033, code lost:
        if (r1[r0 - 1] >= r1[r0 + 1]) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0035, code lost:
        r0 = r0 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0016, code lost:
        if (r2[r0 - 1] > (r2[r0] + r2[r0 + 1])) goto L_0x0018;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0027, code lost:
        if (r1[r0 - 2] <= (r1[r0] + r1[r0 - 1])) goto L_0x0029;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0029, code lost:
        r1 = r6.runLen;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void mergeCollapse() {
        /*
            r6 = this;
        L_0x0000:
            int r0 = r6.stackSize
            r1 = 1
            if (r0 <= r1) goto L_0x0049
            int r0 = r0 + -2
            if (r0 <= 0) goto L_0x0018
            int[] r2 = r6.runLen
            int r3 = r0 + -1
            r3 = r2[r3]
            r4 = r2[r0]
            int r5 = r0 + 1
            r2 = r2[r5]
            int r4 = r4 + r2
            if (r3 <= r4) goto L_0x0029
        L_0x0018:
            if (r0 <= r1) goto L_0x0038
            int[] r1 = r6.runLen
            int r2 = r0 + -2
            r2 = r1[r2]
            r3 = r1[r0]
            int r4 = r0 + -1
            r1 = r1[r4]
            int r3 = r3 + r1
            if (r2 > r3) goto L_0x0038
        L_0x0029:
            int[] r1 = r6.runLen
            int r2 = r0 + -1
            r2 = r1[r2]
            int r3 = r0 + 1
            r1 = r1[r3]
            if (r2 >= r1) goto L_0x0045
            int r0 = r0 + -1
            goto L_0x0045
        L_0x0038:
            if (r0 < 0) goto L_0x0049
            int[] r1 = r6.runLen
            r2 = r1[r0]
            int r3 = r0 + 1
            r1 = r1[r3]
            if (r2 <= r1) goto L_0x0045
            goto L_0x0049
        L_0x0045:
            r6.mergeAt(r0)
            goto L_0x0000
        L_0x0049:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TimSort.mergeCollapse():void");
    }

    private void mergeForceCollapse() {
        while (true) {
            int i = this.stackSize;
            if (i > 1) {
                int i2 = i - 2;
                if (i2 > 0) {
                    int[] iArr = this.runLen;
                    if (iArr[i2 - 1] < iArr[i2 + 1]) {
                        i2--;
                    }
                }
                mergeAt(i2);
            } else {
                return;
            }
        }
    }

    private void mergeAt(int i) {
        int i2 = i;
        int[] iArr = this.runBase;
        int i3 = iArr[i2];
        int[] iArr2 = this.runLen;
        int i4 = iArr2[i2];
        int i5 = i2 + 1;
        int i6 = iArr[i5];
        int i7 = iArr2[i5];
        iArr2[i2] = i4 + i7;
        int i8 = this.stackSize;
        if (i2 == i8 - 3) {
            int i9 = i2 + 2;
            iArr[i5] = iArr[i9];
            iArr2[i5] = iArr2[i9];
        }
        this.stackSize = i8 - 1;
        T[] tArr = this.f706a;
        int gallopRight = gallopRight(tArr[i6], tArr, i3, i4, 0, this.f707c);
        int i10 = i3 + gallopRight;
        int i11 = i4 - gallopRight;
        if (i11 != 0) {
            T[] tArr2 = this.f706a;
            int i12 = i6;
            int gallopLeft = gallopLeft(tArr2[(i10 + i11) - 1], tArr2, i6, i7, i7 - 1, this.f707c);
            if (gallopLeft != 0) {
                if (i11 <= gallopLeft) {
                    mergeLo(i10, i11, i12, gallopLeft);
                } else {
                    mergeHi(i10, i11, i12, gallopLeft);
                }
            }
        }
    }

    private static <T> int gallopLeft(T t, T[] tArr, int i, int i2, int i3, Comparator<? super T> comparator) {
        int i4;
        int i5;
        int i6 = i + i3;
        if (comparator.compare(t, tArr[i6]) > 0) {
            int i7 = i2 - i3;
            int i8 = 0;
            int i9 = 1;
            while (i9 < i7 && comparator.compare(t, tArr[i6 + i9]) > 0) {
                int i10 = (i9 << 1) + 1;
                if (i10 <= 0) {
                    i8 = i9;
                    i9 = i7;
                } else {
                    int i11 = i9;
                    i9 = i10;
                    i8 = i11;
                }
            }
            if (i9 <= i7) {
                i7 = i9;
            }
            i5 = i8 + i3;
            i4 = i7 + i3;
        } else {
            int i12 = i3 + 1;
            int i13 = 0;
            int i14 = 1;
            while (i14 < i12 && comparator.compare(t, tArr[i6 - i14]) <= 0) {
                int i15 = (i14 << 1) + 1;
                if (i15 <= 0) {
                    i13 = i14;
                    i14 = i12;
                } else {
                    int i16 = i14;
                    i14 = i15;
                    i13 = i16;
                }
            }
            if (i14 <= i12) {
                i12 = i14;
            }
            int i17 = i3 - i12;
            int i18 = i3 - i13;
            i5 = i17;
            i4 = i18;
        }
        int i19 = i5 + 1;
        while (i19 < i4) {
            int i20 = ((i4 - i19) >>> 1) + i19;
            if (comparator.compare(t, tArr[i + i20]) > 0) {
                i19 = i20 + 1;
            } else {
                i4 = i20;
            }
        }
        return i4;
    }

    private static <T> int gallopRight(T t, T[] tArr, int i, int i2, int i3, Comparator<? super T> comparator) {
        int i4;
        int i5;
        int i6 = i + i3;
        if (comparator.compare(t, tArr[i6]) < 0) {
            int i7 = i3 + 1;
            int i8 = 0;
            int i9 = 1;
            while (i9 < i7 && comparator.compare(t, tArr[i6 - i9]) < 0) {
                int i10 = (i9 << 1) + 1;
                if (i10 <= 0) {
                    i8 = i9;
                    i9 = i7;
                } else {
                    int i11 = i9;
                    i9 = i10;
                    i8 = i11;
                }
            }
            if (i9 <= i7) {
                i7 = i9;
            }
            i5 = i3 - i7;
            i4 = i3 - i8;
        } else {
            int i12 = i2 - i3;
            int i13 = 0;
            int i14 = 1;
            while (i14 < i12 && comparator.compare(t, tArr[i6 + i14]) >= 0) {
                int i15 = (i14 << 1) + 1;
                if (i15 <= 0) {
                    i13 = i14;
                    i14 = i12;
                } else {
                    int i16 = i14;
                    i14 = i15;
                    i13 = i16;
                }
            }
            if (i14 <= i12) {
                i12 = i14;
            }
            int i17 = i13 + i3;
            i4 = i3 + i12;
            i5 = i17;
        }
        int i18 = i5 + 1;
        while (i18 < i4) {
            int i19 = ((i4 - i18) >>> 1) + i18;
            if (comparator.compare(t, tArr[i + i19]) < 0) {
                i4 = i19;
            } else {
                i18 = i19 + 1;
            }
        }
        return i4;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0069, code lost:
        r12 = r1;
        r13 = r2;
        r14 = r3;
        r15 = r4;
        r16 = r5;
        r17 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0071, code lost:
        r6 = gallopRight(r7[r16], r8, r14, r13, 0, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007c, code lost:
        if (r6 == 0) goto L_0x0097;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x007e, code lost:
        java.lang.System.arraycopy((java.lang.Object) r8, r14, (java.lang.Object) r7, r15, r6);
        r1 = r15 + r6;
        r3 = r14 + r6;
        r2 = r13 - r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0087, code lost:
        if (r2 > r9) goto L_0x0094;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0089, code lost:
        r5 = r16;
        r6 = r17;
        r18 = r12;
        r12 = r1;
        r1 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0094, code lost:
        r15 = r1;
        r13 = r2;
        r14 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0097, code lost:
        r5 = r15 + 1;
        r4 = r16 + 1;
        r7[r15] = r7[r16];
        r12 = r12 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00a1, code lost:
        if (r12 != 0) goto L_0x00ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a3, code lost:
        r1 = r12;
        r2 = r13;
        r3 = r14;
        r6 = r17;
        r12 = r5;
        r5 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00ab, code lost:
        r11 = r4;
        r9 = r5;
        r15 = r6;
        r1 = gallopLeft(r8[r14], r7, r4, r12, 0, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00ba, code lost:
        if (r1 == 0) goto L_0x00d4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00bc, code lost:
        java.lang.System.arraycopy((java.lang.Object) r7, r11, (java.lang.Object) r7, r9, r1);
        r2 = r9 + r1;
        r5 = r11 + r1;
        r3 = r12 - r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00c5, code lost:
        if (r3 != 0) goto L_0x00cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00c7, code lost:
        r12 = r2;
        r1 = r3;
        r2 = r13;
        r3 = r14;
        r6 = r17;
        r9 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00cf, code lost:
        r12 = r3;
        r16 = r5;
        r5 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00d4, code lost:
        r5 = r9;
        r16 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00d7, code lost:
        r2 = r5 + 1;
        r3 = r14 + 1;
        r7[r5] = r8[r14];
        r13 = r13 - 1;
        r9 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00e2, code lost:
        if (r13 != 1) goto L_0x0109;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00e4, code lost:
        r1 = r12;
        r5 = r16;
        r6 = r17;
        r12 = r2;
        r2 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0109, code lost:
        r17 = r17 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x010c, code lost:
        if (r15 < 7) goto L_0x0110;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x010e, code lost:
        r5 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0110, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0111, code lost:
        if (r1 < 7) goto L_0x0115;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0113, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0115, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0117, code lost:
        if ((r1 | r5) != false) goto L_0x0128;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0119, code lost:
        if (r17 >= 0) goto L_0x011d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x011b, code lost:
        r11 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x011d, code lost:
        r11 = r17;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0128, code lost:
        r15 = r2;
        r14 = r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void mergeLo(int r20, int r21, int r22, int r23) {
        /*
            r19 = this;
            r0 = r19
            r1 = r20
            r2 = r21
            T[] r7 = r0.f706a
            java.lang.Object[] r8 = r0.ensureCapacity(r2)
            int r3 = r0.tmpBase
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r1, (java.lang.Object) r8, (int) r3, (int) r2)
            int r4 = r1 + 1
            int r5 = r22 + 1
            r6 = r7[r22]
            r7[r1] = r6
            int r1 = r23 + -1
            if (r1 != 0) goto L_0x0021
            java.lang.System.arraycopy((java.lang.Object) r8, (int) r3, (java.lang.Object) r7, (int) r4, (int) r2)
            return
        L_0x0021:
            r9 = 1
            if (r2 != r9) goto L_0x002d
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r5, (java.lang.Object) r7, (int) r4, (int) r1)
            int r4 = r4 + r1
            r0 = r8[r3]
            r7[r4] = r0
            return
        L_0x002d:
            java.util.Comparator<? super T> r10 = r0.f707c
            int r6 = r0.minGallop
        L_0x0031:
            r12 = 0
            r13 = 0
        L_0x0033:
            r14 = r7[r5]
            r15 = r8[r3]
            int r14 = r10.compare(r14, r15)
            if (r14 >= 0) goto L_0x0051
            int r12 = r4 + 1
            int r14 = r5 + 1
            r5 = r7[r5]
            r7[r4] = r5
            int r13 = r13 + r9
            int r1 = r1 + -1
            if (r1 != 0) goto L_0x004d
            r5 = r14
            goto L_0x00eb
        L_0x004d:
            r4 = r12
            r5 = r14
            r12 = 0
            goto L_0x0065
        L_0x0051:
            int r13 = r4 + 1
            int r14 = r3 + 1
            r3 = r8[r3]
            r7[r4] = r3
            int r12 = r12 + r9
            int r2 = r2 + -1
            if (r2 != r9) goto L_0x0062
            r12 = r13
            r3 = r14
            goto L_0x00eb
        L_0x0062:
            r4 = r13
            r3 = r14
            r13 = 0
        L_0x0065:
            r14 = r12 | r13
            if (r14 < r6) goto L_0x0033
            r12 = r1
            r13 = r2
            r14 = r3
            r15 = r4
            r16 = r5
            r17 = r6
        L_0x0071:
            r1 = r7[r16]
            r5 = 0
            r2 = r8
            r3 = r14
            r4 = r13
            r6 = r10
            int r6 = gallopRight(r1, r2, r3, r4, r5, r6)
            if (r6 == 0) goto L_0x0097
            java.lang.System.arraycopy((java.lang.Object) r8, (int) r14, (java.lang.Object) r7, (int) r15, (int) r6)
            int r1 = r15 + r6
            int r3 = r14 + r6
            int r2 = r13 - r6
            if (r2 > r9) goto L_0x0094
            r5 = r16
            r6 = r17
            r18 = r12
            r12 = r1
            r1 = r18
            goto L_0x00eb
        L_0x0094:
            r15 = r1
            r13 = r2
            r14 = r3
        L_0x0097:
            int r5 = r15 + 1
            int r4 = r16 + 1
            r1 = r7[r16]
            r7[r15] = r1
            int r12 = r12 + -1
            if (r12 != 0) goto L_0x00ab
            r1 = r12
            r2 = r13
            r3 = r14
            r6 = r17
            r12 = r5
            r5 = r4
            goto L_0x00eb
        L_0x00ab:
            r1 = r8[r14]
            r15 = 0
            r2 = r7
            r3 = r4
            r11 = r4
            r4 = r12
            r9 = r5
            r5 = r15
            r15 = r6
            r6 = r10
            int r1 = gallopLeft(r1, r2, r3, r4, r5, r6)
            if (r1 == 0) goto L_0x00d4
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r11, (java.lang.Object) r7, (int) r9, (int) r1)
            int r2 = r9 + r1
            int r5 = r11 + r1
            int r3 = r12 - r1
            if (r3 != 0) goto L_0x00cf
            r12 = r2
            r1 = r3
            r2 = r13
            r3 = r14
            r6 = r17
            r9 = 1
            goto L_0x00eb
        L_0x00cf:
            r12 = r3
            r16 = r5
            r5 = r2
            goto L_0x00d7
        L_0x00d4:
            r5 = r9
            r16 = r11
        L_0x00d7:
            int r2 = r5 + 1
            int r3 = r14 + 1
            r4 = r8[r14]
            r7[r5] = r4
            int r13 = r13 + -1
            r9 = 1
            if (r13 != r9) goto L_0x0109
            r1 = r12
            r5 = r16
            r6 = r17
            r12 = r2
            r2 = r13
        L_0x00eb:
            if (r6 >= r9) goto L_0x00ee
            r6 = r9
        L_0x00ee:
            r0.minGallop = r6
            if (r2 != r9) goto L_0x00fb
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r5, (java.lang.Object) r7, (int) r12, (int) r1)
            int r12 = r12 + r1
            r0 = r8[r3]
            r7[r12] = r0
            goto L_0x0100
        L_0x00fb:
            if (r2 == 0) goto L_0x0101
            java.lang.System.arraycopy((java.lang.Object) r8, (int) r3, (java.lang.Object) r7, (int) r12, (int) r2)
        L_0x0100:
            return
        L_0x0101:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Comparison method violates its general contract!"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0109:
            int r17 = r17 + -1
            r4 = 7
            if (r15 < r4) goto L_0x0110
            r5 = r9
            goto L_0x0111
        L_0x0110:
            r5 = 0
        L_0x0111:
            if (r1 < r4) goto L_0x0115
            r1 = r9
            goto L_0x0116
        L_0x0115:
            r1 = 0
        L_0x0116:
            r1 = r1 | r5
            if (r1 != 0) goto L_0x0128
            if (r17 >= 0) goto L_0x011d
            r11 = 0
            goto L_0x011f
        L_0x011d:
            r11 = r17
        L_0x011f:
            int r6 = r11 + 2
            r4 = r2
            r1 = r12
            r2 = r13
            r5 = r16
            goto L_0x0031
        L_0x0128:
            r15 = r2
            r14 = r3
            goto L_0x0071
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TimSort.mergeLo(int, int, int, int):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0077, code lost:
        r12 = r1;
        r13 = r2;
        r14 = r3;
        r15 = r4;
        r16 = r5;
        r17 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x007f, code lost:
        r6 = r12 - gallopRight(r8[r15], r7, r21, r12, r12 - 1, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x008e, code lost:
        if (r6 == 0) goto L_0x00ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0090, code lost:
        r1 = r16 - r6;
        r2 = r17 - r6;
        r3 = r12 - r6;
        java.lang.System.arraycopy((java.lang.Object) r7, r2 + 1, (java.lang.Object) r7, r1 + 1, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x009d, code lost:
        if (r3 != 0) goto L_0x00a7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x009f, code lost:
        r12 = r1;
        r6 = r2;
        r1 = r3;
        r2 = r13;
        r3 = r14;
        r4 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00a7, code lost:
        r16 = r1;
        r17 = r2;
        r12 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00ac, code lost:
        r18 = r16 - 1;
        r19 = r15 - 1;
        r7[r16] = r8[r15];
        r13 = r13 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00b6, code lost:
        if (r13 != 1) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b8, code lost:
        r1 = r12;
        r2 = r13;
        r3 = r14;
        r6 = r17;
        r12 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00c2, code lost:
        r15 = r6;
        r1 = r13 - gallopLeft(r7[r17], r8, r9, r13, r13 - 1, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00d1, code lost:
        if (r1 == 0) goto L_0x00ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00d3, code lost:
        r2 = r18 - r1;
        r4 = r19 - r1;
        r3 = r13 - r1;
        java.lang.System.arraycopy((java.lang.Object) r8, r4 + 1, (java.lang.Object) r7, r2 + 1, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e0, code lost:
        if (r3 > 1) goto L_0x00e9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e2, code lost:
        r1 = r12;
        r6 = r17;
        r12 = r2;
        r2 = r3;
        r3 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e9, code lost:
        r18 = r2;
        r13 = r3;
        r19 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ee, code lost:
        r16 = r18 - 1;
        r2 = r17 - 1;
        r7[r18] = r7[r17];
        r12 = r12 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00f8, code lost:
        if (r12 != 0) goto L_0x0126;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00fa, code lost:
        r6 = r2;
        r1 = r12;
        r2 = r13;
        r3 = r14;
        r12 = r16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0126, code lost:
        r14 = r14 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0129, code lost:
        if (r15 < 7) goto L_0x012d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x012b, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x012d, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x012e, code lost:
        if (r1 < 7) goto L_0x0132;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0130, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0132, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0134, code lost:
        if ((r1 | r4) != false) goto L_0x0144;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0136, code lost:
        if (r14 >= 0) goto L_0x0139;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0138, code lost:
        r14 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0144, code lost:
        r17 = r2;
        r15 = r19;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void mergeHi(int r21, int r22, int r23, int r24) {
        /*
            r20 = this;
            r0 = r20
            r1 = r23
            r2 = r24
            T[] r7 = r0.f706a
            java.lang.Object[] r8 = r0.ensureCapacity(r2)
            int r9 = r0.tmpBase
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r1, (java.lang.Object) r8, (int) r9, (int) r2)
            int r3 = r21 + r22
            r10 = 1
            int r3 = r3 - r10
            int r4 = r9 + r2
            int r4 = r4 - r10
            int r1 = r1 + r2
            int r1 = r1 - r10
            int r5 = r1 + -1
            int r6 = r3 + -1
            r3 = r7[r3]
            r7[r1] = r3
            int r1 = r22 + -1
            if (r1 != 0) goto L_0x002d
            int r0 = r2 + -1
            int r5 = r5 - r0
            java.lang.System.arraycopy((java.lang.Object) r8, (int) r9, (java.lang.Object) r7, (int) r5, (int) r2)
            return
        L_0x002d:
            if (r2 != r10) goto L_0x003c
            int r5 = r5 - r1
            int r6 = r6 - r1
            int r6 = r6 + r10
            int r0 = r5 + 1
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r6, (java.lang.Object) r7, (int) r0, (int) r1)
            r0 = r8[r4]
            r7[r5] = r0
            return
        L_0x003c:
            java.util.Comparator<? super T> r11 = r0.f707c
            int r3 = r0.minGallop
        L_0x0040:
            r13 = 0
            r14 = 0
        L_0x0042:
            r15 = r8[r4]
            r12 = r7[r6]
            int r12 = r11.compare(r15, r12)
            if (r12 >= 0) goto L_0x0060
            int r12 = r5 + -1
            int r14 = r6 + -1
            r6 = r7[r6]
            r7[r5] = r6
            int r13 = r13 + r10
            int r1 = r1 + -1
            if (r1 != 0) goto L_0x005c
            r6 = r14
            goto L_0x0101
        L_0x005c:
            r5 = r12
            r6 = r14
            r14 = 0
            goto L_0x0073
        L_0x0060:
            int r12 = r5 + -1
            int r13 = r4 + -1
            r4 = r8[r4]
            r7[r5] = r4
            int r14 = r14 + r10
            int r2 = r2 + -1
            if (r2 != r10) goto L_0x0070
            r4 = r13
            goto L_0x0101
        L_0x0070:
            r5 = r12
            r4 = r13
            r13 = 0
        L_0x0073:
            r12 = r13 | r14
            if (r12 < r3) goto L_0x0042
            r12 = r1
            r13 = r2
            r14 = r3
            r15 = r4
            r16 = r5
            r17 = r6
        L_0x007f:
            r1 = r8[r15]
            int r5 = r12 + -1
            r2 = r7
            r3 = r21
            r4 = r12
            r6 = r11
            int r1 = gallopRight(r1, r2, r3, r4, r5, r6)
            int r6 = r12 - r1
            if (r6 == 0) goto L_0x00ac
            int r1 = r16 - r6
            int r2 = r17 - r6
            int r3 = r12 - r6
            int r4 = r2 + 1
            int r5 = r1 + 1
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r4, (java.lang.Object) r7, (int) r5, (int) r6)
            if (r3 != 0) goto L_0x00a7
            r12 = r1
            r6 = r2
            r1 = r3
            r2 = r13
            r3 = r14
            r4 = r15
            goto L_0x0101
        L_0x00a7:
            r16 = r1
            r17 = r2
            r12 = r3
        L_0x00ac:
            int r18 = r16 + -1
            int r19 = r15 + -1
            r1 = r8[r15]
            r7[r16] = r1
            int r13 = r13 + -1
            if (r13 != r10) goto L_0x00c2
            r1 = r12
            r2 = r13
            r3 = r14
            r6 = r17
            r12 = r18
        L_0x00bf:
            r4 = r19
            goto L_0x0101
        L_0x00c2:
            r1 = r7[r17]
            int r5 = r13 + -1
            r2 = r8
            r3 = r9
            r4 = r13
            r15 = r6
            r6 = r11
            int r1 = gallopLeft(r1, r2, r3, r4, r5, r6)
            int r1 = r13 - r1
            if (r1 == 0) goto L_0x00ee
            int r2 = r18 - r1
            int r4 = r19 - r1
            int r3 = r13 - r1
            int r5 = r4 + 1
            int r6 = r2 + 1
            java.lang.System.arraycopy((java.lang.Object) r8, (int) r5, (java.lang.Object) r7, (int) r6, (int) r1)
            if (r3 > r10) goto L_0x00e9
            r1 = r12
            r6 = r17
            r12 = r2
            r2 = r3
            r3 = r14
            goto L_0x0101
        L_0x00e9:
            r18 = r2
            r13 = r3
            r19 = r4
        L_0x00ee:
            int r16 = r18 + -1
            int r2 = r17 + -1
            r3 = r7[r17]
            r7[r18] = r3
            int r12 = r12 + -1
            if (r12 != 0) goto L_0x0126
            r6 = r2
            r1 = r12
            r2 = r13
            r3 = r14
            r12 = r16
            goto L_0x00bf
        L_0x0101:
            if (r3 >= r10) goto L_0x0104
            r3 = r10
        L_0x0104:
            r0.minGallop = r3
            if (r2 != r10) goto L_0x0115
            int r12 = r12 - r1
            int r6 = r6 - r1
            int r6 = r6 + r10
            int r0 = r12 + 1
            java.lang.System.arraycopy((java.lang.Object) r7, (int) r6, (java.lang.Object) r7, (int) r0, (int) r1)
            r0 = r8[r4]
            r7[r12] = r0
            goto L_0x011d
        L_0x0115:
            if (r2 == 0) goto L_0x011e
            int r0 = r2 + -1
            int r12 = r12 - r0
            java.lang.System.arraycopy((java.lang.Object) r8, (int) r9, (java.lang.Object) r7, (int) r12, (int) r2)
        L_0x011d:
            return
        L_0x011e:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Comparison method violates its general contract!"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0126:
            int r14 = r14 + -1
            r3 = 7
            if (r15 < r3) goto L_0x012d
            r4 = r10
            goto L_0x012e
        L_0x012d:
            r4 = 0
        L_0x012e:
            if (r1 < r3) goto L_0x0132
            r1 = r10
            goto L_0x0133
        L_0x0132:
            r1 = 0
        L_0x0133:
            r1 = r1 | r4
            if (r1 != 0) goto L_0x0144
            if (r14 >= 0) goto L_0x0139
            r14 = 0
        L_0x0139:
            int r3 = r14 + 2
            r6 = r2
            r1 = r12
            r2 = r13
            r5 = r16
            r4 = r19
            goto L_0x0040
        L_0x0144:
            r17 = r2
            r15 = r19
            goto L_0x007f
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TimSort.mergeHi(int, int, int, int):void");
    }

    private T[] ensureCapacity(int i) {
        if (this.tmpLen < i) {
            int numberOfLeadingZeros = (-1 >>> Integer.numberOfLeadingZeros(i)) + 1;
            if (numberOfLeadingZeros >= 0) {
                i = Math.min(numberOfLeadingZeros, this.f706a.length >>> 1);
            }
            this.tmp = (Object[]) Array.newInstance(this.f706a.getClass().getComponentType(), i);
            this.tmpLen = i;
            this.tmpBase = 0;
        }
        return this.tmp;
    }
}

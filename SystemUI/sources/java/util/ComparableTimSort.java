package java.util;

class ComparableTimSort {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
    private static final int MIN_GALLOP = 7;
    private static final int MIN_MERGE = 32;

    /* renamed from: a */
    private final Object[] f674a;
    private int minGallop = 7;
    private final int[] runBase;
    private final int[] runLen;
    private int stackSize = 0;
    private Object[] tmp;
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

    private ComparableTimSort(Object[] objArr, Object[] objArr2, int i, int i2) {
        this.f674a = objArr;
        int length = objArr.length;
        int i3 = length < 512 ? length >>> 1 : 256;
        if (objArr2 == null || i2 < i3 || i + i3 > objArr2.length) {
            this.tmp = new Object[i3];
            this.tmpBase = 0;
            this.tmpLen = i3;
        } else {
            this.tmp = objArr2;
            this.tmpBase = i;
            this.tmpLen = i2;
        }
        int i4 = length < 120 ? 5 : length < 1542 ? 10 : length < 119151 ? 24 : 49;
        this.runBase = new int[i4];
        this.runLen = new int[i4];
    }

    static void sort(Object[] objArr, int i, int i2, Object[] objArr2, int i3, int i4) {
        int i5 = i2 - i;
        if (i5 >= 2) {
            if (i5 < 32) {
                binarySort(objArr, i, i2, countRunAndMakeAscending(objArr, i, i2) + i);
                return;
            }
            ComparableTimSort comparableTimSort = new ComparableTimSort(objArr, objArr2, i3, i4);
            int minRunLength = minRunLength(i5);
            do {
                int countRunAndMakeAscending = countRunAndMakeAscending(objArr, i, i2);
                if (countRunAndMakeAscending < minRunLength) {
                    int i6 = i5 <= minRunLength ? i5 : minRunLength;
                    binarySort(objArr, i, i + i6, countRunAndMakeAscending + i);
                    countRunAndMakeAscending = i6;
                }
                comparableTimSort.pushRun(i, countRunAndMakeAscending);
                comparableTimSort.mergeCollapse();
                i += countRunAndMakeAscending;
                i5 -= countRunAndMakeAscending;
            } while (i5 != 0);
            comparableTimSort.mergeForceCollapse();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:0:0x0000, code lost:
        if (r8 == r6) goto L_0x0002;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0020, code lost:
        r2 = r8 - r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0022, code lost:
        if (r2 == 1) goto L_0x0035;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0025, code lost:
        if (r2 == 2) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0027, code lost:
        java.lang.System.arraycopy((java.lang.Object) r5, r1, (java.lang.Object) r5, r1 + 1, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
        r5[r1 + 2] = r5[r1 + 1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0035, code lost:
        r5[r1 + 1] = r5[r1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003b, code lost:
        r5[r1] = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003e, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r8 = r8 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        if (r8 >= r7) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0006, code lost:
        r0 = (java.lang.Comparable) r5[r8];
        r1 = r6;
        r2 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000d, code lost:
        if (r1 >= r2) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000f, code lost:
        r3 = (r1 + r2) >>> 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0019, code lost:
        if (r0.compareTo(r5[r3]) >= 0) goto L_0x001d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001b, code lost:
        r2 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001d, code lost:
        r1 = r3 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void binarySort(java.lang.Object[] r5, int r6, int r7, int r8) {
        /*
            if (r8 != r6) goto L_0x0004
        L_0x0002:
            int r8 = r8 + 1
        L_0x0004:
            if (r8 >= r7) goto L_0x003e
            r0 = r5[r8]
            java.lang.Comparable r0 = (java.lang.Comparable) r0
            r1 = r6
            r2 = r8
        L_0x000c:
            r3 = 1
            if (r1 >= r2) goto L_0x0020
            int r4 = r1 + r2
            int r3 = r4 >>> 1
            r4 = r5[r3]
            int r4 = r0.compareTo(r4)
            if (r4 >= 0) goto L_0x001d
            r2 = r3
            goto L_0x000c
        L_0x001d:
            int r1 = r3 + 1
            goto L_0x000c
        L_0x0020:
            int r2 = r8 - r1
            if (r2 == r3) goto L_0x0035
            r3 = 2
            if (r2 == r3) goto L_0x002d
            int r3 = r1 + 1
            java.lang.System.arraycopy((java.lang.Object) r5, (int) r1, (java.lang.Object) r5, (int) r3, (int) r2)
            goto L_0x003b
        L_0x002d:
            int r2 = r1 + 2
            int r3 = r1 + 1
            r3 = r5[r3]
            r5[r2] = r3
        L_0x0035:
            int r2 = r1 + 1
            r3 = r5[r1]
            r5[r2] = r3
        L_0x003b:
            r5[r1] = r0
            goto L_0x0002
        L_0x003e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.ComparableTimSort.binarySort(java.lang.Object[], int, int, int):void");
    }

    private static int countRunAndMakeAscending(Object[] objArr, int i, int i2) {
        int i3 = i + 1;
        if (i3 == i2) {
            return 1;
        }
        int i4 = i3 + 1;
        if (objArr[i3].compareTo(objArr[i]) < 0) {
            while (i4 < i2 && objArr[i4].compareTo(objArr[i4 - 1]) < 0) {
                i4++;
            }
            reverseRange(objArr, i, i4);
        } else {
            while (i4 < i2 && objArr[i4].compareTo(objArr[i4 - 1]) >= 0) {
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
        throw new UnsupportedOperationException("Method not decompiled: java.util.ComparableTimSort.mergeCollapse():void");
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
        int[] iArr = this.runBase;
        int i2 = iArr[i];
        int[] iArr2 = this.runLen;
        int i3 = iArr2[i];
        int i4 = i + 1;
        int i5 = iArr[i4];
        int i6 = iArr2[i4];
        iArr2[i] = i3 + i6;
        int i7 = this.stackSize;
        if (i == i7 - 3) {
            int i8 = i + 2;
            iArr[i4] = iArr[i8];
            iArr2[i4] = iArr2[i8];
        }
        this.stackSize = i7 - 1;
        Object[] objArr = this.f674a;
        int gallopRight = gallopRight((Comparable) objArr[i5], objArr, i2, i3, 0);
        int i9 = i2 + gallopRight;
        int i10 = i3 - gallopRight;
        if (i10 != 0) {
            Object[] objArr2 = this.f674a;
            int gallopLeft = gallopLeft((Comparable) objArr2[(i9 + i10) - 1], objArr2, i5, i6, i6 - 1);
            if (gallopLeft != 0) {
                if (i10 <= gallopLeft) {
                    mergeLo(i9, i10, i5, gallopLeft);
                } else {
                    mergeHi(i9, i10, i5, gallopLeft);
                }
            }
        }
    }

    private static int gallopLeft(Comparable<Object> comparable, Object[] objArr, int i, int i2, int i3) {
        int i4;
        int i5;
        int i6 = i + i3;
        if (comparable.compareTo(objArr[i6]) > 0) {
            int i7 = i2 - i3;
            int i8 = 0;
            int i9 = 1;
            while (i9 < i7 && comparable.compareTo(objArr[i6 + i9]) > 0) {
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
            while (i14 < i12 && comparable.compareTo(objArr[i6 - i14]) <= 0) {
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
            if (comparable.compareTo(objArr[i + i20]) > 0) {
                i19 = i20 + 1;
            } else {
                i4 = i20;
            }
        }
        return i4;
    }

    private static int gallopRight(Comparable<Object> comparable, Object[] objArr, int i, int i2, int i3) {
        int i4;
        int i5;
        int i6 = i + i3;
        if (comparable.compareTo(objArr[i6]) < 0) {
            int i7 = i3 + 1;
            int i8 = 0;
            int i9 = 1;
            while (i9 < i7 && comparable.compareTo(objArr[i6 - i9]) < 0) {
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
            while (i14 < i12 && comparable.compareTo(objArr[i6 + i14]) >= 0) {
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
            if (comparable.compareTo(objArr[i + i19]) < 0) {
                i4 = i19;
            } else {
                i18 = i19 + 1;
            }
        }
        return i4;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0045, code lost:
        r4 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0063, code lost:
        r6 = gallopRight((java.lang.Comparable) r0[r4], r1, r2, r13, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006b, code lost:
        if (r6 == 0) goto L_0x0077;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006d, code lost:
        java.lang.System.arraycopy((java.lang.Object) r1, r2, (java.lang.Object) r0, r3, r6);
        r3 = r3 + r6;
        r2 = r2 + r6;
        r13 = r13 - r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0073, code lost:
        if (r13 > 1) goto L_0x0077;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0075, code lost:
        r6 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0077, code lost:
        r7 = r3 + 1;
        r8 = r4 + 1;
        r0[r3] = r0[r4];
        r15 = r15 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0081, code lost:
        if (r15 != 0) goto L_0x0085;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0083, code lost:
        r6 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0085, code lost:
        r3 = gallopLeft((java.lang.Comparable) r1[r2], r0, r8, r15, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x008d, code lost:
        if (r3 == 0) goto L_0x00a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x008f, code lost:
        java.lang.System.arraycopy((java.lang.Object) r0, r8, (java.lang.Object) r0, r7, r3);
        r4 = r7 + r3;
        r7 = r8 + r3;
        r15 = r15 - r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0097, code lost:
        if (r15 != 0) goto L_0x009c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0099, code lost:
        r6 = r4;
        r4 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x009c, code lost:
        r10 = r7;
        r7 = r4;
        r4 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a0, code lost:
        r4 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00a1, code lost:
        r8 = r7 + 1;
        r9 = r2 + 1;
        r0[r7] = r1[r2];
        r13 = r13 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ab, code lost:
        if (r13 != 1) goto L_0x00cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00ad, code lost:
        r6 = r8;
        r2 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00cd, code lost:
        r14 = r14 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00d0, code lost:
        if (r6 < 7) goto L_0x00d4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00d2, code lost:
        r6 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00d4, code lost:
        r6 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00d5, code lost:
        if (r3 < 7) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00d7, code lost:
        r2 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00d9, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00db, code lost:
        if ((r2 | r6) != false) goto L_0x00e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00dd, code lost:
        if (r14 >= 0) goto L_0x00e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00e0, code lost:
        r5 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00e7, code lost:
        r3 = r8;
        r2 = r9;
     */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0063 A[EDGE_INSN: B:70:0x0063->B:22:0x0063 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void mergeLo(int r12, int r13, int r14, int r15) {
        /*
            r11 = this;
            java.lang.Object[] r0 = r11.f674a
            java.lang.Object[] r1 = r11.ensureCapacity(r13)
            int r2 = r11.tmpBase
            java.lang.System.arraycopy((java.lang.Object) r0, (int) r12, (java.lang.Object) r1, (int) r2, (int) r13)
            int r3 = r12 + 1
            int r4 = r14 + 1
            r14 = r0[r14]
            r0[r12] = r14
            int r15 = r15 + -1
            if (r15 != 0) goto L_0x001b
            java.lang.System.arraycopy((java.lang.Object) r1, (int) r2, (java.lang.Object) r0, (int) r3, (int) r13)
            return
        L_0x001b:
            r12 = 1
            if (r13 != r12) goto L_0x0027
            java.lang.System.arraycopy((java.lang.Object) r0, (int) r4, (java.lang.Object) r0, (int) r3, (int) r15)
            int r3 = r3 + r15
            r11 = r1[r2]
            r0[r3] = r11
            return
        L_0x0027:
            int r14 = r11.minGallop
        L_0x0029:
            r5 = 0
            r6 = r5
            r7 = r6
        L_0x002c:
            r8 = r0[r4]
            java.lang.Comparable r8 = (java.lang.Comparable) r8
            r9 = r1[r2]
            int r8 = r8.compareTo(r9)
            if (r8 >= 0) goto L_0x004c
            int r6 = r3 + 1
            int r8 = r4 + 1
            r4 = r0[r4]
            r0[r3] = r4
            int r7 = r7 + r12
            int r15 = r15 + -1
            if (r15 != 0) goto L_0x0048
        L_0x0045:
            r4 = r8
            goto L_0x00af
        L_0x0048:
            r3 = r6
            r4 = r8
            r6 = r5
            goto L_0x005f
        L_0x004c:
            int r7 = r3 + 1
            int r8 = r2 + 1
            r2 = r1[r2]
            r0[r3] = r2
            int r6 = r6 + r12
            int r13 = r13 + -1
            if (r13 != r12) goto L_0x005c
            r6 = r7
            r2 = r8
            goto L_0x00af
        L_0x005c:
            r3 = r7
            r2 = r8
            r7 = r5
        L_0x005f:
            r8 = r6 | r7
            if (r8 < r14) goto L_0x002c
        L_0x0063:
            r6 = r0[r4]
            java.lang.Comparable r6 = (java.lang.Comparable) r6
            int r6 = gallopRight(r6, r1, r2, r13, r5)
            if (r6 == 0) goto L_0x0077
            java.lang.System.arraycopy((java.lang.Object) r1, (int) r2, (java.lang.Object) r0, (int) r3, (int) r6)
            int r3 = r3 + r6
            int r2 = r2 + r6
            int r13 = r13 - r6
            if (r13 > r12) goto L_0x0077
            r6 = r3
            goto L_0x00af
        L_0x0077:
            int r7 = r3 + 1
            int r8 = r4 + 1
            r4 = r0[r4]
            r0[r3] = r4
            int r15 = r15 + -1
            if (r15 != 0) goto L_0x0085
            r6 = r7
            goto L_0x0045
        L_0x0085:
            r3 = r1[r2]
            java.lang.Comparable r3 = (java.lang.Comparable) r3
            int r3 = gallopLeft(r3, r0, r8, r15, r5)
            if (r3 == 0) goto L_0x00a0
            java.lang.System.arraycopy((java.lang.Object) r0, (int) r8, (java.lang.Object) r0, (int) r7, (int) r3)
            int r4 = r7 + r3
            int r7 = r8 + r3
            int r15 = r15 - r3
            if (r15 != 0) goto L_0x009c
            r6 = r4
            r4 = r7
            goto L_0x00af
        L_0x009c:
            r10 = r7
            r7 = r4
            r4 = r10
            goto L_0x00a1
        L_0x00a0:
            r4 = r8
        L_0x00a1:
            int r8 = r7 + 1
            int r9 = r2 + 1
            r2 = r1[r2]
            r0[r7] = r2
            int r13 = r13 + -1
            if (r13 != r12) goto L_0x00cd
            r6 = r8
            r2 = r9
        L_0x00af:
            if (r14 >= r12) goto L_0x00b2
            r14 = r12
        L_0x00b2:
            r11.minGallop = r14
            if (r13 != r12) goto L_0x00bf
            java.lang.System.arraycopy((java.lang.Object) r0, (int) r4, (java.lang.Object) r0, (int) r6, (int) r15)
            int r6 = r6 + r15
            r11 = r1[r2]
            r0[r6] = r11
            goto L_0x00c4
        L_0x00bf:
            if (r13 == 0) goto L_0x00c5
            java.lang.System.arraycopy((java.lang.Object) r1, (int) r2, (java.lang.Object) r0, (int) r6, (int) r13)
        L_0x00c4:
            return
        L_0x00c5:
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException
            java.lang.String r12 = "Comparison method violates its general contract!"
            r11.<init>((java.lang.String) r12)
            throw r11
        L_0x00cd:
            int r14 = r14 + -1
            r2 = 7
            if (r6 < r2) goto L_0x00d4
            r6 = r12
            goto L_0x00d5
        L_0x00d4:
            r6 = r5
        L_0x00d5:
            if (r3 < r2) goto L_0x00d9
            r2 = r12
            goto L_0x00da
        L_0x00d9:
            r2 = r5
        L_0x00da:
            r2 = r2 | r6
            if (r2 != 0) goto L_0x00e7
            if (r14 >= 0) goto L_0x00e0
            goto L_0x00e1
        L_0x00e0:
            r5 = r14
        L_0x00e1:
            int r14 = r5 + 2
            r3 = r8
            r2 = r9
            goto L_0x0029
        L_0x00e7:
            r3 = r8
            r2 = r9
            goto L_0x0063
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.ComparableTimSort.mergeLo(int, int, int, int):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0079, code lost:
        r12 = r2 - gallopRight((java.lang.Comparable) r5[r9], r4, r1, r2, r2 - 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0085, code lost:
        if (r12 == 0) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0087, code lost:
        r10 = r10 - r12;
        r11 = r11 - r12;
        r2 = r2 - r12;
        java.lang.System.arraycopy((java.lang.Object) r4, r11 + 1, (java.lang.Object) r4, r10 + 1, r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0091, code lost:
        if (r2 != 0) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0093, code lost:
        r12 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0095, code lost:
        r13 = r10 - 1;
        r14 = r9 - 1;
        r4[r10] = r5[r9];
        r3 = r3 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x009f, code lost:
        if (r3 != 1) goto L_0x00a4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00a1, code lost:
        r12 = r13;
        r9 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00a4, code lost:
        r9 = r3 - gallopLeft((java.lang.Comparable) r4[r11], r5, r6, r3, r3 - 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b0, code lost:
        if (r9 == 0) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00b2, code lost:
        r10 = r13 - r9;
        r13 = r14 - r9;
        r3 = r3 - r9;
        java.lang.System.arraycopy((java.lang.Object) r5, r13 + 1, (java.lang.Object) r4, r10 + 1, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00be, code lost:
        if (r3 > 1) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00c0, code lost:
        r12 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00c2, code lost:
        r10 = r13;
        r13 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00c4, code lost:
        r14 = r10 - 1;
        r15 = r11 - 1;
        r4[r10] = r4[r11];
        r2 = r2 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00ce, code lost:
        if (r2 != 0) goto L_0x00f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00d0, code lost:
        r9 = r13;
        r12 = r14;
        r11 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00f8, code lost:
        r7 = r7 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00fb, code lost:
        if (r12 < 7) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00fd, code lost:
        r11 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00ff, code lost:
        r11 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0100, code lost:
        if (r9 < 7) goto L_0x0104;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0102, code lost:
        r9 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0104, code lost:
        r9 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0106, code lost:
        if ((r9 | r11) != false) goto L_0x0114;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0108, code lost:
        if (r7 >= 0) goto L_0x010c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x010a, code lost:
        r12 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x010c, code lost:
        r12 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0114, code lost:
        r9 = r13;
        r10 = r14;
        r11 = r15;
     */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0079 A[EDGE_INSN: B:69:0x0079->B:21:0x0079 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void mergeHi(int r17, int r18, int r19, int r20) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r19
            r3 = r20
            java.lang.Object[] r4 = r0.f674a
            java.lang.Object[] r5 = r0.ensureCapacity(r3)
            int r6 = r0.tmpBase
            java.lang.System.arraycopy((java.lang.Object) r4, (int) r2, (java.lang.Object) r5, (int) r6, (int) r3)
            int r7 = r1 + r18
            r8 = 1
            int r7 = r7 - r8
            int r9 = r6 + r3
            int r9 = r9 - r8
            int r2 = r2 + r3
            int r2 = r2 - r8
            int r10 = r2 + -1
            int r11 = r7 + -1
            r7 = r4[r7]
            r4[r2] = r7
            int r2 = r18 + -1
            if (r2 != 0) goto L_0x002f
            int r0 = r3 + -1
            int r10 = r10 - r0
            java.lang.System.arraycopy((java.lang.Object) r5, (int) r6, (java.lang.Object) r4, (int) r10, (int) r3)
            return
        L_0x002f:
            if (r3 != r8) goto L_0x003e
            int r10 = r10 - r2
            int r11 = r11 - r2
            int r11 = r11 + r8
            int r0 = r10 + 1
            java.lang.System.arraycopy((java.lang.Object) r4, (int) r11, (java.lang.Object) r4, (int) r0, (int) r2)
            r0 = r5[r9]
            r4[r10] = r0
            return
        L_0x003e:
            int r7 = r0.minGallop
        L_0x0040:
            r13 = 0
            r14 = 0
        L_0x0042:
            r15 = r5[r9]
            java.lang.Comparable r15 = (java.lang.Comparable) r15
            r12 = r4[r11]
            int r12 = r15.compareTo(r12)
            if (r12 >= 0) goto L_0x0062
            int r12 = r10 + -1
            int r14 = r11 + -1
            r11 = r4[r11]
            r4[r10] = r11
            int r13 = r13 + r8
            int r2 = r2 + -1
            if (r2 != 0) goto L_0x005e
            r11 = r14
            goto L_0x00d3
        L_0x005e:
            r10 = r12
            r11 = r14
            r14 = 0
            goto L_0x0075
        L_0x0062:
            int r12 = r10 + -1
            int r13 = r9 + -1
            r9 = r5[r9]
            r4[r10] = r9
            int r14 = r14 + r8
            int r3 = r3 + -1
            if (r3 != r8) goto L_0x0072
        L_0x006f:
            r9 = r13
            goto L_0x00d3
        L_0x0072:
            r10 = r12
            r9 = r13
            r13 = 0
        L_0x0075:
            r12 = r13 | r14
            if (r12 < r7) goto L_0x0042
        L_0x0079:
            r12 = r5[r9]
            java.lang.Comparable r12 = (java.lang.Comparable) r12
            int r13 = r2 + -1
            int r12 = gallopRight(r12, r4, r1, r2, r13)
            int r12 = r2 - r12
            if (r12 == 0) goto L_0x0095
            int r10 = r10 - r12
            int r11 = r11 - r12
            int r2 = r2 - r12
            int r13 = r11 + 1
            int r14 = r10 + 1
            java.lang.System.arraycopy((java.lang.Object) r4, (int) r13, (java.lang.Object) r4, (int) r14, (int) r12)
            if (r2 != 0) goto L_0x0095
            r12 = r10
            goto L_0x00d3
        L_0x0095:
            int r13 = r10 + -1
            int r14 = r9 + -1
            r9 = r5[r9]
            r4[r10] = r9
            int r3 = r3 + -1
            if (r3 != r8) goto L_0x00a4
            r12 = r13
            r9 = r14
            goto L_0x00d3
        L_0x00a4:
            r9 = r4[r11]
            java.lang.Comparable r9 = (java.lang.Comparable) r9
            int r10 = r3 + -1
            int r9 = gallopLeft(r9, r5, r6, r3, r10)
            int r9 = r3 - r9
            if (r9 == 0) goto L_0x00c2
            int r10 = r13 - r9
            int r13 = r14 - r9
            int r3 = r3 - r9
            int r14 = r13 + 1
            int r15 = r10 + 1
            java.lang.System.arraycopy((java.lang.Object) r5, (int) r14, (java.lang.Object) r4, (int) r15, (int) r9)
            if (r3 > r8) goto L_0x00c4
            r12 = r10
            goto L_0x006f
        L_0x00c2:
            r10 = r13
            r13 = r14
        L_0x00c4:
            int r14 = r10 + -1
            int r15 = r11 + -1
            r11 = r4[r11]
            r4[r10] = r11
            int r2 = r2 + -1
            if (r2 != 0) goto L_0x00f8
            r9 = r13
            r12 = r14
            r11 = r15
        L_0x00d3:
            if (r7 >= r8) goto L_0x00d6
            r7 = r8
        L_0x00d6:
            r0.minGallop = r7
            if (r3 != r8) goto L_0x00e7
            int r12 = r12 - r2
            int r11 = r11 - r2
            int r11 = r11 + r8
            int r0 = r12 + 1
            java.lang.System.arraycopy((java.lang.Object) r4, (int) r11, (java.lang.Object) r4, (int) r0, (int) r2)
            r0 = r5[r9]
            r4[r12] = r0
            goto L_0x00ef
        L_0x00e7:
            if (r3 == 0) goto L_0x00f0
            int r0 = r3 + -1
            int r12 = r12 - r0
            java.lang.System.arraycopy((java.lang.Object) r5, (int) r6, (java.lang.Object) r4, (int) r12, (int) r3)
        L_0x00ef:
            return
        L_0x00f0:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Comparison method violates its general contract!"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x00f8:
            int r7 = r7 + -1
            r10 = 7
            if (r12 < r10) goto L_0x00ff
            r11 = r8
            goto L_0x0100
        L_0x00ff:
            r11 = 0
        L_0x0100:
            if (r9 < r10) goto L_0x0104
            r9 = r8
            goto L_0x0105
        L_0x0104:
            r9 = 0
        L_0x0105:
            r9 = r9 | r11
            if (r9 != 0) goto L_0x0114
            if (r7 >= 0) goto L_0x010c
            r12 = 0
            goto L_0x010d
        L_0x010c:
            r12 = r7
        L_0x010d:
            int r7 = r12 + 2
            r9 = r13
            r10 = r14
            r11 = r15
            goto L_0x0040
        L_0x0114:
            r9 = r13
            r10 = r14
            r11 = r15
            goto L_0x0079
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.ComparableTimSort.mergeHi(int, int, int, int):void");
    }

    private Object[] ensureCapacity(int i) {
        if (this.tmpLen < i) {
            int numberOfLeadingZeros = (-1 >>> Integer.numberOfLeadingZeros(i)) + 1;
            if (numberOfLeadingZeros >= 0) {
                i = Math.min(numberOfLeadingZeros, this.f674a.length >>> 1);
            }
            this.tmp = new Object[i];
            this.tmpLen = i;
            this.tmpBase = 0;
        }
        return this.tmp;
    }
}

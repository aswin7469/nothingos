package com.google.zxing.oned;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public final class Code128Reader extends OneDReader {
    private static final int CODE_CODE_A = 101;
    private static final int CODE_CODE_B = 100;
    private static final int CODE_CODE_C = 99;
    private static final int CODE_FNC_1 = 102;
    private static final int CODE_FNC_2 = 97;
    private static final int CODE_FNC_3 = 96;
    private static final int CODE_FNC_4_A = 101;
    private static final int CODE_FNC_4_B = 100;
    static final int[][] CODE_PATTERNS = {new int[]{2, 1, 2, 2, 2, 2}, new int[]{2, 2, 2, 1, 2, 2}, new int[]{2, 2, 2, 2, 2, 1}, new int[]{1, 2, 1, 2, 2, 3}, new int[]{1, 2, 1, 3, 2, 2}, new int[]{1, 3, 1, 2, 2, 2}, new int[]{1, 2, 2, 2, 1, 3}, new int[]{1, 2, 2, 3, 1, 2}, new int[]{1, 3, 2, 2, 1, 2}, new int[]{2, 2, 1, 2, 1, 3}, new int[]{2, 2, 1, 3, 1, 2}, new int[]{2, 3, 1, 2, 1, 2}, new int[]{1, 1, 2, 2, 3, 2}, new int[]{1, 2, 2, 1, 3, 2}, new int[]{1, 2, 2, 2, 3, 1}, new int[]{1, 1, 3, 2, 2, 2}, new int[]{1, 2, 3, 1, 2, 2}, new int[]{1, 2, 3, 2, 2, 1}, new int[]{2, 2, 3, 2, 1, 1}, new int[]{2, 2, 1, 1, 3, 2}, new int[]{2, 2, 1, 2, 3, 1}, new int[]{2, 1, 3, 2, 1, 2}, new int[]{2, 2, 3, 1, 1, 2}, new int[]{3, 1, 2, 1, 3, 1}, new int[]{3, 1, 1, 2, 2, 2}, new int[]{3, 2, 1, 1, 2, 2}, new int[]{3, 2, 1, 2, 2, 1}, new int[]{3, 1, 2, 2, 1, 2}, new int[]{3, 2, 2, 1, 1, 2}, new int[]{3, 2, 2, 2, 1, 1}, new int[]{2, 1, 2, 1, 2, 3}, new int[]{2, 1, 2, 3, 2, 1}, new int[]{2, 3, 2, 1, 2, 1}, new int[]{1, 1, 1, 3, 2, 3}, new int[]{1, 3, 1, 1, 2, 3}, new int[]{1, 3, 1, 3, 2, 1}, new int[]{1, 1, 2, 3, 1, 3}, new int[]{1, 3, 2, 1, 1, 3}, new int[]{1, 3, 2, 3, 1, 1}, new int[]{2, 1, 1, 3, 1, 3}, new int[]{2, 3, 1, 1, 1, 3}, new int[]{2, 3, 1, 3, 1, 1}, new int[]{1, 1, 2, 1, 3, 3}, new int[]{1, 1, 2, 3, 3, 1}, new int[]{1, 3, 2, 1, 3, 1}, new int[]{1, 1, 3, 1, 2, 3}, new int[]{1, 1, 3, 3, 2, 1}, new int[]{1, 3, 3, 1, 2, 1}, new int[]{3, 1, 3, 1, 2, 1}, new int[]{2, 1, 1, 3, 3, 1}, new int[]{2, 3, 1, 1, 3, 1}, new int[]{2, 1, 3, 1, 1, 3}, new int[]{2, 1, 3, 3, 1, 1}, new int[]{2, 1, 3, 1, 3, 1}, new int[]{3, 1, 1, 1, 2, 3}, new int[]{3, 1, 1, 3, 2, 1}, new int[]{3, 3, 1, 1, 2, 1}, new int[]{3, 1, 2, 1, 1, 3}, new int[]{3, 1, 2, 3, 1, 1}, new int[]{3, 3, 2, 1, 1, 1}, new int[]{3, 1, 4, 1, 1, 1}, new int[]{2, 2, 1, 4, 1, 1}, new int[]{4, 3, 1, 1, 1, 1}, new int[]{1, 1, 1, 2, 2, 4}, new int[]{1, 1, 1, 4, 2, 2}, new int[]{1, 2, 1, 1, 2, 4}, new int[]{1, 2, 1, 4, 2, 1}, new int[]{1, 4, 1, 1, 2, 2}, new int[]{1, 4, 1, 2, 2, 1}, new int[]{1, 1, 2, 2, 1, 4}, new int[]{1, 1, 2, 4, 1, 2}, new int[]{1, 2, 2, 1, 1, 4}, new int[]{1, 2, 2, 4, 1, 1}, new int[]{1, 4, 2, 1, 1, 2}, new int[]{1, 4, 2, 2, 1, 1}, new int[]{2, 4, 1, 2, 1, 1}, new int[]{2, 2, 1, 1, 1, 4}, new int[]{4, 1, 3, 1, 1, 1}, new int[]{2, 4, 1, 1, 1, 2}, new int[]{1, 3, 4, 1, 1, 1}, new int[]{1, 1, 1, 2, 4, 2}, new int[]{1, 2, 1, 1, 4, 2}, new int[]{1, 2, 1, 2, 4, 1}, new int[]{1, 1, 4, 2, 1, 2}, new int[]{1, 2, 4, 1, 1, 2}, new int[]{1, 2, 4, 2, 1, 1}, new int[]{4, 1, 1, 2, 1, 2}, new int[]{4, 2, 1, 1, 1, 2}, new int[]{4, 2, 1, 2, 1, 1}, new int[]{2, 1, 2, 1, 4, 1}, new int[]{2, 1, 4, 1, 2, 1}, new int[]{4, 1, 2, 1, 2, 1}, new int[]{1, 1, 1, 1, 4, 3}, new int[]{1, 1, 1, 3, 4, 1}, new int[]{1, 3, 1, 1, 4, 1}, new int[]{1, 1, 4, 1, 1, 3}, new int[]{1, 1, 4, 3, 1, 1}, new int[]{4, 1, 1, 1, 1, 3}, new int[]{4, 1, 1, 3, 1, 1}, new int[]{1, 1, 3, 1, 4, 1}, new int[]{1, 1, 4, 1, 3, 1}, new int[]{3, 1, 1, 1, 4, 1}, new int[]{4, 1, 1, 1, 3, 1}, new int[]{2, 1, 1, 4, 1, 2}, new int[]{2, 1, 1, 2, 1, 4}, new int[]{2, 1, 1, 2, 3, 2}, new int[]{2, 3, 3, 1, 1, 1, 2}};
    private static final int CODE_SHIFT = 98;
    private static final int CODE_START_A = 103;
    private static final int CODE_START_B = 104;
    private static final int CODE_START_C = 105;
    private static final int CODE_STOP = 106;
    private static final float MAX_AVG_VARIANCE = 0.25f;
    private static final float MAX_INDIVIDUAL_VARIANCE = 0.7f;

    private static int[] findStartPattern(BitArray bitArray) throws NotFoundException {
        int size = bitArray.getSize();
        int nextSet = bitArray.getNextSet(0);
        int[] iArr = new int[6];
        boolean z = false;
        int i = 0;
        int i2 = nextSet;
        while (nextSet < size) {
            if (bitArray.get(nextSet) != z) {
                iArr[i] = iArr[i] + 1;
            } else {
                if (i == 5) {
                    int i3 = -1;
                    float f = 0.25f;
                    for (int i4 = 103; i4 <= 105; i4++) {
                        float patternMatchVariance = patternMatchVariance(iArr, CODE_PATTERNS[i4], 0.7f);
                        if (patternMatchVariance < f) {
                            i3 = i4;
                            f = patternMatchVariance;
                        }
                    }
                    if (i3 < 0 || !bitArray.isRange(Math.max(0, i2 - ((nextSet - i2) / 2)), i2, false)) {
                        i2 += iArr[0] + iArr[1];
                        int i5 = i - 1;
                        System.arraycopy((Object) iArr, 2, (Object) iArr, 0, i5);
                        iArr[i5] = 0;
                        iArr[i] = 0;
                        i--;
                    } else {
                        return new int[]{i2, nextSet, i3};
                    }
                } else {
                    i++;
                }
                iArr[i] = 1;
                z = !z;
            }
            nextSet++;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static int decodeCode(BitArray bitArray, int[] iArr, int i) throws NotFoundException {
        recordPattern(bitArray, i, iArr);
        float f = 0.25f;
        int i2 = -1;
        int i3 = 0;
        while (true) {
            int[][] iArr2 = CODE_PATTERNS;
            if (i3 >= iArr2.length) {
                break;
            }
            float patternMatchVariance = patternMatchVariance(iArr, iArr2[i3], 0.7f);
            if (patternMatchVariance < f) {
                i2 = i3;
                f = patternMatchVariance;
            }
            i3++;
        }
        if (i2 >= 0) {
            return i2;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x01b6, code lost:
        r10 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x01b9, code lost:
        if (r17 == false) goto L_0x01c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x01bb, code lost:
        if (r14 != 'e') goto L_0x01bf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x01bd, code lost:
        r14 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x01bf, code lost:
        r14 = 'e';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x01c0, code lost:
        r17 = r10;
        r2 = true;
        r15 = 6;
        r24 = r12;
        r12 = r8;
        r8 = r20;
        r20 = r18;
        r18 = r24;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00f4, code lost:
        if (r3 != false) goto L_0x0153;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00fa, code lost:
        r14 = 'd';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0111, code lost:
        r2 = 'd';
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x011f, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x014b, code lost:
        r2 = false;
        r3 = false;
        r5 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0151, code lost:
        if (r3 != false) goto L_0x0153;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0153, code lost:
        r2 = false;
        r3 = false;
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0157, code lost:
        r2 = false;
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x015a, code lost:
        r2 = false;
        r14 = 'c';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x015f, code lost:
        r14 = 'e';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0162, code lost:
        r2 = false;
        r22 = 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0166, code lost:
        r2 = false;
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0169, code lost:
        r10 = r2;
        r2 = 'd';
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.Result decodeRow(int r26, com.google.zxing.common.BitArray r27, java.util.Map<com.google.zxing.DecodeHintType, ?> r28) throws com.google.zxing.NotFoundException, com.google.zxing.FormatException, com.google.zxing.ChecksumException {
        /*
            r25 = this;
            r0 = r27
            r1 = r28
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0012
            com.google.zxing.DecodeHintType r4 = com.google.zxing.DecodeHintType.ASSUME_GS1
            boolean r1 = r1.containsKey(r4)
            if (r1 == 0) goto L_0x0012
            r1 = r2
            goto L_0x0013
        L_0x0012:
            r1 = r3
        L_0x0013:
            int[] r4 = findStartPattern(r27)
            r5 = 2
            r6 = r4[r5]
            java.util.ArrayList r7 = new java.util.ArrayList
            r8 = 20
            r7.<init>((int) r8)
            byte r9 = (byte) r6
            java.lang.Byte r9 = java.lang.Byte.valueOf((byte) r9)
            r7.add(r9)
            switch(r6) {
                case 103: goto L_0x0037;
                case 104: goto L_0x0034;
                case 105: goto L_0x0031;
                default: goto L_0x002c;
            }
        L_0x002c:
            com.google.zxing.FormatException r0 = com.google.zxing.FormatException.getFormatInstance()
            throw r0
        L_0x0031:
            r12 = 99
            goto L_0x0039
        L_0x0034:
            r12 = 100
            goto L_0x0039
        L_0x0037:
            r12 = 101(0x65, float:1.42E-43)
        L_0x0039:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>((int) r8)
            r8 = r4[r3]
            r14 = r4[r2]
            r15 = 6
            int[] r9 = new int[r15]
            r21 = r2
            r5 = r3
            r16 = r5
            r17 = r16
            r18 = r17
            r19 = r18
            r20 = r19
            r22 = r20
            r24 = r12
            r12 = r8
            r8 = r14
            r14 = r24
        L_0x005a:
            if (r16 != 0) goto L_0x01cf
            int r12 = decodeCode(r0, r9, r8)
            byte r11 = (byte) r12
            java.lang.Byte r11 = java.lang.Byte.valueOf((byte) r11)
            r7.add(r11)
            r11 = 106(0x6a, float:1.49E-43)
            if (r12 == r11) goto L_0x006e
            r21 = r2
        L_0x006e:
            if (r12 == r11) goto L_0x0076
            int r19 = r19 + 1
            int r20 = r19 * r12
            int r6 = r6 + r20
        L_0x0076:
            r20 = r8
            r10 = 0
        L_0x0079:
            if (r10 >= r15) goto L_0x0082
            r23 = r9[r10]
            int r20 = r20 + r23
            int r10 = r10 + 1
            goto L_0x0079
        L_0x0082:
            switch(r12) {
                case 103: goto L_0x0090;
                case 104: goto L_0x0090;
                case 105: goto L_0x0090;
                default: goto L_0x0085;
            }
        L_0x0085:
            r10 = 96
            java.lang.String r15 = "]C1"
            switch(r14) {
                case 99: goto L_0x016d;
                case 100: goto L_0x00fe;
                case 101: goto L_0x0095;
                default: goto L_0x008c;
            }
        L_0x008c:
            r2 = 100
            goto L_0x01b6
        L_0x0090:
            com.google.zxing.FormatException r0 = com.google.zxing.FormatException.getFormatInstance()
            throw r0
        L_0x0095:
            r2 = 64
            if (r12 >= r2) goto L_0x00ad
            if (r3 != r5) goto L_0x00a3
            int r2 = r12 + 32
            char r2 = (char) r2
            r13.append((char) r2)
            goto L_0x0111
        L_0x00a3:
            int r2 = r12 + 32
            int r2 = r2 + 128
            char r2 = (char) r2
            r13.append((char) r2)
            goto L_0x0111
        L_0x00ad:
            if (r12 >= r10) goto L_0x00bf
            if (r3 != r5) goto L_0x00b8
            int r2 = r12 + -64
            char r2 = (char) r2
            r13.append((char) r2)
            goto L_0x0111
        L_0x00b8:
            int r2 = r12 + 64
            char r2 = (char) r2
            r13.append((char) r2)
            goto L_0x0111
        L_0x00bf:
            if (r12 == r11) goto L_0x00c3
            r21 = 0
        L_0x00c3:
            if (r12 == r11) goto L_0x0166
            switch(r12) {
                case 97: goto L_0x0162;
                case 98: goto L_0x00f9;
                case 99: goto L_0x015a;
                case 100: goto L_0x00f7;
                case 101: goto L_0x00ed;
                case 102: goto L_0x00c9;
                default: goto L_0x00c8;
            }
        L_0x00c8:
            goto L_0x011f
        L_0x00c9:
            int r2 = r13.length()
            if (r2 != 0) goto L_0x00d2
            r22 = 1
            goto L_0x00db
        L_0x00d2:
            int r2 = r13.length()
            r10 = 1
            if (r2 != r10) goto L_0x00db
            r22 = 2
        L_0x00db:
            if (r1 == 0) goto L_0x011f
            int r2 = r13.length()
            if (r2 != 0) goto L_0x00e7
            r13.append((java.lang.String) r15)
            goto L_0x00c8
        L_0x00e7:
            r2 = 29
            r13.append((char) r2)
            goto L_0x00c8
        L_0x00ed:
            if (r5 != 0) goto L_0x00f2
            if (r3 == 0) goto L_0x00f2
            goto L_0x014b
        L_0x00f2:
            if (r5 == 0) goto L_0x0157
            if (r3 == 0) goto L_0x0157
            goto L_0x0153
        L_0x00f7:
            r2 = 0
            goto L_0x00fa
        L_0x00f9:
            r2 = 1
        L_0x00fa:
            r14 = 100
            goto L_0x0169
        L_0x00fe:
            if (r12 >= r10) goto L_0x0116
            if (r3 != r5) goto L_0x0109
            int r2 = r12 + 32
            char r2 = (char) r2
            r13.append((char) r2)
            goto L_0x0111
        L_0x0109:
            int r2 = r12 + 32
            int r2 = r2 + 128
            char r2 = (char) r2
            r13.append((char) r2)
        L_0x0111:
            r2 = 100
            r3 = 0
            goto L_0x01b6
        L_0x0116:
            if (r12 == r11) goto L_0x011a
            r21 = 0
        L_0x011a:
            if (r12 == r11) goto L_0x0166
            switch(r12) {
                case 97: goto L_0x0162;
                case 98: goto L_0x015e;
                case 99: goto L_0x015a;
                case 100: goto L_0x0147;
                case 101: goto L_0x0145;
                case 102: goto L_0x0121;
                default: goto L_0x011f;
            }
        L_0x011f:
            r2 = 0
            goto L_0x0169
        L_0x0121:
            int r2 = r13.length()
            if (r2 != 0) goto L_0x012a
            r22 = 1
            goto L_0x0133
        L_0x012a:
            int r2 = r13.length()
            r10 = 1
            if (r2 != r10) goto L_0x0133
            r22 = 2
        L_0x0133:
            if (r1 == 0) goto L_0x011f
            int r2 = r13.length()
            if (r2 != 0) goto L_0x013f
            r13.append((java.lang.String) r15)
            goto L_0x011f
        L_0x013f:
            r2 = 29
            r13.append((char) r2)
            goto L_0x011f
        L_0x0145:
            r2 = 0
            goto L_0x015f
        L_0x0147:
            if (r5 != 0) goto L_0x014f
            if (r3 == 0) goto L_0x014f
        L_0x014b:
            r2 = 0
            r3 = 0
            r5 = 1
            goto L_0x0169
        L_0x014f:
            if (r5 == 0) goto L_0x0157
            if (r3 == 0) goto L_0x0157
        L_0x0153:
            r2 = 0
            r3 = 0
            r5 = 0
            goto L_0x0169
        L_0x0157:
            r2 = 0
            r3 = 1
            goto L_0x0169
        L_0x015a:
            r2 = 0
            r14 = 99
            goto L_0x0169
        L_0x015e:
            r2 = 1
        L_0x015f:
            r14 = 101(0x65, float:1.42E-43)
            goto L_0x0169
        L_0x0162:
            r2 = 0
            r22 = 4
            goto L_0x0169
        L_0x0166:
            r2 = 0
            r16 = 1
        L_0x0169:
            r10 = r2
            r2 = 100
            goto L_0x01b7
        L_0x016d:
            r2 = 100
            if (r12 >= r2) goto L_0x017e
            r10 = 10
            if (r12 >= r10) goto L_0x017a
            r10 = 48
            r13.append((char) r10)
        L_0x017a:
            r13.append((int) r12)
            goto L_0x01b6
        L_0x017e:
            if (r12 == r11) goto L_0x0182
            r21 = 0
        L_0x0182:
            if (r12 == r11) goto L_0x01b2
            switch(r12) {
                case 100: goto L_0x01b0;
                case 101: goto L_0x01ac;
                case 102: goto L_0x0188;
                default: goto L_0x0187;
            }
        L_0x0187:
            goto L_0x01b6
        L_0x0188:
            int r10 = r13.length()
            if (r10 != 0) goto L_0x0191
            r22 = 1
            goto L_0x019a
        L_0x0191:
            int r10 = r13.length()
            r11 = 1
            if (r10 != r11) goto L_0x019a
            r22 = 2
        L_0x019a:
            if (r1 == 0) goto L_0x01b6
            int r10 = r13.length()
            if (r10 != 0) goto L_0x01a6
            r13.append((java.lang.String) r15)
            goto L_0x01b6
        L_0x01a6:
            r10 = 29
            r13.append((char) r10)
            goto L_0x01b6
        L_0x01ac:
            r10 = 0
            r14 = 101(0x65, float:1.42E-43)
            goto L_0x01b7
        L_0x01b0:
            r14 = r2
            goto L_0x01b6
        L_0x01b2:
            r10 = 0
            r16 = 1
            goto L_0x01b7
        L_0x01b6:
            r10 = 0
        L_0x01b7:
            r11 = 101(0x65, float:1.42E-43)
            if (r17 == 0) goto L_0x01c0
            if (r14 != r11) goto L_0x01bf
            r14 = r2
            goto L_0x01c0
        L_0x01bf:
            r14 = r11
        L_0x01c0:
            r17 = r10
            r2 = 1
            r15 = 6
            r24 = r12
            r12 = r8
            r8 = r20
            r20 = r18
            r18 = r24
            goto L_0x005a
        L_0x01cf:
            int r1 = r8 - r12
            int r2 = r0.getNextUnset(r8)
            int r3 = r27.getSize()
            int r5 = r2 - r12
            r8 = 2
            int r5 = r5 / r8
            int r5 = r5 + r2
            int r3 = java.lang.Math.min((int) r3, (int) r5)
            r5 = 0
            boolean r0 = r0.isRange(r2, r3, r5)
            if (r0 == 0) goto L_0x0275
            r3 = r20
            int r19 = r19 * r3
            int r6 = r6 - r19
            int r6 = r6 % 103
            if (r6 != r3) goto L_0x0270
            int r0 = r13.length()
            if (r0 == 0) goto L_0x026b
            if (r0 <= 0) goto L_0x020c
            if (r21 == 0) goto L_0x020c
            r2 = 99
            if (r14 != r2) goto L_0x0207
            int r2 = r0 + -2
            r13.delete((int) r2, (int) r0)
            goto L_0x020c
        L_0x0207:
            int r2 = r0 + -1
            r13.delete((int) r2, (int) r0)
        L_0x020c:
            r0 = 1
            r2 = r4[r0]
            r0 = 0
            r3 = r4[r0]
            int r2 = r2 + r3
            float r0 = (float) r2
            r2 = 1073741824(0x40000000, float:2.0)
            float r0 = r0 / r2
            float r3 = (float) r12
            float r1 = (float) r1
            float r1 = r1 / r2
            float r3 = r3 + r1
            int r1 = r7.size()
            byte[] r2 = new byte[r1]
            r5 = 0
        L_0x0222:
            if (r5 >= r1) goto L_0x0233
            java.lang.Object r4 = r7.get(r5)
            java.lang.Byte r4 = (java.lang.Byte) r4
            byte r4 = r4.byteValue()
            r2[r5] = r4
            int r5 = r5 + 1
            goto L_0x0222
        L_0x0233:
            com.google.zxing.Result r1 = new com.google.zxing.Result
            java.lang.String r4 = r13.toString()
            r5 = 2
            com.google.zxing.ResultPoint[] r5 = new com.google.zxing.ResultPoint[r5]
            com.google.zxing.ResultPoint r6 = new com.google.zxing.ResultPoint
            r7 = r26
            float r7 = (float) r7
            r6.<init>(r0, r7)
            r0 = 0
            r5[r0] = r6
            com.google.zxing.ResultPoint r0 = new com.google.zxing.ResultPoint
            r0.<init>(r3, r7)
            r3 = 1
            r5[r3] = r0
            com.google.zxing.BarcodeFormat r0 = com.google.zxing.BarcodeFormat.CODE_128
            r1.<init>(r4, r2, r5, r0)
            com.google.zxing.ResultMetadataType r0 = com.google.zxing.ResultMetadataType.SYMBOLOGY_IDENTIFIER
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "]C"
            r2.<init>((java.lang.String) r3)
            r3 = r22
            java.lang.StringBuilder r2 = r2.append((int) r3)
            java.lang.String r2 = r2.toString()
            r1.putMetadata(r0, r2)
            return r1
        L_0x026b:
            com.google.zxing.NotFoundException r0 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r0
        L_0x0270:
            com.google.zxing.ChecksumException r0 = com.google.zxing.ChecksumException.getChecksumInstance()
            throw r0
        L_0x0275:
            com.google.zxing.NotFoundException r0 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.Code128Reader.decodeRow(int, com.google.zxing.common.BitArray, java.util.Map):com.google.zxing.Result");
    }
}

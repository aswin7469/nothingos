package com.google.zxing.oned;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public final class CodaBarReader extends OneDReader {
    static final char[] ALPHABET = ALPHABET_STRING.toCharArray();
    private static final String ALPHABET_STRING = "0123456789-$:/.+ABCD";
    static final int[] CHARACTER_ENCODINGS = {3, 6, 9, 96, 18, 66, 33, 36, 48, 72, 12, 24, 69, 81, 84, 21, 26, 41, 11, 14};
    private static final float MAX_ACCEPTABLE = 2.0f;
    private static final int MIN_CHARACTER_LENGTH = 3;
    private static final float PADDING = 1.5f;
    private static final char[] STARTEND_ENCODING = {'A', 'B', 'C', 'D'};
    private int counterLength = 0;
    private int[] counters = new int[80];
    private final StringBuilder decodeRowResult = new StringBuilder(20);

    /* JADX WARNING: Removed duplicated region for block: B:3:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0109 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.Result decodeRow(int r11, com.google.zxing.common.BitArray r12, java.util.Map<com.google.zxing.DecodeHintType, ?> r13) throws com.google.zxing.NotFoundException {
        /*
            r10 = this;
            int[] r0 = r10.counters
            r1 = 0
            java.util.Arrays.fill((int[]) r0, (int) r1)
            r10.setCounters(r12)
            int r12 = r10.findStartPattern()
            java.lang.StringBuilder r0 = r10.decodeRowResult
            r0.setLength(r1)
            r0 = r12
        L_0x0013:
            int r2 = r10.toNarrowWidePattern(r0)
            r3 = -1
            if (r2 == r3) goto L_0x0109
            java.lang.StringBuilder r4 = r10.decodeRowResult
            char r5 = (char) r2
            r4.append((char) r5)
            int r0 = r0 + 8
            java.lang.StringBuilder r4 = r10.decodeRowResult
            int r4 = r4.length()
            r5 = 1
            if (r4 <= r5) goto L_0x0038
            char[] r4 = STARTEND_ENCODING
            char[] r6 = ALPHABET
            char r2 = r6[r2]
            boolean r2 = arrayContains(r4, r2)
            if (r2 == 0) goto L_0x0038
            goto L_0x003c
        L_0x0038:
            int r2 = r10.counterLength
            if (r0 < r2) goto L_0x0013
        L_0x003c:
            int[] r2 = r10.counters
            int r4 = r0 + -1
            r2 = r2[r4]
            r6 = -8
            r7 = r1
        L_0x0044:
            if (r6 >= r3) goto L_0x0050
            int[] r8 = r10.counters
            int r9 = r0 + r6
            r8 = r8[r9]
            int r7 = r7 + r8
            int r6 = r6 + 1
            goto L_0x0044
        L_0x0050:
            int r3 = r10.counterLength
            r6 = 2
            if (r0 >= r3) goto L_0x005e
            int r7 = r7 / r6
            if (r2 < r7) goto L_0x0059
            goto L_0x005e
        L_0x0059:
            com.google.zxing.NotFoundException r10 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r10
        L_0x005e:
            r10.validatePattern(r12)
            r0 = r1
        L_0x0062:
            java.lang.StringBuilder r2 = r10.decodeRowResult
            int r2 = r2.length()
            if (r0 >= r2) goto L_0x007a
            java.lang.StringBuilder r2 = r10.decodeRowResult
            char[] r3 = ALPHABET
            char r7 = r2.charAt(r0)
            char r3 = r3[r7]
            r2.setCharAt(r0, r3)
            int r0 = r0 + 1
            goto L_0x0062
        L_0x007a:
            java.lang.StringBuilder r0 = r10.decodeRowResult
            char r0 = r0.charAt(r1)
            char[] r2 = STARTEND_ENCODING
            boolean r0 = arrayContains(r2, r0)
            if (r0 == 0) goto L_0x0104
            java.lang.StringBuilder r0 = r10.decodeRowResult
            int r3 = r0.length()
            int r3 = r3 - r5
            char r0 = r0.charAt(r3)
            boolean r0 = arrayContains(r2, r0)
            if (r0 == 0) goto L_0x00ff
            java.lang.StringBuilder r0 = r10.decodeRowResult
            int r0 = r0.length()
            r2 = 3
            if (r0 <= r2) goto L_0x00fa
            if (r13 == 0) goto L_0x00ac
            com.google.zxing.DecodeHintType r0 = com.google.zxing.DecodeHintType.RETURN_CODABAR_START_END
            boolean r13 = r13.containsKey(r0)
            if (r13 != 0) goto L_0x00bb
        L_0x00ac:
            java.lang.StringBuilder r13 = r10.decodeRowResult
            int r0 = r13.length()
            int r0 = r0 - r5
            r13.deleteCharAt((int) r0)
            java.lang.StringBuilder r13 = r10.decodeRowResult
            r13.deleteCharAt((int) r1)
        L_0x00bb:
            r13 = r1
            r0 = r13
        L_0x00bd:
            if (r13 >= r12) goto L_0x00c7
            int[] r2 = r10.counters
            r2 = r2[r13]
            int r0 = r0 + r2
            int r13 = r13 + 1
            goto L_0x00bd
        L_0x00c7:
            float r13 = (float) r0
        L_0x00c8:
            if (r12 >= r4) goto L_0x00d2
            int[] r2 = r10.counters
            r2 = r2[r12]
            int r0 = r0 + r2
            int r12 = r12 + 1
            goto L_0x00c8
        L_0x00d2:
            float r12 = (float) r0
            com.google.zxing.Result r0 = new com.google.zxing.Result
            java.lang.StringBuilder r10 = r10.decodeRowResult
            java.lang.String r10 = r10.toString()
            com.google.zxing.ResultPoint[] r2 = new com.google.zxing.ResultPoint[r6]
            com.google.zxing.ResultPoint r3 = new com.google.zxing.ResultPoint
            float r11 = (float) r11
            r3.<init>(r13, r11)
            r2[r1] = r3
            com.google.zxing.ResultPoint r13 = new com.google.zxing.ResultPoint
            r13.<init>(r12, r11)
            r2[r5] = r13
            com.google.zxing.BarcodeFormat r11 = com.google.zxing.BarcodeFormat.CODABAR
            r12 = 0
            r0.<init>(r10, r12, r2, r11)
            com.google.zxing.ResultMetadataType r10 = com.google.zxing.ResultMetadataType.SYMBOLOGY_IDENTIFIER
            java.lang.String r11 = "]F0"
            r0.putMetadata(r10, r11)
            return r0
        L_0x00fa:
            com.google.zxing.NotFoundException r10 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r10
        L_0x00ff:
            com.google.zxing.NotFoundException r10 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r10
        L_0x0104:
            com.google.zxing.NotFoundException r10 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r10
        L_0x0109:
            com.google.zxing.NotFoundException r10 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.CodaBarReader.decodeRow(int, com.google.zxing.common.BitArray, java.util.Map):com.google.zxing.Result");
    }

    private void validatePattern(int i) throws NotFoundException {
        int[] iArr = {0, 0, 0, 0};
        int[] iArr2 = {0, 0, 0, 0};
        int length = this.decodeRowResult.length() - 1;
        int i2 = i;
        int i3 = 0;
        while (true) {
            if (i3 > length) {
                break;
            }
            int i4 = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(i3)];
            for (int i5 = 6; i5 >= 0; i5--) {
                int i6 = (i5 & 1) + ((i4 & 1) * 2);
                iArr[i6] = iArr[i6] + this.counters[i2 + i5];
                iArr2[i6] = iArr2[i6] + 1;
                i4 >>= 1;
            }
            i2 += 8;
            i3++;
        }
        float[] fArr = new float[4];
        float[] fArr2 = new float[4];
        for (int i7 = 0; i7 < 2; i7++) {
            fArr2[i7] = 0.0f;
            int i8 = i7 + 2;
            float f = ((float) iArr[i7]) / ((float) iArr2[i7]);
            int i9 = iArr[i8];
            int i10 = iArr2[i8];
            float f2 = (f + (((float) i9) / ((float) i10))) / MAX_ACCEPTABLE;
            fArr2[i8] = f2;
            fArr[i7] = f2;
            fArr[i8] = ((((float) i9) * MAX_ACCEPTABLE) + 1.5f) / ((float) i10);
        }
        int i11 = i;
        for (int i12 = 0; i12 <= length; i12++) {
            int i13 = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(i12)];
            for (int i14 = 6; i14 >= 0; i14--) {
                int i15 = (i14 & 1) + ((i13 & 1) * 2);
                float f3 = (float) this.counters[i11 + i14];
                if (f3 < fArr2[i15] || f3 > fArr[i15]) {
                    throw NotFoundException.getNotFoundInstance();
                }
                i13 >>= 1;
            }
            i11 += 8;
        }
    }

    private void setCounters(BitArray bitArray) throws NotFoundException {
        int i = 0;
        this.counterLength = 0;
        int nextUnset = bitArray.getNextUnset(0);
        int size = bitArray.getSize();
        if (nextUnset < size) {
            boolean z = true;
            while (nextUnset < size) {
                if (bitArray.get(nextUnset) != z) {
                    i++;
                } else {
                    counterAppend(i);
                    z = !z;
                    i = 1;
                }
                nextUnset++;
            }
            counterAppend(i);
            return;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private void counterAppend(int i) {
        int[] iArr = this.counters;
        int i2 = this.counterLength;
        iArr[i2] = i;
        int i3 = i2 + 1;
        this.counterLength = i3;
        if (i3 >= iArr.length) {
            int[] iArr2 = new int[(i3 * 2)];
            System.arraycopy((Object) iArr, 0, (Object) iArr2, 0, i3);
            this.counters = iArr2;
        }
    }

    private int findStartPattern() throws NotFoundException {
        for (int i = 1; i < this.counterLength; i += 2) {
            int narrowWidePattern = toNarrowWidePattern(i);
            if (narrowWidePattern != -1 && arrayContains(STARTEND_ENCODING, ALPHABET[narrowWidePattern])) {
                int i2 = 0;
                for (int i3 = i; i3 < i + 7; i3++) {
                    i2 += this.counters[i3];
                }
                if (i == 1 || this.counters[i - 1] >= i2 / 2) {
                    return i;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    static boolean arrayContains(char[] cArr, char c) {
        if (cArr != null) {
            for (char c2 : cArr) {
                if (c2 == c) {
                    return true;
                }
            }
        }
        return false;
    }

    private int toNarrowWidePattern(int i) {
        int i2 = i + 7;
        if (i2 >= this.counterLength) {
            return -1;
        }
        int[] iArr = this.counters;
        int i3 = Integer.MAX_VALUE;
        int i4 = 0;
        int i5 = Integer.MAX_VALUE;
        int i6 = 0;
        for (int i7 = i; i7 < i2; i7 += 2) {
            int i8 = iArr[i7];
            if (i8 < i5) {
                i5 = i8;
            }
            if (i8 > i6) {
                i6 = i8;
            }
        }
        int i9 = (i5 + i6) / 2;
        int i10 = 0;
        for (int i11 = i + 1; i11 < i2; i11 += 2) {
            int i12 = iArr[i11];
            if (i12 < i3) {
                i3 = i12;
            }
            if (i12 > i10) {
                i10 = i12;
            }
        }
        int i13 = (i3 + i10) / 2;
        int i14 = 128;
        int i15 = 0;
        for (int i16 = 0; i16 < 7; i16++) {
            i14 >>= 1;
            if (iArr[i + i16] > ((i16 & 1) == 0 ? i9 : i13)) {
                i15 |= i14;
            }
        }
        while (true) {
            int[] iArr2 = CHARACTER_ENCODINGS;
            if (i4 >= iArr2.length) {
                return -1;
            }
            if (iArr2[i4] == i15) {
                return i4;
            }
            i4++;
        }
    }
}

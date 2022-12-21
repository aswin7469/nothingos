package com.google.zxing.oned;

import android.icu.lang.UCharacter;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Map;

public final class Code93Reader extends OneDReader {
    private static final char[] ALPHABET = ALPHABET_STRING.toCharArray();
    static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*";
    static final int ASTERISK_ENCODING;
    static final int[] CHARACTER_ENCODINGS;
    private final int[] counters = new int[6];
    private final StringBuilder decodeRowResult = new StringBuilder(20);

    static {
        int[] iArr = {UCharacter.UnicodeBlock.MASARAM_GONDI_ID, 328, 324, 322, UCharacter.UnicodeBlock.OTTOMAN_SIYAQ_NUMBERS_ID, UCharacter.UnicodeBlock.EGYPTIAN_HIEROGLYPH_FORMAT_CONTROLS_ID, UCharacter.UnicodeBlock.OLD_SOGDIAN_ID, 336, UCharacter.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_F_ID, 266, 424, 420, 418, HttpURLConnection.HTTP_NOT_FOUND, 402, 394, StackStateAnimator.ANIMATION_DURATION_STANDARD, 356, 354, 308, 282, 344, 332, 326, 300, UCharacter.UnicodeBlock.SOYOMBO_ID, 436, 434, 428, 422, HttpURLConnection.HTTP_NOT_ACCEPTABLE, HttpURLConnection.HTTP_GONE, 364, 358, UCharacter.UnicodeBlock.CYPRO_MINOAN_ID, UCharacter.UnicodeBlock.LATIN_EXTENDED_G_ID, 302, 468, SysUiStatsLog.MEDIAOUTPUT_OP_INTERACTION_REPORT, 458, 366, 374, 430, UCharacter.UnicodeBlock.NANDINAGARI_ID, 474, 470, UCharacter.UnicodeBlock.SYMBOLS_FOR_LEGACY_COMPUTING_ID, 350};
        CHARACTER_ENCODINGS = iArr;
        ASTERISK_ENCODING = iArr[47];
    }

    public Result decodeRow(int i, BitArray bitArray, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException {
        int[] findAsteriskPattern = findAsteriskPattern(bitArray);
        int nextSet = bitArray.getNextSet(findAsteriskPattern[1]);
        int size = bitArray.getSize();
        int[] iArr = this.counters;
        Arrays.fill(iArr, 0);
        StringBuilder sb = this.decodeRowResult;
        sb.setLength(0);
        while (true) {
            recordPattern(bitArray, nextSet, iArr);
            int pattern = toPattern(iArr);
            if (pattern >= 0) {
                char patternToChar = patternToChar(pattern);
                sb.append(patternToChar);
                int i2 = nextSet;
                for (int i3 : iArr) {
                    i2 += i3;
                }
                int nextSet2 = bitArray.getNextSet(i2);
                if (patternToChar == '*') {
                    sb.deleteCharAt(sb.length() - 1);
                    int i4 = 0;
                    for (int i5 : iArr) {
                        i4 += i5;
                    }
                    if (nextSet2 == size || !bitArray.get(nextSet2)) {
                        throw NotFoundException.getNotFoundInstance();
                    } else if (sb.length() >= 2) {
                        checkChecksums(sb);
                        sb.setLength(sb.length() - 2);
                        float f = (float) i;
                        Result result = new Result(decodeExtended(sb), (byte[]) null, new ResultPoint[]{new ResultPoint(((float) (findAsteriskPattern[1] + findAsteriskPattern[0])) / 2.0f, f), new ResultPoint(((float) nextSet) + (((float) i4) / 2.0f), f)}, BarcodeFormat.CODE_93);
                        result.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]G0");
                        return result;
                    } else {
                        throw NotFoundException.getNotFoundInstance();
                    }
                } else {
                    nextSet = nextSet2;
                }
            } else {
                throw NotFoundException.getNotFoundInstance();
            }
        }
    }

    private int[] findAsteriskPattern(BitArray bitArray) throws NotFoundException {
        int size = bitArray.getSize();
        int nextSet = bitArray.getNextSet(0);
        Arrays.fill(this.counters, 0);
        int[] iArr = this.counters;
        int length = iArr.length;
        boolean z = false;
        int i = 0;
        int i2 = nextSet;
        while (nextSet < size) {
            if (bitArray.get(nextSet) != z) {
                iArr[i] = iArr[i] + 1;
            } else {
                if (i != length - 1) {
                    i++;
                } else if (toPattern(iArr) == ASTERISK_ENCODING) {
                    return new int[]{i2, nextSet};
                } else {
                    i2 += iArr[0] + iArr[1];
                    int i3 = i - 1;
                    System.arraycopy((Object) iArr, 2, (Object) iArr, 0, i3);
                    iArr[i3] = 0;
                    iArr[i] = 0;
                    i--;
                }
                iArr[i] = 1;
                z = !z;
            }
            nextSet++;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static int toPattern(int[] iArr) {
        int i = 0;
        for (int i2 : iArr) {
            i += i2;
        }
        int length = iArr.length;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            int round = Math.round((((float) iArr[i4]) * 9.0f) / ((float) i));
            if (round < 1 || round > 4) {
                return -1;
            }
            if ((i4 & 1) == 0) {
                for (int i5 = 0; i5 < round; i5++) {
                    i3 = (i3 << 1) | 1;
                }
            } else {
                i3 <<= round;
            }
        }
        return i3;
    }

    private static char patternToChar(int i) throws NotFoundException {
        int i2 = 0;
        while (true) {
            int[] iArr = CHARACTER_ENCODINGS;
            if (i2 >= iArr.length) {
                throw NotFoundException.getNotFoundInstance();
            } else if (iArr[i2] == i) {
                return ALPHABET[i2];
            } else {
                i2++;
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002c, code lost:
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00a3, code lost:
        r1.append(r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String decodeExtended(java.lang.CharSequence r9) throws com.google.zxing.FormatException {
        /*
            int r0 = r9.length()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>((int) r0)
            r2 = 0
            r3 = r2
        L_0x000b:
            if (r3 >= r0) goto L_0x00b3
            char r4 = r9.charAt(r3)
            r5 = 97
            if (r4 < r5) goto L_0x00ac
            r5 = 100
            if (r4 > r5) goto L_0x00ac
            int r5 = r0 + -1
            if (r3 >= r5) goto L_0x00a7
            int r3 = r3 + 1
            char r5 = r9.charAt(r3)
            r6 = 79
            r7 = 90
            r8 = 65
            switch(r4) {
                case 97: goto L_0x0096;
                case 98: goto L_0x004d;
                case 99: goto L_0x003c;
                case 100: goto L_0x002f;
                default: goto L_0x002c;
            }
        L_0x002c:
            r4 = r2
            goto L_0x00a3
        L_0x002f:
            if (r5 < r8) goto L_0x0037
            if (r5 > r7) goto L_0x0037
            int r5 = r5 + 32
            goto L_0x009c
        L_0x0037:
            com.google.zxing.FormatException r9 = com.google.zxing.FormatException.getFormatInstance()
            throw r9
        L_0x003c:
            if (r5 < r8) goto L_0x0043
            if (r5 > r6) goto L_0x0043
            int r5 = r5 + -32
            goto L_0x009c
        L_0x0043:
            if (r5 != r7) goto L_0x0048
            r4 = 58
            goto L_0x00a3
        L_0x0048:
            com.google.zxing.FormatException r9 = com.google.zxing.FormatException.getFormatInstance()
            throw r9
        L_0x004d:
            if (r5 < r8) goto L_0x0056
            r4 = 69
            if (r5 > r4) goto L_0x0056
            int r5 = r5 + -38
            goto L_0x009c
        L_0x0056:
            r4 = 70
            if (r5 < r4) goto L_0x0061
            r4 = 74
            if (r5 > r4) goto L_0x0061
            int r5 = r5 + -11
            goto L_0x009c
        L_0x0061:
            r4 = 75
            if (r5 < r4) goto L_0x006a
            if (r5 > r6) goto L_0x006a
            int r5 = r5 + 16
            goto L_0x009c
        L_0x006a:
            r4 = 80
            if (r5 < r4) goto L_0x0075
            r4 = 84
            if (r5 > r4) goto L_0x0075
            int r5 = r5 + 43
            goto L_0x009c
        L_0x0075:
            r4 = 85
            if (r5 != r4) goto L_0x007a
            goto L_0x002c
        L_0x007a:
            r4 = 86
            if (r5 != r4) goto L_0x0081
            r4 = 64
            goto L_0x00a3
        L_0x0081:
            r4 = 87
            if (r5 != r4) goto L_0x0088
            r4 = 96
            goto L_0x00a3
        L_0x0088:
            r4 = 88
            if (r5 < r4) goto L_0x0091
            if (r5 > r7) goto L_0x0091
            r4 = 127(0x7f, float:1.78E-43)
            goto L_0x00a3
        L_0x0091:
            com.google.zxing.FormatException r9 = com.google.zxing.FormatException.getFormatInstance()
            throw r9
        L_0x0096:
            if (r5 < r8) goto L_0x009e
            if (r5 > r7) goto L_0x009e
            int r5 = r5 + -64
        L_0x009c:
            char r4 = (char) r5
            goto L_0x00a3
        L_0x009e:
            com.google.zxing.FormatException r9 = com.google.zxing.FormatException.getFormatInstance()
            throw r9
        L_0x00a3:
            r1.append((char) r4)
            goto L_0x00af
        L_0x00a7:
            com.google.zxing.FormatException r9 = com.google.zxing.FormatException.getFormatInstance()
            throw r9
        L_0x00ac:
            r1.append((char) r4)
        L_0x00af:
            int r3 = r3 + 1
            goto L_0x000b
        L_0x00b3:
            java.lang.String r9 = r1.toString()
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.Code93Reader.decodeExtended(java.lang.CharSequence):java.lang.String");
    }

    private static void checkChecksums(CharSequence charSequence) throws ChecksumException {
        int length = charSequence.length();
        checkOneChecksum(charSequence, length - 2, 20);
        checkOneChecksum(charSequence, length - 1, 15);
    }

    private static void checkOneChecksum(CharSequence charSequence, int i, int i2) throws ChecksumException {
        int i3 = 0;
        int i4 = 1;
        for (int i5 = i - 1; i5 >= 0; i5--) {
            i3 += ALPHABET_STRING.indexOf((int) charSequence.charAt(i5)) * i4;
            i4++;
            if (i4 > i2) {
                i4 = 1;
            }
        }
        if (charSequence.charAt(i) != ALPHABET[i3 % 47]) {
            throw ChecksumException.getChecksumInstance();
        }
    }
}

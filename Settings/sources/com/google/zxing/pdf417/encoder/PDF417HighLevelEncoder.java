package com.google.zxing.pdf417.encoder;

import com.google.zxing.WriterException;
import java.math.BigInteger;
import java.util.Arrays;
/* loaded from: classes2.dex */
final class PDF417HighLevelEncoder {
    private static final byte[] MIXED;
    private static final byte[] TEXT_MIXED_RAW = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 38, 13, 9, 44, 58, 35, 45, 46, 36, 47, 43, 37, 42, 61, 94, 0, 32, 0, 0, 0};
    private static final byte[] TEXT_PUNCTUATION_RAW = {59, 60, 62, 64, 91, 92, 93, 95, 96, 126, 33, 13, 9, 44, 58, 10, 45, 46, 36, 47, 34, 124, 42, 40, 41, 63, 123, 125, 39, 0};
    private static final byte[] PUNCTUATION = new byte[128];

    private static boolean isAlphaLower(char c) {
        return c == ' ' || (c >= 'a' && c <= 'z');
    }

    private static boolean isAlphaUpper(char c) {
        return c == ' ' || (c >= 'A' && c <= 'Z');
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isText(char c) {
        return c == '\t' || c == '\n' || c == '\r' || (c >= ' ' && c <= '~');
    }

    static {
        byte[] bArr = new byte[128];
        MIXED = bArr;
        Arrays.fill(bArr, (byte) -1);
        byte b = 0;
        byte b2 = 0;
        while (true) {
            byte[] bArr2 = TEXT_MIXED_RAW;
            if (b2 >= bArr2.length) {
                break;
            }
            byte b3 = bArr2[b2];
            if (b3 > 0) {
                MIXED[b3] = b2;
            }
            b2 = (byte) (b2 + 1);
        }
        Arrays.fill(PUNCTUATION, (byte) -1);
        while (true) {
            byte[] bArr3 = TEXT_PUNCTUATION_RAW;
            if (b < bArr3.length) {
                byte b4 = bArr3[b];
                if (b4 > 0) {
                    PUNCTUATION[b4] = b;
                }
                b = (byte) (b + 1);
            } else {
                return;
            }
        }
    }

    private static byte[] getBytesForMessage(String str) {
        return str.getBytes();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String encodeHighLevel(String str, Compaction compaction) throws WriterException {
        StringBuilder sb = new StringBuilder(str.length());
        int length = str.length();
        if (compaction == Compaction.TEXT) {
            encodeText(str, 0, length, sb, 0);
        } else if (compaction == Compaction.BYTE) {
            byte[] bytesForMessage = getBytesForMessage(str);
            encodeBinary(bytesForMessage, 0, bytesForMessage.length, 1, sb);
        } else if (compaction == Compaction.NUMERIC) {
            sb.append((char) 902);
            encodeNumeric(str, 0, length, sb);
        } else {
            byte[] bArr = null;
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            while (i < length) {
                int determineConsecutiveDigitCount = determineConsecutiveDigitCount(str, i);
                if (determineConsecutiveDigitCount >= 13) {
                    sb.append((char) 902);
                    encodeNumeric(str, i, determineConsecutiveDigitCount, sb);
                    i += determineConsecutiveDigitCount;
                    i3 = 2;
                    i2 = 0;
                } else {
                    int determineConsecutiveTextCount = determineConsecutiveTextCount(str, i);
                    if (determineConsecutiveTextCount >= 5 || determineConsecutiveDigitCount == length) {
                        if (i3 != 0) {
                            sb.append((char) 900);
                            i2 = 0;
                            i3 = 0;
                        }
                        i2 = encodeText(str, i, determineConsecutiveTextCount, sb, i2);
                        i += determineConsecutiveTextCount;
                    } else {
                        if (bArr == null) {
                            bArr = getBytesForMessage(str);
                        }
                        int determineConsecutiveBinaryCount = determineConsecutiveBinaryCount(str, bArr, i);
                        if (determineConsecutiveBinaryCount == 0) {
                            determineConsecutiveBinaryCount = 1;
                        }
                        if (determineConsecutiveBinaryCount == 1 && i3 == 0) {
                            encodeBinary(bArr, i, 1, 0, sb);
                        } else {
                            encodeBinary(bArr, i, determineConsecutiveBinaryCount, i3, sb);
                            i2 = 0;
                            i3 = 1;
                        }
                        i += determineConsecutiveBinaryCount;
                    }
                }
            }
        }
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00f6 A[EDGE_INSN: B:21:0x00f6->B:22:0x00f6 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0011 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int encodeText(CharSequence charSequence, int i, int i2, StringBuilder sb, int i3) {
        StringBuilder sb2 = new StringBuilder(i2);
        int i4 = i3;
        int i5 = 0;
        while (true) {
            int i6 = i + i5;
            char charAt = charSequence.charAt(i6);
            if (i4 != 0) {
                if (i4 != 1) {
                    if (i4 == 2) {
                        if (isMixed(charAt)) {
                            sb2.append((char) MIXED[charAt]);
                        } else if (isAlphaUpper(charAt)) {
                            sb2.append((char) 28);
                            i4 = 0;
                        } else if (isAlphaLower(charAt)) {
                            sb2.append((char) 27);
                            i4 = 1;
                        } else {
                            int i7 = i6 + 1;
                            if (i7 < i2 && isPunctuation(charSequence.charAt(i7))) {
                                i4 = 3;
                                sb2.append((char) 25);
                            } else {
                                sb2.append((char) 29);
                                sb2.append((char) PUNCTUATION[charAt]);
                            }
                        }
                    } else if (isPunctuation(charAt)) {
                        sb2.append((char) PUNCTUATION[charAt]);
                    } else {
                        sb2.append((char) 29);
                        i4 = 0;
                    }
                } else if (isAlphaLower(charAt)) {
                    if (charAt == ' ') {
                        sb2.append((char) 26);
                    } else {
                        sb2.append((char) (charAt - 'a'));
                    }
                } else if (isAlphaUpper(charAt)) {
                    sb2.append((char) 27);
                    sb2.append((char) (charAt - 'A'));
                } else if (isMixed(charAt)) {
                    sb2.append((char) 28);
                    i4 = 2;
                } else {
                    sb2.append((char) 29);
                    sb2.append((char) PUNCTUATION[charAt]);
                }
                i5++;
                if (i5 < i2) {
                    break;
                }
            } else {
                if (isAlphaUpper(charAt)) {
                    if (charAt == ' ') {
                        sb2.append((char) 26);
                    } else {
                        sb2.append((char) (charAt - 'A'));
                    }
                } else if (isAlphaLower(charAt)) {
                    sb2.append((char) 27);
                    i4 = 1;
                } else if (isMixed(charAt)) {
                    sb2.append((char) 28);
                    i4 = 2;
                } else {
                    sb2.append((char) 29);
                    sb2.append((char) PUNCTUATION[charAt]);
                }
                i5++;
                if (i5 < i2) {
                }
            }
        }
        int length = sb2.length();
        char c = 0;
        for (int i8 = 0; i8 < length; i8++) {
            if (i8 % 2 != 0) {
                c = (char) ((c * 30) + sb2.charAt(i8));
                sb.append(c);
            } else {
                c = sb2.charAt(i8);
            }
        }
        if (length % 2 != 0) {
            sb.append((char) ((c * 30) + 29));
        }
        return i4;
    }

    private static void encodeBinary(byte[] bArr, int i, int i2, int i3, StringBuilder sb) {
        int i4;
        if (i2 == 1 && i3 == 0) {
            sb.append((char) 913);
        }
        if (i2 >= 6) {
            sb.append((char) 924);
            char[] cArr = new char[5];
            i4 = i;
            while ((i + i2) - i4 >= 6) {
                long j = 0;
                for (int i5 = 0; i5 < 6; i5++) {
                    j = (j << 8) + (bArr[i4 + i5] & 255);
                }
                for (int i6 = 0; i6 < 5; i6++) {
                    cArr[i6] = (char) (j % 900);
                    j /= 900;
                }
                for (int i7 = 4; i7 >= 0; i7--) {
                    sb.append(cArr[i7]);
                }
                i4 += 6;
            }
        } else {
            i4 = i;
        }
        int i8 = i + i2;
        if (i4 < i8) {
            sb.append((char) 901);
        }
        while (i4 < i8) {
            sb.append((char) (bArr[i4] & 255));
            i4++;
        }
    }

    private static void encodeNumeric(String str, int i, int i2, StringBuilder sb) {
        StringBuilder sb2 = new StringBuilder((i2 / 3) + 1);
        BigInteger valueOf = BigInteger.valueOf(900L);
        BigInteger valueOf2 = BigInteger.valueOf(0L);
        int i3 = 0;
        while (i3 < i2 - 1) {
            sb2.setLength(0);
            int min = Math.min(44, i2 - i3);
            StringBuilder sb3 = new StringBuilder();
            sb3.append('1');
            int i4 = i + i3;
            sb3.append(str.substring(i4, i4 + min));
            BigInteger bigInteger = new BigInteger(sb3.toString());
            do {
                sb2.append((char) bigInteger.mod(valueOf).intValue());
                bigInteger = bigInteger.divide(valueOf);
            } while (!bigInteger.equals(valueOf2));
            for (int length = sb2.length() - 1; length >= 0; length--) {
                sb.append(sb2.charAt(length));
            }
            i3 += min;
        }
    }

    private static boolean isMixed(char c) {
        return MIXED[c] != -1;
    }

    private static boolean isPunctuation(char c) {
        return PUNCTUATION[c] != -1;
    }

    private static int determineConsecutiveDigitCount(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = 0;
        if (i < length) {
            char charAt = charSequence.charAt(i);
            while (isDigit(charAt) && i < length) {
                i2++;
                i++;
                if (i < length) {
                    charAt = charSequence.charAt(i);
                }
            }
        }
        return i2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0027, code lost:
        return (r1 - r7) - r3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int determineConsecutiveTextCount(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = i;
        while (i2 < length) {
            char charAt = charSequence.charAt(i2);
            int i3 = 0;
            while (i3 < 13 && isDigit(charAt) && i2 < length) {
                i3++;
                i2++;
                if (i2 < length) {
                    charAt = charSequence.charAt(i2);
                }
            }
            if (i3 <= 0) {
                if (!isText(charSequence.charAt(i2))) {
                    break;
                }
                i2++;
            }
        }
        return i2 - i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x003f, code lost:
        return r1 - r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0026, code lost:
        return r1 - r9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int determineConsecutiveBinaryCount(CharSequence charSequence, byte[] bArr, int i) throws WriterException {
        int i2;
        int i3;
        int length = charSequence.length();
        int i4 = i;
        while (i4 < length) {
            char charAt = charSequence.charAt(i4);
            int i5 = 0;
            int i6 = 0;
            while (i6 < 13 && isDigit(charAt) && (i3 = i4 + (i6 = i6 + 1)) < length) {
                charAt = charSequence.charAt(i3);
            }
            while (i5 < 5 && isText(charAt) && (i2 = i4 + (i5 = i5 + 1)) < length) {
                charAt = charSequence.charAt(i2);
            }
            char charAt2 = charSequence.charAt(i4);
            if (bArr[i4] == 63 && charAt2 != '?') {
                throw new WriterException("Non-encodable character detected: " + charAt2 + " (Unicode: " + ((int) charAt2) + ')');
            }
            i4++;
        }
        return i4 - i;
    }
}

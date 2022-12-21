package com.google.zxing.maxicode.decoder;

import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import com.google.zxing.FormatException;
import com.google.zxing.common.DecoderResult;
import java.text.DecimalFormat;
import java.util.List;

final class DecodedBitStreamParser {
    private static final byte[] COUNTRY_BYTES = {53, 54, 43, 44, 45, 46, 47, 48, 37, 38};
    private static final char ECI = '￺';

    /* renamed from: FS */
    private static final char f468FS = '\u001c';

    /* renamed from: GS */
    private static final char f469GS = '\u001d';
    private static final char LATCHA = '￷';
    private static final char LATCHB = '￸';
    private static final char LOCK = '￹';

    /* renamed from: NS */
    private static final char f470NS = '￻';
    private static final char PAD = '￼';
    private static final byte[] POSTCODE_2_BYTES = {33, 34, 35, 36, 25, 26, 27, 28, 29, 30, 19, 20, 21, 22, 23, 24, 13, 14, 15, 16, 17, 18, 7, 8, 9, 10, 11, 12, 1, 2};
    private static final byte[] POSTCODE_2_LENGTH_BYTES = {39, 40, 41, 42, 31, NetworkStackConstants.TCPHDR_URG};
    private static final byte[][] POSTCODE_3_BYTES = {new byte[]{39, 40, 41, 42, 31, NetworkStackConstants.TCPHDR_URG}, new byte[]{33, 34, 35, 36, 25, 26}, new byte[]{27, 28, 29, 30, 19, 20}, new byte[]{21, 22, 23, 24, 13, 14}, new byte[]{15, 16, 17, 18, 7, 8}, new byte[]{9, 10, 11, 12, 1, 2}};

    /* renamed from: RS */
    private static final char f471RS = '\u001e';
    private static final byte[] SERVICE_CLASS_BYTES = {55, 56, 57, 58, 59, 60, 49, 50, 51, 52};
    private static final String[] SETS = {"\nABCDEFGHIJKLMNOPQRSTUVWXYZ￺\u001c\u001d\u001e￻ ￼\"#$%&'()*+,-./0123456789:￱￲￳￴￸", "`abcdefghijklmnopqrstuvwxyz￺\u001c\u001d\u001e￻{￼}~;<=>?[\\]^_ ,./:@!|￼￵￶￼￰￲￳￴￷", "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚ￺\u001c\u001d\u001e￻ÛÜÝÞßª¬±²³µ¹º¼½¾￷ ￹￳￴￸", "àáâãäåæçèéêëìíîïðñòóôõö÷øùú￺\u001c\u001d\u001e￻ûüýþÿ¡¨«¯°´·¸»¿￷ ￲￹￴￸", "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a￺￼￼\u001b￻\u001c\u001d\u001e\u001f ¢£¤¥¦§©­®¶￷ ￲￳￹￸"};
    private static final char SHIFTA = '￰';
    private static final char SHIFTB = '￱';
    private static final char SHIFTC = '￲';
    private static final char SHIFTD = '￳';
    private static final char SHIFTE = '￴';
    private static final char THREESHIFTA = '￶';
    private static final char TWOSHIFTA = '￵';

    private DecodedBitStreamParser() {
    }

    static DecoderResult decode(byte[] bArr, int i) throws FormatException {
        String str;
        StringBuilder sb = new StringBuilder(144);
        if (i == 2 || i == 3) {
            if (i == 2) {
                int postCode2 = getPostCode2(bArr);
                int postCode2Length = getPostCode2Length(bArr);
                if (postCode2Length <= 10) {
                    str = new DecimalFormat("0000000000".substring(0, postCode2Length)).format((long) postCode2);
                } else {
                    throw FormatException.getFormatInstance();
                }
            } else {
                str = getPostCode3(bArr);
            }
            DecimalFormat decimalFormat = new DecimalFormat("000");
            String format = decimalFormat.format((long) getCountry(bArr));
            String format2 = decimalFormat.format((long) getServiceClass(bArr));
            sb.append(getMessage(bArr, 10, 84));
            if (sb.toString().startsWith("[)>\u001e01\u001d")) {
                sb.insert(9, str + f469GS + format + f469GS + format2 + f469GS);
            } else {
                sb.insert(0, str + f469GS + format + f469GS + format2 + f469GS);
            }
        } else if (i == 4) {
            sb.append(getMessage(bArr, 1, 93));
        } else if (i == 5) {
            sb.append(getMessage(bArr, 1, 77));
        }
        return new DecoderResult(bArr, sb.toString(), (List<byte[]>) null, String.valueOf(i));
    }

    private static int getBit(int i, byte[] bArr) {
        int i2 = i - 1;
        return ((1 << (5 - (i2 % 6))) & bArr[i2 / 6]) == 0 ? 0 : 1;
    }

    private static int getInt(byte[] bArr, byte[] bArr2) {
        int i = 0;
        for (int i2 = 0; i2 < bArr2.length; i2++) {
            i += getBit(bArr2[i2], bArr) << ((bArr2.length - i2) - 1);
        }
        return i;
    }

    private static int getCountry(byte[] bArr) {
        return getInt(bArr, COUNTRY_BYTES);
    }

    private static int getServiceClass(byte[] bArr) {
        return getInt(bArr, SERVICE_CLASS_BYTES);
    }

    private static int getPostCode2Length(byte[] bArr) {
        return getInt(bArr, POSTCODE_2_LENGTH_BYTES);
    }

    private static int getPostCode2(byte[] bArr) {
        return getInt(bArr, POSTCODE_2_BYTES);
    }

    private static String getPostCode3(byte[] bArr) {
        byte[][] bArr2 = POSTCODE_3_BYTES;
        StringBuilder sb = new StringBuilder(bArr2.length);
        for (byte[] bArr3 : bArr2) {
            sb.append(SETS[0].charAt(getInt(bArr, bArr3)));
        }
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0057, code lost:
        r6 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0058, code lost:
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0061, code lost:
        r7 = r5 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0063, code lost:
        if (r5 != 0) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0065, code lost:
        r4 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0066, code lost:
        r3 = r3 + 1;
        r5 = r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getMessage(byte[] r12, int r13, int r14) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r1 = -1
            r2 = 0
            r3 = r13
            r5 = r1
            r4 = r2
            r6 = r4
        L_0x000b:
            int r7 = r13 + r14
            r8 = 1
            if (r3 >= r7) goto L_0x0069
            java.lang.String[] r7 = SETS
            r7 = r7[r4]
            byte r9 = r12[r3]
            char r7 = r7.charAt(r9)
            switch(r7) {
                case 65520: goto L_0x005a;
                case 65521: goto L_0x005a;
                case 65522: goto L_0x005a;
                case 65523: goto L_0x005a;
                case 65524: goto L_0x005a;
                case 65525: goto L_0x0056;
                case 65526: goto L_0x0054;
                case 65527: goto L_0x0052;
                case 65528: goto L_0x004f;
                case 65529: goto L_0x004d;
                case 65530: goto L_0x001d;
                case 65531: goto L_0x0021;
                default: goto L_0x001d;
            }
        L_0x001d:
            r0.append((char) r7)
            goto L_0x0061
        L_0x0021:
            int r3 = r3 + 1
            byte r7 = r12[r3]
            int r7 = r7 << 24
            int r3 = r3 + r8
            byte r9 = r12[r3]
            int r9 = r9 << 18
            int r7 = r7 + r9
            int r3 = r3 + r8
            byte r9 = r12[r3]
            int r9 = r9 << 12
            int r7 = r7 + r9
            int r3 = r3 + r8
            byte r9 = r12[r3]
            int r9 = r9 << 6
            int r7 = r7 + r9
            int r3 = r3 + r8
            byte r9 = r12[r3]
            int r7 = r7 + r9
            java.text.DecimalFormat r9 = new java.text.DecimalFormat
            java.lang.String r10 = "000000000"
            r9.<init>(r10)
            long r10 = (long) r7
            java.lang.String r7 = r9.format((long) r10)
            r0.append((java.lang.String) r7)
            goto L_0x0061
        L_0x004d:
            r5 = r1
            goto L_0x0061
        L_0x004f:
            r5 = r1
            r4 = r8
            goto L_0x0061
        L_0x0052:
            r5 = r1
            goto L_0x0058
        L_0x0054:
            r5 = 3
            goto L_0x0057
        L_0x0056:
            r5 = 2
        L_0x0057:
            r6 = r4
        L_0x0058:
            r4 = r2
            goto L_0x0061
        L_0x005a:
            r5 = 65520(0xfff0, float:9.1813E-41)
            int r7 = r7 - r5
            r6 = r4
            r4 = r7
            r5 = r8
        L_0x0061:
            int r7 = r5 + -1
            if (r5 != 0) goto L_0x0066
            r4 = r6
        L_0x0066:
            int r3 = r3 + r8
            r5 = r7
            goto L_0x000b
        L_0x0069:
            int r12 = r0.length()
            if (r12 <= 0) goto L_0x0086
            int r12 = r0.length()
            int r12 = r12 - r8
            char r12 = r0.charAt(r12)
            r13 = 65532(0xfffc, float:9.183E-41)
            if (r12 != r13) goto L_0x0086
            int r12 = r0.length()
            int r12 = r12 - r8
            r0.setLength(r12)
            goto L_0x0069
        L_0x0086:
            java.lang.String r12 = r0.toString()
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.maxicode.decoder.DecodedBitStreamParser.getMessage(byte[], int, int):java.lang.String");
    }
}

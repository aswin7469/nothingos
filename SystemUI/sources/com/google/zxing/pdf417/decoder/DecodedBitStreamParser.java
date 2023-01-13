package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.ECIStringBuilder;
import com.google.zxing.pdf417.PDF417ResultMetadata;
import java.math.BigInteger;
import java.util.Arrays;

final class DecodedBitStreamParser {

    /* renamed from: AL */
    private static final int f486AL = 28;

    /* renamed from: AS */
    private static final int f487AS = 27;
    private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
    private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
    private static final int BYTE_COMPACTION_MODE_LATCH = 901;
    private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
    private static final int ECI_CHARSET = 927;
    private static final int ECI_GENERAL_PURPOSE = 926;
    private static final int ECI_USER_DEFINED = 925;
    private static final BigInteger[] EXP900;

    /* renamed from: LL */
    private static final int f488LL = 27;
    private static final int MACRO_PDF417_OPTIONAL_FIELD_ADDRESSEE = 4;
    private static final int MACRO_PDF417_OPTIONAL_FIELD_CHECKSUM = 6;
    private static final int MACRO_PDF417_OPTIONAL_FIELD_FILE_NAME = 0;
    private static final int MACRO_PDF417_OPTIONAL_FIELD_FILE_SIZE = 5;
    private static final int MACRO_PDF417_OPTIONAL_FIELD_SEGMENT_COUNT = 1;
    private static final int MACRO_PDF417_OPTIONAL_FIELD_SENDER = 3;
    private static final int MACRO_PDF417_OPTIONAL_FIELD_TIME_STAMP = 2;
    private static final int MACRO_PDF417_TERMINATOR = 922;
    private static final int MAX_NUMERIC_CODEWORDS = 15;
    private static final char[] MIXED_CHARS = "0123456789&\r\t,:#-.$/+%*=^".toCharArray();

    /* renamed from: ML */
    private static final int f489ML = 28;
    private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
    private static final int NUMBER_OF_SEQUENCE_CODEWORDS = 2;
    private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
    private static final int PAL = 29;

    /* renamed from: PL */
    private static final int f490PL = 25;

    /* renamed from: PS */
    private static final int f491PS = 29;
    private static final char[] PUNCT_CHARS = ";<>@[\\]_`~!\r\t,:\n-.$/\"|*()?{}'".toCharArray();
    private static final int TEXT_COMPACTION_MODE_LATCH = 900;

    private enum Mode {
        ALPHA,
        LOWER,
        MIXED,
        PUNCT,
        ALPHA_SHIFT,
        PUNCT_SHIFT
    }

    static {
        BigInteger[] bigIntegerArr = new BigInteger[16];
        EXP900 = bigIntegerArr;
        bigIntegerArr[0] = BigInteger.ONE;
        BigInteger valueOf = BigInteger.valueOf(900);
        bigIntegerArr[1] = valueOf;
        int i = 2;
        while (true) {
            BigInteger[] bigIntegerArr2 = EXP900;
            if (i < bigIntegerArr2.length) {
                bigIntegerArr2[i] = bigIntegerArr2[i - 1].multiply(valueOf);
                i++;
            } else {
                return;
            }
        }
    }

    private DecodedBitStreamParser() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003e, code lost:
        r1 = r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.google.zxing.common.DecoderResult decode(int[] r5, java.lang.String r6) throws com.google.zxing.FormatException {
        /*
            com.google.zxing.common.ECIStringBuilder r0 = new com.google.zxing.common.ECIStringBuilder
            int r1 = r5.length
            int r1 = r1 * 2
            r0.<init>(r1)
            r1 = 1
            int r1 = textCompaction(r5, r1, r0)
            com.google.zxing.pdf417.PDF417ResultMetadata r2 = new com.google.zxing.pdf417.PDF417ResultMetadata
            r2.<init>()
        L_0x0012:
            r3 = 0
            r3 = r5[r3]
            if (r1 >= r3) goto L_0x005d
            int r3 = r1 + 1
            r1 = r5[r1]
            r4 = 913(0x391, float:1.28E-42)
            if (r1 == r4) goto L_0x0054
            switch(r1) {
                case 900: goto L_0x004f;
                case 901: goto L_0x004a;
                case 902: goto L_0x0045;
                default: goto L_0x0022;
            }
        L_0x0022:
            switch(r1) {
                case 922: goto L_0x0040;
                case 923: goto L_0x0040;
                case 924: goto L_0x004a;
                case 925: goto L_0x003c;
                case 926: goto L_0x0039;
                case 927: goto L_0x0031;
                case 928: goto L_0x002c;
                default: goto L_0x0025;
            }
        L_0x0025:
            int r3 = r3 + -1
            int r1 = textCompaction(r5, r3, r0)
            goto L_0x0012
        L_0x002c:
            int r1 = decodeMacroBlock(r5, r3, r2)
            goto L_0x0012
        L_0x0031:
            int r1 = r3 + 1
            r3 = r5[r3]
            r0.appendECI(r3)
            goto L_0x0012
        L_0x0039:
            int r3 = r3 + 2
            goto L_0x003e
        L_0x003c:
            int r3 = r3 + 1
        L_0x003e:
            r1 = r3
            goto L_0x0012
        L_0x0040:
            com.google.zxing.FormatException r5 = com.google.zxing.FormatException.getFormatInstance()
            throw r5
        L_0x0045:
            int r1 = numericCompaction(r5, r3, r0)
            goto L_0x0012
        L_0x004a:
            int r1 = byteCompaction(r1, r5, r3, r0)
            goto L_0x0012
        L_0x004f:
            int r1 = textCompaction(r5, r3, r0)
            goto L_0x0012
        L_0x0054:
            int r1 = r3 + 1
            r3 = r5[r3]
            char r3 = (char) r3
            r0.append((char) r3)
            goto L_0x0012
        L_0x005d:
            boolean r5 = r0.isEmpty()
            if (r5 == 0) goto L_0x006f
            java.lang.String r5 = r2.getFileId()
            if (r5 == 0) goto L_0x006a
            goto L_0x006f
        L_0x006a:
            com.google.zxing.FormatException r5 = com.google.zxing.FormatException.getFormatInstance()
            throw r5
        L_0x006f:
            com.google.zxing.common.DecoderResult r5 = new com.google.zxing.common.DecoderResult
            java.lang.String r0 = r0.toString()
            r1 = 0
            r5.<init>(r1, r0, r1, r6)
            r5.setOther(r2)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.decode(int[], java.lang.String):com.google.zxing.common.DecoderResult");
    }

    static int decodeMacroBlock(int[] iArr, int i, PDF417ResultMetadata pDF417ResultMetadata) throws FormatException {
        int i2;
        if (i + 2 <= iArr[0]) {
            int[] iArr2 = new int[2];
            int i3 = 0;
            while (i3 < 2) {
                iArr2[i3] = iArr[i];
                i3++;
                i++;
            }
            String decodeBase900toBase10 = decodeBase900toBase10(iArr2, 2);
            if (decodeBase900toBase10.isEmpty()) {
                pDF417ResultMetadata.setSegmentIndex(0);
            } else {
                try {
                    pDF417ResultMetadata.setSegmentIndex(Integer.parseInt(decodeBase900toBase10));
                } catch (NumberFormatException unused) {
                    throw FormatException.getFormatInstance();
                }
            }
            StringBuilder sb = new StringBuilder();
            while (r9 < iArr[0] && r9 < iArr.length && (i2 = iArr[r9]) != MACRO_PDF417_TERMINATOR && i2 != BEGIN_MACRO_PDF417_OPTIONAL_FIELD) {
                sb.append(String.format("%03d", Integer.valueOf(i2)));
                i = r9 + 1;
            }
            if (sb.length() != 0) {
                pDF417ResultMetadata.setFileId(sb.toString());
                int i4 = iArr[r9] == BEGIN_MACRO_PDF417_OPTIONAL_FIELD ? r9 + 1 : -1;
                while (r9 < iArr[0]) {
                    int i5 = iArr[r9];
                    if (i5 == MACRO_PDF417_TERMINATOR) {
                        r9++;
                        pDF417ResultMetadata.setLastSegment(true);
                    } else if (i5 == BEGIN_MACRO_PDF417_OPTIONAL_FIELD) {
                        int i6 = r9 + 1;
                        switch (iArr[i6]) {
                            case 0:
                                ECIStringBuilder eCIStringBuilder = new ECIStringBuilder();
                                r9 = textCompaction(iArr, i6 + 1, eCIStringBuilder);
                                pDF417ResultMetadata.setFileName(eCIStringBuilder.toString());
                                break;
                            case 1:
                                ECIStringBuilder eCIStringBuilder2 = new ECIStringBuilder();
                                r9 = numericCompaction(iArr, i6 + 1, eCIStringBuilder2);
                                pDF417ResultMetadata.setSegmentCount(Integer.parseInt(eCIStringBuilder2.toString()));
                                break;
                            case 2:
                                ECIStringBuilder eCIStringBuilder3 = new ECIStringBuilder();
                                r9 = numericCompaction(iArr, i6 + 1, eCIStringBuilder3);
                                pDF417ResultMetadata.setTimestamp(Long.parseLong(eCIStringBuilder3.toString()));
                                break;
                            case 3:
                                ECIStringBuilder eCIStringBuilder4 = new ECIStringBuilder();
                                r9 = textCompaction(iArr, i6 + 1, eCIStringBuilder4);
                                pDF417ResultMetadata.setSender(eCIStringBuilder4.toString());
                                break;
                            case 4:
                                ECIStringBuilder eCIStringBuilder5 = new ECIStringBuilder();
                                r9 = textCompaction(iArr, i6 + 1, eCIStringBuilder5);
                                pDF417ResultMetadata.setAddressee(eCIStringBuilder5.toString());
                                break;
                            case 5:
                                ECIStringBuilder eCIStringBuilder6 = new ECIStringBuilder();
                                r9 = numericCompaction(iArr, i6 + 1, eCIStringBuilder6);
                                pDF417ResultMetadata.setFileSize(Long.parseLong(eCIStringBuilder6.toString()));
                                break;
                            case 6:
                                ECIStringBuilder eCIStringBuilder7 = new ECIStringBuilder();
                                r9 = numericCompaction(iArr, i6 + 1, eCIStringBuilder7);
                                pDF417ResultMetadata.setChecksum(Integer.parseInt(eCIStringBuilder7.toString()));
                                break;
                            default:
                                throw FormatException.getFormatInstance();
                        }
                    } else {
                        throw FormatException.getFormatInstance();
                    }
                }
                if (i4 != -1) {
                    int i7 = r9 - i4;
                    if (pDF417ResultMetadata.isLastSegment()) {
                        i7--;
                    }
                    pDF417ResultMetadata.setOptionalData(Arrays.copyOfRange(iArr, i4, i7 + i4));
                }
                return r9;
            }
            throw FormatException.getFormatInstance();
        }
        throw FormatException.getFormatInstance();
    }

    private static int textCompaction(int[] iArr, int i, ECIStringBuilder eCIStringBuilder) throws FormatException {
        int i2 = iArr[0];
        int[] iArr2 = new int[((i2 - i) * 2)];
        int[] iArr3 = new int[((i2 - i) * 2)];
        Mode mode = Mode.ALPHA;
        boolean z = false;
        int i3 = 0;
        while (i < iArr[0] && !z) {
            int i4 = i + 1;
            int i5 = iArr[i];
            if (i5 < TEXT_COMPACTION_MODE_LATCH) {
                iArr2[i3] = i5 / 30;
                iArr2[i3 + 1] = i5 % 30;
                i3 += 2;
            } else if (i5 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                iArr2[i3] = MODE_SHIFT_TO_BYTE_COMPACTION_MODE;
                i = i4 + 1;
                iArr3[i3] = iArr[i4];
                i3++;
            } else if (i5 != ECI_CHARSET) {
                if (i5 != 928) {
                    switch (i5) {
                        case TEXT_COMPACTION_MODE_LATCH /*900*/:
                            iArr2[i3] = TEXT_COMPACTION_MODE_LATCH;
                            i3++;
                            break;
                        case BYTE_COMPACTION_MODE_LATCH /*901*/:
                        case NUMERIC_COMPACTION_MODE_LATCH /*902*/:
                            break;
                        default:
                            switch (i5) {
                                case MACRO_PDF417_TERMINATOR /*922*/:
                                case BEGIN_MACRO_PDF417_OPTIONAL_FIELD /*923*/:
                                case BYTE_COMPACTION_MODE_LATCH_6 /*924*/:
                                    break;
                            }
                    }
                }
                i4--;
                z = true;
            } else {
                Mode decodeTextCompaction = decodeTextCompaction(iArr2, iArr3, i3, eCIStringBuilder, mode);
                int i6 = i4 + 1;
                eCIStringBuilder.appendECI(iArr[i4]);
                int i7 = iArr[0];
                i3 = 0;
                int[] iArr4 = new int[((i7 - i6) * 2)];
                mode = decodeTextCompaction;
                i = i6;
                iArr3 = new int[((i7 - i6) * 2)];
                iArr2 = iArr4;
            }
            i = i4;
            continue;
        }
        decodeTextCompaction(iArr2, iArr3, i3, eCIStringBuilder, mode);
        return i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0046, code lost:
        r7 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0023, code lost:
        r7 = r4;
        r4 = r3;
        r3 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00ba, code lost:
        r7 = (char) r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00bb, code lost:
        r14 = r4;
        r4 = r3;
        r3 = r7;
        r7 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00ca, code lost:
        r9 = 0;
        r14 = r4;
        r4 = r3;
        r3 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00d4, code lost:
        r9 = 0;
        r3 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00dd, code lost:
        r9 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00e2, code lost:
        r7 = r4;
        r4 = r3;
        r3 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00e5, code lost:
        if (r3 == 0) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00e7, code lost:
        r0.append(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00ea, code lost:
        r6 = r6 + 1;
        r3 = r4;
        r4 = r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode decodeTextCompaction(int[] r15, int[] r16, int r17, com.google.zxing.common.ECIStringBuilder r18, com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode r19) {
        /*
            r0 = r18
            r1 = 0
            r5 = r17
            r2 = r19
            r3 = r2
            r4 = r3
            r6 = r1
        L_0x000a:
            if (r6 >= r5) goto L_0x00f0
            r7 = r15[r6]
            int[] r8 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.C41101.f492x45bba1d
            int r9 = r3.ordinal()
            r8 = r8[r9]
            r9 = 32
            r10 = 29
            r11 = 26
            r12 = 913(0x391, float:1.28E-42)
            r13 = 900(0x384, float:1.261E-42)
            switch(r8) {
                case 1: goto L_0x00b6;
                case 2: goto L_0x0096;
                case 3: goto L_0x006f;
                case 4: goto L_0x0055;
                case 5: goto L_0x0041;
                case 6: goto L_0x0028;
                default: goto L_0x0023;
            }
        L_0x0023:
            r7 = r4
            r4 = r3
            r3 = r1
            goto L_0x00e5
        L_0x0028:
            if (r7 >= r10) goto L_0x002f
            char[] r3 = PUNCT_CHARS
            char r3 = r3[r7]
            goto L_0x0046
        L_0x002f:
            if (r7 == r10) goto L_0x003e
            if (r7 == r13) goto L_0x003e
            if (r7 == r12) goto L_0x0036
            goto L_0x003c
        L_0x0036:
            r3 = r16[r6]
            char r3 = (char) r3
            r0.append((char) r3)
        L_0x003c:
            r3 = r1
            goto L_0x0046
        L_0x003e:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0023
        L_0x0041:
            if (r7 >= r11) goto L_0x0049
            int r7 = r7 + 65
            char r3 = (char) r7
        L_0x0046:
            r7 = r4
            goto L_0x00e5
        L_0x0049:
            if (r7 == r11) goto L_0x004e
            if (r7 == r13) goto L_0x0051
            r9 = r1
        L_0x004e:
            r3 = r4
            goto L_0x00e2
        L_0x0051:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00dd
        L_0x0055:
            if (r7 >= r10) goto L_0x005d
            char[] r8 = PUNCT_CHARS
            char r7 = r8[r7]
            goto L_0x00bb
        L_0x005d:
            if (r7 == r10) goto L_0x006b
            if (r7 == r13) goto L_0x006b
            if (r7 == r12) goto L_0x0064
            goto L_0x0023
        L_0x0064:
            r7 = r16[r6]
            char r7 = (char) r7
            r0.append((char) r7)
            goto L_0x0023
        L_0x006b:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r2 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            r3 = r2
            goto L_0x0023
        L_0x006f:
            r8 = 25
            if (r7 >= r8) goto L_0x0078
            char[] r8 = MIXED_CHARS
            char r7 = r8[r7]
            goto L_0x00bb
        L_0x0078:
            if (r7 == r13) goto L_0x0093
            if (r7 == r12) goto L_0x008b
            switch(r7) {
                case 25: goto L_0x0088;
                case 26: goto L_0x00e2;
                case 27: goto L_0x0084;
                case 28: goto L_0x0093;
                case 29: goto L_0x0081;
                default: goto L_0x007f;
            }
        L_0x007f:
            goto L_0x00dd
        L_0x0081:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r4 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT
            goto L_0x00ca
        L_0x0084:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r2 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.LOWER
            goto L_0x00d4
        L_0x0088:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r2 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT
            goto L_0x00d4
        L_0x008b:
            r7 = r16[r6]
            char r7 = (char) r7
            r0.append((char) r7)
            goto L_0x00dd
        L_0x0093:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r2 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00d4
        L_0x0096:
            if (r7 >= r11) goto L_0x009b
            int r7 = r7 + 97
            goto L_0x00ba
        L_0x009b:
            if (r7 == r13) goto L_0x00b3
            if (r7 == r12) goto L_0x00ac
            switch(r7) {
                case 26: goto L_0x00e2;
                case 27: goto L_0x00a9;
                case 28: goto L_0x00a6;
                case 29: goto L_0x00a3;
                default: goto L_0x00a2;
            }
        L_0x00a2:
            goto L_0x00dd
        L_0x00a3:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r4 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT
            goto L_0x00ca
        L_0x00a6:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r2 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.MIXED
            goto L_0x00d4
        L_0x00a9:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r4 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA_SHIFT
            goto L_0x00ca
        L_0x00ac:
            r7 = r16[r6]
            char r7 = (char) r7
            r0.append((char) r7)
            goto L_0x00dd
        L_0x00b3:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r2 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00d4
        L_0x00b6:
            if (r7 >= r11) goto L_0x00c0
            int r7 = r7 + 65
        L_0x00ba:
            char r7 = (char) r7
        L_0x00bb:
            r14 = r4
            r4 = r3
            r3 = r7
            r7 = r14
            goto L_0x00e5
        L_0x00c0:
            if (r7 == r13) goto L_0x00df
            if (r7 == r12) goto L_0x00d7
            switch(r7) {
                case 26: goto L_0x00e2;
                case 27: goto L_0x00d2;
                case 28: goto L_0x00cf;
                case 29: goto L_0x00c8;
                default: goto L_0x00c7;
            }
        L_0x00c7:
            goto L_0x00dd
        L_0x00c8:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r4 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT
        L_0x00ca:
            r9 = r1
            r14 = r4
            r4 = r3
            r3 = r14
            goto L_0x00e2
        L_0x00cf:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r2 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.MIXED
            goto L_0x00d4
        L_0x00d2:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r2 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.LOWER
        L_0x00d4:
            r9 = r1
            r3 = r2
            goto L_0x00e2
        L_0x00d7:
            r7 = r16[r6]
            char r7 = (char) r7
            r0.append((char) r7)
        L_0x00dd:
            r9 = r1
            goto L_0x00e2
        L_0x00df:
            com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r2 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x00d4
        L_0x00e2:
            r7 = r4
            r4 = r3
            r3 = r9
        L_0x00e5:
            if (r3 == 0) goto L_0x00ea
            r0.append((char) r3)
        L_0x00ea:
            int r6 = r6 + 1
            r3 = r4
            r4 = r7
            goto L_0x000a
        L_0x00f0:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.decodeTextCompaction(int[], int[], int, com.google.zxing.common.ECIStringBuilder, com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode):com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode");
    }

    /* renamed from: com.google.zxing.pdf417.decoder.DecodedBitStreamParser$1 */
    static /* synthetic */ class C41101 {

        /* renamed from: $SwitchMap$com$google$zxing$pdf417$decoder$DecodedBitStreamParser$Mode */
        static final /* synthetic */ int[] f492x45bba1d;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode[] r0 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f492x45bba1d = r0
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f492x45bba1d     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.LOWER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f492x45bba1d     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.MIXED     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f492x45bba1d     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f492x45bba1d     // Catch:{ NoSuchFieldError -> 0x003e }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA_SHIFT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = f492x45bba1d     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.google.zxing.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.C41101.<clinit>():void");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0026  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x004f  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0087  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int byteCompaction(int r11, int[] r12, int r13, com.google.zxing.common.ECIStringBuilder r14) throws com.google.zxing.FormatException {
        /*
            r0 = 0
            r1 = r0
        L_0x0002:
            r2 = r12[r0]
            if (r13 >= r2) goto L_0x008a
            if (r1 != 0) goto L_0x008a
        L_0x0008:
            r2 = r12[r0]
            r3 = 927(0x39f, float:1.299E-42)
            r4 = 1
            if (r13 >= r2) goto L_0x001c
            r5 = r12[r13]
            if (r5 != r3) goto L_0x001c
            int r13 = r13 + 1
            r2 = r12[r13]
            r14.appendECI(r2)
            int r13 = r13 + r4
            goto L_0x0008
        L_0x001c:
            if (r13 >= r2) goto L_0x0087
            r2 = r12[r13]
            r5 = 900(0x384, float:1.261E-42)
            if (r2 < r5) goto L_0x0026
            goto L_0x0087
        L_0x0026:
            r6 = 0
            r2 = r0
        L_0x0029:
            r8 = 900(0x384, double:4.447E-321)
            long r6 = r6 * r8
            int r8 = r13 + 1
            r13 = r12[r13]
            long r9 = (long) r13
            long r6 = r6 + r9
            int r2 = r2 + r4
            r13 = 5
            if (r2 >= r13) goto L_0x0041
            r9 = r12[r0]
            if (r8 >= r9) goto L_0x0041
            r9 = r12[r8]
            if (r9 < r5) goto L_0x003f
            goto L_0x0041
        L_0x003f:
            r13 = r8
            goto L_0x0029
        L_0x0041:
            if (r2 != r13) goto L_0x0061
            r13 = 924(0x39c, float:1.295E-42)
            if (r11 == r13) goto L_0x004f
            r13 = r12[r0]
            if (r8 >= r13) goto L_0x0061
            r13 = r12[r8]
            if (r13 >= r5) goto L_0x0061
        L_0x004f:
            r13 = r0
        L_0x0050:
            r2 = 6
            if (r13 >= r2) goto L_0x0084
            int r2 = 5 - r13
            int r2 = r2 * 8
            long r2 = r6 >> r2
            int r2 = (int) r2
            byte r2 = (byte) r2
            r14.append((byte) r2)
            int r13 = r13 + 1
            goto L_0x0050
        L_0x0061:
            int r8 = r8 - r2
        L_0x0062:
            r13 = r12[r0]
            if (r8 >= r13) goto L_0x0084
            if (r1 != 0) goto L_0x0084
            int r13 = r8 + 1
            r2 = r12[r8]
            if (r2 >= r5) goto L_0x0074
            byte r2 = (byte) r2
            r14.append((byte) r2)
            r8 = r13
            goto L_0x0062
        L_0x0074:
            if (r2 != r3) goto L_0x007f
            int r2 = r13 + 1
            r13 = r12[r13]
            r14.appendECI(r13)
            r8 = r2
            goto L_0x0062
        L_0x007f:
            int r13 = r13 + -1
            r8 = r13
            r1 = r4
            goto L_0x0062
        L_0x0084:
            r13 = r8
            goto L_0x0002
        L_0x0087:
            r1 = r4
            goto L_0x0002
        L_0x008a:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.byteCompaction(int, int[], int, com.google.zxing.common.ECIStringBuilder):int");
    }

    private static int numericCompaction(int[] iArr, int i, ECIStringBuilder eCIStringBuilder) throws FormatException {
        int[] iArr2 = new int[15];
        boolean z = false;
        int i2 = 0;
        while (true) {
            int i3 = iArr[0];
            if (i >= i3 || z) {
                return i;
            }
            int i4 = i + 1;
            int i5 = iArr[i];
            if (i4 == i3) {
                z = true;
            }
            if (i5 < TEXT_COMPACTION_MODE_LATCH) {
                iArr2[i2] = i5;
                i2++;
            } else {
                if (!(i5 == TEXT_COMPACTION_MODE_LATCH || i5 == BYTE_COMPACTION_MODE_LATCH || i5 == ECI_CHARSET || i5 == 928)) {
                    switch (i5) {
                        case MACRO_PDF417_TERMINATOR /*922*/:
                        case BEGIN_MACRO_PDF417_OPTIONAL_FIELD /*923*/:
                        case BYTE_COMPACTION_MODE_LATCH_6 /*924*/:
                            break;
                    }
                }
                i4--;
                z = true;
            }
            if ((i2 % 15 == 0 || i5 == NUMERIC_COMPACTION_MODE_LATCH || z) && i2 > 0) {
                eCIStringBuilder.append(decodeBase900toBase10(iArr2, i2));
                i2 = 0;
            }
            i = i4;
        }
        return i;
    }

    private static String decodeBase900toBase10(int[] iArr, int i) throws FormatException {
        BigInteger bigInteger = BigInteger.ZERO;
        for (int i2 = 0; i2 < i; i2++) {
            bigInteger = bigInteger.add(EXP900[(i - i2) - 1].multiply(BigInteger.valueOf((long) iArr[i2])));
        }
        String bigInteger2 = bigInteger.toString();
        if (bigInteger2.charAt(0) == '1') {
            return bigInteger2.substring(1);
        }
        throw FormatException.getFormatInstance();
    }
}

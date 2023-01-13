package com.google.zxing.qrcode.encoder;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.StringUtils;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonEncoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public final class Encoder {
    private static final int[] ALPHANUMERIC_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1};
    static final Charset DEFAULT_BYTE_MODE_ENCODING = StandardCharsets.ISO_8859_1;

    private Encoder() {
    }

    private static int calculateMaskPenalty(ByteMatrix byteMatrix) {
        return MaskUtil.applyMaskPenaltyRule1(byteMatrix) + MaskUtil.applyMaskPenaltyRule2(byteMatrix) + MaskUtil.applyMaskPenaltyRule3(byteMatrix) + MaskUtil.applyMaskPenaltyRule4(byteMatrix);
    }

    public static QRCode encode(String str, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException {
        return encode(str, errorCorrectionLevel, (Map<EncodeHintType, ?>) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0145, code lost:
        if (com.google.zxing.qrcode.encoder.QRCode.isValidMaskPattern(r8) != false) goto L_0x0149;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.zxing.qrcode.encoder.QRCode encode(java.lang.String r6, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel r7, java.util.Map<com.google.zxing.EncodeHintType, ?> r8) throws com.google.zxing.WriterException {
        /*
            r0 = 1
            r1 = 0
            if (r8 == 0) goto L_0x001e
            com.google.zxing.EncodeHintType r2 = com.google.zxing.EncodeHintType.GS1_FORMAT
            boolean r2 = r8.containsKey(r2)
            if (r2 == 0) goto L_0x001e
            com.google.zxing.EncodeHintType r2 = com.google.zxing.EncodeHintType.GS1_FORMAT
            java.lang.Object r2 = r8.get(r2)
            java.lang.String r2 = r2.toString()
            boolean r2 = java.lang.Boolean.parseBoolean(r2)
            if (r2 == 0) goto L_0x001e
            r2 = r0
            goto L_0x001f
        L_0x001e:
            r2 = r1
        L_0x001f:
            if (r8 == 0) goto L_0x003b
            com.google.zxing.EncodeHintType r3 = com.google.zxing.EncodeHintType.QR_COMPACT
            boolean r3 = r8.containsKey(r3)
            if (r3 == 0) goto L_0x003b
            com.google.zxing.EncodeHintType r3 = com.google.zxing.EncodeHintType.QR_COMPACT
            java.lang.Object r3 = r8.get(r3)
            java.lang.String r3 = r3.toString()
            boolean r3 = java.lang.Boolean.parseBoolean(r3)
            if (r3 == 0) goto L_0x003b
            r3 = r0
            goto L_0x003c
        L_0x003b:
            r3 = r1
        L_0x003c:
            java.nio.charset.Charset r4 = DEFAULT_BYTE_MODE_ENCODING
            if (r8 == 0) goto L_0x0049
            com.google.zxing.EncodeHintType r5 = com.google.zxing.EncodeHintType.CHARACTER_SET
            boolean r5 = r8.containsKey(r5)
            if (r5 == 0) goto L_0x0049
            goto L_0x004a
        L_0x0049:
            r0 = r1
        L_0x004a:
            if (r0 == 0) goto L_0x005b
            com.google.zxing.EncodeHintType r1 = com.google.zxing.EncodeHintType.CHARACTER_SET
            java.lang.Object r1 = r8.get(r1)
            java.lang.String r1 = r1.toString()
            java.nio.charset.Charset r1 = java.nio.charset.Charset.forName(r1)
            goto L_0x005c
        L_0x005b:
            r1 = r4
        L_0x005c:
            if (r3 == 0) goto L_0x007a
            com.google.zxing.qrcode.decoder.Mode r0 = com.google.zxing.qrcode.decoder.Mode.BYTE
            boolean r3 = r1.equals(r4)
            r4 = 0
            if (r3 == 0) goto L_0x0068
            r1 = r4
        L_0x0068:
            com.google.zxing.qrcode.encoder.MinimalEncoder$ResultList r6 = com.google.zxing.qrcode.encoder.MinimalEncoder.encode(r6, r4, r1, r2, r7)
            com.google.zxing.common.BitArray r1 = new com.google.zxing.common.BitArray
            r1.<init>()
            r6.getBits(r1)
            com.google.zxing.qrcode.decoder.Version r6 = r6.getVersion()
            goto L_0x00f5
        L_0x007a:
            com.google.zxing.qrcode.decoder.Mode r3 = chooseMode(r6, r1)
            com.google.zxing.common.BitArray r4 = new com.google.zxing.common.BitArray
            r4.<init>()
            com.google.zxing.qrcode.decoder.Mode r5 = com.google.zxing.qrcode.decoder.Mode.BYTE
            if (r3 != r5) goto L_0x0092
            if (r0 == 0) goto L_0x0092
            com.google.zxing.common.CharacterSetECI r0 = com.google.zxing.common.CharacterSetECI.getCharacterSetECI(r1)
            if (r0 == 0) goto L_0x0092
            appendECI(r0, r4)
        L_0x0092:
            if (r2 == 0) goto L_0x0099
            com.google.zxing.qrcode.decoder.Mode r0 = com.google.zxing.qrcode.decoder.Mode.FNC1_FIRST_POSITION
            appendModeInfo(r0, r4)
        L_0x0099:
            appendModeInfo(r3, r4)
            com.google.zxing.common.BitArray r0 = new com.google.zxing.common.BitArray
            r0.<init>()
            appendBytes(r6, r3, r0, r1)
            if (r8 == 0) goto L_0x00d3
            com.google.zxing.EncodeHintType r1 = com.google.zxing.EncodeHintType.QR_VERSION
            boolean r1 = r8.containsKey(r1)
            if (r1 == 0) goto L_0x00d3
            com.google.zxing.EncodeHintType r1 = com.google.zxing.EncodeHintType.QR_VERSION
            java.lang.Object r1 = r8.get(r1)
            java.lang.String r1 = r1.toString()
            int r1 = java.lang.Integer.parseInt(r1)
            com.google.zxing.qrcode.decoder.Version r1 = com.google.zxing.qrcode.decoder.Version.getVersionForNumber(r1)
            int r2 = calculateBitsNeeded(r3, r4, r0, r1)
            boolean r2 = willFit(r2, r1, r7)
            if (r2 == 0) goto L_0x00cb
            goto L_0x00d7
        L_0x00cb:
            com.google.zxing.WriterException r6 = new com.google.zxing.WriterException
            java.lang.String r7 = "Data too big for requested version"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x00d3:
            com.google.zxing.qrcode.decoder.Version r1 = recommendVersion(r7, r3, r4, r0)
        L_0x00d7:
            com.google.zxing.common.BitArray r2 = new com.google.zxing.common.BitArray
            r2.<init>()
            r2.appendBitArray(r4)
            com.google.zxing.qrcode.decoder.Mode r4 = com.google.zxing.qrcode.decoder.Mode.BYTE
            if (r3 != r4) goto L_0x00e8
            int r6 = r0.getSizeInBytes()
            goto L_0x00ec
        L_0x00e8:
            int r6 = r6.length()
        L_0x00ec:
            appendLengthInfo(r6, r1, r3, r2)
            r2.appendBitArray(r0)
            r6 = r1
            r1 = r2
            r0 = r3
        L_0x00f5:
            com.google.zxing.qrcode.decoder.Version$ECBlocks r2 = r6.getECBlocksForLevel(r7)
            int r3 = r6.getTotalCodewords()
            int r4 = r2.getTotalECCodewords()
            int r3 = r3 - r4
            terminateBits(r3, r1)
            int r4 = r6.getTotalCodewords()
            int r2 = r2.getNumBlocks()
            com.google.zxing.common.BitArray r1 = interleaveWithECBytes(r1, r4, r3, r2)
            com.google.zxing.qrcode.encoder.QRCode r2 = new com.google.zxing.qrcode.encoder.QRCode
            r2.<init>()
            r2.setECLevel(r7)
            r2.setMode(r0)
            r2.setVersion(r6)
            int r0 = r6.getDimensionForVersion()
            com.google.zxing.qrcode.encoder.ByteMatrix r3 = new com.google.zxing.qrcode.encoder.ByteMatrix
            r3.<init>(r0, r0)
            r0 = -1
            if (r8 == 0) goto L_0x0148
            com.google.zxing.EncodeHintType r4 = com.google.zxing.EncodeHintType.QR_MASK_PATTERN
            boolean r4 = r8.containsKey(r4)
            if (r4 == 0) goto L_0x0148
            com.google.zxing.EncodeHintType r4 = com.google.zxing.EncodeHintType.QR_MASK_PATTERN
            java.lang.Object r8 = r8.get(r4)
            java.lang.String r8 = r8.toString()
            int r8 = java.lang.Integer.parseInt(r8)
            boolean r4 = com.google.zxing.qrcode.encoder.QRCode.isValidMaskPattern(r8)
            if (r4 == 0) goto L_0x0148
            goto L_0x0149
        L_0x0148:
            r8 = r0
        L_0x0149:
            if (r8 != r0) goto L_0x014f
            int r8 = chooseMaskPattern(r1, r7, r6, r3)
        L_0x014f:
            r2.setMaskPattern(r8)
            com.google.zxing.qrcode.encoder.MatrixUtil.buildMatrix(r1, r7, r6, r8, r3)
            r2.setMatrix(r3)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.encoder.Encoder.encode(java.lang.String, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel, java.util.Map):com.google.zxing.qrcode.encoder.QRCode");
    }

    private static Version recommendVersion(ErrorCorrectionLevel errorCorrectionLevel, Mode mode, BitArray bitArray, BitArray bitArray2) throws WriterException {
        return chooseVersion(calculateBitsNeeded(mode, bitArray, bitArray2, chooseVersion(calculateBitsNeeded(mode, bitArray, bitArray2, Version.getVersionForNumber(1)), errorCorrectionLevel)), errorCorrectionLevel);
    }

    private static int calculateBitsNeeded(Mode mode, BitArray bitArray, BitArray bitArray2, Version version) {
        return bitArray.getSize() + mode.getCharacterCountBits(version) + bitArray2.getSize();
    }

    static int getAlphanumericCode(int i) {
        int[] iArr = ALPHANUMERIC_TABLE;
        if (i < iArr.length) {
            return iArr[i];
        }
        return -1;
    }

    public static Mode chooseMode(String str) {
        return chooseMode(str, (Charset) null);
    }

    private static Mode chooseMode(String str, Charset charset) {
        if (StringUtils.SHIFT_JIS_CHARSET.equals(charset) && isOnlyDoubleByteKanji(str)) {
            return Mode.KANJI;
        }
        boolean z = false;
        boolean z2 = false;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                z2 = true;
            } else if (getAlphanumericCode(charAt) == -1) {
                return Mode.BYTE;
            } else {
                z = true;
            }
        }
        if (z) {
            return Mode.ALPHANUMERIC;
        }
        if (z2) {
            return Mode.NUMERIC;
        }
        return Mode.BYTE;
    }

    static boolean isOnlyDoubleByteKanji(String str) {
        byte[] bytes = str.getBytes(StringUtils.SHIFT_JIS_CHARSET);
        int length = bytes.length;
        if (length % 2 != 0) {
            return false;
        }
        for (int i = 0; i < length; i += 2) {
            byte b = bytes[i] & 255;
            if ((b < 129 || b > 159) && (b < 224 || b > 235)) {
                return false;
            }
        }
        return true;
    }

    private static int chooseMaskPattern(BitArray bitArray, ErrorCorrectionLevel errorCorrectionLevel, Version version, ByteMatrix byteMatrix) throws WriterException {
        int i = Integer.MAX_VALUE;
        int i2 = -1;
        for (int i3 = 0; i3 < 8; i3++) {
            MatrixUtil.buildMatrix(bitArray, errorCorrectionLevel, version, i3, byteMatrix);
            int calculateMaskPenalty = calculateMaskPenalty(byteMatrix);
            if (calculateMaskPenalty < i) {
                i2 = i3;
                i = calculateMaskPenalty;
            }
        }
        return i2;
    }

    private static Version chooseVersion(int i, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException {
        for (int i2 = 1; i2 <= 40; i2++) {
            Version versionForNumber = Version.getVersionForNumber(i2);
            if (willFit(i, versionForNumber, errorCorrectionLevel)) {
                return versionForNumber;
            }
        }
        throw new WriterException("Data too big");
    }

    static boolean willFit(int i, Version version, ErrorCorrectionLevel errorCorrectionLevel) {
        return version.getTotalCodewords() - version.getECBlocksForLevel(errorCorrectionLevel).getTotalECCodewords() >= (i + 7) / 8;
    }

    static void terminateBits(int i, BitArray bitArray) throws WriterException {
        int i2 = i * 8;
        if (bitArray.getSize() <= i2) {
            for (int i3 = 0; i3 < 4 && bitArray.getSize() < i2; i3++) {
                bitArray.appendBit(false);
            }
            int size = bitArray.getSize() & 7;
            if (size > 0) {
                while (size < 8) {
                    bitArray.appendBit(false);
                    size++;
                }
            }
            int sizeInBytes = i - bitArray.getSizeInBytes();
            for (int i4 = 0; i4 < sizeInBytes; i4++) {
                bitArray.appendBits((i4 & 1) == 0 ? 236 : 17, 8);
            }
            if (bitArray.getSize() != i2) {
                throw new WriterException("Bits size does not equal capacity");
            }
            return;
        }
        throw new WriterException("data bits cannot fit in the QR Code" + bitArray.getSize() + " > " + i2);
    }

    static void getNumDataBytesAndNumECBytesForBlockID(int i, int i2, int i3, int i4, int[] iArr, int[] iArr2) throws WriterException {
        if (i4 < i3) {
            int i5 = i % i3;
            int i6 = i3 - i5;
            int i7 = i / i3;
            int i8 = i7 + 1;
            int i9 = i2 / i3;
            int i10 = i9 + 1;
            int i11 = i7 - i9;
            int i12 = i8 - i10;
            if (i11 != i12) {
                throw new WriterException("EC bytes mismatch");
            } else if (i3 != i6 + i5) {
                throw new WriterException("RS blocks mismatch");
            } else if (i != ((i9 + i11) * i6) + ((i10 + i12) * i5)) {
                throw new WriterException("Total bytes mismatch");
            } else if (i4 < i6) {
                iArr[0] = i9;
                iArr2[0] = i11;
            } else {
                iArr[0] = i10;
                iArr2[0] = i12;
            }
        } else {
            throw new WriterException("Block ID too large");
        }
    }

    static BitArray interleaveWithECBytes(BitArray bitArray, int i, int i2, int i3) throws WriterException {
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        if (bitArray.getSizeInBytes() == i5) {
            ArrayList<BlockPair> arrayList = new ArrayList<>(i6);
            int i7 = 0;
            int i8 = 0;
            int i9 = 0;
            for (int i10 = 0; i10 < i6; i10++) {
                int[] iArr = new int[1];
                int[] iArr2 = new int[1];
                getNumDataBytesAndNumECBytesForBlockID(i, i2, i3, i10, iArr, iArr2);
                int i11 = iArr[0];
                byte[] bArr = new byte[i11];
                bitArray.toBytes(i7 * 8, bArr, 0, i11);
                byte[] generateECBytes = generateECBytes(bArr, iArr2[0]);
                arrayList.add(new BlockPair(bArr, generateECBytes));
                i8 = Math.max(i8, i11);
                i9 = Math.max(i9, generateECBytes.length);
                i7 += iArr[0];
            }
            if (i5 == i7) {
                BitArray bitArray2 = new BitArray();
                for (int i12 = 0; i12 < i8; i12++) {
                    for (BlockPair dataBytes : arrayList) {
                        byte[] dataBytes2 = dataBytes.getDataBytes();
                        if (i12 < dataBytes2.length) {
                            bitArray2.appendBits(dataBytes2[i12], 8);
                        }
                    }
                }
                for (int i13 = 0; i13 < i9; i13++) {
                    for (BlockPair errorCorrectionBytes : arrayList) {
                        byte[] errorCorrectionBytes2 = errorCorrectionBytes.getErrorCorrectionBytes();
                        if (i13 < errorCorrectionBytes2.length) {
                            bitArray2.appendBits(errorCorrectionBytes2[i13], 8);
                        }
                    }
                }
                if (i4 == bitArray2.getSizeInBytes()) {
                    return bitArray2;
                }
                throw new WriterException("Interleaving error: " + i4 + " and " + bitArray2.getSizeInBytes() + " differ.");
            }
            throw new WriterException("Data bytes does not match offset");
        }
        throw new WriterException("Number of bits and data bytes does not match");
    }

    static byte[] generateECBytes(byte[] bArr, int i) {
        int length = bArr.length;
        int[] iArr = new int[(length + i)];
        for (int i2 = 0; i2 < length; i2++) {
            iArr[i2] = bArr[i2] & 255;
        }
        new ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256).encode(iArr, i);
        byte[] bArr2 = new byte[i];
        for (int i3 = 0; i3 < i; i3++) {
            bArr2[i3] = (byte) iArr[length + i3];
        }
        return bArr2;
    }

    static void appendModeInfo(Mode mode, BitArray bitArray) {
        bitArray.appendBits(mode.getBits(), 4);
    }

    static void appendLengthInfo(int i, Version version, Mode mode, BitArray bitArray) throws WriterException {
        int characterCountBits = mode.getCharacterCountBits(version);
        int i2 = 1 << characterCountBits;
        if (i < i2) {
            bitArray.appendBits(i, characterCountBits);
            return;
        }
        throw new WriterException(i + " is bigger than " + (i2 - 1));
    }

    /* renamed from: com.google.zxing.qrcode.encoder.Encoder$1 */
    static /* synthetic */ class C41221 {
        static final /* synthetic */ int[] $SwitchMap$com$google$zxing$qrcode$decoder$Mode;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.google.zxing.qrcode.decoder.Mode[] r0 = com.google.zxing.qrcode.decoder.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$google$zxing$qrcode$decoder$Mode = r0
                com.google.zxing.qrcode.decoder.Mode r1 = com.google.zxing.qrcode.decoder.Mode.NUMERIC     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.zxing.qrcode.decoder.Mode r1 = com.google.zxing.qrcode.decoder.Mode.ALPHANUMERIC     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.google.zxing.qrcode.decoder.Mode r1 = com.google.zxing.qrcode.decoder.Mode.BYTE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.google.zxing.qrcode.decoder.Mode r1 = com.google.zxing.qrcode.decoder.Mode.KANJI     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.encoder.Encoder.C41221.<clinit>():void");
        }
    }

    static void appendBytes(String str, Mode mode, BitArray bitArray, Charset charset) throws WriterException {
        int i = C41221.$SwitchMap$com$google$zxing$qrcode$decoder$Mode[mode.ordinal()];
        if (i == 1) {
            appendNumericBytes(str, bitArray);
        } else if (i == 2) {
            appendAlphanumericBytes(str, bitArray);
        } else if (i == 3) {
            append8BitBytes(str, bitArray, charset);
        } else if (i == 4) {
            appendKanjiBytes(str, bitArray);
        } else {
            throw new WriterException("Invalid mode: " + mode);
        }
    }

    static void appendNumericBytes(CharSequence charSequence, BitArray bitArray) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            int charAt = charSequence.charAt(i) - '0';
            int i2 = i + 2;
            if (i2 < length) {
                bitArray.appendBits((charAt * 100) + ((charSequence.charAt(i + 1) - '0') * 10) + (charSequence.charAt(i2) - '0'), 10);
                i += 3;
            } else {
                i++;
                if (i < length) {
                    bitArray.appendBits((charAt * 10) + (charSequence.charAt(i) - '0'), 7);
                    i = i2;
                } else {
                    bitArray.appendBits(charAt, 4);
                }
            }
        }
    }

    static void appendAlphanumericBytes(CharSequence charSequence, BitArray bitArray) throws WriterException {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            int alphanumericCode = getAlphanumericCode(charSequence.charAt(i));
            if (alphanumericCode != -1) {
                int i2 = i + 1;
                if (i2 < length) {
                    int alphanumericCode2 = getAlphanumericCode(charSequence.charAt(i2));
                    if (alphanumericCode2 != -1) {
                        bitArray.appendBits((alphanumericCode * 45) + alphanumericCode2, 11);
                        i += 2;
                    } else {
                        throw new WriterException();
                    }
                } else {
                    bitArray.appendBits(alphanumericCode, 6);
                    i = i2;
                }
            } else {
                throw new WriterException();
            }
        }
    }

    static void append8BitBytes(String str, BitArray bitArray, Charset charset) {
        for (byte appendBits : str.getBytes(charset)) {
            bitArray.appendBits(appendBits, 8);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x003c A[LOOP:0: B:3:0x000f->B:16:0x003c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x004b A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void appendKanjiBytes(java.lang.String r6, com.google.zxing.common.BitArray r7) throws com.google.zxing.WriterException {
        /*
            java.nio.charset.Charset r0 = com.google.zxing.common.StringUtils.SHIFT_JIS_CHARSET
            byte[] r6 = r6.getBytes((java.nio.charset.Charset) r0)
            int r0 = r6.length
            int r0 = r0 % 2
            if (r0 != 0) goto L_0x0054
            int r0 = r6.length
            int r0 = r0 + -1
            r1 = 0
        L_0x000f:
            if (r1 >= r0) goto L_0x0053
            byte r2 = r6[r1]
            r2 = r2 & 255(0xff, float:3.57E-43)
            int r3 = r1 + 1
            byte r3 = r6[r3]
            r3 = r3 & 255(0xff, float:3.57E-43)
            int r2 = r2 << 8
            r2 = r2 | r3
            r3 = 33088(0x8140, float:4.6366E-41)
            r4 = -1
            if (r2 < r3) goto L_0x002b
            r5 = 40956(0x9ffc, float:5.7392E-41)
            if (r2 > r5) goto L_0x002b
        L_0x0029:
            int r2 = r2 - r3
            goto L_0x003a
        L_0x002b:
            r3 = 57408(0xe040, float:8.0446E-41)
            if (r2 < r3) goto L_0x0039
            r3 = 60351(0xebbf, float:8.457E-41)
            if (r2 > r3) goto L_0x0039
            r3 = 49472(0xc140, float:6.9325E-41)
            goto L_0x0029
        L_0x0039:
            r2 = r4
        L_0x003a:
            if (r2 == r4) goto L_0x004b
            int r3 = r2 >> 8
            int r3 = r3 * 192
            r2 = r2 & 255(0xff, float:3.57E-43)
            int r3 = r3 + r2
            r2 = 13
            r7.appendBits(r3, r2)
            int r1 = r1 + 2
            goto L_0x000f
        L_0x004b:
            com.google.zxing.WriterException r6 = new com.google.zxing.WriterException
            java.lang.String r7 = "Invalid byte sequence"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x0053:
            return
        L_0x0054:
            com.google.zxing.WriterException r6 = new com.google.zxing.WriterException
            java.lang.String r7 = "Kanji byte size not even"
            r6.<init>((java.lang.String) r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.encoder.Encoder.appendKanjiBytes(java.lang.String, com.google.zxing.common.BitArray):void");
    }

    private static void appendECI(CharacterSetECI characterSetECI, BitArray bitArray) {
        bitArray.appendBits(Mode.ECI.getBits(), 4);
        bitArray.appendBits(characterSetECI.getValue(), 8);
    }
}

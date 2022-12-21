package com.google.zxing.datamatrix.decoder;

import android.icu.lang.UCharacter;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.ECIStringBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import kotlin.text.Typography;

final class DecodedBitStreamParser {
    private static final char[] C40_BASIC_SET_CHARS = {'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] C40_SHIFT2_SET_CHARS;
    private static final char[] TEXT_BASIC_SET_CHARS = {'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char[] TEXT_SHIFT2_SET_CHARS;
    private static final char[] TEXT_SHIFT3_SET_CHARS = {'`', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '{', '|', '}', '~', 127};

    private enum Mode {
        PAD_ENCODE,
        ASCII_ENCODE,
        C40_ENCODE,
        TEXT_ENCODE,
        ANSIX12_ENCODE,
        EDIFACT_ENCODE,
        BASE256_ENCODE,
        ECI_ENCODE
    }

    static {
        char[] cArr = {'!', Typography.quote, '#', '$', '%', Typography.amp, '\'', '(', ')', '*', '+', ',', '-', '.', '/', AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR, ';', Typography.less, '=', Typography.greater, '?', '@', '[', '\\', ']', '^', '_'};
        C40_SHIFT2_SET_CHARS = cArr;
        TEXT_SHIFT2_SET_CHARS = cArr;
    }

    private DecodedBitStreamParser() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0099  */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.google.zxing.common.DecoderResult decode(byte[] r13) throws com.google.zxing.FormatException {
        /*
            com.google.zxing.common.BitSource r0 = new com.google.zxing.common.BitSource
            r0.<init>(r13)
            com.google.zxing.common.ECIStringBuilder r1 = new com.google.zxing.common.ECIStringBuilder
            r2 = 100
            r1.<init>(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = 0
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r3)
            r2.<init>((int) r3)
            java.util.ArrayList r5 = new java.util.ArrayList
            r6 = 1
            java.lang.Integer r7 = java.lang.Integer.valueOf((int) r6)
            r5.<init>((int) r6)
            com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r8 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.ASCII_ENCODE
            java.util.HashSet r9 = new java.util.HashSet
            r9.<init>()
        L_0x0027:
            com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r10 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.ASCII_ENCODE
            if (r8 != r10) goto L_0x0030
            com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r8 = decodeAsciiSegment(r0, r1, r2, r9)
            goto L_0x005a
        L_0x0030:
            int[] r10 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.C40941.f466xb73eb560
            int r8 = r8.ordinal()
            r8 = r10[r8]
            switch(r8) {
                case 1: goto L_0x0055;
                case 2: goto L_0x0051;
                case 3: goto L_0x004d;
                case 4: goto L_0x0049;
                case 5: goto L_0x0045;
                case 6: goto L_0x0040;
                default: goto L_0x003b;
            }
        L_0x003b:
            com.google.zxing.FormatException r13 = com.google.zxing.FormatException.getFormatInstance()
            throw r13
        L_0x0040:
            decodeECISegment(r0, r1)
            r3 = r6
            goto L_0x0058
        L_0x0045:
            decodeBase256Segment(r0, r1, r5)
            goto L_0x0058
        L_0x0049:
            decodeEdifactSegment(r0, r1)
            goto L_0x0058
        L_0x004d:
            decodeAnsiX12Segment(r0, r1)
            goto L_0x0058
        L_0x0051:
            decodeTextSegment(r0, r1, r9)
            goto L_0x0058
        L_0x0055:
            decodeC40Segment(r0, r1, r9)
        L_0x0058:
            com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r8 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.ASCII_ENCODE
        L_0x005a:
            com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r10 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.PAD_ENCODE
            if (r8 == r10) goto L_0x0064
            int r10 = r0.available()
            if (r10 > 0) goto L_0x0027
        L_0x0064:
            int r0 = r2.length()
            if (r0 <= 0) goto L_0x006d
            r1.appendCharacters(r2)
        L_0x006d:
            r0 = 5
            r2 = 4
            if (r3 == 0) goto L_0x0099
            boolean r3 = r9.contains(r4)
            if (r3 != 0) goto L_0x0097
            java.lang.Integer r3 = java.lang.Integer.valueOf((int) r2)
            boolean r3 = r9.contains(r3)
            if (r3 == 0) goto L_0x0082
            goto L_0x0097
        L_0x0082:
            boolean r3 = r9.contains(r7)
            if (r3 != 0) goto L_0x0095
            java.lang.Integer r0 = java.lang.Integer.valueOf((int) r0)
            boolean r0 = r9.contains(r0)
            if (r0 == 0) goto L_0x0093
            goto L_0x0095
        L_0x0093:
            r12 = r2
            goto L_0x00be
        L_0x0095:
            r6 = 6
            goto L_0x00bd
        L_0x0097:
            r12 = r0
            goto L_0x00be
        L_0x0099:
            boolean r3 = r9.contains(r4)
            if (r3 != 0) goto L_0x00bc
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r2)
            boolean r2 = r9.contains(r2)
            if (r2 == 0) goto L_0x00aa
            goto L_0x00bc
        L_0x00aa:
            boolean r2 = r9.contains(r7)
            if (r2 != 0) goto L_0x00ba
            java.lang.Integer r0 = java.lang.Integer.valueOf((int) r0)
            boolean r0 = r9.contains(r0)
            if (r0 == 0) goto L_0x00bd
        L_0x00ba:
            r6 = 3
            goto L_0x00bd
        L_0x00bc:
            r6 = 2
        L_0x00bd:
            r12 = r6
        L_0x00be:
            com.google.zxing.common.DecoderResult r0 = new com.google.zxing.common.DecoderResult
            java.lang.String r9 = r1.toString()
            boolean r1 = r5.isEmpty()
            if (r1 == 0) goto L_0x00cb
            r5 = 0
        L_0x00cb:
            r10 = r5
            r11 = 0
            r7 = r0
            r8 = r13
            r7.<init>(r8, r9, r10, r11, r12)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.decode(byte[]):com.google.zxing.common.DecoderResult");
    }

    /* renamed from: com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$1 */
    static /* synthetic */ class C40941 {

        /* renamed from: $SwitchMap$com$google$zxing$datamatrix$decoder$DecodedBitStreamParser$Mode */
        static final /* synthetic */ int[] f466xb73eb560;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode[] r0 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f466xb73eb560 = r0
                com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.C40_ENCODE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f466xb73eb560     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.TEXT_ENCODE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f466xb73eb560     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.ANSIX12_ENCODE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f466xb73eb560     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.EDIFACT_ENCODE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f466xb73eb560     // Catch:{ NoSuchFieldError -> 0x003e }
                com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.BASE256_ENCODE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = f466xb73eb560     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.google.zxing.datamatrix.decoder.DecodedBitStreamParser$Mode r1 = com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.Mode.ECI_ENCODE     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.datamatrix.decoder.DecodedBitStreamParser.C40941.<clinit>():void");
        }
    }

    private static Mode decodeAsciiSegment(BitSource bitSource, ECIStringBuilder eCIStringBuilder, StringBuilder sb, Set<Integer> set) throws FormatException {
        boolean z = false;
        do {
            int readBits = bitSource.readBits(8);
            if (readBits == 0) {
                throw FormatException.getFormatInstance();
            } else if (readBits <= 128) {
                if (z) {
                    readBits += 128;
                }
                eCIStringBuilder.append((char) (readBits - 1));
                return Mode.ASCII_ENCODE;
            } else if (readBits == 129) {
                return Mode.PAD_ENCODE;
            } else {
                if (readBits <= 229) {
                    int i = readBits - 130;
                    if (i < 10) {
                        eCIStringBuilder.append('0');
                    }
                    eCIStringBuilder.append(i);
                } else {
                    switch (readBits) {
                        case UCharacter.UnicodeBlock.KHUDAWADI_ID:
                            return Mode.C40_ENCODE;
                        case UCharacter.UnicodeBlock.LATIN_EXTENDED_E_ID:
                            return Mode.BASE256_ENCODE;
                        case 232:
                            set.add(Integer.valueOf(eCIStringBuilder.length()));
                            eCIStringBuilder.append(29);
                            break;
                        case 233:
                        case 234:
                            break;
                        case 235:
                            z = true;
                            break;
                        case 236:
                            eCIStringBuilder.append("[)>\u001e05\u001d");
                            sb.insert(0, "\u001e\u0004");
                            break;
                        case 237:
                            eCIStringBuilder.append("[)>\u001e06\u001d");
                            sb.insert(0, "\u001e\u0004");
                            break;
                        case 238:
                            return Mode.ANSIX12_ENCODE;
                        case 239:
                            return Mode.TEXT_ENCODE;
                        case 240:
                            return Mode.EDIFACT_ENCODE;
                        case UCharacter.UnicodeBlock.OLD_PERMIC_ID:
                            return Mode.ECI_ENCODE;
                        default:
                            if (!(readBits == 254 && bitSource.available() == 0)) {
                                throw FormatException.getFormatInstance();
                            }
                    }
                }
            }
        } while (bitSource.available() > 0);
        return Mode.ASCII_ENCODE;
    }

    private static void decodeC40Segment(BitSource bitSource, ECIStringBuilder eCIStringBuilder, Set<Integer> set) throws FormatException {
        int readBits;
        int[] iArr = new int[3];
        boolean z = false;
        int i = 0;
        while (bitSource.available() != 8 && (readBits = bitSource.readBits(8)) != 254) {
            parseTwoBytes(readBits, bitSource.readBits(8), iArr);
            for (int i2 = 0; i2 < 3; i2++) {
                int i3 = iArr[i2];
                if (i != 0) {
                    if (i != 1) {
                        if (i == 2) {
                            char[] cArr = C40_SHIFT2_SET_CHARS;
                            if (i3 < cArr.length) {
                                char c = cArr[i3];
                                if (z) {
                                    eCIStringBuilder.append((char) (c + 128));
                                } else {
                                    eCIStringBuilder.append(c);
                                }
                            } else if (i3 == 27) {
                                set.add(Integer.valueOf(eCIStringBuilder.length()));
                                eCIStringBuilder.append(29);
                            } else if (i3 == 30) {
                                z = true;
                            } else {
                                throw FormatException.getFormatInstance();
                            }
                            i = 0;
                        } else if (i != 3) {
                            throw FormatException.getFormatInstance();
                        } else if (z) {
                            eCIStringBuilder.append((char) (i3 + 224));
                        } else {
                            eCIStringBuilder.append((char) (i3 + 96));
                            i = 0;
                        }
                    } else if (z) {
                        eCIStringBuilder.append((char) (i3 + 128));
                    } else {
                        eCIStringBuilder.append((char) i3);
                        i = 0;
                    }
                    z = false;
                    i = 0;
                } else if (i3 < 3) {
                    i = i3 + 1;
                } else {
                    char[] cArr2 = C40_BASIC_SET_CHARS;
                    if (i3 < cArr2.length) {
                        char c2 = cArr2[i3];
                        if (z) {
                            eCIStringBuilder.append((char) (c2 + 128));
                            z = false;
                        } else {
                            eCIStringBuilder.append(c2);
                        }
                    } else {
                        throw FormatException.getFormatInstance();
                    }
                }
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void decodeTextSegment(BitSource bitSource, ECIStringBuilder eCIStringBuilder, Set<Integer> set) throws FormatException {
        int readBits;
        int[] iArr = new int[3];
        boolean z = false;
        int i = 0;
        while (bitSource.available() != 8 && (readBits = bitSource.readBits(8)) != 254) {
            parseTwoBytes(readBits, bitSource.readBits(8), iArr);
            for (int i2 = 0; i2 < 3; i2++) {
                int i3 = iArr[i2];
                if (i != 0) {
                    if (i != 1) {
                        if (i == 2) {
                            char[] cArr = TEXT_SHIFT2_SET_CHARS;
                            if (i3 < cArr.length) {
                                char c = cArr[i3];
                                if (z) {
                                    eCIStringBuilder.append((char) (c + 128));
                                } else {
                                    eCIStringBuilder.append(c);
                                }
                            } else if (i3 == 27) {
                                set.add(Integer.valueOf(eCIStringBuilder.length()));
                                eCIStringBuilder.append(29);
                            } else if (i3 == 30) {
                                z = true;
                            } else {
                                throw FormatException.getFormatInstance();
                            }
                            i = 0;
                        } else if (i == 3) {
                            char[] cArr2 = TEXT_SHIFT3_SET_CHARS;
                            if (i3 < cArr2.length) {
                                char c2 = cArr2[i3];
                                if (z) {
                                    eCIStringBuilder.append((char) (c2 + 128));
                                } else {
                                    eCIStringBuilder.append(c2);
                                    i = 0;
                                }
                            } else {
                                throw FormatException.getFormatInstance();
                            }
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                    } else if (z) {
                        eCIStringBuilder.append((char) (i3 + 128));
                    } else {
                        eCIStringBuilder.append((char) i3);
                        i = 0;
                    }
                    z = false;
                    i = 0;
                } else if (i3 < 3) {
                    i = i3 + 1;
                } else {
                    char[] cArr3 = TEXT_BASIC_SET_CHARS;
                    if (i3 < cArr3.length) {
                        char c3 = cArr3[i3];
                        if (z) {
                            eCIStringBuilder.append((char) (c3 + 128));
                            z = false;
                        } else {
                            eCIStringBuilder.append(c3);
                        }
                    } else {
                        throw FormatException.getFormatInstance();
                    }
                }
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void decodeAnsiX12Segment(BitSource bitSource, ECIStringBuilder eCIStringBuilder) throws FormatException {
        int readBits;
        int[] iArr = new int[3];
        while (bitSource.available() != 8 && (readBits = bitSource.readBits(8)) != 254) {
            parseTwoBytes(readBits, bitSource.readBits(8), iArr);
            for (int i = 0; i < 3; i++) {
                int i2 = iArr[i];
                if (i2 == 0) {
                    eCIStringBuilder.append(13);
                } else if (i2 == 1) {
                    eCIStringBuilder.append('*');
                } else if (i2 == 2) {
                    eCIStringBuilder.append((char) Typography.greater);
                } else if (i2 == 3) {
                    eCIStringBuilder.append(' ');
                } else if (i2 < 14) {
                    eCIStringBuilder.append((char) (i2 + 44));
                } else if (i2 < 40) {
                    eCIStringBuilder.append((char) (i2 + 51));
                } else {
                    throw FormatException.getFormatInstance();
                }
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void parseTwoBytes(int i, int i2, int[] iArr) {
        int i3 = ((i << 8) + i2) - 1;
        int i4 = i3 / 1600;
        iArr[0] = i4;
        int i5 = i3 - (i4 * 1600);
        int i6 = i5 / 40;
        iArr[1] = i6;
        iArr[2] = i5 - (i6 * 40);
    }

    private static void decodeEdifactSegment(BitSource bitSource, ECIStringBuilder eCIStringBuilder) {
        while (bitSource.available() > 16) {
            for (int i = 0; i < 4; i++) {
                int readBits = bitSource.readBits(6);
                if (readBits == 31) {
                    int bitOffset = 8 - bitSource.getBitOffset();
                    if (bitOffset != 8) {
                        bitSource.readBits(bitOffset);
                        return;
                    }
                    return;
                }
                if ((readBits & 32) == 0) {
                    readBits |= 64;
                }
                eCIStringBuilder.append((char) readBits);
            }
            if (bitSource.available() <= 0) {
                return;
            }
        }
    }

    private static void decodeBase256Segment(BitSource bitSource, ECIStringBuilder eCIStringBuilder, Collection<byte[]> collection) throws FormatException {
        int byteOffset = bitSource.getByteOffset() + 1;
        int i = byteOffset + 1;
        int unrandomize255State = unrandomize255State(bitSource.readBits(8), byteOffset);
        if (unrandomize255State == 0) {
            unrandomize255State = bitSource.available() / 8;
        } else if (unrandomize255State >= 250) {
            unrandomize255State = ((unrandomize255State - 249) * 250) + unrandomize255State(bitSource.readBits(8), i);
            i++;
        }
        if (unrandomize255State >= 0) {
            byte[] bArr = new byte[unrandomize255State];
            int i2 = 0;
            while (i2 < unrandomize255State) {
                if (bitSource.available() >= 8) {
                    bArr[i2] = (byte) unrandomize255State(bitSource.readBits(8), i);
                    i2++;
                    i++;
                } else {
                    throw FormatException.getFormatInstance();
                }
            }
            collection.add(bArr);
            eCIStringBuilder.append(new String(bArr, StandardCharsets.ISO_8859_1));
            return;
        }
        throw FormatException.getFormatInstance();
    }

    private static void decodeECISegment(BitSource bitSource, ECIStringBuilder eCIStringBuilder) throws FormatException {
        if (bitSource.available() >= 8) {
            int readBits = bitSource.readBits(8);
            if (readBits <= 127) {
                eCIStringBuilder.appendECI(readBits - 1);
                return;
            }
            return;
        }
        throw FormatException.getFormatInstance();
    }

    private static int unrandomize255State(int i, int i2) {
        int i3 = i - (((i2 * 149) % 255) + 1);
        return i3 >= 0 ? i3 : i3 + 256;
    }
}

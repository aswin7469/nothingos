package com.google.zxing.oned;

import android.icu.lang.UCharacter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class Code128Writer extends OneDimensionalCodeWriter {
    private static final int CODE_CODE_A = 101;
    private static final int CODE_CODE_B = 100;
    private static final int CODE_CODE_C = 99;
    private static final int CODE_FNC_1 = 102;
    private static final int CODE_FNC_2 = 97;
    private static final int CODE_FNC_3 = 96;
    private static final int CODE_FNC_4_A = 101;
    private static final int CODE_FNC_4_B = 100;
    private static final int CODE_START_A = 103;
    private static final int CODE_START_B = 104;
    private static final int CODE_START_C = 105;
    private static final int CODE_STOP = 106;
    private static final char ESCAPE_FNC_1 = 'ñ';
    private static final char ESCAPE_FNC_2 = 'ò';
    private static final char ESCAPE_FNC_3 = 'ó';
    private static final char ESCAPE_FNC_4 = 'ô';

    private enum CType {
        UNCODABLE,
        ONE_DIGIT,
        TWO_DIGITS,
        FNC_1
    }

    /* access modifiers changed from: protected */
    public Collection<BarcodeFormat> getSupportedWriteFormats() {
        return Collections.singleton(BarcodeFormat.CODE_128);
    }

    public boolean[] encode(String str) {
        return encode(str, (Map<EncodeHintType, ?>) null);
    }

    /* access modifiers changed from: protected */
    public boolean[] encode(String str, Map<EncodeHintType, ?> map) {
        return map != null && map.containsKey(EncodeHintType.CODE128_COMPACT) && Boolean.parseBoolean(map.get(EncodeHintType.CODE128_COMPACT).toString()) ? new MinimalEncoder((C40991) null).encode(str) : encodeFast(str, check(str, map));
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003e, code lost:
        if (r6.equals("B") == false) goto L_0x002b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int check(java.lang.String r5, java.util.Map<com.google.zxing.EncodeHintType, ?> r6) {
        /*
            int r0 = r5.length()
            r1 = 1
            if (r0 < r1) goto L_0x00f7
            r2 = 80
            if (r0 > r2) goto L_0x00f7
            r2 = 0
            r3 = -1
            if (r6 == 0) goto L_0x006b
            com.google.zxing.EncodeHintType r4 = com.google.zxing.EncodeHintType.FORCE_CODE_SET
            boolean r4 = r6.containsKey(r4)
            if (r4 == 0) goto L_0x006b
            com.google.zxing.EncodeHintType r4 = com.google.zxing.EncodeHintType.FORCE_CODE_SET
            java.lang.Object r6 = r6.get(r4)
            java.lang.String r6 = r6.toString()
            r6.hashCode()
            int r4 = r6.hashCode()
            switch(r4) {
                case 65: goto L_0x0041;
                case 66: goto L_0x0038;
                case 67: goto L_0x002d;
                default: goto L_0x002b;
            }
        L_0x002b:
            r1 = r3
            goto L_0x004b
        L_0x002d:
            java.lang.String r1 = "C"
            boolean r1 = r6.equals(r1)
            if (r1 != 0) goto L_0x0036
            goto L_0x002b
        L_0x0036:
            r1 = 2
            goto L_0x004b
        L_0x0038:
            java.lang.String r4 = "B"
            boolean r4 = r6.equals(r4)
            if (r4 != 0) goto L_0x004b
            goto L_0x002b
        L_0x0041:
            java.lang.String r1 = "A"
            boolean r1 = r6.equals(r1)
            if (r1 != 0) goto L_0x004a
            goto L_0x002b
        L_0x004a:
            r1 = r2
        L_0x004b:
            switch(r1) {
                case 0: goto L_0x0069;
                case 1: goto L_0x0066;
                case 2: goto L_0x0063;
                default: goto L_0x004e;
            }
        L_0x004e:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Unsupported code set hint: "
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r6 = r0.append((java.lang.String) r6)
            java.lang.String r6 = r6.toString()
            r5.<init>((java.lang.String) r6)
            throw r5
        L_0x0063:
            r3 = 99
            goto L_0x006b
        L_0x0066:
            r3 = 100
            goto L_0x006b
        L_0x0069:
            r3 = 101(0x65, float:1.42E-43)
        L_0x006b:
            if (r2 >= r0) goto L_0x00f6
            char r6 = r5.charAt(r2)
            r1 = 127(0x7f, float:1.78E-43)
            switch(r6) {
                case 241: goto L_0x0078;
                case 242: goto L_0x0078;
                case 243: goto L_0x0078;
                case 244: goto L_0x0078;
                default: goto L_0x0076;
            }
        L_0x0076:
            if (r6 > r1) goto L_0x00e1
        L_0x0078:
            switch(r3) {
                case 99: goto L_0x00b2;
                case 100: goto L_0x0098;
                case 101: goto L_0x007c;
                default: goto L_0x007b;
            }
        L_0x007b:
            goto L_0x00de
        L_0x007c:
            r4 = 95
            if (r6 <= r4) goto L_0x00de
            if (r6 <= r1) goto L_0x0083
            goto L_0x00de
        L_0x0083:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Bad character in input for forced code set A: ASCII value="
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r6 = r0.append((int) r6)
            java.lang.String r6 = r6.toString()
            r5.<init>((java.lang.String) r6)
            throw r5
        L_0x0098:
            r1 = 32
            if (r6 <= r1) goto L_0x009d
            goto L_0x00de
        L_0x009d:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Bad character in input for forced code set B: ASCII value="
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r6 = r0.append((int) r6)
            java.lang.String r6 = r6.toString()
            r5.<init>((java.lang.String) r6)
            throw r5
        L_0x00b2:
            r4 = 48
            if (r6 < r4) goto L_0x00c9
            r4 = 57
            if (r6 <= r4) goto L_0x00bc
            if (r6 <= r1) goto L_0x00c9
        L_0x00bc:
            r1 = 242(0xf2, float:3.39E-43)
            if (r6 == r1) goto L_0x00c9
            r1 = 243(0xf3, float:3.4E-43)
            if (r6 == r1) goto L_0x00c9
            r1 = 244(0xf4, float:3.42E-43)
            if (r6 == r1) goto L_0x00c9
            goto L_0x00de
        L_0x00c9:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Bad character in input for forced code set C: ASCII value="
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r6 = r0.append((int) r6)
            java.lang.String r6 = r6.toString()
            r5.<init>((java.lang.String) r6)
            throw r5
        L_0x00de:
            int r2 = r2 + 1
            goto L_0x006b
        L_0x00e1:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Bad character in input: ASCII value="
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r6 = r0.append((int) r6)
            java.lang.String r6 = r6.toString()
            r5.<init>((java.lang.String) r6)
            throw r5
        L_0x00f6:
            return r3
        L_0x00f7:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r1 = "Contents length should be between 1 and 80 characters, but got "
            r6.<init>((java.lang.String) r1)
            java.lang.StringBuilder r6 = r6.append((int) r0)
            java.lang.String r6 = r6.toString()
            r5.<init>((java.lang.String) r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.Code128Writer.check(java.lang.String, java.util.Map):int");
    }

    private static boolean[] encodeFast(String str, int i) {
        int i2;
        int length = str.length();
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 1;
        while (i3 < length) {
            int chooseCode = i == -1 ? chooseCode(str, i3, i5) : i;
            int i7 = 100;
            if (chooseCode == i5) {
                switch (str.charAt(i3)) {
                    case UCharacter.UnicodeBlock.OLD_PERMIC_ID:
                        i7 = 102;
                        break;
                    case 242:
                        i7 = 97;
                        break;
                    case 243:
                        i7 = 96;
                        break;
                    case 244:
                        if (i5 == 101) {
                            i7 = 101;
                            break;
                        }
                        break;
                    default:
                        if (i5 != 100) {
                            if (i5 == 101) {
                                i7 = str.charAt(i3) - ' ';
                                if (i7 < 0) {
                                    i7 += 96;
                                    break;
                                }
                            } else {
                                int i8 = i3 + 1;
                                if (i8 != length) {
                                    i7 = Integer.parseInt(str.substring(i3, i3 + 2));
                                    i3 = i8;
                                    break;
                                } else {
                                    throw new IllegalArgumentException("Bad number of characters for digit only encoding.");
                                }
                            }
                        } else {
                            i7 = str.charAt(i3) - ' ';
                            break;
                        }
                        break;
                }
                i3++;
            } else {
                if (i5 == 0) {
                    i2 = chooseCode != 100 ? chooseCode != 101 ? 105 : 103 : 104;
                } else {
                    i2 = chooseCode;
                }
                i5 = chooseCode;
            }
            arrayList.add(Code128Reader.CODE_PATTERNS[i2]);
            i4 += i2 * i6;
            if (i3 != 0) {
                i6++;
            }
        }
        return produceResult(arrayList, i4);
    }

    static boolean[] produceResult(Collection<int[]> collection, int i) {
        collection.add(Code128Reader.CODE_PATTERNS[i % 103]);
        collection.add(Code128Reader.CODE_PATTERNS[106]);
        int i2 = 0;
        int i3 = 0;
        for (int[] next : collection) {
            for (int i4 : r7.next()) {
                i3 += i4;
            }
        }
        boolean[] zArr = new boolean[i3];
        for (int[] appendPattern : collection) {
            i2 += appendPattern(zArr, i2, appendPattern, true);
        }
        return zArr;
    }

    private static CType findCType(CharSequence charSequence, int i) {
        int length = charSequence.length();
        if (i >= length) {
            return CType.UNCODABLE;
        }
        char charAt = charSequence.charAt(i);
        if (charAt == 241) {
            return CType.FNC_1;
        }
        if (charAt < '0' || charAt > '9') {
            return CType.UNCODABLE;
        }
        int i2 = i + 1;
        if (i2 >= length) {
            return CType.ONE_DIGIT;
        }
        char charAt2 = charSequence.charAt(i2);
        if (charAt2 < '0' || charAt2 > '9') {
            return CType.ONE_DIGIT;
        }
        return CType.TWO_DIGITS;
    }

    private static int chooseCode(CharSequence charSequence, int i, int i2) {
        CType findCType;
        CType findCType2;
        char charAt;
        CType findCType3 = findCType(charSequence, i);
        if (findCType3 == CType.ONE_DIGIT) {
            return i2 == 101 ? 101 : 100;
        }
        if (findCType3 == CType.UNCODABLE) {
            return (i >= charSequence.length() || ((charAt = charSequence.charAt(i)) >= ' ' && (i2 != 101 || (charAt >= '`' && (charAt < 241 || charAt > 244))))) ? 100 : 101;
        }
        if (i2 == 101 && findCType3 == CType.FNC_1) {
            return 101;
        }
        if (i2 == 99) {
            return 99;
        }
        if (i2 != 100) {
            if (findCType3 == CType.FNC_1) {
                findCType3 = findCType(charSequence, i + 1);
            }
            if (findCType3 == CType.TWO_DIGITS) {
                return 99;
            }
            return 100;
        } else if (findCType3 == CType.FNC_1 || (findCType = findCType(charSequence, i + 2)) == CType.UNCODABLE || findCType == CType.ONE_DIGIT) {
            return 100;
        } else {
            if (findCType != CType.FNC_1) {
                int i3 = i + 4;
                while (true) {
                    findCType2 = findCType(charSequence, i3);
                    if (findCType2 != CType.TWO_DIGITS) {
                        break;
                    }
                    i3 += 2;
                }
                if (findCType2 == CType.ONE_DIGIT) {
                    return 100;
                }
                return 99;
            } else if (findCType(charSequence, i + 3) == CType.TWO_DIGITS) {
                return 99;
            } else {
                return 100;
            }
        }
    }

    private static final class MinimalEncoder {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        /* renamed from: A */
        static final String f474A = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001fÿ";

        /* renamed from: B */
        static final String f475B = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ÿ";
        private static final int CODE_SHIFT = 98;
        private int[][] memoizedCost;
        private Latch[][] minPath;

        private enum Charset {
            f476A,
            B,
            C,
            NONE
        }

        private enum Latch {
            f479A,
            B,
            C,
            SHIFT,
            NONE
        }

        private static boolean isDigit(char c) {
            return c >= '0' && c <= '9';
        }

        static {
            Class<Code128Writer> cls = Code128Writer.class;
        }

        private MinimalEncoder() {
        }

        /* synthetic */ MinimalEncoder(C40991 r1) {
            this();
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x009c  */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x00bb  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean[] encode(java.lang.String r17) {
            /*
                r16 = this;
                r0 = r16
                r1 = r17
                int r2 = r17.length()
                r3 = 2
                int[] r4 = new int[r3]
                r5 = 1
                r4[r5] = r2
                r2 = 0
                r6 = 4
                r4[r2] = r6
                java.lang.Class<java.lang.Integer> r7 = java.lang.Integer.TYPE
                java.lang.Object r4 = java.lang.reflect.Array.newInstance((java.lang.Class<?>) r7, (int[]) r4)
                int[][] r4 = (int[][]) r4
                r0.memoizedCost = r4
                int r4 = r17.length()
                int[] r7 = new int[r3]
                r7[r5] = r4
                r7[r2] = r6
                java.lang.Class<com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch> r4 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.class
                java.lang.Object r4 = java.lang.reflect.Array.newInstance((java.lang.Class<?>) r4, (int[]) r7)
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch[][] r4 = (com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch[][]) r4
                r0.minPath = r4
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r4 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.NONE
                r0.encode(r1, r4, r2)
                java.util.ArrayList r4 = new java.util.ArrayList
                r4.<init>()
                int[] r7 = new int[r5]
                r7[r2] = r2
                int[] r8 = new int[r5]
                r8[r2] = r5
                int r9 = r17.length()
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r10 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.NONE
                r11 = r2
            L_0x0049:
                if (r11 >= r9) goto L_0x00fd
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch[][] r12 = r0.minPath
                int r13 = r10.ordinal()
                r12 = r12[r13]
                r12 = r12[r11]
                int[] r13 = com.google.zxing.oned.Code128Writer.C40991.f473x56054aa0
                int r14 = r12.ordinal()
                r13 = r13[r14]
                r14 = 100
                r15 = 101(0x65, float:1.42E-43)
                if (r13 == r5) goto L_0x008a
                if (r13 == r3) goto L_0x007e
                r3 = 3
                if (r13 == r3) goto L_0x0071
                if (r13 == r6) goto L_0x006b
                goto L_0x0096
            L_0x006b:
                r3 = 98
                addPattern(r4, r3, r7, r8, r11)
                goto L_0x0096
            L_0x0071:
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.C
                if (r11 != 0) goto L_0x0078
                r10 = 105(0x69, float:1.47E-43)
                goto L_0x007a
            L_0x0078:
                r10 = 99
            L_0x007a:
                addPattern(r4, r10, r7, r8, r11)
                goto L_0x0095
            L_0x007e:
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.B
                if (r11 != 0) goto L_0x0085
                r10 = 104(0x68, float:1.46E-43)
                goto L_0x0086
            L_0x0085:
                r10 = r14
            L_0x0086:
                addPattern(r4, r10, r7, r8, r11)
                goto L_0x0095
            L_0x008a:
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.f476A
                if (r11 != 0) goto L_0x0091
                r10 = 103(0x67, float:1.44E-43)
                goto L_0x0092
            L_0x0091:
                r10 = r15
            L_0x0092:
                addPattern(r4, r10, r7, r8, r11)
            L_0x0095:
                r10 = r3
            L_0x0096:
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.C
                r13 = 102(0x66, float:1.43E-43)
                if (r10 != r3) goto L_0x00bb
                char r3 = r1.charAt(r11)
                r12 = 241(0xf1, float:3.38E-43)
                if (r3 != r12) goto L_0x00a8
                addPattern(r4, r13, r7, r8, r11)
                goto L_0x00f9
            L_0x00a8:
                int r3 = r11 + 2
                java.lang.String r3 = r1.substring(r11, r3)
                int r3 = java.lang.Integer.parseInt(r3)
                addPattern(r4, r3, r7, r8, r11)
                int r3 = r11 + 1
                if (r3 >= r9) goto L_0x00f9
                r11 = r3
                goto L_0x00f9
            L_0x00bb:
                char r3 = r1.charAt(r11)
                switch(r3) {
                    case 241: goto L_0x00e1;
                    case 242: goto L_0x00de;
                    case 243: goto L_0x00db;
                    case 244: goto L_0x00c9;
                    default: goto L_0x00c2;
                }
            L_0x00c2:
                char r3 = r1.charAt(r11)
                int r14 = r3 + -32
                goto L_0x00e2
            L_0x00c9:
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.f476A
                if (r10 != r3) goto L_0x00d1
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.SHIFT
                if (r12 != r3) goto L_0x00d9
            L_0x00d1:
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.B
                if (r10 != r3) goto L_0x00e2
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.SHIFT
                if (r12 != r3) goto L_0x00e2
            L_0x00d9:
                r14 = r15
                goto L_0x00e2
            L_0x00db:
                r14 = 96
                goto L_0x00e2
            L_0x00de:
                r14 = 97
                goto L_0x00e2
            L_0x00e1:
                r14 = r13
            L_0x00e2:
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.f476A
                if (r10 != r3) goto L_0x00ea
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.SHIFT
                if (r12 != r3) goto L_0x00f2
            L_0x00ea:
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.B
                if (r10 != r3) goto L_0x00f6
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.SHIFT
                if (r12 != r3) goto L_0x00f6
            L_0x00f2:
                if (r14 >= 0) goto L_0x00f6
                int r14 = r14 + 96
            L_0x00f6:
                addPattern(r4, r14, r7, r8, r11)
            L_0x00f9:
                int r11 = r11 + r5
                r3 = 2
                goto L_0x0049
            L_0x00fd:
                r1 = 0
                r3 = r1
                int[][] r3 = (int[][]) r3
                r0.memoizedCost = r1
                r3 = r1
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch[][] r3 = (com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch[][]) r3
                r0.minPath = r1
                r0 = r7[r2]
                boolean[] r0 = com.google.zxing.oned.Code128Writer.produceResult(r4, r0)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.Code128Writer.MinimalEncoder.encode(java.lang.String):boolean[]");
        }

        private static void addPattern(Collection<int[]> collection, int i, int[] iArr, int[] iArr2, int i2) {
            collection.add(Code128Reader.CODE_PATTERNS[i]);
            if (i2 != 0) {
                iArr2[0] = iArr2[0] + 1;
            }
            iArr[0] = iArr[0] + (i * iArr2[0]);
        }

        private boolean canEncode(CharSequence charSequence, Charset charset, int i) {
            int i2;
            char charAt = charSequence.charAt(i);
            int i3 = C40991.f472x18b242a8[charset.ordinal()];
            if (i3 != 1) {
                if (i3 != 2) {
                    if (i3 != 3) {
                        return false;
                    }
                    if (charAt == 241 || ((i2 = i + 1) < charSequence.length() && isDigit(charAt) && isDigit(charSequence.charAt(i2)))) {
                        return true;
                    }
                    return false;
                } else if (charAt == 241 || charAt == 242 || charAt == 243 || charAt == 244 || f475B.indexOf((int) charAt) >= 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (charAt == 241 || charAt == 242 || charAt == 243 || charAt == 244 || f474A.indexOf((int) charAt) >= 0) {
                return true;
            } else {
                return false;
            }
        }

        private int encode(CharSequence charSequence, Charset charset, int i) {
            int i2;
            int i3;
            int i4 = this.memoizedCost[charset.ordinal()][i];
            if (i4 > 0) {
                return i4;
            }
            Latch latch = Latch.NONE;
            int i5 = i + 1;
            int i6 = 1;
            boolean z = i5 >= charSequence.length();
            Charset[] charsetArr = {Charset.f476A, Charset.B};
            int i7 = Integer.MAX_VALUE;
            for (int i8 = 0; i8 <= 1; i8++) {
                if (canEncode(charSequence, charsetArr[i8], i)) {
                    Latch latch2 = Latch.NONE;
                    Charset charset2 = charsetArr[i8];
                    if (charset != charset2) {
                        latch2 = Latch.valueOf(charset2.toString());
                        i3 = 2;
                    } else {
                        i3 = 1;
                    }
                    if (!z) {
                        i3 += encode(charSequence, charsetArr[i8], i5);
                    }
                    if (i3 < i7) {
                        latch = latch2;
                        i7 = i3;
                    }
                    if (charset == charsetArr[(i8 + 1) % 2]) {
                        Latch latch3 = Latch.SHIFT;
                        int encode = !z ? encode(charSequence, charset, i5) + 2 : 2;
                        if (encode < i7) {
                            latch = latch3;
                            i7 = encode;
                        }
                    }
                }
            }
            if (canEncode(charSequence, Charset.C, i)) {
                Latch latch4 = Latch.NONE;
                if (charset != Charset.C) {
                    latch4 = Latch.C;
                    i2 = 2;
                } else {
                    i2 = 1;
                }
                if (charSequence.charAt(i) != 241) {
                    i6 = 2;
                }
                int i9 = i6 + i;
                if (i9 < charSequence.length()) {
                    i2 += encode(charSequence, Charset.C, i9);
                }
                if (i2 < i7) {
                    latch = latch4;
                    i7 = i2;
                }
            }
            if (i7 != Integer.MAX_VALUE) {
                this.memoizedCost[charset.ordinal()][i] = i7;
                this.minPath[charset.ordinal()][i] = latch;
                return i7;
            }
            throw new IllegalArgumentException("Bad character in input: ASCII value=" + charSequence.charAt(i));
        }
    }

    /* renamed from: com.google.zxing.oned.Code128Writer$1 */
    static /* synthetic */ class C40991 {

        /* renamed from: $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Charset */
        static final /* synthetic */ int[] f472x18b242a8;

        /* renamed from: $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Latch */
        static final /* synthetic */ int[] f473x56054aa0;

        /* JADX WARNING: Can't wrap try/catch for region: R(17:0|(2:1|2)|3|(2:5|6)|7|9|10|11|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Can't wrap try/catch for region: R(19:0|1|2|3|5|6|7|9|10|11|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0039 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0043 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x004d */
        static {
            /*
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset[] r0 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f472x18b242a8 = r0
                r1 = 1
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r2 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.f476A     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = f472x18b242a8     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.B     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = f472x18b242a8     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Charset r4 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Charset.C     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch[] r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                f473x56054aa0 = r3
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch r4 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.f479A     // Catch:{ NoSuchFieldError -> 0x0039 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0039 }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x0039 }
            L_0x0039:
                int[] r1 = f473x56054aa0     // Catch:{ NoSuchFieldError -> 0x0043 }
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch r3 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.B     // Catch:{ NoSuchFieldError -> 0x0043 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0043 }
                r1[r3] = r0     // Catch:{ NoSuchFieldError -> 0x0043 }
            L_0x0043:
                int[] r0 = f473x56054aa0     // Catch:{ NoSuchFieldError -> 0x004d }
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch r1 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.C     // Catch:{ NoSuchFieldError -> 0x004d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004d }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004d }
            L_0x004d:
                int[] r0 = f473x56054aa0     // Catch:{ NoSuchFieldError -> 0x0058 }
                com.google.zxing.oned.Code128Writer$MinimalEncoder$Latch r1 = com.google.zxing.oned.Code128Writer.MinimalEncoder.Latch.SHIFT     // Catch:{ NoSuchFieldError -> 0x0058 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0058 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0058 }
            L_0x0058:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.Code128Writer.C40991.<clinit>():void");
        }
    }
}

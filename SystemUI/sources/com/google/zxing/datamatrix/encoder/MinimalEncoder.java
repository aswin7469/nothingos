package com.google.zxing.datamatrix.encoder;

import android.icu.lang.UCharacter;
import android.net.TrafficStats;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.google.zxing.common.MinimalECIInput;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import kotlin.text.Typography;

public final class MinimalEncoder {
    static final char[] C40_SHIFT2_CHARS = {'!', Typography.quote, '#', '$', '%', Typography.amp, '\'', '(', ')', '*', '+', ',', '-', '.', '/', AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR, ';', Typography.less, '=', Typography.greater, '?', '@', '[', '\\', ']', '^', '_'};

    enum Mode {
        ASCII,
        C40,
        TEXT,
        X12,
        EDF,
        B256
    }

    static boolean isExtendedASCII(char c, int i) {
        return c != i && c >= 128 && c <= 255;
    }

    /* access modifiers changed from: private */
    public static boolean isInC40Shift1Set(char c) {
        return c <= 31;
    }

    private MinimalEncoder() {
    }

    /* access modifiers changed from: private */
    public static boolean isInC40Shift2Set(char c, int i) {
        for (char c2 : C40_SHIFT2_CHARS) {
            if (c2 == c) {
                return true;
            }
        }
        return c == i;
    }

    /* access modifiers changed from: private */
    public static boolean isInTextShift1Set(char c) {
        return isInC40Shift1Set(c);
    }

    /* access modifiers changed from: private */
    public static boolean isInTextShift2Set(char c, int i) {
        return isInC40Shift2Set(c, i);
    }

    public static String encodeHighLevel(String str) {
        return encodeHighLevel(str, (Charset) null, -1, SymbolShapeHint.FORCE_NONE);
    }

    public static String encodeHighLevel(String str, Charset charset, int i, SymbolShapeHint symbolShapeHint) {
        int i2;
        if (str.startsWith("[)>\u001e05\u001d") && str.endsWith("\u001e\u0004")) {
            str = str.substring(7, str.length() - 2);
            i2 = 5;
        } else if (!str.startsWith("[)>\u001e06\u001d") || !str.endsWith("\u001e\u0004")) {
            i2 = 0;
        } else {
            str = str.substring(7, str.length() - 2);
            i2 = 6;
        }
        return new String(encode(str, charset, i, symbolShapeHint, i2), StandardCharsets.ISO_8859_1);
    }

    static byte[] encode(String str, Charset charset, int i, SymbolShapeHint symbolShapeHint, int i2) {
        return encodeMinimally(new Input(str, charset, i, symbolShapeHint, i2, (C41061) null)).getBytes();
    }

    static void addEdge(Edge[][] edgeArr, Edge edge) {
        int access$100 = edge.fromPosition + edge.characterLength;
        if (edgeArr[access$100][edge.getEndMode().ordinal()] == null || edgeArr[access$100][edge.getEndMode().ordinal()].cachedTotalSize > edge.cachedTotalSize) {
            edgeArr[access$100][edge.getEndMode().ordinal()] = edge;
        }
    }

    static int getNumberOfC40Words(Input input, int i, boolean z, int[] iArr) {
        int i2 = 0;
        for (int i3 = i; i3 < input.length(); i3++) {
            if (input.isECI(i3)) {
                iArr[0] = 0;
                return 0;
            }
            char charAt = input.charAt(i3);
            if ((z && HighLevelEncoder.isNativeC40(charAt)) || (!z && HighLevelEncoder.isNativeText(charAt))) {
                i2++;
            } else if (!isExtendedASCII(charAt, input.getFNC1Character())) {
                i2 += 2;
            } else {
                char c = charAt & 255;
                i2 = (c < 128 || ((!z || !HighLevelEncoder.isNativeC40((char) (c + 65408))) && (z || !HighLevelEncoder.isNativeText((char) (c + 65408))))) ? i2 + 4 : i2 + 3;
            }
            if (i2 % 3 == 0 || ((i2 - 2) % 3 == 0 && i3 + 1 == input.length())) {
                iArr[0] = (i3 - i) + 1;
                return (int) Math.ceil(((double) i2) / 3.0d);
            }
        }
        iArr[0] = 0;
        return 0;
    }

    /* JADX WARNING: type inference failed for: r11v0 */
    /* JADX WARNING: type inference failed for: r11v4 */
    /* JADX WARNING: type inference failed for: r11v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void addEdges(com.google.zxing.datamatrix.encoder.MinimalEncoder.Input r17, com.google.zxing.datamatrix.encoder.MinimalEncoder.Edge[][] r18, int r19, com.google.zxing.datamatrix.encoder.MinimalEncoder.Edge r20) {
        /*
            r7 = r17
            r8 = r18
            r9 = r19
            boolean r0 = r7.isECI(r9)
            if (r0 == 0) goto L_0x0020
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge r10 = new com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.ASCII
            r4 = 1
            r6 = 0
            r0 = r10
            r1 = r17
            r3 = r19
            r5 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6)
            addEdge(r8, r10)
            return
        L_0x0020:
            char r0 = r7.charAt(r9)
            r10 = 3
            r11 = 0
            r12 = 1
            if (r20 == 0) goto L_0x0031
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r1 = r20.getEndMode()
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.EDF
            if (r1 == r2) goto L_0x00f7
        L_0x0031:
            boolean r0 = com.google.zxing.datamatrix.encoder.HighLevelEncoder.isDigit(r0)
            r13 = 2
            if (r0 == 0) goto L_0x005e
            boolean r0 = r7.haveNCharacters(r9, r13)
            if (r0 == 0) goto L_0x005e
            int r0 = r9 + 1
            char r0 = r7.charAt(r0)
            boolean r0 = com.google.zxing.datamatrix.encoder.HighLevelEncoder.isDigit(r0)
            if (r0 == 0) goto L_0x005e
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge r14 = new com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.ASCII
            r4 = 2
            r6 = 0
            r0 = r14
            r1 = r17
            r3 = r19
            r5 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6)
            addEdge(r8, r14)
            goto L_0x0071
        L_0x005e:
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge r14 = new com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.ASCII
            r4 = 1
            r6 = 0
            r0 = r14
            r1 = r17
            r3 = r19
            r5 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6)
            addEdge(r8, r14)
        L_0x0071:
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode[] r14 = new com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode[r13]
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r0 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.C40
            r14[r11] = r0
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r0 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.TEXT
            r14[r12] = r0
            r15 = r11
        L_0x007c:
            if (r15 >= r13) goto L_0x00a9
            r2 = r14[r15]
            int[] r0 = new int[r12]
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r1 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.C40
            if (r2 != r1) goto L_0x0088
            r1 = r12
            goto L_0x0089
        L_0x0088:
            r1 = r11
        L_0x0089:
            int r1 = getNumberOfC40Words(r7, r9, r1, r0)
            if (r1 <= 0) goto L_0x00a5
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge r6 = new com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge
            r4 = r0[r11]
            r16 = 0
            r0 = r6
            r1 = r17
            r3 = r19
            r5 = r20
            r11 = r6
            r6 = r16
            r0.<init>(r1, r2, r3, r4, r5, r6)
            addEdge(r8, r11)
        L_0x00a5:
            int r15 = r15 + 1
            r11 = 0
            goto L_0x007c
        L_0x00a9:
            boolean r0 = r7.haveNCharacters(r9, r10)
            if (r0 == 0) goto L_0x00e4
            char r0 = r7.charAt(r9)
            boolean r0 = com.google.zxing.datamatrix.encoder.HighLevelEncoder.isNativeX12(r0)
            if (r0 == 0) goto L_0x00e4
            int r0 = r9 + 1
            char r0 = r7.charAt(r0)
            boolean r0 = com.google.zxing.datamatrix.encoder.HighLevelEncoder.isNativeX12(r0)
            if (r0 == 0) goto L_0x00e4
            int r0 = r9 + 2
            char r0 = r7.charAt(r0)
            boolean r0 = com.google.zxing.datamatrix.encoder.HighLevelEncoder.isNativeX12(r0)
            if (r0 == 0) goto L_0x00e4
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge r11 = new com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12
            r4 = 3
            r6 = 0
            r0 = r11
            r1 = r17
            r3 = r19
            r5 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6)
            addEdge(r8, r11)
        L_0x00e4:
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge r11 = new com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.B256
            r4 = 1
            r6 = 0
            r0 = r11
            r1 = r17
            r3 = r19
            r5 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6)
            addEdge(r8, r11)
        L_0x00f7:
            r11 = 0
        L_0x00f8:
            if (r11 >= r10) goto L_0x0122
            int r0 = r9 + r11
            boolean r1 = r7.haveNCharacters(r0, r12)
            if (r1 == 0) goto L_0x0122
            char r0 = r7.charAt(r0)
            boolean r0 = com.google.zxing.datamatrix.encoder.HighLevelEncoder.isNativeEDIFACT(r0)
            if (r0 == 0) goto L_0x0122
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge r13 = new com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.EDF
            int r11 = r11 + 1
            r6 = 0
            r0 = r13
            r1 = r17
            r3 = r19
            r4 = r11
            r5 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6)
            addEdge(r8, r13)
            goto L_0x00f8
        L_0x0122:
            if (r11 != r10) goto L_0x014a
            r0 = 4
            boolean r0 = r7.haveNCharacters(r9, r0)
            if (r0 == 0) goto L_0x014a
            int r0 = r9 + 3
            char r0 = r7.charAt(r0)
            boolean r0 = com.google.zxing.datamatrix.encoder.HighLevelEncoder.isNativeEDIFACT(r0)
            if (r0 == 0) goto L_0x014a
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge r10 = new com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge
            com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.EDF
            r4 = 4
            r6 = 0
            r0 = r10
            r1 = r17
            r3 = r19
            r5 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6)
            addEdge(r8, r10)
        L_0x014a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.datamatrix.encoder.MinimalEncoder.addEdges(com.google.zxing.datamatrix.encoder.MinimalEncoder$Input, com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge[][], int, com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge):void");
    }

    static Result encodeMinimally(Input input) {
        int length = input.length();
        int[] iArr = new int[2];
        iArr[1] = 6;
        int i = 0;
        iArr[0] = length + 1;
        Edge[][] edgeArr = (Edge[][]) Array.newInstance((Class<?>) Edge.class, iArr);
        addEdges(input, edgeArr, 0, (Edge) null);
        for (int i2 = 1; i2 <= length; i2++) {
            for (int i3 = 0; i3 < 6; i3++) {
                Edge edge = edgeArr[i2][i3];
                if (edge != null && i2 < length) {
                    addEdges(input, edgeArr, i2, edge);
                }
            }
            for (int i4 = 0; i4 < 6; i4++) {
                edgeArr[i2 - 1][i4] = null;
            }
        }
        int i5 = -1;
        int i6 = Integer.MAX_VALUE;
        while (i < 6) {
            Edge edge2 = edgeArr[length][i];
            if (edge2 != null) {
                int access$300 = (i < 1 || i > 3) ? edge2.cachedTotalSize : edge2.cachedTotalSize + 1;
                if (access$300 < i6) {
                    i5 = i;
                    i6 = access$300;
                }
            }
            i++;
        }
        if (i5 >= 0) {
            return new Result(edgeArr[length][i5]);
        }
        throw new RuntimeException("Internal error: failed to encode \"" + input + "\"");
    }

    private static final class Edge {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final int[] allCodewordCapacities = {3, 5, 8, 10, 12, 16, 18, 22, 30, 32, 36, 44, 49, 62, 86, 114, 144, 174, 204, 280, 368, 456, 576, 696, 816, 1050, 1304, 1558};
        private static final int[] rectangularCodewordCapacities = {5, 10, 16, 33, 32, 49};
        private static final int[] squareCodewordCapacities = {3, 5, 8, 12, 18, 22, 30, 36, 44, 62, 86, 114, 144, 174, 204, 280, 368, 456, 576, 696, 816, 1050, 1304, 1558};
        /* access modifiers changed from: private */
        public final int cachedTotalSize;
        /* access modifiers changed from: private */
        public final int characterLength;
        /* access modifiers changed from: private */
        public final int fromPosition;
        /* access modifiers changed from: private */
        public final Input input;
        /* access modifiers changed from: private */
        public final Mode mode;
        /* access modifiers changed from: private */
        public final Edge previous;

        static byte[] getBytes(int i) {
            return new byte[]{(byte) i};
        }

        static byte[] getBytes(int i, int i2) {
            return new byte[]{(byte) i, (byte) i2};
        }

        private static int getC40Value(boolean z, int i, char c, int i2) {
            if (c == i2) {
                return 27;
            }
            if (!z) {
                if (c != 0) {
                    if (i == 0 && c <= 3) {
                        return c - 1;
                    }
                    if (i == 1 && c <= 31) {
                        return c;
                    }
                    if (c == ' ') {
                        return 3;
                    }
                    if (c >= '!' && c <= '/') {
                        return c - '!';
                    }
                    if (c >= '0' && c <= '9') {
                        return c - ',';
                    }
                    if (c >= ':' && c <= '@') {
                        return c - '+';
                    }
                    if (c >= 'A' && c <= 'Z') {
                        return c - '@';
                    }
                    if (c >= '[' && c <= '_') {
                        return c - 'E';
                    }
                    if (c != '`') {
                        return (c < 'a' || c > 'z') ? (c < '{' || c > 127) ? c : c - '`' : c - 'S';
                    }
                }
                return 0;
            } else if (c <= 31) {
                return c;
            } else {
                if (c == ' ') {
                    return 3;
                }
                return c <= '/' ? c - '!' : c <= '9' ? c - ',' : c <= '@' ? c - '+' : c <= 'Z' ? c - '3' : c <= '_' ? c - 'E' : c <= 127 ? c - '`' : c;
            }
        }

        private static int getX12Value(char c) {
            if (c == 13) {
                return 0;
            }
            if (c == '*') {
                return 1;
            }
            if (c == '>') {
                return 2;
            }
            if (c == ' ') {
                return 3;
            }
            return (c < '0' || c > '9') ? (c < 'A' || c > 'Z') ? c : c - '3' : c - ',';
        }

        static {
            Class<MinimalEncoder> cls = MinimalEncoder.class;
        }

        /* synthetic */ Edge(Input input2, Mode mode2, int i, int i2, Edge edge, C41061 r6) {
            this(input2, mode2, i, i2, edge);
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x003a, code lost:
            if (r0 != com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12) goto L_0x00bb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x003c, code lost:
            r9 = r9 + 2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x006f, code lost:
            if (r0 == com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12) goto L_0x003c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0093, code lost:
            if (r0 != com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12) goto L_0x00bb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x00b8, code lost:
            if (r0 != com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12) goto L_0x00bb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x00bb, code lost:
            r4.cachedTotalSize = r9;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x00bd, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private Edge(com.google.zxing.datamatrix.encoder.MinimalEncoder.Input r5, com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode r6, int r7, int r8, com.google.zxing.datamatrix.encoder.MinimalEncoder.Edge r9) {
            /*
                r4 = this;
                r4.<init>()
                r4.input = r5
                r4.mode = r6
                r4.fromPosition = r7
                r4.characterLength = r8
                r4.previous = r9
                r8 = 0
                if (r9 == 0) goto L_0x0013
                int r9 = r9.cachedTotalSize
                goto L_0x0014
            L_0x0013:
                r9 = r8
            L_0x0014:
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r0 = r4.getPreviousMode()
                int[] r1 = com.google.zxing.datamatrix.encoder.MinimalEncoder.C41061.f466x93f64673
                int r2 = r6.ordinal()
                r1 = r1[r2]
                switch(r1) {
                    case 1: goto L_0x0096;
                    case 2: goto L_0x0072;
                    case 3: goto L_0x0044;
                    case 4: goto L_0x0044;
                    case 5: goto L_0x0044;
                    case 6: goto L_0x0025;
                    default: goto L_0x0023;
                }
            L_0x0023:
                goto L_0x00bb
            L_0x0025:
                int r9 = r9 + 3
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.ASCII
                if (r0 == r5) goto L_0x0040
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.B256
                if (r0 != r5) goto L_0x0030
                goto L_0x0040
            L_0x0030:
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.C40
                if (r0 == r5) goto L_0x003c
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.TEXT
                if (r0 == r5) goto L_0x003c
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12
                if (r0 != r5) goto L_0x00bb
            L_0x003c:
                int r9 = r9 + 2
                goto L_0x00bb
            L_0x0040:
                int r9 = r9 + 1
                goto L_0x00bb
            L_0x0044:
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r1 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12
                if (r6 != r1) goto L_0x004b
                int r9 = r9 + 2
                goto L_0x005a
            L_0x004b:
                r1 = 1
                int[] r2 = new int[r1]
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r3 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.C40
                if (r6 != r3) goto L_0x0053
                r8 = r1
            L_0x0053:
                int r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.getNumberOfC40Words(r5, r7, r8, r2)
                int r5 = r5 * 2
                int r9 = r9 + r5
            L_0x005a:
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.ASCII
                if (r0 == r5) goto L_0x0040
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.B256
                if (r0 != r5) goto L_0x0063
                goto L_0x0040
            L_0x0063:
                if (r0 == r6) goto L_0x00bb
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.C40
                if (r0 == r5) goto L_0x003c
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.TEXT
                if (r0 == r5) goto L_0x003c
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12
                if (r0 != r5) goto L_0x00bb
                goto L_0x0095
            L_0x0072:
                int r9 = r9 + 1
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.B256
                if (r0 == r5) goto L_0x007b
            L_0x0078:
                int r9 = r9 + 1
                goto L_0x0084
            L_0x007b:
                int r5 = r4.getB256Size()
                r6 = 250(0xfa, float:3.5E-43)
                if (r5 != r6) goto L_0x0084
                goto L_0x0078
            L_0x0084:
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.ASCII
                if (r0 != r5) goto L_0x0089
                goto L_0x0040
            L_0x0089:
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.C40
                if (r0 == r5) goto L_0x003c
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.TEXT
                if (r0 == r5) goto L_0x003c
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12
                if (r0 != r5) goto L_0x00bb
            L_0x0095:
                goto L_0x003c
            L_0x0096:
                int r9 = r9 + 1
                boolean r6 = r5.isECI(r7)
                if (r6 != 0) goto L_0x00ac
                char r6 = r5.charAt(r7)
                int r5 = r5.getFNC1Character()
                boolean r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.isExtendedASCII(r6, r5)
                if (r5 == 0) goto L_0x00ae
            L_0x00ac:
                int r9 = r9 + 1
            L_0x00ae:
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.C40
                if (r0 == r5) goto L_0x0040
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.TEXT
                if (r0 == r5) goto L_0x0040
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r5 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12
                if (r0 != r5) goto L_0x00bb
                goto L_0x0040
            L_0x00bb:
                r4.cachedTotalSize = r9
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.datamatrix.encoder.MinimalEncoder.Edge.<init>(com.google.zxing.datamatrix.encoder.MinimalEncoder$Input, com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode, int, int, com.google.zxing.datamatrix.encoder.MinimalEncoder$Edge):void");
        }

        /* access modifiers changed from: package-private */
        public int getB256Size() {
            int i = 0;
            while (this != null && this.mode == Mode.B256 && i <= 250) {
                i++;
                this = this.previous;
            }
            return i;
        }

        /* access modifiers changed from: package-private */
        public Mode getPreviousStartMode() {
            Edge edge = this.previous;
            return edge == null ? Mode.ASCII : edge.mode;
        }

        /* access modifiers changed from: package-private */
        public Mode getPreviousMode() {
            Edge edge = this.previous;
            return edge == null ? Mode.ASCII : edge.getEndMode();
        }

        /* access modifiers changed from: package-private */
        public Mode getEndMode() {
            if (this.mode == Mode.EDF) {
                if (this.characterLength < 4) {
                    return Mode.ASCII;
                }
                int lastASCII = getLastASCII();
                if (lastASCII > 0 && getCodewordsRemaining(this.cachedTotalSize + lastASCII) <= 2 - lastASCII) {
                    return Mode.ASCII;
                }
            }
            if (this.mode == Mode.C40 || this.mode == Mode.TEXT || this.mode == Mode.X12) {
                if (this.fromPosition + this.characterLength >= this.input.length() && getCodewordsRemaining(this.cachedTotalSize) == 0) {
                    return Mode.ASCII;
                }
                if (getLastASCII() == 1 && getCodewordsRemaining(this.cachedTotalSize + 1) == 0) {
                    return Mode.ASCII;
                }
            }
            return this.mode;
        }

        /* access modifiers changed from: package-private */
        public Mode getMode() {
            return this.mode;
        }

        /* access modifiers changed from: package-private */
        public int getLastASCII() {
            int length = this.input.length();
            int i = this.fromPosition + this.characterLength;
            int i2 = length - i;
            if (i2 <= 4 && i < length) {
                if (i2 == 1) {
                    return MinimalEncoder.isExtendedASCII(this.input.charAt(i), this.input.getFNC1Character()) ? 0 : 1;
                }
                if (i2 == 2) {
                    if (!MinimalEncoder.isExtendedASCII(this.input.charAt(i), this.input.getFNC1Character())) {
                        int i3 = i + 1;
                        if (!MinimalEncoder.isExtendedASCII(this.input.charAt(i3), this.input.getFNC1Character())) {
                            if (!HighLevelEncoder.isDigit(this.input.charAt(i)) || !HighLevelEncoder.isDigit(this.input.charAt(i3))) {
                                return 2;
                            }
                            return 1;
                        }
                    }
                    return 0;
                } else if (i2 == 3) {
                    if (HighLevelEncoder.isDigit(this.input.charAt(i)) && HighLevelEncoder.isDigit(this.input.charAt(i + 1)) && !MinimalEncoder.isExtendedASCII(this.input.charAt(i + 2), this.input.getFNC1Character())) {
                        return 2;
                    }
                    if (!HighLevelEncoder.isDigit(this.input.charAt(i + 1)) || !HighLevelEncoder.isDigit(this.input.charAt(i + 2)) || MinimalEncoder.isExtendedASCII(this.input.charAt(i), this.input.getFNC1Character())) {
                        return 0;
                    }
                    return 2;
                } else if (!HighLevelEncoder.isDigit(this.input.charAt(i)) || !HighLevelEncoder.isDigit(this.input.charAt(i + 1)) || !HighLevelEncoder.isDigit(this.input.charAt(i + 2)) || !HighLevelEncoder.isDigit(this.input.charAt(i + 3))) {
                    return 0;
                } else {
                    return 2;
                }
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public int getMinSymbolSize(int i) {
            int i2 = C41061.$SwitchMap$com$google$zxing$datamatrix$encoder$SymbolShapeHint[this.input.getShapeHint().ordinal()];
            if (i2 == 1) {
                for (int i3 : squareCodewordCapacities) {
                    if (i3 >= i) {
                        return i3;
                    }
                }
            } else if (i2 == 2) {
                for (int i4 : rectangularCodewordCapacities) {
                    if (i4 >= i) {
                        return i4;
                    }
                }
            }
            for (int i5 : allCodewordCapacities) {
                if (i5 >= i) {
                    return i5;
                }
            }
            int[] iArr = allCodewordCapacities;
            return iArr[iArr.length - 1];
        }

        /* access modifiers changed from: package-private */
        public int getCodewordsRemaining(int i) {
            return getMinSymbolSize(i) - i;
        }

        static void setC40Word(byte[] bArr, int i, int i2, int i3, int i4) {
            int i5 = ((i2 & 255) * 1600) + ((i3 & 255) * 40) + (i4 & 255) + 1;
            bArr[i] = (byte) (i5 / 256);
            bArr[i + 1] = (byte) (i5 % 256);
        }

        /* access modifiers changed from: package-private */
        public byte[] getX12Words() {
            int i = (this.characterLength / 3) * 2;
            byte[] bArr = new byte[i];
            for (int i2 = 0; i2 < i; i2 += 2) {
                int i3 = (i2 / 2) * 3;
                setC40Word(bArr, i2, getX12Value(this.input.charAt(this.fromPosition + i3)), getX12Value(this.input.charAt(this.fromPosition + i3 + 1)), getX12Value(this.input.charAt(this.fromPosition + i3 + 2)));
            }
            return bArr;
        }

        static int getShiftValue(char c, boolean z, int i) {
            if ((!z || !MinimalEncoder.isInC40Shift1Set(c)) && (z || !MinimalEncoder.isInTextShift1Set(c))) {
                return ((!z || !MinimalEncoder.isInC40Shift2Set(c, i)) && (z || !MinimalEncoder.isInTextShift2Set(c, i))) ? 2 : 1;
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public byte[] getC40Words(boolean z, int i) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < this.characterLength; i2++) {
                char charAt = this.input.charAt(this.fromPosition + i2);
                if ((z && HighLevelEncoder.isNativeC40(charAt)) || (!z && HighLevelEncoder.isNativeText(charAt))) {
                    arrayList.add(Byte.valueOf((byte) getC40Value(z, 0, charAt, i)));
                } else if (!MinimalEncoder.isExtendedASCII(charAt, i)) {
                    int shiftValue = getShiftValue(charAt, z, i);
                    arrayList.add(Byte.valueOf((byte) shiftValue));
                    arrayList.add(Byte.valueOf((byte) getC40Value(z, shiftValue, charAt, i)));
                } else {
                    char c = (char) ((charAt & 255) - 128);
                    if ((!z || !HighLevelEncoder.isNativeC40(c)) && (z || !HighLevelEncoder.isNativeText(c))) {
                        arrayList.add((byte) 1);
                        arrayList.add((byte) 30);
                        int shiftValue2 = getShiftValue(c, z, i);
                        arrayList.add(Byte.valueOf((byte) shiftValue2));
                        arrayList.add(Byte.valueOf((byte) getC40Value(z, shiftValue2, c, i)));
                    } else {
                        arrayList.add((byte) 1);
                        arrayList.add((byte) 30);
                        arrayList.add(Byte.valueOf((byte) getC40Value(z, 0, c, i)));
                    }
                }
            }
            if (arrayList.size() % 3 != 0) {
                arrayList.add((byte) 0);
            }
            byte[] bArr = new byte[((arrayList.size() / 3) * 2)];
            int i3 = 0;
            for (int i4 = 0; i4 < arrayList.size(); i4 += 3) {
                setC40Word(bArr, i3, ((Byte) arrayList.get(i4)).byteValue() & 255, ((Byte) arrayList.get(i4 + 1)).byteValue() & 255, ((Byte) arrayList.get(i4 + 2)).byteValue() & 255);
                i3 += 2;
            }
            return bArr;
        }

        /* access modifiers changed from: package-private */
        public byte[] getEDFBytes() {
            int ceil = (int) Math.ceil(((double) this.characterLength) / 4.0d);
            byte[] bArr = new byte[(ceil * 3)];
            int i = this.fromPosition;
            int min = Math.min((this.characterLength + i) - 1, this.input.length() - 1);
            for (int i2 = 0; i2 < ceil; i2 += 3) {
                int[] iArr = new int[4];
                for (int i3 = 0; i3 < 4; i3++) {
                    if (i <= min) {
                        iArr[i3] = this.input.charAt(i) & '?';
                        i++;
                    } else {
                        iArr[i3] = i == min + 1 ? 31 : 0;
                    }
                }
                int i4 = (iArr[0] << 18) | (iArr[1] << 12) | (iArr[2] << 6) | iArr[3];
                bArr[i2] = (byte) ((i4 >> 16) & 255);
                bArr[i2 + 1] = (byte) ((i4 >> 8) & 255);
                bArr[i2 + 2] = (byte) (i4 & 255);
            }
            return bArr;
        }

        /* access modifiers changed from: package-private */
        public byte[] getLatchBytes() {
            switch (C41061.f466x93f64673[getPreviousMode().ordinal()]) {
                case 1:
                case 2:
                    int i = C41061.f466x93f64673[this.mode.ordinal()];
                    if (i == 2) {
                        return getBytes(UCharacter.UnicodeBlock.LATIN_EXTENDED_E_ID);
                    }
                    if (i == 3) {
                        return getBytes(UCharacter.UnicodeBlock.KHUDAWADI_ID);
                    }
                    if (i == 4) {
                        return getBytes(239);
                    }
                    if (i == 5) {
                        return getBytes(238);
                    }
                    if (i == 6) {
                        return getBytes(240);
                    }
                    break;
                case 3:
                case 4:
                case 5:
                    if (this.mode != getPreviousMode()) {
                        switch (C41061.f466x93f64673[this.mode.ordinal()]) {
                            case 1:
                                return getBytes(254);
                            case 2:
                                return getBytes(254, UCharacter.UnicodeBlock.LATIN_EXTENDED_E_ID);
                            case 3:
                                return getBytes(254, UCharacter.UnicodeBlock.KHUDAWADI_ID);
                            case 4:
                                return getBytes(254, 239);
                            case 5:
                                return getBytes(254, 238);
                            case 6:
                                return getBytes(254, 240);
                        }
                    }
                    break;
            }
            return new byte[0];
        }

        /* access modifiers changed from: package-private */
        public byte[] getDataBytes() {
            switch (C41061.f466x93f64673[this.mode.ordinal()]) {
                case 1:
                    if (this.input.isECI(this.fromPosition)) {
                        return getBytes(UCharacter.UnicodeBlock.OLD_PERMIC_ID, this.input.getECIValue(this.fromPosition) + 1);
                    }
                    if (MinimalEncoder.isExtendedASCII(this.input.charAt(this.fromPosition), this.input.getFNC1Character())) {
                        return getBytes(235, this.input.charAt(this.fromPosition) - 127);
                    }
                    if (this.characterLength == 2) {
                        return getBytes(((((this.input.charAt(this.fromPosition) - '0') * 10) + this.input.charAt(this.fromPosition + 1)) - 48) + 130);
                    }
                    if (this.input.isFNC1(this.fromPosition)) {
                        return getBytes(232);
                    }
                    return getBytes(this.input.charAt(this.fromPosition) + 1);
                case 2:
                    return getBytes(this.input.charAt(this.fromPosition));
                case 3:
                    return getC40Words(true, this.input.getFNC1Character());
                case 4:
                    return getC40Words(false, this.input.getFNC1Character());
                case 5:
                    return getX12Words();
                case 6:
                    return getEDFBytes();
                default:
                    return new byte[0];
            }
        }
    }

    /* renamed from: com.google.zxing.datamatrix.encoder.MinimalEncoder$1 */
    static /* synthetic */ class C41061 {

        /* renamed from: $SwitchMap$com$google$zxing$datamatrix$encoder$MinimalEncoder$Mode */
        static final /* synthetic */ int[] f466x93f64673;
        static final /* synthetic */ int[] $SwitchMap$com$google$zxing$datamatrix$encoder$SymbolShapeHint;

        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|(2:1|2)|3|(2:5|6)|7|9|10|11|12|13|14|15|16|17|18|(3:19|20|22)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(19:0|1|2|3|(2:5|6)|7|9|10|11|12|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0038 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0043 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0059 */
        static {
            /*
                com.google.zxing.datamatrix.encoder.SymbolShapeHint[] r0 = com.google.zxing.datamatrix.encoder.SymbolShapeHint.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$google$zxing$datamatrix$encoder$SymbolShapeHint = r0
                r1 = 1
                com.google.zxing.datamatrix.encoder.SymbolShapeHint r2 = com.google.zxing.datamatrix.encoder.SymbolShapeHint.FORCE_SQUARE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$com$google$zxing$datamatrix$encoder$SymbolShapeHint     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.zxing.datamatrix.encoder.SymbolShapeHint r3 = com.google.zxing.datamatrix.encoder.SymbolShapeHint.FORCE_RECTANGLE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode[] r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.values()
                int r2 = r2.length
                int[] r2 = new int[r2]
                f466x93f64673 = r2
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r3 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.ASCII     // Catch:{ NoSuchFieldError -> 0x002e }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x002e }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x002e }
            L_0x002e:
                int[] r1 = f466x93f64673     // Catch:{ NoSuchFieldError -> 0x0038 }
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r2 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.B256     // Catch:{ NoSuchFieldError -> 0x0038 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0038 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0038 }
            L_0x0038:
                int[] r0 = f466x93f64673     // Catch:{ NoSuchFieldError -> 0x0043 }
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r1 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.C40     // Catch:{ NoSuchFieldError -> 0x0043 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0043 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0043 }
            L_0x0043:
                int[] r0 = f466x93f64673     // Catch:{ NoSuchFieldError -> 0x004e }
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r1 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.TEXT     // Catch:{ NoSuchFieldError -> 0x004e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004e }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004e }
            L_0x004e:
                int[] r0 = f466x93f64673     // Catch:{ NoSuchFieldError -> 0x0059 }
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r1 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.X12     // Catch:{ NoSuchFieldError -> 0x0059 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0059 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0059 }
            L_0x0059:
                int[] r0 = f466x93f64673     // Catch:{ NoSuchFieldError -> 0x0064 }
                com.google.zxing.datamatrix.encoder.MinimalEncoder$Mode r1 = com.google.zxing.datamatrix.encoder.MinimalEncoder.Mode.EDF     // Catch:{ NoSuchFieldError -> 0x0064 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0064 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0064 }
            L_0x0064:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.datamatrix.encoder.MinimalEncoder.C41061.<clinit>():void");
        }
    }

    private static final class Result {
        private final byte[] bytes;

        Result(Edge edge) {
            int i;
            Input access$1000 = edge.input;
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            int i2 = 0;
            int prepend = ((edge.mode == Mode.C40 || edge.mode == Mode.TEXT || edge.mode == Mode.X12) && edge.getEndMode() != Mode.ASCII) ? prepend(Edge.getBytes(254), arrayList) + 0 : 0;
            for (Edge edge2 = edge; edge2 != null; edge2 = edge2.previous) {
                int prepend2 = prepend + prepend(edge2.getDataBytes(), arrayList);
                if (edge2.previous == null || edge2.getPreviousStartMode() != edge2.getMode()) {
                    if (edge2.getMode() == Mode.B256) {
                        if (prepend2 <= 249) {
                            arrayList.add(0, Byte.valueOf((byte) prepend2));
                            i = prepend2 + 1;
                        } else {
                            arrayList.add(0, Byte.valueOf((byte) (prepend2 % 250)));
                            arrayList.add(0, Byte.valueOf((byte) ((prepend2 / 250) + 249)));
                            i = prepend2 + 2;
                        }
                        arrayList2.add(Integer.valueOf(arrayList.size()));
                        arrayList3.add(Integer.valueOf(i));
                    }
                    prepend(edge2.getLatchBytes(), arrayList);
                    prepend2 = 0;
                }
            }
            if (access$1000.getMacroId() == 5) {
                prepend(Edge.getBytes(236), arrayList);
            } else if (access$1000.getMacroId() == 6) {
                prepend(Edge.getBytes(237), arrayList);
            }
            if (access$1000.getFNC1Character() > 0) {
                prepend(Edge.getBytes(232), arrayList);
            }
            for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                applyRandomPattern(arrayList, arrayList.size() - ((Integer) arrayList2.get(i3)).intValue(), ((Integer) arrayList3.get(i3)).intValue());
            }
            int minSymbolSize = edge.getMinSymbolSize(arrayList.size());
            if (arrayList.size() < minSymbolSize) {
                arrayList.add((byte) -127);
            }
            while (arrayList.size() < minSymbolSize) {
                arrayList.add(Byte.valueOf((byte) randomize253State(arrayList.size() + 1)));
            }
            this.bytes = new byte[arrayList.size()];
            while (true) {
                byte[] bArr = this.bytes;
                if (i2 < bArr.length) {
                    bArr[i2] = ((Byte) arrayList.get(i2)).byteValue();
                    i2++;
                } else {
                    return;
                }
            }
        }

        static int prepend(byte[] bArr, List<Byte> list) {
            for (int length = bArr.length - 1; length >= 0; length--) {
                list.add(0, Byte.valueOf(bArr[length]));
            }
            return bArr.length;
        }

        private static int randomize253State(int i) {
            int i2 = ((i * 149) % 253) + 1 + 129;
            return i2 <= 254 ? i2 : i2 + TrafficStats.TAG_SYSTEM_MEDIA;
        }

        static void applyRandomPattern(List<Byte> list, int i, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                int i4 = i + i3;
                int byteValue = (list.get(i4).byteValue() & 255) + (((i4 + 1) * 149) % 255) + 1;
                if (byteValue > 255) {
                    byteValue -= 256;
                }
                list.set(i4, Byte.valueOf((byte) byteValue));
            }
        }

        public byte[] getBytes() {
            return this.bytes;
        }
    }

    private static final class Input extends MinimalECIInput {
        private final int macroId;
        private final SymbolShapeHint shape;

        /* synthetic */ Input(String str, Charset charset, int i, SymbolShapeHint symbolShapeHint, int i2, C41061 r6) {
            this(str, charset, i, symbolShapeHint, i2);
        }

        private Input(String str, Charset charset, int i, SymbolShapeHint symbolShapeHint, int i2) {
            super(str, charset, i);
            this.shape = symbolShapeHint;
            this.macroId = i2;
        }

        /* access modifiers changed from: private */
        public int getMacroId() {
            return this.macroId;
        }

        /* access modifiers changed from: private */
        public SymbolShapeHint getShapeHint() {
            return this.shape;
        }
    }
}

package com.google.zxing.qrcode.encoder;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.ECIEncoderSet;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

final class MinimalEncoder {
    /* access modifiers changed from: private */
    public final ErrorCorrectionLevel ecLevel;
    /* access modifiers changed from: private */
    public final ECIEncoderSet encoders;
    /* access modifiers changed from: private */
    public final boolean isGS1;
    /* access modifiers changed from: private */
    public final String stringToEncode;

    static boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    private enum VersionSize {
        SMALL("version 1-9"),
        MEDIUM("version 10-26"),
        LARGE("version 27-40");
        
        private final String description;

        private VersionSize(String str) {
            this.description = str;
        }

        public String toString() {
            return this.description;
        }
    }

    MinimalEncoder(String str, Charset charset, boolean z, ErrorCorrectionLevel errorCorrectionLevel) {
        this.stringToEncode = str;
        this.isGS1 = z;
        this.encoders = new ECIEncoderSet(str, charset, -1);
        this.ecLevel = errorCorrectionLevel;
    }

    static ResultList encode(String str, Version version, Charset charset, boolean z, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException {
        return new MinimalEncoder(str, charset, z, errorCorrectionLevel).encode(version);
    }

    /* access modifiers changed from: package-private */
    public ResultList encode(Version version) throws WriterException {
        if (version == null) {
            Version[] versionArr = {getVersion(VersionSize.SMALL), getVersion(VersionSize.MEDIUM), getVersion(VersionSize.LARGE)};
            ResultList[] resultListArr = {encodeSpecificVersion(versionArr[0]), encodeSpecificVersion(versionArr[1]), encodeSpecificVersion(versionArr[2])};
            int i = Integer.MAX_VALUE;
            int i2 = -1;
            for (int i3 = 0; i3 < 3; i3++) {
                int size = resultListArr[i3].getSize();
                if (Encoder.willFit(size, versionArr[i3], this.ecLevel) && size < i) {
                    i2 = i3;
                    i = size;
                }
            }
            if (i2 >= 0) {
                return resultListArr[i2];
            }
            throw new WriterException("Data too big for any version");
        }
        ResultList encodeSpecificVersion = encodeSpecificVersion(version);
        if (Encoder.willFit(encodeSpecificVersion.getSize(), getVersion(getVersionSize(encodeSpecificVersion.getVersion())), this.ecLevel)) {
            return encodeSpecificVersion;
        }
        throw new WriterException("Data too big for version" + version);
    }

    static VersionSize getVersionSize(Version version) {
        if (version.getVersionNumber() <= 9) {
            return VersionSize.SMALL;
        }
        return version.getVersionNumber() <= 26 ? VersionSize.MEDIUM : VersionSize.LARGE;
    }

    static Version getVersion(VersionSize versionSize) {
        int i = C41231.f501x9d14eba6[versionSize.ordinal()];
        if (i == 1) {
            return Version.getVersionForNumber(9);
        }
        if (i != 2) {
            return Version.getVersionForNumber(40);
        }
        return Version.getVersionForNumber(26);
    }

    static boolean isDoubleByteKanji(char c) {
        return Encoder.isOnlyDoubleByteKanji(String.valueOf(c));
    }

    static boolean isAlphanumeric(char c) {
        return Encoder.getAlphanumericCode(c) != -1;
    }

    /* renamed from: com.google.zxing.qrcode.encoder.MinimalEncoder$1 */
    static /* synthetic */ class C41231 {
        static final /* synthetic */ int[] $SwitchMap$com$google$zxing$qrcode$decoder$Mode;

        /* renamed from: $SwitchMap$com$google$zxing$qrcode$encoder$MinimalEncoder$VersionSize */
        static final /* synthetic */ int[] f501x9d14eba6;

        /* JADX WARNING: Can't wrap try/catch for region: R(17:0|(2:1|2)|3|(2:5|6)|7|9|10|11|12|13|14|15|17|18|19|20|(3:21|22|24)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|(2:1|2)|3|5|6|7|9|10|11|12|13|14|15|17|18|19|20|(3:21|22|24)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(21:0|1|2|3|5|6|7|9|10|11|12|13|14|15|17|18|19|20|21|22|24) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0033 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x004f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0059 */
        static {
            /*
                com.google.zxing.qrcode.decoder.Mode[] r0 = com.google.zxing.qrcode.decoder.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$google$zxing$qrcode$decoder$Mode = r0
                r1 = 1
                com.google.zxing.qrcode.decoder.Mode r2 = com.google.zxing.qrcode.decoder.Mode.KANJI     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.zxing.qrcode.decoder.Mode r3 = com.google.zxing.qrcode.decoder.Mode.ALPHANUMERIC     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.google.zxing.qrcode.decoder.Mode r4 = com.google.zxing.qrcode.decoder.Mode.NUMERIC     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r3 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.google.zxing.qrcode.decoder.Mode r4 = com.google.zxing.qrcode.decoder.Mode.BYTE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r5 = 4
                r3[r4] = r5     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r3 = $SwitchMap$com$google$zxing$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x003e }
                com.google.zxing.qrcode.decoder.Mode r4 = com.google.zxing.qrcode.decoder.Mode.ECI     // Catch:{ NoSuchFieldError -> 0x003e }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r5 = 5
                r3[r4] = r5     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                com.google.zxing.qrcode.encoder.MinimalEncoder$VersionSize[] r3 = com.google.zxing.qrcode.encoder.MinimalEncoder.VersionSize.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                f501x9d14eba6 = r3
                com.google.zxing.qrcode.encoder.MinimalEncoder$VersionSize r4 = com.google.zxing.qrcode.encoder.MinimalEncoder.VersionSize.SMALL     // Catch:{ NoSuchFieldError -> 0x004f }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x004f }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x004f }
            L_0x004f:
                int[] r1 = f501x9d14eba6     // Catch:{ NoSuchFieldError -> 0x0059 }
                com.google.zxing.qrcode.encoder.MinimalEncoder$VersionSize r3 = com.google.zxing.qrcode.encoder.MinimalEncoder.VersionSize.MEDIUM     // Catch:{ NoSuchFieldError -> 0x0059 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0059 }
                r1[r3] = r0     // Catch:{ NoSuchFieldError -> 0x0059 }
            L_0x0059:
                int[] r0 = f501x9d14eba6     // Catch:{ NoSuchFieldError -> 0x0063 }
                com.google.zxing.qrcode.encoder.MinimalEncoder$VersionSize r1 = com.google.zxing.qrcode.encoder.MinimalEncoder.VersionSize.LARGE     // Catch:{ NoSuchFieldError -> 0x0063 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0063 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0063 }
            L_0x0063:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.qrcode.encoder.MinimalEncoder.C41231.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean canEncode(Mode mode, char c) {
        int i = C41231.$SwitchMap$com$google$zxing$qrcode$decoder$Mode[mode.ordinal()];
        if (i == 1) {
            return isDoubleByteKanji(c);
        }
        if (i == 2) {
            return isAlphanumeric(c);
        }
        if (i != 3) {
            return i == 4;
        }
        return isNumeric(c);
    }

    static int getCompactedOrdinal(Mode mode) {
        int i;
        if (mode == null || (i = C41231.$SwitchMap$com$google$zxing$qrcode$decoder$Mode[mode.ordinal()]) == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3) {
            return 2;
        }
        if (i == 4) {
            return 3;
        }
        throw new IllegalStateException("Illegal mode " + mode);
    }

    /* access modifiers changed from: package-private */
    public void addEdge(Edge[][][] edgeArr, int i, Edge edge) {
        Edge[] edgeArr2 = edgeArr[i + edge.characterLength][edge.charsetEncoderIndex];
        int compactedOrdinal = getCompactedOrdinal(edge.mode);
        Edge edge2 = edgeArr2[compactedOrdinal];
        if (edge2 == null || edge2.cachedTotalSize > edge.cachedTotalSize) {
            edgeArr2[compactedOrdinal] = edge;
        }
    }

    /* access modifiers changed from: package-private */
    public void addEdges(Version version, Edge[][][] edgeArr, int i, Edge edge) {
        int i2;
        Edge[][][] edgeArr2 = edgeArr;
        int i3 = i;
        int length = this.encoders.length();
        int priorityEncoderIndex = this.encoders.getPriorityEncoderIndex();
        if (priorityEncoderIndex < 0 || !this.encoders.canEncode(this.stringToEncode.charAt(i3), priorityEncoderIndex)) {
            priorityEncoderIndex = 0;
        } else {
            length = priorityEncoderIndex + 1;
        }
        int i4 = length;
        for (int i5 = priorityEncoderIndex; i5 < i4; i5++) {
            if (this.encoders.canEncode(this.stringToEncode.charAt(i3), i5)) {
                addEdge(edgeArr2, i3, new Edge(this, Mode.BYTE, i, i5, 1, edge, version, (C41231) null));
            }
        }
        if (canEncode(Mode.KANJI, this.stringToEncode.charAt(i3))) {
            addEdge(edgeArr2, i3, new Edge(this, Mode.KANJI, i, 0, 1, edge, version, (C41231) null));
        }
        int length2 = this.stringToEncode.length();
        if (canEncode(Mode.ALPHANUMERIC, this.stringToEncode.charAt(i3))) {
            int i6 = i3 + 1;
            addEdge(edgeArr2, i3, new Edge(this, Mode.ALPHANUMERIC, i, 0, (i6 >= length2 || !canEncode(Mode.ALPHANUMERIC, this.stringToEncode.charAt(i6))) ? 1 : 2, edge, version, (C41231) null));
        }
        if (canEncode(Mode.NUMERIC, this.stringToEncode.charAt(i3))) {
            Mode mode = Mode.NUMERIC;
            int i7 = i3 + 1;
            if (i7 >= length2 || !canEncode(Mode.NUMERIC, this.stringToEncode.charAt(i7))) {
                i2 = 1;
            } else {
                int i8 = i3 + 2;
                i2 = (i8 >= length2 || !canEncode(Mode.NUMERIC, this.stringToEncode.charAt(i8))) ? 2 : 3;
            }
            addEdge(edgeArr2, i3, new Edge(this, mode, i, 0, i2, edge, version, (C41231) null));
        }
    }

    /* access modifiers changed from: package-private */
    public ResultList encodeSpecificVersion(Version version) throws WriterException {
        int length = this.stringToEncode.length();
        int length2 = this.encoders.length();
        int[] iArr = new int[3];
        iArr[2] = 4;
        iArr[1] = length2;
        iArr[0] = length + 1;
        Edge[][][] edgeArr = (Edge[][][]) Array.newInstance((Class<?>) Edge.class, iArr);
        addEdges(version, edgeArr, 0, (Edge) null);
        for (int i = 1; i <= length; i++) {
            for (int i2 = 0; i2 < this.encoders.length(); i2++) {
                for (int i3 = 0; i3 < 4; i3++) {
                    Edge edge = edgeArr[i][i2][i3];
                    if (edge != null && i < length) {
                        addEdges(version, edgeArr, i, edge);
                    }
                }
            }
        }
        int i4 = -1;
        int i5 = Integer.MAX_VALUE;
        int i6 = -1;
        for (int i7 = 0; i7 < this.encoders.length(); i7++) {
            for (int i8 = 0; i8 < 4; i8++) {
                Edge edge2 = edgeArr[length][i7][i8];
                if (edge2 != null && edge2.cachedTotalSize < i5) {
                    i5 = edge2.cachedTotalSize;
                    i4 = i7;
                    i6 = i8;
                }
            }
        }
        if (i4 >= 0) {
            return new ResultList(version, edgeArr[length][i4][i6]);
        }
        throw new WriterException("Internal error: failed to encode \"" + this.stringToEncode + "\"");
    }

    private final class Edge {
        /* access modifiers changed from: private */
        public final int cachedTotalSize;
        /* access modifiers changed from: private */
        public final int characterLength;
        /* access modifiers changed from: private */
        public final int charsetEncoderIndex;
        /* access modifiers changed from: private */
        public final int fromPosition;
        /* access modifiers changed from: private */
        public final Mode mode;
        /* access modifiers changed from: private */
        public final Edge previous;

        /* synthetic */ Edge(MinimalEncoder minimalEncoder, Mode mode2, int i, int i2, int i3, Edge edge, Version version, C41231 r8) {
            this(mode2, i, i2, i3, edge, version);
        }

        private Edge(Mode mode2, int i, int i2, int i3, Edge edge, Version version) {
            this.mode = mode2;
            this.fromPosition = i;
            int i4 = (mode2 == Mode.BYTE || edge == null) ? i2 : edge.charsetEncoderIndex;
            this.charsetEncoderIndex = i4;
            this.characterLength = i3;
            this.previous = edge;
            boolean z = false;
            int i5 = edge != null ? edge.cachedTotalSize : 0;
            if ((mode2 == Mode.BYTE && edge == null && i4 != 0) || !(edge == null || i4 == edge.charsetEncoderIndex)) {
                z = true;
            }
            int i6 = 4;
            i5 = (edge == null || mode2 != edge.mode || z) ? i5 + mode2.getCharacterCountBits(version) + 4 : i5;
            int i7 = C41231.$SwitchMap$com$google$zxing$qrcode$decoder$Mode[mode2.ordinal()];
            if (i7 == 1) {
                i5 += 13;
            } else if (i7 == 2) {
                i5 += i3 == 1 ? 6 : 11;
            } else if (i7 == 3) {
                i5 += i3 != 1 ? i3 == 2 ? 7 : 10 : i6;
            } else if (i7 == 4) {
                i5 += MinimalEncoder.this.encoders.encode(MinimalEncoder.this.stringToEncode.substring(i, i3 + i), i2).length * 8;
                if (z) {
                    i5 += 12;
                }
            }
            this.cachedTotalSize = i5;
        }
    }

    final class ResultList {
        private final List<ResultNode> list = new ArrayList();
        /* access modifiers changed from: private */
        public final Version version;

        ResultList(Version version2, Edge edge) {
            int i;
            int i2;
            int i3 = 0;
            int i4 = 0;
            boolean z = false;
            while (true) {
                i = 1;
                if (edge == null) {
                    break;
                }
                int access$000 = i4 + edge.characterLength;
                Edge access$700 = edge.previous;
                boolean z2 = (edge.mode == Mode.BYTE && access$700 == null && edge.charsetEncoderIndex != 0) || !(access$700 == null || edge.charsetEncoderIndex == access$700.charsetEncoderIndex);
                z = z2 ? true : z;
                if (access$700 == null || access$700.mode != edge.mode || z2) {
                    this.list.add(0, new ResultNode(edge.mode, edge.fromPosition, edge.charsetEncoderIndex, access$000));
                    access$000 = 0;
                }
                if (z2) {
                    this.list.add(0, new ResultNode(Mode.ECI, edge.fromPosition, edge.charsetEncoderIndex, 0));
                }
                edge = access$700;
                i4 = access$000;
            }
            if (MinimalEncoder.this.isGS1) {
                ResultNode resultNode = this.list.get(0);
                if (!(resultNode == null || resultNode.mode == Mode.ECI || !z)) {
                    this.list.add(0, new ResultNode(Mode.ECI, 0, 0, 0));
                }
                this.list.add(this.list.get(0).mode == Mode.ECI ? 1 : i3, new ResultNode(Mode.FNC1_FIRST_POSITION, 0, 0, 0));
            }
            int versionNumber = version2.getVersionNumber();
            int i5 = C41231.f501x9d14eba6[MinimalEncoder.getVersionSize(version2).ordinal()];
            if (i5 == 1) {
                i2 = 9;
            } else if (i5 != 2) {
                i = 27;
                i2 = 40;
            } else {
                i = 10;
                i2 = 26;
            }
            int size = getSize(version2);
            while (versionNumber < i2 && !Encoder.willFit(size, Version.getVersionForNumber(versionNumber), MinimalEncoder.this.ecLevel)) {
                versionNumber++;
            }
            while (versionNumber > i && Encoder.willFit(size, Version.getVersionForNumber(versionNumber - 1), MinimalEncoder.this.ecLevel)) {
                versionNumber--;
            }
            this.version = Version.getVersionForNumber(versionNumber);
        }

        /* access modifiers changed from: package-private */
        public int getSize() {
            return getSize(this.version);
        }

        private int getSize(Version version2) {
            int i = 0;
            for (ResultNode access$1200 : this.list) {
                i += access$1200.getSize(version2);
            }
            return i;
        }

        /* access modifiers changed from: package-private */
        public void getBits(BitArray bitArray) throws WriterException {
            for (ResultNode access$1300 : this.list) {
                access$1300.getBits(bitArray);
            }
        }

        /* access modifiers changed from: package-private */
        public Version getVersion() {
            return this.version;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            ResultNode resultNode = null;
            for (ResultNode next : this.list) {
                if (resultNode != null) {
                    sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
                }
                sb.append(next.toString());
                resultNode = next;
            }
            return sb.toString();
        }

        final class ResultNode {
            private final int characterLength;
            private final int charsetEncoderIndex;
            private final int fromPosition;
            /* access modifiers changed from: private */
            public final Mode mode;

            ResultNode(Mode mode2, int i, int i2, int i3) {
                this.mode = mode2;
                this.fromPosition = i;
                this.charsetEncoderIndex = i2;
                this.characterLength = i3;
            }

            /* access modifiers changed from: private */
            public int getSize(Version version) {
                int i;
                int i2 = 4;
                int characterCountBits = this.mode.getCharacterCountBits(version) + 4;
                int i3 = C41231.$SwitchMap$com$google$zxing$qrcode$decoder$Mode[this.mode.ordinal()];
                if (i3 != 1) {
                    int i4 = 0;
                    if (i3 == 2) {
                        int i5 = this.characterLength;
                        int i6 = characterCountBits + ((i5 / 2) * 11);
                        if (i5 % 2 == 1) {
                            i4 = 6;
                        }
                        return i6 + i4;
                    } else if (i3 == 3) {
                        int i7 = this.characterLength;
                        int i8 = characterCountBits + ((i7 / 3) * 10);
                        int i9 = i7 % 3;
                        if (i9 != 1) {
                            i2 = i9 == 2 ? 7 : 0;
                        }
                        return i8 + i2;
                    } else if (i3 == 4) {
                        i = getCharacterCountIndicator() * 8;
                    } else if (i3 != 5) {
                        return characterCountBits;
                    } else {
                        return characterCountBits + 8;
                    }
                } else {
                    i = this.characterLength * 13;
                }
                return characterCountBits + i;
            }

            private int getCharacterCountIndicator() {
                if (this.mode != Mode.BYTE) {
                    return this.characterLength;
                }
                ECIEncoderSet access$600 = MinimalEncoder.this.encoders;
                String access$500 = MinimalEncoder.this.stringToEncode;
                int i = this.fromPosition;
                return access$600.encode(access$500.substring(i, this.characterLength + i), this.charsetEncoderIndex).length;
            }

            /* access modifiers changed from: private */
            public void getBits(BitArray bitArray) throws WriterException {
                bitArray.appendBits(this.mode.getBits(), 4);
                if (this.characterLength > 0) {
                    bitArray.appendBits(getCharacterCountIndicator(), this.mode.getCharacterCountBits(ResultList.this.version));
                }
                if (this.mode == Mode.ECI) {
                    bitArray.appendBits(MinimalEncoder.this.encoders.getECIValue(this.charsetEncoderIndex), 8);
                } else if (this.characterLength > 0) {
                    String access$500 = MinimalEncoder.this.stringToEncode;
                    int i = this.fromPosition;
                    Encoder.appendBytes(access$500.substring(i, this.characterLength + i), this.mode, bitArray, MinimalEncoder.this.encoders.getCharset(this.charsetEncoderIndex));
                }
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append((Object) this.mode).append('(');
                if (this.mode == Mode.ECI) {
                    sb.append(MinimalEncoder.this.encoders.getCharset(this.charsetEncoderIndex).displayName());
                } else {
                    String access$500 = MinimalEncoder.this.stringToEncode;
                    int i = this.fromPosition;
                    sb.append(makePrintable(access$500.substring(i, this.characterLength + i)));
                }
                sb.append(')');
                return sb.toString();
            }

            private String makePrintable(String str) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) < ' ' || str.charAt(i) > '~') {
                        sb.append('.');
                    } else {
                        sb.append(str.charAt(i));
                    }
                }
                return sb.toString();
            }
        }
    }
}

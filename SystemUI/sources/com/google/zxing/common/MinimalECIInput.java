package com.google.zxing.common;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MinimalECIInput implements ECIInput {
    private static final int COST_PER_ECI = 3;
    private final int[] bytes;
    private final int fnc1;

    public MinimalECIInput(String str, Charset charset, int i) {
        this.fnc1 = i;
        ECIEncoderSet eCIEncoderSet = new ECIEncoderSet(str, charset, i);
        if (eCIEncoderSet.length() == 1) {
            this.bytes = new int[str.length()];
            for (int i2 = 0; i2 < this.bytes.length; i2++) {
                int charAt = str.charAt(i2);
                int[] iArr = this.bytes;
                if (charAt == i) {
                    charAt = 1000;
                }
                iArr[i2] = charAt;
            }
            return;
        }
        this.bytes = encodeMinimally(str, eCIEncoderSet, i);
    }

    public int getFNC1Character() {
        return this.fnc1;
    }

    public int length() {
        return this.bytes.length;
    }

    public boolean haveNCharacters(int i, int i2) {
        if ((i + i2) - 1 >= this.bytes.length) {
            return false;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (isECI(i + i3)) {
                return false;
            }
        }
        return true;
    }

    public char charAt(int i) {
        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("" + i);
        } else if (!isECI(i)) {
            return (char) (isFNC1(i) ? this.fnc1 : this.bytes[i]);
        } else {
            throw new IllegalArgumentException("value at " + i + " is not a character but an ECI");
        }
    }

    public CharSequence subSequence(int i, int i2) {
        if (i < 0 || i > i2 || i2 > length()) {
            throw new IndexOutOfBoundsException("" + i);
        }
        StringBuilder sb = new StringBuilder();
        while (i < i2) {
            if (!isECI(i)) {
                sb.append(charAt(i));
                i++;
            } else {
                throw new IllegalArgumentException("value at " + i + " is not a character but an ECI");
            }
        }
        return sb;
    }

    public boolean isECI(int i) {
        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("" + i);
        }
        int i2 = this.bytes[i];
        return i2 > 255 && i2 <= 999;
    }

    public boolean isFNC1(int i) {
        if (i >= 0 && i < length()) {
            return this.bytes[i] == 1000;
        }
        throw new IndexOutOfBoundsException("" + i);
    }

    public int getECIValue(int i) {
        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("" + i);
        } else if (isECI(i)) {
            return this.bytes[i] - 256;
        } else {
            throw new IllegalArgumentException("value at " + i + " is not an ECI but a character");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            if (isECI(i)) {
                sb.append("ECI(");
                sb.append(getECIValue(i));
                sb.append(')');
            } else if (charAt(i) < 128) {
                sb.append('\'');
                sb.append(charAt(i));
                sb.append('\'');
            } else {
                sb.append((int) charAt(i));
            }
        }
        return sb.toString();
    }

    static void addEdge(InputEdge[][] inputEdgeArr, int i, InputEdge inputEdge) {
        if (inputEdgeArr[i][inputEdge.encoderIndex] == null || inputEdgeArr[i][inputEdge.encoderIndex].cachedTotalSize > inputEdge.cachedTotalSize) {
            inputEdgeArr[i][inputEdge.encoderIndex] = inputEdge;
        }
    }

    static void addEdges(String str, ECIEncoderSet eCIEncoderSet, InputEdge[][] inputEdgeArr, int i, InputEdge inputEdge, int i2) {
        int i3;
        int i4;
        ECIEncoderSet eCIEncoderSet2 = eCIEncoderSet;
        int i5 = i;
        String str2 = str;
        int i6 = i2;
        char charAt = str.charAt(i5);
        int length = eCIEncoderSet.length();
        if (eCIEncoderSet.getPriorityEncoderIndex() < 0 || (charAt != i6 && !eCIEncoderSet2.canEncode(charAt, eCIEncoderSet.getPriorityEncoderIndex()))) {
            i3 = length;
            i4 = 0;
        } else {
            i4 = eCIEncoderSet.getPriorityEncoderIndex();
            i3 = i4 + 1;
        }
        for (int i7 = i4; i7 < i3; i7++) {
            if (charAt == i6 || eCIEncoderSet2.canEncode(charAt, i7)) {
                addEdge(inputEdgeArr, i5 + 1, new InputEdge(charAt, eCIEncoderSet, i7, inputEdge, i2));
            } else {
                InputEdge[][] inputEdgeArr2 = inputEdgeArr;
            }
        }
    }

    static int[] encodeMinimally(String str, ECIEncoderSet eCIEncoderSet, int i) {
        int length = str.length();
        int[] iArr = new int[2];
        iArr[1] = eCIEncoderSet.length();
        iArr[0] = length + 1;
        InputEdge[][] inputEdgeArr = (InputEdge[][]) Array.newInstance((Class<?>) InputEdge.class, iArr);
        addEdges(str, eCIEncoderSet, inputEdgeArr, 0, (InputEdge) null, i);
        for (int i2 = 1; i2 <= length; i2++) {
            for (int i3 = 0; i3 < eCIEncoderSet.length(); i3++) {
                InputEdge inputEdge = inputEdgeArr[i2][i3];
                if (inputEdge != null && i2 < length) {
                    addEdges(str, eCIEncoderSet, inputEdgeArr, i2, inputEdge, i);
                }
            }
            for (int i4 = 0; i4 < eCIEncoderSet.length(); i4++) {
                inputEdgeArr[i2 - 1][i4] = null;
            }
        }
        int i5 = -1;
        int i6 = Integer.MAX_VALUE;
        for (int i7 = 0; i7 < eCIEncoderSet.length(); i7++) {
            InputEdge inputEdge2 = inputEdgeArr[length][i7];
            if (inputEdge2 != null && inputEdge2.cachedTotalSize < i6) {
                i6 = inputEdge2.cachedTotalSize;
                i5 = i7;
            }
        }
        if (i5 >= 0) {
            ArrayList arrayList = new ArrayList();
            for (InputEdge inputEdge3 = inputEdgeArr[length][i5]; inputEdge3 != null; inputEdge3 = inputEdge3.previous) {
                if (inputEdge3.isFNC1()) {
                    arrayList.add(0, 1000);
                } else {
                    byte[] encode = eCIEncoderSet.encode(inputEdge3.f464c, inputEdge3.encoderIndex);
                    for (int length2 = encode.length - 1; length2 >= 0; length2--) {
                        arrayList.add(0, Integer.valueOf((int) encode[length2] & 255));
                    }
                }
                if ((inputEdge3.previous == null ? 0 : inputEdge3.previous.encoderIndex) != inputEdge3.encoderIndex) {
                    arrayList.add(0, Integer.valueOf(eCIEncoderSet.getECIValue(inputEdge3.encoderIndex) + 256));
                }
            }
            int size = arrayList.size();
            int[] iArr2 = new int[size];
            for (int i8 = 0; i8 < size; i8++) {
                iArr2[i8] = ((Integer) arrayList.get(i8)).intValue();
            }
            return iArr2;
        }
        throw new RuntimeException("Internal error: failed to encode \"" + str + "\"");
    }

    private static final class InputEdge {
        /* access modifiers changed from: private */

        /* renamed from: c */
        public final char f464c;
        /* access modifiers changed from: private */
        public final int cachedTotalSize;
        /* access modifiers changed from: private */
        public final int encoderIndex;
        /* access modifiers changed from: private */
        public final InputEdge previous;

        private InputEdge(char c, ECIEncoderSet eCIEncoderSet, int i, InputEdge inputEdge, int i2) {
            int i3;
            int i4;
            char c2 = c == i2 ? 1000 : c;
            this.f464c = c2;
            this.encoderIndex = i;
            this.previous = inputEdge;
            if (c2 == 1000) {
                i3 = 1;
            } else {
                i3 = eCIEncoderSet.encode(c, i).length;
            }
            if (inputEdge == null) {
                i4 = 0;
            } else {
                i4 = inputEdge.encoderIndex;
            }
            i3 = i4 != i ? i3 + 3 : i3;
            this.cachedTotalSize = inputEdge != null ? i3 + inputEdge.cachedTotalSize : i3;
        }

        /* access modifiers changed from: package-private */
        public boolean isFNC1() {
            return this.f464c == 1000;
        }
    }
}

package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

final class State {
    static final State INITIAL_STATE = new State(Token.EMPTY, 0, 0, 0);
    private final int binaryShiftByteCount;
    private final int binaryShiftCost;
    private final int bitCount;
    private final int mode;
    private final Token token;

    private static int calculateBinaryShiftCost(int i) {
        if (i > 62) {
            return 21;
        }
        if (i > 31) {
            return 20;
        }
        return i > 0 ? 10 : 0;
    }

    private State(Token token2, int i, int i2, int i3) {
        this.token = token2;
        this.mode = i;
        this.binaryShiftByteCount = i2;
        this.bitCount = i3;
        this.binaryShiftCost = calculateBinaryShiftCost(i2);
    }

    /* access modifiers changed from: package-private */
    public int getMode() {
        return this.mode;
    }

    /* access modifiers changed from: package-private */
    public Token getToken() {
        return this.token;
    }

    /* access modifiers changed from: package-private */
    public int getBinaryShiftByteCount() {
        return this.binaryShiftByteCount;
    }

    /* access modifiers changed from: package-private */
    public int getBitCount() {
        return this.bitCount;
    }

    /* access modifiers changed from: package-private */
    public State appendFLGn(int i) {
        Token token2;
        Token token3 = shiftAndAppend(4, 0).token;
        int i2 = 3;
        if (i < 0) {
            token2 = token3.add(0, 3);
        } else if (i <= 999999) {
            byte[] bytes = Integer.toString(i).getBytes(StandardCharsets.ISO_8859_1);
            Token add = token3.add(bytes.length, 3);
            int length = bytes.length;
            for (int i3 = 0; i3 < length; i3++) {
                add = add.add((bytes[i3] - 48) + 2, 4);
            }
            i2 = 3 + (bytes.length * 4);
            token2 = add;
        } else {
            throw new IllegalArgumentException("ECI code must be between 0 and 999999");
        }
        return new State(token2, this.mode, 0, this.bitCount + i2);
    }

    /* access modifiers changed from: package-private */
    public State latchAndAppend(int i, int i2) {
        int i3 = this.bitCount;
        Token token2 = this.token;
        if (i != this.mode) {
            int i4 = HighLevelEncoder.LATCH_TABLE[this.mode][i];
            int i5 = 65535 & i4;
            int i6 = i4 >> 16;
            token2 = token2.add(i5, i6);
            i3 += i6;
        }
        int i7 = i == 2 ? 4 : 5;
        return new State(token2.add(i2, i7), i, 0, i3 + i7);
    }

    /* access modifiers changed from: package-private */
    public State shiftAndAppend(int i, int i2) {
        Token token2 = this.token;
        int i3 = this.mode == 2 ? 4 : 5;
        return new State(token2.add(HighLevelEncoder.SHIFT_TABLE[this.mode][i], i3).add(i2, 5), this.mode, 0, this.bitCount + i3 + 5);
    }

    /* access modifiers changed from: package-private */
    public State addBinaryShiftChar(int i) {
        Token token2 = this.token;
        int i2 = this.mode;
        int i3 = this.bitCount;
        if (i2 == 4 || i2 == 2) {
            int i4 = HighLevelEncoder.LATCH_TABLE[i2][0];
            int i5 = 65535 & i4;
            int i6 = i4 >> 16;
            token2 = token2.add(i5, i6);
            i3 += i6;
            i2 = 0;
        }
        int i7 = this.binaryShiftByteCount;
        State state = new State(token2, i2, i7 + 1, i3 + ((i7 == 0 || i7 == 31) ? 18 : i7 == 62 ? 9 : 8));
        return state.binaryShiftByteCount == 2078 ? state.endBinaryShift(i + 1) : state;
    }

    /* access modifiers changed from: package-private */
    public State endBinaryShift(int i) {
        int i2 = this.binaryShiftByteCount;
        if (i2 == 0) {
            return this;
        }
        return new State(this.token.addBinaryShift(i - i2, i2), this.mode, 0, this.bitCount);
    }

    /* access modifiers changed from: package-private */
    public boolean isBetterThanOrEqualTo(State state) {
        int i = this.bitCount + (HighLevelEncoder.LATCH_TABLE[this.mode][state.mode] >> 16);
        int i2 = this.binaryShiftByteCount;
        int i3 = state.binaryShiftByteCount;
        if (i2 < i3) {
            i += state.binaryShiftCost - this.binaryShiftCost;
        } else if (i2 > i3 && i3 > 0) {
            i += 10;
        }
        return i <= state.bitCount;
    }

    /* access modifiers changed from: package-private */
    public BitArray toBitArray(byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        for (Token token2 = endBinaryShift(bArr.length).token; token2 != null; token2 = token2.getPrevious()) {
            arrayList.add(token2);
        }
        BitArray bitArray = new BitArray();
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            ((Token) arrayList.get(size)).appendTo(bitArray, bArr);
        }
        return bitArray;
    }

    public String toString() {
        return String.format("%s bits=%d bytes=%d", HighLevelEncoder.MODE_NAMES[this.mode], Integer.valueOf(this.bitCount), Integer.valueOf(this.binaryShiftByteCount));
    }
}

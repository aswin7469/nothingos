package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;
import kotlin.text.Typography;

final class BinaryShiftToken extends Token {
    private final int binaryShiftByteCount;
    private final int binaryShiftStart;

    BinaryShiftToken(Token token, int i, int i2) {
        super(token);
        this.binaryShiftStart = i;
        this.binaryShiftByteCount = i2;
    }

    public void appendTo(BitArray bitArray, byte[] bArr) {
        int i = this.binaryShiftByteCount;
        for (int i2 = 0; i2 < i; i2++) {
            if (i2 == 0 || (i2 == 31 && i <= 62)) {
                bitArray.appendBits(31, 5);
                if (i > 62) {
                    bitArray.appendBits(i - 31, 16);
                } else if (i2 == 0) {
                    bitArray.appendBits(Math.min(i, 31), 5);
                } else {
                    bitArray.appendBits(i - 31, 5);
                }
            }
            bitArray.appendBits(bArr[this.binaryShiftStart + i2], 8);
        }
    }

    public String toString() {
        return "<" + this.binaryShiftStart + "::" + ((this.binaryShiftStart + this.binaryShiftByteCount) - 1) + Typography.greater;
    }
}

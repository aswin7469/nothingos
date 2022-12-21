package com.google.zxing.datamatrix.encoder;

import com.nothing.p023os.device.DeviceConstant;

final class DataMatrixSymbolInfo144 extends SymbolInfo {
    public int getDataLengthForInterleavedBlock(int i) {
        return i <= 8 ? 156 : 155;
    }

    public int getInterleavedBlockCount() {
        return 10;
    }

    DataMatrixSymbolInfo144() {
        super(false, 1558, DeviceConstant.ORDER_EQUALIZER, 22, 22, 36, -1, 62);
    }
}

package com.google.zxing.oned;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
/* loaded from: classes2.dex */
public abstract class OneDReader implements Reader {
    public abstract Result decodeRow(int i, BitArray bitArray, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException;

    @Override // com.google.zxing.Reader
    public void reset() {
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException, FormatException {
        try {
            return doDecode(binaryBitmap, map);
        } catch (NotFoundException e) {
            if ((map != null && map.containsKey(DecodeHintType.TRY_HARDER)) && binaryBitmap.isRotateSupported()) {
                BinaryBitmap rotateCounterClockwise = binaryBitmap.rotateCounterClockwise();
                Result doDecode = this.doDecode(rotateCounterClockwise, map);
                Map<ResultMetadataType, Object> resultMetadata = doDecode.getResultMetadata();
                int i = 270;
                if (resultMetadata != null) {
                    ResultMetadataType resultMetadataType = ResultMetadataType.ORIENTATION;
                    if (resultMetadata.containsKey(resultMetadataType)) {
                        i = (((Integer) resultMetadata.get(resultMetadataType)).intValue() + 270) % 360;
                    }
                }
                doDecode.putMetadata(ResultMetadataType.ORIENTATION, Integer.valueOf(i));
                ResultPoint[] resultPoints = doDecode.getResultPoints();
                if (resultPoints != null) {
                    int height = rotateCounterClockwise.getHeight();
                    for (int i2 = 0; i2 < resultPoints.length; i2++) {
                        resultPoints[i2] = new ResultPoint((height - resultPoints[i2].getY()) - 1.0f, resultPoints[i2].getX());
                    }
                }
                return doDecode;
            }
            throw e;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x007c A[Catch: ReaderException -> 0x00cc, TRY_LEAVE, TryCatch #5 {ReaderException -> 0x00cc, blocks: (B:34:0x0076, B:36:0x007c), top: B:33:0x0076 }] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00cb A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Result doDecode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException {
        OneDReader oneDReader;
        Map<DecodeHintType, ?> map2;
        int i;
        int i2;
        EnumMap enumMap = map;
        int width = binaryBitmap.getWidth();
        int height = binaryBitmap.getHeight();
        BitArray bitArray = new BitArray(width);
        int i3 = height >> 1;
        int i4 = 0;
        int i5 = 1;
        boolean z = enumMap != null && enumMap.containsKey(DecodeHintType.TRY_HARDER);
        int max = Math.max(1, height >> (z ? 8 : 5));
        int i6 = z ? height : 15;
        int i7 = 0;
        while (i7 < i6) {
            int i8 = i7 + 1;
            int i9 = i8 >> 1;
            if (((i7 & 1) == 0 ? i5 : i4) == 0) {
                i9 = -i9;
            }
            int i10 = (i9 * max) + i3;
            if (i10 < 0 || i10 >= height) {
                break;
            }
            try {
                bitArray = binaryBitmap.getBlackRow(i10, bitArray);
                int i11 = i4;
                while (i11 < 2) {
                    try {
                        if (i11 == i5) {
                            bitArray.reverse();
                            if (enumMap != null) {
                                DecodeHintType decodeHintType = DecodeHintType.NEED_RESULT_POINT_CALLBACK;
                                if (enumMap.containsKey(decodeHintType)) {
                                    EnumMap enumMap2 = new EnumMap(DecodeHintType.class);
                                    enumMap2.putAll(enumMap);
                                    enumMap2.remove(decodeHintType);
                                    oneDReader = this;
                                    enumMap = enumMap2;
                                    Result decodeRow = oneDReader.decodeRow(i10, bitArray, enumMap);
                                    if (i11 != i5) {
                                        try {
                                            decodeRow.putMetadata(ResultMetadataType.ORIENTATION, 180);
                                            ResultPoint[] resultPoints = decodeRow.getResultPoints();
                                            if (resultPoints != null) {
                                                map2 = enumMap;
                                                float f = width;
                                                try {
                                                    i = width;
                                                } catch (ReaderException unused) {
                                                    i = width;
                                                    i2 = 1;
                                                    i11++;
                                                    i5 = i2;
                                                    enumMap = map2;
                                                    width = i;
                                                    i4 = 0;
                                                }
                                                try {
                                                    resultPoints[0] = new ResultPoint((f - resultPoints[i4].getX()) - 1.0f, resultPoints[i4].getY());
                                                    i2 = 1;
                                                    try {
                                                        resultPoints[1] = new ResultPoint((f - resultPoints[1].getX()) - 1.0f, resultPoints[1].getY());
                                                    } catch (ReaderException unused2) {
                                                        continue;
                                                        i11++;
                                                        i5 = i2;
                                                        enumMap = map2;
                                                        width = i;
                                                        i4 = 0;
                                                    }
                                                } catch (ReaderException unused3) {
                                                    i2 = 1;
                                                    i11++;
                                                    i5 = i2;
                                                    enumMap = map2;
                                                    width = i;
                                                    i4 = 0;
                                                }
                                            }
                                        } catch (ReaderException unused4) {
                                            map2 = enumMap;
                                        }
                                    }
                                    return decodeRow;
                                }
                            }
                        }
                        Result decodeRow2 = oneDReader.decodeRow(i10, bitArray, enumMap);
                        if (i11 != i5) {
                        }
                        return decodeRow2;
                    } catch (ReaderException unused5) {
                        map2 = enumMap;
                        i = width;
                        i2 = i5;
                    }
                    oneDReader = this;
                }
                continue;
            } catch (NotFoundException unused6) {
            }
            i7 = i8;
            i5 = i5;
            width = width;
            i4 = 0;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void recordPattern(BitArray bitArray, int i, int[] iArr) throws NotFoundException {
        int length = iArr.length;
        int i2 = 0;
        Arrays.fill(iArr, 0, length, 0);
        int size = bitArray.getSize();
        if (i >= size) {
            throw NotFoundException.getNotFoundInstance();
        }
        boolean z = !bitArray.get(i);
        while (i < size) {
            if (bitArray.get(i) ^ z) {
                iArr[i2] = iArr[i2] + 1;
            } else {
                i2++;
                if (i2 == length) {
                    break;
                }
                iArr[i2] = 1;
                z = !z;
            }
            i++;
        }
        if (i2 == length) {
            return;
        }
        if (i2 != length - 1 || i != size) {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void recordPatternInReverse(BitArray bitArray, int i, int[] iArr) throws NotFoundException {
        int length = iArr.length;
        boolean z = bitArray.get(i);
        while (i > 0 && length >= 0) {
            i--;
            if (bitArray.get(i) != z) {
                length--;
                z = !z;
            }
        }
        if (length >= 0) {
            throw NotFoundException.getNotFoundInstance();
        }
        recordPattern(bitArray, i + 1, iArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int patternMatchVariance(int[] iArr, int[] iArr2, int i) {
        int length = iArr.length;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            i2 += iArr[i4];
            i3 += iArr2[i4];
        }
        if (i2 < i3) {
            return Integer.MAX_VALUE;
        }
        int i5 = (i2 << 8) / i3;
        int i6 = (i * i5) >> 8;
        int i7 = 0;
        for (int i8 = 0; i8 < length; i8++) {
            int i9 = iArr[i8] << 8;
            int i10 = iArr2[i8] * i5;
            int i11 = i9 > i10 ? i9 - i10 : i10 - i9;
            if (i11 > i6) {
                return Integer.MAX_VALUE;
            }
            i7 += i11;
        }
        return i7 / i2;
    }
}

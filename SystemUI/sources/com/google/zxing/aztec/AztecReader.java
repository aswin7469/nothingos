package com.google.zxing.aztec;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import java.util.Map;

public final class AztecReader implements Reader {
    public void reset() {
    }

    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return decode(binaryBitmap, (Map<DecodeHintType, ?>) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x005c A[LOOP:0: B:29:0x005a->B:30:0x005c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x008d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.Result decode(com.google.zxing.BinaryBitmap r10, java.util.Map<com.google.zxing.DecodeHintType, ?> r11) throws com.google.zxing.NotFoundException, com.google.zxing.FormatException {
        /*
            r9 = this;
            com.google.zxing.aztec.detector.Detector r9 = new com.google.zxing.aztec.detector.Detector
            com.google.zxing.common.BitMatrix r10 = r10.getBlackMatrix()
            r9.<init>(r10)
            r10 = 0
            r0 = 0
            com.google.zxing.aztec.AztecDetectorResult r1 = r9.detect(r10)     // Catch:{ NotFoundException -> 0x002b, FormatException -> 0x0025 }
            com.google.zxing.ResultPoint[] r2 = r1.getPoints()     // Catch:{ NotFoundException -> 0x002b, FormatException -> 0x0025 }
            com.google.zxing.aztec.decoder.Decoder r3 = new com.google.zxing.aztec.decoder.Decoder     // Catch:{ NotFoundException -> 0x0023, FormatException -> 0x0021 }
            r3.<init>()     // Catch:{ NotFoundException -> 0x0023, FormatException -> 0x0021 }
            com.google.zxing.common.DecoderResult r1 = r3.decode(r1)     // Catch:{ NotFoundException -> 0x0023, FormatException -> 0x0021 }
            r3 = r2
            r2 = r0
            r0 = r1
            r1 = r2
            goto L_0x002f
        L_0x0021:
            r1 = move-exception
            goto L_0x0027
        L_0x0023:
            r1 = move-exception
            goto L_0x002d
        L_0x0025:
            r1 = move-exception
            r2 = r0
        L_0x0027:
            r3 = r2
            r2 = r1
            r1 = r0
            goto L_0x002f
        L_0x002b:
            r1 = move-exception
            r2 = r0
        L_0x002d:
            r3 = r2
            r2 = r0
        L_0x002f:
            if (r0 != 0) goto L_0x004c
            r0 = 1
            com.google.zxing.aztec.AztecDetectorResult r9 = r9.detect(r0)     // Catch:{ FormatException | NotFoundException -> 0x0044 }
            com.google.zxing.ResultPoint[] r3 = r9.getPoints()     // Catch:{ FormatException | NotFoundException -> 0x0044 }
            com.google.zxing.aztec.decoder.Decoder r0 = new com.google.zxing.aztec.decoder.Decoder     // Catch:{ FormatException | NotFoundException -> 0x0044 }
            r0.<init>()     // Catch:{ FormatException | NotFoundException -> 0x0044 }
            com.google.zxing.common.DecoderResult r0 = r0.decode(r9)     // Catch:{ FormatException | NotFoundException -> 0x0044 }
            goto L_0x004c
        L_0x0044:
            r9 = move-exception
            if (r1 != 0) goto L_0x004b
            if (r2 == 0) goto L_0x004a
            throw r2
        L_0x004a:
            throw r9
        L_0x004b:
            throw r1
        L_0x004c:
            r5 = r3
            if (r11 == 0) goto L_0x0064
            com.google.zxing.DecodeHintType r9 = com.google.zxing.DecodeHintType.NEED_RESULT_POINT_CALLBACK
            java.lang.Object r9 = r11.get(r9)
            com.google.zxing.ResultPointCallback r9 = (com.google.zxing.ResultPointCallback) r9
            if (r9 == 0) goto L_0x0064
            int r11 = r5.length
        L_0x005a:
            if (r10 >= r11) goto L_0x0064
            r1 = r5[r10]
            r9.foundPossibleResultPoint(r1)
            int r10 = r10 + 1
            goto L_0x005a
        L_0x0064:
            com.google.zxing.Result r9 = new com.google.zxing.Result
            java.lang.String r2 = r0.getText()
            byte[] r3 = r0.getRawBytes()
            int r4 = r0.getNumBits()
            com.google.zxing.BarcodeFormat r6 = com.google.zxing.BarcodeFormat.AZTEC
            long r7 = java.lang.System.currentTimeMillis()
            r1 = r9
            r1.<init>(r2, r3, r4, r5, r6, r7)
            java.util.List r10 = r0.getByteSegments()
            if (r10 == 0) goto L_0x0087
            com.google.zxing.ResultMetadataType r11 = com.google.zxing.ResultMetadataType.BYTE_SEGMENTS
            r9.putMetadata(r11, r10)
        L_0x0087:
            java.lang.String r10 = r0.getECLevel()
            if (r10 == 0) goto L_0x0092
            com.google.zxing.ResultMetadataType r11 = com.google.zxing.ResultMetadataType.ERROR_CORRECTION_LEVEL
            r9.putMetadata(r11, r10)
        L_0x0092:
            com.google.zxing.ResultMetadataType r10 = com.google.zxing.ResultMetadataType.SYMBOLOGY_IDENTIFIER
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r1 = "]z"
            r11.<init>((java.lang.String) r1)
            int r0 = r0.getSymbologyModifier()
            java.lang.StringBuilder r11 = r11.append((int) r0)
            java.lang.String r11 = r11.toString()
            r9.putMetadata(r10, r11)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.aztec.AztecReader.decode(com.google.zxing.BinaryBitmap, java.util.Map):com.google.zxing.Result");
    }
}

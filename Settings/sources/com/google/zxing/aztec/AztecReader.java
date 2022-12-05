package com.google.zxing.aztec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.aztec.decoder.Decoder;
import com.google.zxing.aztec.detector.Detector;
import com.google.zxing.common.DecoderResult;
import java.util.List;
import java.util.Map;
/* loaded from: classes2.dex */
public final class AztecReader implements Reader {
    @Override // com.google.zxing.Reader
    public void reset() {
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0063 A[LOOP:0: B:29:0x0061->B:30:0x0063, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0031  */
    @Override // com.google.zxing.Reader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException, FormatException {
        ResultPoint[] resultPointArr;
        ResultPoint[] resultPointArr2;
        FormatException formatException;
        List<byte[]> byteSegments;
        String eCLevel;
        ResultPointCallback resultPointCallback;
        Detector detector = new Detector(binaryBitmap.getBlackMatrix());
        DecoderResult decoderResult = null;
        try {
            AztecDetectorResult detect = detector.detect(false);
            resultPointArr = detect.getPoints();
            try {
                resultPointArr2 = resultPointArr;
                formatException = null;
                decoderResult = new Decoder().decode(detect);
                e = null;
            } catch (FormatException e) {
                e = e;
                resultPointArr2 = resultPointArr;
                formatException = e;
                e = null;
                if (decoderResult == null) {
                }
                if (map != null) {
                    while (r5 < r6) {
                    }
                }
                Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), resultPointArr2, BarcodeFormat.AZTEC);
                byteSegments = decoderResult.getByteSegments();
                if (byteSegments != null) {
                }
                eCLevel = decoderResult.getECLevel();
                if (eCLevel != null) {
                }
                return result;
            } catch (NotFoundException e2) {
                e = e2;
                resultPointArr2 = resultPointArr;
                formatException = null;
                if (decoderResult == null) {
                }
                if (map != null) {
                }
                Result result2 = new Result(decoderResult.getText(), decoderResult.getRawBytes(), resultPointArr2, BarcodeFormat.AZTEC);
                byteSegments = decoderResult.getByteSegments();
                if (byteSegments != null) {
                }
                eCLevel = decoderResult.getECLevel();
                if (eCLevel != null) {
                }
                return result2;
            }
        } catch (FormatException e3) {
            e = e3;
            resultPointArr = null;
        } catch (NotFoundException e4) {
            e = e4;
            resultPointArr = null;
        }
        if (decoderResult == null) {
            try {
                AztecDetectorResult detect2 = detector.detect(true);
                resultPointArr2 = detect2.getPoints();
                decoderResult = new Decoder().decode(detect2);
            } catch (FormatException e5) {
                if (e != null) {
                    throw e;
                }
                if (formatException != null) {
                    throw formatException;
                }
                throw e5;
            } catch (NotFoundException e6) {
                if (e != null) {
                    throw e;
                }
                if (formatException != null) {
                    throw formatException;
                }
                throw e6;
            }
        }
        if (map != null && (resultPointCallback = (ResultPointCallback) map.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK)) != null) {
            for (ResultPoint resultPoint : resultPointArr2) {
                resultPointCallback.foundPossibleResultPoint(resultPoint);
            }
        }
        Result result22 = new Result(decoderResult.getText(), decoderResult.getRawBytes(), resultPointArr2, BarcodeFormat.AZTEC);
        byteSegments = decoderResult.getByteSegments();
        if (byteSegments != null) {
            result22.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        eCLevel = decoderResult.getECLevel();
        if (eCLevel != null) {
            result22.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, eCLevel);
        }
        return result22;
    }
}

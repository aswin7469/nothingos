package com.google.zxing;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.google.zxing.common.detector.MathUtils;

public class ResultPoint {

    /* renamed from: x */
    private final float f460x;

    /* renamed from: y */
    private final float f461y;

    public ResultPoint(float f, float f2) {
        this.f460x = f;
        this.f461y = f2;
    }

    public final float getX() {
        return this.f460x;
    }

    public final float getY() {
        return this.f461y;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof ResultPoint)) {
            return false;
        }
        ResultPoint resultPoint = (ResultPoint) obj;
        if (this.f460x == resultPoint.f460x && this.f461y == resultPoint.f461y) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return (Float.floatToIntBits(this.f460x) * 31) + Float.floatToIntBits(this.f461y);
    }

    public final String toString() {
        return NavigationBarInflaterView.KEY_CODE_START + this.f460x + ',' + this.f461y + ')';
    }

    public static void orderBestPatterns(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint;
        ResultPoint resultPoint2;
        ResultPoint resultPoint3;
        float distance = distance(resultPointArr[0], resultPointArr[1]);
        float distance2 = distance(resultPointArr[1], resultPointArr[2]);
        float distance3 = distance(resultPointArr[0], resultPointArr[2]);
        if (distance2 >= distance && distance2 >= distance3) {
            resultPoint3 = resultPointArr[0];
            resultPoint2 = resultPointArr[1];
            resultPoint = resultPointArr[2];
        } else if (distance3 < distance2 || distance3 < distance) {
            resultPoint3 = resultPointArr[2];
            resultPoint2 = resultPointArr[0];
            resultPoint = resultPointArr[1];
        } else {
            resultPoint3 = resultPointArr[1];
            resultPoint2 = resultPointArr[0];
            resultPoint = resultPointArr[2];
        }
        if (crossProductZ(resultPoint2, resultPoint3, resultPoint) < 0.0f) {
            ResultPoint resultPoint4 = resultPoint;
            resultPoint = resultPoint2;
            resultPoint2 = resultPoint4;
        }
        resultPointArr[0] = resultPoint2;
        resultPointArr[1] = resultPoint3;
        resultPointArr[2] = resultPoint;
    }

    public static float distance(ResultPoint resultPoint, ResultPoint resultPoint2) {
        return MathUtils.distance(resultPoint.f460x, resultPoint.f461y, resultPoint2.f460x, resultPoint2.f461y);
    }

    private static float crossProductZ(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3) {
        float f = resultPoint2.f460x;
        float f2 = resultPoint2.f461y;
        return ((resultPoint3.f460x - f) * (resultPoint.f461y - f2)) - ((resultPoint3.f461y - f2) * (resultPoint.f460x - f));
    }
}

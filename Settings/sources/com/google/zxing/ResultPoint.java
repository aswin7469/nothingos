package com.google.zxing;

import com.google.zxing.common.detector.MathUtils;

public class ResultPoint {

    /* renamed from: x */
    private final float f246x;

    /* renamed from: y */
    private final float f247y;

    public ResultPoint(float f, float f2) {
        this.f246x = f;
        this.f247y = f2;
    }

    public final float getX() {
        return this.f246x;
    }

    public final float getY() {
        return this.f247y;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof ResultPoint)) {
            return false;
        }
        ResultPoint resultPoint = (ResultPoint) obj;
        if (this.f246x == resultPoint.f246x && this.f247y == resultPoint.f247y) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return (Float.floatToIntBits(this.f246x) * 31) + Float.floatToIntBits(this.f247y);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(25);
        sb.append('(');
        sb.append(this.f246x);
        sb.append(',');
        sb.append(this.f247y);
        sb.append(')');
        return sb.toString();
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
        return MathUtils.distance(resultPoint.f246x, resultPoint.f247y, resultPoint2.f246x, resultPoint2.f247y);
    }

    private static float crossProductZ(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3) {
        float f = resultPoint2.f246x;
        float f2 = resultPoint2.f247y;
        return ((resultPoint3.f246x - f) * (resultPoint.f247y - f2)) - ((resultPoint3.f247y - f2) * (resultPoint.f246x - f));
    }
}

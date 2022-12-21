package com.google.zxing.datamatrix.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.WhiteRectangleDetector;

public final class Detector {
    private final BitMatrix image;
    private final WhiteRectangleDetector rectangleDetector;

    public Detector(BitMatrix bitMatrix) throws NotFoundException {
        this.image = bitMatrix;
        this.rectangleDetector = new WhiteRectangleDetector(bitMatrix);
    }

    public DetectorResult detect() throws NotFoundException {
        int i;
        int i2;
        ResultPoint[] detectSolid2 = detectSolid2(detectSolid1(this.rectangleDetector.detect()));
        ResultPoint correctTopRight = correctTopRight(detectSolid2);
        detectSolid2[3] = correctTopRight;
        if (correctTopRight != null) {
            ResultPoint[] shiftToModuleCenter = shiftToModuleCenter(detectSolid2);
            ResultPoint resultPoint = shiftToModuleCenter[0];
            ResultPoint resultPoint2 = shiftToModuleCenter[1];
            ResultPoint resultPoint3 = shiftToModuleCenter[2];
            ResultPoint resultPoint4 = shiftToModuleCenter[3];
            int transitionsBetween = transitionsBetween(resultPoint, resultPoint4) + 1;
            int transitionsBetween2 = transitionsBetween(resultPoint3, resultPoint4) + 1;
            if ((transitionsBetween & 1) == 1) {
                transitionsBetween++;
            }
            if ((transitionsBetween2 & 1) == 1) {
                transitionsBetween2++;
            }
            if (transitionsBetween * 4 >= transitionsBetween2 * 6 || transitionsBetween2 * 4 >= transitionsBetween * 6) {
                i2 = transitionsBetween;
                i = transitionsBetween2;
            } else {
                i2 = Math.max(transitionsBetween, transitionsBetween2);
                i = i2;
            }
            return new DetectorResult(sampleGrid(this.image, resultPoint, resultPoint2, resultPoint3, resultPoint4, i2, i), new ResultPoint[]{resultPoint, resultPoint2, resultPoint3, resultPoint4});
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static ResultPoint shiftPoint(ResultPoint resultPoint, ResultPoint resultPoint2, int i) {
        float f = (float) (i + 1);
        return new ResultPoint(resultPoint.getX() + ((resultPoint2.getX() - resultPoint.getX()) / f), resultPoint.getY() + ((resultPoint2.getY() - resultPoint.getY()) / f));
    }

    private static ResultPoint moveAway(ResultPoint resultPoint, float f, float f2) {
        float x = resultPoint.getX();
        float y = resultPoint.getY();
        return new ResultPoint(x < f ? x - 1.0f : x + 1.0f, y < f2 ? y - 1.0f : y + 1.0f);
    }

    private ResultPoint[] detectSolid1(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint = resultPointArr[0];
        ResultPoint resultPoint2 = resultPointArr[1];
        ResultPoint resultPoint3 = resultPointArr[3];
        ResultPoint resultPoint4 = resultPointArr[2];
        int transitionsBetween = transitionsBetween(resultPoint, resultPoint2);
        int transitionsBetween2 = transitionsBetween(resultPoint2, resultPoint3);
        int transitionsBetween3 = transitionsBetween(resultPoint3, resultPoint4);
        int transitionsBetween4 = transitionsBetween(resultPoint4, resultPoint);
        ResultPoint[] resultPointArr2 = {resultPoint4, resultPoint, resultPoint2, resultPoint3};
        if (transitionsBetween > transitionsBetween2) {
            resultPointArr2[0] = resultPoint;
            resultPointArr2[1] = resultPoint2;
            resultPointArr2[2] = resultPoint3;
            resultPointArr2[3] = resultPoint4;
            transitionsBetween = transitionsBetween2;
        }
        if (transitionsBetween > transitionsBetween3) {
            resultPointArr2[0] = resultPoint2;
            resultPointArr2[1] = resultPoint3;
            resultPointArr2[2] = resultPoint4;
            resultPointArr2[3] = resultPoint;
        } else {
            transitionsBetween3 = transitionsBetween;
        }
        if (transitionsBetween3 > transitionsBetween4) {
            resultPointArr2[0] = resultPoint3;
            resultPointArr2[1] = resultPoint4;
            resultPointArr2[2] = resultPoint;
            resultPointArr2[3] = resultPoint2;
        }
        return resultPointArr2;
    }

    private ResultPoint[] detectSolid2(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint = resultPointArr[0];
        ResultPoint resultPoint2 = resultPointArr[1];
        ResultPoint resultPoint3 = resultPointArr[2];
        ResultPoint resultPoint4 = resultPointArr[3];
        int transitionsBetween = (transitionsBetween(resultPoint, resultPoint4) + 1) * 4;
        if (transitionsBetween(shiftPoint(resultPoint2, resultPoint3, transitionsBetween), resultPoint) < transitionsBetween(shiftPoint(resultPoint3, resultPoint2, transitionsBetween), resultPoint4)) {
            resultPointArr[0] = resultPoint;
            resultPointArr[1] = resultPoint2;
            resultPointArr[2] = resultPoint3;
            resultPointArr[3] = resultPoint4;
        } else {
            resultPointArr[0] = resultPoint2;
            resultPointArr[1] = resultPoint3;
            resultPointArr[2] = resultPoint4;
            resultPointArr[3] = resultPoint;
        }
        return resultPointArr;
    }

    private ResultPoint correctTopRight(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint = resultPointArr[0];
        ResultPoint resultPoint2 = resultPointArr[1];
        ResultPoint resultPoint3 = resultPointArr[2];
        ResultPoint resultPoint4 = resultPointArr[3];
        int transitionsBetween = transitionsBetween(resultPoint, resultPoint4);
        ResultPoint shiftPoint = shiftPoint(resultPoint, resultPoint2, (transitionsBetween(resultPoint2, resultPoint4) + 1) * 4);
        ResultPoint shiftPoint2 = shiftPoint(resultPoint3, resultPoint2, (transitionsBetween + 1) * 4);
        int transitionsBetween2 = transitionsBetween(shiftPoint, resultPoint4);
        int transitionsBetween3 = transitionsBetween(shiftPoint2, resultPoint4);
        float f = (float) (transitionsBetween2 + 1);
        ResultPoint resultPoint5 = new ResultPoint(resultPoint4.getX() + ((resultPoint3.getX() - resultPoint2.getX()) / f), resultPoint4.getY() + ((resultPoint3.getY() - resultPoint2.getY()) / f));
        float f2 = (float) (transitionsBetween3 + 1);
        ResultPoint resultPoint6 = new ResultPoint(resultPoint4.getX() + ((resultPoint.getX() - resultPoint2.getX()) / f2), resultPoint4.getY() + ((resultPoint.getY() - resultPoint2.getY()) / f2));
        if (isValid(resultPoint5)) {
            return (isValid(resultPoint6) && transitionsBetween(shiftPoint, resultPoint5) + transitionsBetween(shiftPoint2, resultPoint5) <= transitionsBetween(shiftPoint, resultPoint6) + transitionsBetween(shiftPoint2, resultPoint6)) ? resultPoint6 : resultPoint5;
        }
        if (isValid(resultPoint6)) {
            return resultPoint6;
        }
        return null;
    }

    private ResultPoint[] shiftToModuleCenter(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint = resultPointArr[0];
        ResultPoint resultPoint2 = resultPointArr[1];
        ResultPoint resultPoint3 = resultPointArr[2];
        ResultPoint resultPoint4 = resultPointArr[3];
        ResultPoint shiftPoint = shiftPoint(resultPoint, resultPoint2, (transitionsBetween(resultPoint3, resultPoint4) + 1) * 4);
        ResultPoint shiftPoint2 = shiftPoint(resultPoint3, resultPoint2, (transitionsBetween(resultPoint, resultPoint4) + 1) * 4);
        int transitionsBetween = transitionsBetween(shiftPoint, resultPoint4) + 1;
        int transitionsBetween2 = transitionsBetween(shiftPoint2, resultPoint4) + 1;
        if ((transitionsBetween & 1) == 1) {
            transitionsBetween++;
        }
        if ((transitionsBetween2 & 1) == 1) {
            transitionsBetween2++;
        }
        float x = (((resultPoint.getX() + resultPoint2.getX()) + resultPoint3.getX()) + resultPoint4.getX()) / 4.0f;
        float y = (((resultPoint.getY() + resultPoint2.getY()) + resultPoint3.getY()) + resultPoint4.getY()) / 4.0f;
        ResultPoint moveAway = moveAway(resultPoint, x, y);
        ResultPoint moveAway2 = moveAway(resultPoint2, x, y);
        ResultPoint moveAway3 = moveAway(resultPoint3, x, y);
        ResultPoint moveAway4 = moveAway(resultPoint4, x, y);
        int i = transitionsBetween2 * 4;
        int i2 = transitionsBetween * 4;
        return new ResultPoint[]{shiftPoint(shiftPoint(moveAway, moveAway2, i), moveAway4, i2), shiftPoint(shiftPoint(moveAway2, moveAway, i), moveAway3, i2), shiftPoint(shiftPoint(moveAway3, moveAway4, i), moveAway2, i2), shiftPoint(shiftPoint(moveAway4, moveAway3, i), moveAway, i2)};
    }

    private boolean isValid(ResultPoint resultPoint) {
        return resultPoint.getX() >= 0.0f && resultPoint.getX() <= ((float) (this.image.getWidth() - 1)) && resultPoint.getY() > 0.0f && resultPoint.getY() <= ((float) (this.image.getHeight() - 1));
    }

    private static BitMatrix sampleGrid(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4, int i, int i2) throws NotFoundException {
        float f = ((float) i) - 0.5f;
        float f2 = ((float) i2) - 0.5f;
        return GridSampler.getInstance().sampleGrid(bitMatrix, i, i2, 0.5f, 0.5f, f, 0.5f, f, f2, 0.5f, f2, resultPoint.getX(), resultPoint.getY(), resultPoint4.getX(), resultPoint4.getY(), resultPoint3.getX(), resultPoint3.getY(), resultPoint2.getX(), resultPoint2.getY());
    }

    private int transitionsBetween(ResultPoint resultPoint, ResultPoint resultPoint2) {
        int x = (int) resultPoint.getX();
        int y = (int) resultPoint.getY();
        int x2 = (int) resultPoint2.getX();
        int i = 1;
        int min = Math.min(this.image.getHeight() - 1, (int) resultPoint2.getY());
        int i2 = 0;
        boolean z = Math.abs(min - y) > Math.abs(x2 - x);
        if (z) {
            int i3 = y;
            y = x;
            x = i3;
            int i4 = min;
            min = x2;
            x2 = i4;
        }
        int abs = Math.abs(x2 - x);
        int abs2 = Math.abs(min - y);
        int i5 = (-abs) / 2;
        int i6 = y < min ? 1 : -1;
        if (x >= x2) {
            i = -1;
        }
        boolean z2 = this.image.get(z ? y : x, z ? x : y);
        while (x != x2) {
            boolean z3 = this.image.get(z ? y : x, z ? x : y);
            if (z3 != z2) {
                i2++;
                z2 = z3;
            }
            i5 += abs2;
            if (i5 > 0) {
                if (y == min) {
                    break;
                }
                y += i6;
                i5 -= abs;
            }
            x += i;
        }
        return i2;
    }
}

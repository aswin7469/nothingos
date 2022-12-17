package androidx.constraintlayout.motion.utils;

public class LinearCurveFit extends CurveFit {

    /* renamed from: mT */
    private double[] f7mT;
    private double mTotalLength = Double.NaN;

    /* renamed from: mY */
    private double[][] f8mY;

    public LinearCurveFit(double[] dArr, double[][] dArr2) {
        int length = dArr.length;
        int length2 = dArr2[0].length;
        this.f7mT = dArr;
        this.f8mY = dArr2;
        if (length2 > 2) {
            int i = 0;
            double d = 0.0d;
            while (true) {
                double d2 = d;
                if (i < dArr.length) {
                    double d3 = dArr2[i][0];
                    if (i > 0) {
                        Math.hypot(d3 - d, d3 - d2);
                    }
                    i++;
                    d = d3;
                } else {
                    this.mTotalLength = 0.0d;
                    return;
                }
            }
        }
    }

    public void getPos(double d, double[] dArr) {
        double[] dArr2 = this.f7mT;
        int length = dArr2.length;
        int i = 0;
        int length2 = this.f8mY[0].length;
        if (d <= dArr2[0]) {
            for (int i2 = 0; i2 < length2; i2++) {
                dArr[i2] = this.f8mY[0][i2];
            }
            return;
        }
        int i3 = length - 1;
        if (d >= dArr2[i3]) {
            while (i < length2) {
                dArr[i] = this.f8mY[i3][i];
                i++;
            }
            return;
        }
        int i4 = 0;
        while (i4 < i3) {
            if (d == this.f7mT[i4]) {
                for (int i5 = 0; i5 < length2; i5++) {
                    dArr[i5] = this.f8mY[i4][i5];
                }
            }
            double[] dArr3 = this.f7mT;
            int i6 = i4 + 1;
            double d2 = dArr3[i6];
            if (d < d2) {
                double d3 = dArr3[i4];
                double d4 = (d - d3) / (d2 - d3);
                while (i < length2) {
                    double[][] dArr4 = this.f8mY;
                    dArr[i] = (dArr4[i4][i] * (1.0d - d4)) + (dArr4[i6][i] * d4);
                    i++;
                }
                return;
            }
            i4 = i6;
        }
    }

    public void getPos(double d, float[] fArr) {
        double[] dArr = this.f7mT;
        int length = dArr.length;
        int i = 0;
        int length2 = this.f8mY[0].length;
        if (d <= dArr[0]) {
            for (int i2 = 0; i2 < length2; i2++) {
                fArr[i2] = (float) this.f8mY[0][i2];
            }
            return;
        }
        int i3 = length - 1;
        if (d >= dArr[i3]) {
            while (i < length2) {
                fArr[i] = (float) this.f8mY[i3][i];
                i++;
            }
            return;
        }
        int i4 = 0;
        while (i4 < i3) {
            if (d == this.f7mT[i4]) {
                for (int i5 = 0; i5 < length2; i5++) {
                    fArr[i5] = (float) this.f8mY[i4][i5];
                }
            }
            double[] dArr2 = this.f7mT;
            int i6 = i4 + 1;
            double d2 = dArr2[i6];
            if (d < d2) {
                double d3 = dArr2[i4];
                double d4 = (d - d3) / (d2 - d3);
                while (i < length2) {
                    double[][] dArr3 = this.f8mY;
                    fArr[i] = (float) ((dArr3[i4][i] * (1.0d - d4)) + (dArr3[i6][i] * d4));
                    i++;
                }
                return;
            }
            i4 = i6;
        }
    }

    public double getPos(double d, int i) {
        double[] dArr = this.f7mT;
        int length = dArr.length;
        int i2 = 0;
        if (d <= dArr[0]) {
            return this.f8mY[0][i];
        }
        int i3 = length - 1;
        if (d >= dArr[i3]) {
            return this.f8mY[i3][i];
        }
        while (i2 < i3) {
            double[] dArr2 = this.f7mT;
            double d2 = dArr2[i2];
            if (d == d2) {
                return this.f8mY[i2][i];
            }
            int i4 = i2 + 1;
            double d3 = dArr2[i4];
            if (d < d3) {
                double d4 = (d - d2) / (d3 - d2);
                double[][] dArr3 = this.f8mY;
                return (dArr3[i2][i] * (1.0d - d4)) + (dArr3[i4][i] * d4);
            }
            i2 = i4;
        }
        return 0.0d;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0017, code lost:
        if (r11 >= r4) goto L_0x000f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getSlope(double r11, double[] r13) {
        /*
            r10 = this;
            double[] r0 = r10.f7mT
            int r1 = r0.length
            double[][] r2 = r10.f8mY
            r3 = 0
            r2 = r2[r3]
            int r2 = r2.length
            r4 = r0[r3]
            int r6 = (r11 > r4 ? 1 : (r11 == r4 ? 0 : -1))
            if (r6 > 0) goto L_0x0011
        L_0x000f:
            r11 = r4
            goto L_0x001a
        L_0x0011:
            int r4 = r1 + -1
            r4 = r0[r4]
            int r0 = (r11 > r4 ? 1 : (r11 == r4 ? 0 : -1))
            if (r0 < 0) goto L_0x001a
            goto L_0x000f
        L_0x001a:
            r0 = r3
        L_0x001b:
            int r4 = r1 + -1
            if (r0 >= r4) goto L_0x0041
            double[] r4 = r10.f7mT
            int r5 = r0 + 1
            r6 = r4[r5]
            int r8 = (r11 > r6 ? 1 : (r11 == r6 ? 0 : -1))
            if (r8 > 0) goto L_0x003f
            r11 = r4[r0]
            double r6 = r6 - r11
        L_0x002c:
            if (r3 >= r2) goto L_0x0041
            double[][] r11 = r10.f8mY
            r12 = r11[r0]
            r8 = r12[r3]
            r11 = r11[r5]
            r11 = r11[r3]
            double r11 = r11 - r8
            double r11 = r11 / r6
            r13[r3] = r11
            int r3 = r3 + 1
            goto L_0x002c
        L_0x003f:
            r0 = r5
            goto L_0x001b
        L_0x0041:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.utils.LinearCurveFit.getSlope(double, double[]):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0012, code lost:
        if (r8 >= r3) goto L_0x000a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public double getSlope(double r8, int r10) {
        /*
            r7 = this;
            double[] r0 = r7.f7mT
            int r1 = r0.length
            r2 = 0
            r3 = r0[r2]
            int r5 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r5 >= 0) goto L_0x000c
        L_0x000a:
            r8 = r3
            goto L_0x0015
        L_0x000c:
            int r3 = r1 + -1
            r3 = r0[r3]
            int r0 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r0 < 0) goto L_0x0015
            goto L_0x000a
        L_0x0015:
            int r0 = r1 + -1
            if (r2 >= r0) goto L_0x0035
            double[] r0 = r7.f7mT
            int r3 = r2 + 1
            r4 = r0[r3]
            int r6 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r6 > 0) goto L_0x0033
            r8 = r0[r2]
            double r4 = r4 - r8
            double[][] r7 = r7.f8mY
            r8 = r7[r2]
            r8 = r8[r10]
            r7 = r7[r3]
            r0 = r7[r10]
            double r0 = r0 - r8
            double r0 = r0 / r4
            return r0
        L_0x0033:
            r2 = r3
            goto L_0x0015
        L_0x0035:
            r7 = 0
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.utils.LinearCurveFit.getSlope(double, int):double");
    }

    public double[] getTimePoints() {
        return this.f7mT;
    }
}

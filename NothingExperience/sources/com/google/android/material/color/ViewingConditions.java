package com.google.android.material.color;

final class ViewingConditions {
    public static final ViewingConditions DEFAULT = make(ColorUtils.whitePointD65(), (float) ((((double) ColorUtils.yFromLstar(50.0f)) * 63.66197723675813d) / 100.0d), 50.0f, 2.0f, false);

    /* renamed from: aw */
    private final float f119aw;

    /* renamed from: c */
    private final float f120c;

    /* renamed from: fl */
    private final float f121fl;
    private final float flRoot;

    /* renamed from: n */
    private final float f122n;
    private final float nbb;

    /* renamed from: nc */
    private final float f123nc;
    private final float ncb;
    private final float[] rgbD;

    /* renamed from: z */
    private final float f124z;

    public float getAw() {
        return this.f119aw;
    }

    public float getN() {
        return this.f122n;
    }

    public float getNbb() {
        return this.nbb;
    }

    /* access modifiers changed from: package-private */
    public float getNcb() {
        return this.ncb;
    }

    /* access modifiers changed from: package-private */
    public float getC() {
        return this.f120c;
    }

    /* access modifiers changed from: package-private */
    public float getNc() {
        return this.f123nc;
    }

    public float[] getRgbD() {
        return this.rgbD;
    }

    /* access modifiers changed from: package-private */
    public float getFl() {
        return this.f121fl;
    }

    public float getFlRoot() {
        return this.flRoot;
    }

    /* access modifiers changed from: package-private */
    public float getZ() {
        return this.f124z;
    }

    static ViewingConditions make(float[] fArr, float f, float f2, float f3, boolean z) {
        float f4;
        float f5;
        float f6 = f;
        float[][] fArr2 = Cam16.XYZ_TO_CAM16RGB;
        float f7 = (fArr[0] * fArr2[0][0]) + (fArr[1] * fArr2[0][1]) + (fArr[2] * fArr2[0][2]);
        float f8 = (fArr[0] * fArr2[1][0]) + (fArr[1] * fArr2[1][1]) + (fArr[2] * fArr2[1][2]);
        float f9 = (fArr[0] * fArr2[2][0]) + (fArr[1] * fArr2[2][1]) + (fArr[2] * fArr2[2][2]);
        float f10 = (f3 / 10.0f) + 0.8f;
        if (((double) f10) >= 0.9d) {
            f4 = MathUtils.lerp(0.59f, 0.69f, (f10 - 0.9f) * 10.0f);
        } else {
            f4 = MathUtils.lerp(0.525f, 0.59f, (f10 - 0.8f) * 10.0f);
        }
        float f11 = f4;
        if (z) {
            f5 = 1.0f;
        } else {
            f5 = (1.0f - (((float) Math.exp((double) (((-f6) - 42.0f) / 92.0f))) * 0.2777778f)) * f10;
        }
        double d = (double) f5;
        if (d > 1.0d) {
            f5 = 1.0f;
        } else if (d < 0.0d) {
            f5 = 0.0f;
        }
        float[] fArr3 = {(((100.0f / f7) * f5) + 1.0f) - f5, (((100.0f / f8) * f5) + 1.0f) - f5, (((100.0f / f9) * f5) + 1.0f) - f5};
        float f12 = 1.0f / ((5.0f * f6) + 1.0f);
        float f13 = f12 * f12 * f12 * f12;
        float f14 = 1.0f - f13;
        float cbrt = (f13 * f6) + (0.1f * f14 * f14 * ((float) Math.cbrt(((double) f6) * 5.0d)));
        float yFromLstar = ColorUtils.yFromLstar(f2) / fArr[1];
        double d2 = (double) yFromLstar;
        float sqrt = ((float) Math.sqrt(d2)) + 1.48f;
        float pow = 0.725f / ((float) Math.pow(d2, 0.2d));
        float[] fArr4 = {(float) Math.pow(((double) ((fArr3[0] * cbrt) * f7)) / 100.0d, 0.42d), (float) Math.pow(((double) ((fArr3[1] * cbrt) * f8)) / 100.0d, 0.42d), (float) Math.pow(((double) ((fArr3[2] * cbrt) * f9)) / 100.0d, 0.42d)};
        float[] fArr5 = {(fArr4[0] * 400.0f) / (fArr4[0] + 27.13f), (fArr4[1] * 400.0f) / (fArr4[1] + 27.13f), (fArr4[2] * 400.0f) / (fArr4[2] + 27.13f)};
        return new ViewingConditions(yFromLstar, ((fArr5[0] * 2.0f) + fArr5[1] + (fArr5[2] * 0.05f)) * pow, pow, pow, f11, f10, fArr3, cbrt, (float) Math.pow((double) cbrt, 0.25d), sqrt);
    }

    private ViewingConditions(float f, float f2, float f3, float f4, float f5, float f6, float[] fArr, float f7, float f8, float f9) {
        this.f122n = f;
        this.f119aw = f2;
        this.nbb = f3;
        this.ncb = f4;
        this.f120c = f5;
        this.f123nc = f6;
        this.rgbD = fArr;
        this.f121fl = f7;
        this.flRoot = f8;
        this.f124z = f9;
    }
}

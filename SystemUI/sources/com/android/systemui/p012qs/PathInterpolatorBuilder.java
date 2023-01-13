package com.android.systemui.p012qs;

import android.graphics.Path;
import android.view.animation.BaseInterpolator;
import android.view.animation.Interpolator;

/* renamed from: com.android.systemui.qs.PathInterpolatorBuilder */
public class PathInterpolatorBuilder {
    private static final float PRECISION = 0.002f;
    private float[] mDist;

    /* renamed from: mX */
    private float[] f326mX;

    /* renamed from: mY */
    private float[] f327mY;

    public PathInterpolatorBuilder(Path path) {
        initPath(path);
    }

    public PathInterpolatorBuilder(float f, float f2) {
        initQuad(f, f2);
    }

    public PathInterpolatorBuilder(float f, float f2, float f3, float f4) {
        initCubic(f, f2, f3, f4);
    }

    private void initQuad(float f, float f2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(f, f2, 1.0f, 1.0f);
        initPath(path);
    }

    private void initCubic(float f, float f2, float f3, float f4) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(f, f2, f3, f4, 1.0f, 1.0f);
        initPath(path);
    }

    private void initPath(Path path) {
        float[] approximate = path.approximate(0.002f);
        int length = approximate.length / 3;
        float f = 0.0f;
        if (approximate[1] == 0.0f && approximate[2] == 0.0f && approximate[approximate.length - 2] == 1.0f && approximate[approximate.length - 1] == 1.0f) {
            this.f326mX = new float[length];
            this.f327mY = new float[length];
            this.mDist = new float[length];
            int i = 0;
            int i2 = 0;
            float f2 = 0.0f;
            while (i < length) {
                int i3 = i2 + 1;
                float f3 = approximate[i2];
                int i4 = i3 + 1;
                float f4 = approximate[i3];
                int i5 = i4 + 1;
                float f5 = approximate[i4];
                if (f3 == f && f4 != f2) {
                    throw new IllegalArgumentException("The Path cannot have discontinuity in the X axis.");
                } else if (f4 >= f2) {
                    float[] fArr = this.f326mX;
                    fArr[i] = f4;
                    float[] fArr2 = this.f327mY;
                    fArr2[i] = f5;
                    if (i > 0) {
                        int i6 = i - 1;
                        float f6 = fArr[i] - fArr[i6];
                        float f7 = f5 - fArr2[i6];
                        float[] fArr3 = this.mDist;
                        fArr3[i] = fArr3[i6] + ((float) Math.sqrt((double) ((f6 * f6) + (f7 * f7))));
                    }
                    i++;
                    f = f3;
                    f2 = f4;
                    i2 = i5;
                } else {
                    throw new IllegalArgumentException("The Path cannot loop back on itself.");
                }
            }
            float[] fArr4 = this.mDist;
            float f8 = fArr4[fArr4.length - 1];
            for (int i7 = 0; i7 < length; i7++) {
                float[] fArr5 = this.mDist;
                fArr5[i7] = fArr5[i7] / f8;
            }
            return;
        }
        throw new IllegalArgumentException("The Path must start at (0,0) and end at (1,1)");
    }

    public Interpolator getXInterpolator() {
        return new PathInterpolator(this.mDist, this.f326mX);
    }

    public Interpolator getYInterpolator() {
        return new PathInterpolator(this.mDist, this.f327mY);
    }

    /* renamed from: com.android.systemui.qs.PathInterpolatorBuilder$PathInterpolator */
    private static class PathInterpolator extends BaseInterpolator {

        /* renamed from: mX */
        private final float[] f328mX;

        /* renamed from: mY */
        private final float[] f329mY;

        private PathInterpolator(float[] fArr, float[] fArr2) {
            this.f328mX = fArr;
            this.f329mY = fArr2;
        }

        public float getInterpolation(float f) {
            if (f <= 0.0f) {
                return 0.0f;
            }
            if (f >= 1.0f) {
                return 1.0f;
            }
            int length = this.f328mX.length - 1;
            int i = 0;
            while (length - i > 1) {
                int i2 = (i + length) / 2;
                if (f < this.f328mX[i2]) {
                    length = i2;
                } else {
                    i = i2;
                }
            }
            float[] fArr = this.f328mX;
            float f2 = fArr[length];
            float f3 = fArr[i];
            float f4 = f2 - f3;
            if (f4 == 0.0f) {
                return this.f329mY[i];
            }
            float[] fArr2 = this.f329mY;
            float f5 = fArr2[i];
            return f5 + (((f - f3) / f4) * (fArr2[length] - f5));
        }
    }
}

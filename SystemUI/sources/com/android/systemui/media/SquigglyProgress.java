package com.android.systemui.media;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.MathUtils;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.animation.Interpolators;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-H\u0016J\b\u0010.\u001a\u00020/H\u0016J\b\u00100\u001a\u00020/H\u0016J\u0010\u00101\u001a\u00020\u00042\u0006\u00102\u001a\u00020/H\u0014J\u0010\u00103\u001a\u00020+2\u0006\u00104\u001a\u00020/H\u0016J\u0012\u00105\u001a\u00020+2\b\u00106\u001a\u0004\u0018\u000107H\u0016J\u0010\u00108\u001a\u00020+2\u0006\u00109\u001a\u00020/H\u0016J\u0012\u0010:\u001a\u00020+2\b\u0010;\u001a\u0004\u0018\u00010<H\u0016J\u0018\u0010=\u001a\u00020+2\u0006\u00109\u001a\u00020/2\u0006\u00104\u001a\u00020/H\u0002R$\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\rXD¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\rXD¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u001c\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0012\"\u0004\b\u001e\u0010\u0014R$\u0010\u001f\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0012\"\u0004\b!\u0010\u0014R$\u0010\"\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0007\"\u0004\b$\u0010\tR\u000e\u0010%\u001a\u00020\rXD¢\u0006\u0002\n\u0000R\u001a\u0010&\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u0012\"\u0004\b(\u0010\u0014R\u000e\u0010)\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000¨\u0006>"}, mo64987d2 = {"Lcom/android/systemui/media/SquigglyProgress;", "Landroid/graphics/drawable/Drawable;", "()V", "value", "", "animate", "getAnimate", "()Z", "setAnimate", "(Z)V", "heightAnimator", "Landroid/animation/ValueAnimator;", "heightFraction", "", "lastFrameTime", "", "lineAmplitude", "getLineAmplitude", "()F", "setLineAmplitude", "(F)V", "linePaint", "Landroid/graphics/Paint;", "matchedWaveEndpoint", "minWaveEndpoint", "path", "Landroid/graphics/Path;", "phaseOffset", "phaseSpeed", "getPhaseSpeed", "setPhaseSpeed", "strokeWidth", "getStrokeWidth", "setStrokeWidth", "transitionEnabled", "getTransitionEnabled", "setTransitionEnabled", "transitionPeriods", "waveLength", "getWaveLength", "setWaveLength", "wavePaint", "draw", "", "canvas", "Landroid/graphics/Canvas;", "getAlpha", "", "getOpacity", "onLevelChange", "level", "setAlpha", "alpha", "setColorFilter", "colorFilter", "Landroid/graphics/ColorFilter;", "setTint", "tintColor", "setTintList", "tint", "Landroid/content/res/ColorStateList;", "updateColors", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SquigglyProgress.kt */
public final class SquigglyProgress extends Drawable {
    private boolean animate;
    /* access modifiers changed from: private */
    public ValueAnimator heightAnimator;
    /* access modifiers changed from: private */
    public float heightFraction;
    private long lastFrameTime = -1;
    private float lineAmplitude;
    private final Paint linePaint;
    private final float matchedWaveEndpoint = 0.6f;
    private final float minWaveEndpoint = 0.2f;
    private final Path path = new Path();
    private float phaseOffset;
    private float phaseSpeed;
    private float strokeWidth;
    private boolean transitionEnabled = true;
    private final float transitionPeriods = 1.5f;
    private float waveLength;
    private final Paint wavePaint;

    public int getOpacity() {
        return -3;
    }

    public SquigglyProgress() {
        Paint paint = new Paint();
        this.wavePaint = paint;
        Paint paint2 = new Paint();
        this.linePaint = paint2;
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.STROKE);
        paint2.setAlpha(77);
    }

    public final float getWaveLength() {
        return this.waveLength;
    }

    public final void setWaveLength(float f) {
        this.waveLength = f;
    }

    public final float getLineAmplitude() {
        return this.lineAmplitude;
    }

    public final void setLineAmplitude(float f) {
        this.lineAmplitude = f;
    }

    public final float getPhaseSpeed() {
        return this.phaseSpeed;
    }

    public final void setPhaseSpeed(float f) {
        this.phaseSpeed = f;
    }

    public final float getStrokeWidth() {
        return this.strokeWidth;
    }

    public final void setStrokeWidth(float f) {
        if (!(this.strokeWidth == f)) {
            this.strokeWidth = f;
            this.wavePaint.setStrokeWidth(f);
            this.linePaint.setStrokeWidth(f);
        }
    }

    public final boolean getTransitionEnabled() {
        return this.transitionEnabled;
    }

    public final void setTransitionEnabled(boolean z) {
        this.transitionEnabled = z;
        invalidateSelf();
    }

    public final boolean getAnimate() {
        return this.animate;
    }

    public final void setAnimate(boolean z) {
        if (this.animate != z) {
            this.animate = z;
            if (z) {
                this.lastFrameTime = SystemClock.uptimeMillis();
            }
            ValueAnimator valueAnimator = this.heightAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.heightFraction;
            fArr[1] = this.animate ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            if (this.animate) {
                ofFloat.setStartDelay(60);
                ofFloat.setDuration(800);
                ofFloat.setInterpolator(Interpolators.EMPHASIZED_DECELERATE);
            } else {
                ofFloat.setDuration(550);
                ofFloat.setInterpolator(Interpolators.STANDARD_DECELERATE);
            }
            ofFloat.addUpdateListener(new SquigglyProgress$$ExternalSyntheticLambda0(this));
            ofFloat.addListener(new SquigglyProgress$animate$1$2(this));
            ofFloat.start();
            this.heightAnimator = ofFloat;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: _set_animate_$lambda-1$lambda-0  reason: not valid java name */
    public static final void m2841_set_animate_$lambda1$lambda0(SquigglyProgress squigglyProgress, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(squigglyProgress, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            squigglyProgress.heightFraction = ((Float) animatedValue).floatValue();
            squigglyProgress.invalidateSelf();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public void draw(Canvas canvas) {
        Canvas canvas2 = canvas;
        Intrinsics.checkNotNullParameter(canvas2, "canvas");
        if (this.animate) {
            invalidateSelf();
            long uptimeMillis = SystemClock.uptimeMillis();
            this.phaseOffset = (this.phaseOffset + ((((float) (uptimeMillis - this.lastFrameTime)) / 1000.0f) * this.phaseSpeed)) % this.waveLength;
            this.lastFrameTime = uptimeMillis;
        }
        float level = ((float) getLevel()) / 10000.0f;
        float width = ((float) getBounds().width()) * level;
        float width2 = (float) getBounds().width();
        if (this.transitionEnabled) {
            float f = this.matchedWaveEndpoint;
            if (level <= f) {
                level = MathUtils.lerp(this.minWaveEndpoint, f, MathUtils.lerpInv(0.0f, f, level));
            }
        }
        float f2 = width2 * level;
        float f3 = -this.phaseOffset;
        Function2 squigglyProgress$draw$computeAmplitude$1 = new SquigglyProgress$draw$computeAmplitude$1(this, f2, this.transitionEnabled ? this.transitionPeriods * this.waveLength : 0.01f);
        float f4 = (float) 2;
        float f5 = this.phaseOffset < this.waveLength / f4 ? 1.0f : -1.0f;
        this.path.rewind();
        this.path.moveTo((float) getBounds().width(), 0.0f);
        this.path.lineTo(f2, 0.0f);
        float f6 = f2 - (this.phaseOffset % (this.waveLength / f4));
        float floatValue = ((Number) squigglyProgress$draw$computeAmplitude$1.invoke(Float.valueOf(f6), Float.valueOf(f5))).floatValue();
        this.path.cubicTo(f2, floatValue * 0.25f, MathUtils.lerp(f6, f2, 0.25f), floatValue, f6, floatValue);
        float f7 = (((float) -1) * this.waveLength) / 2.0f;
        float f8 = floatValue;
        float f9 = f6;
        while (f9 > f3) {
            f5 = -f5;
            float f10 = f9 + f7;
            float f11 = f9 + (f7 / f4);
            float floatValue2 = ((Number) squigglyProgress$draw$computeAmplitude$1.invoke(Float.valueOf(f10), Float.valueOf(f5))).floatValue();
            this.path.cubicTo(f11, f8, f11, floatValue2, f10, floatValue2);
            f9 = f10;
            f8 = floatValue2;
        }
        canvas.save();
        canvas2.translate((float) getBounds().left, (float) getBounds().centerY());
        float f12 = this.lineAmplitude;
        float f13 = this.strokeWidth;
        canvas2.clipRect(0.0f, (-f12) - f13, width, f12 + f13);
        canvas2.drawPath(this.path, this.wavePaint);
        canvas.restore();
        canvas.save();
        canvas2.translate((float) getBounds().left, (float) getBounds().centerY());
        canvas2.clipRect(width, (-this.lineAmplitude) - this.strokeWidth, (float) getBounds().width(), this.lineAmplitude + this.strokeWidth);
        canvas2.drawPath(this.path, this.linePaint);
        canvas.restore();
        canvas2.drawPoint((float) getBounds().left, ((float) getBounds().centerY()) + (((float) Math.cos((double) ((Math.abs(f2 - this.phaseOffset) / this.waveLength) * 6.2831855f))) * this.lineAmplitude * this.heightFraction), this.wavePaint);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.wavePaint.setColorFilter(colorFilter);
        this.linePaint.setColorFilter(colorFilter);
    }

    public void setAlpha(int i) {
        updateColors(this.wavePaint.getColor(), i);
    }

    public int getAlpha() {
        return this.wavePaint.getAlpha();
    }

    public void setTint(int i) {
        updateColors(i, getAlpha());
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int i) {
        return this.animate;
    }

    public void setTintList(ColorStateList colorStateList) {
        if (colorStateList != null) {
            updateColors(colorStateList.getDefaultColor(), getAlpha());
        }
    }

    private final void updateColors(int i, int i2) {
        this.wavePaint.setColor(ColorUtils.setAlphaComponent(i, i2));
        this.linePaint.setColor(ColorUtils.setAlphaComponent(i, (int) (((float) 77) * (((float) i2) / 255.0f))));
    }
}

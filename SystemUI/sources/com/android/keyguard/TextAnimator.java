package com.android.keyguard;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.SparseArray;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u000e\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u0019J\u0006\u0010\u001a\u001a\u00020\u001bJa\u0010\u001c\u001a\u00020\u00062\b\b\u0002\u0010\u001d\u001a\u00020\u001e2\b\b\u0002\u0010\u001f\u001a\u00020 2\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\u001e2\b\b\u0002\u0010\"\u001a\u00020\u001b2\b\b\u0002\u0010#\u001a\u00020$2\n\b\u0002\u0010%\u001a\u0004\u0018\u00010&2\b\b\u0002\u0010'\u001a\u00020$2\n\b\u0002\u0010(\u001a\u0004\u0018\u00010)¢\u0006\u0002\u0010*J\u000e\u0010+\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0003R\u001a\u0010\b\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u0016\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u0015X\u0004¢\u0006\u0002\n\u0000¨\u0006,"}, mo64987d2 = {"Lcom/android/keyguard/TextAnimator;", "", "layout", "Landroid/text/Layout;", "invalidateCallback", "Lkotlin/Function0;", "", "(Landroid/text/Layout;Lkotlin/jvm/functions/Function0;)V", "animator", "Landroid/animation/ValueAnimator;", "getAnimator$SystemUI_nothingRelease", "()Landroid/animation/ValueAnimator;", "setAnimator$SystemUI_nothingRelease", "(Landroid/animation/ValueAnimator;)V", "textInterpolator", "Lcom/android/keyguard/TextInterpolator;", "getTextInterpolator$SystemUI_nothingRelease", "()Lcom/android/keyguard/TextInterpolator;", "setTextInterpolator$SystemUI_nothingRelease", "(Lcom/android/keyguard/TextInterpolator;)V", "typefaceCache", "Landroid/util/SparseArray;", "Landroid/graphics/Typeface;", "draw", "c", "Landroid/graphics/Canvas;", "isRunning", "", "setTextStyle", "weight", "", "textSize", "", "color", "animate", "duration", "", "interpolator", "Landroid/animation/TimeInterpolator;", "delay", "onAnimationEnd", "Ljava/lang/Runnable;", "(IFLjava/lang/Integer;ZJLandroid/animation/TimeInterpolator;JLjava/lang/Runnable;)V", "updateLayout", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TextAnimator.kt */
public final class TextAnimator {
    private ValueAnimator animator;
    private final Function0<Unit> invalidateCallback;
    private TextInterpolator textInterpolator;
    private final SparseArray<Typeface> typefaceCache = new SparseArray<>();

    public TextAnimator(Layout layout, Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(layout, "layout");
        Intrinsics.checkNotNullParameter(function0, "invalidateCallback");
        this.invalidateCallback = function0;
        this.textInterpolator = new TextInterpolator(layout);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f});
        ofFloat.setDuration(300);
        ofFloat.addUpdateListener(new TextAnimator$$ExternalSyntheticLambda0(this));
        ofFloat.addListener(new TextAnimator$animator$1$2(this));
        Intrinsics.checkNotNullExpressionValue(ofFloat, "ofFloat(1f).apply {\n    …rebase()\n        })\n    }");
        this.animator = ofFloat;
    }

    public final TextInterpolator getTextInterpolator$SystemUI_nothingRelease() {
        return this.textInterpolator;
    }

    public final void setTextInterpolator$SystemUI_nothingRelease(TextInterpolator textInterpolator2) {
        Intrinsics.checkNotNullParameter(textInterpolator2, "<set-?>");
        this.textInterpolator = textInterpolator2;
    }

    public final ValueAnimator getAnimator$SystemUI_nothingRelease() {
        return this.animator;
    }

    public final void setAnimator$SystemUI_nothingRelease(ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(valueAnimator, "<set-?>");
        this.animator = valueAnimator;
    }

    /* access modifiers changed from: private */
    /* renamed from: animator$lambda-1$lambda-0  reason: not valid java name */
    public static final void m2311animator$lambda1$lambda0(TextAnimator textAnimator, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(textAnimator, "this$0");
        TextInterpolator textInterpolator2 = textAnimator.textInterpolator;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            textInterpolator2.setProgress(((Float) animatedValue).floatValue());
            textAnimator.invalidateCallback.invoke();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public final void updateLayout(Layout layout) {
        Intrinsics.checkNotNullParameter(layout, "layout");
        this.textInterpolator.setLayout(layout);
    }

    public final boolean isRunning() {
        return this.animator.isRunning();
    }

    public final void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "c");
        this.textInterpolator.draw(canvas);
    }

    public static /* synthetic */ void setTextStyle$default(TextAnimator textAnimator, int i, float f, Integer num, boolean z, long j, TimeInterpolator timeInterpolator, long j2, Runnable runnable, int i2, Object obj) {
        int i3 = i2;
        int i4 = (i3 & 1) != 0 ? -1 : i;
        float f2 = (i3 & 2) != 0 ? -1.0f : f;
        Runnable runnable2 = null;
        Integer num2 = (i3 & 4) != 0 ? null : num;
        boolean z2 = (i3 & 8) != 0 ? true : z;
        long j3 = (i3 & 16) != 0 ? -1 : j;
        TimeInterpolator timeInterpolator2 = (i3 & 32) != 0 ? null : timeInterpolator;
        long j4 = (i3 & 64) != 0 ? 0 : j2;
        if ((i3 & 128) == 0) {
            runnable2 = runnable;
        }
        textAnimator.setTextStyle(i4, f2, num2, z2, j3, timeInterpolator2, j4, runnable2);
    }

    public final void setTextStyle(int i, float f, Integer num, boolean z, long j, TimeInterpolator timeInterpolator, long j2, Runnable runnable) {
        if (z) {
            this.animator.cancel();
            this.textInterpolator.rebase();
        }
        if (f >= 0.0f) {
            this.textInterpolator.getTargetPaint().setTextSize(f);
        }
        if (i >= 0) {
            this.textInterpolator.getTargetPaint().setTypeface((Typeface) TextAnimatorKt.getOrElse(this.typefaceCache, i, new TextAnimator$setTextStyle$1(this, i)));
        }
        if (num != null) {
            this.textInterpolator.getTargetPaint().setColor(num.intValue());
        }
        this.textInterpolator.onTargetPaintModified();
        if (z) {
            this.animator.setStartDelay(j2);
            ValueAnimator valueAnimator = this.animator;
            if (j == -1) {
                j = 300;
            }
            valueAnimator.setDuration(j);
            if (timeInterpolator != null) {
                this.animator.setInterpolator(timeInterpolator);
            }
            if (runnable != null) {
                this.animator.addListener(new TextAnimator$setTextStyle$listener$1(runnable, this));
            }
            this.animator.start();
            return;
        }
        this.textInterpolator.setProgress(1.0f);
        this.textInterpolator.rebase();
        this.invalidateCallback.invoke();
    }
}

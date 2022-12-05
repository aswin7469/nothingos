package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.util.SparseArray;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: TextAnimator.kt */
/* loaded from: classes.dex */
public final class TextAnimator {
    @NotNull
    private ValueAnimator animator;
    @NotNull
    private final Function0<Unit> invalidateCallback;
    @NotNull
    private TextInterpolator textInterpolator;
    @NotNull
    private final SparseArray<Typeface> typefaceCache = new SparseArray<>();

    public TextAnimator(@NotNull Layout layout, @NotNull Function0<Unit> invalidateCallback) {
        Intrinsics.checkNotNullParameter(layout, "layout");
        Intrinsics.checkNotNullParameter(invalidateCallback, "invalidateCallback");
        this.invalidateCallback = invalidateCallback;
        this.textInterpolator = new TextInterpolator(layout);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f);
        ofFloat.setDuration(300L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.TextAnimator$animator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                Function0 function0;
                TextInterpolator textInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core = TextAnimator.this.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                textInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core.setProgress(((Float) animatedValue).floatValue());
                function0 = TextAnimator.this.invalidateCallback;
                function0.mo1951invoke();
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.TextAnimator$animator$1$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator) {
                TextAnimator.this.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().rebase();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(@Nullable Animator animator) {
                TextAnimator.this.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().rebase();
            }
        });
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(ofFloat, "ofFloat(1f).apply {\n        duration = DEFAULT_ANIMATION_DURATION\n        addUpdateListener {\n            textInterpolator.progress = it.animatedValue as Float\n            invalidateCallback()\n        }\n        addListener(object : AnimatorListenerAdapter() {\n            override fun onAnimationEnd(animation: Animator?) {\n                textInterpolator.rebase()\n            }\n            override fun onAnimationCancel(animation: Animator?) = textInterpolator.rebase()\n        })\n    }");
        this.animator = ofFloat;
    }

    @NotNull
    public final TextInterpolator getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.textInterpolator;
    }

    @NotNull
    public final ValueAnimator getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.animator;
    }

    public final void updateLayout(@NotNull Layout layout) {
        Intrinsics.checkNotNullParameter(layout, "layout");
        this.textInterpolator.setLayout(layout);
    }

    public final boolean isRunning() {
        return this.animator.isRunning();
    }

    public final void draw(@NotNull Canvas c) {
        Intrinsics.checkNotNullParameter(c, "c");
        this.textInterpolator.draw(c);
    }

    public final void setTextStyle(int i, float f, @Nullable Integer num, boolean z, long j, @Nullable TimeInterpolator timeInterpolator, long j2, @Nullable final Runnable runnable) {
        Object orElse;
        if (z) {
            this.animator.cancel();
            this.textInterpolator.rebase();
        }
        if (f >= 0.0f) {
            this.textInterpolator.getTargetPaint().setTextSize(f);
        }
        if (i >= 0) {
            TextPaint targetPaint = this.textInterpolator.getTargetPaint();
            orElse = TextAnimatorKt.getOrElse(this.typefaceCache, i, new TextAnimator$setTextStyle$1(this, i));
            targetPaint.setTypeface((Typeface) orElse);
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
                getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().setInterpolator(timeInterpolator);
            }
            if (runnable != null) {
                this.animator.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.TextAnimator$setTextStyle$listener$1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(@Nullable Animator animator) {
                        runnable.run();
                        this.getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().removeListener(this);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(@Nullable Animator animator) {
                        this.getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().removeListener(this);
                    }
                });
            }
            this.animator.start();
            return;
        }
        this.textInterpolator.setProgress(1.0f);
        this.textInterpolator.rebase();
    }
}
